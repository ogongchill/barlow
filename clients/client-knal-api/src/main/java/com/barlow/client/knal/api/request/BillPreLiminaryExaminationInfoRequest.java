package com.barlow.client.knal.api.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <a href="https://www.data.go.kr/data/3037286/openapi.do">IROS4_OA_DV_0401_OpenAPI활용가이드_의안정보(국회사무처)_v1.30<</a></href>의 <code>getBillAdditionalInfo</code>오퍼레이션의 요청 파라미터임
 * @see com.barlow.client.knal.api.Operation
 * @see com.barlow.client.knal.api.response.BillPreLiminaryExaminationInfoResponse
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillPreLiminaryExaminationInfoRequest {

	@JsonProperty(value = "bill_id")
	private String billId;

	public BillPreLiminaryExaminationInfoRequest(String billId) {
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

		public BillPreLiminaryExaminationInfoRequest build() {
			return new BillPreLiminaryExaminationInfoRequest(billId);
		}
	}

	public String getBillId() {
		return billId;
	}
}
