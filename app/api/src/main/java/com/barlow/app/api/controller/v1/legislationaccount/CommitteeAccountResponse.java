package com.barlow.app.api.controller.v1.legislationaccount;

import java.util.List;

import com.barlow.core.domain.legislationaccount.LegislationAccount;
import com.barlow.app.support.response.Constant;

public record CommitteeAccountResponse(
	String title,
	String subtitle,
	String description,
	List<CommitteeAccount> accounts
) {
	private static final String _TITLE = "소관위원회 더 알아보기";
	private static final String _SUBTITLE = "소관위원회란?";
	private static final String _DESCRIPTION = """
		대한민국 소관위원회는 국회 내에서 입법 및 행정에 대한 심의·조사를 담당하는 전문 기구예요.
		총 17개 위원회가 있으며, 국회의원들이 소속되어 법률안 심사, 예산안 검토, 국정감사 등을 수행해요.
		""";

	static CommitteeAccountResponse from(List<LegislationAccount> legislationAccounts) {
		return new CommitteeAccountResponse(
			_TITLE,
			_SUBTITLE,
			_DESCRIPTION,
			legislationAccounts.stream()
				.map(CommitteeAccount::from)
				.toList()
		);
	}

	record CommitteeAccount(
		long accountNo,
		String accountName,
		String iconUrl,
		boolean isSubscribed,
		boolean isNotifiable
	) {
		static CommitteeAccount from(LegislationAccount legislationAccount) {
			return new CommitteeAccount(
				legislationAccount.getNo(),
				legislationAccount.getLegislationType(),
				Constant.IMAGE_ACCESS_URL + legislationAccount.getIconPath(),
				legislationAccount.isSubscribed(),
				legislationAccount.isNotifiable()
			);
		}
	}
}
