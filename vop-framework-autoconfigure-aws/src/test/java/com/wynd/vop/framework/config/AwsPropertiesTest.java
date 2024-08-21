package com.wynd.vop.framework.config;

import junit.framework.TestCase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AwsPropertiesTest {

    @Test
    public void testSetSecretKey() {
        AwsProperties instance = new AwsProperties();
        instance.setSecretKey("secretKey");
        assertEquals("secretKey", instance.getSecretKey());
    }

    @Test
    public void testSetAccessKey() {
        AwsProperties instance = new AwsProperties();
        instance.setAccessKey("accessKey");
        assertEquals("accessKey", instance.getAccessKey());
    }

    @Test
    public void testSetEnabled() {
        AwsProperties instance = new AwsProperties();
        instance.setEnabled(true);
        TestCase.assertTrue(instance.isEnabled());
    }
}
