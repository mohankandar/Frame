package com.wynd.vop.framework.localstack.autoconfigure;

import cloud.localstack.Localstack;
import cloud.localstack.docker.DockerExe;
import cloud.localstack.docker.annotation.LocalstackDockerConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.wynd.vop.framework.config.BipCommonSpringProfiles;
import com.wynd.vop.framework.s3.config.S3Properties;
import com.wynd.vop.framework.s3.services.StandardS3Service;
import com.wynd.vop.framework.sns.config.SnsProperties;
import com.wynd.vop.framework.sns.services.StandardSnsService;
import com.wynd.vop.framework.sqs.config.SqsProperties;
import com.wynd.vop.framework.sqs.services.StandardSqsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Performs configuration of a Localstack instance
 *
 */
@Configuration
@Profile({BipCommonSpringProfiles.PROFILE_EMBEDDED_AWS})
@EnableConfigurationProperties({LocalstackProperties.class, SqsProperties.class, SnsProperties.class, S3Properties.class})
@ConditionalOnProperty(name = "vop.framework.localstack.enabled", havingValue = "true")
@Primary
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LocalstackAutoConfigurationImpl {

	/**
	 * Class logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LocalstackAutoConfigurationImpl.class);
	private static final String LOCALSTACK = "localstack";
	private static final Pattern LOCALSTACK_READY_TOKEN = Pattern.compile("Ready\\.");

	@Autowired
	private LocalstackProperties localstackProperties;

	@Autowired
	private SqsProperties sqsProperties;

	@Autowired
	private SnsProperties snsProperties;

	@Autowired
	private S3Properties s3Properties;

	@Autowired
	Environment environment;

	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	@Autowired(required=false)
	List<AmazonSQS> sqsClientList = new ArrayList<>();

	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	@Autowired(required=false)
	List<AmazonSNS> snsClientList = new ArrayList<>();

	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	@Autowired(required=false)
	List<AmazonS3> s3ClientList = new ArrayList<>();

	/**
	 * Start embedded AWS servers on context load
	 *
	 * @throws IOException
	 */
	@PostConstruct
	public void startAwsLocalStack() {
		// If the Embedded AWS profile is active, ensure LocalStack is running
		if (profileCheck(BipCommonSpringProfiles.PROFILE_EMBEDDED_AWS)) {

			if (Localstack.INSTANCE != null && Localstack.INSTANCE.getLocalStackContainer() != null) {
				LOGGER.info("Localstack container instance is already running...");
			} else if (Localstack.INSTANCE != null) {
				// Clean the localstack
				cleanAwsLocalStack();

				Localstack.INSTANCE.startup(buildLocalstackDockerConfiguration());
			}
		}

		//Add LocalStack to the network, if necessary
		addToDockerNetwork();

		//Wait for LocalStack to be ready
		waitForLocalstack();

		// Initialize the LocalStack services
		createLocalstackServices();
	}

	/**
	 * Stop embedded AWS servers on context destroy
	 */
	@PreDestroy
	public void stopAwsLocalStack() {
		// If we have Embedded AWS and we are not running this code inside a container (which has no access to a Docker install), execute the following.
		if (profileCheck(BipCommonSpringProfiles.PROFILE_EMBEDDED_AWS) && !profileCheck(BipCommonSpringProfiles.PROFILE_ENV_LOCAL_INT)) {
			// Stop the LocalStack container
			if (Localstack.INSTANCE != null && Localstack.INSTANCE.getLocalStackContainer() != null) {
				Localstack.INSTANCE.stop();
			}

			// Clean the LocalStack docker containers
			cleanAwsLocalStack();
		}
	}

	private boolean profileCheck(String profile) {
		boolean profileMatches = false;

		for (final String profileName : environment.getActiveProfiles()) {
			if (profileName.equals(profile)) {
				profileMatches = true;
				break;
			}
		}

		return profileMatches;
	}

	/**
	 * Clean AWS Localstack containers
	 */
	private void cleanAwsLocalStack() {
		// Get containers Ids
		DockerExe newDockerExe = new DockerExe();
		String[] containerIdArray = getLocalstackContainerIds();

		if(containerIdArray != null) {
			for (String containerId : containerIdArray) {
				newDockerExe.execute(Arrays.asList("rm", "-f", containerId));
			}
		}
	}

	private String[] getLocalstackContainerIds() {
		// Get containers Ids
		DockerExe newDockerExe = new DockerExe();
		String listContainerIds =
				newDockerExe.execute(Arrays.asList("ps", "--no-trunc", "-aq", "--filter", "ancestor=localstack/localstack:"
						+ localstackProperties.getImageTag()));

		String[] splitArray = null;

		if (StringUtils.isNotEmpty(listContainerIds)) {
			try {
				splitArray = listContainerIds.split("\\s+");
			} catch (PatternSyntaxException ex) {
				// PatternSyntaxException During Splitting
			}
		}

		return splitArray;
	}

	private void addToDockerNetwork() {
		if(localstackProperties.getNetwork() != null) {
			//Add LocalStack to the network
			DockerExe newDockerExe = new DockerExe();
			String[] containerIdArray = getLocalstackContainerIds();

			if(containerIdArray != null) {
				for (String containerId : containerIdArray) {
					newDockerExe.execute(Arrays.asList("network", "connect", "--alias="+LOCALSTACK, localstackProperties.getNetwork(), containerId));
				}
			}
		}
	}

	private LocalstackDockerConfiguration buildLocalstackDockerConfiguration() {
		LocalstackDockerConfiguration.LocalstackDockerConfigurationBuilder configBuilder = LocalstackDockerConfiguration.builder();

		configBuilder.externalHostName(localstackProperties.getExternalHostName());
		configBuilder.imageTag(localstackProperties.getImageTag());
		configBuilder.pullNewImage(localstackProperties.isPullNewImage());
		configBuilder.randomizePorts(localstackProperties.isRandomizePorts());

		List<LocalstackProperties.Services> listServices = localstackProperties.getServices();

		Map<String, String> localstackEnvironmentVariables = new HashMap<>();

		if (!CollectionUtils.isEmpty(listServices)) {
			// Put selected services into a list
			StringBuilder builder = new StringBuilder();
			for (LocalstackProperties.Services service : listServices) {
				if(builder.length() != 0) {
					builder.append(",");
				}
				builder.append(service.getName());
				builder.append(":");
				builder.append(service.getPort());
			}

			String services = String.join(",", builder.toString());
			if (StringUtils.isNotEmpty(services)) {
				// Listed Services will be started
				localstackEnvironmentVariables.put("SERVICES", services);
			}
		}

		localstackEnvironmentVariables.put("DEFAULT_REGION", localstackProperties.getDefaultRegion());

		configBuilder.environmentVariables(localstackEnvironmentVariables);

		return configBuilder.build();
	}

	private void waitForLocalstack() {
		if(Localstack.INSTANCE.getLocalStackContainer() != null) {
			Localstack.INSTANCE.getLocalStackContainer().waitForLogToken(LOCALSTACK_READY_TOKEN);
		} else {
			LOGGER.error("Encountered an issue when trying to wait for the LocalStack container to come up; " +
					"Could not find the LocalStack container to wait for the appropriate log token!");
		}
	}

	private void createLocalstackServices() {
		if(s3Properties.isEnabled()) {
			initializeS3Services();
		}
		if(snsProperties.isEnabled()) {
			initializeSnsServices();
		}
		if(sqsProperties.isEnabled()) {
			initializeSqsServices();
		}
	}

	private void initializeS3Services() {
		LOGGER.info("Initializing S3 Services in LocalStack...");
		for(S3Properties.S3Bucket bucket : s3Properties.getBuckets()) {
			AmazonS3 client = getS3ClientInRegion(bucket.getRegion());

			CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucket.getName(), bucket.getRegion());

			client.createBucket(createBucketRequest);
		}
		LOGGER.info("Completed initializing S3 Services in LocalStack!");
	}

	private void initializeSnsServices() {
		LOGGER.info("Initializing SNS Services in LocalStack...");
		for(SnsProperties.SnsTopic topic : snsProperties.getTopics()) {
			AmazonSNS client = getSNSClientInRegion(topic.getRegion());

			CreateTopicRequest createTopicRequest = new CreateTopicRequest(topic.getName());

			CreateTopicResult result = client.createTopic(createTopicRequest);

			topic.setTopicArn(result.getTopicArn());
		}
		LOGGER.info("Completed initializing SNS Services in LocalStack!");
	}

	private void initializeSqsServices() {
		LOGGER.info("Initializing SQS Services in LocalStack...");
		for(SqsProperties.SqsQueue queue : sqsProperties.getQueues()) {
			AmazonSQS client = getSQSClientInRegion(queue.getRegion());

			CreateQueueRequest createQueueRequest = new CreateQueueRequest(queue.getName());

			client.createQueue(createQueueRequest);
		}
		LOGGER.info("Completed initializing SQS Services in LocalStack!");
	}

	private AmazonSQS getSQSClientInRegion(String regionToMatch) {

		AmazonSQS returnClient = null;

		// Attempt to find and use an AmazonSQS Client in the same region
		for(AmazonSQS sqsClient : sqsClientList) {
			if(sqsClient instanceof StandardSqsService) {
				StandardSqsService standardSqsService = (StandardSqsService) sqsClient;

				if(standardSqsService.getRelatedQueue() != null
						&& standardSqsService.getRelatedQueue().getRegion().equals(regionToMatch)) {
					returnClient = sqsClient;
				}
			}
		}

		if(returnClient == null) {
			throw new NullPointerException("LocalStack AutoConfiguration couldn't find a suitable AmazonSQS Client Bean to instantiate related topics and settings!");
		}

		return returnClient;
	}

	private AmazonSNS getSNSClientInRegion(String regionToMatch) {

		AmazonSNS returnClient = null;

		// Attempt to find and use an AmazonSNS Client in the same region
		for(AmazonSNS snsClient : snsClientList) {
			if(snsClient instanceof StandardSnsService) {
				StandardSnsService standardSnsService = (StandardSnsService) snsClient;

				if(standardSnsService.getRelatedTopic() != null
						&& standardSnsService.getRelatedTopic().getRegion().equals(regionToMatch)) {
					returnClient = snsClient;
				}
			}
		}

		if(returnClient == null) {
			throw new NullPointerException("LocalStack AutoConfiguration couldn't find a suitable AmazonSNS Client Bean to instantiate related topics and settings!");
		}

		return returnClient;
	}

	private AmazonS3 getS3ClientInRegion(String regionToMatch) {

		AmazonS3 returnClient = null;

		// Attempt to find and use an AmazonS3 Client in the same region
		for(AmazonS3 s3Client : s3ClientList) {
			if(s3Client instanceof StandardS3Service) {
				StandardS3Service standardS3Service = (StandardS3Service) s3Client;

				if(standardS3Service.getRelatedBucket() != null
						&& standardS3Service.getRelatedBucket().getRegion().equals(regionToMatch)) {
					returnClient = s3Client;
				}
			}
		}

		if(returnClient == null) {
			throw new NullPointerException("LocalStack AutoConfiguration couldn't find a suitable AmazonS3 Client Bean to instantiate related topics and settings!");
		}

		return returnClient;
	}
}
