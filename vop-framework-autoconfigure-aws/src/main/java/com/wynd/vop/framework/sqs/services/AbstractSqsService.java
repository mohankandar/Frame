package com.wynd.vop.framework.sqs.services;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.regions.Region;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.*;
import com.wynd.vop.framework.sqs.config.SqsProperties;

import java.util.List;
import java.util.Map;

/**
 * This class is intended as a wrapper for an AmazonSQS Service
 */
public abstract class AbstractSqsService implements AmazonSQS {

    AmazonSQS amazonSqsClient;
    SqsProperties.SqsQueue relatedQueue;

    @Override
    public void setEndpoint(String s) {
        throw new java.lang.UnsupportedOperationException("The SQS Service method setEndpoint is deprecated and should not be " +
                "used. See Amazon SQS API documentation for more information. [AmazonSQS.setEndpoint(String s);]");
    }

    @Override
    public void setRegion(Region region) {
        throw new java.lang.UnsupportedOperationException("The SQS Service method setRegion is deprecated and should not be " +
                "used. See Amazon SQS API documentation for more information. [AmazonSQS.setRegion(Region region);]");
    }

    @Override
    public AddPermissionResult addPermission(AddPermissionRequest addPermissionRequest) {
        return amazonSqsClient.addPermission(addPermissionRequest);
    }

    @Override
    public AddPermissionResult addPermission(String s, String s1, List<String> list, List<String> list1) {
        return amazonSqsClient.addPermission(s, s1, list, list1);
    }

    @Override
    public ChangeMessageVisibilityResult changeMessageVisibility(ChangeMessageVisibilityRequest changeMessageVisibilityRequest) {
        return amazonSqsClient.changeMessageVisibility(changeMessageVisibilityRequest);
    }

    @Override
    public ChangeMessageVisibilityResult changeMessageVisibility(String s, String s1, Integer integer) {
        return amazonSqsClient.changeMessageVisibility(s, s1, integer);
    }

    @Override
    public ChangeMessageVisibilityBatchResult changeMessageVisibilityBatch(ChangeMessageVisibilityBatchRequest changeMessageVisibilityBatchRequest) {
        return amazonSqsClient.changeMessageVisibilityBatch(changeMessageVisibilityBatchRequest);
    }

    @Override
    public ChangeMessageVisibilityBatchResult changeMessageVisibilityBatch(String s, List<ChangeMessageVisibilityBatchRequestEntry> list) {
        return amazonSqsClient.changeMessageVisibilityBatch(s, list);
    }

    @Override
    public CreateQueueResult createQueue(CreateQueueRequest createQueueRequest) {
        return amazonSqsClient.createQueue(createQueueRequest);
    }

    @Override
    public CreateQueueResult createQueue(String s) {
        return amazonSqsClient.createQueue(s);
    }

    @Override
    public DeleteMessageResult deleteMessage(DeleteMessageRequest deleteMessageRequest) {
        return amazonSqsClient.deleteMessage(deleteMessageRequest);
    }

    @Override
    public DeleteMessageResult deleteMessage(String s, String s1) {
        return amazonSqsClient.deleteMessage(s, s1);
    }

    @Override
    public DeleteMessageBatchResult deleteMessageBatch(DeleteMessageBatchRequest deleteMessageBatchRequest) {
        return amazonSqsClient.deleteMessageBatch(deleteMessageBatchRequest);
    }

    @Override
    public DeleteMessageBatchResult deleteMessageBatch(String s, List<DeleteMessageBatchRequestEntry> list) {
        return amazonSqsClient.deleteMessageBatch(s, list);
    }

    @Override
    public DeleteQueueResult deleteQueue(DeleteQueueRequest deleteQueueRequest) {
        return amazonSqsClient.deleteQueue(deleteQueueRequest);
    }

    @Override
    public DeleteQueueResult deleteQueue(String s) {
        return amazonSqsClient.deleteQueue(s);
    }

    @Override
    public GetQueueAttributesResult getQueueAttributes(GetQueueAttributesRequest getQueueAttributesRequest) {
        return amazonSqsClient.getQueueAttributes(getQueueAttributesRequest);
    }

    @Override
    public GetQueueAttributesResult getQueueAttributes(String s, List<String> list) {
        return amazonSqsClient.getQueueAttributes(s, list);
    }

    @Override
    public GetQueueUrlResult getQueueUrl(GetQueueUrlRequest getQueueUrlRequest) {
        return amazonSqsClient.getQueueUrl(getQueueUrlRequest);
    }

    @Override
    public GetQueueUrlResult getQueueUrl(String s) {
        return amazonSqsClient.getQueueUrl(s);
    }

    @Override
    public ListDeadLetterSourceQueuesResult listDeadLetterSourceQueues(ListDeadLetterSourceQueuesRequest listDeadLetterSourceQueuesRequest) {
        return amazonSqsClient.listDeadLetterSourceQueues(listDeadLetterSourceQueuesRequest);
    }

    @Override
    public ListQueueTagsResult listQueueTags(ListQueueTagsRequest listQueueTagsRequest) {
        return amazonSqsClient.listQueueTags(listQueueTagsRequest);
    }

    @Override
    public ListQueueTagsResult listQueueTags(String s) {
        return amazonSqsClient.listQueueTags(s);
    }

    @Override
    public ListQueuesResult listQueues(ListQueuesRequest listQueuesRequest) {
        return amazonSqsClient.listQueues(listQueuesRequest);
    }

    @Override
    public ListQueuesResult listQueues() {
        return amazonSqsClient.listQueues();
    }

    @Override
    public ListQueuesResult listQueues(String s) {
        return amazonSqsClient.listQueues(s);
    }

    @Override
    public PurgeQueueResult purgeQueue(PurgeQueueRequest purgeQueueRequest) {
        return amazonSqsClient.purgeQueue(purgeQueueRequest);
    }

    @Override
    public ReceiveMessageResult receiveMessage(ReceiveMessageRequest receiveMessageRequest) {
        return amazonSqsClient.receiveMessage(receiveMessageRequest);
    }

    @Override
    public ReceiveMessageResult receiveMessage(String s) {
        return amazonSqsClient.receiveMessage(s);
    }

    @Override
    public RemovePermissionResult removePermission(RemovePermissionRequest removePermissionRequest) {
        return amazonSqsClient.removePermission(removePermissionRequest);
    }

    @Override
    public RemovePermissionResult removePermission(String s, String s1) {
        return amazonSqsClient.removePermission(s, s1);
    }

    @Override
    public SendMessageResult sendMessage(SendMessageRequest sendMessageRequest) {
        return amazonSqsClient.sendMessage(sendMessageRequest);
    }

    @Override
    public SendMessageResult sendMessage(String s, String s1) {
        return amazonSqsClient.sendMessage(s, s1);
    }

    @Override
    public SendMessageBatchResult sendMessageBatch(SendMessageBatchRequest sendMessageBatchRequest) {
        return amazonSqsClient.sendMessageBatch(sendMessageBatchRequest);
    }

    @Override
    public SendMessageBatchResult sendMessageBatch(String s, List<SendMessageBatchRequestEntry> list) {
        return amazonSqsClient.sendMessageBatch(s, list);
    }

    @Override
    public SetQueueAttributesResult setQueueAttributes(SetQueueAttributesRequest setQueueAttributesRequest) {
        return amazonSqsClient.setQueueAttributes(setQueueAttributesRequest);
    }

    @Override
    public SetQueueAttributesResult setQueueAttributes(String s, Map<String, String> map) {
        return amazonSqsClient.setQueueAttributes(s, map);
    }

    @Override
    public TagQueueResult tagQueue(TagQueueRequest tagQueueRequest) {
        return amazonSqsClient.tagQueue(tagQueueRequest);
    }

    @Override
    public TagQueueResult tagQueue(String s, Map<String, String> map) {
        return amazonSqsClient.tagQueue(s, map);
    }

    @Override
    public UntagQueueResult untagQueue(UntagQueueRequest untagQueueRequest) {
        return amazonSqsClient.untagQueue(untagQueueRequest);
    }

    @Override
    public UntagQueueResult untagQueue(String s, List<String> list) {
        return amazonSqsClient.untagQueue(s, list);
    }

    @Override
    public void shutdown() {
        amazonSqsClient.shutdown();
    }

    @Override
    public ResponseMetadata getCachedResponseMetadata(AmazonWebServiceRequest amazonWebServiceRequest) {
        return amazonSqsClient.getCachedResponseMetadata(amazonWebServiceRequest);
    }

    public AmazonSQS getWrappedClient() {
        return amazonSqsClient;
    }

    public void setWrappedClient(AmazonSQS amazonSqsClient) {
        this.amazonSqsClient = amazonSqsClient;
    }

    public SqsProperties.SqsQueue getRelatedQueue() {
        return relatedQueue;
    }

    public void setRelatedQueue(SqsProperties.SqsQueue relatedQueue) {
        this.relatedQueue = relatedQueue;
    }
}