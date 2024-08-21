package com.wynd.vop.framework.s3.services;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.wynd.vop.framework.log.BipLogger;
import com.wynd.vop.framework.log.BipLoggerFactory;
import com.wynd.vop.framework.s3.config.S3Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StandardS3Service extends AbstractS3Service {

	private final BipLogger logger = BipLoggerFactory.getLogger(StandardS3Service.class);
	
	@Autowired
	S3Properties s3Properties;

	public StandardS3Service() {
	}

	public StandardS3Service(AWSCredentialsProvider awsCredentialsProvider,
							 AwsClientBuilder.EndpointConfiguration endpointConfiguration,
							 S3Properties.S3Bucket relatedBucket) {
		instantiateAmazonS3Client(awsCredentialsProvider, endpointConfiguration, relatedBucket);
	}

	private void instantiateAmazonS3Client(AWSCredentialsProvider awsCredentialsProvider,
										   AwsClientBuilder.EndpointConfiguration endpointConfiguration,
										   S3Properties.S3Bucket relatedBucket) {
		logger.info("Instantiating Amazon S3 Client in StandardS3Service.");
		amazonS3Client = AmazonS3ClientBuilder
				.standard()
				.withCredentials(awsCredentialsProvider)
				.withEndpointConfiguration(endpointConfiguration)
				.withPathStyleAccessEnabled(true)
				.build();

		setRelatedBucket(relatedBucket);
	}


}





