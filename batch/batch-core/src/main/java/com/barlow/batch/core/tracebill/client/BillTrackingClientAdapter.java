package com.barlow.batch.core.tracebill.client;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.barlow.batch.core.tracebill.job.NationalAssemblyLegislationClient;
import com.barlow.batch.core.tracebill.job.CurrentBillInfoResult;
import com.barlow.client.knal.api.NationalAssemblyLegislationApi;
import com.barlow.client.knal.api.request.BillInfoListRequest;
import com.barlow.client.knal.api.request.BillPreliminaryExaminationInfoRequest;
import com.barlow.client.knal.api.response.BillInfoListResponse;
import com.barlow.client.knal.api.response.BillPreliminaryExaminationInfoResponse;
import com.barlow.client.knal.api.response.item.BillInfoListItem;
import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.enumerate.ProgressStatus;

@Component
public class NationalAssemblyLegislationClientAdapter implements NationalAssemblyLegislationClient {

	private static final Logger log = LoggerFactory.getLogger(NationalAssemblyLegislationClientAdapter.class);

	private final NationalAssemblyLegislationApi api;
	private final Integer startOrd;
	private final Integer endOrd;
	private final Integer numOfRows;

	public NationalAssemblyLegislationClientAdapter(
		NationalAssemblyLegislationApi api,
		@Value("${start-ordinal:22}") Integer startOrd,
		@Value("${end-ordinal:22}") Integer endOrd,
		@Value("${num-of-rows:10000}") Integer numOfRows
	) {
		this.api = api;
		this.startOrd = startOrd;
		this.endOrd = endOrd;
		this.numOfRows = numOfRows;
	}

	@Override
	public CurrentBillInfoResult getTraceBillInfo(LocalDate startProposeDate, LocalDate batchDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String startProposeDateStr = startProposeDate.format(formatter);
		String traceDateStr = batchDate.minusDays(1).format(formatter);
		BillInfoListRequest request = BillInfoListRequest.builder()
			.startProposeDate(startProposeDateStr)
			.endProposeDate(traceDateStr)
			.startOrdinal(startOrd)
			.endOrdinal(endOrd)
			.numOfRows(numOfRows)
			.pageNo(1)
			.build();
		log.info("상태 추적을 위해 {} - {} 까지의 법안 조회 호출", startProposeDate, traceDateStr);
		BillInfoListResponse response = api.getBillInfoList(request);
		log.info("상태 추적을 위해 {} - {} 까지의 법안 조회 완료", startProposeDate, traceDateStr);
		return new CurrentBillInfoResult(
			response.getBody().getItems()
				.stream()
				.collect(Collectors.toMap(
					BillInfoListItem::billId,
					listItem -> ProgressStatus.findByValue(listItem.procStageCd())
				))
		);
	}

	@Override
	public LegislationType getCommittee(String billId) {
		BillPreliminaryExaminationInfoRequest request = BillPreliminaryExaminationInfoRequest.builder()
			.billId(billId)
			.build();
		log.info("{}의 소관위원회 조회 호출", billId);
		BillPreliminaryExaminationInfoResponse response = api.getBillPreliminaryExaminationInfo(request);
		log.info("{}의 소관위원회 조회 완료", billId);
		String committeeName = response.body().getItems().getFirst().comitName();
		return LegislationType.findByValue(committeeName);
	}

}
