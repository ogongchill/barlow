package com.barlow.core.domain.billpost;

import org.springframework.stereotype.Component;

@Component
public class BillPostReader {

	private final BillPostRepository billPostRepository;

	public BillPostReader(BillPostRepository billPostRepository) {
		this.billPostRepository = billPostRepository;
	}

	public BillPostsStatus readBillPosts(BillPostQuery query) {
		return billPostRepository.retrieveRecentBillPosts(query);
	}

	public BillPost readBillPostDetail(BillPostDetailQuery query) {
		BillPost billPost = billPostRepository.retrieveRecentBillPost(query);
		if (billPost == null) {
			throw BillPostDomainException.notFound(query.billId());
		}
		return billPost;
	}
}
