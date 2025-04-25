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

	DefaultRequest(Type type, Integer pIndex, Integer pSize) {
		this.type = type;
		this.pIndex = pIndex;
		this.pSize = pSize;
	}

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
			return new DefaultRequest(type, pIndex, pSize);
		}
	}

	public Type getType() {
		return type;
	}

	public Integer getpIndex() {
		return pIndex;
	}

	public Integer getpSize() {
		return pSize;
	}

	enum Type {
		JSON, XML
	}
}
