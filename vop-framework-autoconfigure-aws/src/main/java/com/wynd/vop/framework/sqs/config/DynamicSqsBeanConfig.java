package com.wynd.vop.framework.sqs.config;

import com.wynd.vop.framework.config.BipAwsPostProcessor;
import com.wynd.vop.framework.log.BipLogger;
import com.wynd.vop.framework.sqs.services.StandardSqsService;
import com.wynd.vop.framework.log.BipLoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.ArrayList;
import java.util.List;

/**
 * This Spring configuration, when included in the active SpringContext, will attempt to gather property definitions
 * for SQS service configurations, and will generate and register a {@link StandardSqsService} Spring Bean for each
 * configuration definition identified.
 *
 * These dynamically generated beans will instantiate themselves with the provided configurations, then make themselves
 * available to the application through autowiring using {@link org.springframework.beans.factory.annotation.Qualifier}
 * annotations.
 *
 * The dynamic creation and registry of these beans will occur at the {@link BeanDefinitionRegistryPostProcessor} step
 * of the Spring Lifecycle.
 */
@Configuration
public class DynamicSqsBeanConfig {

    private static final BipLogger LOGGER = BipLoggerFactory.getLogger(DynamicSqsBeanConfig.class);

    private static final String PROPERTY_QUEUE_PREFIX = "vop.framework.aws.sqs.queues[";
    private static final String PROPERTY_QUEUE_ID = "].id";
    private static final String PROPERTY_QUEUE_NAME = "].name";
    private static final String PROPERTY_QUEUE_ENDPOINT = "].endpoint";
    private static final String PROPERTY_QUEUE_REGION = "].region";
    private static final String PROPERTY_LOCALSTACK_SQS_ENABLED = "vop.framework.localstack.services.sqs.enabled";

    @Bean
    public BeanDefinitionRegistryPostProcessor sqsBeanPostProcessor(ConfigurableEnvironment environment) {
        return new BipSqsPostProcessor(environment);
    }

    class BipSqsPostProcessor extends BipAwsPostProcessor implements BeanDefinitionRegistryPostProcessor {

        private List<SqsProperties.SqsQueue> sqsQueues = null;

        /**
         * Reads property value from the configuration, then stores it
         * @param environment
         */
        public BipSqsPostProcessor(ConfigurableEnvironment environment) {
            initializeSqsQueuesFromProps(environment);
            initializeLocalstackProps(environment, PROPERTY_LOCALSTACK_SQS_ENABLED);
        }

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
            // Do nothing. We will handle bean registry in postProcessBeanDefinitionRegistry(...) below.
        }

        /*
         * Creates the bean definition dynamically (using the configuration value), then registers it
         */
        @Override
        public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
            LOGGER.info("Registering AmazonSQS beans for creation...");
            for(SqsProperties.SqsQueue sqsQueue : sqsQueues) {
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(StandardSqsService.class);

                builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_NAME);

                builder.addConstructorArgValue(getAWSCredentialsProvider());
                builder.addConstructorArgValue(getEndpointConfiguration(sqsQueue));
                builder.addConstructorArgValue(sqsQueue);

                LOGGER.info("Registering AmazonSQS bean: "+sqsQueue.getId());
                registry.registerBeanDefinition(sqsQueue.getId(), builder.getBeanDefinition());
            }
            LOGGER.info("Completed registering AmazonSQS beans for creation!");
        }

        /*
         * Will attempt to gather all correctly defined SQS Queue definitions from the given environment's properties.
         *
         * These definitions will be stored as a list in the sqsQueues variable.
         *
         * If an improper configuration is hit while scanning properties, this function will assume it has hit the end
         * of the configuration list and will still add any properly configured configurations to the sqsQueues list.
         */
        private void initializeSqsQueuesFromProps(ConfigurableEnvironment environment) {
            sqsQueues = new ArrayList<>();
            int queueNumber = 0;
            boolean continueLooping = true;

            while(continueLooping) {
                try {
                    String id = readProperty(environment, PROPERTY_QUEUE_PREFIX + queueNumber + PROPERTY_QUEUE_ID);
                    String name = readProperty(environment, PROPERTY_QUEUE_PREFIX + queueNumber + PROPERTY_QUEUE_NAME);
                    String endpoint = readProperty(environment, PROPERTY_QUEUE_PREFIX + queueNumber + PROPERTY_QUEUE_ENDPOINT);
                    String region = readProperty(environment, PROPERTY_QUEUE_PREFIX + queueNumber + PROPERTY_QUEUE_REGION);

                    if(name == null || name.length() == 0) {
                        // We have run out queues, or hit an improperly defined queue. Exit queue property gathering.
                        continueLooping = false;
                    } else {
                        // Create and add new SQS queue to list
                        SqsProperties.SqsQueue newQueue = new SqsProperties.SqsQueue();
                        newQueue.setId(id);
                        newQueue.setName(name);
                        newQueue.setEndpoint(endpoint);
                        newQueue.setRegion(region);

                        sqsQueues.add(newQueue);

                        // Increment queueNumber for next iteration
                        queueNumber++;
                    }
                } catch (IllegalArgumentException | IllegalStateException e) {
                    // We have run out queues, or hit an improperly defined queue. Exit queue property gathering.
                    continueLooping = false;
                }
            }
        }
    }
}
