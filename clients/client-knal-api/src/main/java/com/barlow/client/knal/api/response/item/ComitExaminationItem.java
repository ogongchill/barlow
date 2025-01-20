package com.barlow.client.knal.api.response.item;

public record ComitExaminationItem(
        String comitName, // 관련위원회
        String submitDt, // 회부일
        String presentDt, // 상정일
        String procDt, // 의견서제시일
        String pdfUrl // 문서
) {
}
