package com.wynd.vop.framework.localstack.autoconfigure;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({LocalstackAutoConfigurationImpl.class,
        LocalstackInitializerConfiguration.class})
public class LocalstackAutoConfiguration {
}
