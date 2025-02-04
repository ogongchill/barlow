package com.barlow.batch.core.recentbill.job.step;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.barlow.batch.core.recentbill.LawmakerProvider;
import com.barlow.batch.core.recentbill.client.NationalAssemblyLegislationClient;
import com.barlow.batch.core.recentbill.client.TodayBillInfoResult;

@Component
@StepScope
public class BillProposerReader implements ItemReader<BillProposer>, ItemStream {

	private static final String CURRENT_INDEX_KEY = "BillProposerReader.currentIndex";

	private final NationalAssemblyLegislationClient client;
	private final TodayBillInfoResult todayBillInfo;
	private final LawmakerProvider lawmakerProvider;
	private int currentIndex = 0;

	public BillProposerReader(
		NationalAssemblyLegislationClient client,
		@Value("#{jobParameters['todayBillInfo']}") TodayBillInfoResult todayBillInfo,
		LawmakerProvider lawmakerProvider
	) {
		this.client = client;
		this.todayBillInfo = todayBillInfo;
		this.lawmakerProvider = lawmakerProvider;
	}

	@Override
	public BillProposer read() throws UnexpectedInputException, ParseException, NonTransientResourceException {
		if (currentIndex >= todayBillInfo.totalCount()) {
			return null;
		}
		String billId = todayBillInfo.items().get(currentIndex).billId();
		LawmakerProvider billProposeLawmakers = client.getBillProposerInfo(billId)
			.billProposerInfos()
			.stream()
			.map(info -> lawmakerProvider.provide(info.name(), info.partyName()))
			.findFirst()
			.orElseThrow();
		currentIndex++;
		return new BillProposer(billId, billProposeLawmakers.lawmakers());
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		if (executionContext.containsKey(CURRENT_INDEX_KEY)) {
			currentIndex = executionContext.getInt(CURRENT_INDEX_KEY);
		} else {
			currentIndex = 0; // 처음부터 시작
		}
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		executionContext.putInt(CURRENT_INDEX_KEY, currentIndex);
	}
}
