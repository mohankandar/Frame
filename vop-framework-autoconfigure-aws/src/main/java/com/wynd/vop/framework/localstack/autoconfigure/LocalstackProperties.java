package com.wynd.vop.framework.localstack.autoconfigure;

import com.wynd.vop.framework.localstack.sns.config.LocalstackSnsProperties;
import com.wynd.vop.framework.localstack.sqs.config.LocalstackSqsProperties;
import com.wynd.vop.framework.localstack.s3.config.LocalstackS3Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@EnableConfigurationProperties({
        LocalstackSqsProperties.class,
        LocalstackSnsProperties.class,
        LocalstackS3Properties.class
})
@ConfigurationProperties(prefix = "vop.framework.localstack")
@ConditionalOnProperty(value = "vop.framework.localstack.enabled")
public class LocalstackProperties {

    public static final String DEFAULT_LOCALSTACK_ACCESS_KEY = "test-key";

    public static final String DEFAULT_LOCALSTACK_SECRET_KEY = "test-secret";

    @Autowired
    private LocalstackSnsProperties localstackSnsProperties;

    @Autowired
    private LocalstackSqsProperties localstackSqsProperties;

    @Autowired
    private LocalstackS3Properties localstackS3Properties;

    private String externalHostName = "localhost";

    private String imageTag = "0.10.7";

    private boolean pullNewImage = false;

    private boolean randomizePorts = false;

    private String defaultRegion = "us-east-1";

    private String network = null;

    /**
     * Create enabled service definitions used on Localstack.
     */
    private List<Services> services;

    public String getExternalHostName() {
        return externalHostName;
    }

    public void setExternalHostName(String externalHostName) {
        this.externalHostName = externalHostName;
    }

    public String getImageTag() {
        return imageTag;
    }

    public void setImageTag(String imageTag) {
        this.imageTag = imageTag;
    }

    public boolean isPullNewImage() {
        return pullNewImage;
    }

    public void setPullNewImage(boolean pullNewImage) {
        this.pullNewImage = pullNewImage;
    }

    public boolean isRandomizePorts() {
        return randomizePorts;
    }

    public void setRandomizePorts(boolean randomizePorts) {
        this.randomizePorts = randomizePorts;
    }

    public String getDefaultRegion() {
        return defaultRegion;
    }

    public void setDefaultRegion(String defaultRegion) {
        this.defaultRegion = defaultRegion;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public void setServices(List<Services> services) {
        this.services = services;
    }

    public List<Services> getServices() {
        if (this.services == null) {

            this.services = new ArrayList<>();

            if (localstackSnsProperties.isEnabled()) {
                this.services.add(new Services("sns", localstackSnsProperties.getPort()));
            }

            if (localstackSqsProperties.isEnabled()) {
                this.services.add(new Services("sqs", localstackSqsProperties.getPort()));
            }

            if (localstackS3Properties.isEnabled()) {
                this.services.add(new Services("s3", localstackS3Properties.getPort()));
            }

            // ... more services to come
        }
        return this.services;
    }

    /** Inner class with Services specific config properties */
    public class Services {

        /** AWS Service name */
        private String name;

        /** AWS Service port */
        private int port;

        /**
         * Instantiate an object that defines Localstack service properties.
         *
         * @param name
         * @param port
         */
        public Services(String name, int port) {
            this.name = name;
            this.port = port;
        }

        public Services() {
            super();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }

    public LocalstackSnsProperties getLocalstackSnsProperties() {
        return localstackSnsProperties;
    }

    public void setLocalstackSnsProperties(LocalstackSnsProperties localstackSnsProperties) {
        this.localstackSnsProperties = localstackSnsProperties;
    }

    public LocalstackSqsProperties getLocalstackSqsProperties() {
        return localstackSqsProperties;
    }

    public void setLocalstackSqsProperties(LocalstackSqsProperties localstackSqsProperties) {
        this.localstackSqsProperties = localstackSqsProperties;
    }

    public LocalstackS3Properties getLocalstackS3Properties() {
        return localstackS3Properties;
    }

    public void setLocalstackS3Properties(LocalstackS3Properties localstackS3Properties) {
        this.localstackS3Properties = localstackS3Properties;
    }
}
