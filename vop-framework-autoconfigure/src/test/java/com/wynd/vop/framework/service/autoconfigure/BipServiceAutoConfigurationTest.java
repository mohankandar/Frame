package com.wynd.vop.framework.service.autoconfigure;

import com.wynd.vop.framework.audit.autoconfigure.BipAuditAutoConfiguration;
import com.wynd.vop.framework.cache.autoconfigure.TestConfigurationForAuditBeans;
import org.junit.After;
import org.junit.Test;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import static org.junit.Assert.assertNotNull;

/**
 * Created by rthota on 8/24/17.
 */
public class BipServiceAutoConfigurationTest {

	private AnnotationConfigWebApplicationContext context;

	@After
	public void close() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void testWebConfiguration() throws Exception {
		context = new AnnotationConfigWebApplicationContext();
		context.register(BipAuditAutoConfiguration.class, BipServiceAutoConfiguration.class, TestConfigurationForAuditBeans.class);
		context.refresh();
		assertNotNull(context);
		assertNotNull(this.context.getBean(BipAuditAutoConfiguration.class));
		assertNotNull(this.context.getBean(BipServiceAutoConfiguration.class));

	}
}
