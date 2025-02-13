package com.barlow.client.knal.api.response.item;

public record ProcMeetingItem(
        String confName, // 회의명
        String confDt, // 회의일
        String confResult, // 회의결과
        String pdfUrl, // 회의록 PDF 파일 경로
        String fileUrl // 문서에는 없지만 실제로 응답되는 항목
) {
}
