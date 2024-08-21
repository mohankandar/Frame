This page documents the purpose and capabilities of _VOP Framework Libraries_ for the services.

# What is this library project for?

This project contains interfaces, annotations and classes consumed by the application services for various functionality:

- Marker interfaces for transfer objects to support common identification and behavior
- Rest Provider Message classes, RestTemplate
- Audit and Performance Logging aspects
- Utility ops for logging and handling exceptions
- Root classes for checked and runtime exceptions
- WebService client config
- Security JWT base classes, properties and exceptions
- API Gateway Kong Configuration for JWT Consumer 
- Service Domain Message classes, timer and validation aspects

# VOP Framework principles

VOP Framework aims to:

- free developers from many of the complexities of dealing with the underlying platform,
- enable centralized application configuration,
- enable developers to focus more on business requirements and less on boilerplate code,
- encourage developers to use good coding practices and patterns that are effective and efficient,
- encourage developers to write code that presents a common "look and feel" across projects,
- enable developers to produce reliable code that takes less time to develop and test.

# How to add the Framework dependency

Add the dependency in the application project's POM file.

```xml
<dependency>
    <groupId>com.wynd.vop.framework</groupId>
    <artifactId>vop-framework-libraries</artifactId>
    <version><!-- add the appropriate version --></version>
</dependency>
```

# Framework usage in service applications

For more information about developing applications on the VOP Framework, see [Developing with VOP Framework](https://github.ec.va.gov/EPMO/vop-reference-person/tree/master/docs/developing-with-vop-framework.md).

# Log Masking

Logback is configured in [`vop-framework-logback-starter.xml`](https://github.ec.va.gov/EPMO/vop-framework/blob/master/vop-framework-autoconfigure/src/main/resources/gov/va/vop/framework/starter/logger/vop-framework-logback-starter.xml). As the app starts up, logback's `ContextInitializer.configureByResource(..)` method reads the configured `BipMaskingMessageProvider` encoder provider. Logback invokes this custom provider by convention: the tag names and values within the `<provider>` xml declaration are used to infer java class names and properties used by the provider.

The framework uses masking rules to provide default masking for the `VOP_FRAMEWORK_ASYNC_CONSOLE_APPENDER`. See the [_Logger_](#logger) sequence diagram below.

Additional log masking definitions can be declared within services with the `BipMaskingFilter` class. This class can be referenced to declare masking in a logback filter.

If declarative masking in logback config is not sufficient for specific data, developers can manually mask data with methods from `MaskUtils`.

See [Log and Audit Management](https://github.ec.va.gov/EPMO/vop-reference-person/blob/master/docs/log-audit-management.md) for more information.

# Sequence Diagrams

## _Audit_

### _@Auditable_

![](/images/sd-lib-audit-annotation-before.png) ![](/images/sd-lib-audit-annotation-afterreturning.png) ![](/images/sd-lib-audit-annotation-afterthrowing.png)

### _ProviderHttpAspect_

![](/images/sd-lib-audit-providerhttpascpect-before.png) ![](/images/sd-lib-audit-providerhttpascpect-afterreturning.png) ![](/images/sd-lib-audit-providerhttpascpect-afterthrowing.png)

### _BipCacheInterceptor_

![](/images/sd-lib-audit-cache.png)

### _AuditLogSerializer & AuditLogger_

The AuditLogger can now be disabled by setting the following two variables as JAVA SYSTEM PROPERTIES. 

`com.wynd.vop.framework.audit.enableAuditLoggingLevelOverride` which will take a boolean `true` or `false` value, and `com.wynd.vop.framework.audit.auditLogger.auditLevel` which will take your given logging level, EX: `ERROR` or `WARN`

While the AuditLogger can be modified from the standard INFO logging, this is strongly unrecommended and should only be overridden in circumstances such as testing or other unique situations. 

![](/images/sd-lib-audit-logserializer.png)

## _Logger_

![](/images/sd-lib-log.png)

## _JWT Token Generator_

![](/images/sd-lib-security-jwt-generator.png)

# Class Diagrams

## _Aspects, Join Points and Pointcuts_

```
com.wynd.vop.framework.aspect
com.wynd.vop.framework.rest.provider.aspect
com.wynd.vop.framework.service.aspect
```

![](/images/cd-lib-aspect.png)

## _Audit_

```
com.wynd.vop.framework.audit
com.wynd.vop.framework.audit.annotation
com.wynd.vop.framework.audit.http
com.wynd.vop.framework.audit.model
```

![](/images/cd-lib-audit.png)

## _Cache_

```
com.wynd.vop.framework.cache
com.wynd.vop.framework.cache.interceptor
```

![](/images/cd-lib-cache.png)

## _Client_

### _REST Client_

```
com.wynd.vop.framework.client.rest.template
```

![](/images/cd-lib-client-rest.png)

### _SOAP Client_

```
com.wynd.vop.framework.client.ws
com.wynd.vop.framework.client.ws.interceptor
com.wynd.vop.framework.client.ws.interceptor.transport
com.wynd.vop.framework.client.ws.remote
```

![](/images/cd-lib-client-ws.png)

## _Config_

```
com.wynd.vop.framework.config
```

![](/images/cd-lib-config.png)

## _Exception_

```
com.wynd.vop.framework.exception
com.wynd.vop.framework.exception.interceptor
com.wynd.vop.framework.rest.exception
com.wynd.vop.framework.security.jwt
com.wynd.vop.framework.service
```

![](/images/cd-lib-exception.png)

## _kong_

```
com.wynd.vop.framework.kong
com.wynd.vop.framework.kong.model
```

![](/images/cd-lib-kong.png)

## _Logging_

```
com.wynd.vop.framework.log
```

![](/images/cd-lib-log.png)

## _Messages_

```
com.wynd.vop.framework.messages
```

![](/images/cd-lib-messages.png)

## _Rest_

```
com.wynd.vop.framework.rest.exception
com.wynd.vop.framework.rest.provider
com.wynd.vop.framework.rest.aspect
```

![](/images/cd-lib-rest.png)

## _Security_

```
com.wynd.vop.framework.security
com.wynd.vop.framework.security.model
com.wynd.vop.framework.security.util
```

![](/images/cd-lib-security.png)

## _Security JWT_

```
com.wynd.vop.framework.security.jwt
com.wynd.vop.framework.security.jwt.correlation
```

![](/images/cd-lib-security-jwt.png)

## _Service_

```
com.wynd.vop.framework.service
com.wynd.vop.framework.service.spect
```

![](/images/cd-lib-service.png)

## _Swagger_

```
com.wynd.vop.framework.swagger
```

![](/images/cd-lib-swagger.png)

## _Transfer_

```
com.wynd.vop.framework.transfer
com.wynd.vop.framework.transfer.jaxb.adapters
com.wynd.vop.framework.transfer.transform
```

![](/images/cd-lib-transfer.png)

## _Validation_

```
com.wynd.vop.framework.validation
```

![](/images/cd-lib-validation.png)
