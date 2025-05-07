package com.barlow.app.batch.recentbill.client;

public record ParsedBillName(
	String fullMatch, // 전체
	String title, // 첫 번째 괄호 이전 텍스트
	String firstParens, // 첫 번째 괄호 텍스트
	String secondParens // 두 번째 괄호 텍스트
) {
}
