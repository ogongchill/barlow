package com.barlow.client.knal.opendata.api.response.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public final class SingleItemBody<T> {

    @JacksonXmlProperty(localName = "item")
    private T item;

    public T getItem() {
        return item;
    }
}
