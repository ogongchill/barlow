package com.barlow.core.domain.recentbill;

public class BillProposer {

	private final long proposerNo;
	private final String proposerName;
	private final String partyName;
	private final String profileImagePath;

	public BillProposer(
		long proposerNo,
		String proposerName,
		String partyName,
		String profileImagePath
	) {
		this.proposerNo = proposerNo;
		this.proposerName = proposerName;
		this.partyName = partyName;
		this.profileImagePath = profileImagePath;
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
