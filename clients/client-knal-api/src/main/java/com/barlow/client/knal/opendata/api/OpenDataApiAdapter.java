package com.barlow.client.knal.opendata.api;

import org.springframework.stereotype.Component;

import com.barlow.client.knal.opendata.api.request.BillInfoListRequest;
import com.barlow.client.knal.opendata.api.request.BillPetitionMemberListRequest;
import com.barlow.client.knal.opendata.api.request.BillPreliminaryExaminationInfoRequest;
import com.barlow.client.knal.opendata.api.response.BillInfoListResponse;
import com.barlow.client.knal.opendata.api.response.BillPetitionMemberListResponse;
import com.barlow.client.knal.opendata.api.response.BillPreliminaryExaminationInfoResponse;

@Component
public class OpenDataApiAdapter implements OpenDataApiPort {

	private final NationalAssemblyLegislationOpenDataApi api;

	public OpenDataApiAdapter(NationalAssemblyLegislationOpenDataApi api) {
		this.api = api;
	}

	@Override
	public BillInfoListResponse getBillInfoList(BillInfoListRequest request) throws OpenDataException {
		return api.getBillInfoList(request);
	}

	@Override
	public BillPetitionMemberListResponse getBillPetitionMemberList(BillPetitionMemberListRequest request) throws
		OpenDataException {
		return api.getBillPetitionMemberList(request);
	}

	@Override
	public BillPreliminaryExaminationInfoResponse getBillPreliminaryExaminationInfo(
		BillPreliminaryExaminationInfoRequest request) throws OpenDataException {
		return api.getBillPreliminaryExaminationInfo(request);
	}
}
