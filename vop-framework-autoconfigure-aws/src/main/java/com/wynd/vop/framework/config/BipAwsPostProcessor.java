package com.wynd.vop.framework.config;

import com.amazonaws.auth.*;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.wynd.vop.framework.localstack.autoconfigure.LocalstackProperties;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;

public abstract class BipAwsPostProcessor {

    private static final String PROPERTY_LOCALSTACK_ENABLED = "vop.framework.localstack.enabled";

    private boolean isContainerizedLocalstack;

    /*
     * Iterates over all configuration sources, looking for the property value.
     * As Spring orders the property sources by relevance, the value of the first
     * encountered property with the correct name is read and returned.
     */
    protected String readProperty(ConfigurableEnvironment environment, String propertyKey) {
        for (PropertySource<?> source : environment.getPropertySources()) {
            if (source instanceof EnumerablePropertySource) {
                EnumerablePropertySource<?> propertySource = (EnumerablePropertySource<?>) source;
                for (String property : propertySource.getPropertyNames()) {
                    if (propertyKey.equals(property)) {

                        String propAsString = "";
                        Object prop = propertySource.getProperty(propertyKey);

                        propAsString = convertPropToString(prop);

                        return propAsString;
                    }
                }
            }
        }
        throw new IllegalStateException("Unable to determine value of property " + propertyKey);
    }

    private String convertPropToString(Object prop) {
        String propAsString = "";

        if(prop == null) {
            propAsString = "";
        } else if(prop instanceof Boolean) {
            propAsString = Boolean.toString((Boolean) prop);
        } else if(prop instanceof String) {
            propAsString = (String) prop;
        } else {
            propAsString = prop.toString();
        }

        return propAsString;
    }

    protected AWSCredentialsProvider getAWSCredentialsProvider() {

        // Create credentials provider for LocalStack defaults
        AWSCredentialsProvider localstackCredentialsProvider =
                new AWSStaticCredentialsProvider(new BasicAWSCredentials(
                    LocalstackProperties.DEFAULT_LOCALSTACK_ACCESS_KEY, LocalstackProperties.DEFAULT_LOCALSTACK_SECRET_KEY));

        // Return new credentials provider chain
        return new AWSCredentialsProviderChain(new DefaultAWSCredentialsProviderChain(), localstackCredentialsProvider);
    }

    protected AwsClientBuilder.EndpointConfiguration getEndpointConfiguration(BipAwsServiceEndpoint awsServiceEndpoint) {

        AwsClientBuilder.EndpointConfiguration endpointConfiguration;

        // If LocalStack is enabled, and we want to leverage Embedded AWS, and we are using the LocalInt profile
        // (Dockerized environment), then point to the LocalStack Docker container name
        if (isContainerizedLocalstack() && awsServiceEndpoint.getBaseUrl().contains("localhost")) {
            awsServiceEndpoint.setEndpoint(awsServiceEndpoint.getEndpoint().replaceFirst("localhost", "localstack"));
        }

        endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(awsServiceEndpoint.getBaseUrl(), awsServiceEndpoint.getRegions().getName());

        return endpointConfiguration;
    }

    protected void initializeLocalstackProps(ConfigurableEnvironment environment, String localstackServiceEnabledPropertyKey) {
        // Set defaults
        boolean isEmbeddedAws = false;
        boolean isLocalInt = false;
        boolean isLocalstackEnabled = false;
        boolean isLocalstackServiceEnabled = false;

        for (final String profileName : environment.getActiveProfiles()) {
            if (profileName.equals(BipCommonSpringProfiles.PROFILE_EMBEDDED_AWS)) {
                isEmbeddedAws = true;
            }
            if (profileName.equals(BipCommonSpringProfiles.PROFILE_ENV_LOCAL_INT)) {
                isLocalInt = true;
            }
        }

        try {
            isLocalstackEnabled = Boolean.parseBoolean(readProperty(environment, PROPERTY_LOCALSTACK_ENABLED));
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Defaults to false, do nothing.
        }

        try {
            isLocalstackServiceEnabled = Boolean.parseBoolean(readProperty(environment, localstackServiceEnabledPropertyKey));
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Defaults to false, do nothing.
        }

        isContainerizedLocalstack = isEmbeddedAws && isLocalInt && isLocalstackEnabled && isLocalstackServiceEnabled;
    }

    protected boolean isContainerizedLocalstack() {
        return isContainerizedLocalstack;
    }
}
