package com.barlow.client.knal.api.response.item;

public record JsictionComiteProcessListItem(
    String billId, // 의안 ID
    String billNo, // 의안번호
    String billName, // 의안명
    String proposer, // 제안자
    String proposerKind, // 제안자구분
    String proposeDt, // 제안일
    String submitDt, // 회부일
    String committeeName, // 소관위원회
    String procDt, // 의결일자
    String generalResult // 의결결과
) {
}
