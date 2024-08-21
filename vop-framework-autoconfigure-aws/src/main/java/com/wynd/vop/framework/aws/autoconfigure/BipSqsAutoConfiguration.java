package com.wynd.vop.framework.aws.autoconfigure;

import com.wynd.vop.framework.sqs.config.DynamicSqsBeanConfig;
import com.wynd.vop.framework.sqs.config.SqsProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(SqsProperties.class)
@ConditionalOnProperty(name = "vop.framework.aws.sqs.enabled", havingValue = "true")
@Import({DynamicSqsBeanConfig.class})
public class BipSqsAutoConfiguration {

}
