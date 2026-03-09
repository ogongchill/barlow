package com.barlow.infra.knal.opendata.api;

import com.barlow.infra.knal.opendata.api.request.BillInfoListRequest;
import com.barlow.infra.knal.opendata.api.request.BillPetitionMemberListRequest;
import com.barlow.infra.knal.opendata.api.request.BillPreliminaryExaminationInfoRequest;
import com.barlow.infra.knal.opendata.api.response.BillInfoListResponse;
import com.barlow.infra.knal.opendata.api.response.BillPetitionMemberListResponse;
import com.barlow.infra.knal.opendata.api.response.BillPreliminaryExaminationInfoResponse;

public interface OpenDataApiPort {

	BillInfoListResponse getBillInfoList(BillInfoListRequest request) throws OpenDataException;

	BillPetitionMemberListResponse getBillPetitionMemberList(BillPetitionMemberListRequest request)
		throws OpenDataException;

	BillPreliminaryExaminationInfoResponse getBillPreliminaryExaminationInfo(
		BillPreliminaryExaminationInfoRequest request) throws OpenDataException;
}
