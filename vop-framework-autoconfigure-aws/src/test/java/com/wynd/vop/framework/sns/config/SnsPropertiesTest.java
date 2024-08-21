package com.wynd.vop.framework.sns.config;

import com.amazonaws.regions.Regions;
import com.wynd.vop.framework.log.BipLogger;
import com.wynd.vop.framework.log.BipLoggerFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SnsPropertiesTest {

    SnsProperties propsInstance;
    SnsProperties.SnsTopic topicInstance;

    BipLogger expectedLogger = BipLoggerFactory.getLogger(SnsProperties.class);
    String expectedString = "expectedString";
    String expectedEndpoint = "http://localhost:4575/topic/test_my_topic";
    String expectedBaseUrl = "http://localhost:4575";
    String expectedRegionsName = Regions.GovCloud.getName();
    Regions expectedRegions = Regions.GovCloud;

    @Before
    public void before() {
        propsInstance = new SnsProperties();
        topicInstance = new SnsProperties.SnsTopic();

        List<SnsProperties.SnsTopic> topicList = new ArrayList<>();
        topicList.add(topicInstance);
        propsInstance.setTopics(topicList);
    }

    @Test
    public void testGetLogger() {
        propsInstance.setLogger(expectedLogger);

        assertEquals(Optional.ofNullable(expectedLogger), Optional.of(propsInstance.getLogger()));
    }

    @Test
    public void testGetTopics() {
        assertNotNull(propsInstance.getTopics());
        assertEquals(topicInstance, propsInstance.getTopics().get(0));
    }

    @Test
    public void testId() {
        topicInstance.setId(expectedString);

        assertEquals(expectedString, topicInstance.getId());
    }

    @Test
    public void testName() {
        topicInstance.setName(expectedString);

        assertEquals(expectedString, topicInstance.getName());
    }

    @Test
    public void testType() {
        topicInstance.setType(expectedString);

        assertEquals(expectedString, topicInstance.getType());
    }

    @Test
    public void testTopic() {
        topicInstance.setTopic(expectedString);

        assertEquals(expectedString, topicInstance.getTopic());
    }

    @Test
    public void testTopicArn() {
        topicInstance.setTopicArn(expectedString);

        assertEquals(expectedString, topicInstance.getTopicArn());
    }

    @Test
    public void testGetTopicByName() {
        String name = "String";

        List<SnsProperties.SnsTopic> topicList = new ArrayList<>();
        SnsProperties.SnsTopic testTopic = new SnsProperties.SnsTopic();
        testTopic.setName(name);
        topicList.add(testTopic);

        SnsProperties instance = new SnsProperties();
        instance.setTopics(topicList);

        SnsProperties.SnsTopic result = instance.getTopicByName(name);

        assertNotNull(result);
        assertEquals(testTopic, result);
    }

    @Test
    public void testGetTopicByName_null() {
        String name = "String";

        List<SnsProperties.SnsTopic> topicList = new ArrayList<>();
        SnsProperties.SnsTopic testTopic = new SnsProperties.SnsTopic();
        testTopic.setName(name);
        topicList.add(testTopic);

        SnsProperties instance = new SnsProperties();
        instance.setTopics(topicList);

        SnsProperties.SnsTopic result = instance.getTopicByName(null);

        assertNull(result);
    }

    @Test
    public void testGetTopicById() {
        String id = "String";

        List<SnsProperties.SnsTopic> topicList = new ArrayList<>();
        SnsProperties.SnsTopic testTopic = new SnsProperties.SnsTopic();
        testTopic.setId(id);
        topicList.add(testTopic);

        SnsProperties instance = new SnsProperties();
        instance.setTopics(topicList);

        SnsProperties.SnsTopic result = instance.getTopicById(id);

        assertNotNull(result);
        assertEquals(testTopic, result);
    }

    @Test
    public void testGetTopicById_null() {
        String id = "String";

        List<SnsProperties.SnsTopic> topicList = new ArrayList<>();
        SnsProperties.SnsTopic testTopic = new SnsProperties.SnsTopic();
        testTopic.setId(id);
        topicList.add(testTopic);

        SnsProperties instance = new SnsProperties();
        instance.setTopics(topicList);

        SnsProperties.SnsTopic result = instance.getTopicById(null);

        assertNull(result);
    }
}