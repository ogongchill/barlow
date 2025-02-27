package com.barlow.client.knal.api.response.item;

public record BillInfoListItem(
    String billId, // 의안ID
    String billNo, // 의안번호
	String generalResult,
    String passGubn, // 처리구분
    String billName, // 의안명
    String proposerKind, // 제안자구분
    String proposeDt, // 제안일자
    String procDt, // 의결일자
    String summary, // 주요내용
    String procStageCd // 심사진행상태
) {
}
