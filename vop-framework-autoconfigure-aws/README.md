# What is this project for?

VOP Framework Autoconfigure AWS Project provides application services for SQS and SNS through Localstack for the VOP platform.

# Implementation for project teams 

Teams would have to include the dependecy for this module in the application project pom.xml:

```xml
<!-- Include the vop framework autoconfigure aws -->
<dependency>
    <groupId>com.wynd.vop.framework</groupId>
    <artifactId>vop-framework-autoconfigure-aws</artifactId>
    <version>${vop-framework.version}</version>
</dependency>
```

Add the embedded aws profile to the default profile. The default profile is used for local running, local-int is used for the local containerized scenario. Each upper environment should use different profiles.
```
---
spring.profiles: default
spring.profiles.include: remote_client_sims, embedded-redis, embedded-aws
```

Enable Localstack and enable each service the project is intending to implement.
```
---
### Localstack
vop.framework.localstack:
  enabled: true
  services:
    sns:
      enabled: true
      port: 4575
    sqs:
      enabled: true
      port: 4576
```
Set values for creation of resources in Localstack (for local situations). It is intended that application properties for upper environments would have the proper values for resources created in AWS. For SQS Localstack setup certain defaults are set internal to the code which match the defaults of AWS SQS Queue creation. 
```
---
### AWS Service settings
### SQS and SNS Configurations for Bip Reference Person
vop.framework:
  aws:
    sqs:
      enabled: true
      #region: us-east-1

      endpoint: http://localhost:4576/queue/sub_new_queue
      #delay: 0
      #maxMessageSize: "262144"
      #messageRetentionPeriod: "345600"
      #waitTime: 0
      #visibilityTimeout: 30

      dlqEnabled: true
      dlqEndpoint: http://localhost:4576/queue/sub_new_queue_dead
      #maxReceiveCount: 1
      #dlqdelay: 0
      #dlqmaxMessageSize: "262144"
      #dlqmessageRetentionPeriod: "345600"
      #dlqwaitTime: 0
      #dlqvisibilityTimeout: 30

      #For SQS Configuration - Messaging Provider Configuration
      #numberOfMessagesToPrefetch: 0

      #For Message Listener Setup in QueueAsyncMessageReceiver
      #Defines the maximum number of times the message can enter the DLQ
      retries: 1

    sns:
      enabled: true
      #region: us-east-1
      endpoint: http://localhost:4575/topic/test_my_topic
      topicarn: arn:aws:sns:us-east-1:000000000000:test_my_topic
      name: test_my_topic
      message: "SNS Test Message"
      type: String
```
Also settings below are required. Each implementing application team would need to add these.
```
# Required or else errors occur (Spring requirement)
cloud.aws.stack.auto: false
cloud.aws.region.static: us-east-1

# Required for localstack SQS integration
spring.sleuth.messaging.enabled: false
```

# Local Integration Setup 

Implementing project teams would need to simply add another service to the docker-compose.yml

In the vop-reference-person the relevant part of the docker-compose.yml looks like this:

```
  vop-reference-person:
    image: vopdev/vop-reference-person
    build:
      context: ./vop-reference-person
      dockerfile: Dockerfile.local
    environment:
      - spring.profiles.active=local-int
      ...
      - JAVA_TOOL_OPTIONS=-agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=n #Remote Debugging
    ports:
      - "8080:8080"
      - "8000:8000" #Remote Debugging
    networks:
      - vop
    links:
      - localstack
    depends_on:
      ...
      - localstack

  localstack:
    image: localstack/localstack:0.10.7
    container_name: localstack
    ports:
      - "4567-4584:4567-4584"
      - "8888:8888"
    environment:
      - HOSTNAME_EXTERNAL=localstack
      - DEBUG=1
      - USE_SSL=0
      - SERVICES=sns:4575,sqs:4576
      - PORT_WEB_UI=8888
    networks:
      - vop
```
---
 ### Dev8 Configuration
 
 In order for your project to have access to the Dev8 env, you will need to submit a ticket for an IAM role with the desired amount of SQS queues and SNS topics you desire.
 
 `E_VOP_PLATFORM_SUPPORT@BAH.COM`
 
 Once you have your IAM role, you will need to ensure your external config is set properly in order for your properties to override the localstack configuration:
 
 `vop-reference-person.yml`<< this will be: your-projects-name.yml under config/dev
 ```
    vop.framework:
      aws:
        sqs:
          enabled: true
          dlqEnabled: true
          region: us-gov-west-1

          #Aws Dev8 vars
          endpoint: https://sqs.us-gov-west-1.amazonaws.com/261727212250/project-blue-dev-blue-dev-queue-1
          dlqEndpoint: https://sqs.us-gov-west-1.amazonaws.com/261727212250/project-blue-dev-blue-dev-queue-2

          #Defines the maximum number of times the message can enter the DLQ
          retries: 1

        sns:
          enabled: true
          region: us-gov-west-1

          #Aws Dev8 vars
          endpoint: https://sns.us-gov-west-1.amazonaws.com/261727212250/project-blue-dev-blue-dev-topic
          topicarn: arn:aws-us-gov:iam::261727212250:/role/project/project-vop-blue-dev
          name: project-vop-blue-dev
```

You must also update your role in the helm chart in order for the project to pickup the IAM role
`charts/vop-reference-person/values.yaml`

```aws:
     iamRole: "project-vop-blue-dev-role"```
     
     