package com.barlow.client.knal.opendata.api.request;

import com.barlow.client.knal.opendata.api.Operation;
import com.barlow.client.knal.opendata.api.response.RecentRceptListResponse;
import com.barlow.client.knal.opendata.api.response.item.RecentRceptListItem;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <a href="https://www.data.go.kr/data/3037286/openapi.do">IROS4_OA_DV_0401_OpenAPI활용가이드_의안정보(국회사무처)_v1.30<</a></href>의 <code>getRecentPasageList</code>요청 파라미터임
 * @see Operation
 * @see RecentRceptListResponse
 * @see RecentRceptListItem
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecentPasageListRequest {

    private Integer numOfRows; // 한 페이지 결과 수, 기본값은 10
    private Integer pageNo; // 페이지 번호
    @JsonProperty(value = "bill_name")
    private String billName; // 의안명

    private RecentPasageListRequest(Builder builder) {
        this.numOfRows = builder.numOfRows;
        this.pageNo = builder.pageNo;
        this.billName = builder.billName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Integer numOfRows;
        private Integer pageNo;
        private String billName;

        public Builder numOfRows(Integer numOfRows) {
            this.numOfRows = numOfRows;
            return this;
        }

        public Builder pageNo(Integer pageNo) {
            this.pageNo = pageNo;
            return this;
        }

        public Builder billName(String billName) {
            this.billName = billName;
            return this;
        }

        public RecentPasageListRequest build() {
            return new RecentPasageListRequest(this);
        }
    }

    public Integer getNumOfRows() {
        return numOfRows;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public String getBillName() {
        return billName;
    }
}
