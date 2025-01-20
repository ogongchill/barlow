package com.barlow.client.knal.api.response.item;

public record RecentRceptListItem(
    String billId,// 의안 ID
    String billNo,// 의안번호
    String billName, // 의안명
    String proposerKind, // 제안자 구분
    String proposeDt, // 제안일
    String committeeName // 소관위원회명
) {
}
