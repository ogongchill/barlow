package com.barlow.batch.admin.bill.job.step;

import static com.barlow.batch.admin.bill.BillConstant.BILL_LEGISLATION_READER_INDEX_KEY;
import static com.barlow.batch.admin.bill.BillConstant.BILL_WITH_LEGISLATION_BODY_SHARE_KEY;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.stereotype.Component;

import com.barlow.batch.admin.bill.job.BillInfoBatchEntity;
import com.barlow.batch.admin.bill.job.BillJobScopeShareRepository;
import com.barlow.batch.admin.bill.job.BillRetrieveClient;
import com.barlow.batch.admin.common.AdminAbstractExecutionContextSharingManager;
import com.barlow.infra.knal.opendata.api.OpenDataException;
import com.barlow.core.enumerate.LegislationType;

@Component
@StepScope
public class BillLegislationReader extends AdminAbstractExecutionContextSharingManager
	implements ItemReader<BillLegislationBody>, ItemStream {

	private final BillJobScopeShareRepository jobScopeShareRepository;
	private final BillRetrieveClient client;

	private int currentIndex = 0;
	private BillInfoBatchEntity billsWithLegislationBody;

	public BillLegislationReader(BillJobScopeShareRepository jobScopeShareRepository, BillRetrieveClient client) {
		this.jobScopeShareRepository = jobScopeShareRepository;
		this.client = client;
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		super.setCurrentExecutionContext(executionContext);
		if (executionContext.containsKey(BILL_LEGISLATION_READER_INDEX_KEY) &&
			executionContext.containsKey(BILL_WITH_LEGISLATION_BODY_SHARE_KEY)
		) {
			currentIndex = executionContext.getInt(BILL_LEGISLATION_READER_INDEX_KEY);
			String hashKey = super.getDataFromJobExecutionContext(BILL_WITH_LEGISLATION_BODY_SHARE_KEY);
			billsWithLegislationBody = jobScopeShareRepository.findByKey(hashKey);
		} else {
			currentIndex = 0; // 처음부터 시작
			String hashKey = super.getDataFromJobExecutionContext(BILL_WITH_LEGISLATION_BODY_SHARE_KEY);
			billsWithLegislationBody = jobScopeShareRepository.findByKey(hashKey);
		}
	}

	@Override
	public BillLegislationBody read() throws OpenDataException {
		if (currentIndex >= billsWithLegislationBody.itemSize()) {
			return null;
		}
		String billId = billsWithLegislationBody.items().get(currentIndex).billId();
		LegislationType committee = client.getCommittee(billId);
		currentIndex++;
		return new BillLegislationBody(billId, committee);
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		executionContext.putInt(BILL_LEGISLATION_READER_INDEX_KEY, currentIndex);
	}
}
