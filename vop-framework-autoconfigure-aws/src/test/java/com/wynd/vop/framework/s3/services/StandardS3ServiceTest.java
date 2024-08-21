package com.wynd.vop.framework.s3.services;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.HttpMethod;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.model.analytics.AnalyticsConfiguration;
import com.amazonaws.services.s3.model.intelligenttiering.IntelligentTieringConfiguration;
import com.amazonaws.services.s3.model.inventory.InventoryConfiguration;
import com.amazonaws.services.s3.model.metrics.MetricsConfiguration;
import com.amazonaws.services.s3.model.ownership.OwnershipControls;
import com.wynd.vop.framework.s3.config.S3Properties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = StandardS3Service.class)
public class StandardS3ServiceTest {

	private static final String TEST_STRING = "A Test String";
	private static final Region TEST_REGION = Region.getRegion(Regions.GovCloud);

	private StandardS3Service instance;

	@MockBean
	S3Properties mockS3Properties;

	@Mock
	AmazonS3 mockAmazonS3Client;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);

		instance = new StandardS3Service();
		instance.setWrappedClient(mockAmazonS3Client);
	}

	@Test
	public void testConstructor() {
		assertNotNull(instance);
	}

	@Test
	public void testSetEndpoint() {
		String s = TEST_STRING;

		instance.setEndpoint(s);

		Mockito.verify(mockAmazonS3Client, times(1)).setEndpoint(TEST_STRING);
	}

	@Test
	public void testSetRegion() {
		Region region = TEST_REGION;

		instance.setRegion(region);

		Mockito.verify(mockAmazonS3Client, times(1)).setRegion(TEST_REGION);
	}

	@Test
	public void testSetS3ClientOptions() {
		S3ClientOptions s3ClientOptions = S3ClientOptions.builder().build();

		instance.setS3ClientOptions(s3ClientOptions);

		Mockito.verify(mockAmazonS3Client, times(1)).setS3ClientOptions(s3ClientOptions);
	}

	@Test
	public void testChangeObjectStorageClass() {
		try {
			String s = TEST_STRING;
			String s1 = TEST_STRING;
			StorageClass storageClass = StorageClass.Standard;

			instance.changeObjectStorageClass(s, s1, storageClass);

			fail();
		} catch(UnsupportedOperationException e) {
			assertNotNull(e);
			assertNotNull(e.getMessage());
		}
	}

	@Test
	public void testSetObjectRedirectLocation() {
		try {
			String s = TEST_STRING;
			String s1 = TEST_STRING;
			String s2 = TEST_STRING;

			instance.setObjectRedirectLocation(s, s1, s2);

			fail();
		} catch(UnsupportedOperationException e) {
			assertNotNull(e);
			assertNotNull(e.getMessage());
		}
	}

	@Test
	public void testListObjects() {
		String s = TEST_STRING;

		instance.listObjects(s);

		Mockito.verify(mockAmazonS3Client, times(1)).listObjects(TEST_STRING);
	}

	@Test
	public void testListObjects_1() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;

		instance.listObjects(s, s1);

		Mockito.verify(mockAmazonS3Client, times(1)).listObjects(TEST_STRING, TEST_STRING);
	}

	@Test
	public void testListObjects_2() {
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest();

		instance.listObjects(listObjectsRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).listObjects(listObjectsRequest);
	}

	@Test
	public void testListObjectsV2() {
		String s = TEST_STRING;

		instance.listObjectsV2(s);

		Mockito.verify(mockAmazonS3Client, times(1)).listObjectsV2(TEST_STRING);
	}

	@Test
	public void testListObjectsV2_1() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;

		instance.listObjectsV2(s, s1);

		Mockito.verify(mockAmazonS3Client, times(1)).listObjectsV2(TEST_STRING, TEST_STRING);
	}

	@Test
	public void testListObjectsV2_2() {
		ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request();

		instance.listObjectsV2(listObjectsV2Request);

		Mockito.verify(mockAmazonS3Client, times(1)).listObjectsV2(listObjectsV2Request);
	}

	@Test
	public void testListNextBatchOfObjects() {
		ObjectListing objectListing = new ObjectListing();

		instance.listNextBatchOfObjects(objectListing);

		Mockito.verify(mockAmazonS3Client, times(1)).listNextBatchOfObjects(objectListing);
	}

	@Test
	public void testListNextBatchOfObjects_1() {
		ListNextBatchOfObjectsRequest listNextBatchOfObjectsRequest = null;

		instance.listNextBatchOfObjects(listNextBatchOfObjectsRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).listNextBatchOfObjects(listNextBatchOfObjectsRequest);
	}

	@Test
	public void testListVersions() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;

		instance.listVersions(s, s1);

		Mockito.verify(mockAmazonS3Client, times(1)).listVersions(TEST_STRING, TEST_STRING);
	}

	@Test
	public void testListNextBatchOfVersions() {
		VersionListing versionListing = new VersionListing();

		instance.listNextBatchOfVersions(versionListing);

		Mockito.verify(mockAmazonS3Client, times(1)).listNextBatchOfVersions(versionListing);
	}

	@Test
	public void testListNextBatchOfVersions_1() {
		ListNextBatchOfVersionsRequest listNextBatchOfVersionsRequest = null;

		instance.listNextBatchOfVersions(listNextBatchOfVersionsRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).listNextBatchOfVersions(listNextBatchOfVersionsRequest);
	}

	@Test
	public void testListVersions_1() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;
		String s2 = TEST_STRING;
		String s3 = TEST_STRING;
		String s4 = TEST_STRING;
		Integer integer = null;

		instance.listVersions(s, s1, s2, s3, s4, integer);

		Mockito.verify(mockAmazonS3Client, times(1)).listVersions(TEST_STRING, TEST_STRING, TEST_STRING, TEST_STRING, TEST_STRING, null);
	}

	@Test
	public void testListVersions_2() {
		ListVersionsRequest listVersionsRequest = new ListVersionsRequest();

		instance.listVersions(listVersionsRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).listVersions(listVersionsRequest);
	}

	@Test
	public void testGetS3AccountOwner() {
		instance.getS3AccountOwner();

		Mockito.verify(mockAmazonS3Client, times(1)).getS3AccountOwner();
	}

	@Test
	public void testGetS3AccountOwner_1() {
		GetS3AccountOwnerRequest getS3AccountOwnerRequest = new GetS3AccountOwnerRequest();

		instance.getS3AccountOwner(getS3AccountOwnerRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).getS3AccountOwner(getS3AccountOwnerRequest);
	}

	@Test
	public void testDoesBucketExist() {
		try {
			String s = TEST_STRING;

			instance.doesBucketExist(s);

			fail();
		} catch(UnsupportedOperationException e) {
			assertNotNull(e);
			assertNotNull(e.getMessage());
		}
	}

	@Test
	public void testDoesBucketExistV2() {
		String s = TEST_STRING;

		instance.doesBucketExistV2(s);

		Mockito.verify(mockAmazonS3Client, times(1)).doesBucketExistV2(TEST_STRING);
	}

	@Test
	public void testHeadBucket() {
		HeadBucketRequest headBucketRequest = null;

		instance.headBucket(headBucketRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).headBucket(headBucketRequest);
	}

	@Test
	public void testListBuckets() {
		instance.listBuckets();

		Mockito.verify(mockAmazonS3Client, times(1)).listBuckets();
	}

	@Test
	public void testListBuckets_1() {
		ListBucketsRequest listBucketsRequest = new ListBucketsRequest();

		instance.listBuckets(listBucketsRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).listBuckets(listBucketsRequest);
	}

	@Test
	public void testGetBucketLocation() {
		String s = TEST_STRING;

		instance.getBucketLocation(s);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketLocation(TEST_STRING);
	}

	@Test
	public void testGetBucketLocation_1() {
		GetBucketLocationRequest getBucketLocationRequest = null;

		instance.getBucketLocation(getBucketLocationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketLocation(getBucketLocationRequest);
	}

	@Test
	public void testCreateBucket() {
		CreateBucketRequest createBucketRequest = null;

		instance.createBucket(createBucketRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).createBucket(createBucketRequest);
	}

	@Test
	public void testCreateBucket_1() {
		String s = TEST_STRING;

		instance.createBucket(s);

		Mockito.verify(mockAmazonS3Client, times(1)).createBucket(TEST_STRING);
	}

	@Test
	public void testCreateBucket_2() {
		try {
			String s = TEST_STRING;
			com.amazonaws.services.s3.model.Region region = null;

			instance.createBucket(s, region);

			fail();
		} catch(UnsupportedOperationException e) {
			assertNotNull(e);
			assertNotNull(e.getMessage());
		}
	}

	@Test
	public void testCreateBucket_3() {
		try {
			String s = TEST_STRING;
			String s1 = TEST_STRING;

			instance.createBucket(s, s1);

			fail();
		} catch(UnsupportedOperationException e) {
			assertNotNull(e);
			assertNotNull(e.getMessage());
		}
	}

	@Test
	public void testGetObjectAcl() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;

		instance.getObjectAcl(s, s1);

		Mockito.verify(mockAmazonS3Client, times(1)).getObjectAcl(TEST_STRING, TEST_STRING);
	}

	@Test
	public void testGetObjectAcl_1() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;
		String s2 = TEST_STRING;

		instance.getObjectAcl(s, s1, s2);

		Mockito.verify(mockAmazonS3Client, times(1)).getObjectAcl(TEST_STRING, TEST_STRING, TEST_STRING);
	}

	@Test
	public void testGetObjectAcl_2() {
		GetObjectAclRequest getObjectAclRequest = null;

		instance.getObjectAcl(getObjectAclRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).getObjectAcl(getObjectAclRequest);
	}

	@Test
	public void testSetObjectAcl() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;
		AccessControlList accessControlList = new AccessControlList();

		instance.setObjectAcl(s, s1, accessControlList);

		Mockito.verify(mockAmazonS3Client, times(1)).setObjectAcl(TEST_STRING, TEST_STRING, accessControlList);
	}

	@Test
	public void testSetObjectAcl_1() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;
		CannedAccessControlList cannedAccessControlList = null;

		instance.setObjectAcl(s, s1, cannedAccessControlList);

		Mockito.verify(mockAmazonS3Client, times(1)).setObjectAcl(TEST_STRING, TEST_STRING, cannedAccessControlList);
	}

	@Test
	public void testSetObjectAcl_2() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;
		String s2 = TEST_STRING;
		AccessControlList accessControlList = new AccessControlList();

		instance.setObjectAcl(s, s1, s2, accessControlList);

		Mockito.verify(mockAmazonS3Client, times(1)).setObjectAcl(TEST_STRING, TEST_STRING, TEST_STRING, accessControlList);
	}

	@Test
	public void testSetObjectAcl_3() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;
		String s2 = TEST_STRING;
		CannedAccessControlList cannedAccessControlList = null;

		instance.setObjectAcl(s, s1, s2, cannedAccessControlList);

		Mockito.verify(mockAmazonS3Client, times(1)).setObjectAcl(TEST_STRING, TEST_STRING, TEST_STRING, cannedAccessControlList);
	}

	@Test
	public void testSetObjectAcl_4() {
		SetObjectAclRequest setObjectAclRequest = null;

		instance.setObjectAcl(setObjectAclRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).setObjectAcl(setObjectAclRequest);
	}

	@Test
	public void testGetBucketAcl() {
		String s = TEST_STRING;

		instance.getBucketAcl(s);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketAcl(TEST_STRING);
	}

	@Test
	public void testSetBucketAcl() {
		SetBucketAclRequest setBucketAclRequest = null;

		instance.setBucketAcl(setBucketAclRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).setBucketAcl(setBucketAclRequest);
	}

	@Test
	public void testGetBucketAcl_1() {
		GetBucketAclRequest getBucketAclRequest = null;

		instance.getBucketAcl(getBucketAclRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketAcl(getBucketAclRequest);
	}

	@Test
	public void testSetBucketAcl_1() {
		String s = TEST_STRING;
		AccessControlList accessControlList = new AccessControlList();

		instance.setBucketAcl(s, accessControlList);

		Mockito.verify(mockAmazonS3Client, times(1)).setBucketAcl(TEST_STRING, accessControlList);
	}

	@Test
	public void testSetBucketAcl_2() {
		String s = TEST_STRING;
		CannedAccessControlList cannedAccessControlList = null;

		instance.setBucketAcl(s, cannedAccessControlList);

		Mockito.verify(mockAmazonS3Client, times(1)).setBucketAcl(TEST_STRING, cannedAccessControlList);
	}

	@Test
	public void testGetObjectMetadata() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;

		instance.getObjectMetadata(s, s1);

		Mockito.verify(mockAmazonS3Client, times(1)).getObjectMetadata(TEST_STRING, TEST_STRING);
	}

	@Test
	public void testGetObjectMetadata_1() {
		GetObjectMetadataRequest getObjectMetadataRequest = null;

		instance.getObjectMetadata(getObjectMetadataRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).getObjectMetadata(getObjectMetadataRequest);
	}

	@Test
	public void testGetObject() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;

		instance.getObject(s, s1);

		Mockito.verify(mockAmazonS3Client, times(1)).getObject(TEST_STRING, TEST_STRING);
	}

	@Test
	public void testGetObject_1() {
		GetObjectRequest getObjectRequest = null;

		instance.getObject(getObjectRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).getObject(getObjectRequest);
	}

	@Test
	public void testGetObject_2() {
		GetObjectRequest getObjectRequest = null;
		File file = null;

		instance.getObject(getObjectRequest, file);

		Mockito.verify(mockAmazonS3Client, times(1)).getObject(getObjectRequest, file);
	}

	@Test
	public void testGetObjectAsString() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;

		instance.getObjectAsString(s, s1);

		Mockito.verify(mockAmazonS3Client, times(1)).getObjectAsString(TEST_STRING, TEST_STRING);
	}

	@Test
	public void testGetObjectTagging() {
		GetObjectTaggingRequest getObjectTaggingRequest = null;

		instance.getObjectTagging(getObjectTaggingRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).getObjectTagging(getObjectTaggingRequest);
	}

	@Test
	public void testSetObjectTagging() {
		SetObjectTaggingRequest setObjectTaggingRequest = null;

		instance.setObjectTagging(setObjectTaggingRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).setObjectTagging(setObjectTaggingRequest);
	}

	@Test
	public void testDeleteObjectTagging() {
		DeleteObjectTaggingRequest deleteObjectTaggingRequest = null;

		instance.deleteObjectTagging(deleteObjectTaggingRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).deleteObjectTagging(deleteObjectTaggingRequest);
	}

	@Test
	public void testDeleteBucket() {
		DeleteBucketRequest deleteBucketRequest = null;

		instance.deleteBucket(deleteBucketRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).deleteBucket(deleteBucketRequest);
	}

	@Test
	public void testDeleteBucket_1() {
		String s = TEST_STRING;

		instance.deleteBucket(s);

		Mockito.verify(mockAmazonS3Client, times(1)).deleteBucket(TEST_STRING);
	}

	@Test
	public void testPutObject() {
		PutObjectRequest putObjectRequest = null;

		instance.putObject(putObjectRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).putObject(putObjectRequest);
	}

	@Test
	public void testPutObject_1() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;
		File file = null;

		instance.putObject(s, s1, file);

		Mockito.verify(mockAmazonS3Client, times(1)).putObject(TEST_STRING, TEST_STRING, file);
	}

	@Test
	public void testPutObject_2() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;
		InputStream inputStream = null;
		ObjectMetadata objectMetadata = new ObjectMetadata();

		instance.putObject(s, s1, inputStream, objectMetadata);

		Mockito.verify(mockAmazonS3Client, times(1)).putObject(TEST_STRING, TEST_STRING, inputStream, objectMetadata);
	}

	@Test
	public void testPutObject_3() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;
		String s2 = TEST_STRING;

		instance.putObject(s, s1, s2);

		Mockito.verify(mockAmazonS3Client, times(1)).putObject(TEST_STRING, TEST_STRING, TEST_STRING);
	}

	@Test
	public void testCopyObject() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;
		String s2 = TEST_STRING;
		String s3 = TEST_STRING;

		instance.copyObject(s, s1, s2, s3);

		Mockito.verify(mockAmazonS3Client, times(1)).copyObject(TEST_STRING, TEST_STRING, TEST_STRING, TEST_STRING);
	}

	@Test
	public void testCopyObject_1() {
		CopyObjectRequest copyObjectRequest = null;

		instance.copyObject(copyObjectRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).copyObject(copyObjectRequest);
	}

	@Test
	public void testCopyPart() {
		CopyPartRequest copyPartRequest = new CopyPartRequest();

		instance.copyPart(copyPartRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).copyPart(copyPartRequest);
	}

	@Test
	public void testDeleteObject() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;

		instance.deleteObject(s, s1);

		Mockito.verify(mockAmazonS3Client, times(1)).deleteObject(TEST_STRING, TEST_STRING);
	}

	@Test
	public void testDeleteObject_1() {
		DeleteObjectRequest deleteObjectRequest = null;

		instance.deleteObject(deleteObjectRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).deleteObject(deleteObjectRequest);
	}

	@Test
	public void testDeleteObjects() {
		DeleteObjectsRequest deleteObjectsRequest = null;

		instance.deleteObjects(deleteObjectsRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).deleteObjects(deleteObjectsRequest);
	}

	@Test
	public void testDeleteVersion() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;
		String s2 = TEST_STRING;

		instance.deleteVersion(s, s1, s2);

		Mockito.verify(mockAmazonS3Client, times(1)).deleteVersion(TEST_STRING, TEST_STRING, TEST_STRING);
	}

	@Test
	public void testDeleteVersion_1() {
		DeleteVersionRequest deleteVersionRequest = null;

		instance.deleteVersion(deleteVersionRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).deleteVersion(deleteVersionRequest);
	}

	@Test
	public void testGetBucketLoggingConfiguration() {
		String s = TEST_STRING;

		instance.getBucketLoggingConfiguration(s);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketLoggingConfiguration(TEST_STRING);
	}

	@Test
	public void testGetBucketLoggingConfiguration_1() {
		GetBucketLoggingConfigurationRequest getBucketLoggingConfigurationRequest = null;

		instance.getBucketLoggingConfiguration(getBucketLoggingConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketLoggingConfiguration(getBucketLoggingConfigurationRequest);
	}

	@Test
	public void testSetBucketLoggingConfiguration() {
		SetBucketLoggingConfigurationRequest setBucketLoggingConfigurationRequest = null;

		instance.setBucketLoggingConfiguration(setBucketLoggingConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).setBucketLoggingConfiguration(setBucketLoggingConfigurationRequest);
	}

	@Test
	public void testGetBucketVersioningConfiguration() {
		String s = TEST_STRING;

		instance.getBucketVersioningConfiguration(s);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketVersioningConfiguration(TEST_STRING);
	}

	@Test
	public void testGetBucketVersioningConfiguration_1() {
		GetBucketVersioningConfigurationRequest getBucketVersioningConfigurationRequest = null;

		instance.getBucketVersioningConfiguration(getBucketVersioningConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketVersioningConfiguration(getBucketVersioningConfigurationRequest);
	}

	@Test
	public void testSetBucketVersioningConfiguration() {
		SetBucketVersioningConfigurationRequest setBucketVersioningConfigurationRequest = null;

		instance.setBucketVersioningConfiguration(setBucketVersioningConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).setBucketVersioningConfiguration(setBucketVersioningConfigurationRequest);
	}

	@Test
	public void testGetBucketLifecycleConfiguration() {
		String s = TEST_STRING;

		instance.getBucketLifecycleConfiguration(s);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketLifecycleConfiguration(TEST_STRING);
	}

	@Test
	public void testGetBucketLifecycleConfiguration_1() {
		GetBucketLifecycleConfigurationRequest getBucketLifecycleConfigurationRequest = null;

		instance.getBucketLifecycleConfiguration(getBucketLifecycleConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketLifecycleConfiguration(getBucketLifecycleConfigurationRequest);
	}

	@Test
	public void testSetBucketLifecycleConfiguration() {
		String s = TEST_STRING;
		BucketLifecycleConfiguration bucketLifecycleConfiguration = new BucketLifecycleConfiguration();

		instance.setBucketLifecycleConfiguration(s, bucketLifecycleConfiguration);

		Mockito.verify(mockAmazonS3Client, times(1)).setBucketLifecycleConfiguration(TEST_STRING, bucketLifecycleConfiguration);
	}

	@Test
	public void testSetBucketLifecycleConfiguration_1() {
		SetBucketLifecycleConfigurationRequest setBucketLifecycleConfigurationRequest = null;

		instance.setBucketLifecycleConfiguration(setBucketLifecycleConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).setBucketLifecycleConfiguration(setBucketLifecycleConfigurationRequest);
	}

	@Test
	public void testDeleteBucketLifecycleConfiguration() {
		String s = TEST_STRING;

		instance.deleteBucketLifecycleConfiguration(s);

		Mockito.verify(mockAmazonS3Client, times(1)).deleteBucketLifecycleConfiguration(TEST_STRING);
	}

	@Test
	public void testDeleteBucketLifecycleConfiguration_1() {
		DeleteBucketLifecycleConfigurationRequest deleteBucketLifecycleConfigurationRequest = null;

		instance.deleteBucketLifecycleConfiguration(deleteBucketLifecycleConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).deleteBucketLifecycleConfiguration(deleteBucketLifecycleConfigurationRequest);
	}

	@Test
	public void testGetBucketCrossOriginConfiguration() {
		String s = TEST_STRING;

		instance.getBucketCrossOriginConfiguration(s);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketCrossOriginConfiguration(TEST_STRING);
	}

	@Test
	public void testGetBucketCrossOriginConfiguration_1() {
		GetBucketCrossOriginConfigurationRequest getBucketCrossOriginConfigurationRequest = null;

		instance.getBucketCrossOriginConfiguration(getBucketCrossOriginConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketCrossOriginConfiguration(getBucketCrossOriginConfigurationRequest);
	}

	@Test
	public void testSetBucketCrossOriginConfiguration() {
		String s = TEST_STRING;
		BucketCrossOriginConfiguration bucketCrossOriginConfiguration = new BucketCrossOriginConfiguration();

		instance.setBucketCrossOriginConfiguration(s, bucketCrossOriginConfiguration);

		Mockito.verify(mockAmazonS3Client, times(1)).setBucketCrossOriginConfiguration(TEST_STRING, bucketCrossOriginConfiguration);
	}

	@Test
	public void testSetBucketCrossOriginConfiguration_1() {
		SetBucketCrossOriginConfigurationRequest setBucketCrossOriginConfigurationRequest = null;

		instance.setBucketCrossOriginConfiguration(setBucketCrossOriginConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).setBucketCrossOriginConfiguration(setBucketCrossOriginConfigurationRequest);
	}

	@Test
	public void testDeleteBucketCrossOriginConfiguration() {
		String s = TEST_STRING;

		instance.deleteBucketCrossOriginConfiguration(s);

		Mockito.verify(mockAmazonS3Client, times(1)).deleteBucketCrossOriginConfiguration(TEST_STRING);
	}

	@Test
	public void testDeleteBucketCrossOriginConfiguration_1() {
		DeleteBucketCrossOriginConfigurationRequest deleteBucketCrossOriginConfigurationRequest = null;

		instance.deleteBucketCrossOriginConfiguration(deleteBucketCrossOriginConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).deleteBucketCrossOriginConfiguration(deleteBucketCrossOriginConfigurationRequest);
	}

	@Test
	public void testGetBucketTaggingConfiguration() {
		String s = TEST_STRING;

		instance.getBucketTaggingConfiguration(s);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketTaggingConfiguration(TEST_STRING);
	}

	@Test
	public void testGetBucketTaggingConfiguration_1() {
		GetBucketTaggingConfigurationRequest getBucketTaggingConfigurationRequest = null;

		instance.getBucketTaggingConfiguration(getBucketTaggingConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketTaggingConfiguration(getBucketTaggingConfigurationRequest);
	}

	@Test
	public void testSetBucketTaggingConfiguration() {
		String s = TEST_STRING;
		BucketTaggingConfiguration bucketTaggingConfiguration = new BucketTaggingConfiguration();

		instance.setBucketTaggingConfiguration(s, bucketTaggingConfiguration);

		Mockito.verify(mockAmazonS3Client, times(1)).setBucketTaggingConfiguration(TEST_STRING, bucketTaggingConfiguration);
	}

	@Test
	public void testSetBucketTaggingConfiguration_1() {
		SetBucketTaggingConfigurationRequest setBucketTaggingConfigurationRequest = null;

		instance.setBucketTaggingConfiguration(setBucketTaggingConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).setBucketTaggingConfiguration(setBucketTaggingConfigurationRequest);
	}

	@Test
	public void testDeleteBucketTaggingConfiguration() {
		String s = TEST_STRING;

		instance.deleteBucketTaggingConfiguration(s);

		Mockito.verify(mockAmazonS3Client, times(1)).deleteBucketTaggingConfiguration(TEST_STRING);
	}

	@Test
	public void testDeleteBucketTaggingConfiguration_1() {
		DeleteBucketTaggingConfigurationRequest deleteBucketTaggingConfigurationRequest = null;

		instance.deleteBucketTaggingConfiguration(deleteBucketTaggingConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).deleteBucketTaggingConfiguration(deleteBucketTaggingConfigurationRequest);
	}

	@Test
	public void testGetBucketNotificationConfiguration() {
		String s = TEST_STRING;

		instance.getBucketNotificationConfiguration(s);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketNotificationConfiguration(TEST_STRING);
	}

	@Test
	public void testGetBucketNotificationConfiguration_1() {
		GetBucketNotificationConfigurationRequest getBucketNotificationConfigurationRequest = null;

		instance.getBucketNotificationConfiguration(getBucketNotificationConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketNotificationConfiguration(getBucketNotificationConfigurationRequest);
	}

	@Test
	public void testSetBucketNotificationConfiguration() {
		SetBucketNotificationConfigurationRequest setBucketNotificationConfigurationRequest = null;

		instance.setBucketNotificationConfiguration(setBucketNotificationConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).setBucketNotificationConfiguration(setBucketNotificationConfigurationRequest);
	}

	@Test
	public void testSetBucketNotificationConfiguration_1() {
		String s = TEST_STRING;
		BucketNotificationConfiguration bucketNotificationConfiguration = new BucketNotificationConfiguration();

		instance.setBucketNotificationConfiguration(s, bucketNotificationConfiguration);

		Mockito.verify(mockAmazonS3Client, times(1)).setBucketNotificationConfiguration(TEST_STRING, bucketNotificationConfiguration);
	}

	@Test
	public void testGetBucketWebsiteConfiguration() {
		String s = TEST_STRING;

		instance.getBucketWebsiteConfiguration(s);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketWebsiteConfiguration(TEST_STRING);
	}

	@Test
	public void testGetBucketWebsiteConfiguration_1() {
		GetBucketWebsiteConfigurationRequest getBucketWebsiteConfigurationRequest = null;

		instance.getBucketWebsiteConfiguration(getBucketWebsiteConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketWebsiteConfiguration(getBucketWebsiteConfigurationRequest);
	}

	@Test
	public void testSetBucketWebsiteConfiguration() {
		String s = TEST_STRING;
		BucketWebsiteConfiguration bucketWebsiteConfiguration = new BucketWebsiteConfiguration();

		instance.setBucketWebsiteConfiguration(s, bucketWebsiteConfiguration);

		Mockito.verify(mockAmazonS3Client, times(1)).setBucketWebsiteConfiguration(TEST_STRING, bucketWebsiteConfiguration);
	}

	@Test
	public void testSetBucketWebsiteConfiguration_1() {
		SetBucketWebsiteConfigurationRequest setBucketWebsiteConfigurationRequest = null;

		instance.setBucketWebsiteConfiguration(setBucketWebsiteConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).setBucketWebsiteConfiguration(setBucketWebsiteConfigurationRequest);
	}

	@Test
	public void testDeleteBucketWebsiteConfiguration() {
		String s = TEST_STRING;

		instance.deleteBucketWebsiteConfiguration(s);

		Mockito.verify(mockAmazonS3Client, times(1)).deleteBucketWebsiteConfiguration(TEST_STRING);
	}

	@Test
	public void testDeleteBucketWebsiteConfiguration_1() {
		DeleteBucketWebsiteConfigurationRequest deleteBucketWebsiteConfigurationRequest = null;

		instance.deleteBucketWebsiteConfiguration(deleteBucketWebsiteConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).deleteBucketWebsiteConfiguration(deleteBucketWebsiteConfigurationRequest);
	}

	@Test
	public void testGetBucketPolicy() {
		String s = TEST_STRING;

		instance.getBucketPolicy(s);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketPolicy(TEST_STRING);
	}

	@Test
	public void testGetBucketPolicy_1() {
		GetBucketPolicyRequest getBucketPolicyRequest = null;

		instance.getBucketPolicy(getBucketPolicyRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketPolicy(getBucketPolicyRequest);
	}

	@Test
	public void testSetBucketPolicy() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;

		instance.setBucketPolicy(s, s1);

		Mockito.verify(mockAmazonS3Client, times(1)).setBucketPolicy(TEST_STRING, TEST_STRING);
	}

	@Test
	public void testSetBucketPolicy_1() {
		SetBucketPolicyRequest setBucketPolicyRequest = null;

		instance.setBucketPolicy(setBucketPolicyRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).setBucketPolicy(setBucketPolicyRequest);
	}

	@Test
	public void testDeleteBucketPolicy() {
		String s = TEST_STRING;

		instance.deleteBucketPolicy(s);

		Mockito.verify(mockAmazonS3Client, times(1)).deleteBucketPolicy(TEST_STRING);
	}

	@Test
	public void testDeleteBucketPolicy_1() {
		DeleteBucketPolicyRequest deleteBucketPolicyRequest = null;

		instance.deleteBucketPolicy(deleteBucketPolicyRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).deleteBucketPolicy(deleteBucketPolicyRequest);
	}

	@Test
	public void testGeneratePresignedUrl() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;
		Date date = new Date();

		instance.generatePresignedUrl(s, s1, date);

		Mockito.verify(mockAmazonS3Client, times(1)).generatePresignedUrl(TEST_STRING, TEST_STRING, date);
	}

	@Test
	public void testGeneratePresignedUrl_1() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;
		Date date = new Date();
		HttpMethod httpMethod = null;

		instance.generatePresignedUrl(s, s1, date, httpMethod);

		Mockito.verify(mockAmazonS3Client, times(1)).generatePresignedUrl(TEST_STRING, TEST_STRING, date, httpMethod);
	}

	@Test
	public void testGeneratePresignedUrl_2() {
		GeneratePresignedUrlRequest generatePresignedUrlRequest = null;

		instance.generatePresignedUrl(generatePresignedUrlRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).generatePresignedUrl(generatePresignedUrlRequest);
	}

	@Test
	public void testInitiateMultipartUpload() {
		InitiateMultipartUploadRequest initiateMultipartUploadRequest = null;

		instance.initiateMultipartUpload(initiateMultipartUploadRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).initiateMultipartUpload(initiateMultipartUploadRequest);
	}

	@Test
	public void testUploadPart() {
		UploadPartRequest uploadPartRequest = new UploadPartRequest();

		instance.uploadPart(uploadPartRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).uploadPart(uploadPartRequest);
	}

	@Test
	public void testListParts() {
		ListPartsRequest listPartsRequest = null;

		instance.listParts(listPartsRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).listParts(listPartsRequest);
	}

	@Test
	public void testAbortMultipartUpload() {
		AbortMultipartUploadRequest abortMultipartUploadRequest = null;

		instance.abortMultipartUpload(abortMultipartUploadRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).abortMultipartUpload(abortMultipartUploadRequest);
	}

	@Test
	public void testCompleteMultipartUpload() {
		CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest();

		instance.completeMultipartUpload(completeMultipartUploadRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).completeMultipartUpload(completeMultipartUploadRequest);
	}

	@Test
	public void testListMultipartUploads() {
		ListMultipartUploadsRequest listMultipartUploadsRequest = null;

		instance.listMultipartUploads(listMultipartUploadsRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).listMultipartUploads(listMultipartUploadsRequest);
	}

	@Test
	public void testGetCachedResponseMetadata() {
		AmazonWebServiceRequest amazonWebServiceRequest = null;

		instance.getCachedResponseMetadata(amazonWebServiceRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).getCachedResponseMetadata(amazonWebServiceRequest);
	}

	@Test
	public void testRestoreObject() {
		try {
			RestoreObjectRequest restoreObjectRequest = null;

			instance.restoreObject(restoreObjectRequest);

			fail();
		} catch(UnsupportedOperationException e) {
			assertNotNull(e);
			assertNotNull(e.getMessage());
		}
	}

	@Test
	public void testRestoreObjectV2() {
		RestoreObjectRequest restoreObjectRequest = null;

		instance.restoreObjectV2(restoreObjectRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).restoreObjectV2(restoreObjectRequest);
	}

	@Test
	public void testRestoreObject_1() {
		try {
			String s = TEST_STRING;
			String s1 = TEST_STRING;
			int i = 0;

			instance.restoreObject(s, s1, i);

			fail();
		} catch(UnsupportedOperationException e) {
			assertNotNull(e);
			assertNotNull(e.getMessage());
		}
	}

	@Test
	public void testEnableRequesterPays() {
		String s = TEST_STRING;

		instance.enableRequesterPays(s);

		Mockito.verify(mockAmazonS3Client, times(1)).enableRequesterPays(TEST_STRING);
	}

	@Test
	public void testDisableRequesterPays() {
		String s = TEST_STRING;

		instance.disableRequesterPays(s);

		Mockito.verify(mockAmazonS3Client, times(1)).disableRequesterPays(TEST_STRING);
	}

	@Test
	public void testIsRequesterPaysEnabled() {
		String s = TEST_STRING;

		instance.isRequesterPaysEnabled(s);

		Mockito.verify(mockAmazonS3Client, times(1)).isRequesterPaysEnabled(TEST_STRING);
	}

	@Test
	public void testSetBucketReplicationConfiguration() {
		String s = TEST_STRING;
		BucketReplicationConfiguration bucketReplicationConfiguration = new BucketReplicationConfiguration();

		instance.setBucketReplicationConfiguration(s, bucketReplicationConfiguration);

		Mockito.verify(mockAmazonS3Client, times(1)).setBucketReplicationConfiguration(TEST_STRING, bucketReplicationConfiguration);
	}

	@Test
	public void testSetBucketReplicationConfiguration_1() {
		SetBucketReplicationConfigurationRequest setBucketReplicationConfigurationRequest = new SetBucketReplicationConfigurationRequest();

		instance.setBucketReplicationConfiguration(setBucketReplicationConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).setBucketReplicationConfiguration(setBucketReplicationConfigurationRequest);
	}

	@Test
	public void testGetBucketReplicationConfiguration() {
		String s = TEST_STRING;

		instance.getBucketReplicationConfiguration(s);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketReplicationConfiguration(TEST_STRING);
	}

	@Test
	public void testGetBucketReplicationConfiguration_1() {
		GetBucketReplicationConfigurationRequest getBucketReplicationConfigurationRequest = null;

		instance.getBucketReplicationConfiguration(getBucketReplicationConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketReplicationConfiguration(getBucketReplicationConfigurationRequest);
	}

	@Test
	public void testDeleteBucketReplicationConfiguration() {
		String s = TEST_STRING;

		instance.deleteBucketReplicationConfiguration(s);

		Mockito.verify(mockAmazonS3Client, times(1)).deleteBucketReplicationConfiguration(TEST_STRING);
	}

	@Test
	public void testDeleteBucketReplicationConfiguration_1() {
		DeleteBucketReplicationConfigurationRequest deleteBucketReplicationConfigurationRequest = null;

		instance.deleteBucketReplicationConfiguration(deleteBucketReplicationConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).deleteBucketReplicationConfiguration(deleteBucketReplicationConfigurationRequest);
	}

	@Test
	public void testDoesObjectExist() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;

		instance.doesObjectExist(s, s1);

		Mockito.verify(mockAmazonS3Client, times(1)).doesObjectExist(TEST_STRING, TEST_STRING);
	}

	@Test
	public void testGetBucketAccelerateConfiguration() {
		String s = TEST_STRING;

		instance.getBucketAccelerateConfiguration(s);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketAccelerateConfiguration(TEST_STRING);
	}

	@Test
	public void testGetBucketAccelerateConfiguration_1() {
		GetBucketAccelerateConfigurationRequest getBucketAccelerateConfigurationRequest = null;

		instance.getBucketAccelerateConfiguration(getBucketAccelerateConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketAccelerateConfiguration(getBucketAccelerateConfigurationRequest);
	}

	@Test
	public void testSetBucketAccelerateConfiguration() {
		String s = TEST_STRING;
		BucketAccelerateConfiguration bucketAccelerateConfiguration = null;

		instance.setBucketAccelerateConfiguration(s, bucketAccelerateConfiguration);

		Mockito.verify(mockAmazonS3Client, times(1)).setBucketAccelerateConfiguration(TEST_STRING, bucketAccelerateConfiguration);
	}

	@Test
	public void testSetBucketAccelerateConfiguration_1() {
		SetBucketAccelerateConfigurationRequest setBucketAccelerateConfigurationRequest = null;

		instance.setBucketAccelerateConfiguration(setBucketAccelerateConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).setBucketAccelerateConfiguration(setBucketAccelerateConfigurationRequest);
	}

	@Test
	public void testDeleteBucketMetricsConfiguration() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;

		instance.deleteBucketMetricsConfiguration(s, s1);

		Mockito.verify(mockAmazonS3Client, times(1)).deleteBucketMetricsConfiguration(TEST_STRING, TEST_STRING);
	}

	@Test
	public void testDeleteBucketMetricsConfiguration_1() {
		DeleteBucketMetricsConfigurationRequest deleteBucketMetricsConfigurationRequest = new DeleteBucketMetricsConfigurationRequest();

		instance.deleteBucketMetricsConfiguration(deleteBucketMetricsConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).deleteBucketMetricsConfiguration(deleteBucketMetricsConfigurationRequest);
	}

	@Test
	public void testGetBucketMetricsConfiguration() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;

		instance.getBucketMetricsConfiguration(s, s1);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketMetricsConfiguration(TEST_STRING, TEST_STRING);
	}

	@Test
	public void testGetBucketMetricsConfiguration_1() {
		GetBucketMetricsConfigurationRequest getBucketMetricsConfigurationRequest = new GetBucketMetricsConfigurationRequest();

		instance.getBucketMetricsConfiguration(getBucketMetricsConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketMetricsConfiguration(getBucketMetricsConfigurationRequest);
	}

	@Test
	public void testSetBucketMetricsConfiguration() {
		String s = TEST_STRING;
		MetricsConfiguration metricsConfiguration = new MetricsConfiguration();

		instance.setBucketMetricsConfiguration(s, metricsConfiguration);

		Mockito.verify(mockAmazonS3Client, times(1)).setBucketMetricsConfiguration(TEST_STRING, metricsConfiguration);
	}

	@Test
	public void testSetBucketMetricsConfiguration_1() {
		SetBucketMetricsConfigurationRequest setBucketMetricsConfigurationRequest = new SetBucketMetricsConfigurationRequest();

		instance.setBucketMetricsConfiguration(setBucketMetricsConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).setBucketMetricsConfiguration(setBucketMetricsConfigurationRequest);
	}

	@Test
	public void testListBucketMetricsConfigurations() {
		ListBucketMetricsConfigurationsRequest listBucketMetricsConfigurationsRequest = new ListBucketMetricsConfigurationsRequest();

		instance.listBucketMetricsConfigurations(listBucketMetricsConfigurationsRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).listBucketMetricsConfigurations(listBucketMetricsConfigurationsRequest);
	}

	@Test
	public void testDeleteBucketAnalyticsConfiguration() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;

		instance.deleteBucketAnalyticsConfiguration(s, s1);

		Mockito.verify(mockAmazonS3Client, times(1)).deleteBucketAnalyticsConfiguration(TEST_STRING, TEST_STRING);
	}

	@Test
	public void testDeleteBucketAnalyticsConfiguration_1() {
		DeleteBucketAnalyticsConfigurationRequest deleteBucketAnalyticsConfigurationRequest = new DeleteBucketAnalyticsConfigurationRequest();

		instance.deleteBucketAnalyticsConfiguration(deleteBucketAnalyticsConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).deleteBucketAnalyticsConfiguration(deleteBucketAnalyticsConfigurationRequest);
	}

	@Test
	public void testGetBucketAnalyticsConfiguration() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;

		instance.getBucketAnalyticsConfiguration(s, s1);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketAnalyticsConfiguration(TEST_STRING, TEST_STRING);
	}

	@Test
	public void testGetBucketAnalyticsConfiguration_1() {
		GetBucketAnalyticsConfigurationRequest getBucketAnalyticsConfigurationRequest = new GetBucketAnalyticsConfigurationRequest();

		instance.getBucketAnalyticsConfiguration(getBucketAnalyticsConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketAnalyticsConfiguration(getBucketAnalyticsConfigurationRequest);
	}

	@Test
	public void testSetBucketAnalyticsConfiguration() {
		String s = TEST_STRING;
		AnalyticsConfiguration analyticsConfiguration = new AnalyticsConfiguration();

		instance.setBucketAnalyticsConfiguration(s, analyticsConfiguration);

		Mockito.verify(mockAmazonS3Client, times(1)).setBucketAnalyticsConfiguration(TEST_STRING, analyticsConfiguration);
	}

	@Test
	public void testSetBucketAnalyticsConfiguration_1() {
		SetBucketAnalyticsConfigurationRequest setBucketAnalyticsConfigurationRequest = new SetBucketAnalyticsConfigurationRequest();

		instance.setBucketAnalyticsConfiguration(setBucketAnalyticsConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).setBucketAnalyticsConfiguration(setBucketAnalyticsConfigurationRequest);
	}

	@Test
	public void testListBucketAnalyticsConfigurations() {
		ListBucketAnalyticsConfigurationsRequest listBucketAnalyticsConfigurationsRequest = new ListBucketAnalyticsConfigurationsRequest();

		instance.listBucketAnalyticsConfigurations(listBucketAnalyticsConfigurationsRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).listBucketAnalyticsConfigurations(listBucketAnalyticsConfigurationsRequest);
	}

	@Test
	public void testDeleteBucketInventoryConfiguration() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;

		instance.deleteBucketInventoryConfiguration(s, s1);

		Mockito.verify(mockAmazonS3Client, times(1)).deleteBucketInventoryConfiguration(TEST_STRING, TEST_STRING);
	}

	@Test
	public void testDeleteBucketInventoryConfiguration_1() {
		DeleteBucketInventoryConfigurationRequest deleteBucketInventoryConfigurationRequest = new DeleteBucketInventoryConfigurationRequest();

		instance.deleteBucketInventoryConfiguration(deleteBucketInventoryConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).deleteBucketInventoryConfiguration(deleteBucketInventoryConfigurationRequest);
	}

	@Test
	public void testGetBucketInventoryConfiguration() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;

		instance.getBucketInventoryConfiguration(s, s1);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketInventoryConfiguration(TEST_STRING, TEST_STRING);
	}

	@Test
	public void testGetBucketInventoryConfiguration_1() {
		GetBucketInventoryConfigurationRequest getBucketInventoryConfigurationRequest = new GetBucketInventoryConfigurationRequest();

		instance.getBucketInventoryConfiguration(getBucketInventoryConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketInventoryConfiguration(getBucketInventoryConfigurationRequest);
	}

	@Test
	public void testSetBucketInventoryConfiguration() {
		String s = TEST_STRING;
		InventoryConfiguration inventoryConfiguration = new InventoryConfiguration();

		instance.setBucketInventoryConfiguration(s, inventoryConfiguration);

		Mockito.verify(mockAmazonS3Client, times(1)).setBucketInventoryConfiguration(TEST_STRING, inventoryConfiguration);
	}

	@Test
	public void testSetBucketInventoryConfiguration_1() {
		SetBucketInventoryConfigurationRequest setBucketInventoryConfigurationRequest = new SetBucketInventoryConfigurationRequest();

		instance.setBucketInventoryConfiguration(setBucketInventoryConfigurationRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).setBucketInventoryConfiguration(setBucketInventoryConfigurationRequest);
	}

	@Test
	public void testListBucketInventoryConfigurations() {
		ListBucketInventoryConfigurationsRequest listBucketInventoryConfigurationsRequest = new ListBucketInventoryConfigurationsRequest();

		instance.listBucketInventoryConfigurations(listBucketInventoryConfigurationsRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).listBucketInventoryConfigurations(listBucketInventoryConfigurationsRequest);
	}

	@Test
	public void testDeleteBucketEncryption() {
		String s = TEST_STRING;

		instance.deleteBucketEncryption(s);

		Mockito.verify(mockAmazonS3Client, times(1)).deleteBucketEncryption(TEST_STRING);
	}

	@Test
	public void testDeleteBucketEncryption_1() {
		DeleteBucketEncryptionRequest deleteBucketEncryptionRequest = new DeleteBucketEncryptionRequest();

		instance.deleteBucketEncryption(deleteBucketEncryptionRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).deleteBucketEncryption(deleteBucketEncryptionRequest);
	}

	@Test
	public void testGetBucketEncryption() {
		String s = TEST_STRING;

		instance.getBucketEncryption(s);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketEncryption(TEST_STRING);
	}

	@Test
	public void testGetBucketEncryption_1() {
		GetBucketEncryptionRequest getBucketEncryptionRequest = new GetBucketEncryptionRequest();

		instance.getBucketEncryption(getBucketEncryptionRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).getBucketEncryption(getBucketEncryptionRequest);
	}

	@Test
	public void testSetBucketEncryption() {
		SetBucketEncryptionRequest setBucketEncryptionRequest = new SetBucketEncryptionRequest();

		instance.setBucketEncryption(setBucketEncryptionRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).setBucketEncryption(setBucketEncryptionRequest);
	}

	@Test
	public void testSelectObjectContent() {
		SelectObjectContentRequest selectObjectContentRequest = new SelectObjectContentRequest();

		instance.selectObjectContent(selectObjectContentRequest);

		Mockito.verify(mockAmazonS3Client, times(1)).selectObjectContent(selectObjectContentRequest);
	}

	@Test
	public void testSetBucketOwnershipControls(){
		SetBucketOwnershipControlsRequest setBucketOwnershipControlsRequest = new SetBucketOwnershipControlsRequest();
		instance.setBucketOwnershipControls(setBucketOwnershipControlsRequest);
		Mockito.verify(mockAmazonS3Client, times(1)).setBucketOwnershipControls(setBucketOwnershipControlsRequest);
	}

	@Test
	public void testSetBucketOwnershipControls_1(){
		String s = TEST_STRING;
		OwnershipControls ownershipControls = new OwnershipControls();
		instance.setBucketOwnershipControls(s, ownershipControls);
		Mockito.verify(mockAmazonS3Client, times(1)).setBucketOwnershipControls(s, ownershipControls);
	}

	@Test
	public void getBucketOwnershipControls(){
		GetBucketOwnershipControlsRequest getBucketOwnershipControlsRequest = new GetBucketOwnershipControlsRequest();
		instance.getBucketOwnershipControls(getBucketOwnershipControlsRequest);
		Mockito.verify(mockAmazonS3Client, times(1)).getBucketOwnershipControls(getBucketOwnershipControlsRequest);
	}

	@Test
	public void testDeleteBucketOwnershipControls(){
		DeleteBucketOwnershipControlsRequest deleteBucketOwnershipControls = new DeleteBucketOwnershipControlsRequest();
		instance.deleteBucketOwnershipControls(deleteBucketOwnershipControls);
		Mockito.verify(mockAmazonS3Client, times(1)).deleteBucketOwnershipControls(deleteBucketOwnershipControls);
	}


	@Test
	public void getWriteGetObjectResponse(){
		WriteGetObjectResponseRequest writeGetObjectResponse = new WriteGetObjectResponseRequest();
		instance.writeGetObjectResponse(writeGetObjectResponse);
		Mockito.verify(mockAmazonS3Client, times(1)).writeGetObjectResponse(writeGetObjectResponse);
	}

	@Test
	public void testListBucketIntelligentTieringConfigurations(){
		ListBucketIntelligentTieringConfigurationsRequest listBucketIntelligentTieringConfigurationsRequest = new ListBucketIntelligentTieringConfigurationsRequest();
		instance.listBucketIntelligentTieringConfigurations(listBucketIntelligentTieringConfigurationsRequest);
		Mockito.verify(mockAmazonS3Client, times(1)).listBucketIntelligentTieringConfigurations(listBucketIntelligentTieringConfigurationsRequest);
	}

	@Test
	public void testGetBucketIntelligentTieringConfiguration(){
		String s = TEST_STRING;
		String s1 = TEST_STRING;
		instance.getBucketIntelligentTieringConfiguration(s,s1);
		Mockito.verify(mockAmazonS3Client, times(1)).getBucketIntelligentTieringConfiguration(s, s1);
	}

	@Test
	public void testGetBucketIntelligentTieringConfiguration_1(){
		GetBucketIntelligentTieringConfigurationRequest getBucketIntelligentTieringConfigurationRequest = new GetBucketIntelligentTieringConfigurationRequest();
		instance.getBucketIntelligentTieringConfiguration(getBucketIntelligentTieringConfigurationRequest);
		Mockito.verify(mockAmazonS3Client, times(1)).getBucketIntelligentTieringConfiguration(getBucketIntelligentTieringConfigurationRequest);
	}

	@Test
	public void testSetBucketIntelligentTieringConfiguration(){
		String s = TEST_STRING;
		IntelligentTieringConfiguration intelligentTieringConfiguration = new IntelligentTieringConfiguration();
		instance.setBucketIntelligentTieringConfiguration(s,intelligentTieringConfiguration);
		Mockito.verify(mockAmazonS3Client, times(1)).setBucketIntelligentTieringConfiguration(s, intelligentTieringConfiguration);
	}

	@Test
	public void testSetBucketIntelligentTieringConfiguration_1(){
		SetBucketIntelligentTieringConfigurationRequest setBucketIntelligentTieringConfigurationRequest = new SetBucketIntelligentTieringConfigurationRequest();
		instance.setBucketIntelligentTieringConfiguration(setBucketIntelligentTieringConfigurationRequest);
		Mockito.verify(mockAmazonS3Client, times(1)).setBucketIntelligentTieringConfiguration(setBucketIntelligentTieringConfigurationRequest);
	}

	@Test
	public void testDeleteBucketIntelligentTieringConfiguration(){
		String s = TEST_STRING;
		String s1 = TEST_STRING;
		instance.deleteBucketIntelligentTieringConfiguration(s,s1);
		Mockito.verify(mockAmazonS3Client, times(1)).deleteBucketIntelligentTieringConfiguration(s, s1);
	}

	@Test
	public void testDeleteBucketIntelligentTieringConfiguration_1(){
		DeleteBucketIntelligentTieringConfigurationRequest deleteBucketIntelligentTieringConfigurationRequest = new DeleteBucketIntelligentTieringConfigurationRequest();

		instance.deleteBucketIntelligentTieringConfiguration(deleteBucketIntelligentTieringConfigurationRequest);
		Mockito.verify(mockAmazonS3Client, times(1)).deleteBucketIntelligentTieringConfiguration(deleteBucketIntelligentTieringConfigurationRequest);
	}

	@Test
	public void testSetRequestPaymentConfigurationForRequester(){
		String s = TEST_STRING;
		RequestPaymentConfiguration requestPaymentConfiguration = new RequestPaymentConfiguration(RequestPaymentConfiguration.Payer.Requester);
		SetRequestPaymentConfigurationRequest setRequestPaymentConfigurationRequest = new SetRequestPaymentConfigurationRequest(s, requestPaymentConfiguration);

		instance.setRequestPaymentConfiguration(setRequestPaymentConfigurationRequest);
		Mockito.verify(mockAmazonS3Client, times(1)).setRequestPaymentConfiguration(setRequestPaymentConfigurationRequest);
	}

	@Test
	public void testSetRequestPaymentConfigurationForBucketOwner(){
		String s = TEST_STRING;
		RequestPaymentConfiguration requestPaymentConfiguration = new RequestPaymentConfiguration(RequestPaymentConfiguration.Payer.BucketOwner);

		SetRequestPaymentConfigurationRequest setRequestPaymentConfigurationRequest = new SetRequestPaymentConfigurationRequest(s, requestPaymentConfiguration);

		instance.setRequestPaymentConfiguration(setRequestPaymentConfigurationRequest);
		Mockito.verify(mockAmazonS3Client, times(1)).setRequestPaymentConfiguration(setRequestPaymentConfigurationRequest);
	}

	@Test
	public void testShutdown() {
		instance.shutdown();

		Mockito.verify(mockAmazonS3Client, times(1)).shutdown();
	}

	@Test
	public void testGetRegion() {
		instance.getRegion();

		Mockito.verify(mockAmazonS3Client, times(1)).getRegion();
	}

	@Test
	public void testGetRegionName() {
		instance.getRegionName();

		Mockito.verify(mockAmazonS3Client, times(1)).getRegionName();
	}

	@Test
	public void testGetUrl() {
		String s = TEST_STRING;
		String s1 = TEST_STRING;

		instance.getUrl(s, s1);

		Mockito.verify(mockAmazonS3Client, times(1)).getUrl(TEST_STRING, TEST_STRING);
	}

	@Test
	public void testWaiters() {
		instance.waiters();

		Mockito.verify(mockAmazonS3Client, times(1)).waiters();
	}


	@Test
	public void testGetWrappedClient() {
		AmazonS3 result = instance.getWrappedClient();

		assertEquals(mockAmazonS3Client, result);
	}
}
