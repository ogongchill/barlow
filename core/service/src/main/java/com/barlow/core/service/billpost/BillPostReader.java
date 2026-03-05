package com.barlow.core.service.billpost;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.billpost.BillPost;
import com.barlow.core.domain.billpost.BillPostDetailQuery;
import com.barlow.core.domain.billpost.BillPostDomainException;
import com.barlow.core.domain.billpost.BillPostQuery;
import com.barlow.core.domain.billpost.BillPostRepository;
import com.barlow.core.domain.billpost.BillPostsStatus;

@Component
public class BillPostReader {

	private final BillPostRepository billPostRepository;

	public BillPostReader(BillPostRepository billPostRepository) {
		this.billPostRepository = billPostRepository;
	}

	public BillPostsStatus readBillPosts(BillPostQuery query) {
		return billPostRepository.retrieveRecentBillPosts(query);
	}

	public BillPost readBillPost(BillPostDetailQuery query) {
		BillPost billPost = billPostRepository.retrieveRecentBillPost(query);
		if (billPost == null) {
			throw BillPostDomainException.notFound(query.billId());
		}
		return billPost;
	}

}
