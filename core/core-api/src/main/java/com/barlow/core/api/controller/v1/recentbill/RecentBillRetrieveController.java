package com.barlow.core.api.controller.v1.recentbill;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
import com.barlow.core.support.response.ApiResponse;

@RestController
@RequestMapping("/api/v1/recent-bill")
public class RecentBillRetrieveController {

	private final BillPostRetrieveService billPostRetrieveService;

	public RecentBillRetrieveController(BillPostRetrieveService billPostRetrieveService) {
		this.billPostRetrieveService = billPostRetrieveService;
	}

	@GetMapping("/thumbnail")
	public ApiResponse<RecentBillPostsResponse> retrieveRecentBill(
		@RequestParam(name = "page") Integer page,
		@RequestParam(name = "size") Integer size,
		@RequestParam(name = "sort", defaultValue = "createdAt#DESC", required = false) String sortKey,
		@RequestParam(name = "tags", required = false) Map<String, List<String>> tags
	) {
		BillPostsStatus billPostsStatus
			= billPostRetrieveService.readBillPosts(BillPostQuery.defaultOf(page, size, sortKey, tags));
		RecentBillPostsApiSpecComposer apiSpecComposer = new RecentBillPostsApiSpecComposer(billPostsStatus);
		return ApiResponse.success(apiSpecComposer.compose(LocalDate.now()));
	}

	@GetMapping("/detail/{recentBillId}")
	public ApiResponse<RecentBillPostDetailResponse> retrieveRecentBillDetail(
		@PathVariable("recentBillId") String recentBillId
	) {
		BillPost billPost = billPostRetrieveService.readBillPostDetail(new BillPostDetailQuery(recentBillId));
		RecentBillPostDetailApiSpecComposer apiSpecComposer = new RecentBillPostDetailApiSpecComposer(billPost);
		return ApiResponse.success(apiSpecComposer.compose());
	}
}
