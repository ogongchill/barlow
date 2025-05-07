package com.barlow.app.batch.tracebill.job;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.enumerate.NotificationTopic;
import com.barlow.core.enumerate.ProgressStatus;
import com.barlow.services.notification.NotificationRequest;

public class UpdatedBills {

	private final Map<ProgressStatus, List<BillInfo>> values;

	public UpdatedBills(Map<ProgressStatus, List<BillInfo>> values) {
		this.values = values;
	}

	public boolean isEmpty() {
		return values.isEmpty();
	}

	public UpdatedBills filterCommitteeReceived() {
		Map<ProgressStatus, List<BillInfo>> map = new EnumMap<>(ProgressStatus.class);
		List<BillInfo> receivedBillInfo = values.get(ProgressStatus.COMMITTEE_RECEIVED);
		if (receivedBillInfo == null) {
			return new UpdatedBills(map);
		}
		map.put(ProgressStatus.COMMITTEE_RECEIVED, receivedBillInfo);
		return new UpdatedBills(map);
	}

	public List<BillInfo> getCommitteeReceived() {
		return values.get(ProgressStatus.COMMITTEE_RECEIVED);
	}

	public void assignCommittee(String billId, LegislationType committee) {
		values.computeIfPresent(
			ProgressStatus.COMMITTEE_RECEIVED,
			(status, billInfos) -> billInfos.stream()
				.map(billInfo -> billInfo.isCommitteeAssignedYet(billId)
					? new BillInfo(billInfo.billId, billInfo.billName, committee)
					: billInfo
				)
				.toList()
		);
	}

	public Map<LegislationType, List<String>> groupByCommittee() {
		return values.get(ProgressStatus.COMMITTEE_RECEIVED)
			.stream()
			.collect(Collectors.groupingBy(
				BillInfo::committee,
				Collectors.mapping(BillInfo::billId, Collectors.toList())
			));
	}

	public Map<NotificationTopic, List<NotificationRequest.BillInfo>> groupByCommitteeNotificationTopic() {
		return values.get(ProgressStatus.COMMITTEE_RECEIVED)
			.stream()
			.collect(Collectors.groupingBy(
				billInfo -> NotificationTopic.findByLegislationType(billInfo.committee),
				Collectors.mapping(billInfo -> new NotificationRequest.BillInfo(
					billInfo.billId, billInfo.billName
				), Collectors.toList())
			));
	}

	public UpdatedBills filterNonCommitteeReceived() {
		return new UpdatedBills(
			values.entrySet()
				.stream()
				.filter(entry -> !entry.getKey().isCommitteeReceived())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
		);
	}

	public Map<ProgressStatus, List<String>> groupByProgressStatus() {
		Map<ProgressStatus, List<String>> map = new EnumMap<>(ProgressStatus.class);
		for (Map.Entry<ProgressStatus, List<BillInfo>> entry : values.entrySet()) {
			List<String> billIds = entry.getValue().stream()
				.map(billInfo -> billInfo.billId)
				.toList();
			map.computeIfAbsent(entry.getKey(), status -> new ArrayList<>())
				.addAll(billIds);
		}
		return map;
	}

	public Map<NotificationTopic, List<NotificationRequest.BillInfo>> groupByNonCommitteeNotificationTopic() {
		return values.entrySet()
			.stream()
			.filter(entry -> NotificationTopic.isNotifiableProgressStatus(entry.getKey()))
			.collect(Collectors.groupingBy(
				entry -> NotificationTopic.findByProgressStatus(entry.getKey()),
				Collectors.flatMapping(entry -> entry.getValue().stream()
						.map(billInfo -> new NotificationRequest.BillInfo(billInfo.billId, billInfo.billName)),
					Collectors.toList())
			));
	}

	public record BillInfo(
		String billId,
		String billName,
		LegislationType committee
	) {
		boolean isCommitteeAssignedYet(String billId) {
			return this.billId.equals(billId) && this.committee.isEmpty();
		}
	}
}
