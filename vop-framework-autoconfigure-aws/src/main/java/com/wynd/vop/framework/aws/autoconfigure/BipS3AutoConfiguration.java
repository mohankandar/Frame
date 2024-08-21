package com.wynd.vop.framework.aws.autoconfigure;

import com.wynd.vop.framework.s3.config.DynamicS3BeanConfig;
import com.wynd.vop.framework.s3.config.S3Properties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(S3Properties.class)
@ConditionalOnProperty(name = "vop.framework.aws.s3.enabled", havingValue = "true")
@Import({DynamicS3BeanConfig.class})
public class BipS3AutoConfiguration {

}
