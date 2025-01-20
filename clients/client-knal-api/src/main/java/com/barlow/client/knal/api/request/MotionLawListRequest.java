package com.barlow.client.knal.api.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <a href="https://www.data.go.kr/data/3037286/openapi.do">IROS4_OA_DV_0401_OpenAPI활용가이드_의안정보(국회사무처)_v1.30<</a></href>의 <code>getMotionLawList</code>오퍼레이션의 요청 파라미터임
 * @see com.barlow.client.knal.api.Operation
 * @see com.barlow.client.knal.api.response.item.MotionLawListItem
 * @see com.barlow.client.knal.api.response.MotionLawListResponse
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MotionLawListRequest {

    private Integer numOfRows;
    private Integer pageNo;
    @JsonProperty("ord")
    private Integer ordinal;
    @JsonProperty("start_ord")
    private Integer startOrdinal;
    @JsonProperty("end_ord")
    private Integer endOrdinal;
    @JsonProperty("bill_name")
    private String billName;
    @JsonProperty("mem_name")
    private String memberName;
    @JsonProperty("hj_nm")
    private String hanjaName;
    @JsonProperty("mem_name_check")
    private String memNameCheck;

    public MotionLawListRequest(Builder builder) {
        this.numOfRows = builder.numOfRows;
        this.pageNo = builder.pageNo;
        this.ordinal = builder.ordinal;
        this.startOrdinal = builder.startOrdinal;
        this.endOrdinal = builder.endOrdinal;
        this.billName = builder.billName;
        this.memberName = builder.memberName;
        this.hanjaName = builder.hanjaName;
        this.memNameCheck = builder.memNameCheck;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Integer numOfRows;
        private Integer pageNo;
        private Integer ordinal;
        private Integer startOrdinal;
        private Integer endOrdinal;
        private String billName;
        private String memberName;
        private String hanjaName;
        private String memNameCheck;

        public Builder numOfRows(Integer numOfRows) {
            this.numOfRows = numOfRows;
            return this;
        }

        public Builder pageNo(Integer pageNo) {
            this.pageNo = pageNo;
            return this;
        }

        public Builder ordinal(Integer ordinal) {
            this.ordinal = ordinal;
            return this;
        }

        public Builder startOrdinal(Integer startOrd) {
            this.startOrdinal = startOrd;
            return this;
        }

        public Builder endOrdinal(Integer endOrd) {
            this.endOrdinal = endOrd;
            return this;
        }

        public Builder billName(String billName) {
            this.billName = billName;
            return this;
        }

        public Builder memName(String memName) {
            this.memberName = memName;
            return this;
        }

        public Builder hanjaName(String hanjaName) {
            this.hanjaName = hanjaName;
            return this;
        }

        public Builder memNameCheck(String memNameCheck) {
            this.memNameCheck = memNameCheck;
            return this;
        }

        public MotionLawListRequest build() {
            return new MotionLawListRequest(this);
        }
    }

    public Integer getNumOfRows() {
        return numOfRows;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public Integer getStartOrdinal() {
        return startOrdinal;
    }

    public Integer getEndOrdinal() {
        return endOrdinal;
    }

    public String getBillName() {
        return billName;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getHanjaName() {
        return hanjaName;
    }

    public String getMemNameCheck() {
        return memNameCheck;
    }
}
