package com.barlow.core.storage;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.barlow.core.domain.User;
import com.barlow.core.storage.support.StorageTest;

@StorageTest({"dummy/legislationAccount.json", "dummy/subscribe.json"})
class MyLegislationAccountRepositoryAdapterTest extends CoreDbContextTest {

	private final MyLegislationAccountRepositoryAdapter adapter;

	public MyLegislationAccountRepositoryAdapterTest(MyLegislationAccountRepositoryAdapter adapter) {
		this.adapter = adapter;
	}

	@DisplayName("회원이 구독한 입법계정 정보를 조회한다")
	@Test
	void retrieveMyLegislationAccounts() {
		User user = User.of(1L, User.Role.GUEST);

		assertThat(adapter.retrieveMyLegislationAccounts(user))
			.isNotEmpty()
			.hasSize(1);
	}
}