package com.barlow.storage.db.core;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.barlow.core.domain.billpost.BillPostDetailQuery;
import com.barlow.core.domain.billpost.BillPostQuery;
import com.barlow.core.domain.billpost.BillProposer;
import com.barlow.core.domain.billpost.BillPost;
import com.barlow.core.domain.billpost.BillPostRepository;
import com.barlow.core.domain.billpost.BillPostsStatus;
import com.barlow.core.domain.billpost.BillPostFilterTag;
import com.barlow.core.support.SortKey;

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
		SortKey sortKey = query.sortKey();
		BillPostFilterTag filterTag = query.tags();
		Sort sort = Sort.by(Sort.Direction.valueOf(sortKey.getSortOrder()), sortKey.getSortField());
		Pageable pageable = PageRequest.of(query.page(), query.size(), sort);

		Specification<BillPostJpaEntity> specification = Specification
			.where(BillPostSpecifications.isPreAnnouncement(filterTag.isPreAnnouncement()))
			.and(BillPostSpecifications.hasLegislationTypeTag(filterTag.getLegislationTypeTags()))
			.and(BillPostSpecifications.hasProgressStatusTag(filterTag.getProgressStatusTags()))
			.and(BillPostSpecifications.hasProposerTypeTag(filterTag.getProposerTypeTags()))
			.and(BillPostSpecifications.hasPartyNameTag(filterTag.getPartyNameTags()));
		Slice<BillPostJpaEntity> billPostJpaEntities = billPostJpaRepository.findAll(specification, pageable);

		List<BillPost> billPosts = billPostJpaEntities.stream()
			.map(billPostJpaEntity -> {
				BillPost recentBillPost = billPostJpaEntity.toRecentBillPost();
				if (billPostJpaEntity.hasPreAnnouncementInfo()) {
					recentBillPost.assignPreAnnouncementInfo(billPostJpaEntity.getPreAnnouncementInfo());
				}
				return recentBillPost;
			})
			.toList();
		return new BillPostsStatus(billPosts, billPostJpaEntities.isLast());
	}

	@Override
	public BillPost retrieveRecentBillPost(BillPostDetailQuery query) {
		BillPostJpaEntity billPostJpaEntity = billPostJpaRepository.findByBillId(query.billId());
		if (billPostJpaEntity == null) {
			return null;
		}
		BillPost billPost = billPostJpaEntity.toRecentBillPost();
		List<BillProposer> billProposers = billProposerJpaRepository.findAllByBillId(query.billId())
			.stream()
			.map(BillProposerJpaEntity::toBillProposer)
			.toList();
		billPost.setBillProposers(billProposers);
		return billPost;
	}
}
