package com.barlow.client.knal.api.code;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum ProposerCode {

	F01("의원"),
	F02("위원장"),
	F03("정부"),
	F04("의장");

	private final String description;

	ProposerCode(String description) {
		this.description = description;
	}

	public static ProposerCode find(String target) {
		return Arrays.stream(ProposerCode.values())
			.filter(proposerCode -> proposerCode.description.equals(target))
			.findFirst()
			.orElseThrow(() -> new NoSuchElementException("no such proposer code: " + target));
	}
}
