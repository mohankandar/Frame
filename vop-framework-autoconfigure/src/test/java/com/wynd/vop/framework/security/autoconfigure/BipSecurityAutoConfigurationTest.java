package com.wynd.vop.framework.security.autoconfigure;

import com.wynd.vop.framework.kong.KongProperties;
import com.wynd.vop.framework.security.jwt.JwtAuthenticationProperties;
import com.wynd.vop.framework.security.opa.BipOpaProperties;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import static org.junit.Assert.*;

/**
 * Created by vgadda on 7/31/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@ContextConfiguration(classes = { JwtAuthenticationProperties.class, KongProperties.class, BipOpaProperties.class })
public class BipSecurityAutoConfigurationTest {

	private AnnotationConfigWebApplicationContext context;

	private static final String VOP_FRAMEWORK_JWT_DISABLED = "vop.framework.security.jwt.enabled=false";
	private static final String VOP_FRAMEWORK_OPA_ENABLED = "vop.framework.security.opa.enabled=true";
	private static final String VOP_FRAMEWORK_OPA_ALLVOTERS_ABSTAIN_GRANT_AACCESS = "vop.framework.security.opa.allVotersAbstainGrantAccess=true";
	private static final String VOP_FRAMEWORK_OPA_URLS = "vop.framework.security.opa.urls[0]=http://localhost:8080/api/v1/mytest/pid";
	private static final String VOP_FRAMEWORK_OPA_URLS_INVALID = "vop.framework.security.opa.urls[0]=";
	private static final String VOP_FRAMEWORK_ACTUATOR_SECURITY_DISABLED = "vop.framework.security.actuator.enabled=false";
	private static final String VOP_FRAMEWORK_KONG_ENABLED = "vop.framework.kong.enabled=true";
	private static final String VOP_FRAMEWORK_KONG_URL = "vop.framework.kong.url=http://dev8-kong-proxy.api-gateway";
	private static final String VOP_FRAMEWORK_KONG_JWT_RESOURCE_PATH = "vop.framework.kong.consumerJwtResourcePath=admin/consumers/{0}/jwt?apikey={1}";

	@After
	public void close() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void testWebConfiguration() throws Exception {
		context = new AnnotationConfigWebApplicationContext();
		context.register(SecurityAutoConfiguration.class, EmbeddedWebServerFactoryCustomizerAutoConfiguration.class,
				BipSecurityAutoConfiguration.class);
		context.refresh();
		assertNotNull(context);
		assertTrue(this.context.getBean(FilterChainProxy.class).getFilterChains().size() > 2);
	}

	@Test
	public void testWebConfigurationJwtDisabled() throws Exception {
		context = new AnnotationConfigWebApplicationContext();
		TestPropertyValues.of(VOP_FRAMEWORK_JWT_DISABLED).applyTo(context);
		context.register(SecurityAutoConfiguration.class, EmbeddedWebServerFactoryCustomizerAutoConfiguration.class,
				BipSecurityAutoConfiguration.class);
		context.refresh();
		assertNotNull(context);
		assertTrue(this.context.getBean(FilterChainProxy.class).getFilterChains().size() > 2);
	}

	@Test
	public void testWebConfigurationActuatorSecurityDisabled() throws Exception {
		context = new AnnotationConfigWebApplicationContext();
		TestPropertyValues.of(VOP_FRAMEWORK_ACTUATOR_SECURITY_DISABLED).applyTo(context);
		context.register(SecurityAutoConfiguration.class, EmbeddedWebServerFactoryCustomizerAutoConfiguration.class,
				BipSecurityAutoConfiguration.class);
		context.refresh();
		assertNotNull(context);
		assertTrue(this.context.getBean(FilterChainProxy.class).getFilterChains().size() > 2);

	}

	@Test
	public void testWebConfigurationActuatorSecurityEnabled() throws Exception {
		context = new AnnotationConfigWebApplicationContext();
		context.register(SecurityAutoConfiguration.class, EmbeddedWebServerFactoryCustomizerAutoConfiguration.class,
				BipSecurityAutoConfiguration.class);
		context.refresh();
		assertNotNull(context);
		assertTrue(this.context.getBean(FilterChainProxy.class).getFilterChains().size() > 2);

	}

	@Test
	public void testWebConfigurationOpaEnabled() throws Exception {
		context = new AnnotationConfigWebApplicationContext();
		TestPropertyValues.of(VOP_FRAMEWORK_OPA_ENABLED).applyTo(context);
		context.register(SecurityAutoConfiguration.class, EmbeddedWebServerFactoryCustomizerAutoConfiguration.class,
				BipSecurityAutoConfiguration.class);
		context.refresh();
		assertNotNull(context);
		assertTrue(this.context.getBean(FilterChainProxy.class).getFilterChains().size() > 2);

	}

	@Test
	public void testWebConfigurationOpaEnabledWithUrls() throws Exception {
		context = new AnnotationConfigWebApplicationContext();
		TestPropertyValues.of(VOP_FRAMEWORK_OPA_ENABLED).applyTo(context);
		TestPropertyValues.of(VOP_FRAMEWORK_OPA_URLS).applyTo(context);
		context.register(SecurityAutoConfiguration.class, EmbeddedWebServerFactoryCustomizerAutoConfiguration.class,
				BipSecurityAutoConfiguration.class);
		context.refresh();
		assertNotNull(context);
		assertTrue(this.context.getBean(FilterChainProxy.class).getFilterChains().size() > 2);
	}

	@Test
	public void testWebConfigurationOpaEnabledWithUrlsBoolean() throws Exception {
		context = new AnnotationConfigWebApplicationContext();
		TestPropertyValues.of(VOP_FRAMEWORK_OPA_ENABLED).applyTo(context);
		TestPropertyValues.of(VOP_FRAMEWORK_OPA_URLS).applyTo(context);
		TestPropertyValues.of(VOP_FRAMEWORK_OPA_ALLVOTERS_ABSTAIN_GRANT_AACCESS).applyTo(context);
		context.register(SecurityAutoConfiguration.class, EmbeddedWebServerFactoryCustomizerAutoConfiguration.class,
				BipSecurityAutoConfiguration.class);
		context.refresh();
		assertNotNull(context);
		assertTrue(this.context.getBean(FilterChainProxy.class).getFilterChains().size() > 2);
	}

	@Test
	public void testWebConfigurationOpaEnabledWithInvalidUrls() throws Exception {
		context = new AnnotationConfigWebApplicationContext();
		TestPropertyValues.of(VOP_FRAMEWORK_OPA_ENABLED).applyTo(context);
		TestPropertyValues.of(VOP_FRAMEWORK_OPA_URLS_INVALID).applyTo(context);
		context.register(SecurityAutoConfiguration.class, EmbeddedWebServerFactoryCustomizerAutoConfiguration.class,
				BipSecurityAutoConfiguration.class);
		context.refresh();
		assertNotNull(context);
		assertTrue(this.context.getBean(FilterChainProxy.class).getFilterChains().size() > 2);
	}

	@Test
	public void testWebConfigurationKongEnabled() throws Exception {
		context = new AnnotationConfigWebApplicationContext();
		TestPropertyValues.of(VOP_FRAMEWORK_KONG_ENABLED).applyTo(context);
		TestPropertyValues.of(VOP_FRAMEWORK_KONG_URL).applyTo(context);
		TestPropertyValues.of(VOP_FRAMEWORK_KONG_JWT_RESOURCE_PATH).applyTo(context);
		context.register(SecurityAutoConfiguration.class, EmbeddedWebServerFactoryCustomizerAutoConfiguration.class,
				BipSecurityAutoConfiguration.class);
		context.refresh();
		assertNotNull(context);
		assertTrue(this.context.getBean(FilterChainProxy.class).getFilterChains().size() > 2);
	}

	@Test
	public void testWebConfigurationKongEnabledJwtDisabled() throws Exception {
		context = new AnnotationConfigWebApplicationContext();
		TestPropertyValues.of(VOP_FRAMEWORK_JWT_DISABLED).applyTo(context);
		TestPropertyValues.of(VOP_FRAMEWORK_KONG_ENABLED).applyTo(context);
		TestPropertyValues.of(VOP_FRAMEWORK_KONG_URL).applyTo(context);
		TestPropertyValues.of(VOP_FRAMEWORK_KONG_JWT_RESOURCE_PATH).applyTo(context);
		context.register(SecurityAutoConfiguration.class, EmbeddedWebServerFactoryCustomizerAutoConfiguration.class,
				BipSecurityAutoConfiguration.class);
		context.refresh();
		assertNotNull(context);
		assertTrue(this.context.getBean(FilterChainProxy.class).getFilterChains().size() > 2);
	}

}
