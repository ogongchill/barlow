package com.barlow.core.api.controller.v1.preannounce;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.barlow.core.support.response.ApiResponse;

@RestController
@RequestMapping("/api/v1/pre-announcement-bills")
public class PreAnnounceBillRetrieveController {

	private static final Logger log = LoggerFactory.getLogger(PreAnnounceBillRetrieveController.class);

	private final BillPostRetrieveService billPostRetrieveService;

	public PreAnnounceBillRetrieveController(BillPostRetrieveService billPostRetrieveService) {
		this.billPostRetrieveService = billPostRetrieveService;
	}

	@GetMapping
	public ApiResponse<PreAnnounceBillPostsResponse> retrievePreAnnouncementBills(
		@RequestParam MultiValueMap<String, String> params
	) {
		log.info("Received retrieve pre-announcement bill post thumbnail request.");
		PreAnnounceBillPostsRequest request = PreAnnounceBillPostsRequest.sanitizeFrom(params);
		BillPostsStatus billPostsStatus = billPostRetrieveService.readBillPosts(
			BillPostQuery.preAnnounceOf(request.getPage(), request.getSize(), request.getSort(), request.getFilters())
		);
		PreAnnounceBillPostsApiSpecComposer specComposer = new PreAnnounceBillPostsApiSpecComposer(billPostsStatus);
		return ApiResponse.success(specComposer.compose(LocalDate.now()));
	}

	@GetMapping("/{billId}")
	public ApiResponse<PreAnnounceBillPostDetailResponse> retrieveBillPostWithPreAnnouncementBill(
		@PathVariable("billId") String billId
	) {
		log.info("Received retrieve pre-announcement bill {} post detail request.", billId);
		BillPost billPost = billPostRetrieveService.readBillPostDetail(new BillPostDetailQuery(billId));
		PreAnnounceBillPostDetailApiSpecComposer specComposer = new PreAnnounceBillPostDetailApiSpecComposer(billPost);
		return ApiResponse.success(specComposer.compose(LocalDate.now()));
	}
}
