package com.barlow.client.knal.api.response.item;

public record ExhaustItem(
        String billLink, // 대안반영폐기 링크 경로
        String billName // 대안반영폐기 의안명
) {
}
