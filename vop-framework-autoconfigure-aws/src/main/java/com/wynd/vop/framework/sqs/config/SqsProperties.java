package com.wynd.vop.framework.sqs.config;

import com.wynd.vop.framework.config.AwsProperties;
import com.wynd.vop.framework.config.BipAwsServiceEndpoint;
import com.wynd.vop.framework.log.BipLogger;
import com.wynd.vop.framework.log.BipLoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Optional;

/**
 * Configuration properties file for storing VOP Framework SQS Properties.
 */
@ConfigurationProperties(prefix = "vop.framework.aws.sqs", ignoreUnknownFields = false)
public class SqsProperties extends AwsProperties {

	private BipLogger logger = BipLoggerFactory.getLogger(SqsProperties.class);

	private List<SqsQueue> queues;

	public BipLogger getLogger() {
		return logger;
	}

	public void setLogger (BipLogger logger) {
		this.logger = logger;
	}

	public List<SqsQueue> getQueues() {
		return queues;
	}

	public void setQueues(List<SqsQueue> queues) {
		this.queues = queues;
	}

	public static class SqsQueue extends BipAwsServiceEndpoint {

		private String id;
		private String name;

		public SqsQueue() {
			// Empty constructor, these get instantiated by config parameters.
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	public SqsQueue getQueueByName(String queueName) {

		SqsQueue matchingQueue = null;

		if(queueName != null) {
			Optional<SqsQueue> sqsQueue = queues.stream().filter(queue -> queueName.equals(queue.getName())).findAny();
			if (sqsQueue.isPresent()) {
				matchingQueue = sqsQueue.get();
			}
		}

		logger.info("getQueueByName: {} : {} ", queueName, matchingQueue);

		return matchingQueue;
	}

	public SqsQueue getQueueById(String queueId) {

		SqsQueue matchingQueue = null;

		if(queueId != null) {
			Optional<SqsQueue> sqsQueue = queues.stream().filter(queue -> queueId.equals(queue.getId())).findAny();
			if (sqsQueue.isPresent()) {
				matchingQueue = sqsQueue.get();
			}
		}

		logger.info("getQueueById: {} : {} ", queueId, matchingQueue);

		return matchingQueue;
	}
}
