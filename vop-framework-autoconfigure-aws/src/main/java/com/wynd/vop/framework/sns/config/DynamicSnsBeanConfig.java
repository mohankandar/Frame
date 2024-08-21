package com.wynd.vop.framework.sns.config;

import com.wynd.vop.framework.config.BipAwsPostProcessor;
import com.wynd.vop.framework.log.BipLogger;
import com.wynd.vop.framework.log.BipLoggerFactory;
import com.wynd.vop.framework.sns.services.StandardSnsService;
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
 * for SNS service configurations, and will generate and register a {@link StandardSnsService} Spring Bean for each
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
public class DynamicSnsBeanConfig {

    private static final BipLogger LOGGER = BipLoggerFactory.getLogger(DynamicSnsBeanConfig.class);

    private static final String PROPERTY_TOPIC_PREFIX = "vop.framework.aws.sns.topics[";
    private static final String PROPERTY_TOPIC_ID = "].id";
    private static final String PROPERTY_TOPIC_NAME = "].name";
    private static final String PROPERTY_TOPIC_ENDPOINT = "].endpoint";
    private static final String PROPERTY_TOPIC_REGION = "].region";
    private static final String PROPERTY_LOCALSTACK_SNS_ENABLED = "vop.framework.localstack.services.sns.enabled";

    @Bean
    public BeanDefinitionRegistryPostProcessor snsBeanPostProcessor(ConfigurableEnvironment environment) {
        return new BipSnsPostProcessor(environment);
    }

    class BipSnsPostProcessor extends BipAwsPostProcessor implements BeanDefinitionRegistryPostProcessor {

        private List<SnsProperties.SnsTopic> snsTopics = null;

        /**
         * Reads property value from the configuration, then stores it
         * @param environment
         */
        public BipSnsPostProcessor(ConfigurableEnvironment environment) {
            initializeSnsTopicsFromProps(environment);
            initializeLocalstackProps(environment, PROPERTY_LOCALSTACK_SNS_ENABLED);
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
            LOGGER.info("Registering AmazonSNS beans for creation...");
            for(SnsProperties.SnsTopic snsTopic : snsTopics) {
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(StandardSnsService.class);

                builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_NAME);

                builder.addConstructorArgValue(getAWSCredentialsProvider());
                builder.addConstructorArgValue(getEndpointConfiguration(snsTopic));
                builder.addConstructorArgValue(snsTopic);

                LOGGER.info("Registering AmazonSNS bean: "+snsTopic.getId());
                registry.registerBeanDefinition(snsTopic.getId(), builder.getBeanDefinition());
            }
            LOGGER.info("Completed registering AmazonSNS beans for creation!");
        }

        /*
         * Will attempt to gather all correctly defined SNS Topic definitions from the given environment's properties.
         *
         * These definitions will be stored as a list in the snsTopics variable.
         *
         * If an improper configuration is hit while scanning properties, this function will assume it has hit the end
         * of the configuration list and will still add any properly configured configurations to the snsTopics list.
         */
        private void initializeSnsTopicsFromProps(ConfigurableEnvironment environment) {
            snsTopics = new ArrayList<>();
            int topicNumber = 0;
            boolean continueLooping = true;

            while(continueLooping) {
                try {
                    String id = readProperty(environment, PROPERTY_TOPIC_PREFIX + topicNumber + PROPERTY_TOPIC_ID);
                    String name = readProperty(environment, PROPERTY_TOPIC_PREFIX + topicNumber + PROPERTY_TOPIC_NAME);
                    String endpoint = readProperty(environment, PROPERTY_TOPIC_PREFIX + topicNumber + PROPERTY_TOPIC_ENDPOINT);
                    String region = readProperty(environment, PROPERTY_TOPIC_PREFIX + topicNumber + PROPERTY_TOPIC_REGION);

                    if(name == null || name.length() == 0) {
                        // We have run out topics, or hit an improperly defined topic. Exit topic property gathering.
                        continueLooping = false;
                    } else {
                        // Create and add new SNS topic to list
                        SnsProperties.SnsTopic newTopic = new SnsProperties.SnsTopic();
                        newTopic.setId(id);
                        newTopic.setName(name);
                        newTopic.setEndpoint(endpoint);
                        newTopic.setRegion(region);

                        snsTopics.add(newTopic);

                        // Increment topicNumber for next iteration
                        topicNumber++;
                    }
                } catch (IllegalArgumentException | IllegalStateException e) {
                    // We have run out topics, or hit an improperly defined topic. Exit topic property gathering.
                    continueLooping = false;
                }
            }
        }
    }
}
