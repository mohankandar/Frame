package com.wynd.vop.framework.sqs.services;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.*;
import com.wynd.vop.framework.sqs.config.SqsProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.UnsupportedOperationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = StandardSqsService.class)
public class StandardSqsServiceTest {

	private static final String TEST_STRING = "A Test String";
	private static final Region TEST_REGION = Region.getRegion(Regions.GovCloud);
	private static final List<String> TEST_STRING_LIST = new ArrayList<>();

	private StandardSqsService instance;

	@MockBean
	SqsProperties mockSqsProperties;

	@Mock
	AmazonSQS mockAmazonSqsClient;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);

		instance = new StandardSqsService();
		instance.setWrappedClient(mockAmazonSqsClient);
	}

	@Test
	public void testConstructor() {
		assertNotNull(instance);
	}

	@Test
	public void testSetEndpoint() {
		try {
			instance.setEndpoint(TEST_STRING);
			fail();
		} catch(UnsupportedOperationException e) {
			assertNotNull(e);
			assertNotNull(e.getMessage());
		}
	}

	@Test
	public void testSetRegion() {
		try {
			instance.setRegion(TEST_REGION);
			fail();
		} catch(UnsupportedOperationException e) {
			assertNotNull(e);
			assertNotNull(e.getMessage());
		}
	}

	@Test
	public void testAddPermission() {
		AddPermissionRequest request = new AddPermissionRequest();

		instance.addPermission(request);

		Mockito.verify(mockAmazonSqsClient, times(1)).addPermission(request);
	}

	@Test
	public void testAddPermission_2() {
		instance.addPermission(TEST_STRING, TEST_STRING, TEST_STRING_LIST, TEST_STRING_LIST);

		Mockito.verify(mockAmazonSqsClient, times(1)).addPermission(TEST_STRING, TEST_STRING, TEST_STRING_LIST, TEST_STRING_LIST);
	}

	@Test
	public void testChangeMessageVisibility() {
		ChangeMessageVisibilityRequest request = new ChangeMessageVisibilityRequest();

		instance.changeMessageVisibility(request);

		Mockito.verify(mockAmazonSqsClient, times(1)).changeMessageVisibility(request);
	}

	@Test
	public void testChangeMessageVisibility_2() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;
		Integer i = 0;

		instance.changeMessageVisibility(s, s1, i);

		Mockito.verify(mockAmazonSqsClient, times(1)).changeMessageVisibility(s, s1, i);
	}

	@Test
	public void testChangeMessageVisibilityBatch() {
		ChangeMessageVisibilityBatchRequest request = new ChangeMessageVisibilityBatchRequest();

		instance.changeMessageVisibilityBatch(request);

		Mockito.verify(mockAmazonSqsClient, times(1)).changeMessageVisibilityBatch(request);
	}

	@Test
	public void testChangeMessageVisibilityBatch_2() {
		String s = TEST_STRING;
		List<ChangeMessageVisibilityBatchRequestEntry> list = new ArrayList<>();

		instance.changeMessageVisibilityBatch(s, list);

		Mockito.verify(mockAmazonSqsClient, times(1)).changeMessageVisibilityBatch(s, list);
	}

	@Test
	public void testCreateQueue() {
		CreateQueueRequest request = new CreateQueueRequest();

		instance.createQueue(request);

		Mockito.verify(mockAmazonSqsClient, times(1)).createQueue(request);
	}

	@Test
	public void testCreateQueue_2() {
		instance.createQueue(TEST_STRING);

		Mockito.verify(mockAmazonSqsClient, times(1)).createQueue(TEST_STRING);
	}

	@Test
	public void testDeleteMessage() {
		DeleteMessageRequest request = new DeleteMessageRequest();

		instance.deleteMessage(request);

		Mockito.verify(mockAmazonSqsClient, times(1)).deleteMessage(request);
	}

	@Test
	public void testDeleteMessage_2() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;

		instance.deleteMessage(s, s1);

		Mockito.verify(mockAmazonSqsClient, times(1)).deleteMessage(s, s1);
	}

	@Test
	public void testDeleteMessageBatch() {
		DeleteMessageBatchRequest request = new DeleteMessageBatchRequest();

		instance.deleteMessageBatch(request);

		Mockito.verify(mockAmazonSqsClient, times(1)).deleteMessageBatch(request);
	}

	@Test
	public void testDeleteMessageBatch_2() {
		String s = TEST_STRING;
		List<DeleteMessageBatchRequestEntry> list = new ArrayList<>();

		instance.deleteMessageBatch(s, list);

		Mockito.verify(mockAmazonSqsClient, times(1)).deleteMessageBatch(s, list);
	}

	@Test
	public void testDeleteQueue() {
		DeleteQueueRequest request = new DeleteQueueRequest();

		instance.deleteQueue(request);

		Mockito.verify(mockAmazonSqsClient, times(1)).deleteQueue(request);
	}

	@Test
	public void testDeleteQueue_2() {
		instance.deleteQueue(TEST_STRING);

		Mockito.verify(mockAmazonSqsClient, times(1)).deleteQueue(TEST_STRING);
	}


	@Test
	public void testGetQueueAttributes() {
		GetQueueAttributesRequest request = new GetQueueAttributesRequest();

		instance.getQueueAttributes(request);

		Mockito.verify(mockAmazonSqsClient, times(1)).getQueueAttributes(request);
	}

	@Test
	public void testGetQueueAttributes_2() {
		List<String> list = new ArrayList<>();

		instance.getQueueAttributes(TEST_STRING, list);

		Mockito.verify(mockAmazonSqsClient, times(1)).getQueueAttributes(TEST_STRING, list);
	}

	@Test
	public void testGetQueueUrl() {
		GetQueueUrlRequest request = new GetQueueUrlRequest();

		instance.getQueueUrl(request);

		Mockito.verify(mockAmazonSqsClient, times(1)).getQueueUrl(request);
	}

	@Test
	public void testGetQueueUrl_2() {
		String s = TEST_STRING;

		instance.getQueueUrl(s);

		Mockito.verify(mockAmazonSqsClient, times(1)).getQueueUrl(s);
	}

	@Test
	public void testListDeadLetterSourceQueues() {
		ListDeadLetterSourceQueuesRequest request = new ListDeadLetterSourceQueuesRequest();

		instance.listDeadLetterSourceQueues(request);

		Mockito.verify(mockAmazonSqsClient, times(1)).listDeadLetterSourceQueues(request);
	}

	@Test
	public void testListQueueTags() {
		ListQueueTagsRequest request = new ListQueueTagsRequest();

		instance.listQueueTags(request);

		Mockito.verify(mockAmazonSqsClient, times(1)).listQueueTags(request);
	}

	@Test
	public void testListQueueTags_2() {
		String s = TEST_STRING;

		instance.listQueueTags(s);

		Mockito.verify(mockAmazonSqsClient, times(1)).listQueueTags(s);
	}

	@Test
	public void testListQueues() {
		ListQueuesRequest request = new ListQueuesRequest();

		instance.listQueues(request);

		Mockito.verify(mockAmazonSqsClient, times(1)).listQueues(request);
	}

	@Test
	public void testListQueues_2() {
		instance.listQueues();

		Mockito.verify(mockAmazonSqsClient, times(1)).listQueues();
	}

	@Test
	public void testListQueues_3() {
		instance.listQueues(TEST_STRING);

		Mockito.verify(mockAmazonSqsClient, times(1)).listQueues(TEST_STRING);
	}

	@Test
	public void testPurgeQueue() {
		PurgeQueueRequest request = new PurgeQueueRequest();

		instance.purgeQueue(request);

		Mockito.verify(mockAmazonSqsClient, times(1)).purgeQueue(request);
	}

	@Test
	public void testReceiveMessage() {
		ReceiveMessageRequest request = new ReceiveMessageRequest();

		instance.receiveMessage(request);

		Mockito.verify(mockAmazonSqsClient, times(1)).receiveMessage(request);
	}

	@Test
	public void testReceiveMessage_2() {
		String s = TEST_STRING;

		instance.receiveMessage(s);

		Mockito.verify(mockAmazonSqsClient, times(1)).receiveMessage(s);
	}

	@Test
	public void testRemovePermission() {
		RemovePermissionRequest request = new RemovePermissionRequest();

		instance.removePermission(request);

		Mockito.verify(mockAmazonSqsClient, times(1)).removePermission(request);
	}

	@Test
	public void testRemovePermission_2() {
		instance.removePermission(TEST_STRING, TEST_STRING);

		Mockito.verify(mockAmazonSqsClient, times(1)).removePermission(TEST_STRING, TEST_STRING);
	}


	@Test
	public void testSendMessage() {
		SendMessageRequest request = new SendMessageRequest();

		instance.sendMessage(request);

		Mockito.verify(mockAmazonSqsClient, times(1)).sendMessage(request);
	}

	@Test
	public void testSendMessage_2() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;

		instance.sendMessage(s, s1);

		Mockito.verify(mockAmazonSqsClient, times(1)).sendMessage(s, s1);
	}

	@Test
	public void testSendMessageBatch() {
		SendMessageBatchRequest request = new SendMessageBatchRequest();

		instance.sendMessageBatch(request);

		Mockito.verify(mockAmazonSqsClient, times(1)).sendMessageBatch(request);
	}

	@Test
	public void testSendMessageBatch_2() {
		String s = TEST_STRING;
		List<SendMessageBatchRequestEntry> list = new ArrayList<>();

		instance.sendMessageBatch(s, list);

		Mockito.verify(mockAmazonSqsClient, times(1)).sendMessageBatch(s, list);
	}

	@Test
	public void testSetQueueAttributes() {
		SetQueueAttributesRequest request = new SetQueueAttributesRequest();

		instance.setQueueAttributes(request);

		Mockito.verify(mockAmazonSqsClient, times(1)).setQueueAttributes(request);
	}

	@Test
	public void testSetQueueAttributes_2() {
		Map<String, String> map = new HashMap<>();

		instance.setQueueAttributes(TEST_STRING, map);

		Mockito.verify(mockAmazonSqsClient, times(1)).setQueueAttributes(TEST_STRING, map);
	}

	@Test
	public void testTagQueue() {
		TagQueueRequest request = new TagQueueRequest();

		instance.tagQueue(request);

		Mockito.verify(mockAmazonSqsClient, times(1)).tagQueue(request);
	}

	@Test
	public void testTagQueue_2() {
		Map<String, String> map = new HashMap<>();

		instance.tagQueue(TEST_STRING, map);

		Mockito.verify(mockAmazonSqsClient, times(1)).tagQueue(TEST_STRING, map);
	}

	@Test
	public void testUntagQueue() {
		UntagQueueRequest request = new UntagQueueRequest();

		instance.untagQueue(request);

		Mockito.verify(mockAmazonSqsClient, times(1)).untagQueue(request);
	}

	@Test
	public void testUntagQueue_2() {
		List<String> list = new ArrayList<>();

		instance.untagQueue(TEST_STRING, list);

		Mockito.verify(mockAmazonSqsClient, times(1)).untagQueue(TEST_STRING, list);
	}

	@Test
	public void testShutdown() {
		instance.shutdown();

		Mockito.verify(mockAmazonSqsClient, times(1)).shutdown();
	}

	@Test
	public void testGetCachedResponseMetadata() {
		AmazonWebServiceRequest request = new AmazonWebServiceRequest() {};

		instance.getCachedResponseMetadata(request);

		Mockito.verify(mockAmazonSqsClient, times(1)).getCachedResponseMetadata(request);
	}

	@Test
	public void testGetWrappedClient() {
		AmazonSQS result = instance.getWrappedClient();

		assertEquals(mockAmazonSqsClient, result);
	}
}
