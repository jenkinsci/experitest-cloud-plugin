package com.experitest.plugin;

import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.experitest.plugin.i18n.Messages;
import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.*;
import hudson.model.Item;
import hudson.security.ACL;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.jvnet.hudson.test.JenkinsRule;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

public class ExperitestCredentialsTest {

    private HtmlPage page;

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule();
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void before() throws IOException, SAXException {
        JenkinsRule.WebClient webClient = jenkinsRule.createWebClient();
        webClient.setAjaxController(new AjaxController() {
            public boolean processSynchron(HtmlPage page, WebRequest settings, boolean async) {
                return true;
            }
        });

        this.page = webClient.goTo("credentials/store/system/domain/_/newCredentials");
        HtmlSelect credSelect = (HtmlSelect) this.page.getElementsByTagName("select").get(0);
        List<?> creds = credSelect.getByXPath("//option[contains(.,'" + Messages.credentialsDisplayName() +"')]");
        Assert.assertEquals(1, creds.size());

        HtmlOption option = (HtmlOption) creds.get(0);
        option.setSelected(true);
    }

    @Test
    public void shouldSaveSucceed() throws IOException {
        HtmlTextInput apiUrlElem = this.page.getElementByName("_.apiUrl");
        apiUrlElem.setText("https://test.com");

        HtmlPasswordInput secretKey = this.page.getElementByName("_.secretKey");
        secretKey.setText("secretKey");

        HtmlButton okButton = (HtmlButton) this.page.getByXPath("//button[contains(.,'OK')]").get(0);
        okButton.click();

        List<ExperitestCredentials> credentials = CredentialsProvider.lookupCredentials(ExperitestCredentials.class, (Item) null, ACL.SYSTEM, null, null);
        Assert.assertEquals(1, credentials.size());
    }

    @Test
    public void shouldNotSaved() throws IOException {
        exceptionRule.expect(FailingHttpStatusCodeException.class);

        final HtmlButton okButton = (HtmlButton) this.page.getByXPath("//button[contains(.,'OK')]").get(0);
        try {
            okButton.click();
        } finally { // we need to check the creds when exception is thrown
            List<ExperitestCredentials> credentials = CredentialsProvider.lookupCredentials(ExperitestCredentials.class, (Item) null, ACL.SYSTEM, null, null);
            Assert.assertEquals(0, credentials.size());
        }
    }
}
