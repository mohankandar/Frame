package com.wynd.vop.framework.s3.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.S3ResponseMetadata;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.model.analytics.AnalyticsConfiguration;
import com.amazonaws.services.s3.model.intelligenttiering.IntelligentTieringConfiguration;
import com.amazonaws.services.s3.model.inventory.InventoryConfiguration;
import com.amazonaws.services.s3.model.metrics.MetricsConfiguration;
import com.amazonaws.services.s3.model.ownership.OwnershipControls;
import com.amazonaws.services.s3.waiters.AmazonS3Waiters;
import com.wynd.vop.framework.s3.config.S3Properties;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * This class is intended as a wrapper for an AmazonS3 Service
 */
public abstract class AbstractS3Service implements AmazonS3 {

    AmazonS3 amazonS3Client;
    S3Properties.S3Bucket relatedBucket;

    @Override
    public void setEndpoint(String s) {
        amazonS3Client.setEndpoint(s);
    }

    @Override
    public void setRegion(Region region) {
        amazonS3Client.setRegion(region);
    }

    @Override
    public void setS3ClientOptions(S3ClientOptions s3ClientOptions) {
        amazonS3Client.setS3ClientOptions(s3ClientOptions);
    }

    @Override
    public void changeObjectStorageClass(String s, String s1, StorageClass storageClass) {
        throw new UnsupportedOperationException("The S3 Service method changeObjectStorageClass is deprecated and should not be " +
                "used. See Amazon S3 API documentation for more information. [AmazonS3.changeObjectStorageClass(String s, String s1, StorageClass storageClass);]");
    }

    @Override
    public void setObjectRedirectLocation(String s, String s1, String s2) {
        throw new UnsupportedOperationException("The S3 Service method setObjectRedirectLocation is deprecated and should not be " +
                "used. See Amazon S3 API documentation for more information. [AmazonS3.setObjectRedirectLocation(String s, String s1, String s2);]");
    }

    @Override
    public ObjectListing listObjects(String s) {
        return amazonS3Client.listObjects(s);
    }

    @Override
    public ObjectListing listObjects(String s, String s1) {
        return amazonS3Client.listObjects(s, s1);
    }

    @Override
    public ObjectListing listObjects(ListObjectsRequest listObjectsRequest) {
        return amazonS3Client.listObjects(listObjectsRequest);
    }

    @Override
    public ListObjectsV2Result listObjectsV2(String s) {
        return amazonS3Client.listObjectsV2(s);
    }

    @Override
    public ListObjectsV2Result listObjectsV2(String s, String s1) {
        return amazonS3Client.listObjectsV2(s, s1);
    }

    @Override
    public ListObjectsV2Result listObjectsV2(ListObjectsV2Request listObjectsV2Request) {
        return amazonS3Client.listObjectsV2(listObjectsV2Request);
    }

    @Override
    public ObjectListing listNextBatchOfObjects(ObjectListing objectListing) {
        return amazonS3Client.listNextBatchOfObjects(objectListing);
    }

    @Override
    public ObjectListing listNextBatchOfObjects(ListNextBatchOfObjectsRequest listNextBatchOfObjectsRequest) {
        return amazonS3Client.listNextBatchOfObjects(listNextBatchOfObjectsRequest);
    }

    @Override
    public VersionListing listVersions(String s, String s1) {
        return amazonS3Client.listVersions(s, s1);
    }

    @Override
    public VersionListing listNextBatchOfVersions(VersionListing versionListing) {
        return amazonS3Client.listNextBatchOfVersions(versionListing);
    }

    @Override
    public VersionListing listNextBatchOfVersions(ListNextBatchOfVersionsRequest listNextBatchOfVersionsRequest) {
        return amazonS3Client.listNextBatchOfVersions(listNextBatchOfVersionsRequest);
    }

    @Override
    public VersionListing listVersions(String s, String s1, String s2, String s3, String s4, Integer integer) {
        return amazonS3Client.listVersions(s, s1, s2, s3, s4, integer);
    }

    @Override
    public VersionListing listVersions(ListVersionsRequest listVersionsRequest) {
        return amazonS3Client.listVersions(listVersionsRequest);
    }

    @Override
    public Owner getS3AccountOwner() {
        return amazonS3Client.getS3AccountOwner();
    }

    @Override
    public Owner getS3AccountOwner(GetS3AccountOwnerRequest getS3AccountOwnerRequest) {
        return amazonS3Client.getS3AccountOwner(getS3AccountOwnerRequest);
    }

    @Override
    public boolean doesBucketExist(String s) {
        throw new UnsupportedOperationException("The S3 Service method doesBucketExist is deprecated and should not be " +
                "used. See Amazon S3 API documentation for more information. [AmazonS3.doesBucketExist(String s);]");
    }

    @Override
    public boolean doesBucketExistV2(String s) {
        return amazonS3Client.doesBucketExistV2(s);
    }

    @Override
    public HeadBucketResult headBucket(HeadBucketRequest headBucketRequest) {
        return amazonS3Client.headBucket(headBucketRequest);
    }

    @Override
    public List<Bucket> listBuckets() {
        return amazonS3Client.listBuckets();
    }

    @Override
    public List<Bucket> listBuckets(ListBucketsRequest listBucketsRequest) {
        return amazonS3Client.listBuckets(listBucketsRequest);
    }

    @Override
    public String getBucketLocation(String s) {
        return amazonS3Client.getBucketLocation(s);
    }

    @Override
    public String getBucketLocation(GetBucketLocationRequest getBucketLocationRequest) {
        return amazonS3Client.getBucketLocation(getBucketLocationRequest);
    }

    @Override
    public Bucket createBucket(CreateBucketRequest createBucketRequest) {
        return amazonS3Client.createBucket(createBucketRequest);
    }

    @Override
    public Bucket createBucket(String s) {
        return amazonS3Client.createBucket(s);
    }

    @Override
    public Bucket createBucket(String s, com.amazonaws.services.s3.model.Region region) {
        throw new UnsupportedOperationException("The S3 Service method createBucket is deprecated and should not be " +
                "used. See Amazon S3 API documentation for more information. [AmazonS3.createBucket(String s, com.amazonaws.services.s3.model.Region region);]");
    }

    @Override
    public Bucket createBucket(String s, String s1) {
        throw new UnsupportedOperationException("The S3 Service method createBucket is deprecated and should not be " +
                "used. See Amazon S3 API documentation for more information. [AmazonS3.createBucket(String s, String s1);]");
    }

    @Override
    public AccessControlList getObjectAcl(String s, String s1) {
        return amazonS3Client.getObjectAcl(s, s1);
    }

    @Override
    public AccessControlList getObjectAcl(String s, String s1, String s2) {
        return amazonS3Client.getObjectAcl(s, s1, s2);
    }

    @Override
    public AccessControlList getObjectAcl(GetObjectAclRequest getObjectAclRequest) {
        return amazonS3Client.getObjectAcl(getObjectAclRequest);
    }

    @Override
    public void setObjectAcl(String s, String s1, AccessControlList accessControlList) {
        amazonS3Client.setObjectAcl(s, s1, accessControlList);
    }

    @Override
    public void setObjectAcl(String s, String s1, CannedAccessControlList cannedAccessControlList) {
        amazonS3Client.setObjectAcl(s, s1, cannedAccessControlList);
    }

    @Override
    public void setObjectAcl(String s, String s1, String s2, AccessControlList accessControlList) {
        amazonS3Client.setObjectAcl(s, s1, s2, accessControlList);
    }

    @Override
    public void setObjectAcl(String s, String s1, String s2, CannedAccessControlList cannedAccessControlList) {
        amazonS3Client.setObjectAcl(s, s1, s2, cannedAccessControlList);
    }

    @Override
    public void setObjectAcl(SetObjectAclRequest setObjectAclRequest) {
        amazonS3Client.setObjectAcl(setObjectAclRequest);
    }

    @Override
    public AccessControlList getBucketAcl(String s) {
        return amazonS3Client.getBucketAcl(s);
    }

    @Override
    public void setBucketAcl(SetBucketAclRequest setBucketAclRequest) {
        amazonS3Client.setBucketAcl(setBucketAclRequest);
    }

    @Override
    public AccessControlList getBucketAcl(GetBucketAclRequest getBucketAclRequest) {
        return amazonS3Client.getBucketAcl(getBucketAclRequest);
    }

    @Override
    public void setBucketAcl(String s, AccessControlList accessControlList) {
        amazonS3Client.setBucketAcl(s, accessControlList);
    }

    @Override
    public void setBucketAcl(String s, CannedAccessControlList cannedAccessControlList) {
        amazonS3Client.setBucketAcl(s, cannedAccessControlList);
    }

    @Override
    public SetBucketOwnershipControlsResult setBucketOwnershipControls(String s, OwnershipControls ownershipControls) throws AmazonServiceException, SdkClientException {
        return amazonS3Client.setBucketOwnershipControls(s, ownershipControls);
    }

    @Override
    public SetBucketOwnershipControlsResult setBucketOwnershipControls(SetBucketOwnershipControlsRequest setBucketOwnershipControlsRequest) throws AmazonServiceException, SdkClientException {
        return amazonS3Client.setBucketOwnershipControls(setBucketOwnershipControlsRequest);
    }

    @Override
    public GetBucketOwnershipControlsResult getBucketOwnershipControls(GetBucketOwnershipControlsRequest getBucketOwnershipControlsRequest) throws AmazonServiceException, SdkClientException {
        return amazonS3Client.getBucketOwnershipControls(getBucketOwnershipControlsRequest);
    }

    @Override
    public DeleteBucketOwnershipControlsResult deleteBucketOwnershipControls(DeleteBucketOwnershipControlsRequest deleteBucketOwnershipControlsRequest) throws AmazonServiceException, SdkClientException {
        return amazonS3Client.deleteBucketOwnershipControls(deleteBucketOwnershipControlsRequest);
    }

    @Override
    public ObjectMetadata getObjectMetadata(String s, String s1) {
        return amazonS3Client.getObjectMetadata(s, s1);
    }

    @Override
    public ObjectMetadata getObjectMetadata(GetObjectMetadataRequest getObjectMetadataRequest) {
        return amazonS3Client.getObjectMetadata(getObjectMetadataRequest);
    }

    @Override
    public S3Object getObject(String s, String s1) {
        return amazonS3Client.getObject(s, s1);
    }

    @Override
    public S3Object getObject(GetObjectRequest getObjectRequest) {
        return amazonS3Client.getObject(getObjectRequest);
    }

    @Override
    public ObjectMetadata getObject(GetObjectRequest getObjectRequest, File file) {
        return amazonS3Client.getObject(getObjectRequest, file);
    }

    @Override
    public String getObjectAsString(String s, String s1) {
        return amazonS3Client.getObjectAsString(s, s1);
    }

    @Override
    public GetObjectTaggingResult getObjectTagging(GetObjectTaggingRequest getObjectTaggingRequest) {
        return amazonS3Client.getObjectTagging(getObjectTaggingRequest);
    }

    @Override
    public SetObjectTaggingResult setObjectTagging(SetObjectTaggingRequest setObjectTaggingRequest) {
        return amazonS3Client.setObjectTagging(setObjectTaggingRequest);
    }

    @Override
    public DeleteObjectTaggingResult deleteObjectTagging(DeleteObjectTaggingRequest deleteObjectTaggingRequest) {
        return amazonS3Client.deleteObjectTagging(deleteObjectTaggingRequest);
    }

    @Override
    public void deleteBucket(DeleteBucketRequest deleteBucketRequest) {
        amazonS3Client.deleteBucket(deleteBucketRequest);
    }

    @Override
    public void deleteBucket(String s) {
        amazonS3Client.deleteBucket(s);
    }

    @Override
    public WriteGetObjectResponseResult writeGetObjectResponse(WriteGetObjectResponseRequest writeGetObjectResponseRequest) {
        return amazonS3Client.writeGetObjectResponse(writeGetObjectResponseRequest);
    }



    @Override
    public PutObjectResult putObject(PutObjectRequest putObjectRequest) {
        return amazonS3Client.putObject(putObjectRequest);
    }

    @Override
    public PutObjectResult putObject(String s, String s1, File file) {
        return amazonS3Client.putObject(s, s1, file);
    }

    @Override
    public PutObjectResult putObject(String s, String s1, InputStream inputStream, ObjectMetadata objectMetadata) {
        return amazonS3Client.putObject(s, s1, inputStream, objectMetadata);
    }

    @Override
    public PutObjectResult putObject(String s, String s1, String s2) {
        return amazonS3Client.putObject(s, s1, s2);
    }

    @Override
    public CopyObjectResult copyObject(String s, String s1, String s2, String s3) {
        return amazonS3Client.copyObject(s, s1, s2, s3);
    }

    @Override
    public CopyObjectResult copyObject(CopyObjectRequest copyObjectRequest) {
        return amazonS3Client.copyObject(copyObjectRequest);
    }

    @Override
    public CopyPartResult copyPart(CopyPartRequest copyPartRequest) {
        return amazonS3Client.copyPart(copyPartRequest);
    }

    @Override
    public void deleteObject(String s, String s1) {
        amazonS3Client.deleteObject(s, s1);
    }

    @Override
    public void deleteObject(DeleteObjectRequest deleteObjectRequest) {
        amazonS3Client.deleteObject(deleteObjectRequest);
    }

    @Override
    public DeleteObjectsResult deleteObjects(DeleteObjectsRequest deleteObjectsRequest) {
        return amazonS3Client.deleteObjects(deleteObjectsRequest);
    }

    @Override
    public void deleteVersion(String s, String s1, String s2) {
        amazonS3Client.deleteVersion(s, s1, s2);
    }

    @Override
    public void deleteVersion(DeleteVersionRequest deleteVersionRequest) {
        amazonS3Client.deleteVersion(deleteVersionRequest);
    }

    @Override
    public BucketLoggingConfiguration getBucketLoggingConfiguration(String s) {
        return amazonS3Client.getBucketLoggingConfiguration(s);
    }

    @Override
    public BucketLoggingConfiguration getBucketLoggingConfiguration(GetBucketLoggingConfigurationRequest getBucketLoggingConfigurationRequest) {
        return amazonS3Client.getBucketLoggingConfiguration(getBucketLoggingConfigurationRequest);
    }

    @Override
    public void setBucketLoggingConfiguration(SetBucketLoggingConfigurationRequest setBucketLoggingConfigurationRequest) {
        amazonS3Client.setBucketLoggingConfiguration(setBucketLoggingConfigurationRequest);
    }

    @Override
    public BucketVersioningConfiguration getBucketVersioningConfiguration(String s) {
        return amazonS3Client.getBucketVersioningConfiguration(s);
    }

    @Override
    public BucketVersioningConfiguration getBucketVersioningConfiguration(GetBucketVersioningConfigurationRequest getBucketVersioningConfigurationRequest) {
        return amazonS3Client.getBucketVersioningConfiguration(getBucketVersioningConfigurationRequest);
    }

    @Override
    public void setBucketVersioningConfiguration(SetBucketVersioningConfigurationRequest setBucketVersioningConfigurationRequest) {
        amazonS3Client.setBucketVersioningConfiguration(setBucketVersioningConfigurationRequest);
    }

    @Override
    public BucketLifecycleConfiguration getBucketLifecycleConfiguration(String s) {
        return amazonS3Client.getBucketLifecycleConfiguration(s);
    }

    @Override
    public BucketLifecycleConfiguration getBucketLifecycleConfiguration(GetBucketLifecycleConfigurationRequest getBucketLifecycleConfigurationRequest) {
        return amazonS3Client.getBucketLifecycleConfiguration(getBucketLifecycleConfigurationRequest);
    }

    @Override
    public ListBucketIntelligentTieringConfigurationsResult listBucketIntelligentTieringConfigurations(ListBucketIntelligentTieringConfigurationsRequest listBucketIntelligentTieringConfigurationsRequest) throws AmazonServiceException, SdkClientException {
        return amazonS3Client.listBucketIntelligentTieringConfigurations(listBucketIntelligentTieringConfigurationsRequest);
    }

    @Override
    public GetBucketIntelligentTieringConfigurationResult getBucketIntelligentTieringConfiguration(GetBucketIntelligentTieringConfigurationRequest getBucketIntelligentTieringConfigurationRequest) throws AmazonServiceException, SdkClientException {
        return amazonS3Client.getBucketIntelligentTieringConfiguration(getBucketIntelligentTieringConfigurationRequest);
    }

    @Override
    public GetBucketIntelligentTieringConfigurationResult getBucketIntelligentTieringConfiguration(String s, String s1) throws AmazonServiceException, SdkClientException {
        return amazonS3Client.getBucketIntelligentTieringConfiguration(s, s1);
    }

    @Override
    public SetBucketIntelligentTieringConfigurationResult setBucketIntelligentTieringConfiguration(String s, IntelligentTieringConfiguration intelligentTieringConfiguration) throws AmazonServiceException, SdkClientException {
        return amazonS3Client.setBucketIntelligentTieringConfiguration(s, intelligentTieringConfiguration);
    }

    @Override
    public SetBucketIntelligentTieringConfigurationResult setBucketIntelligentTieringConfiguration(SetBucketIntelligentTieringConfigurationRequest setBucketIntelligentTieringConfigurationRequest) throws AmazonServiceException, SdkClientException {
        return amazonS3Client.setBucketIntelligentTieringConfiguration(setBucketIntelligentTieringConfigurationRequest);
    }

    @Override
    public DeleteBucketIntelligentTieringConfigurationResult deleteBucketIntelligentTieringConfiguration(String s, String s1) throws AmazonServiceException, SdkClientException {
        return amazonS3Client.deleteBucketIntelligentTieringConfiguration(s, s1);
    }

    @Override
    public DeleteBucketIntelligentTieringConfigurationResult deleteBucketIntelligentTieringConfiguration(DeleteBucketIntelligentTieringConfigurationRequest deleteBucketIntelligentTieringConfigurationRequest) throws AmazonServiceException, SdkClientException {
        return amazonS3Client.deleteBucketIntelligentTieringConfiguration(deleteBucketIntelligentTieringConfigurationRequest);
    }

    @Override
    public void setBucketLifecycleConfiguration(String s, BucketLifecycleConfiguration bucketLifecycleConfiguration) {
        amazonS3Client.setBucketLifecycleConfiguration(s, bucketLifecycleConfiguration);
    }

    @Override
    public void setBucketLifecycleConfiguration(SetBucketLifecycleConfigurationRequest setBucketLifecycleConfigurationRequest) {
        amazonS3Client.setBucketLifecycleConfiguration(setBucketLifecycleConfigurationRequest);
    }

    @Override
    public void deleteBucketLifecycleConfiguration(String s) {
        amazonS3Client.deleteBucketLifecycleConfiguration(s);
    }

    @Override
    public void deleteBucketLifecycleConfiguration(DeleteBucketLifecycleConfigurationRequest deleteBucketLifecycleConfigurationRequest) {
        amazonS3Client.deleteBucketLifecycleConfiguration(deleteBucketLifecycleConfigurationRequest);
    }

    @Override
    public BucketCrossOriginConfiguration getBucketCrossOriginConfiguration(String s) {
        return amazonS3Client.getBucketCrossOriginConfiguration(s);
    }

    @Override
    public BucketCrossOriginConfiguration getBucketCrossOriginConfiguration(GetBucketCrossOriginConfigurationRequest getBucketCrossOriginConfigurationRequest) {
        return amazonS3Client.getBucketCrossOriginConfiguration(getBucketCrossOriginConfigurationRequest);
    }

    @Override
    public void setBucketCrossOriginConfiguration(String s, BucketCrossOriginConfiguration bucketCrossOriginConfiguration) {
        amazonS3Client.setBucketCrossOriginConfiguration(s, bucketCrossOriginConfiguration);
    }

    @Override
    public void setBucketCrossOriginConfiguration(SetBucketCrossOriginConfigurationRequest setBucketCrossOriginConfigurationRequest) {
        amazonS3Client.setBucketCrossOriginConfiguration(setBucketCrossOriginConfigurationRequest);
    }

    @Override
    public void deleteBucketCrossOriginConfiguration(String s) {
        amazonS3Client.deleteBucketCrossOriginConfiguration(s);
    }

    @Override
    public void deleteBucketCrossOriginConfiguration(DeleteBucketCrossOriginConfigurationRequest deleteBucketCrossOriginConfigurationRequest) {
        amazonS3Client.deleteBucketCrossOriginConfiguration(deleteBucketCrossOriginConfigurationRequest);
    }

    @Override
    public BucketTaggingConfiguration getBucketTaggingConfiguration(String s) {
        return amazonS3Client.getBucketTaggingConfiguration(s);
    }

    @Override
    public BucketTaggingConfiguration getBucketTaggingConfiguration(GetBucketTaggingConfigurationRequest getBucketTaggingConfigurationRequest) {
        return amazonS3Client.getBucketTaggingConfiguration(getBucketTaggingConfigurationRequest);
    }

    @Override
    public void setBucketTaggingConfiguration(String s, BucketTaggingConfiguration bucketTaggingConfiguration) {
        amazonS3Client.setBucketTaggingConfiguration(s, bucketTaggingConfiguration);
    }

    @Override
    public void setBucketTaggingConfiguration(SetBucketTaggingConfigurationRequest setBucketTaggingConfigurationRequest) {
        amazonS3Client.setBucketTaggingConfiguration(setBucketTaggingConfigurationRequest);
    }

    @Override
    public void deleteBucketTaggingConfiguration(String s) {
        amazonS3Client.deleteBucketTaggingConfiguration(s);
    }

    @Override
    public void deleteBucketTaggingConfiguration(DeleteBucketTaggingConfigurationRequest deleteBucketTaggingConfigurationRequest) {
        amazonS3Client.deleteBucketTaggingConfiguration(deleteBucketTaggingConfigurationRequest);
    }

    @Override
    public BucketNotificationConfiguration getBucketNotificationConfiguration(String s) {
        return amazonS3Client.getBucketNotificationConfiguration(s);
    }

    @Override
    public BucketNotificationConfiguration getBucketNotificationConfiguration(GetBucketNotificationConfigurationRequest getBucketNotificationConfigurationRequest) {
        return amazonS3Client.getBucketNotificationConfiguration(getBucketNotificationConfigurationRequest);
    }

    @Override
    public void setBucketNotificationConfiguration(SetBucketNotificationConfigurationRequest setBucketNotificationConfigurationRequest) {
        amazonS3Client.setBucketNotificationConfiguration(setBucketNotificationConfigurationRequest);
    }

    @Override
    public void setBucketNotificationConfiguration(String s, BucketNotificationConfiguration bucketNotificationConfiguration) {
        amazonS3Client.setBucketNotificationConfiguration(s, bucketNotificationConfiguration);
    }

    @Override
    public BucketWebsiteConfiguration getBucketWebsiteConfiguration(String s) {
        return amazonS3Client.getBucketWebsiteConfiguration(s);
    }

    @Override
    public BucketWebsiteConfiguration getBucketWebsiteConfiguration(GetBucketWebsiteConfigurationRequest getBucketWebsiteConfigurationRequest) {
        return amazonS3Client.getBucketWebsiteConfiguration(getBucketWebsiteConfigurationRequest);
    }

    @Override
    public void setBucketWebsiteConfiguration(String s, BucketWebsiteConfiguration bucketWebsiteConfiguration) {
        amazonS3Client.setBucketWebsiteConfiguration(s, bucketWebsiteConfiguration);
    }

    @Override
    public void setBucketWebsiteConfiguration(SetBucketWebsiteConfigurationRequest setBucketWebsiteConfigurationRequest) {
        amazonS3Client.setBucketWebsiteConfiguration(setBucketWebsiteConfigurationRequest);
    }

    @Override
    public void deleteBucketWebsiteConfiguration(String s) {
        amazonS3Client.deleteBucketWebsiteConfiguration(s);
    }

    @Override
    public void deleteBucketWebsiteConfiguration(DeleteBucketWebsiteConfigurationRequest deleteBucketWebsiteConfigurationRequest) {
        amazonS3Client.deleteBucketWebsiteConfiguration(deleteBucketWebsiteConfigurationRequest);
    }

    @Override
    public BucketPolicy getBucketPolicy(String s) {
        return amazonS3Client.getBucketPolicy(s);
    }

    @Override
    public BucketPolicy getBucketPolicy(GetBucketPolicyRequest getBucketPolicyRequest) {
        return amazonS3Client.getBucketPolicy(getBucketPolicyRequest);
    }

    @Override
    public void setBucketPolicy(String s, String s1) {
        amazonS3Client.setBucketPolicy(s, s1);
    }

    @Override
    public void setBucketPolicy(SetBucketPolicyRequest setBucketPolicyRequest) {
        amazonS3Client.setBucketPolicy(setBucketPolicyRequest);
    }

    @Override
    public void deleteBucketPolicy(String s) {
        amazonS3Client.deleteBucketPolicy(s);
    }

    @Override
    public void deleteBucketPolicy(DeleteBucketPolicyRequest deleteBucketPolicyRequest) {
        amazonS3Client.deleteBucketPolicy(deleteBucketPolicyRequest);
    }

    @Override
    public URL generatePresignedUrl(String s, String s1, Date date) {
        return amazonS3Client.generatePresignedUrl(s, s1, date);
    }

    @Override
    public URL generatePresignedUrl(String s, String s1, Date date, HttpMethod httpMethod) {
        return amazonS3Client.generatePresignedUrl(s, s1, date, httpMethod);
    }

    @Override
    public URL generatePresignedUrl(GeneratePresignedUrlRequest generatePresignedUrlRequest) {
        return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
    }

    @Override
    public InitiateMultipartUploadResult initiateMultipartUpload(InitiateMultipartUploadRequest initiateMultipartUploadRequest) {
        return amazonS3Client.initiateMultipartUpload(initiateMultipartUploadRequest);
    }

    @Override
    public UploadPartResult uploadPart(UploadPartRequest uploadPartRequest) {
        return amazonS3Client.uploadPart(uploadPartRequest);
    }

    @Override
    public PartListing listParts(ListPartsRequest listPartsRequest) {
        return amazonS3Client.listParts(listPartsRequest);
    }

    @Override
    public void abortMultipartUpload(AbortMultipartUploadRequest abortMultipartUploadRequest) {
        amazonS3Client.abortMultipartUpload(abortMultipartUploadRequest);
    }

    @Override
    public CompleteMultipartUploadResult completeMultipartUpload(CompleteMultipartUploadRequest completeMultipartUploadRequest) {
        return amazonS3Client.completeMultipartUpload(completeMultipartUploadRequest);
    }

    @Override
    public MultipartUploadListing listMultipartUploads(ListMultipartUploadsRequest listMultipartUploadsRequest) {
        return amazonS3Client.listMultipartUploads(listMultipartUploadsRequest);
    }

    @Override
    public S3ResponseMetadata getCachedResponseMetadata(AmazonWebServiceRequest amazonWebServiceRequest) {
        return amazonS3Client.getCachedResponseMetadata(amazonWebServiceRequest);
    }

    @Override
    public void restoreObject(RestoreObjectRequest restoreObjectRequest) {
        throw new UnsupportedOperationException("The S3 Service method restoreObject is deprecated and should not be " +
                "used. See Amazon S3 API documentation for more information. [AmazonS3.restoreObject(RestoreObjectRequest restoreObjectRequest);]");
    }

    @Override
    public RestoreObjectResult restoreObjectV2(RestoreObjectRequest restoreObjectRequest) {
        return amazonS3Client.restoreObjectV2(restoreObjectRequest);
    }

    @Override
    public void restoreObject(String s, String s1, int i) {
        throw new UnsupportedOperationException("The S3 Service method restoreObject is deprecated and should not be " +
                "used. See Amazon S3 API documentation for more information. [AmazonS3.restoreObject(String s, String s1, int i);]");
    }

    @Override
    public void enableRequesterPays(String s) {
        amazonS3Client.enableRequesterPays(s);
    }

    @Override
    public void disableRequesterPays(String s) {
        amazonS3Client.disableRequesterPays(s);
    }

    @Override
    public boolean isRequesterPaysEnabled(String s) {
        return amazonS3Client.isRequesterPaysEnabled(s);
    }

    @Override
    public void setRequestPaymentConfiguration(SetRequestPaymentConfigurationRequest setRequestPaymentConfigurationRequest) {
        amazonS3Client.setRequestPaymentConfiguration(setRequestPaymentConfigurationRequest);
    }

    @Override
    public void setBucketReplicationConfiguration(String s, BucketReplicationConfiguration bucketReplicationConfiguration) {
        amazonS3Client.setBucketReplicationConfiguration(s, bucketReplicationConfiguration);
    }

    @Override
    public void setBucketReplicationConfiguration(SetBucketReplicationConfigurationRequest setBucketReplicationConfigurationRequest) {
        amazonS3Client.setBucketReplicationConfiguration(setBucketReplicationConfigurationRequest);
    }

    @Override
    public BucketReplicationConfiguration getBucketReplicationConfiguration(String s) {
        return amazonS3Client.getBucketReplicationConfiguration(s);
    }

    @Override
    public BucketReplicationConfiguration getBucketReplicationConfiguration(GetBucketReplicationConfigurationRequest getBucketReplicationConfigurationRequest) {
        return amazonS3Client.getBucketReplicationConfiguration(getBucketReplicationConfigurationRequest);
    }

    @Override
    public void deleteBucketReplicationConfiguration(String s) {
        amazonS3Client.deleteBucketReplicationConfiguration(s);
    }

    @Override
    public void deleteBucketReplicationConfiguration(DeleteBucketReplicationConfigurationRequest deleteBucketReplicationConfigurationRequest) {
        amazonS3Client.deleteBucketReplicationConfiguration(deleteBucketReplicationConfigurationRequest);
    }

    @Override
    public boolean doesObjectExist(String s, String s1) {
        return amazonS3Client.doesObjectExist(s, s1);
    }

    @Override
    public BucketAccelerateConfiguration getBucketAccelerateConfiguration(String s) {
        return amazonS3Client.getBucketAccelerateConfiguration(s);
    }

    @Override
    public BucketAccelerateConfiguration getBucketAccelerateConfiguration(GetBucketAccelerateConfigurationRequest getBucketAccelerateConfigurationRequest) {
        return amazonS3Client.getBucketAccelerateConfiguration(getBucketAccelerateConfigurationRequest);
    }

    @Override
    public void setBucketAccelerateConfiguration(String s, BucketAccelerateConfiguration bucketAccelerateConfiguration) {
        amazonS3Client.setBucketAccelerateConfiguration(s, bucketAccelerateConfiguration);
    }

    @Override
    public void setBucketAccelerateConfiguration(SetBucketAccelerateConfigurationRequest setBucketAccelerateConfigurationRequest) {
        amazonS3Client.setBucketAccelerateConfiguration(setBucketAccelerateConfigurationRequest);
    }

    @Override
    public DeleteBucketMetricsConfigurationResult deleteBucketMetricsConfiguration(String s, String s1) {
        return amazonS3Client.deleteBucketMetricsConfiguration(s, s1);
    }

    @Override
    public DeleteBucketMetricsConfigurationResult deleteBucketMetricsConfiguration(DeleteBucketMetricsConfigurationRequest deleteBucketMetricsConfigurationRequest) {
        return amazonS3Client.deleteBucketMetricsConfiguration(deleteBucketMetricsConfigurationRequest);
    }

    @Override
    public GetBucketMetricsConfigurationResult getBucketMetricsConfiguration(String s, String s1) {
        return amazonS3Client.getBucketMetricsConfiguration(s, s1);
    }

    @Override
    public GetBucketMetricsConfigurationResult getBucketMetricsConfiguration(GetBucketMetricsConfigurationRequest getBucketMetricsConfigurationRequest) {
        return amazonS3Client.getBucketMetricsConfiguration(getBucketMetricsConfigurationRequest);
    }

    @Override
    public SetBucketMetricsConfigurationResult setBucketMetricsConfiguration(String s, MetricsConfiguration metricsConfiguration) {
        return amazonS3Client.setBucketMetricsConfiguration(s, metricsConfiguration);
    }

    @Override
    public SetBucketMetricsConfigurationResult setBucketMetricsConfiguration(SetBucketMetricsConfigurationRequest setBucketMetricsConfigurationRequest) {
        return amazonS3Client.setBucketMetricsConfiguration(setBucketMetricsConfigurationRequest);
    }

    @Override
    public ListBucketMetricsConfigurationsResult listBucketMetricsConfigurations(ListBucketMetricsConfigurationsRequest listBucketMetricsConfigurationsRequest) {
        return amazonS3Client.listBucketMetricsConfigurations(listBucketMetricsConfigurationsRequest);
    }

    @Override
    public DeleteBucketAnalyticsConfigurationResult deleteBucketAnalyticsConfiguration(String s, String s1) {
        return amazonS3Client.deleteBucketAnalyticsConfiguration(s, s1);
    }

    @Override
    public DeleteBucketAnalyticsConfigurationResult deleteBucketAnalyticsConfiguration(DeleteBucketAnalyticsConfigurationRequest deleteBucketAnalyticsConfigurationRequest) {
        return amazonS3Client.deleteBucketAnalyticsConfiguration(deleteBucketAnalyticsConfigurationRequest);
    }

    @Override
    public GetBucketAnalyticsConfigurationResult getBucketAnalyticsConfiguration(String s, String s1) {
        return amazonS3Client.getBucketAnalyticsConfiguration(s, s1);
    }

    @Override
    public GetBucketAnalyticsConfigurationResult getBucketAnalyticsConfiguration(GetBucketAnalyticsConfigurationRequest getBucketAnalyticsConfigurationRequest) {
        return amazonS3Client.getBucketAnalyticsConfiguration(getBucketAnalyticsConfigurationRequest);
    }

    @Override
    public SetBucketAnalyticsConfigurationResult setBucketAnalyticsConfiguration(String s, AnalyticsConfiguration analyticsConfiguration) {
        return amazonS3Client.setBucketAnalyticsConfiguration(s, analyticsConfiguration);
    }

    @Override
    public SetBucketAnalyticsConfigurationResult setBucketAnalyticsConfiguration(SetBucketAnalyticsConfigurationRequest setBucketAnalyticsConfigurationRequest) {
        return amazonS3Client.setBucketAnalyticsConfiguration(setBucketAnalyticsConfigurationRequest);
    }

    @Override
    public ListBucketAnalyticsConfigurationsResult listBucketAnalyticsConfigurations(ListBucketAnalyticsConfigurationsRequest listBucketAnalyticsConfigurationsRequest) {
        return amazonS3Client.listBucketAnalyticsConfigurations(listBucketAnalyticsConfigurationsRequest);
    }

    @Override
    public DeleteBucketInventoryConfigurationResult deleteBucketInventoryConfiguration(String s, String s1) {
        return amazonS3Client.deleteBucketInventoryConfiguration(s, s1);
    }

    @Override
    public DeleteBucketInventoryConfigurationResult deleteBucketInventoryConfiguration(DeleteBucketInventoryConfigurationRequest deleteBucketInventoryConfigurationRequest) {
        return amazonS3Client.deleteBucketInventoryConfiguration(deleteBucketInventoryConfigurationRequest);
    }

    @Override
    public GetBucketInventoryConfigurationResult getBucketInventoryConfiguration(String s, String s1) {
        return amazonS3Client.getBucketInventoryConfiguration(s, s1);
    }

    @Override
    public GetBucketInventoryConfigurationResult getBucketInventoryConfiguration(GetBucketInventoryConfigurationRequest getBucketInventoryConfigurationRequest) {
        return amazonS3Client.getBucketInventoryConfiguration(getBucketInventoryConfigurationRequest);
    }

    @Override
    public SetBucketInventoryConfigurationResult setBucketInventoryConfiguration(String s, InventoryConfiguration inventoryConfiguration) {
        return amazonS3Client.setBucketInventoryConfiguration(s, inventoryConfiguration);
    }

    @Override
    public SetBucketInventoryConfigurationResult setBucketInventoryConfiguration(SetBucketInventoryConfigurationRequest setBucketInventoryConfigurationRequest) {
        return amazonS3Client.setBucketInventoryConfiguration(setBucketInventoryConfigurationRequest);
    }

    @Override
    public ListBucketInventoryConfigurationsResult listBucketInventoryConfigurations(ListBucketInventoryConfigurationsRequest listBucketInventoryConfigurationsRequest) {
        return amazonS3Client.listBucketInventoryConfigurations(listBucketInventoryConfigurationsRequest);
    }

    @Override
    public DeleteBucketEncryptionResult deleteBucketEncryption(String s) {
        return amazonS3Client.deleteBucketEncryption(s);
    }

    @Override
    public DeleteBucketEncryptionResult deleteBucketEncryption(DeleteBucketEncryptionRequest deleteBucketEncryptionRequest) {
        return amazonS3Client.deleteBucketEncryption(deleteBucketEncryptionRequest);
    }

    @Override
    public GetBucketEncryptionResult getBucketEncryption(String s) {
        return amazonS3Client.getBucketEncryption(s);
    }

    @Override
    public GetBucketEncryptionResult getBucketEncryption(GetBucketEncryptionRequest getBucketEncryptionRequest) {
        return amazonS3Client.getBucketEncryption(getBucketEncryptionRequest);
    }

    @Override
    public SetBucketEncryptionResult setBucketEncryption(SetBucketEncryptionRequest setBucketEncryptionRequest) {
        return amazonS3Client.setBucketEncryption(setBucketEncryptionRequest);
    }

    @Override
    public SelectObjectContentResult selectObjectContent(SelectObjectContentRequest selectObjectContentRequest) {
        return amazonS3Client.selectObjectContent(selectObjectContentRequest);
    }
    
    @Override
	public SetPublicAccessBlockResult setPublicAccessBlock(SetPublicAccessBlockRequest request) {
    	return amazonS3Client.setPublicAccessBlock(request);
	}

	@Override
	public GetPublicAccessBlockResult getPublicAccessBlock(GetPublicAccessBlockRequest request) {
		return amazonS3Client.getPublicAccessBlock(request);
	}

	@Override
	public DeletePublicAccessBlockResult deletePublicAccessBlock(DeletePublicAccessBlockRequest request) {
		return amazonS3Client.deletePublicAccessBlock(request);
	}

	@Override
	public GetBucketPolicyStatusResult getBucketPolicyStatus(GetBucketPolicyStatusRequest request) {
		return amazonS3Client.getBucketPolicyStatus(request);
	}

	@Override
	public SetObjectLegalHoldResult setObjectLegalHold(SetObjectLegalHoldRequest setObjectLegalHoldRequest) {
		return amazonS3Client.setObjectLegalHold(setObjectLegalHoldRequest);
	}

	@Override
	public GetObjectLegalHoldResult getObjectLegalHold(GetObjectLegalHoldRequest getObjectLegalHoldRequest) {
		return amazonS3Client.getObjectLegalHold(getObjectLegalHoldRequest);
	}
    
    @Override
	public SetObjectLockConfigurationResult setObjectLockConfiguration(
			SetObjectLockConfigurationRequest setObjectLockConfigurationRequest) {
		return amazonS3Client.setObjectLockConfiguration(setObjectLockConfigurationRequest);
	}

	@Override
	public GetObjectLockConfigurationResult getObjectLockConfiguration(
			GetObjectLockConfigurationRequest getObjectLockConfigurationRequest) {
		return amazonS3Client.getObjectLockConfiguration(getObjectLockConfigurationRequest);
	}
    
    @Override
    public SetObjectRetentionResult setObjectRetention(SetObjectRetentionRequest setObjectRetentionRequest) {
    	return amazonS3Client.setObjectRetention(setObjectRetentionRequest);
    }
    
    @Override
    public GetObjectRetentionResult getObjectRetention(GetObjectRetentionRequest getObjectRetentionRequest) {
    	return amazonS3Client.getObjectRetention(getObjectRetentionRequest);
    }
    
    @Override
    public PresignedUrlDownloadResult download(PresignedUrlDownloadRequest presignedUrlDownloadRequest) {
    	return amazonS3Client.download(presignedUrlDownloadRequest);
    }
    
    @Override
    public void download(PresignedUrlDownloadRequest presignedUrlDownloadRequest, File destinationFile) {
    	amazonS3Client.download(presignedUrlDownloadRequest, destinationFile);	
    }
    
    @Override
    public PresignedUrlUploadResult upload(PresignedUrlUploadRequest presignedUrlUploadRequest) {
    	return amazonS3Client.upload(presignedUrlUploadRequest);	
    }

    @Override
    public void shutdown() {
        amazonS3Client.shutdown();
    }

    @Override
    public com.amazonaws.services.s3.model.Region getRegion() {
        return amazonS3Client.getRegion();
    }

    @Override
    public String getRegionName() {
        return amazonS3Client.getRegionName();
    }

    @Override
    public URL getUrl(String s, String s1) {
        return amazonS3Client.getUrl(s, s1);
    }

    @Override
    public AmazonS3Waiters waiters() {
        return amazonS3Client.waiters();
    }

    public AmazonS3 getWrappedClient() {
        return amazonS3Client;
    }

    public void setWrappedClient(AmazonS3 amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    public S3Properties.S3Bucket getRelatedBucket() {
        return relatedBucket;
    }

    public void setRelatedBucket(S3Properties.S3Bucket relatedBucket) {
        this.relatedBucket = relatedBucket;
    }
}