package com.barlow.client.knal.api.request;

import com.barlow.client.knal.api.code.BillKindCode;
import com.barlow.client.knal.api.code.CommitteeCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <a href="https://www.data.go.kr/data/3037286/openapi.do">IROS4_OA_DV_0401_OpenAPI활용가이드_의안정보(국회사무처)_v1.30<</a></href>의 <code>getRecentMoorListRequest</code>오퍼레이션의 요청 파라미터임
 * @see com.barlow.client.knal.api.Operation
 * @see com.barlow.client.knal.api.response.RecentMoorListResponse
 * @see com.barlow.client.knal.api.response.item.RecentMoorListItem
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecentMoorListRequest {

	private Integer numOfRows;
	private Integer pageNo;
	@JsonProperty(value = "bill_kind_cd")
	private String billKindCode;
	@JsonProperty(value = "curr_committee")
	private String currentCommittee;
	@JsonProperty(value = "bill_name")
	private String billName;
	@JsonProperty(value = "start_age_cd")
	private Integer startAgeCode; // 대수(현재 대수만 가능)

	private RecentMoorListRequest(Builder builder) {
		this.numOfRows = builder.numOfRows;
		this.pageNo = builder.pageNo;
		this.billKindCode = builder.billKindCd;
		this.currentCommittee = builder.currentCommittee;
		this.billName = builder.billName;
		this.startAgeCode = builder.startAgeCode;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private Integer numOfRows;
		private Integer pageNo;
		private String billKindCd;
		private String currentCommittee;
		private String billName;
		private Integer startAgeCode;

		public Builder numOfRows(Integer numOfRows) {
			this.numOfRows = numOfRows;
			return this;
		}

		public Builder pageNo(Integer pageNo) {
			this.pageNo = pageNo;
			return this;
		}

		public Builder billKindCode(BillKindCode billKindCode) {
			this.billKindCd = billKindCode.name();
			return this;
		}

		public Builder currCommittee(CommitteeCode currentCommitteeCode) {
			this.currentCommittee = currentCommitteeCode.getCode();
			return this;
		}

		public Builder billName(String billName) {
			this.billName = billName;
			return this;
		}

		public Builder startAgeCode(Integer startAgeCode) {
			this.startAgeCode = startAgeCode;
			return this;
		}

		public RecentMoorListRequest build() {
			return new RecentMoorListRequest(this);
		}
	}

	public Integer getNumOfRows() {
		return numOfRows;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public String getBillKindCode() {
		return billKindCode;
	}

	public String getCurrentCommittee() {
		return currentCommittee;
	}

	public String getBillName() {
		return billName;
	}

	public Integer getStartAgeCode() {
		return startAgeCode;
	}
}
