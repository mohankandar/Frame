package com.wynd.vop.framework.cache.server;

import com.wynd.vop.framework.cache.autoconfigure.BipRedisCacheProperties;
import com.wynd.vop.framework.cache.autoconfigure.server.BipEmbeddedRedisServer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author rthota
 *
 */
@Configuration
public class TestEmbeddedRedisServerAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public BipEmbeddedRedisServer vopEmbeddedRedisServer() {
		return new BipEmbeddedRedisServer();
	}

	@Bean
	@ConditionalOnMissingBean
	public RedisProperties redisProperties() {
		RedisProperties redisProperties = new RedisProperties();
		redisProperties.setPort(0);
		return redisProperties;
	}

	@Bean
	@ConditionalOnMissingBean
	public BipRedisCacheProperties vopRedisCacheProperties() {
		return new BipRedisCacheProperties();
	}
}