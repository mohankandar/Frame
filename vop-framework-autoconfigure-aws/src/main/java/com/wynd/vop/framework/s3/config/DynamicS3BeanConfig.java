package com.wynd.vop.framework.s3.config;

import com.wynd.vop.framework.config.BipAwsPostProcessor;
import com.wynd.vop.framework.log.BipLogger;
import com.wynd.vop.framework.log.BipLoggerFactory;
import com.wynd.vop.framework.s3.services.StandardS3Service;
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
 * for S3 service configurations, and will generate and register a {@link StandardS3Service} Spring Bean for each
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
public class DynamicS3BeanConfig {

    private static final BipLogger LOGGER = BipLoggerFactory.getLogger(DynamicS3BeanConfig.class);

    private static final String PROPERTY_BUCKET_PREFIX = "vop.framework.aws.s3.buckets[";
    private static final String PROPERTY_BUCKET_ID = "].id";
    private static final String PROPERTY_BUCKET_NAME = "].name";
    private static final String PROPERTY_BUCKET_ENDPOINT = "].endpoint";
    private static final String PROPERTY_BUCKET_REGION = "].region";
    private static final String PROPERTY_LOCALSTACK_S3_ENABLED = "vop.framework.localstack.services.s3.enabled";

    @Bean
    public BeanDefinitionRegistryPostProcessor s3BeanPostProcessor(ConfigurableEnvironment environment) {
        return new BipS3PostProcessor(environment);
    }

    class BipS3PostProcessor extends BipAwsPostProcessor implements BeanDefinitionRegistryPostProcessor {

        private List<S3Properties.S3Bucket> s3Buckets = null;

        /**
         * Reads property value from the configuration, then stores it
         * @param environment
         */
        public BipS3PostProcessor(ConfigurableEnvironment environment) {
            initializeS3BucketsFromProps(environment);
            initializeLocalstackProps(environment, PROPERTY_LOCALSTACK_S3_ENABLED);
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
            LOGGER.info("Registering AmazonS3 beans for creation...");
            for(S3Properties.S3Bucket s3Bucket : s3Buckets) {
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(StandardS3Service.class);

                builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_NAME);

                builder.addConstructorArgValue(getAWSCredentialsProvider());
                builder.addConstructorArgValue(getEndpointConfiguration(s3Bucket));
                builder.addConstructorArgValue(s3Bucket);

                LOGGER.info("Registering AmazonS3 bean: "+s3Bucket.getId());
                registry.registerBeanDefinition(s3Bucket.getId(), builder.getBeanDefinition());
            }
            LOGGER.info("Completed registering AmazonS3 beans for creation!");
        }

        /*
         * Will attempt to gather all correctly defined S3 Bucket definitions from the given environment's properties.
         *
         * These definitions will be stored as a list in the s3Buckets variable.
         *
         * If an improper configuration is hit while scanning properties, this function will assume it has hit the end
         * of the configuration list and will still add any properly configured configurations to the s3Buckets list.
         */
        private void initializeS3BucketsFromProps(ConfigurableEnvironment environment) {
            s3Buckets = new ArrayList<>();
            int bucketNumber = 0;
            boolean continueLooping = true;

            while(continueLooping) {
                try {
                    String id = readProperty(environment, PROPERTY_BUCKET_PREFIX + bucketNumber + PROPERTY_BUCKET_ID);
                    String name = readProperty(environment, PROPERTY_BUCKET_PREFIX + bucketNumber + PROPERTY_BUCKET_NAME);
                    String endpoint = readProperty(environment, PROPERTY_BUCKET_PREFIX + bucketNumber + PROPERTY_BUCKET_ENDPOINT);
                    String region = readProperty(environment, PROPERTY_BUCKET_PREFIX + bucketNumber + PROPERTY_BUCKET_REGION);

                    if(name == null || name.length() == 0) {
                        // We have run out buckets, or hit an improperly defined bucket. Exit bucket property gathering.
                        continueLooping = false;
                    } else {
                        // Create and add new S3 bucket to list
                        S3Properties.S3Bucket newBucket = new S3Properties.S3Bucket();
                        newBucket.setId(id);
                        newBucket.setName(name);
                        newBucket.setEndpoint(endpoint);
                        newBucket.setRegion(region);

                        s3Buckets.add(newBucket);

                        // Increment bucketNumber for next iteration
                        bucketNumber++;
                    }
                } catch (IllegalArgumentException | IllegalStateException e) {
                    // We have run out buckets, or hit an improperly defined bucket. Exit bucket property gathering.
                    continueLooping = false;
                }
            }
        }
    }
}
