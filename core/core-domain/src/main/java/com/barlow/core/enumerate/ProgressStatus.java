package com.barlow.core.enumerate;

import java.util.Arrays;
import java.util.List;

public enum ProgressStatus {

	RECEIVED("접수"),
	COMMITTEE_RECEIVED("소관위접수"),
	COMMITTEE_REVIEW("소관위심사"),
	REPLACED_AND_DISCARDED("대안반영폐기"),
	SYSTEMATIC_WORDING_REVIEW("체계자구심사"),
	PLENARY_SUBMITTED("본회의부의안건"),
	PLENARY_DECIDED("본회의의결"),
	WITHDRAWN("철회"),
	GOVERNMENT_TRANSFERRED("정부이송"),
	REDEMAND_REQUESTED("재의요구"),
	REJECTED("재의(부결)"),
	PROMULGATED("공포"),

	ABROGATE("폐기"),
	;

	private final String value;

	public static ProgressStatus findByValue(String value) {
		return Arrays.stream(ProgressStatus.values())
			.filter(progress -> progress.value.equals(value))
			.findFirst()
			.orElse(ABROGATE);
	}

	public static List<String> findDefaultTagNames() {
		return Arrays.stream(ProgressStatus.values())
			.filter(ProgressStatus::isNotReceived)
			.map(Enum::name)
			.toList();
	}

	public boolean isReceived() {
		return this.equals(RECEIVED);
	}


	public boolean isNotReceived() {
		return !this.equals(RECEIVED);
	}

	public boolean isNotWithdrawn() {
		return !this.equals(WITHDRAWN);
	}

	public boolean isNotAbrogate() {
		return !this.equals(ABROGATE);
	}

	public boolean isCommitteeReceived() {
		return this.equals(COMMITTEE_RECEIVED);
	}

	ProgressStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
