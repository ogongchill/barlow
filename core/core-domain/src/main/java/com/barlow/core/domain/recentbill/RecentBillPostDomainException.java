package com.barlow.core.domain.recentbill;

import com.barlow.core.exception.CoreDomainException;
import com.barlow.core.exception.CoreDomainExceptionCode;
import com.barlow.core.exception.CoreDomainExceptionLevel;
import com.barlow.core.exception.CoreDomainExceptionType;

public final class RecentBillPostDomainException extends CoreDomainException {

	RecentBillPostDomainException(CoreDomainExceptionCode code, CoreDomainExceptionLevel level, String message) {
		super(code, level, message);
	}

	RecentBillPostDomainException(CoreDomainExceptionType exceptionType, String message) {
		super(exceptionType, message);
	}

	static RecentBillPostDomainException notFound(String billId) {
		return new RecentBillPostDomainException(
			CoreDomainExceptionType.NOT_FOUND_EXCEPTION,
			String.format("최근발의법안 %s 에 대한 게시글이 존재하지 않습니다", billId)
		);
	}
}
