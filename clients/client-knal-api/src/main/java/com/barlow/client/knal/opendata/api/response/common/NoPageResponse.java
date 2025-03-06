package com.barlow.client.knal.opendata.api.response.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class NoPageResponse<T> {

    @JacksonXmlProperty(localName = "header")
    protected Header header;

    @JacksonXmlProperty(localName = "body")
    protected ItemListBody<T> body;

    public Header getHeader() {
        return header;
    }

    public ItemListBody<T> getBody() {
        return body;
    }

    @Override
    public String toString() {
        String linedSeparator = System.lineSeparator();
        StringBuilder builder = new StringBuilder();
        if (header != null) {
            builder.append("header.code: ").append(header.getCode()).append(linedSeparator)
                    .append("header.message: ").append(header.getMessage()).append(linedSeparator);
        }
        if (body != null) {
            for (T item : body.getItems()) {
                builder.append(item).append(linedSeparator);
            }
        }
        return builder.toString();
    }
}
