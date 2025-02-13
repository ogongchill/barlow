package com.barlow.client.knal.api.response.item;

public record CommitMeetingItem(
        String confName, // 회의명
        String confDt, // 회의일
        String confResult, // 회의 결과
        String fileName // 회의록
) {
}
