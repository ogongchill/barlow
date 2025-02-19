package com.barlow.core.domain.billpost;

import com.barlow.core.exception.CoreDomainException;
import com.barlow.core.exception.CoreDomainExceptionCode;
import com.barlow.core.exception.CoreDomainExceptionLevel;
import com.barlow.core.exception.CoreDomainExceptionType;

public final class BillPostDomainException extends CoreDomainException {

	BillPostDomainException(CoreDomainExceptionCode code, CoreDomainExceptionLevel level, String message) {
		super(code, level, message);
	}

	BillPostDomainException(CoreDomainExceptionType exceptionType, String message) {
		super(exceptionType, message);
	}

	static BillPostDomainException notFound(String billId) {
		return new BillPostDomainException(
			CoreDomainExceptionType.NOT_FOUND_EXCEPTION,
			String.format("법안 %s 에 대한 게시글이 존재하지 않습니다", billId)
		);
	}
}
