package com.barlow.storage.db.core;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.barlow.storage.db.CoreDbContextTest;
import com.barlow.storage.db.support.StorageTest;

@StorageTest("dummy/billPost.json")
class TodayBillPostRepositoryAdapterTest extends CoreDbContextTest {

	private final TodayBillPostRepositoryAdapter adapter;

	public TodayBillPostRepositoryAdapterTest(TodayBillPostRepositoryAdapter adapter) {
		this.adapter = adapter;
	}

	@DisplayName("오늘 날짜를 받아 오늘 날짜로 등록된 법안게시글을 조회한다")
	@ParameterizedTest
	@CsvSource(value = {"2025-01-01:3", "2025-01-02:0"}, delimiter = ':')
	void retrieveTodayBillPostThumbnails(String date, int expected) {
		assertThat(adapter.retrieveTodayBillPostThumbnails(LocalDate.parse(date)))
			.hasSize(expected);
	}
}