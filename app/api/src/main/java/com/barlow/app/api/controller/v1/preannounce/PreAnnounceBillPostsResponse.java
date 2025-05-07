package com.barlow.app.api.controller.v1.preannounce;

import java.util.List;

public record PreAnnounceBillPostsResponse(
	String display,
	List<PreAnnounceBillThumbnail> thumbnails,
	boolean isLastPage
) {
	static PreAnnounceBillPostsResponse of(List<PreAnnounceBillThumbnail> thumbnails, boolean isLastPage) {
		return new PreAnnounceBillPostsResponse(
			"위원회에 회부된 법률안을 심사하기 전에 법률안의 입법 취지와 주요 내용 등을 국민들에게 미리 알리는 절차를 말해요",
			thumbnails,
			isLastPage
		);
	}

	record PreAnnounceBillThumbnail(
		String billId,
		String billName,
		String proposers,
		String legislativeBody,
		int dDay
	) {
	}
}
