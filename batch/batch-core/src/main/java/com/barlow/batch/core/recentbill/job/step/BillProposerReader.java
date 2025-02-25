package com.barlow.batch.core.recentbill.job.step;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import com.barlow.batch.core.recentbill.LawmakerProvider;
import com.barlow.batch.core.recentbill.job.TodayBillRetrieveClient;
import com.barlow.batch.core.recentbill.job.TodayBillInfoResult;
import com.barlow.batch.core.recentbill.job.AbstractExecutionContextSharingManager;
import com.barlow.batch.core.recentbill.job.RecentBillJobScopeShareRepository;

@Component
@StepScope
public class BillProposerReader
	extends AbstractExecutionContextSharingManager
	implements ItemReader<BillProposer>, ItemStream {

	private static final String BILL_PROPOSER_READER_INDEX_KEY = "BillProposerReader.currentIndex";

	private final RecentBillJobScopeShareRepository jobScopeShareRepository;
	private final TodayBillRetrieveClient client;
	private final LawmakerProvider lawmakerProvider;
	private int currentIndex = 0;

	public BillProposerReader(
		RecentBillJobScopeShareRepository jobScopeShareRepository,
		TodayBillRetrieveClient client,
		LawmakerProvider lawmakerProvider
	) {
		super();
		this.jobScopeShareRepository = jobScopeShareRepository;
		this.lawmakerProvider = lawmakerProvider;
		this.client = client;
	}

	@Override
	public void open(@NotNull ExecutionContext executionContext) throws ItemStreamException {
		super.setCurrentExecutionContext(executionContext);
		if (executionContext.containsKey(BILL_PROPOSER_READER_INDEX_KEY)
			&& executionContext.containsKey(RECEIVED_BILL_WITH_FEW_PROPOSERS_JOB_KEY)) {
			currentIndex = executionContext.getInt(BILL_PROPOSER_READER_INDEX_KEY);
		} else {
			currentIndex = 0; // 처음부터 시작
		}
	}

	@Override
	public BillProposer read() throws UnexpectedInputException, ParseException, NonTransientResourceException {
		String hashKey = super.getDataFromJobExecutionContext(RECEIVED_BILL_WITH_FEW_PROPOSERS_JOB_KEY);
		TodayBillInfoResult receiveBillWithFewProposers = jobScopeShareRepository.findByKey(hashKey);
		if (currentIndex >= receiveBillWithFewProposers.itemSize()) {
			return null;
		}
		String billId = receiveBillWithFewProposers.items().get(currentIndex).billId();
		List<LawmakerProvider.Lawmaker> billProposeLawmakers = client.getBillProposerInfo(billId)
			.billProposerInfos()
			.stream()
			.map(info -> lawmakerProvider.provide(info.name(), info.partyName()))
			.toList();
		currentIndex++;
		return new BillProposer(billId, billProposeLawmakers);
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		executionContext.putInt(BILL_PROPOSER_READER_INDEX_KEY, currentIndex);
	}
}
