package com.barlow.client.knal.api.request;

import com.barlow.client.knal.api.code.BillKindCode;
import com.barlow.client.knal.api.code.CommitteeCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <a href="https://www.data.go.kr/data/3037286/openapi.do">IROS4_OA_DV_0401_OpenAPI활용가이드_의안정보(국회사무처)_v1.30<</a></href>의 <code>getJsictionComiteProcessList</code>오퍼레이션의 요청 파라미터임
 * @see com.barlow.client.knal.api.Operation
 * @see com.barlow.client.knal.api.response.JsictionComiteProcessListResponse
 * @see com.barlow.client.knal.api.response.item.JsictionComiteProcessListItem
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsictionComiteProcessListRequest {

	private Integer numOfRows;
	private Integer pageNo;
	@JsonProperty(value = "start_age_cd")
	private Integer startAgeCode;
	@JsonProperty(value = "bill_kind_cd")
	private String billKindCode;
	@JsonProperty(value = "curr_committee")
	private String currentCommittee;

	private JsictionComiteProcessListRequest(Builder builder) {
		this.numOfRows = builder.numOfRows;
		this.pageNo = builder.pageNo;
		this.startAgeCode = builder.startAgeCode;
		this.billKindCode = builder.billKindCode;
		this.currentCommittee = builder.currentCommittee;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Integer numOfRows;
		private Integer pageNo;
		private Integer startAgeCode;
		private String billKindCode;
		private String currentCommittee;

		public Builder numOfRows(Integer numOfRows) {
			this.numOfRows = numOfRows;
			return this;
		}

		public Builder pageNo(Integer pageNo) {
			this.pageNo = pageNo;
			return this;
		}

		public Builder startAgeCode(Integer startAgeCode) {
			this.startAgeCode = startAgeCode;
			return this;
		}

		public Builder billKindCd(BillKindCode billKindCode) {
			this.billKindCode = billKindCode.name();
			return this;
		}

		public Builder currentCommittee(CommitteeCode currentCommitteeCode) {
			this.currentCommittee = currentCommitteeCode.getCode();
			return this;
		}

		public JsictionComiteProcessListRequest build() {
			return new JsictionComiteProcessListRequest(this);
		}
	}

	public Integer getNumOfRows() {
		return numOfRows;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public Integer getStartAgeCode() {
		return startAgeCode;
	}

	public String getBillKindCode() {
		return billKindCode;
	}

	public String getCurrentCommittee() {
		return currentCommittee;
	}
}
