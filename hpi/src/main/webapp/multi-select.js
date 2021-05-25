/*!
 * IconicMultiSelect v0.3.1
 * Licence:  MIT
 * (c) 2021 Sidney Wimart.
 */

/**
 * @version IconicMultiSelect v0.3.1
 * @licence  MIT
 */
class IconicMultiSelect {
    domElements = {};
    event = () => {
    };
    noData;
    noResults;
    options = [];
    placeholder;
    prefix = "iconic" + Math.floor(1000 + Math.random() * 9000) + "-";
    selectName
    selectedOptions = []
    onlyOnce;

    /**
     * Iconic Multiselect constructor.
     * @param { string } noData - Defines the message when there is no data input.
     * @param { string } noResults - Defines the message when there is no result if options are filtered.
     * @param { string } placeholder -  Defines the placeholder's text.
     * @param { string } select - DOM element to be selected. It must be a HTML Select tag - https://developer.mozilla.org/en-US/docs/Web/HTML/Element/select
     */
    constructor({noData, noResults, placeholder, select}) {
        this.noData = noData ?? "No data found.";
        this.noResults = noResults ?? "No results found.";
        this.placeholder = placeholder ?? "Select...";
        this.selectName = select;
        this.onlyOnce = true;
        this.start(select);
    }

    start(select) {
        const self = this;
        let initInterval = setInterval(function () {
            let credentialsElement = document.getElementsByName("_.credentialsId")[0];
            const ele = document.getElementsByName(select)[0];
            let currentBrowserSelect;
            if (credentialsElement.selectedIndex !== undefined && ele !== undefined && ele.options.length > 0) {
                self.init();
                currentBrowserSelect = ele.options.length;

                credentialsElement.addEventListener("change", () => {
                    let onChangeInterval = setInterval(function () {
                        if (credentialsElement.selectedIndex !== undefined && ele !== undefined &&
                            currentBrowserSelect !== ele.options.length && ele.options.length > 0) {
                            currentBrowserSelect = ele.options.length;
                            self.reload();
                            clearInterval(onChangeInterval);
                        }
                    }, 500);
                });
                clearInterval(initInterval);
            }
        }, 500);
    }

    /**
     * Initialize the Iconic Multiselect component.
     * @public
     */
    init() {
        if (document.getElementsByName(this.selectName) &&
            document.getElementsByName(this.selectName)[0].nodeName === "SELECT") {
            this.options = this._getDataFromSelectTag();
            if (this.onlyOnce) {
                this._injectCss();
                this._renderMultiselect();
                this.onlyOnce = false;
            }
            this.initDom();
            this._enableEventListeners();
        } else {
            throw new Error(`The selector '${document.getElementsByName(this.selectName)}' did not select any valid select tag.`);
        }
    }

    /**
     * Subscribes to the emitted events.
     * @param { Function } callback - Callback function which emits a custom event object.
     * @public
     */
    subscribe(callback) {
        if (typeof callback === "function") {
            this.event = callback;
        } else {
            throw new Error(`parameter in the subscribe method is not a function`);
        }
    }

    reload() {
        if (this.domElements.clear !== undefined) {
            this.domElements.clear.click();
        }
        //remove old HTML
        this._removeRenderOptionsList();
        // init again only with the new options

        if (document.getElementsByName(this.selectName) &&
            document.getElementsByName(this.selectName)[0].nodeName === "SELECT") {
            this.options = this._getDataFromSelectTag();
            this.initDom();

            this.domElements.options.forEach((option) => {
                option.addEventListener("click", ({target}) => {
                    this._handleOption(target);
                });
            });

        } else {
            throw new Error(`The selector '${document.getElementsByName(this.selectName)}' did not select any valid select tag.`);
        }
    }

    initDom() {
        this._renderOptionsList();

        this.domElements = {
            clear: document.querySelector(`.${this.prefix + "multiselect__clear-btn"}`),
            input: document.querySelector(`.${this.prefix + "multiselect__input"}`),
            optionsContainer: document.querySelector(`.${this.prefix + "multiselect__options"}`),
            optionsContainerList: document.querySelector(`.${this.prefix + "multiselect__options > ul"}`),
            options: document.querySelectorAll(`.${this.prefix + "multiselect__options"} > ul > li`),
        };
    }

    /**
     * Add an option to the selection list.
     * @param option { Object: { text: string; value: string; }}
     * @private
     */
    _addOptionToList(option) {
        const html = `<span class="${this.prefix + "multiselect__selected"}" data-value="${option.value}">${
            option.text
        }<span class="${this.prefix + "multiselect__remove-btn"}">&#10006;</span></span>`;

        this.domElements.input.insertAdjacentHTML("beforebegin", html);

        const {firstElementChild: removeBtn} = document.querySelector(`span[data-value="${option.value}"]`);
        removeBtn.addEventListener("click", () => {
            const target = Array.from(this.domElements.options).find((el) => el.dataset.value === option.value);
            this._handleOption(target);
        });
    }

    /**
     * Clears all selected options.
     * @private
     */
    _clearSelection() {
        this.selectedOptions.forEach((el) => {
            const targetLastSelectedOption = Array.from(this.domElements.options).find((t) => t.dataset.value === el.value);
            this._handleOption(targetLastSelectedOption, false);
        });

        this._dispatchEvent({
            action: "CLEAR_ALL_OPTIONS",
            selection: this.selectedOptions,
        });
    }

    /**
     * Close the options container.
     * @private
     */
    _closeList() {
        this.domElements.input.value = "";
        this.domElements.optionsContainer.style.display = "none";
        this._filterOptions("");
        this._removeAllArrowSelected();
    }

    /**
     * Dispatches new events.
     * @param event "{ object : { action: string; selection: { option: string; text: string; }[]; value?: string; } }"
     * @private
     */
    _dispatchEvent(event) {
        this.event(event);
    }

    /**
     * Enables all main event listenners.
     * @private
     */
    _enableEventListeners() {
        this.domElements.clear.addEventListener("click", () => {
            this._clearSelection();
        });

        this.domElements.options.forEach((option) => {
            option.addEventListener("click", ({target}) => {
                this._handleOption(target);
            });
        });

        document.addEventListener("click", (e) => {
            const isClickInside = document.querySelector(`.${this.prefix + "multiselect__container"}`).contains(e.target);
            if (!isClickInside) {
                this._closeList();
            }
        });

        this.domElements.input.addEventListener("focus", () => {
            this.domElements.optionsContainer.style.display = "block";
        });

        this.domElements.input.addEventListener("input", ({target: {value}}) => {
            this._filterOptions(value);
        });

        this.domElements.input.addEventListener("keydown", (e) => {
            this._handleArrows(e);
            this._handleBackspace(e);
            this._handleEnter(e);
        });
    }

    /**
     * Filters user input.
     * @param { string } value
     * @private
     */
    _filterOptions(value) {
        if (this.domElements.optionsContainer.style.display !== "block" && value.length > 0) {
            this.domElements.optionsContainer.style.display = "block";
        }
        const valueLowerCase = value.toLowerCase();

        this.domElements.options.forEach((el) => {
            if (el.innerText.toLowerCase().include(valueLowerCase)) {
                this.domElements.optionsContainerList.append(el);
            } else if (this.domElements.optionsContainerList.contains(el)) {
                el.remove();
            }
        });
        this._showNoResults(this.domElements.optionsContainerList.children.length === 0);
    }

    /**
     * Gets data from select tag.
     * @private
     */
    _getDataFromSelectTag() {
        return Array.from(document.getElementsByName(this.selectName)[0].options).map((option) => ({
            text: option.text,
            value: option.value.replaceAll("\"", "'"),
        }));
    }

    /**
     * Handles Arrow up & Down. Selection of an option is also possible with these keys.
     * @param { Event } event
     * @private
     */
    _handleArrows(event) {
        if (event.keyCode === 40 || event.keyCode === 38) {
            const isOpen = this.domElements.optionsContainer.style.display === "block";
            // An updated view of the container is needed
            const optionsContainerList = document.querySelector(`.${this.prefix + "multiselect__options > ul"}`);

            if (!isOpen) {
                this.domElements.optionsContainer.style.display = "block";
                optionsContainerList.firstElementChild.classList.add("arrow-selected");
                optionsContainerList.firstElementChild.scrollIntoView();
            } else {
                let selected = document.querySelector(`.${this.prefix}multiselect__options ul li.arrow-selected`);
                const scrollIntoViewOption = {block: "nearest", inline: "nearest"};
                const action = {ArrowUp: "previous", Up: "previous", ArrowDown: "next", Down: "next"};

                if (!selected) {
                    optionsContainerList.firstElementChild.classList.add("arrow-selected");
                    optionsContainerList.firstElementChild.scrollIntoView();
                    return;
                }

                selected.classList.remove("arrow-selected");

                selected = selected[action[event.key] + "ElementSibling"];

                if (!selected) {
                    selected =
                        optionsContainerList.children[action[event.key] === "next" ? 0 : optionsContainerList.children.length - 1];
                    selected.classList.add("arrow-selected");
                    selected.scrollIntoView(scrollIntoViewOption);
                    return;
                }

                selected.classList.add("arrow-selected");
                selected.scrollIntoView(scrollIntoViewOption);
            }
        }
    }

    /**
     * Handles the backspace key event - Deletes the preceding option in the selection list.
     * @param { Event } e
     * @private
     */
    _handleBackspace(e) {
        if (e.keyCode === 8 && e.target.value === "") {
            const lastSelectedOption =
                this.selectedOptions.length > 0 ? this.selectedOptions[this.selectedOptions.length - 1] : null;

            if (lastSelectedOption) {
                const targetLastSelectedOption = document.querySelector(`li[data-value="${lastSelectedOption.value}"]`);
                this._handleOption(targetLastSelectedOption);
            }
        }
    }

    /**
     * Handles the enter key event.
     * @param { Event } event
     * @private
     */
    _handleEnter(event) {
        if (event.keyCode === 13) {
            const selected = document.querySelector(`.${this.prefix}multiselect__options ul li.arrow-selected`);
            if (selected) {
                this._handleOption(selected);
                this._closeList();
            }
        }
    }

    /**
     * Shows clear selection button if some options are selected.
     * @private
     */
    _handleClearSelectionBtn() {
        if (this.selectedOptions.length > 0) {
            this.domElements.clear.style.display = "block";
        } else {
            this.domElements.clear.style.display = "none";
        }
    }

    _handleOption(target, dispatchEvent = true) {

        const list = document.getElementsByName(this.selectName)[0];
        const option = this.options.find((el) => el.value === target.dataset.value);
        let isSelect;
        if (this.selectedOptions.some((el) => el.value === target.dataset.value)) {
            target.classList.remove(`${this.prefix}multiselect__options--selected`);
            this.selectedOptions = this.selectedOptions.filter((el) => el.value !== target.dataset.value);
            this._removeOptionFromList(target.dataset.value);
            dispatchEvent &&
            this._dispatchEvent({
                action: "REMOVE_OPTION",
                value: target.dataset.value,
                selection: this.selectedOptions,
            });
            isSelect = false;
        } else {
            target.classList.add(`${this.prefix}multiselect__options--selected`);
            this.selectedOptions = [...this.selectedOptions, option];
            this._addOptionToList(option);

            dispatchEvent &&
            this._dispatchEvent({
                action: "ADD_OPTION",
                value: target.dataset.value,
                selection: this.selectedOptions,
            });
            isSelect = true;
        }
        this._handleClearSelectionBtn();
        this._handlePlaceholder();
        for (let aOption of list.options) {
            if (option.text === aOption.text) {
                aOption.selected = isSelect;
            }
        }
    }

    /**
     * Shows the placeholder if no options are selected.
     * @private
     */
    _handlePlaceholder() {
        if (this.selectedOptions.length > 0) {
            this.domElements.input.placeholder = "";
        } else {
            this.domElements.input.placeholder = this.placeholder;
        }
    }

    _removeAllArrowSelected() {
        const className = "arrow-selected";
        this.domElements.options.forEach((el) => {
            if (el.classList.contains(className)) {
                el.classList.remove(className);
            }
        });
    }

    /**
     * Removes an option from the list.
     * @param { string } value
     * @private
     */
    _removeOptionFromList(value) {
        const optionDom = document.querySelector(`span[data-value="${value}"]`);
        optionDom.remove();
    }

    _removeRenderOptionsList() {
        document.getElementsByClassName(this.prefix + "multiselect__options")[0].remove()
    }

    /**
     * Renders the multiselect options list view.
     * @private
     */
    _renderOptionsList() {
        const html = `
        <div style="display: none;" class="${this.prefix}multiselect__options">
          <ul>
          ${
            this.options.length > 0
                ? this.options
                    .map((option) => {
                        return `
              <li data-value="${option.value}">${option.text}</li>
            `;
                    })
                    .join("")
                : ""
        }
          ${this._showNoData(this.options.length === 0)}
          </ul>
        </div>
      `;

        document.querySelector(`.${this.prefix + "multiselect__container"}`).insertAdjacentHTML("beforeend", html);
    }

    /**
     * Renders the multiselect view.
     * @private
     */
    _renderMultiselect() {
        document.getElementsByName(this.selectName)[0].style.display = "none";
        const html = `
      <div class="${this.prefix + "multiselect__container"}">
        <div class="${this.prefix + "multiselect__wrapper"}">
          <input class="${this.prefix + "multiselect__input"}" placeholder="${this.placeholder}" />
        </div>
        <span style="display: none;" class="${this.prefix + "multiselect__clear-btn"}">&#10006;</span>
      </div>
    `;

        document.getElementsByName(this.selectName)[0].insertAdjacentHTML("afterend", html);
    }

    /**
     * Shows a no data message.
     * @param { boolean } condition
     * @private
     */
    _showNoData(condition) {
        return condition ? `<p class="${this.prefix}multiselect__options--no-data">${this.noData}</p>` : "";
    }

    /**
     * Shows a no results message.
     * @param { boolean } condition
     * @private
     */
    _showNoResults(condition) {
        const dom = document.querySelector(`.${this.prefix}multiselect__options--no-results`);
        if (condition) {
            const html = `<p class="${this.prefix}multiselect__options--no-results">${this.noResults}</p>`;
            !dom && this.domElements.optionsContainerList.insertAdjacentHTML("beforeend", html);
        } else {
            dom && dom.remove();
        }
    }

    /**
     * Injects required CSS class properties in the <head></head>
     * @private
     */
    _injectCss() {
        const css = `
      <style>
        .${this.prefix}multiselect__container {
          align-items: center;
          background-color: #fff;
          border-radius: 2px;
          border: 1px solid rgba(0,0,0,.08);
          box-sizing: border-box;
          display: flex;
          font-family: Arial,Helvetica,sans-serif;
          min-height: 40px;
          padding: 4px 8px 0 8px;
          position: relative;
          width: 100%;
        }

        .${this.prefix}multiselect__container:after {
          content:'';
          min-height:inherit;
          font-size:0;
        }

        .${this.prefix}multiselect__container > * {
          color: #656565;
          font-size: 14px;
        }

        .${this.prefix + "multiselect__wrapper"} {
          display: flex;
          flex-wrap: wrap;
          height: 100%;
          width: 100%;
        }

        .${this.prefix}multiselect__clear-btn {
           cursor: pointer;
           margin-bottom: 4px;
           margin-left: 4px;
        }

        .${this.prefix}multiselect__options {
          background-color: #f6f6f6;
          border-radius: 2px;
          border: 1px solid rgba(0,0,0,.08);
          left: -1px;
          position: absolute;
          top: calc(100% + 2px);
          width: 100%;
          z-index: 2
        }

        .${this.prefix}multiselect__options ul {
          list-style: none;
          margin: 0;
          padding: 2px 0;
          max-height: 120px;
          overflow: auto;
        }

        .${this.prefix}multiselect__options ul li {
          cursor: pointer;
          padding: 4px 8px;
        }

        .${this.prefix}multiselect__options ul p.${this.prefix}multiselect__options--no-results,
        .${this.prefix}multiselect__options ul p.${this.prefix}multiselect__options--no-data {
          margin: 0;
          padding: 8px;
          text-align: center;
        }

        .${this.prefix}multiselect__options ul li.${this.prefix}multiselect__options--selected {
          background-color: #2783f8;
          color: #fff;
        }

        .${this.prefix}multiselect__options ul li.${this.prefix}multiselect__options--selected:hover {
          background-color: #1D64BF;
        }

        .${this.prefix}multiselect__options ul li:hover {
          background-color: #dedede;
        }

        .${this.prefix}multiselect__options ul li.arrow-selected {
          border: 2px solid rgb(101, 101, 101, 0.5);
        }

        .${this.prefix}multiselect__selected {
          background-color: #2f3b52;
          border-radius: 2px;
          color: #fff;
          margin-bottom: 4px;
          margin-right: 4px;
          padding: 4px 8px;
          display: flex;
          align-items: center;
        }

        .${this.prefix}multiselect__selected .${this.prefix}multiselect__remove-btn {
          cursor: pointer;
          margin-left: 6px;
        }

        .${this.prefix}multiselect__input {
          border: none;
          flex-basis: 40px;
          flex-grow: 1;
          margin-bottom: 4px;
          min-width: 40px;
          outline: none;
        }
      </style>
      `;

        document.querySelector("head").insertAdjacentHTML("beforeend", css);
    }
}
