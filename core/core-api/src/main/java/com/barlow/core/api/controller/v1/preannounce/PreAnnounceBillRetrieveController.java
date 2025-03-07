package com.barlow.core.api.controller.v1.preannounce;

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
@RequestMapping("/api/v1/pre-announcement-bills")
public class PreAnnounceBillRetrieveController {

	private final BillPostRetrieveService billPostRetrieveService;

	public PreAnnounceBillRetrieveController(BillPostRetrieveService billPostRetrieveService) {
		this.billPostRetrieveService = billPostRetrieveService;
	}

	@GetMapping
	public ApiResponse<PreAnnounceBillPostsResponse> retrievePreAnnouncementBills(
		@RequestParam(name = "page") Integer page,
		@RequestParam(name = "size") Integer size,
		@RequestParam(name = "sort", defaultValue = "deadlineDate#DESC", required = false) String sortKey,
		@RequestParam(name = "tags", required = false) Map<String, List<String>> tags
	) {
		BillPostQuery billPostQuery = BillPostQuery.preAnnounceOf(page, size, sortKey, tags);
		BillPostsStatus billPostsStatus = billPostRetrieveService.readBillPosts(billPostQuery);
		PreAnnounceBillPostsApiSpecComposer specComposer = new PreAnnounceBillPostsApiSpecComposer(billPostsStatus);
		return ApiResponse.success(specComposer.compose(LocalDate.now()));
	}

	@GetMapping("/{billId}")
	public ApiResponse<PreAnnounceBillPostDetailResponse> retrieveBillPostWithPreAnnouncementBill(
		@PathVariable("billId") String billId
	) {
		BillPost billPost = billPostRetrieveService.readBillPostDetail(new BillPostDetailQuery(billId));
		PreAnnounceBillPostDetailApiSpecComposer specComposer = new PreAnnounceBillPostDetailApiSpecComposer(billPost);
		return ApiResponse.success(specComposer.compose(LocalDate.now()));
	}
}
