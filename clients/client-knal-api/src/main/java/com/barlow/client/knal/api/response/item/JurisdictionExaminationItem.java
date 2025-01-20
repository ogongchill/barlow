package com.barlow.client.knal.api.response.item;

public record JurisdictionExaminationItem(
        String committeeName, //소관위원회
        String submitDt, // 회부일
        String presentDt, // 상정일
        String procDt, // 처리일
        String procResultCd, //처리 결과
        String docName1, // 문서1 파일명
        String docName2, // 문서2 파일명
        String hwpUrl1, // 문서1 HWP파일 경로
        String hwpUrl2, // 문서2 HWP파일 경로
        String pdfUrl1, // 문서1 PDF파일 경로
        String pdfUrl2// 문서2 PDF파일 경로
) {
}
