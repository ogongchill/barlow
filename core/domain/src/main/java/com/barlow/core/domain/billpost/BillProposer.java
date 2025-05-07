package com.barlow.core.domain.billpost;

public class BillProposer {

	private final String proposerCode;
	private final String proposerName;
	private final String partyName;
	private final String profileImagePath;

	public BillProposer(
		String proposerCode,
		String proposerName,
		String partyName,
		String profileImagePath
	) {
		this.proposerCode = proposerCode;
		this.proposerName = proposerName;
		this.partyName = partyName;
		this.profileImagePath = profileImagePath;
	}

	public String getProposerCode() {
		return proposerCode;
	}

	public String getProposerName() {
		return proposerName;
	}

	public String getPartyName() {
		return partyName;
	}

	public String getProfileImagePath() {
		return profileImagePath;
	}
}
