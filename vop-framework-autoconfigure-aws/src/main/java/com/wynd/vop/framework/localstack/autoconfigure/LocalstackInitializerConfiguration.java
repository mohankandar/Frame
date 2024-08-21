package com.wynd.vop.framework.localstack.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LocalstackInitializerConfiguration {

    protected LocalstackInitializerConfiguration() {
        // Empty, do nothing.
    }

    @Bean
    public static LocalstackInitialized localstackInitialized(){
        return new LocalstackInitialized();
    }

    public static class LocalstackInitialized {
        @Autowired(required = false)
        LocalstackAutoConfigurationImpl localstackAutoConfigurationImpl = null;
    }
}
