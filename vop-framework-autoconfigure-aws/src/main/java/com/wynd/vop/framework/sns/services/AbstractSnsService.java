package com.wynd.vop.framework.sns.services;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.regions.Region;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.*;
import com.wynd.vop.framework.sns.config.SnsProperties;

import java.util.List;

/**
 * This class is intended as a wrapper for an AmazonSNS Service
 */
public abstract class AbstractSnsService implements AmazonSNS {

    AmazonSNS amazonSnsClient;
    SnsProperties.SnsTopic relatedTopic;

    @Override
    public void setEndpoint(String s) {
        throw new UnsupportedOperationException("The SNS Service method setEndpoint is deprecated and should not be " +
                "used. See Amazon SNS API documentation for more information. [AmazonSNS.setEndpoint(String s);]");
    }

    @Override
    public void setRegion(Region region) {
        throw new UnsupportedOperationException("The SNS Service method setRegion is deprecated and should not be " +
                "used. See Amazon SNS API documentation for more information. [AmazonSNS.setRegion(Region region);]");
    }

    @Override
    public AddPermissionResult addPermission(AddPermissionRequest addPermissionRequest) {
        return amazonSnsClient.addPermission(addPermissionRequest);
    }

    @Override
    public AddPermissionResult addPermission(String s, String s1, List<String> list, List<String> list1) {
        return amazonSnsClient.addPermission(s, s1, list, list1);
    }

    @Override
    public CheckIfPhoneNumberIsOptedOutResult checkIfPhoneNumberIsOptedOut(CheckIfPhoneNumberIsOptedOutRequest checkIfPhoneNumberIsOptedOutRequest) {
        return amazonSnsClient.checkIfPhoneNumberIsOptedOut(checkIfPhoneNumberIsOptedOutRequest);
    }

    @Override
    public ConfirmSubscriptionResult confirmSubscription(ConfirmSubscriptionRequest confirmSubscriptionRequest) {
        return amazonSnsClient.confirmSubscription(confirmSubscriptionRequest);
    }

    @Override
    public ConfirmSubscriptionResult confirmSubscription(String s, String s1, String s2) {
        return amazonSnsClient.confirmSubscription(s, s1, s2);
    }

    @Override
    public ConfirmSubscriptionResult confirmSubscription(String s, String s1) {
        return amazonSnsClient.confirmSubscription(s, s1);
    }

    @Override
    public CreatePlatformApplicationResult createPlatformApplication(CreatePlatformApplicationRequest createPlatformApplicationRequest) {
        return amazonSnsClient.createPlatformApplication(createPlatformApplicationRequest);
    }

    @Override
    public CreatePlatformEndpointResult createPlatformEndpoint(CreatePlatformEndpointRequest createPlatformEndpointRequest) {
        return amazonSnsClient.createPlatformEndpoint(createPlatformEndpointRequest);
    }

    @Override
    public CreateTopicResult createTopic(CreateTopicRequest createTopicRequest) {
        return amazonSnsClient.createTopic(createTopicRequest);
    }

    @Override
    public CreateTopicResult createTopic(String s) {
        return amazonSnsClient.createTopic(s);
    }

    @Override
    public DeleteEndpointResult deleteEndpoint(DeleteEndpointRequest deleteEndpointRequest) {
        return amazonSnsClient.deleteEndpoint(deleteEndpointRequest);
    }

    @Override
    public DeletePlatformApplicationResult deletePlatformApplication(DeletePlatformApplicationRequest deletePlatformApplicationRequest) {
        return amazonSnsClient.deletePlatformApplication(deletePlatformApplicationRequest);
    }

    @Override
    public DeleteTopicResult deleteTopic(DeleteTopicRequest deleteTopicRequest) {
        return amazonSnsClient.deleteTopic(deleteTopicRequest);
    }

    @Override
    public DeleteTopicResult deleteTopic(String s) {
        return amazonSnsClient.deleteTopic(s);
    }

    @Override
    public GetEndpointAttributesResult getEndpointAttributes(GetEndpointAttributesRequest getEndpointAttributesRequest) {
        return amazonSnsClient.getEndpointAttributes(getEndpointAttributesRequest);
    }

    @Override
    public GetPlatformApplicationAttributesResult getPlatformApplicationAttributes(GetPlatformApplicationAttributesRequest getPlatformApplicationAttributesRequest) {
        return amazonSnsClient.getPlatformApplicationAttributes(getPlatformApplicationAttributesRequest);
    }

    @Override
    public GetSMSAttributesResult getSMSAttributes(GetSMSAttributesRequest getSMSAttributesRequest) {
        return amazonSnsClient.getSMSAttributes(getSMSAttributesRequest);
    }

    @Override
    public GetSubscriptionAttributesResult getSubscriptionAttributes(GetSubscriptionAttributesRequest getSubscriptionAttributesRequest) {
        return amazonSnsClient.getSubscriptionAttributes(getSubscriptionAttributesRequest);
    }

    @Override
    public GetSubscriptionAttributesResult getSubscriptionAttributes(String s) {
        return amazonSnsClient.getSubscriptionAttributes(s);
    }

    @Override
    public GetTopicAttributesResult getTopicAttributes(GetTopicAttributesRequest getTopicAttributesRequest) {
        return amazonSnsClient.getTopicAttributes(getTopicAttributesRequest);
    }

    @Override
    public GetTopicAttributesResult getTopicAttributes(String s) {
        return amazonSnsClient.getTopicAttributes(s);
    }

    @Override
    public ListEndpointsByPlatformApplicationResult listEndpointsByPlatformApplication(ListEndpointsByPlatformApplicationRequest listEndpointsByPlatformApplicationRequest) {
        return amazonSnsClient.listEndpointsByPlatformApplication(listEndpointsByPlatformApplicationRequest);
    }

    @Override
    public ListPhoneNumbersOptedOutResult listPhoneNumbersOptedOut(ListPhoneNumbersOptedOutRequest listPhoneNumbersOptedOutRequest) {
        return amazonSnsClient.listPhoneNumbersOptedOut(listPhoneNumbersOptedOutRequest);
    }

    @Override
    public ListPlatformApplicationsResult listPlatformApplications(ListPlatformApplicationsRequest listPlatformApplicationsRequest) {
        return amazonSnsClient.listPlatformApplications(listPlatformApplicationsRequest);
    }

    @Override
    public ListPlatformApplicationsResult listPlatformApplications() {
        return amazonSnsClient.listPlatformApplications();
    }

    @Override
    public ListSubscriptionsResult listSubscriptions(ListSubscriptionsRequest listSubscriptionsRequest) {
        return amazonSnsClient.listSubscriptions(listSubscriptionsRequest);
    }

    @Override
    public ListSubscriptionsResult listSubscriptions() {
        return amazonSnsClient.listSubscriptions();
    }

    @Override
    public ListSubscriptionsResult listSubscriptions(String s) {
        return amazonSnsClient.listSubscriptions(s);
    }

    @Override
    public ListSubscriptionsByTopicResult listSubscriptionsByTopic(ListSubscriptionsByTopicRequest listSubscriptionsByTopicRequest) {
        return amazonSnsClient.listSubscriptionsByTopic(listSubscriptionsByTopicRequest);
    }

    @Override
    public ListSubscriptionsByTopicResult listSubscriptionsByTopic(String s) {
        return amazonSnsClient.listSubscriptionsByTopic(s);
    }

    @Override
    public ListSubscriptionsByTopicResult listSubscriptionsByTopic(String s, String s1) {
        return amazonSnsClient.listSubscriptionsByTopic(s, s1);
    }
    
    @Override
    public ListTagsForResourceResult listTagsForResource(ListTagsForResourceRequest request) {
        return amazonSnsClient.listTagsForResource(request);
    }

    @Override
    public ListTopicsResult listTopics(ListTopicsRequest listTopicsRequest) {
        return amazonSnsClient.listTopics(listTopicsRequest);
    }

    @Override
    public ListTopicsResult listTopics() {
        return amazonSnsClient.listTopics();
    }

    @Override
    public ListTopicsResult listTopics(String s) {
        return amazonSnsClient.listTopics(s);
    }

    @Override
    public OptInPhoneNumberResult optInPhoneNumber(OptInPhoneNumberRequest optInPhoneNumberRequest) {
        return amazonSnsClient.optInPhoneNumber(optInPhoneNumberRequest);
    }

    @Override
    public PublishResult publish(PublishRequest publishRequest) {
        return amazonSnsClient.publish(publishRequest);
    }

    @Override
    public PublishResult publish(String s, String s1) {
        return amazonSnsClient.publish(s, s1);
    }

    @Override
    public PublishResult publish(String s, String s1, String s2) {
        return amazonSnsClient.publish(s, s1, s2);
    }

    @Override
    public RemovePermissionResult removePermission(RemovePermissionRequest removePermissionRequest) {
        return amazonSnsClient.removePermission(removePermissionRequest);
    }

    @Override
    public RemovePermissionResult removePermission(String s, String s1) {
        return amazonSnsClient.removePermission(s, s1);
    }

    @Override
    public SetEndpointAttributesResult setEndpointAttributes(SetEndpointAttributesRequest setEndpointAttributesRequest) {
        return amazonSnsClient.setEndpointAttributes(setEndpointAttributesRequest);
    }

    @Override
    public SetPlatformApplicationAttributesResult setPlatformApplicationAttributes(SetPlatformApplicationAttributesRequest setPlatformApplicationAttributesRequest) {
        return amazonSnsClient.setPlatformApplicationAttributes(setPlatformApplicationAttributesRequest);
    }

    @Override
    public SetSMSAttributesResult setSMSAttributes(SetSMSAttributesRequest setSMSAttributesRequest) {
        return amazonSnsClient.setSMSAttributes(setSMSAttributesRequest);
    }

    @Override
    public SetSubscriptionAttributesResult setSubscriptionAttributes(SetSubscriptionAttributesRequest setSubscriptionAttributesRequest) {
        return amazonSnsClient.setSubscriptionAttributes(setSubscriptionAttributesRequest);
    }

    @Override
    public SetSubscriptionAttributesResult setSubscriptionAttributes(String s, String s1, String s2) {
        return amazonSnsClient.setSubscriptionAttributes(s, s1, s2);
    }

    @Override
    public SetTopicAttributesResult setTopicAttributes(SetTopicAttributesRequest setTopicAttributesRequest) {
        return amazonSnsClient.setTopicAttributes(setTopicAttributesRequest);
    }

    @Override
    public SetTopicAttributesResult setTopicAttributes(String s, String s1, String s2) {
        return amazonSnsClient.setTopicAttributes(s, s1, s2);
    }

    @Override
    public SubscribeResult subscribe(SubscribeRequest subscribeRequest) {
        return amazonSnsClient.subscribe(subscribeRequest);
    }

    @Override
    public SubscribeResult subscribe(String s, String s1, String s2) {
        return amazonSnsClient.subscribe(s, s1, s2);
    }
    
    @Override
    public TagResourceResult tagResource(TagResourceRequest request) {
        return amazonSnsClient.tagResource(request);
    }

    @Override
    public UnsubscribeResult unsubscribe(UnsubscribeRequest unsubscribeRequest) {
        return amazonSnsClient.unsubscribe(unsubscribeRequest);
    }

    @Override
    public UnsubscribeResult unsubscribe(String s) {
        return amazonSnsClient.unsubscribe(s);
    }
    
    @Override
    public UntagResourceResult untagResource(UntagResourceRequest request) {
    	return amazonSnsClient.untagResource(request);
    }

    @Override
    public void shutdown() {
        amazonSnsClient.shutdown();
    }

    @Override
    public ResponseMetadata getCachedResponseMetadata(AmazonWebServiceRequest amazonWebServiceRequest) {
        return amazonSnsClient.getCachedResponseMetadata(amazonWebServiceRequest);
    }

    @Override
    public CreateSMSSandboxPhoneNumberResult createSMSSandboxPhoneNumber(CreateSMSSandboxPhoneNumberRequest createSMSSandboxPhoneNumberRequest) {
        return amazonSnsClient.createSMSSandboxPhoneNumber(createSMSSandboxPhoneNumberRequest);
    }

    @Override
    public VerifySMSSandboxPhoneNumberResult verifySMSSandboxPhoneNumber(VerifySMSSandboxPhoneNumberRequest verifySMSSandboxPhoneNumberRequest) {
        return amazonSnsClient.verifySMSSandboxPhoneNumber(verifySMSSandboxPhoneNumberRequest);
    }

    @Override
    public GetSMSSandboxAccountStatusResult getSMSSandboxAccountStatus(GetSMSSandboxAccountStatusRequest getSMSSandboxAccountStatusRequest) {
        return amazonSnsClient.getSMSSandboxAccountStatus(getSMSSandboxAccountStatusRequest);
    }

    @Override
    public ListSMSSandboxPhoneNumbersResult listSMSSandboxPhoneNumbers(ListSMSSandboxPhoneNumbersRequest listSMSSandboxPhoneNumbersRequest) {
        return amazonSnsClient.listSMSSandboxPhoneNumbers(listSMSSandboxPhoneNumbersRequest);
    }

    @Override
    public DeleteSMSSandboxPhoneNumberResult deleteSMSSandboxPhoneNumber(DeleteSMSSandboxPhoneNumberRequest deleteSMSSandboxPhoneNumberRequest) {
        return amazonSnsClient.deleteSMSSandboxPhoneNumber(deleteSMSSandboxPhoneNumberRequest);
    }

    @Override
    public ListOriginationNumbersResult listOriginationNumbers(ListOriginationNumbersRequest listOriginationNumbersRequest) {
        return amazonSnsClient.listOriginationNumbers(listOriginationNumbersRequest);
    }

    @Override
    public PublishBatchResult publishBatch(PublishBatchRequest publishBatchRequest) {
        return amazonSnsClient.publishBatch(publishBatchRequest);
    }

    public AmazonSNS getWrappedClient() {
        return amazonSnsClient;
    }

    public void setWrappedClient(AmazonSNS amazonSnsClient) {
        this.amazonSnsClient = amazonSnsClient;
    }

    public SnsProperties.SnsTopic getRelatedTopic() {
        return relatedTopic;
    }

    public void setRelatedTopic(SnsProperties.SnsTopic relatedTopic) {
        this.relatedTopic = relatedTopic;
    }



}