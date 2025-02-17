package com.barlow.storage.db.core;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.barlow.core.domain.recentbill.BillPostDetailQuery;
import com.barlow.core.domain.recentbill.BillPostQuery;
import com.barlow.core.domain.recentbill.BillProposer;
import com.barlow.core.domain.recentbill.BillPost;
import com.barlow.core.domain.recentbill.BillPostRepository;
import com.barlow.core.domain.recentbill.BillPostsStatus;

@Component
public class BillPostRepositoryAdapter implements BillPostRepository {

	private final BillPostJpaRepository billPostJpaRepository;
	private final BillProposerJpaRepository billProposerJpaRepository;

	public BillPostRepositoryAdapter(
		BillPostJpaRepository billPostJpaRepository,
		BillProposerJpaRepository billProposerJpaRepository
	) {
		this.billPostJpaRepository = billPostJpaRepository;
		this.billProposerJpaRepository = billProposerJpaRepository;
	}

	@Override
	public BillPostsStatus retrieveRecentBillPosts(BillPostQuery query) {
		SortKey sortKey = new SortKey(query.sortKey());
		BillPostFilterTag filterTag = BillPostFilterTag.from(query.tags());
		Pageable pageable = PageRequest.of(query.page(), query.size(), sortKey.getSort());
		Slice<BillPostJpaEntity> billPostJpaEntities;
		if (filterTag.isPartyNameTagEmpty()) {
			billPostJpaEntities = billPostJpaRepository.findAllBy(
				filterTag.getLegislationTypeTags(),
				filterTag.getLegislationStatusTags(),
				filterTag.getProposerTypeTags(),
				pageable
			);
		} else {
			billPostJpaEntities = billPostJpaRepository.findAllBy(
				filterTag.getLegislationTypeTags(),
				filterTag.getLegislationStatusTags(),
				filterTag.getProposerTypeTags(),
				filterTag.getPartyNameTags(),
				pageable
			);
		}
		List<BillPost> billPosts = billPostJpaEntities.stream()
			.map(BillPostJpaEntity::toRecentBillPost)
			.toList();
		return new BillPostsStatus(billPosts, billPostJpaEntities.isLast());
	}

	@Override
	public BillPost retrieveRecentBillPost(BillPostDetailQuery query) {
		BillPost billPost = billPostJpaRepository.findByBillId(query.billId()).toRecentBillPost();
		List<BillProposer> billProposers = billProposerJpaRepository.findAllByBillId(query.billId())
			.stream()
			.map(BillProposerJpaEntity::toBillProposer)
			.toList();
		billPost.setBillProposers(billProposers);
		return billPost;
	}
}
