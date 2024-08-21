package com.wynd.vop.framework.sqs.config;

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
@ContextConfiguration(classes = DynamicSqsBeanConfig.class, initializers = ConfigDataApplicationContextInitializer.class)
@TestPropertySource(properties = { "spring.config.location = classpath:test_sqs_application.yml"})
public class DynamicSqsBeanConfigTest {

    @Autowired
    DynamicSqsBeanConfig instance;

    @MockBean
    SqsProperties mockSqsProperties;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testBeanPostProcessor_Creation() {
        assertNotNull(instance);
    }
}