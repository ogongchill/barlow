package com.barlow.batch.admin.bill.job.step;

import static com.barlow.batch.admin.bill.BillConstant.BILL_PROPOSER_READER_INDEX_KEY;
import static com.barlow.batch.admin.bill.BillConstant.BILL_WITH_FEW_PROPOSERS_SHARE_KEY;

import java.util.List;
import java.util.Objects;

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

import com.barlow.batch.admin.common.AdminAbstractExecutionContextSharingManager;
import com.barlow.batch.admin.bill.LawmakerProvider;
import com.barlow.batch.admin.bill.job.BillJobScopeShareRepository;
import com.barlow.batch.admin.bill.job.BillInfoBatchEntity;
import com.barlow.batch.admin.bill.job.BillRetrieveClient;
import com.barlow.infra.knal.opendata.api.OpenDataException;

@Component
@StepScope
public class AdminBillProposerReader
	extends AdminAbstractExecutionContextSharingManager
	implements ItemReader<BillProposer>, ItemStream {

	private final BillJobScopeShareRepository jobScopeShareRepository;
	private final BillRetrieveClient client;
	private final LawmakerProvider lawmakerProvider;

	private BillInfoBatchEntity billWithFewProposers;
	private int currentIndex = 0;

	public AdminBillProposerReader(
		BillJobScopeShareRepository jobScopeShareRepository,
		BillRetrieveClient client,
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
		if (executionContext.containsKey(BILL_PROPOSER_READER_INDEX_KEY) &&
			executionContext.containsKey(BILL_WITH_FEW_PROPOSERS_SHARE_KEY)
		) {
			currentIndex = executionContext.getInt(BILL_PROPOSER_READER_INDEX_KEY);
			String hashKey = super.getDataFromJobExecutionContext(BILL_WITH_FEW_PROPOSERS_SHARE_KEY);
			billWithFewProposers = jobScopeShareRepository.findByKey(hashKey);
		} else {
			currentIndex = 0; // 처음부터 시작
			String hashKey = super.getDataFromJobExecutionContext(BILL_WITH_FEW_PROPOSERS_SHARE_KEY);
			billWithFewProposers = jobScopeShareRepository.findByKey(hashKey);
		}
	}

	@Override
	public BillProposer read() throws OpenDataException, UnexpectedInputException, ParseException, NonTransientResourceException {
		if (currentIndex >= billWithFewProposers.itemSize()) {
			return null;
		}
		String billId = billWithFewProposers.items().get(currentIndex).billId();
		List<LawmakerProvider.Lawmaker> billProposeLawmakers = client.getBillProposerInfo(billId)
			.billProposerInfos()
			.parallelStream()
			.map(info -> lawmakerProvider.provide(info.name(), info.partyName()))
			.filter(Objects::nonNull)
			.toList();
		currentIndex++;
		return new BillProposer(billId, billProposeLawmakers);
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		executionContext.putInt(BILL_PROPOSER_READER_INDEX_KEY, currentIndex);
	}
}
