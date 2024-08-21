package com.wynd.vop.framework.sns.config;

import com.wynd.vop.framework.config.AwsProperties;
import com.wynd.vop.framework.config.BipAwsServiceEndpoint;
import com.wynd.vop.framework.log.BipLogger;
import com.wynd.vop.framework.log.BipLoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Optional;

/**
 * Configuration properties file for storing VOP Framework SNS Properties.
 */
@ConfigurationProperties(prefix = "vop.framework.aws.sns", ignoreUnknownFields = false)
public class SnsProperties extends AwsProperties {

	private BipLogger logger = BipLoggerFactory.getLogger(SnsProperties.class);

	private List<SnsTopic> topics;

	public BipLogger getLogger() {
		return logger;
	}

	public void setLogger (BipLogger logger) {
		this.logger = logger;
	}

	public List<SnsTopic> getTopics() {
		return topics;
	}

	public void setTopics(List<SnsTopic> topics) {
		this.topics = topics;
	}

	public static class SnsTopic extends BipAwsServiceEndpoint {

		private String id;
		private String name;
		private String type;
		private String topic;
		private String topicArn;

		public SnsTopic() {
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

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getTopic() {
			return topic;
		}

		public void setTopic(String topic) {
			this.topic = topic;
		}

		public String getTopicArn() {
			return topicArn;
		}

		public void setTopicArn(String topicArn) {
			this.topicArn = topicArn;
		}
	}

	public SnsProperties.SnsTopic getTopicByName(String topicName) {

		SnsProperties.SnsTopic matchingTopic = null;

		if(topicName != null) {
			Optional<SnsProperties.SnsTopic> snsTopic = topics.stream().filter(topic -> topicName.equals(topic.getName())).findAny();
			if (snsTopic.isPresent()) {
				matchingTopic = snsTopic.get();
			}
		}

		logger.info("getTopicByName: {} : {} ", topicName, matchingTopic);

		return matchingTopic;
	}

	public SnsProperties.SnsTopic getTopicById(String topicId) {

		SnsProperties.SnsTopic matchingTopic = null;

		if(topicId != null) {
			Optional<SnsProperties.SnsTopic> snsTopic = topics.stream().filter(topic -> topicId.equals(topic.getId())).findAny();
			if (snsTopic.isPresent()) {
				matchingTopic = snsTopic.get();
			}
		}

		logger.info("getTopicById: {} : {} ", topicId, matchingTopic);

		return matchingTopic;
	}
}
