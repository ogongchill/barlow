package com.barlow.client.knal.opendata.api.response.error;

import java.util.List;

import com.barlow.client.knal.opendata.api.response.common.Body;

public class ErrorEmptyBody implements Body<Void> {

	@Override
	public Integer numOfRows() {
		return null;
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
	public List<Void> items() {
		return List.of();
	}

	@Override
	public Void item() {
		return null;
	}

	@Override
	public Void body() {
		return null;
	}
}
