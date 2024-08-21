package com.wynd.vop.framework.s3.config;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DynamicS3BeanConfig.class, initializers = ConfigDataApplicationContextInitializer.class)
@TestPropertySource(properties = { "spring.config.location = classpath:test_s3_application.yml"})
public class DynamicS3BeanConfigTest {

    @Autowired
    DynamicS3BeanConfig instance;

    @MockBean
    S3Properties mockS3Properties;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testBeanPostProcessor_Creation() {
        assertNotNull(instance);
    }
}