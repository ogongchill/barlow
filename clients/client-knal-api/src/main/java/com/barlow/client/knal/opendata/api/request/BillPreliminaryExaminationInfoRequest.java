package com.barlow.client.knal.opendata.api.request;

import com.barlow.client.knal.opendata.api.Operation;
import com.barlow.client.knal.opendata.api.response.BillPreliminaryExaminationInfoResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <a href="https://www.data.go.kr/data/3037286/openapi.do">IROS4_OA_DV_0401_OpenAPI활용가이드_의안정보(국회사무처)_v1.30<</a></href>의 <resultCode>getBillAdditionalInfo</resultCode>오퍼레이션의 요청 파라미터임
 * @see Operation
 * @see BillPreliminaryExaminationInfoResponse
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillPreliminaryExaminationInfoRequest {

	@JsonProperty(value = "bill_id")
	private String billId;

	public BillPreliminaryExaminationInfoRequest(String billId) {
		this.billId = billId;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private String billId;

		public Builder billId(String billId) {
			this.billId = billId;
			return this;
		}

		public BillPreliminaryExaminationInfoRequest build() {
			return new BillPreliminaryExaminationInfoRequest(billId);
		}
	}

	public String getBillId() {
		return billId;
	}
}
