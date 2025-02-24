package com.barlow.client.knal.api;

/**
 * <a href="https://www.data.go.kr/data/3037286/openapi.do">IROS4_OA_DV_0401_OpenAPI활용가이드_의안정보(국회사무처)_v1.30<</a></href>에 따른 오퍼레이션명
 */
public final class Operation {

	static final String GET_BILL_INFO_LIST = "getBillInfoList"; // "의안 목록 검색"
	static final String GET_RECENT_RCEPT_LIST = "getRecentRceptList"; // "최근 접수의안 목록 정보조회"
	static final String GET_RECENT_PASAGE_LIST = "getRecentPasageList"; // "최근 통과의안 목록 정보조회"
	static final String GET_JSICTION_COMITE_PROCESS_LIST = "getJsictionComiteProcessList"; // "처리의안 목록 정보 조회"
	static final String GET_RECENT_MOOR_LIST = "getRecentMoorList"; // "계류의안 목록 정보조회"
	static final String GET_SESSION_REQUEST_LIST = "getSessionRequestList"; // "본회의부의요청안건 목록 정보조회"
	static final String GET_BILL_RECEIPT_INFO = "getBillReceiptInfo"; // "의안 접수 정보조회"
	static final String GET_BILL_PETITION_MEMBER_LIST = "getBillPetitionMemberList"; // "의안/청원 접수정보 국회의원 목록조회"
	static final String GET_BILL_PRELIMINARY_EXAMINATION_INFO = "getBillPreLiminaryExaminationInfo"; // "의안 예비 심사 정보 조회"
	static final String GET_BILL_COMPREHENSIVE_AUDIT_INFO = "getBillComprehensiveAuditInfo"; // "의안 종합심사 정보조회"
	static final String GET_BILL_COMMISSION_EXAMINATION_INFO = "getBillCommissionExaminationInfo"; // "의안 위원회심사 정보조회"
	static final String GET_BILL_DELIVERATE_INFO = "getBillDeliverateInfo"; // "의안 본회의심의 정보조회"
	static final String GET_BILL_TRANSFERRED_INFO = "getBillTransferredInfo"; // "의안 정부이송 정보조회"
	static final String GET_BILL_ADDITIONAL_INFO = "getBillAdditionalInfo"; // "의안 부가정보조회"
	static final String GET_PASS_RCEPT_RECENT_LIST = "getPassRceptRecentList"; // "처리의안 통계(총괄) 정보조회"
	static final String GET_RCEPT_KND_LIST = "getRceptKndList"; // "처리의안 통계(의안종류별) 정보조회"
	static final String GET_RCEPT_MOOR_LIST = "getRceptMoorList"; // "처리의안 통계(위원회별) 정보조회"
	static final String GET_PROCESS_BILL_STATIC_INFO_LIST = "getProcessBillStaticInfoList"; // "처리의안 통계(법률안) 정보조회"
	static final String GET_MOORING_BILL_STATIC_LIST = "getMooringBillStaticList"; // "계류의안 통계"
	static final String GET_JUDGE_REPORT_BODY_LIST = "getJudgeReportBodyList"; // "심사보고서 내용 목록 정보조회"
	static final String GET_MOTION_LAW_LIST = "getMotionLawList"; // "발의법률안 목록 정보조회"
	static final String GET_OFFER_REASON_LIST = "getOfferReasonList"; // "제안이유/주요내용 목록 정보조회"
	static final String GET_PERIOD_LIST = "getPeriodList"; // "회기정보 조회"
	static final String GET_COMMIT_PETITION_LIST = "getCommitPetitionList"; // "소관위원회 정보조회"
	static final String GET_PETITION_LIST = "getPetitionList"; // "청원 목록 정보조회"
	static final String GET_COMMIT_PETITION_PROCESS_INFO_LIST = "getCommitPetitionProcessInfoList"; // "청원 처리 목록 정보조회"
	static final String GET_COMMIT_PETITION_MOORING_INFO_LIST = "getCommitPetitionMooringInfoList"; // "청원 계류 목록 정보조회"
	static final String GET_PETITION_RECEIPT_INFO = "getPetitionReceiptInfo"; // "청원 접수 정보조회"
	static final String GET_PETITION_COMMISSION_EXAMINATION_INFO = "getPetitionCommissionExaminationInfo"; // "청원 위원회심사 정보조회"
	static final String GET_PETITION_DELIVERATE_INFO = "getPetitionDeliverateInfo"; // "청원 본회의심의 정보조회"
	static final String GET_PETITION_TRANSFERRED_INFO = "getPetitionTransferredInfo"; // "청원 정부이송 정보조회"
	static final String GET_ACHIEVEMENT_INFO = "getAchievementInfo"; // "청원 달성도 정보조회"
	static final String GET_PETITION_ADDITIONAL_INFO = "getPetitionAdditionalInfo"; // "청원 부가 정보조회"
	static final String GET_PETITION_STATIC = "getPetitionStatic"; // "청원 통계 정보조회"
	static final String GET_ANNUAL_REPORT_LIST = "getAnnualReportList"; // "연차보고서 목록 정보조회"
	static final String GET_ANNUAL_REPORT_DETAIL = "getAnnualReportDetail"; // "연차보고서 세부 정보조회"
	static final String GET_BILL_PROMULGATION_INFO = "getBillPromulgationInfo"; // "의안 공포 정보조회"

	private Operation() {
	}
}
