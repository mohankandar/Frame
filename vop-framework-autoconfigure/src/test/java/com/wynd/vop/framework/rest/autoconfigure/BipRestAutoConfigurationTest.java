package com.wynd.vop.framework.rest.autoconfigure;

import com.wynd.vop.framework.cache.autoconfigure.TestConfigurationForAuditBeans;
import com.wynd.vop.framework.audit.autoconfigure.BipAuditAutoConfiguration;
import com.wynd.vop.framework.rest.provider.aspect.ProviderHttpAspect;
import com.wynd.vop.framework.security.autoconfigure.BipSecurityAutoConfiguration;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.wynd.vop.framework.util.HttpClientUtils;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * Created by rthota on 8/24/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class BipRestAutoConfigurationTest {

	private static final String CONNECTION_TIMEOUT = "20000";

	private BipRestAutoConfiguration vopRestAutoConfiguration;

	private AnnotationConfigWebApplicationContext context;

	@Before
	public void setup() {
		context = new AnnotationConfigWebApplicationContext();
		TestPropertyValues.of("feign.hystrix.enabled=true").applyTo(context);
		TestPropertyValues.of("vop.framework.client.rest.connectionTimeout=" + CONNECTION_TIMEOUT).applyTo(context);
		context.register(JacksonAutoConfiguration.class, SecurityAutoConfiguration.class,
				EmbeddedWebServerFactoryCustomizerAutoConfiguration.class,
				BipSecurityAutoConfiguration.class,
				BipAuditAutoConfiguration.class, BipRestAutoConfiguration.class,
				ProviderHttpAspect.class, TestConfigurationForAuditBeans.class);

		context.refresh();
		assertNotNull(context);

		// test configuration and give vopRestAutoConfiguration a value for other tests
		vopRestAutoConfiguration = context.getBean(BipRestAutoConfiguration.class);
		assertNotNull(vopRestAutoConfiguration);
	}

	@After
	public void close() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void testConfiguration_Broken() {
		TestPropertyValues.of("vop.framework.client.rest.connectionTimeout=BLAHBLAH").applyTo(context);

		try {
			context.refresh();
			vopRestAutoConfiguration.restClientTemplate();
			fail("BipRestAutoConfiguration should have thrown IllegalStateException or BeansException");
		} catch (Exception e) {
			assertTrue(BeansException.class.isAssignableFrom(e.getClass()));
		} finally {
			TestPropertyValues.of("vop.framework.client.rest.connectionTimeout=" + CONNECTION_TIMEOUT).applyTo(context);
			context.refresh();
			vopRestAutoConfiguration = context.getBean(BipRestAutoConfiguration.class);
			assertNotNull(vopRestAutoConfiguration);
		}
	}

	@Test
	public void testWebConfiguration() throws Exception {
		assertNotNull(vopRestAutoConfiguration.providerHttpAspect());
		assertNotNull(vopRestAutoConfiguration.restProviderTimerAspect());
		assertNotNull(vopRestAutoConfiguration.restClientTemplate());
		assertNotNull(vopRestAutoConfiguration.tokenClientHttpRequestInterceptor());
	}

	@Test
	public void testSetRetryHandlerToClientBuilder() throws Exception {
		BipRestAutoConfiguration config = new BipRestAutoConfiguration();
		HttpClientBuilder clientBuilder = HttpClients.custom();
        HttpClientUtils.setRetryHandlerToClientBuilder(clientBuilder, 3);
		assertTrue(((HttpRequestRetryHandler) ReflectionTestUtils.getField(clientBuilder, "retryHandler"))
				.retryRequest(new NoHttpResponseException(""), 0, new HttpClientContext()));
	}

}
