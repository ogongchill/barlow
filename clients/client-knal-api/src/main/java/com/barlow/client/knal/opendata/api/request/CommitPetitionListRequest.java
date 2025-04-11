package com.barlow.client.knal.opendata.api.request;

import com.barlow.client.knal.opendata.api.Operation;
import com.barlow.client.knal.opendata.api.code.BillKindCode;
import com.barlow.client.knal.opendata.api.response.CommitPetitionListResponse;
import com.barlow.client.knal.opendata.api.response.item.CommitPetitionListItem;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <a href="https://www.data.go.kr/data/3037286/openapi.do">IROS4_OA_DV_0401_OpenAPI활용가이드_의안정보(국회사무처)_v1.30<</a></href>의 <resultCode>getCommitPetitionList</resultCode>오퍼레이션의 요청 파라미터임
 *
 * @see Operation
 * @see CommitPetitionListItem
 * @see CommitPetitionListResponse
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommitPetitionListRequest {

	private Integer numOfRows;
	private Integer pageNo;
	private String gbn;
	@JsonProperty("bill_kind_cd")
	private String billKindCode;
	@JsonProperty("start_age_cd")
	private Integer startAgeCode;

	private CommitPetitionListRequest(Builder builder) {
		this.numOfRows = builder.numOfRows;
		this.pageNo = builder.pageNo;
		this.gbn = builder.gbn;
		this.billKindCode = builder.billKindCode;
		this.startAgeCode = builder.startAgeCode;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Integer numOfRows;
		private Integer pageNo;
		private String gbn;
		private String billKindCode;
		private Integer startAgeCode;

		public Builder numOfRows(Integer numOfRows) {
			this.numOfRows = numOfRows;
			return this;
		}

		public Builder pageNo(Integer pageNo) {
			this.pageNo = pageNo;
			return this;
		}

		public Builder gbn(String gbn) {
			this.gbn = gbn;
			return this;
		}

		public Builder billKindCode(BillKindCode billKindCode) {
			this.billKindCode = billKindCode.name();
			return this;
		}

		public Builder startAgeCode(Integer startAgeCd) {
			this.startAgeCode = startAgeCd;
			return this;
		}

		public CommitPetitionListRequest build() {
			return new CommitPetitionListRequest(this);
		}
	}

	public Integer getNumOfRows() {
		return numOfRows;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public String getGbn() {
		return gbn;
	}

	public String getBillKindCode() {
		return billKindCode;
	}

	public Integer getStartAgeCode() {
		return startAgeCode;
	}
}
