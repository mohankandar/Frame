package com.wynd.vop.framework.kong.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class KongJwtTokenResult implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	@JsonProperty("data")
	private List<KongJwtToken> data;
	private int total;
	private String next;

	public List<KongJwtToken> getData() {
		return data;
	}

	public void setData(List<KongJwtToken> data) {
		this.data = data;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}
}
