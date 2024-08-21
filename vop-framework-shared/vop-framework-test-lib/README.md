## What is this test library project for?

VOP Test library is customized to support testing the RESTful Service applications deployed on VOP platform. This library is configured to use operations provided by the Spring REST Client `RestTemplate`. Spring's rest template provides a convenient way to test RESTful web services by simplifying the interaction with HTTP servers.

## Dependencies:

Spring rest template is included via the `spring-web` dependency.

```xml
<groupId>org.springframework</groupId>
<artifactId>spring-web</artifactId>
```

Cucmber is included via the `cucumber-java8` dependency. It is a library that makes it easier to write and execute parameterized tests.

```xml
<groupId>info.cukes</groupId>
<artifactId>cucumber-java8</artifactId>
```

## How to add VOP Test Library dependencies
Add the dependency to the POM file in your `project-name-inttest` project.

```xml
<dependency>
	<groupId>com.wynd.vop.framework</groupId>
	<artifactId>vop-framework-test-lib</artifactId>
	<version><!-- add the appropriate version --></version>
</dependency>
```

## Overview of the packages

Test Library uses Maven and RestTemplate for core API validations. It provides support classes and methods that can be used to build Automated API tests for the RESTful services.

#### com.wynd.vop.framework.test.exception:

`BipTestLibException`: Custom exception for VOP application that extends Exception. Application use this test library will throw this exception for any exception.

`BipTestLibRuntimeException`: Custom runtime exception for VOP application that extends RuntimeException. Application use this test library will throw this exception for any runtime exception.

#### com.wynd.vop.framework.test.rest:

`BaseStepDef`: Base class that implements some of the common functionalities aka step definitions such as invoke different HTTP methods, set bearer token, compare expected and actual response.  

`BaseStepDefHandler`: Handler object that extends BaseStepDef to handle rest based API calls. Step definition class reference this class to share state between various step definitions using a Singleton pattern.

#### com.wynd.vop.framework.test.service:

`RestConfigService`: Loads the configuration file that has all the properties related to reference services.

**Note: All the configurations are defined external to the code and is per profile/environment. The naming conversion of the file
vetservices-inttest-<env>.properties**

`BearerTokenService`: Fetches token from the token API. The token is included in the HTTP header when invoking an end point.

#### com.wynd.vop.framework.test.util:

`RestUtil`: Utility wrapper for the rest template, used to simplify making HTTP calls, parsing JSON / XML responses, and performing status code checks.

`JsonUtil`: Utility class for parsing JSON, and extract JSON snippets.

`PropertiesUtil`: Utility class for reading properties files and performing variable substitution.

`RequestResponseLoggingInterceptor`: An interceptor for all rest API calls to trace requests and responses. This interceptor can also be used for troubleshooting to trace request headers, parameters, and the response body.

## Test library example
An example of a functioning integration test can be seen in the [reference-inttest project](https://github.ec.va.gov/EPMO/vop-reference-person/tree/master/vop-reference-inttest).

## Class Diagrams
	com.wynd.vop.framework.test 
<img src="/images/cd-test-lib-package.jpg" />
