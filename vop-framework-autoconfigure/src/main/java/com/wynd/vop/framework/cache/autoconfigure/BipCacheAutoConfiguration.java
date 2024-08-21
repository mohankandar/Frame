package com.wynd.vop.framework.cache.autoconfigure;

import com.wynd.vop.framework.audit.AuditLogSerializer;
import com.wynd.vop.framework.audit.BaseAsyncAudit;
import com.wynd.vop.framework.cache.autoconfigure.jmx.BipCacheOpsImpl;
import com.wynd.vop.framework.cache.autoconfigure.jmx.BipCacheOpsMBean;
import com.wynd.vop.framework.cache.autoconfigure.server.BipEmbeddedRedisServer;
import com.wynd.vop.framework.log.BipLogger;
import com.wynd.vop.framework.log.BipLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * This configuration runs only when the application property
 * {@code spring.cache.type} is set to the value {@code redis}.
 * <p>
 * Cache auto configuration:
 * <ul>
 * <li> Configures and starts {@link BipEmbeddedRedisServer} if necessary (as declared under spring profiles in application yaml).
 * <li> In {@link BipJedisConnectionConfig}, configures the JedisConnectionFactory, consisting of:
 * <ul>
 * <li> RedisStandaloneConfiguration - the redis "Standalone" module (host, port, db index, password)
 * <li> JedisClientConfiguration (timeouts, connection pool, SSL)
 * </ul>
 * <li> Configure {@link BipCachesConfig} (CacheManager, and individual cache TTLs and expirations, and cache GET audits).
 * <li> Configure a JMX MBean, accessible under {@code com.wynd.vop.cache}
 * </ul>
 */
@Configuration
@AutoConfigureAfter(CacheAutoConfiguration.class)
@EnableCaching
/* @Import to participate in the auto configure bootstrap process */
@Import({ BipCachesConfig.class, BipJedisConnectionConfig.class })
@ConditionalOnProperty(name = BipCacheAutoConfiguration.CONDITIONAL_SPRING_REDIS,
		havingValue = BipCacheAutoConfiguration.CACHE_SERVER_TYPE)
@EnableMBeanExport(defaultDomain = BipCacheAutoConfiguration.JMX_DOMAIN, registration = RegistrationPolicy.FAIL_ON_EXISTING)
public class BipCacheAutoConfiguration {
	/** Class logger */
	static final BipLogger LOGGER = BipLoggerFactory.getLogger(BipCacheAutoConfiguration.class);

	/** Domain under which JMX beans are exposed */
	public static final String JMX_DOMAIN = "com.wynd.vop";
	/** ConditionalOnProperty property name */
	public static final String CONDITIONAL_SPRING_REDIS = "spring.cache.type";
	/** The cache server type */
	public static final String CACHE_SERVER_TYPE = "redis";

	/** Refresh order for JedisConnectionFactory must be lower than for CacheManager */
	static final int REFRESH_ORDER_CONNECTION_FACTORY = 1;
	/** Refresh order for CacheManager must be higher than for JedisConnectionFactory */
	static final int REFRESH_ORDER_CACHES = 10;

	/** Embedded Redis bean to make sure embedded redis is started before redis cache is created. */
	@SuppressWarnings("unused")
	@Autowired(required = false)
	private BipEmbeddedRedisServer referenceServerRedisEmbedded;
	
	/**
	 * VOP redis cache properties.
	 *
	 * @return the VOP redis cache properties
	 */
	@Bean
	@ConditionalOnMissingBean
	@RefreshScope
	@ConfigurationProperties(prefix = "vop.framework.cache")
	public BipRedisCacheProperties  vopRedisCacheProperties() {
	    return new BipRedisCacheProperties();
	}

	/**
	 * JMX MBean that exposes cache management operations.
	 *
	 * @return BipCacheOpsMBean - the management bean
	 */
	@Bean
	public BipCacheOpsMBean vopCacheOpsMBean() {
		return new BipCacheOpsImpl();
	}

	/**
	 * Audit log serializer.
	 *
	 * @return the audit log serializer
	 */
	@Bean
	@ConditionalOnMissingBean
	public AuditLogSerializer auditLogSerializer() {
		return new AuditLogSerializer();
	}

	/**
	 * Base async audit.
	 *
	 * @return the base async audit
	 */
	@Bean
	@ConditionalOnMissingBean
	public BaseAsyncAudit baseAsyncAudit() {
		return new BaseAsyncAudit();
	}
}
