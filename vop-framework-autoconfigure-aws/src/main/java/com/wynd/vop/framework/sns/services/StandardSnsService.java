package com.wynd.vop.framework.sns.services;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.wynd.vop.framework.log.BipLogger;
import com.wynd.vop.framework.log.BipLoggerFactory;
import com.wynd.vop.framework.sns.config.SnsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StandardSnsService extends AbstractSnsService {

	private final BipLogger logger = BipLoggerFactory.getLogger(StandardSnsService.class);
	
	@Autowired
  SnsProperties snsProperties;

	public StandardSnsService() {
	}

	public StandardSnsService(AWSCredentialsProvider awsCredentialsProvider,
							  AwsClientBuilder.EndpointConfiguration endpointConfiguration,
							  SnsProperties.SnsTopic relatedTopic) {
		instantiateAmazonSnsClient(awsCredentialsProvider, endpointConfiguration, relatedTopic);
	}

	private void instantiateAmazonSnsClient(AWSCredentialsProvider awsCredentialsProvider,
											AwsClientBuilder.EndpointConfiguration endpointConfiguration,
											SnsProperties.SnsTopic relatedTopic) {
		logger.info("Instantiating Amazon SNS Client in StandardSnsService.");
		amazonSnsClient = AmazonSNSClientBuilder
				.standard()
				.withCredentials(awsCredentialsProvider)
				.withEndpointConfiguration(endpointConfiguration)
				.build();

		setRelatedTopic(relatedTopic);
	}
}





