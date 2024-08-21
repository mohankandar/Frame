# OpenJDK Migration

## Background
As part of being careful stewards of taxpayer resources, the VA has directed projects to attempt a migration from Oracle
Java to an Open JDK distribution to prevent the need to acquire licenses. Since January 2019, Oracle no longer 
issues public updates for commercial users of Java 8 which means free updates for commercial use are no more. This
applies to usage only in production.

Oracle OpenJDK is the free and open-source edition of the Java SE platform, but it does not come with commericial support.

## Distributions
As a result of the licensing changes by Oracle, other vendors have stepped in to provide their own distributions derived
from the Oracle OpenJDK upstream paired with commercial support contracts.
These include:
- AdoptOpenJDK
- Amazon Corretto
- Azul Zulu
- RedHat OpenJDK
- IBM JDK
- etc...

Most of these distributions are not on the approved list or they are only approved within certain restrictive conditions.
For instance, the IBM JDK is only allowed in concert with IBM products. Amazon Corretto is a potential option, but
container images are not based on RedHat Enterprise Linux (RHEL).
RedHat OpenJDK seems the most promising given the TRM restrictions to use RHEL as well as the additional support we 
already get for RHEL.

## Versions
Oracle's new versioning strategy results in more frequent increments of the major version, but each major version is not
equal. Only certain major versions get Long Term Support (LTS) status which is necessary to keep the JDK secure and stable.
The current LTS versions are 8 and 11 and the next LTS version will be 17. With this LTS concern in mind, the VA TRM
only has versions 8 and 11 approved.

## Recommendations
As a platform, adoption of the latest approved JDK will keep our applications more modern, stable, and secure. 
In particular, Java 11 should be adopted to leverage the language additions in Java 9, 10, and 11 as well as the newer, 
more performant garbage collectors. Both OpenJDK 8 and 11 support Java Flight Recordings.

## Limitations
Java 11 has removed a few dependencies as part of the core library, but those are easy to include through
additional application dependencies.

The following packages have been removed:
`java.xml.ws, java.xml.bind, java.activation, java.xml.ws.annotation, java.corba, java.transaction, java.se.ee, jdk.xml.ws, jdk.xml.bind`

## Support
Due to possible incompatibilities for tenant applications, the framework will still be compiled on Java 8 for the time
being which will allow it to work on both 8 and 11. Java 8 and 11 containers will also be supported with a better
plan to include the necessary VA certificates in the trust store. However,
in an effort to provide long term upgrade paths to LTS versions of Java, the framework will be deprecated on Java 8 with
a plan to move the framework to be compiled by Java 11 by the end of 2021.
Framework will be deprecated on Java 8 with support removed at the end of 2021 when it will start being compiled with Java 11.
At that point, any project using previous Java 8 produced framework jars should be on a Java 11 container image so 
that they can easily move to the latest version if they desire new features or bug fixes. 
Also, Java 17 should also be available and compatibility with that version will also be verified and likely
supported. Java 17 will have great new features like the ability to provide the variable that is null when a NullPointerException
is encountered.

# Caveats
OpenJDK 8 LTS actually has a longer support window  (2026) than OpenJDK 11 (2024). However, the transition
from 11 to 17 should be seamless at which point the support window will go out past 2026 and help our strategy into the
foreseeable future.

## Summary
Platform tenants should move to RedHat OpenJDK 11 and if compatibility issues arise, OpenJDK 8 can be used until the end
of 2021. Additional communications will be provided to assist in making the necessary changes. Tenants should not
proceed with migration until common recommendations are made to ensure support teams have a fairly consist set of 
patterns to deal with.

## Link
- [General OpenJDK Info](https://en.wikipedia.org/wiki/OpenJDK)
- [Migration Guide](https://docs.oracle.com/en/java/javase/11/migrate/index.html#JSMIG-GUID-C25E2B1D-624-4403-8540-CFEA875B994A)
