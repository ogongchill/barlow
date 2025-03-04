package com.barlow.core.api.controller.v1.legislationaccount;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.barlow.core.domain.billpost.BillPost;
import com.barlow.core.domain.billpost.BillPostDetailQuery;
import com.barlow.core.domain.billpost.BillPostQuery;
import com.barlow.core.domain.billpost.BillPostRetrieveService;
import com.barlow.core.domain.billpost.BillPostsStatus;
import com.barlow.core.support.response.ApiResponse;

@RestController
@RequestMapping("/api/v1/legislation-accounts")
public class LegislationAccountBillPostRetrieveController {

	private final BillPostRetrieveService billPostRetrieveService;

	public LegislationAccountBillPostRetrieveController(BillPostRetrieveService billPostRetrieveService) {
		this.billPostRetrieveService = billPostRetrieveService;
	}

	@GetMapping("/{legislationType}/bill-posts")
	public ApiResponse<LegislationAccountBillPostsResponse> retrieveBillPostThumbnail(
		@PathVariable("legislationType") String legislationType,
		@RequestParam(name = "page") Integer page,
		@RequestParam(name = "size") Integer size,
		@RequestParam(name = "sort", defaultValue = "createdAt#DESC", required = false) String sortKey,
		@RequestParam(name = "tags", required = false) Map<String, List<String>> tags
	) {
		BillPostQuery billPostQuery = BillPostQuery.legislationOf(legislationType, page, size, sortKey, tags);
		BillPostsStatus billPostsStatus = billPostRetrieveService.readBillPosts(billPostQuery);
		LegislationAccountBillPostsApiSpecComposer apiSpecComposer
			= new LegislationAccountBillPostsApiSpecComposer(billPostsStatus);
		return ApiResponse.success(apiSpecComposer.compose(LocalDate.now()));
	}

	@GetMapping("/bill-posts/{billId}")
	public ApiResponse<LegislationAccountBillPostDetailResponse> retrieveBillPostDetail(
		@PathVariable("billId") String billId
	) {
		BillPost billPost = billPostRetrieveService.readBillPostDetail(new BillPostDetailQuery(billId));
		LegislationAccountBillPostDetailApiSpecComposer apiSpecComposer
			= new LegislationAccountBillPostDetailApiSpecComposer(billPost);
		return ApiResponse.success(apiSpecComposer.compose());
	}
}
