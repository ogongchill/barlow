package com.barlow.client.knal.opendata.api.response.common;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public final class SingleItemBody<T> implements Body<T> {

    @JacksonXmlProperty(localName = "item")
    private T item;

    @Override
    public T item() {
        return item;
    }

    @Override
    public Integer numOfRows() {
        return 0;
    }

    @Override
    public Integer pageNo() {
        return null;
    }

    @Override
    public Integer totalCount() {
        return null;
    }

    @Override
    public List<T> items() {
        return List.of();
    }

    @Override
    public T body() {
        return null;
    }
}
