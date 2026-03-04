package com.barlow.core.service.home;

import org.springframework.stereotype.Service;

import com.barlow.core.domain.User;
import com.barlow.core.domain.home.MyHomeStatus;

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
