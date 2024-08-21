package com.wynd.vop.framework.kong.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public class KongJwtToken implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	@JsonProperty("rsa_public_key")
	private String rsaPublicKey;
	@JsonProperty("consumer_id")
	private String consumerId;
	private String id;
	@JsonProperty("created_at")
	private Instant createdAt;
	private String key;
	private String algorithm;
	private String secret;
	@JsonProperty("consumer")
	private KongConsumer consumer;
	private List<String> tags;

	public String getRsaPublicKey() {
		return rsaPublicKey;
	}

	public void setRsaPublicKey(String rsaPublicKey) {
		this.rsaPublicKey = rsaPublicKey;
	}

	public String getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public KongConsumer getConsumer() {
		return consumer;
	}

	public void setConsumer(KongConsumer consumer) {
		this.consumer = consumer;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
}
