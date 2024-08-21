package com.wynd.vop.framework.s3.config;

import com.wynd.vop.framework.log.BipLogger;
import com.wynd.vop.framework.log.BipLoggerFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class S3PropertiesTest {

    //Test S3 Logger
    @Test
    public void testLogger() {
        BipLogger logger = BipLoggerFactory.getLogger(S3Properties.class);
        S3Properties instance = new S3Properties();
        instance.setLogger(logger);

        assertEquals(Optional.of(instance.getLogger()), Optional.ofNullable(logger));
    }

    //Test S3 Property enabled
    @Test
    public void testBoolean() {
        boolean enabled = true;
        S3Properties instance = new S3Properties();
        instance.setEnabled(enabled);

        assertEquals(Optional.of(instance.isEnabled()), Optional.ofNullable(enabled));
    }

    //Test S3 Property name
    @Test
    public void testBuckets() {
        List<S3Properties.S3Bucket> s3Buckets = new ArrayList<>();
        S3Properties instance = new S3Properties();
        instance.setBuckets(s3Buckets);

        assertEquals(Optional.of(instance.getBuckets()), Optional.ofNullable(s3Buckets));
    }

    //Test S3 Property type
    @Test
    public void testBucketName() {
        String name = "String";
        S3Properties.S3Bucket instance = new S3Properties.S3Bucket();
        instance.setName(name);

        assertEquals(Optional.of(instance.getName()), Optional.ofNullable(name));
    }

    @Test
    public void testGetBucketByName() {
        String name = "String";

        List<S3Properties.S3Bucket> bucketList = new ArrayList<>();
        S3Properties.S3Bucket testBucket = new S3Properties.S3Bucket();
        testBucket.setName(name);
        bucketList.add(testBucket);

        S3Properties instance = new S3Properties();
        instance.setBuckets(bucketList);

        S3Properties.S3Bucket result = instance.getBucketByName(name);

        assertNotNull(result);
        assertEquals(testBucket, result);
    }

    @Test
    public void testGetBucketByName_null() {
        String name = "String";

        List<S3Properties.S3Bucket> bucketList = new ArrayList<>();
        S3Properties.S3Bucket testBucket = new S3Properties.S3Bucket();
        testBucket.setName(name);
        bucketList.add(testBucket);

        S3Properties instance = new S3Properties();
        instance.setBuckets(bucketList);

        S3Properties.S3Bucket result = instance.getBucketByName(null);

        assertNull(result);
    }

    @Test
    public void testGetBucketById() {
        String id = "String";

        List<S3Properties.S3Bucket> bucketList = new ArrayList<>();
        S3Properties.S3Bucket testBucket = new S3Properties.S3Bucket();
        testBucket.setId(id);
        bucketList.add(testBucket);

        S3Properties instance = new S3Properties();
        instance.setBuckets(bucketList);

        S3Properties.S3Bucket result = instance.getBucketById(id);

        assertNotNull(result);
        assertEquals(testBucket, result);
    }

    @Test
    public void testGetBucketById_null() {
        String id = "String";

        List<S3Properties.S3Bucket> bucketList = new ArrayList<>();
        S3Properties.S3Bucket testBucket = new S3Properties.S3Bucket();
        testBucket.setId(id);
        bucketList.add(testBucket);

        S3Properties instance = new S3Properties();
        instance.setBuckets(bucketList);

        S3Properties.S3Bucket result = instance.getBucketById(null);

        assertNull(result);
    }
}