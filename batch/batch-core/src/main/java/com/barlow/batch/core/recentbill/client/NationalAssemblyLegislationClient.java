package com.barlow.batch.core.recentbill.client;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.barlow.client.knal.api.NationalAssemblyLegislationApi;
import com.barlow.client.knal.api.request.BillInfoListRequest;
import com.barlow.client.knal.api.request.BillPetitionMemberListRequest;
import com.barlow.client.knal.api.response.BillInfoListResponse;
import com.barlow.client.knal.api.response.BillPetitionMemberListResponse;

@Component
public class NationalAssemblyLegislationClient {

	private final NationalAssemblyLegislationApi api;

	public NationalAssemblyLegislationClient(NationalAssemblyLegislationApi api) {
		this.api = api;
	}

	public TodayBillInfoResult getTodayBillInfo(
		LocalDate batchDate,
		@Value("${start-ordinal:22}") Integer startOrdinal,
		@Value("${end-ordinal:22}") Integer endOrdinal,
		@Value("${num-of-rows:100}") Integer numOfRows
	) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String batchDateStr = batchDate.format(formatter);
		BillInfoListRequest request = BillInfoListRequest.builder()
			.startProposeDate(batchDateStr)
			.endProposeDate(batchDateStr)
			.startOrdinal(startOrdinal)
			.endOrdinal(endOrdinal)
			.numOfRows(numOfRows)
			.pageNo(1)
			.build();
		BillInfoListResponse response = api.getBillInfoList(request);
		return TodayBillInfoResult.from(response.getBody());
	}

	public BillProposerInfoResult getBillProposerInfo(String billId) {
		BillPetitionMemberListRequest request = BillPetitionMemberListRequest.builder()
			.billId(billId)
			.gbn1("bill")
			.gbn2("reception")
			.build();
		BillPetitionMemberListResponse response = api.getBillPetitionMemberList(request);
		return BillProposerInfoResult.from(response.getBody());
	}
}
