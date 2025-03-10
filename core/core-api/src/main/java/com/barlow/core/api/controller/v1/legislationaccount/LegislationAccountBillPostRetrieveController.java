package com.barlow.core.api.controller.v1.legislationaccount;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
import com.barlow.core.enumerate.LegislationType;
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
		@PathVariable("legislationType") LegislationType legislationType,
		@RequestParam MultiValueMap<String, String> params
	) {
		LegislationAccountBillPostsRequest request = LegislationAccountBillPostsRequest.sanitizeFrom(params);
		BillPostQuery query = BillPostQuery.legislationOf(legislationType, request.getPage(), request.getSize(),
			request.getSort(), request.getFilters());
		BillPostsStatus billPostsStatus = billPostRetrieveService.readBillPosts(query);
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
