package com.barlow.core.domain.home;

import org.springframework.stereotype.Service;

import com.barlow.core.domain.User;

@Service
public class HomeRetrieveService {

	private final HomeInfoReader homeInfoReader;

	public HomeRetrieveService(HomeInfoReader homeInfoReader) {
		this.homeInfoReader = homeInfoReader;
	}

	public HomeStatus retrieveHome(User user) {
		return homeInfoReader.readHome(user);
	}
}
