package com.barlow.batch.core.recentbill.job;

import java.time.LocalDate;
import java.util.List;

import com.barlow.core.enumerate.ProgressStatus;
import com.barlow.core.enumerate.ProposerType;

public class RecentBillFixture {

	static final LocalDate STANDARD_DATE =  LocalDate.of(2025, 2, 5);
	static final TodayBillInfoBatchEntity TODAY_BILL_INFO_RESULT = new TodayBillInfoBatchEntity(
		17,
		List.of(
			new TodayBillInfoBatchEntity.BillInfoItem("PRC_D2B5C0A1A1W4V1V5T0U2S4T1B0Z0A5", "2207919", "조세특례제한법 일부개정법률안", null, "계류의안", ProposerType.LAWMAKER, "김용만의원 등 11인", "2025-02-05", null, ProgressStatus.RECEIVED, null),
			new TodayBillInfoBatchEntity.BillInfoItem("PRC_K2L4J0K6I1Q1R1P6P0O3P0N1V2V1U7", "2207918", "공동주택관리법 일부개정법률안", null, "계류의안", ProposerType.LAWMAKER, "신영대의원 등 16인", "2025-02-05", null, ProgressStatus.RECEIVED, null),
			new TodayBillInfoBatchEntity.BillInfoItem("PRC_P2N5O0N2N0L4M1H0G3H9F3F2E5F4M7", "2207917", "형법 일부개정법률안", null, "계류의안", ProposerType.LAWMAKER, "서영교의원 등 10인", "2025-02-05", null, ProgressStatus.RECEIVED, null),
			new TodayBillInfoBatchEntity.BillInfoItem("PRC_P2N5L0M2K0L3T0T9R5S9R1P2Q5L5M8", "2207916", "국회의장 및 국회 경호에 관한 법률안", null, "계류의안", ProposerType.LAWMAKER, "이정문의원 등 12인", "2025-02-05", null, ProgressStatus.RECEIVED, null),
			new TodayBillInfoBatchEntity.BillInfoItem("PRC_F2E4C1L0L2K9J1H1Q1O0P5O0M7U0T1", "2207915", "지방세법 일부개정법률안", null, "계류의안", ProposerType.LAWMAKER, "김정재의원 등 11인", "2025-02-05", null, ProgressStatus.RECEIVED, null),
			new TodayBillInfoBatchEntity.BillInfoItem("PRC_R2R5W0W1V2T0U0S9T4P7P1N7O5M5L3", "2207914", "전기사업법 일부개정법률안", null, "계류의안", ProposerType.LAWMAKER, "김기현의원 등 10인", "2025-02-05", null, ProgressStatus.RECEIVED, null),
			new TodayBillInfoBatchEntity.BillInfoItem("PRC_M2K5L0T2R0S5Q0R9P5Q4L4M1K3J9J4", "2207913", "보건의료기본법 일부개정법률안", null, "계류의안", ProposerType.LAWMAKER, "안상훈의원 등 11인", "2025-02-05", null, ProgressStatus.RECEIVED, null),
			new TodayBillInfoBatchEntity.BillInfoItem("PRC_W2V5V0D1E2C3D1B7Z2A6I1J5H6H1G8", "2207912", "화학물질의 등록", null, "계류의안", ProposerType.LAWMAKER, "우재준의원 등 13인", "2025-02-05", null, ProgressStatus.RECEIVED, null),
			new TodayBillInfoBatchEntity.BillInfoItem("PRC_A2Y5Z0H2H0F3G1F5F4D4Z0A7Y7Z2X3", "2207911", "국회법 일부개정법률안", null, "계류의안", ProposerType.LAWMAKER, "신장식의원 등 13인", "2025-02-05", null, ProgressStatus.RECEIVED, null),
			new TodayBillInfoBatchEntity.BillInfoItem("PRC_L2L4K0L8J1J4R1Q6Q1O3Q4O0O3K4I1", "2207910", "아동복지법 일부개정법률안", null, "계류의안", ProposerType.LAWMAKER, "서명옥의원 등 10인", "2025-02-05", null, ProgressStatus.RECEIVED, null),
			new TodayBillInfoBatchEntity.BillInfoItem("PRC_H2I5H0O1P2N3O1M5N1L0T5U2S9T3R6", "2207909", "조세특례제한법 일부개정법률안", null, "계류의안", ProposerType.LAWMAKER, "이인선의원 등 10인", "2025-02-05", null, ProgressStatus.RECEIVED, null),
			new TodayBillInfoBatchEntity.BillInfoItem("PRC_A2Y5Z0X1F2G4E1E4D1E2C0C4Y5X3X7", "2207908", "공탁법 일부개정법률안", null, "계류의안", ProposerType.LAWMAKER, "이인선의원 등 10인", "2025-02-05", null, ProgressStatus.RECEIVED, null),
			new TodayBillInfoBatchEntity.BillInfoItem("PRC_A2Y5Z0X1Y1G7E1F7D1D7C2D2K5J7K7", "2207907", "중소기업협동조합법 일부개정법률안", null, "계류의안", ProposerType.LAWMAKER, "이종배의원 등 14인", "2025-02-05", null, ProgressStatus.RECEIVED, null),
			new TodayBillInfoBatchEntity.BillInfoItem("PRC_X2V4V0U9U2S3T1B5Z5A7Y2Z3X6Y3T3", "2207906", "중증장애인생산품 우선구매", null, "계류의안", ProposerType.LAWMAKER, "서명옥의원 등 10인", "2025-02-05", null, ProgressStatus.RECEIVED, null),
			new TodayBillInfoBatchEntity.BillInfoItem("PRC_Q2Q5O0L1L1J6K0I9J3H0I4Q3O7P4N9", "2207905", "공직선거법 일부개정법률안", null, "계류의안", ProposerType.LAWMAKER, "이달희의원 등 12인", "2025-02-05", null, ProgressStatus.RECEIVED, null),
			new TodayBillInfoBatchEntity.BillInfoItem("PRC_A2W5X0V2W0U3T1T7B4C9A3B0Z3X1Y4", "2207904", "동물보호법 일부개정법률안", null, "계류의안", ProposerType.LAWMAKER, "이기헌의원 등 12인", "2025-02-05", null, ProgressStatus.RECEIVED, null),
			new TodayBillInfoBatchEntity.BillInfoItem("PRC_N2N5M0M2K0L4K1R2S4Q6R0P0Q3O9K0", "2207903", "조세특례제한법 일부개정법률안", null, "계류의안", ProposerType.LAWMAKER, "임오경의원 등 19인", "2025-02-05", null, ProgressStatus.RECEIVED, null)
		)
	);
}
