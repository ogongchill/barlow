package com.barlow.client.knal.opendata.api.response.common;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public final class ItemResponseBody<T> implements Body<T> {

    @JacksonXmlProperty(localName = "item")
    @JacksonXmlElementWrapper(localName = "items")
    private List<T> items;

    @JacksonXmlProperty(localName = "numOfRows")
    private Integer numOfRows;

    @JacksonXmlProperty(localName = "pageNo")
    private Integer pageNo;

    @JacksonXmlProperty(localName = "totalCount")
    private Integer totalCount;

    public List<T> items() {
        return items;
    }

	@Override
	public T item() {
		return null;
	}

	@Override
	public T body() {
		return null;
	}

	public Integer numOfRows() {
        return numOfRows;
    }

    public Integer pageNo() {
        return pageNo;
    }

    public Integer totalCount() {
        return totalCount;
    }
}
