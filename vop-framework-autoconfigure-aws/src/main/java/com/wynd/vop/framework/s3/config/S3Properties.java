package com.wynd.vop.framework.s3.config;

import com.wynd.vop.framework.config.AwsProperties;
import com.wynd.vop.framework.config.BipAwsServiceEndpoint;
import com.wynd.vop.framework.log.BipLogger;
import com.wynd.vop.framework.log.BipLoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Optional;

/**
 * Configuration properties file for storing VOP Framework S3 Properties.
 */
@ConfigurationProperties(prefix = "vop.framework.aws.s3", ignoreUnknownFields = false)
public class S3Properties extends AwsProperties {

	private BipLogger logger = BipLoggerFactory.getLogger(S3Properties.class);

	private List<S3Bucket> buckets;

	public BipLogger getLogger() {
		return logger;
	}

	public void setLogger (BipLogger logger) {
		this.logger = logger;
	}

	public List<S3Bucket> getBuckets() {
		return buckets;
	}

	public void setBuckets(List<S3Bucket> s3BucketList) {
		this.buckets = s3BucketList;
	}

	public static class S3Bucket extends BipAwsServiceEndpoint {

		private String id;
		private String name;

		public S3Bucket() {
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

	public S3Properties.S3Bucket getBucketByName(String bucketName) {

		S3Properties.S3Bucket matchingBucket = null;

		if(bucketName != null) {
			Optional<S3Properties.S3Bucket> s3Bucket = buckets.stream().filter(bucket -> bucketName.equals(bucket.getName())).findAny();
			if (s3Bucket.isPresent()) {
				matchingBucket = s3Bucket.get();
			}
		}

		logger.info("getBucketByName: {} : {} ", bucketName, matchingBucket);

		return matchingBucket;
	}

	public S3Properties.S3Bucket getBucketById(String bucketId) {

		S3Properties.S3Bucket matchingBucket = null;

		if(bucketId != null) {
			Optional<S3Properties.S3Bucket> s3Bucket = buckets.stream().filter(bucket -> bucketId.equals(bucket.getId())).findAny();
			if (s3Bucket.isPresent()) {
				matchingBucket = s3Bucket.get();
			}
		}

		logger.info("getBucketById: {} : {} ", bucketId, matchingBucket);

		return matchingBucket;
	}
}
