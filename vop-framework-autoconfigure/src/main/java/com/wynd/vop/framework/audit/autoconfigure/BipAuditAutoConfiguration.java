package com.wynd.vop.framework.audit.autoconfigure;

import com.wynd.vop.framework.audit.AuditLogSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Created by rthota on 8/24/17.
 */

@Configuration
@EnableAsync
public class BipAuditAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public AuditLogSerializer auditLogSerializer() {
		return new AuditLogSerializer();
	}
}
