# What is this repository for?

To run spring boot application and spring cloud enabled services on the VOP Platform, it must adhere to various service patterns. This repository contains a suite of framework libraries, auto configurations, test libraries and parent POM that must be included as dependencies to enable the patterns.

For information regarding recommended development patterns for developing service applications, and the purpose and usage of capabilities that are provided by the VOP Framework, see the [vop-reference-person README.md](https://github.ec.va.gov/EPMO/vop-reference-person).

For information on framework release notes, see the [VOP Framework Release Notes](https://github.ec.va.gov/EPMO/vop-framework/wiki/Framework-Release-Notes).

# Prequisites
The vop-framework requires JDK 8 or 11 and Maven 3.6 or higher. See [OpenJDK Migration](OpenJDKMigration.md) for more details.

# Project Breakdown & Links

1. [vop-framework-reactor](https://github.ec.va.gov/EPMO/vop-framework): This is the root reactor project (you are in that repo now). This project forms the aggregate of modules that make up the complete framework, and manages the Fortify scans.
	- a maven profile, and a `./fortify.sh` script to run Fortify - this script also documents the unique requirements for running fortify against the framework.
	- a local-dev folder with docker images to run in the spring "local-int" (docker) mode, and with tools to simplify SwA code review submissions.

2. [vop-framework-parentpom](vop-framework-parentpom/README.md): Parent POM for spring boot application and cloud enabled services. It provides a common Maven configuration and dependencies for the suite of projects, and dependency management for capabilities (e.g. database management).
	- Makes VOP Framework the parent for your projects.

3. [vop-framework-autoconfigure](vop-framework-autoconfigure/README.md): Shared auto-configuration for the services to enable the patterns for audit, cache, feign, rest, security, swagger, service, vault etc.

4. [vop-framework-libraries](vop-framework-libraries/README.md): Common VOP capabilities for the services to implement consistent behavior.

5. [vop-framework-shared](vop-framework-shared/README.md): This project contains utilities and functional helpers that can be shared freely with any java project. Presently used by `vop-framework-libraries` and `vop-framework-test-lib` for shared utility.

6. [vop-framework-test-lib](vop-framework-test-lib/README.md): Test library framework to support functional testing for service applications.

# How to include VOP Framework libraries in your project

See the [vop-reference-person README](https://github.ec.va.gov/EPMO/vop-reference-person#how-to-include-the-framework-libraries-in-your-project)

# How to download the VOP Framework maven dependencies

There are currently three ways to make these projects available for local activites.

## Option 1 - clone the source from GitHub

This will bring the framework source code to your local machine.

1. Clone the VOP framework repository:

  `git clone https://github.ec.va.gov/EPMO/vop-framework.git`

2. Navigate to the `vop-framework` folder, and run:

`mvn clean install -U` 
   
 `-U means force update of snapshot dependencies. Release dependencies can't be updated this way.`

This will build all the libraries with versions as configured in `pom.xml` files.

## Option 2 - get the JAR from the VA nexus repo

This option is viable **only** if you are working on a machine that is connected to the VA network.

The base URL for the nexus repository is: <https://nexus.dev8.vop.va.gov/repository>

You must have the VOP Nexus URL configured in the reactor (root) `pom.xml` file as shown below.

```xml
<repositories>
	<repository>
		<id>nexus3</id>
		<name>VOP Nexus Repository</name>
		<url>https://nexus.dev8.vop.va.gov/repository/maven-public</url>
	</repository>
</repositories>
```

## Option 3 - use GitHub as a Nexus repository

This option is a workaround if you are _not_ on the VA network, and you only want the VOP Framework JAR.

This is a temporary solution. The GitHub repository acts as your Nexus repository. It requires changes in the reactor POM, and in your local maven settings file, as shown below.

1. **POM file**

Add the section shown below to the reactor (root) `pom.xml` of your service project. An example of this configuration can be seen in the [vop-reference reactor POM](https://github.ec.va.gov/EPMO/vop-reference-person/blob/master/pom.xml).

```xml
<repositories>
	<repository>
		<id>github</id>
		<name>GitHub Repository</name>
		<url>https://raw.github.com/department-of-veterans-affairs/vop-framework/mvn-repo</url>
	</repository>
</repositories>
```

1. **Settings file**

Update your local `~/.m2/settings.xml` as shown below. Replace values between `{{Text}}` with your information.

```xml
<settings>
	<servers>
		<server>
			<id>github</id>
			<username>{{Your GitHub User Name}}</username>
			<password>{{Your Personal Access Token}}</password>
			<configuration>
				<httpHeaders>
					<property>
						<name>Authorization</name>
						<!--
						For value tag below:
							Step 1: Base64-encode your username and Github access token together
								in the form: {{username}}:{{access_token}}
								Example: encode the string "myGithubUsername:ab123983245sldfkjsw398r7"
							Step 2: Add the encoded string to the value tag in the form of
								"Basic {{encoded-string}}"
								Example: <value>Basic YXJtaXvB4F5ghTE2OGYwNmExMWM2NDdhYjWExZjQ1N2FhNGJiMjE=</value>
						Base64 encoders:
							https://www.freeformatter.com/base64-encoder.html
							https://www.base64encode.org/
						-->
						<value>Basic {{base64 encoded content}}</value>
					</property>
				</httpHeaders>
			</configuration>
		</server>
	</servers>
</settings>
```

# How to deploy and host a maven repository on GitHub

source : <http://stackoverflow.com/questions/14013644/hosting-a-maven-repository-on-github>

1. Modify `~/.m2/settings.xml`, use the same format as mentioned in the previous section

2. Update your application's parent pom with a `local-deploy` profile that configures your GitHub repo. For an example, look in [vop-framework-parentpom/pom.xml](https://github.ec.va.gov/EPMO/vop-framework/blob/master/vop-framework-parentpom/pom.xml) for the section framed by:

```xml
<profile>
	<id>local-deploy</id>
	...    ...
</profile>
```

1. Run maven with the appropriate parameters to deploy and upload artifacts to the repository

`mvn clean deploy -Plocal-deploy -DrepositoryName=vop-framework -DrepositoryOwner=EPMO`

-- OR --

`mvn clean deploy -Plocal-deploy -DrepositoryName=vop-framework -DrepositoryOwner=department-of-veterans-affairs`