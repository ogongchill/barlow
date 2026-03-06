package com.barlow.app.api.controller.v1.recentbill;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.barlow.core.domain.Passport;
import com.barlow.core.domain.billpost.BillPostDetailQuery;
import com.barlow.core.domain.billpost.BillPostQuery;
import com.barlow.core.domain.billpost.BillPost;
import com.barlow.core.service.billpost.BillPostRetrieveService;
import com.barlow.core.domain.billpost.BillPostsStatus;
import com.barlow.app.support.response.ApiResponse;
import com.barlow.infra.auth.support.annotation.PassportUser;

@RestController
@RequestMapping("/api/v1/recent-bills")
public class RecentBillRetrieveController {

	private static final Logger log = LoggerFactory.getLogger(RecentBillRetrieveController.class);

	private final BillPostRetrieveService billPostRetrieveService;

	public RecentBillRetrieveController(BillPostRetrieveService billPostRetrieveService) {
		this.billPostRetrieveService = billPostRetrieveService;
	}

	@GetMapping
	public ApiResponse<RecentBillPostsResponse> retrieveRecentBills(
		@RequestParam MultiValueMap<String, String> params) {
		log.info("Received retrieve recent bills request.");
		RecentBillPostsRequest request = RecentBillPostsRequest.sanitizeFrom(params);
		BillPostsStatus billPostsStatus = billPostRetrieveService.readBillPosts(
			BillPostQuery.defaultOf(request.getPage(), request.getSize(), request.getSort(), request.getFilters()));
		RecentBillPostsApiSpecComposer apiSpecComposer = new RecentBillPostsApiSpecComposer(billPostsStatus);
		return ApiResponse.success(apiSpecComposer.compose(LocalDate.now()));
	}

	@GetMapping("/{recentBillId}")
	public ApiResponse<RecentBillPostDetailResponse> retrieveRecentBillDetail(@PassportUser Passport passport,
		@PathVariable("recentBillId") String recentBillId) {
		log.info("Received retrieve recent bill {} detail request.", recentBillId);
		BillPost billPost = billPostRetrieveService.readBillPostDetail(passport, new BillPostDetailQuery(recentBillId));
		RecentBillPostDetailApiSpecComposer apiSpecComposer = new RecentBillPostDetailApiSpecComposer(billPost);
		return ApiResponse.success(apiSpecComposer.compose());
	}
}
