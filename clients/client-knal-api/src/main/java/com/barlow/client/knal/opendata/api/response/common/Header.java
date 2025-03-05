package com.barlow.client.knal.opendata.api.response.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Header {
    @JacksonXmlProperty(localName = "resultCode")
    private Integer code;

    @JacksonXmlProperty(localName = "resultMsg")
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
