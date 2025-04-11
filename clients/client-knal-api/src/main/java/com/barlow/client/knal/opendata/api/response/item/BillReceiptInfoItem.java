package com.barlow.client.knal.opendata.api.response.item;

public record BillReceiptInfoItem(
    Receipt receipt
) {
	public record Receipt(
		String billName, // 의안명
		String billNo, // 의안번호
		String proposeDt, // 제안일자
		String proposer, // 제안자
		String bookHwpName, // 문서명 (HWP)
		String bookPdfName, // 문서명 (PDF)
		String bookHwpUrl, // 의안 원문 HWP 파일 경로
		String bookPdfUrl, // 의안 원문 PDF 파일 경로
		String etcHwpName, // 기타문서 HWP
		String etcPdfName, // 기타문서 PDF
		String etcHwpUrl, // 기타문서 HWP 파일 경로
		String etcPdfUrl, // 기타문서 PDF 파일 경로
		String cosHwpName, // 비용추계서 HWP 문서명
		String cosPdfpName, // 비용추계서 PDF 문서명
		String costHwpUrl, // 비용추계서 HWP 파일 경로
		String costPdfUrl, // 비용추계서 PDF 파일 경로
		String notcostHwpName, // 비용추계서 문서명
		String notcostPdfName, // 비용추계서 문서명
		String notcostHwpUrl, // 비용추계서 HWP 파일 경로
		String notcostPdfUrl, // 비용추계서 PDF 파일 경로
		String summaryLink, // 제안 이유 및 주요내용 링크 경로
		String proposePeriod, // 제안회기
		String withdrawDate // 철회일자
	) {
	}
}
