package com.wynd.vop.framework.kong.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public class KongConsumer implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	private String id;
	@JsonProperty("created_at")
	private Instant createdAt;
	private String username;
	@JsonProperty("custom_id")
	private String customId;
	private List<String> tags;

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCustomId() {
		return customId;
	}

	public void setCustomId(String customId) {
		this.customId = customId;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
}
