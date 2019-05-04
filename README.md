# experitest-jenkins

### Requirements

Need to develop a Jenkins Plugin that does the following:

  1. Define account using URL and accessKey
  2. create a checkbox on Jenkins build configuration enabling the plugin
  3. If the checkBox marked, the user can provide a path on the jenkins machine to an application file
  4. when running the job, it will use our REST API to upload the application to Experitest's cloud


### For test
  - From [root/hpi], run `mvn clean install  hpi:run -Djetty.port=8080` to start test jenkins at port `8080`
  - Go to `http://localhost:8080/jenkins`, create a sample project to check the plugin.
