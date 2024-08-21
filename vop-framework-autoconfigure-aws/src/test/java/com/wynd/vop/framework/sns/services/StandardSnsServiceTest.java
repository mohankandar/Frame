package com.wynd.vop.framework.sns.services;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.*;
import com.wynd.vop.framework.sns.config.SnsProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = StandardSnsService.class)
public class StandardSnsServiceTest {

	private static final String TEST_STRING = "A Test String";
	private static final Region TEST_REGION = Region.getRegion(Regions.GovCloud);
	private static final List<String> TEST_STRING_LIST = new ArrayList<>();

	private StandardSnsService instance;

	@MockBean
	SnsProperties mockSnsProperties;

	@Mock
	AmazonSNS mockAmazonSnsClient;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);

		instance = new StandardSnsService();
		instance.setWrappedClient(mockAmazonSnsClient);
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

		Mockito.verify(mockAmazonSnsClient, times(1)).addPermission(request);
	}

	@Test
	public void testAddPermission_2() {
		instance.addPermission(TEST_STRING, TEST_STRING, TEST_STRING_LIST, TEST_STRING_LIST);

		Mockito.verify(mockAmazonSnsClient, times(1)).addPermission(TEST_STRING, TEST_STRING, TEST_STRING_LIST, TEST_STRING_LIST);
	}

	@Test
	public void testCheckIfPhoneNumberIsOptedOut() {
		CheckIfPhoneNumberIsOptedOutRequest request = new CheckIfPhoneNumberIsOptedOutRequest();

		instance.checkIfPhoneNumberIsOptedOut(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).checkIfPhoneNumberIsOptedOut(request);
	}

	@Test
	public void testConfirmSubscription() {
		ConfirmSubscriptionRequest request = new ConfirmSubscriptionRequest();

		instance.confirmSubscription(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).confirmSubscription(request);
	}
	
	@Test
	public void testConfirmSubscription_2() {
		instance.confirmSubscription(TEST_STRING, TEST_STRING);

		Mockito.verify(mockAmazonSnsClient, times(1)).confirmSubscription(TEST_STRING, TEST_STRING);
	}

	@Test
	public void testConfirmSubscription_3() {
		instance.confirmSubscription(TEST_STRING, TEST_STRING, TEST_STRING);

		Mockito.verify(mockAmazonSnsClient, times(1)).confirmSubscription(TEST_STRING, TEST_STRING, TEST_STRING);
	}

	@Test
	public void testCreatePlatformApplication() {
		CreatePlatformApplicationRequest request = new CreatePlatformApplicationRequest();

		instance.createPlatformApplication(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).createPlatformApplication(request);
	}

	@Test
	public void testCreatePlatformEndpoint() {
		CreatePlatformEndpointRequest request = new CreatePlatformEndpointRequest();

		instance.createPlatformEndpoint(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).createPlatformEndpoint(request);
	}

	@Test
	public void testCreateTopic() {
		CreateTopicRequest request = new CreateTopicRequest();

		instance.createTopic(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).createTopic(request);
	}

	@Test
	public void testCreateTopic_2() {
		instance.createTopic(TEST_STRING);

		Mockito.verify(mockAmazonSnsClient, times(1)).createTopic(TEST_STRING);
	}

	@Test
	public void testDeleteEndpoint() {
		DeleteEndpointRequest request = new DeleteEndpointRequest();

		instance.deleteEndpoint(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).deleteEndpoint(request);
	}

	@Test
	public void testDeletePlatformApplication() {
		DeletePlatformApplicationRequest request = new DeletePlatformApplicationRequest();

		instance.deletePlatformApplication(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).deletePlatformApplication(request);
	}

	@Test
	public void testDeleteTopic() {
		DeleteTopicRequest request = new DeleteTopicRequest();

		instance.deleteTopic(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).deleteTopic(request);
	}

	@Test
	public void testDeleteTopic_2() {
		instance.deleteTopic(TEST_STRING);

		Mockito.verify(mockAmazonSnsClient, times(1)).deleteTopic(TEST_STRING);
	}

	@Test
	public void testGetEndpointAttributes() {
		GetEndpointAttributesRequest request = new GetEndpointAttributesRequest();

		instance.getEndpointAttributes(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).getEndpointAttributes(request);
	}

	@Test
	public void testGetPlatformApplicationAttributes() {
		GetPlatformApplicationAttributesRequest request = new GetPlatformApplicationAttributesRequest();

		instance.getPlatformApplicationAttributes(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).getPlatformApplicationAttributes(request);
	}

	@Test
	public void testGetSMSAttributes() {
		GetSMSAttributesRequest request = new GetSMSAttributesRequest();

		instance.getSMSAttributes(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).getSMSAttributes(request);
	}

	@Test
	public void testGetSubscriptionAttributes() {
		GetSubscriptionAttributesRequest request = new GetSubscriptionAttributesRequest();

		instance.getSubscriptionAttributes(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).getSubscriptionAttributes(request);
	}

	@Test
	public void testGetSubscriptionAttributes_2() {
		instance.getSubscriptionAttributes(TEST_STRING);

		Mockito.verify(mockAmazonSnsClient, times(1)).getSubscriptionAttributes(TEST_STRING);
	}

	@Test
	public void testGetTopicAttributes() {
		GetTopicAttributesRequest request = new GetTopicAttributesRequest();

		instance.getTopicAttributes(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).getTopicAttributes(request);
	}

	@Test
	public void testGetTopicAttributes_2() {
		instance.getTopicAttributes(TEST_STRING);

		Mockito.verify(mockAmazonSnsClient, times(1)).getTopicAttributes(TEST_STRING);
	}

	@Test
	public void testListEndpointsByPlatformApplication() {
		ListEndpointsByPlatformApplicationRequest request = new ListEndpointsByPlatformApplicationRequest();

		instance.listEndpointsByPlatformApplication(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).listEndpointsByPlatformApplication(request);
	}

	@Test
	public void testListPhoneNumbersOptedOut() {
		ListPhoneNumbersOptedOutRequest request = new ListPhoneNumbersOptedOutRequest();

		instance.listPhoneNumbersOptedOut(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).listPhoneNumbersOptedOut(request);
	}

	@Test
	public void testListPlatformApplications() {
		ListPlatformApplicationsRequest request = new ListPlatformApplicationsRequest();

		instance.listPlatformApplications(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).listPlatformApplications(request);
	}

	@Test
	public void testListPlatformApplications_2() {
		instance.listPlatformApplications();

		Mockito.verify(mockAmazonSnsClient, times(1)).listPlatformApplications();
	}

	@Test
	public void testListSubscriptions() {
		ListSubscriptionsRequest request = new ListSubscriptionsRequest();

		instance.listSubscriptions(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).listSubscriptions(request);
	}

	@Test
	public void testListSubscriptions_2() {
		instance.listSubscriptions();

		Mockito.verify(mockAmazonSnsClient, times(1)).listSubscriptions();
	}

	@Test
	public void testListSubscriptions_3() {
		instance.listSubscriptions(TEST_STRING);

		Mockito.verify(mockAmazonSnsClient, times(1)).listSubscriptions(TEST_STRING);
	}

	@Test
	public void testListSubscriptionsByTopic() {
		ListSubscriptionsByTopicRequest request = new ListSubscriptionsByTopicRequest();

		instance.listSubscriptionsByTopic(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).listSubscriptionsByTopic(request);
	}

	@Test
	public void testListSubscriptionsByTopic_2() {
		instance.listSubscriptionsByTopic(TEST_STRING);

		Mockito.verify(mockAmazonSnsClient, times(1)).listSubscriptionsByTopic(TEST_STRING);
	}

	@Test
	public void testListSubscriptionsByTopic_3() {
		instance.listSubscriptionsByTopic(TEST_STRING, TEST_STRING);

		Mockito.verify(mockAmazonSnsClient, times(1)).listSubscriptionsByTopic(TEST_STRING, TEST_STRING);
	}

	@Test
	public void testListTopics() {
		ListTopicsRequest request = new ListTopicsRequest();

		instance.listTopics(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).listTopics(request);
	}

	@Test
	public void testListTopics_2() {
		instance.listTopics();

		Mockito.verify(mockAmazonSnsClient, times(1)).listTopics();
	}

	@Test
	public void testListTopics_3() {
		instance.listTopics(TEST_STRING);

		Mockito.verify(mockAmazonSnsClient, times(1)).listTopics(TEST_STRING);
	}

	@Test
	public void testOptInPhoneNumber() {
		OptInPhoneNumberRequest request = new OptInPhoneNumberRequest();

		instance.optInPhoneNumber(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).optInPhoneNumber(request);
	}

	@Test
	public void createSMSSandboxPhoneNumber(){
		CreateSMSSandboxPhoneNumberRequest createSMSSandboxPhoneNumberRequest = new CreateSMSSandboxPhoneNumberRequest();
		instance.createSMSSandboxPhoneNumber(createSMSSandboxPhoneNumberRequest);
		Mockito.verify(mockAmazonSnsClient, times(1)).createSMSSandboxPhoneNumber(createSMSSandboxPhoneNumberRequest);
	}

	@Test
	public void verifySMSSandboxPhoneNumber(){
		VerifySMSSandboxPhoneNumberRequest verifySMSSandboxPhoneNumberRequest = new VerifySMSSandboxPhoneNumberRequest();
		instance.verifySMSSandboxPhoneNumber(verifySMSSandboxPhoneNumberRequest);
		Mockito.verify(mockAmazonSnsClient, times(1)).verifySMSSandboxPhoneNumber(verifySMSSandboxPhoneNumberRequest);
	}

	@Test
	public void getSMSSandboxAccountStatus(){
		GetSMSSandboxAccountStatusRequest getSMSSandboxAccountStatusRequest = new GetSMSSandboxAccountStatusRequest();
		instance.getSMSSandboxAccountStatus(getSMSSandboxAccountStatusRequest);
		Mockito.verify(mockAmazonSnsClient, times(1)).getSMSSandboxAccountStatus(getSMSSandboxAccountStatusRequest);
	}

	@Test
	public void listSMSSandboxPhoneNumbers(){
		ListSMSSandboxPhoneNumbersRequest listSMSSandboxPhoneNumbersRequest = new ListSMSSandboxPhoneNumbersRequest();
		instance.listSMSSandboxPhoneNumbers(listSMSSandboxPhoneNumbersRequest);
		Mockito.verify(mockAmazonSnsClient, times(1)).listSMSSandboxPhoneNumbers(listSMSSandboxPhoneNumbersRequest);
	}

	@Test
	public void deleteSMSSandboxPhoneNumber(){
		DeleteSMSSandboxPhoneNumberRequest deleteSMSSandboxPhoneNumberRequest = new DeleteSMSSandboxPhoneNumberRequest();
		instance.deleteSMSSandboxPhoneNumber(deleteSMSSandboxPhoneNumberRequest);
		Mockito.verify(mockAmazonSnsClient, times(1)).deleteSMSSandboxPhoneNumber(deleteSMSSandboxPhoneNumberRequest);
	}

	@Test
	public void listOriginationNumbers(){
		ListOriginationNumbersRequest listOriginationNumbersRequest = new ListOriginationNumbersRequest();
		instance.listOriginationNumbers(listOriginationNumbersRequest);
		Mockito.verify(mockAmazonSnsClient, times(1)).listOriginationNumbers(listOriginationNumbersRequest);
	}

	@Test
	public void publishBatch(){
		PublishBatchRequest publishBatchRequest = new PublishBatchRequest();
		instance.publishBatch(publishBatchRequest);
		Mockito.verify(mockAmazonSnsClient, times(1)).publishBatch(publishBatchRequest);
	}


	@Test
	public void testPublish() {
		PublishRequest request = new PublishRequest();

		instance.publish(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).publish(request);
	}

	@Test
	public void testPublish_2() {
		instance.publish(TEST_STRING, TEST_STRING);

		Mockito.verify(mockAmazonSnsClient, times(1)).publish(TEST_STRING, TEST_STRING);
	}

	@Test
	public void testPublish_3() {
		instance.publish(TEST_STRING, TEST_STRING, TEST_STRING);

		Mockito.verify(mockAmazonSnsClient, times(1)).publish(TEST_STRING, TEST_STRING, TEST_STRING);
	}

	@Test
	public void testRemovePermission() {
		RemovePermissionRequest request = new RemovePermissionRequest();

		instance.removePermission(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).removePermission(request);
	}

	@Test
	public void testRemovePermission_2() {
		instance.removePermission(TEST_STRING, TEST_STRING);

		Mockito.verify(mockAmazonSnsClient, times(1)).removePermission(TEST_STRING, TEST_STRING);
	}

	@Test
	public void testSetEndpointAttributes() {
		SetEndpointAttributesRequest request = new SetEndpointAttributesRequest();

		instance.setEndpointAttributes(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).setEndpointAttributes(request);
	}

	@Test
	public void testSetPlatformApplicationAttributes() {
		SetPlatformApplicationAttributesRequest request = new SetPlatformApplicationAttributesRequest();

		instance.setPlatformApplicationAttributes(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).setPlatformApplicationAttributes(request);
	}

	@Test
	public void testSetSMSAttributes() {
		SetSMSAttributesRequest request = new SetSMSAttributesRequest();

		instance.setSMSAttributes(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).setSMSAttributes(request);
	}

	@Test
	public void testSetSubscriptionAttributes() {
		SetSubscriptionAttributesRequest request = new SetSubscriptionAttributesRequest();

		instance.setSubscriptionAttributes(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).setSubscriptionAttributes(request);
	}

	@Test
	public void testSetSubscriptionAttributes_2() {
		instance.setSubscriptionAttributes(TEST_STRING, TEST_STRING, TEST_STRING);

		Mockito.verify(mockAmazonSnsClient, times(1)).setSubscriptionAttributes(TEST_STRING, TEST_STRING, TEST_STRING);
	}

	@Test
	public void testSetTopicAttributes() {
		SetTopicAttributesRequest request = new SetTopicAttributesRequest();

		instance.setTopicAttributes(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).setTopicAttributes(request);
	}

	@Test
	public void testSetTopicAttributes_2() {
		instance.setTopicAttributes(TEST_STRING, TEST_STRING, TEST_STRING);

		Mockito.verify(mockAmazonSnsClient, times(1)).setTopicAttributes(TEST_STRING, TEST_STRING, TEST_STRING);
	}

	@Test
	public void testSubscribe() {
		SubscribeRequest request = new SubscribeRequest();

		instance.subscribe(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).subscribe(request);
	}

	@Test
	public void testSubscribe_2() {
		instance.subscribe(TEST_STRING, TEST_STRING, TEST_STRING);

		Mockito.verify(mockAmazonSnsClient, times(1)).subscribe(TEST_STRING, TEST_STRING, TEST_STRING);
	}

	@Test
	public void testUnsubscribe() {
		UnsubscribeRequest request = new UnsubscribeRequest();

		instance.unsubscribe(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).unsubscribe(request);
	}

	@Test
	public void testUnsubscribe_2() {
		instance.unsubscribe(TEST_STRING);

		Mockito.verify(mockAmazonSnsClient, times(1)).unsubscribe(TEST_STRING);
	}

	@Test
	public void testShutdown() {
		instance.shutdown();

		Mockito.verify(mockAmazonSnsClient, times(1)).shutdown();
	}

	@Test
	public void testGetCachedResponseMetadata() {
		AmazonWebServiceRequest request = new AmazonWebServiceRequest() {};

		instance.getCachedResponseMetadata(request);

		Mockito.verify(mockAmazonSnsClient, times(1)).getCachedResponseMetadata(request);
	}

	@Test
	public void testGetWrappedClient() {
		AmazonSNS result = instance.getWrappedClient();

		assertEquals(mockAmazonSnsClient, result);
	}
}
