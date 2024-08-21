package com.wynd.vop.framework.config;

import org.springframework.beans.factory.annotation.Value;

public class AwsProperties {

	@Value("${vop.framework.aws.accessKey:test-key}")
	private String accessKey;

	@Value("${vop.framework.aws.secretKey:test-secret}")
	private String secretKey;

	@Value("${vop.framework.aws.enabled:false}")
	private Boolean enabled;

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public Boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
}
