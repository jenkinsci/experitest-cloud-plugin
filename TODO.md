General
Many of our users are using XCUITest (iOS) and Espresso (Android) as there automation framework.
The build pipeline include building of both there application APK / IPA and the tests APK / IPA.
Once the 2 applications are ready the next step would be to execute the tests on our cloud environment.
The described jenkins step will enable the user to easily do that.

Rest API
Host:
Provided by the user in creation of the cloud connection.
Path:
/api/v1/test-run/execute-test-run
Method:
POST
Autentication

All the Jenkins UI configuration will result in a rest API call. Following is the Rest API information:

Parameter Name
Options
Type
UI
Description
executionType
xcuitest/ espresso
mandatory
Radio button, no default selection
Select between iOS and Android
runningType
coverage/ fastFeedback
mandatory
Radio button ‘Fast Feedback’ should be default
Select between 2 execution type that will set the test execution distribution.
app
Link to file
mandatory
Text field that will enable the user to select a file from the artifacts
The application under test
testApp
Link to file
mandatory
Text field that will enable the user to select a file from the artifacts
The test application
deviceQueries
String


Text field with a + sign to add additional field.
In fast feedback the + should be disable as only one field is possible.
Fast feedback, xcuitest: @os=’ios’
Fast feedback, espresso: @os=’android’
Coverage, xcuitest first field: @os=’ios’ and @majorVersion=12
Coverage, xcuitest second field: @os=’ios’ and @majorVersion=11
Coverage, xcuitest third field: @os=’ios’ and @majorVersion=10


A list of strings (in case of fastFeedback mode only single string will be available)
runTags
String
optional
Text field with a + sign. Key and Value should contain value (key should be unique).
Enable the user to add multiple key value pers.
maxDevices
number
optional
Number field: default should be 10, value should be 1 or greater
Relevant only for fast feedback
minDevices
number
optional
Number field: default and minimum should be 1, should not be higher than max
Relevant only for fast feedback
overallTimeout
number
optional
Number field: time in minutes
Overall execution timeout in minutes (should be converted to milliseconds in the api)
creationTimeout
number
optional
Number field: time in minutes
Timeout for devices allocations. timeout in minutes (should be converted to milliseconds in the api)
ignoreTestFile
Link to file
optional
Text field
iOS - xcscheme file
Android - text file with include full class name

Detailed description
When creating a new job as part of the ‘build steps’ the user will be able to select ‘Execute XCUITest or Espresso tests’.
Once step added all the UI will be presented to the user, based on the selection some field should be disabled. Other option will be to show next field only when the parent option is selected.
Generic documentation:
This step will enable you to execute your XCUITest or Espresso against devices in the cloud.
1.
Field name:
Framework Type
UI Element: Radio button
Options: XCUITest, Espresso
Default: non
Documentation:
The supported framework by this step are XCUITest (by Apple) and Espresso (by Google).

2.
Field name:
Running type
UI Element: Radio button
Options: Fast Feedback, Coverage
Default: Fast Feedback
Documentation:
Fast Feedback - will distribute the tests between the maximum pull of devices available. Every test will be execute once.
Coverage - will execute all the tests on all of the selected devices. Every test will be execute on every selected device.

3.
Field name:
Application under test
UI Element: Text Field
Default: non
Documentation:
The application to test, in case of XCUITest should be an IPA file and in case of Espresso should be an APK file.

4.
Field name:
Test Application
UI Element: Text Field
Default: non
Documentation:
The application contain the tests to execute, in case of XCUITest should be an IPA file and in case of Espresso should be an APK file.

5.
Field name:
Device Query
UI Element: 
Multiple text field with ‘+’ button to add and ‘x’ optio per field to delete. In case of fast feedback only single field should be possible to configure.
Default:
Fast feedback, xcuitest: @os=’ios’
Fast feedback, espresso: @os=’android’
Coverage, xcuitest first field: @os=’ios’ and @majorVersion=12
Coverage, xcuitest second field: @os=’ios’ and @majorVersion=11
Coverage, xcuitest third field: @os=’ios’ and @majorVersion=10
Documentation:
A query string that will enable to select the devices that will be used.
For example:
@serialnumber=’aabbccddee’
Will select a specific device with a given serial number of udid.
@os=’android’ and @versionnumber > 5.0
Select android with a version that is bigger than 5.0

Other field that can be used:
manufacture: device manufacture for example: samsung
model: Device model, for example: SM-G935F
modelName:  Device marketing name: for example: Galaxy S7 Edge
name
os: Operating system.
remote: If device is connected through SeeTestCloud or not (values are true/false).
serialnumber: S/N of the device.
version: The device's operating system version (for example 4.1.2).
versionnumber: The device's operating system version number (for example 4.1).
category: The device category. Currently supported in android devices. possible values- 'WATCH', 'PHONE' or 'TABLET'.


6.
Field name:
Tests Tags
UI Element: 
Multiple key value double text fields with ‘+’ button to add and ‘x’ option per key value fields pair to delete.
Default:
build.number=${BUILD_NUMBER}
Documentation:
All tests that are part of this step will be ‘tagged’ with the provided key value. The tags will enable to filter your specific execution.

7.
Field name:
Advance options
UI Element:
Button

When the user click on advance he will be able to view the next fields.

8.
Field name:
Max Devices
UI Element: Number Field
Default: 10
Accept values: 1-1000
Documentation:
Set the maximum number of devices to allocate for this step execution.

Only applicable for Fast feedback mode.

9.
Field name:
Min Devices
UI Element: Number Field
Default: 1
Accept values: 1-1000, should be smaller than the Max devices.
Documentation:
Set the minimum number of devices to allocate for this step execution. The step execution will wait until the required minimum amount of devices will be available.

Only applicable for Fast feedback mode.

10.
Field name:
Ignore tests file
UI Element:
Text field
Default:
Non
Documentation:
iOS - xcscheme file
Android - text file with include full class name:
<class name 1>,<method name 1>
<class name 2>,<method name 2>

11.
Field name:
Overall execution timeout
UI Element: Number Field
Default: 240 minutes
Documentation:
Set the step overall execution timeout. If the step execution time exceeded the given time, it will end with failure.

12.
Field name:
Session creation timeout
UI Element: Number Field
Default: 240 minutes
Documentation:
Set the maximum time to wait for all of the query devices to be available.
