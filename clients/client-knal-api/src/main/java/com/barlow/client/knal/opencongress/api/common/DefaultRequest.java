package com.barlow.client.knal.opencongress.api.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefaultRequest {

	@JsonProperty(value = "Type")
	private Type type;

	@JsonProperty(value = "pIndex")
	private Integer pIndex;

	@JsonProperty(value = "pSize")
	private Integer pSize;

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Type type;
		private Integer pIndex;
		private Integer pSize;

		public Builder type(String type) {
			this.type = Type.valueOf(type.toUpperCase());
			return this;
		}

		public Builder pIndex(Integer pIndex) {
			this.pIndex = pIndex;
			return this;
		}

		public Builder pSize(Integer pSize) {
			this.pSize = pSize;
			return this;
		}

		public DefaultRequest build() {
			DefaultRequest defaultRequest = new DefaultRequest();
			defaultRequest.type = type;
			defaultRequest.pIndex = pIndex;
			defaultRequest.pSize = pSize;
			return defaultRequest;
		}
	}

	enum Type {
		JSON, XML
	}
}
