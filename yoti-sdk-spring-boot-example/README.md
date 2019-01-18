# Spring Boot Yoti SDK Example

This example project shows two ways of completing a share with Yoti.  One initiates a share using a static scenario defined in [Yoti Dashboard](https://www.yoti.com/dashboard/applications), the other creates a dynamic scenario on the fly.  Both examples finish by using the Java SDK to retrieve the shared user profile, based on a token returned from Yoti as queryParam.

Before you start, you'll need to create an Application in [Yoti Dashboard](https://www.yoti.com/dashboard/applications) and verify the domain.

**NOTE: Some attributes (except phone number and selfie) may only be retrieved from users who have verified them against a document, such as a passport.  So for instance, if your application requires the user's date of birth but their date of birth has not been verified against a document, this will lead to a failed login.**

## Creating your example Yoti Application
1. In the [Yoti Dashboard](https://www.yoti.com/dashboard/applications) set the application domain of your app to `https://localhost:8443/`. Note that your endpoint must be accessible from the machine that is displaying the QR code.
1. If you want to try out a static scenario, create one in dashboard and set the scenario callback URL to `/login`.  Note the _Scenario ID_ 
1. Use _Actions > Activate_ to make your application live
1. From the _KEYS_ tab, note your _Application ID_ and _Yoti client SDK ID_
1. Hit _Generate Key Pair_ to get a .pem file for your app and save it to your hard drive


## Project Structure
* `resources/app-keypair.pem` is the keypair you can get from Dashboard.
* `resource/application.yml` contains the configuration that enforces SSL usage by your server-app (in case you are not using a proxy server like NGINX). Make sure that you update the SDK Application ID and the configuration points to the right path to the java keystore with an SSL key (there is an already one included in the project ``` server.keystore.jks ```).
* The current SDK version is referenced in the pom by including:
```xml
    <dependency>
      <groupId>com.yoti</groupId>
      <artifactId>yoti-sdk-impl</artifactId>
      <version>2.2.0</version>
    </dependency>
```

### Configuring the Spring Boot App
You only need to edit the [resources/application.yml](src/main/resources/application.yml) file
1. Provide the _applicationId_ and _clientSdkId_ you noted earlier
1. Copy your .pem key file to `src/main/resources`, and enter the _filename_ against the _accessSecurityKey_
1. If you want to try out a static scenario, provide the _Scenario ID_ you noted earlier.  You do not need this for a dynamic scenario. 

### Code
* The logic for initiating a share based on a static scenario is in `com.yoti.api.examples.springboot.YotiStaticScenarioController`
* The logic for initiating a share based on a dynamic scenario is in `com.yoti.api.examples.springboot.YotiDynamicScenarioController`
* The logic for retrieving the profile can be found in `com.yoti.api.examples.springboot.YotiLoginController`.


## Running
* Build your server-app with `mvn clean package`
* You can run your server-app by executing `java -jar target/yoti-sdk-spring-boot-example-2.2.0.jar`
  * If you are using Java 9, you can run the server-app as follows `java -jar target/yoti-sdk-spring-boot-example-2.2.0.jar --add-exports java.base/jdk.internal.ref=ALL-UNNAMED`
* To initiate a share using a static scenario, navigate to `https://localhost:8443/static-scenario-demo`
* To initiate a share using a dynamic scenario, navigate to `https://localhost:8443/dynamic-scenario-demo`
* You can then initiate a login using Yoti.  The Spring demo is listening for the response on `https://localhost:8443/login`.

### Requirements for running the application
* Java 7 or above
* If you are using Oracle JDK/JRE you need to install JCE extension in your server's Java to allow strong encryption (http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html). This is not a requirement if you are using OpenJDK.
