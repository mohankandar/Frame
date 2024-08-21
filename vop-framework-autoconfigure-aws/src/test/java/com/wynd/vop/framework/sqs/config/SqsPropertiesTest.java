package com.wynd.vop.framework.sqs.config;

import com.wynd.vop.framework.log.BipLogger;
import com.wynd.vop.framework.log.BipLoggerFactory;
import com.wynd.vop.framework.sns.config.SnsProperties;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SqsPropertiesTest {

    //Test SQS Logger
    @Test
    public void testLogger() {
        BipLogger logger = BipLoggerFactory.getLogger(SnsProperties.class);
        SqsProperties instance = new SqsProperties();
        instance.setLogger(logger);

        assertEquals(Optional.of(instance.getLogger()), Optional.ofNullable(logger));
    }

    //Test SQS Property enabled
    @Test
    public void testBoolean() {
        boolean enabled = true;
        SqsProperties instance = new SqsProperties();
        instance.setEnabled(enabled);

        assertEquals(Optional.of(instance.isEnabled()), Optional.ofNullable(enabled));
    }

    //Test SQS Property name
    @Test
    public void testQueues() {
        List<SqsProperties.SqsQueue> sqsQueues = new ArrayList<>();
        SqsProperties instance = new SqsProperties();
        instance.setQueues(sqsQueues);

        assertEquals(Optional.of(instance.getQueues()), Optional.ofNullable(sqsQueues));
    }

    //Test SQS Property type
    @Test
    public void testQueueName() {
        String name = "String";
        SqsProperties.SqsQueue instance = new SqsProperties.SqsQueue();
        instance.setName(name);

        assertEquals(Optional.of(instance.getName()), Optional.ofNullable(name));
    }

    @Test
    public void testGetQueueByName() {
        String name = "String";

        List<SqsProperties.SqsQueue> queueList = new ArrayList<>();
        SqsProperties.SqsQueue testQueue = new SqsProperties.SqsQueue();
        testQueue.setName(name);
        queueList.add(testQueue);

        SqsProperties instance = new SqsProperties();
        instance.setQueues(queueList);

        SqsProperties.SqsQueue result = instance.getQueueByName(name);

        assertNotNull(result);
        assertEquals(testQueue, result);
    }

    @Test
    public void testGetQueueByName_null() {
        String name = "String";

        List<SqsProperties.SqsQueue> queueList = new ArrayList<>();
        SqsProperties.SqsQueue testQueue = new SqsProperties.SqsQueue();
        testQueue.setName(name);
        queueList.add(testQueue);

        SqsProperties instance = new SqsProperties();
        instance.setQueues(queueList);

        SqsProperties.SqsQueue result = instance.getQueueByName(null);

        assertNull(result);
    }

    @Test
    public void testGetQueueById() {
        String id = "String";

        List<SqsProperties.SqsQueue> queueList = new ArrayList<>();
        SqsProperties.SqsQueue testQueue = new SqsProperties.SqsQueue();
        testQueue.setId(id);
        queueList.add(testQueue);

        SqsProperties instance = new SqsProperties();
        instance.setQueues(queueList);

        SqsProperties.SqsQueue result = instance.getQueueById(id);

        assertNotNull(result);
        assertEquals(testQueue, result);
    }

    @Test
    public void testGetQueueById_null() {
        String id = "String";

        List<SqsProperties.SqsQueue> queueList = new ArrayList<>();
        SqsProperties.SqsQueue testQueue = new SqsProperties.SqsQueue();
        testQueue.setId(id);
        queueList.add(testQueue);

        SqsProperties instance = new SqsProperties();
        instance.setQueues(queueList);

        SqsProperties.SqsQueue result = instance.getQueueById(null);

        assertNull(result);
    }
}