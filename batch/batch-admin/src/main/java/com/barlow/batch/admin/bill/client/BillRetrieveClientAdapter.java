package com.barlow.batch.admin.bill.client;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.barlow.batch.admin.bill.job.BillInfoBatchEntity;
import com.barlow.batch.admin.bill.job.BillProposerInfoResult;
import com.barlow.batch.admin.bill.job.BillRetrieveClient;
import com.barlow.infra.knal.opendata.api.OpenDataApiPort;
import com.barlow.infra.knal.opendata.api.request.BillInfoListRequest;
import com.barlow.infra.knal.opendata.api.request.BillPetitionMemberListRequest;
import com.barlow.infra.knal.opendata.api.request.BillPreliminaryExaminationInfoRequest;
import com.barlow.infra.knal.opendata.api.response.BillInfoListResponse;
import com.barlow.infra.knal.opendata.api.response.BillPetitionMemberListResponse;
import com.barlow.infra.knal.opendata.api.response.BillPreliminaryExaminationInfoResponse;
import com.barlow.infra.knal.opendata.api.response.item.BillPreliminaryExaminationInfoItem;
import com.barlow.core.enumerate.LegislationType;

@Component
public class BillRetrieveClientAdapter implements BillRetrieveClient {

	private final OpenDataApiPort api;
	private final Integer startOrd;
	private final Integer endOrd;
	private final Integer numOfRows;

	public BillRetrieveClientAdapter(
		OpenDataApiPort api,
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
	public BillInfoBatchEntity getAllBillInfo(LocalDate startDate, LocalDate endDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		BillInfoListRequest request = BillInfoListRequest.builder()
			.startProposeDate(startDate.format(formatter))
			.endProposeDate(endDate.format(formatter))
			.startOrdinal(startOrd)
			.endOrdinal(endOrd)
			.numOfRows(numOfRows)
			.pageNo(1)
			.build();
		BillInfoListResponse response = api.getBillInfoList(request);
		return new BillInfoBatchEntity(
			response.body().totalCount(),
			response.body().items().stream()
				.map(BillInfoBatchEntityFactory::make)
				.filter(Objects::nonNull)
				.toList()
		);
	}

	@Override
	public BillProposerInfoResult getBillProposerInfo(String billId) {
		BillPetitionMemberListRequest request = BillPetitionMemberListRequest.builder()
			.billId(billId)
			.gbn1("bill")
			.gbn2("reception")
			.build();
		BillPetitionMemberListResponse response = api.getBillPetitionMemberList(request);
		return BillProposerInfoResult.from(response.body());
	}

	@Override
	public LegislationType getCommittee(String billId) {
		BillPreliminaryExaminationInfoRequest request = BillPreliminaryExaminationInfoRequest.builder()
			.billId(billId)
			.build();
		BillPreliminaryExaminationInfoResponse response = api.getBillPreliminaryExaminationInfo(request);
		Optional<BillPreliminaryExaminationInfoItem> first = response.body()
			.items()
			.stream()
			.filter(Objects::nonNull)
			.findFirst();
		if (first.isPresent()) {
			return LegislationType.findByValue(first.get().comitName());
		} else {
			return LegislationType.EMPTY;
		}
	}
}
