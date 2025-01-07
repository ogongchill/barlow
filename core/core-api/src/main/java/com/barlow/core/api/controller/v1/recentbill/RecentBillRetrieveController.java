package com.barlow.core.api.controller.v1.recentbill;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.barlow.core.domain.recentbill.BillPostDetailQuery;
import com.barlow.core.domain.recentbill.BillPostQuery;
import com.barlow.core.domain.recentbill.RecentBillPost;
import com.barlow.core.domain.recentbill.RecentBillPostRetrieveService;
import com.barlow.core.domain.recentbill.RecentBillPostsStatus;
import com.barlow.core.support.response.ApiResponse;

@RestController
@RequestMapping("/api/v1/recent-bill")
public class RecentBillRetrieveController {

	private final RecentBillPostRetrieveService recentBillPostRetrieveService;

	public RecentBillRetrieveController(RecentBillPostRetrieveService recentBillPostRetrieveService) {
		this.recentBillPostRetrieveService = recentBillPostRetrieveService;
	}

	@GetMapping("/thumbnail")
	public ApiResponse<RecentBillPostsResponse> retrieveRecentBill(
		@RequestParam(name = "page") Integer page,
		@RequestParam(name = "size") Integer size,
		@RequestParam(name = "sort", required = false) String sortKey,
		@RequestParam(required = false) Map<String, List<String>> tags
	) {
		RecentBillPostsStatus recentBillPostsStatus
			= recentBillPostRetrieveService.readRecentBillPosts(new BillPostQuery(page, size, sortKey, tags));
		RecentBillPostsApiSpecComposer apiSpecComposer = new RecentBillPostsApiSpecComposer(recentBillPostsStatus);
		return ApiResponse.success(apiSpecComposer.compose(LocalDate.now()));
	}

	@GetMapping("/detail/{recentBillId}")
	public ApiResponse<RecentBillPostDetailResponse> retrieveRecentBillDetail(
		@PathVariable String recentBillId
	) {
		RecentBillPost recentBillPost
			= recentBillPostRetrieveService.readRecentBillPostDetail(new BillPostDetailQuery(recentBillId));
		RecentBillPostDetailApiSpecComposer apiSpecComposer = new RecentBillPostDetailApiSpecComposer(recentBillPost);
		return ApiResponse.success(apiSpecComposer.compose());
	}
}
