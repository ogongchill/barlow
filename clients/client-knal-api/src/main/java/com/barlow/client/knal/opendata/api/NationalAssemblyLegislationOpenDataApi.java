package com.barlow.client.knal.opendata.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import com.barlow.client.knal.opendata.api.request.BillAdditionalInfoRequest;
import com.barlow.client.knal.opendata.api.request.BillCommissionExaminationInfoRequest;
import com.barlow.client.knal.opendata.api.request.BillInfoListRequest;
import com.barlow.client.knal.opendata.api.request.BillPetitionMemberListRequest;
import com.barlow.client.knal.opendata.api.request.BillPreliminaryExaminationInfoRequest;
import com.barlow.client.knal.opendata.api.request.BillPromulgationInfoRequest;
import com.barlow.client.knal.opendata.api.request.BillReceiptInfoRequest;
import com.barlow.client.knal.opendata.api.request.CommitPetitionListRequest;
import com.barlow.client.knal.opendata.api.request.JsictionComiteProcessListRequest;
import com.barlow.client.knal.opendata.api.request.MotionLawListRequest;
import com.barlow.client.knal.opendata.api.request.RecentMoorListRequest;
import com.barlow.client.knal.opendata.api.request.RecentPasageListRequest;
import com.barlow.client.knal.opendata.api.request.RecentRceptListRequest;
import com.barlow.client.knal.opendata.api.request.SessionRequestListRequest;
import com.barlow.client.knal.opendata.api.response.BillInfoListResponse;
import com.barlow.client.knal.opendata.api.response.BillPetitionMemberListResponse;
import com.barlow.client.knal.opendata.api.response.BillPreliminaryExaminationInfoResponse;
import com.barlow.client.knal.opendata.api.response.BillPromulgationInfoResponse;
import com.barlow.client.knal.opendata.api.response.BillReceiptInfoResponse;
import com.barlow.client.knal.opendata.api.response.CommitPetitionListResponse;
import com.barlow.client.knal.opendata.api.response.JsictionComiteProcessListResponse;
import com.barlow.client.knal.opendata.api.response.MotionLawListResponse;
import com.barlow.client.knal.opendata.api.response.RecentMoorListResponse;
import com.barlow.client.knal.opendata.api.response.RecentPasageListResponse;
import com.barlow.client.knal.opendata.api.response.RecentRceptListResponse;
import com.barlow.client.knal.opendata.api.response.SessionRequestListResponse;
import com.barlow.client.knal.opendata.api.response.BillAdditionalInfoResponse;
import com.barlow.client.knal.opendata.api.response.BillCommissionExaminationInfoResponse;

@FeignClient(
	value = "${knal.open-data.api.name}",
	url = "${knal.open-data.api.url}",
	configuration = OpenDataConfig.class)
public interface NationalAssemblyLegislationOpenDataApi {

	@GetMapping(
		value = Operation.GET_BILL_INFO_LIST,
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_XML_VALUE)
	BillInfoListResponse getBillInfoList(@SpringQueryMap BillInfoListRequest request);

	@GetMapping(
		value = Operation.GET_BILL_PRELIMINARY_EXAMINATION_INFO,
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_XML_VALUE)
	BillPreliminaryExaminationInfoResponse getBillPreliminaryExaminationInfo(
		@SpringQueryMap BillPreliminaryExaminationInfoRequest request);

	@GetMapping(
		value = Operation.GET_RECENT_RCEPT_LIST,
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_XML_VALUE)
	RecentRceptListResponse getRecentRceptList(@SpringQueryMap RecentRceptListRequest request);

	@GetMapping(
		value = Operation.GET_RECENT_PASAGE_LIST,
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_XML_VALUE)
	RecentPasageListResponse getRecentPasageList(@SpringQueryMap RecentPasageListRequest request);

	@GetMapping(
		value = Operation.GET_JSICTION_COMITE_PROCESS_LIST,
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_XML_VALUE)
	JsictionComiteProcessListResponse getJsictionComiteProcessList(
		@SpringQueryMap JsictionComiteProcessListRequest request);

	@GetMapping(
		value = Operation.GET_RECENT_MOOR_LIST,
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_XML_VALUE)
	RecentMoorListResponse getRecentMoorList(@SpringQueryMap RecentMoorListRequest request);

	@GetMapping(
		value = Operation.GET_SESSION_REQUEST_LIST,
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_XML_VALUE)
	SessionRequestListResponse getSessionRequestList(@SpringQueryMap SessionRequestListRequest request);

	@GetMapping(
		value = Operation.GET_BILL_RECEIPT_INFO,
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_XML_VALUE)
	BillReceiptInfoResponse getBillReceiptInfo(@SpringQueryMap BillReceiptInfoRequest request);

	@GetMapping(
		value = Operation.GET_BILL_PETITION_MEMBER_LIST,
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_XML_VALUE)
	BillPetitionMemberListResponse getBillPetitionMemberList(@SpringQueryMap BillPetitionMemberListRequest request);

	@GetMapping(
		value = Operation.GET_BILL_PROMULGATION_INFO,
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_XML_VALUE)
	BillPromulgationInfoResponse getBillPromulgationInfo(@SpringQueryMap BillPromulgationInfoRequest request);

	@GetMapping(
		value = Operation.GET_BILL_ADDITIONAL_INFO,
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_XML_VALUE)
	BillAdditionalInfoResponse getBillAdditionalInfo(@SpringQueryMap BillAdditionalInfoRequest request);

	@GetMapping(
		value = Operation.GET_BILL_COMMISSION_EXAMINATION_INFO,
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_XML_VALUE)
	BillCommissionExaminationInfoResponse getBillCommissionExaminationInfo(
		@SpringQueryMap BillCommissionExaminationInfoRequest request);

	@GetMapping(
		value = Operation.GET_COMMIT_PETITION_LIST,
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_XML_VALUE)
	CommitPetitionListResponse getCommitPetitionList(@SpringQueryMap CommitPetitionListRequest request);

	@GetMapping(
		value = Operation.GET_MOTION_LAW_LIST,
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_XML_VALUE)
	MotionLawListResponse getMotionLawList(@SpringQueryMap MotionLawListRequest request);
}

