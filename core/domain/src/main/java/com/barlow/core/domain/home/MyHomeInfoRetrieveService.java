package com.barlow.core.domain.home;

import org.springframework.stereotype.Service;

import com.barlow.core.domain.User;

@Service
public class MyHomeInfoRetrieveService {

	private final MyHomeInfoReader myHomeInfoReader;

	public MyHomeInfoRetrieveService(MyHomeInfoReader myHomeInfoReader) {
		this.myHomeInfoReader = myHomeInfoReader;
	}

	public MyHomeStatus retrieveHome(User user) {
		return myHomeInfoReader.readHome(user);
	}
}
