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

import com.barlow.core.domain.billpost.BillPostDetailQuery;
import com.barlow.core.domain.billpost.BillPostQuery;
import com.barlow.core.domain.billpost.BillPost;
import com.barlow.core.domain.billpost.BillPostRetrieveService;
import com.barlow.core.domain.billpost.BillPostsStatus;
import com.barlow.app.support.response.ApiResponse;

@RestController
@RequestMapping("/api/v1/recent-bill")
public class RecentBillRetrieveController {

	private static final Logger log = LoggerFactory.getLogger(RecentBillRetrieveController.class);

	private final BillPostRetrieveService billPostRetrieveService;

	public RecentBillRetrieveController(BillPostRetrieveService billPostRetrieveService) {
		this.billPostRetrieveService = billPostRetrieveService;
	}

	@GetMapping("/thumbnail")
	public ApiResponse<RecentBillPostsResponse> retrieveRecentBill(
		@RequestParam MultiValueMap<String, String> params
	) {
		log.info("Received retrieve recent bill thumbnail request.");
		RecentBillPostsRequest request = RecentBillPostsRequest.sanitizeFrom(params);
		BillPostsStatus billPostsStatus = billPostRetrieveService.readBillPosts(
			BillPostQuery.defaultOf(request.getPage(), request.getSize(), request.getSort(), request.getFilters())
		);
		RecentBillPostsApiSpecComposer apiSpecComposer = new RecentBillPostsApiSpecComposer(billPostsStatus);
		return ApiResponse.success(apiSpecComposer.compose(LocalDate.now()));
	}

	@GetMapping("/detail/{recentBillId}")
	public ApiResponse<RecentBillPostDetailResponse> retrieveRecentBillDetail(
		@PathVariable("recentBillId") String recentBillId
	) {
		log.info("Received retrieve recent bill {} detail request.", recentBillId);
		BillPost billPost = billPostRetrieveService.readBillPostDetail(new BillPostDetailQuery(recentBillId));
		RecentBillPostDetailApiSpecComposer apiSpecComposer = new RecentBillPostDetailApiSpecComposer(billPost);
		return ApiResponse.success(apiSpecComposer.compose());
	}
}
