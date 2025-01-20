package com.barlow.client.knal.api.response.item;

public record JurisdictionMeetingItem(
        String confName, // 회의명
        String confDt, // 회의일
        String confResult, //회의 결과
        String pdfUrl, // 회의록 PDF파일 경로
        String fileUrl // 문서에는 나와있지만 응답에는 있는 항목
) {
}
