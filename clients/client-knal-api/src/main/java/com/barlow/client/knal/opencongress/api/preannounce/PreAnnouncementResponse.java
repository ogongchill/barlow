package com.barlow.client.knal.opencongress.api.preannounce;

import java.util.List;

import com.barlow.client.knal.opencongress.api.Operation;
import com.barlow.client.knal.opencongress.api.common.Head;
import com.barlow.client.knal.opencongress.api.common.Response;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PreAnnouncementResponse {

	@JsonProperty(Operation.GET_PRE_ANNOUNCEMENT)
	private List<Response<PreAnnouncementItem>> responses;

	public List<Response<PreAnnouncementItem>> getResponses() {
		return responses;
	}

	public List<Head> getHeads() {
		return responses.getFirst().getHead();
	}

	public List<PreAnnouncementItem> getRowItems() {
		return responses.getLast().getRow();
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record PreAnnouncementItem(
		@JsonProperty("BILL_ID") String billId,
		@JsonProperty("BILL_NO") String billNo,
		@JsonProperty("BILL_NAME") String billName,
		@JsonProperty("AGE") String age,
		@JsonProperty("PROPOSER") String proposer,
		@JsonProperty("PROPOSER_KIND_CD") String proposerKindCode,
		@JsonProperty("CURR_COMMITTEE_ID") String currentCommitteeId,
		@JsonProperty("CURR_COMMITTEE") String currentCommittee,
		@JsonProperty("NOTI_ED_DT") String notifyEndDate, // 게시종료일
		@JsonProperty("LINK_URL") String linkUrl
	) {
	}
}
