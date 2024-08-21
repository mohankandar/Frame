package com.wynd.vop.framework.sqs.services;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.wynd.vop.framework.log.BipLogger;
import com.wynd.vop.framework.log.BipLoggerFactory;
import com.wynd.vop.framework.sqs.config.SqsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StandardSqsService extends AbstractSqsService {

	private final BipLogger logger = BipLoggerFactory.getLogger(StandardSqsService.class);
	
	@Autowired
	SqsProperties sqsProperties;

	public StandardSqsService() {
	}

	public StandardSqsService(AWSCredentialsProvider awsCredentialsProvider,
							  AwsClientBuilder.EndpointConfiguration endpointConfiguration,
							  SqsProperties.SqsQueue relatedQueue) {
		instantiateAmazonSqsClient(awsCredentialsProvider, endpointConfiguration, relatedQueue);
	}

	private void instantiateAmazonSqsClient(AWSCredentialsProvider awsCredentialsProvider,
											AwsClientBuilder.EndpointConfiguration endpointConfiguration,
											SqsProperties.SqsQueue relatedQueue) {
		logger.info("Instantiating Amazon SQS Client in StandardSqsService.");
		amazonSqsClient = AmazonSQSClientBuilder
				.standard()
				.withCredentials(awsCredentialsProvider)
				.withEndpointConfiguration(endpointConfiguration)
				.build();

		setRelatedQueue(relatedQueue);
	}
}





