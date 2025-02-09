package com.barlow.batch.core.recentbill.client;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.barlow.client.knal.api.NationalAssemblyLegislationApi;
import com.barlow.client.knal.api.request.BillInfoListRequest;
import com.barlow.client.knal.api.request.BillPetitionMemberListRequest;
import com.barlow.client.knal.api.response.BillInfoListResponse;
import com.barlow.client.knal.api.response.BillPetitionMemberListResponse;

@Component
public class NationalAssemblyLegislationClient {

	private static final Logger log = LoggerFactory.getLogger(NationalAssemblyLegislationClient.class);

	private final NationalAssemblyLegislationApi api;

	public NationalAssemblyLegislationClient(NationalAssemblyLegislationApi api) {
		this.api = api;
	}

	public TodayBillInfoResult getTodayBillInfo(
		LocalDate batchDate,
		Integer startOrdinal,
		Integer endOrdinal,
		Integer numOfRows
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
		log.info("{} : 오늘 접수된 법안 호출", LocalDateTime.now());
		BillInfoListResponse response = api.getBillInfoList(request);
		log.info("{} : 오늘 접수된 법안 조회 완료", LocalDateTime.now());
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
