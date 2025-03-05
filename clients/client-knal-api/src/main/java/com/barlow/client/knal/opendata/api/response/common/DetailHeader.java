package com.barlow.client.knal.opendata.api.response.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public final class DetailHeader extends Header {
    @JacksonXmlProperty(localName = "successYN")
    private String successYN;

    @JacksonXmlProperty(localName = "requestMsgID")
    private String requestMsgID;

    @JacksonXmlProperty(localName = "responseMsgID")
    private String responseMsgID;

    @JacksonXmlProperty(localName = "responseTime")
    private String responseTime;

    public String getSuccessYN() {
        return successYN;
    }

    public String getRequestMsgID() {
        return requestMsgID;
    }

    public String getResponseMsgID() {
        return responseMsgID;
    }

    public String getResponseTime() {
        return responseTime;
    }
}
