package com.barlow.core.domain.account.term;

import java.time.LocalDateTime;

public record Term(Long id, String title, String version, String linkUrl, Type termType, boolean required,
	LocalDateTime effectiveAt) {
	public enum Type {

		SERVICE, PRIVACY, MARKETING,;
	}
}
