package com.barlow.batch.core.recentbill.client;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.barlow.batch.core.recentbill.job.BillProposerInfoBatchEntity;
import com.barlow.batch.core.recentbill.job.TodayBillRetrieveClient;
import com.barlow.batch.core.recentbill.job.TodayBillInfoBatchEntity;
import com.barlow.client.knal.opendata.api.NationalAssemblyLegislationOpenDataApi;
import com.barlow.client.knal.opendata.api.request.BillInfoListRequest;
import com.barlow.client.knal.opendata.api.request.BillPetitionMemberListRequest;
import com.barlow.client.knal.opendata.api.response.BillInfoListResponse;
import com.barlow.client.knal.opendata.api.response.BillPetitionMemberListResponse;

@Component
public class TodayBillRetrieveClientAdapter implements TodayBillRetrieveClient {

	private static final Logger log = LoggerFactory.getLogger(TodayBillRetrieveClientAdapter.class);

	private final NationalAssemblyLegislationOpenDataApi api;
	private final Integer startOrd;
	private final Integer endOrd;
	private final Integer numOfRows;

	public TodayBillRetrieveClientAdapter(
		NationalAssemblyLegislationOpenDataApi api,
		@Value("${start-ordinal:22}") Integer startOrd,
		@Value("${end-ordinal:22}") Integer endOrd,
		@Value("${num-of-rows:100}") Integer numOfRows
	) {
		this.api = api;
		this.startOrd = startOrd;
		this.endOrd = endOrd;
		this.numOfRows = numOfRows;
	}

	@Override
	public TodayBillInfoBatchEntity getTodayBillInfo(LocalDate batchDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String batchDateStr = batchDate.format(formatter);
		BillInfoListRequest request = BillInfoListRequest.builder()
			.startProposeDate(batchDateStr)
			.endProposeDate(batchDateStr)
			.startOrdinal(startOrd)
			.endOrdinal(endOrd)
			.numOfRows(numOfRows)
			.pageNo(1)
			.build();
		log.info("{} : 오늘 접수된 법안 호출", LocalDateTime.now());
		BillInfoListResponse response = api.getBillInfoList(request);
		log.info("{} : 오늘 접수된 법안 조회 완료", LocalDateTime.now());
		return new TodayBillInfoBatchEntity(
			response.getBody().getTotalCount(),
			response.getBody().getItems().stream()
				.map(TodayBillInfoBatchEntityFactory::make)
				.filter(Objects::nonNull)
				.toList()
		);
	}

	@Override
	public BillProposerInfoBatchEntity getBillProposerInfo(String billId) {
		BillPetitionMemberListRequest request = BillPetitionMemberListRequest.builder()
			.billId(billId)
			.gbn1("bill")
			.gbn2("reception")
			.build();
		log.info("{} 법안 발의자 조회", billId);
		BillPetitionMemberListResponse response = api.getBillPetitionMemberList(request);
		log.info("{} 법안 발의자 조회 완료", billId);
		return BillProposerInfoBatchEntity.from(response.getBody());
	}
}
