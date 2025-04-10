package com.barlow.batch.core.recentbill.client;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BillNameParser {

	private static final String REGEX_PATTERN_TRAILING_PARENTHESIS = "^(.*?)\\(([^)]+)\\)(?:\\(([^)]+)\\))?$";
	private static final Pattern pattern = Pattern.compile(REGEX_PATTERN_TRAILING_PARENTHESIS);

	public static Optional<ParsedBillName> parse(String billName) {
		Matcher matcher = pattern.matcher(billName);
		if (matcher.find()) {
			return Optional.of(new ParsedBillName(
				matcher.group(0),
				matcher.group(1),
				matcher.group(2),
				matcher.group(3)
			));
		}
		return Optional.empty();
	}

	private BillNameParser() {
	}
}

