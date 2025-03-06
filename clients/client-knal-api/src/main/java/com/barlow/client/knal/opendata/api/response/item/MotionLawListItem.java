package com.barlow.client.knal.opendata.api.response.item;

public record MotionLawListItem(
        String billId, // 의안 ID
        String billNo, // 의안 번호
        String billName, // 의안명
        String coactKind, // 발의 유형
        String proposer, // 제안자
        String proposeDt, // 제안일자
        String procDt, // 의결 일자
        String generalResult, // 의결 결과
        String procStageCd // 심사진행상태
) {
}
