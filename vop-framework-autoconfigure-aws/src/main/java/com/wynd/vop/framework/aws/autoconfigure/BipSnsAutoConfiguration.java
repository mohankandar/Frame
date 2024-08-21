package com.wynd.vop.framework.aws.autoconfigure;

import com.wynd.vop.framework.sns.config.DynamicSnsBeanConfig;
import com.wynd.vop.framework.sns.config.SnsProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(SnsProperties.class)
@ConditionalOnProperty(name = "vop.framework.aws.sns.enabled", havingValue = "true")
@Import({DynamicSnsBeanConfig.class})
public class BipSnsAutoConfiguration {

}
