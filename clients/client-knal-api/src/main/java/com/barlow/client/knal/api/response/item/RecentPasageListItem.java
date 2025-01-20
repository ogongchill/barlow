package com.barlow.client.knal.api.response.item;

/**
 * <a href="https://www.data.go.kr/data/3037286/openapi.do">IROS4_OA_DV_0401_OpenAPI활용가이드_의안정보(국회사무처)_v1.30<</a></href> <code>getRecentPasageList</code>에 따른 응답임 <br>
 * @see com.barlow.client.knal.api.Operation
 * @see com.barlow.client.knal.api.request.RecentRceptListRequest
 */
public record RecentPasageListItem(
    String billId, // 의안 ID
    String billNo, // 의안번호
    String billName, // 의안명
    String proposerKind, // 제안자 구분
    String proposeDt, // 제안일
    String committeeName, // 소관위원회명
    String procDt, // 본회의 의결일자
    String generalResult // 본회의 의결 결과
) {
}
