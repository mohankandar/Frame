package com.wynd.vop.framework.cache.autoconfigure;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BipRedisCachePropertiesTest {

	@Test
	public void testGetters() {
		BipRedisCacheProperties vopRedisCacheProperties = new BipRedisCacheProperties();
		assertNull(vopRedisCacheProperties.getExpires());
		assertEquals(new Long(86400L), vopRedisCacheProperties.getDefaultExpires());
		assertTrue(vopRedisCacheProperties.isAllowNullReturn());
	}

	@Test
	public void testSetters() {
		BipRedisCacheProperties vopRedisCacheProperties = new BipRedisCacheProperties();
		List<BipRedisCacheProperties.RedisExpires> listRedisExpires = new ArrayList<>();
		BipRedisCacheProperties.RedisExpires redisExpires = new BipRedisCacheProperties.RedisExpires();
		redisExpires.setName("methodcachename_projectname_projectversion");
		redisExpires.setTtl(86400L);
		listRedisExpires.add(0, redisExpires);
		vopRedisCacheProperties.setExpires(listRedisExpires);
		vopRedisCacheProperties.setDefaultExpires(500L);
		vopRedisCacheProperties.setAllowNullReturn(false);
		assertTrue(!vopRedisCacheProperties.getExpires().isEmpty());
		assertTrue(Long.valueOf(86400L).equals(vopRedisCacheProperties.getExpires().get(0).getTtl()));
		assertEquals(new Long(500L), vopRedisCacheProperties.getDefaultExpires());
		assertFalse(vopRedisCacheProperties.isAllowNullReturn());
	}
}