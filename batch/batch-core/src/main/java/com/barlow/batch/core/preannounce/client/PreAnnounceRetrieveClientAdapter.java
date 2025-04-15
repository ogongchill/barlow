package com.barlow.batch.core.preannounce.client;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.barlow.batch.core.preannounce.job.CurrentPreAnnounceBills;
import com.barlow.batch.core.preannounce.job.PreAnnounceRetrieveClient;
import com.barlow.batch.core.preannounce.job.PreAnnounceBatchEntity;
import com.barlow.client.knal.opencongress.api.OpenCongressApiPort;
import com.barlow.client.knal.opencongress.api.common.DefaultRequest;
import com.barlow.client.knal.opencongress.api.preannounce.PreAnnouncementResponse;
import com.barlow.core.enumerate.LegislationType;

@Component
public class PreAnnounceRetrieveClientAdapter implements PreAnnounceRetrieveClient {

	private static final Logger log = LoggerFactory.getLogger(PreAnnounceRetrieveClientAdapter.class);

	private final OpenCongressApiPort api;

	public PreAnnounceRetrieveClientAdapter(OpenCongressApiPort api) {
		this.api = api;
	}

	@Override
	public CurrentPreAnnounceBills getPreAnnouncement() {
		DefaultRequest request = DefaultRequest.builder()
			.pSize(300)
			.pIndex(1)
			.type("json")
			.build();
		log.info("{} : 진행중인 입법예고 조회 호출", LocalDateTime.now());
		PreAnnouncementResponse response = api.getPreAnnouncement(request);
		log.info("{} : 진행중인 입법예고 조회 완료", LocalDateTime.now());
		return new CurrentPreAnnounceBills(
			response.getRowItems()
				.stream()
				.map(item -> new PreAnnounceBatchEntity(
					item.billId(),
					item.billName(),
					item.proposer(),
					LegislationType.findByValue(item.currentCommittee()),
					LocalDateTime.parse(item.notifyEndDate()),
					item.linkUrl()
				))
				.toList()
		);
	}
}
