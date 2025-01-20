package com.barlow.client.knal.api.response.common;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ItemListBody<T> {
    @JacksonXmlProperty(localName = "item")
    @JacksonXmlElementWrapper(localName = "items")
    private List<T> items;

    public List<T> getItems() {
        return items;
    }
}
