package com.barlow.core.storage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.barlow.core.domain.registration.Term;
import com.barlow.core.domain.registration.TermAgreement;
import com.barlow.core.domain.registration.UserTermAgreementCommand;
import com.barlow.core.storage.support.StorageTest;

@DataJpaTest
@StorageTest(value = "dummy/term.json", dbType = StorageTest.DatabaseType.H2)
@Import(TermRepositoryAdapter.class)
class TermRepositoryAdapterTest {

	@Autowired
	private TermRepositoryAdapter adapter;

	@Autowired
	private TermAgreementJpaRepository termAgreementJpaRepository;

	@DisplayName("활성화된 약관 목록을 조회한다")
	@Test
	void retrieveActiveTerms() {
		List<Term> terms = adapter.retrieveActiveTerms();

		assertAll(
			() -> assertThat(terms).hasSize(3),
			() -> assertThat(terms).extracting(Term::title)
				.containsExactlyInAnyOrder("서비스 이용약관", "개인정보 처리방침", "마케팅 정보 수신 동의"),
			() -> assertThat(terms).filteredOn(Term::required).hasSize(2)
		);
	}

	@DisplayName("사용자의 약관 동의 정보를 저장한다")
	@Test
	void saveUserAgreement() {
		LocalDateTime agreedAt = LocalDateTime.of(2024, 1, 15, 10, 0, 0);
		List<TermAgreement> agreements = List.of(
			new TermAgreement(1L, true, agreedAt),
			new TermAgreement(2L, true, agreedAt),
			new TermAgreement(3L, false, agreedAt)
		);
		UserTermAgreementCommand command = new UserTermAgreementCommand(1L, agreements);

		List<TermAgreement> result = adapter.saveUserAgreement(command);

		assertAll(
			() -> assertThat(result).hasSize(3),
			() -> assertThat(result).filteredOn(TermAgreement::agreed).hasSize(2),
			() -> assertThat(termAgreementJpaRepository.count()).isEqualTo(3)
		);
	}

	@DisplayName("여러 사용자의 약관 동의 정보를 각각 저장한다")
	@Test
	void saveUserAgreement_multipleUsers() {
		LocalDateTime agreedAt = LocalDateTime.now();
		List<TermAgreement> user1Agreements = List.of(
			new TermAgreement(1L, true, agreedAt),
			new TermAgreement(2L, true, agreedAt)
		);
		List<TermAgreement> user2Agreements = List.of(
			new TermAgreement(1L, true, agreedAt),
			new TermAgreement(2L, true, agreedAt),
			new TermAgreement(3L, true, agreedAt)
		);

		adapter.saveUserAgreement(new UserTermAgreementCommand(1L, user1Agreements));
		adapter.saveUserAgreement(new UserTermAgreementCommand(2L, user2Agreements));

		assertThat(termAgreementJpaRepository.count()).isEqualTo(5);
	}
}
