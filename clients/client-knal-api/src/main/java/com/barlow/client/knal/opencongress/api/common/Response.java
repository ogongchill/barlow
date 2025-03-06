package com.barlow.client.knal.opencongress.api.common;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response<T> {

	@JsonProperty("head")
	protected List<Head> head;

	@JsonProperty("row")
	protected List<T> row;

	public List<Head> getHead() {
		return head;
	}

	public int getTotalCount() {
		return getTotalCountHead().listTotalCount();
	}

	public Head getTotalCountHead() {
		return head.getFirst();
	}

	public String getResultCode() {
		return getResultHead().result().code();
	}

	public String getResultMessage() {
		return getResultHead().result().message();
	}

	public Head getResultHead() {
		return head.getLast();
	}

	public List<T> getRow() {
		return row;
	}
}
