package com.barlow.client.knal.opendata.api.response.common;

import java.util.List;

public interface Body<T> {

	Integer numOfRows();

	Integer pageNo();

	Integer totalCount();

	List<T> items();

	T item();

	T body();
}
