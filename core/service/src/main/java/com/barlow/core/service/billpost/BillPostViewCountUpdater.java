package com.barlow.core.service.billpost;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.billpost.BillPostRepository;

@Component
public class BillPostViewCountUpdater {

	private final BillPostRepository billPostRepository;

	public BillPostViewCountUpdater(BillPostRepository billPostRepository) {
		this.billPostRepository = billPostRepository;
	}

	public void update(String billId) {
		billPostRepository.updateViewCount(billId);
	}
}
