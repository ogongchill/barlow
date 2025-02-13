package com.barlow.client.knal.api.response.item;

public record ProcExaminationItem(
        String submitDt, // 회부일
        String presentDt, // 상정일
        String procDt, // 처리일
        String procResultCd, // 처리결과
        String hwpUrl, // 체계자구검토보고서 hwp 파일 명
        String pdfUrl // 체계자구검토보고서 pdf 파일명
) {
}
