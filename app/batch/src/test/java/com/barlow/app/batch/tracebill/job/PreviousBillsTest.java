package com.barlow.app.batch.tracebill.job;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.enumerate.ProgressStatus;

class PreviousBillsTest {

	@Test
	@DisplayName("getStatusByBillId가 null을 반환하는 billId가 있으면 NPE 없이 정상 반환되고 해당 billId는 UpdatedBills에 포함되지 않는다.")
	void dirtyCheck_CurrentStatusIsNull_ReturnsWithoutNpeAndExcludesBillId() {
		// given
		PreviousBillBatchEntity previous = new PreviousBillBatchEntity(
			"BILL-001", "테스트 법안", ProgressStatus.RECEIVED, LegislationType.EMPTY);
		PreviousBills previousBills = new PreviousBills(List.of(previous));

		CurrentBillInfoResult current = new CurrentBillInfoResult(Map.of());

		// when
		UpdatedBills result = previousBills.dirtyCheck(current);

		// then — NPE 없이 정상 반환
		assertThat(result).isNotNull();

		// then — null 반환된 billId는 결과에 미포함
		assertThat(result.isEmpty()).isTrue();
	}

	@Test
	@DisplayName("getStatusByBillId가 이전 상태와 다른 값을 반환하면 해당 billId가 UpdatedBills에 포함된다.")
	void dirtyCheck_CurrentStatusDiffersFromPrevious_IncludesBillIdInUpdatedBills() {
		// given
		PreviousBillBatchEntity previous = new PreviousBillBatchEntity(
			"BILL-002", "상태변경 법안", ProgressStatus.RECEIVED, LegislationType.EMPTY);
		PreviousBills previousBills = new PreviousBills(List.of(previous));

		CurrentBillInfoResult current = new CurrentBillInfoResult(
			Map.of("BILL-002", ProgressStatus.COMMITTEE_RECEIVED));

		// when
		UpdatedBills result = previousBills.dirtyCheck(current);

		// then — 상태가 변경된 billId가 결과에 포함됨
		assertThat(result.isEmpty()).isFalse();
		List<UpdatedBills.BillInfo> committeeReceived = result.getCommitteeReceived();
		assertThat(committeeReceived).hasSize(1);
		assertThat(committeeReceived.get(0).billId()).isEqualTo("BILL-002");
	}

	@Test
	@DisplayName("getStatusByBillId가 이전 상태와 동일한 값을 반환하면 해당 billId는 UpdatedBills에 포함되지 않는다.")
	void dirtyCheck_CurrentStatusSameAsPrevious_ExcludesBillIdFromUpdatedBills() {
		// given
		PreviousBillBatchEntity previous = new PreviousBillBatchEntity(
			"BILL-003", "상태동일 법안", ProgressStatus.RECEIVED, LegislationType.EMPTY);
		PreviousBills previousBills = new PreviousBills(List.of(previous));

		CurrentBillInfoResult current = new CurrentBillInfoResult(
			Map.of("BILL-003", ProgressStatus.RECEIVED));

		// when
		UpdatedBills result = previousBills.dirtyCheck(current);

		// then — 상태가 동일한 billId는 결과에 미포함
		assertThat(result.isEmpty()).isTrue();
	}

	@Test
	@DisplayName("모든 billId에 대해 getStatusByBillId가 null을 반환하면 빈 UpdatedBills가 NPE 없이 반환된다.")
	void dirtyCheck_AllCurrentStatusesAreNull_ReturnsEmptyUpdatedBillsWithoutNpe() {
		// given
		List<PreviousBillBatchEntity> previousList = List.of(
			new PreviousBillBatchEntity("BILL-010", "법안A", ProgressStatus.RECEIVED, LegislationType.EMPTY),
			new PreviousBillBatchEntity("BILL-011", "법안B", ProgressStatus.COMMITTEE_REVIEW, LegislationType.EDUCATION),
			new PreviousBillBatchEntity("BILL-012", "법안C", ProgressStatus.PLENARY_SUBMITTED, LegislationType.EMPTY));
		PreviousBills previousBills = new PreviousBills(previousList);

		CurrentBillInfoResult current = new CurrentBillInfoResult(Map.of());

		// when
		UpdatedBills result = previousBills.dirtyCheck(current);

		// then — NPE 없이 빈 UpdatedBills 반환
		assertThat(result).isNotNull();
		assertThat(result.isEmpty()).isTrue();
	}
}
