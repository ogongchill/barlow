package com.barlow.client.knal.api.code;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum BillKindCode {

	B01("헌법개정"),
	B02("예산안"),
	B03("결산"),
	B04("법률안"),
	B05("동의안"),
	B06("승인안"),
	B07("결의안"),
	B08("건의안"),
	B09("규칙안"),
	B10("선출안"),
	B11("중요동의"),
	B12("의원징계"),
	B13("의원자격심사"),
	B14("윤리심사"),
	B15("기타안"),
	B16("기타");

	private final String description;

	BillKindCode(String description) {
		this.description = description;
	}

	public BillKindCode find(String target) {
		return Arrays.stream(BillKindCode.values())
			.filter(billCode -> billCode.description.equals(target))
			.findFirst()
			.orElseThrow(() -> new NoSuchElementException("no such bill code : " + target));
	}
}
