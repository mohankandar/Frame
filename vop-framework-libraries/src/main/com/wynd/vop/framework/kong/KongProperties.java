package com.wynd.vop.framework.kong;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

/**
 * Class used as kong properties in projects. The values assigned to members in
 * this class are defaults, and are typically overridden in yml and spring
 * configuration.
 */
@ConfigurationProperties(prefix = "vop.framework.kong")
public class KongProperties {

	/** Kong Integration Enablement */
	private boolean enabled;
	/** The Kong environment url */
	private String url;
	/** The Kong Consumer JWT resource path */
	private String consumerJwtResourcePath;
	/** The Admin Consumer Key (ID) tagged on Kong Consumer for security */
	private String adminConsumerKey;
	/** The List of Kong consumers for JWT Keypairs */
	private List<String> consumers = Collections.emptyList();

	/**
	 * Gets Kong integration feature flag
	 * 
	 * @return enabled
	 */
	public boolean getEnabled() {
		return enabled;
	}

	/**
	 * Sets Kong integration feature flag
	 * 
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Gets Kong environment url
	 * 
	 * @return kongUrl
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets Kong environment url
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Gets The Kong Consumer JWT resource path
	 * 
	 * @return kongConsumerJwtResourcePath
	 */
	public String getConsumerJwtResourcePath() {
		return consumerJwtResourcePath;
	}

	/**
	 * Sets The Kong Consumer JWT resource path
	 * 
	 * @param consumerJwtResourcePath
	 */
	public void setConsumerJwtResourcePath(String consumerJwtResourcePath) {
		this.consumerJwtResourcePath = consumerJwtResourcePath;
	}

	/**
	 * Gets Kong Consumer Admin Key ID
	 * 
	 * @return adminConsumerKey
	 */
	public String getAdminConsumerKey() {
		return adminConsumerKey;
	}

	/**
	 * Sets Kong Consumer Admin Key ID
	 * 
	 * @param adminConsumerKey
	 */
	public void setAdminConsumerKey(String adminConsumerKey) {
		this.adminConsumerKey = adminConsumerKey;
	}

	/**
	 * Gets List of Kong Consumers
	 * 
	 * @return consumers
	 */
	public List<String> getConsumers() {
		return consumers;
	}

	/**
	 * Sets Lists of Kong Consumers
	 * 
	 * @param consumers
	 */
	public void setConsumers(List<String> consumers) {
		this.consumers = consumers;
	}

}
