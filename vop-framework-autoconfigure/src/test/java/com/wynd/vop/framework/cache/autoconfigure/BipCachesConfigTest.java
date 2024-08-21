package com.wynd.vop.framework.cache.autoconfigure;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.junit.Assert.assertTrue;

public class BipCachesConfigTest {

	@Test
	public void getRedisCacheConfigsWithNullExpiresTest() {
		BipCachesConfig config = new BipCachesConfig();
		BipRedisCacheProperties vopRedisCacheProperties = new BipRedisCacheProperties();
		vopRedisCacheProperties.setExpires(null);
		ReflectionTestUtils.setField(config, "vopRedisCacheProperties", vopRedisCacheProperties);
		Map<String, org.springframework.data.redis.cache.RedisCacheConfiguration> map =
				ReflectionTestUtils.invokeMethod(config, "getRedisCacheConfigs", (Object[]) null);
		assertTrue(map.isEmpty());
	}
}
