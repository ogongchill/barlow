package com.barlow.client.knal.opendata.api.response.item;

import com.barlow.client.knal.opendata.api.Operation;
import com.barlow.client.knal.opendata.api.request.BillInfoListRequest;

/**
 * <a href="https://www.data.go.kr/data/3037286/openapi.do">IROS4_OA_DV_0401_OpenAPI활용가이드_의안정보(국회사무처)_v1.30<</a></href> <resultCode>getBillPetitionMemberList</resultCode>에 따른 응답임 <br>
 * @see Operation
 * @see BillInfoListRequest
 */
public record BillPetitionMemberListItem(
    /**
     * <p>구분1</p>
     * 의안 : <resultCode>bill</resultCode><br>
     * 청원 : <resultCode>petition</resultCode><br>
     */
    String gbn1,

    /**
     * <p>구분2</p>
     * 접수 정보 목록<br>
     * 본회의 수정안 목록<br>
     */
    String gbn2,

    /**
     * <p>제안자/철회자 구분</p>
     * <resultCode>발의자</resultCode>, <resultCode>찬성자</resultCode>, <resultCode>철회자</resultCode>로 응답
     */
    String gbnCd,
    String hjNm, // 한자명
    String memName, // 의원명
    String polyNm // 소속정당
) {
}
