package com.wynd.vop.framework.localstack.s3.config;

import com.wynd.vop.framework.localstack.autoconfigure.LocalstackServiceProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Component
@ConfigurationProperties(prefix = "vop.framework.localstack.services.s3")
@ConditionalOnProperty(value = "vop.framework.localstack.enabled")
public class LocalstackS3Properties implements LocalstackServiceProperties {

    // Value annotations here are default values unless overridden by values under localstack.services.s3
    @Min(1025)
    @Max(65536)
    @Value("4572")
    int port;

    @Value("false")
    boolean enabled;

    public void setPort(int port) {
        this.port = port;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public int getPort() {
        return port;
    }
}
