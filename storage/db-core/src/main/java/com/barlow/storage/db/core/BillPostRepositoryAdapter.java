package com.barlow.storage.db.core;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.barlow.core.domain.recentbill.BillPostDetailQuery;
import com.barlow.core.domain.recentbill.BillPostQuery;
import com.barlow.core.domain.recentbill.BillProposer;
import com.barlow.core.domain.recentbill.RecentBillPost;
import com.barlow.core.domain.recentbill.RecentBillPostRepository;
import com.barlow.core.domain.recentbill.RecentBillPostsStatus;

@Component
public class BillPostRepositoryAdapter implements RecentBillPostRepository {

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
	public RecentBillPostsStatus retrieveRecentBillPosts(BillPostQuery query) {
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
		List<RecentBillPost> recentBillPosts = billPostJpaEntities.stream()
			.map(BillPostJpaEntity::toRecentBillPost)
			.toList();
		return new RecentBillPostsStatus(recentBillPosts, billPostJpaEntities.isLast());
	}

	@Override
	public RecentBillPost retrieveRecentBillPost(BillPostDetailQuery query) {
		RecentBillPost recentBillPost = billPostJpaRepository.findByBillId(query.billId()).toRecentBillPost();
		List<BillProposer> billProposers = billProposerJpaRepository.findAllByBillId(query.billId())
			.stream()
			.map(BillProposerJpaEntity::toBillProposer)
			.toList();
		recentBillPost.setBillProposers(billProposers);
		return recentBillPost;
	}
}
