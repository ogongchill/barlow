package com.barlow.client.knal.opendata.api.request;

import com.barlow.client.knal.opendata.api.Operation;
import com.barlow.client.knal.opendata.api.response.item.BillPetitionMemberListItem;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <a href="https://www.data.go.kr/data/3037286/openapi.do">IROS4_OA_DV_0401_OpenAPI활용가이드_의안정보(국회사무처)_v1.30<</a></href>의 <code>getBillPetitionMemberList</code>오퍼레이션의 요청 파라미터임
 *
 * @see Operation
 * @see BillPetitionMemberListItem
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillPetitionMemberListRequest {

	@JsonProperty(value = "bill_id")
	private String billId;
	private String gbn1;
	private String gbn2;

	private BillPetitionMemberListRequest(Builder builder) {
		this.billId = builder.billId;
		this.gbn1 = builder.gbn1;
		this.gbn2 = builder.gbn2;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private String billId;
		private String gbn1;
		private String gbn2;

		private Builder() {
		}

		public Builder billId(String billId) {
			this.billId = billId;
			return this;
		}

		public Builder gbn1(String gbn1) {
			this.gbn1 = gbn1;
			return this;
		}

		public Builder gbn2(String gbn2) {
			this.gbn2 = gbn2;
			return this;
		}

		public BillPetitionMemberListRequest build() {
			return new BillPetitionMemberListRequest(this);
		}
	}

	public String getBillId() {
		return billId;
	}

	public String getGbn1() {
		return gbn1;
	}

	public String getGbn2() {
		return gbn2;
	}
}
