package com.barlow.batch.core.recentbill.job.step;

import static com.barlow.batch.core.recentbill.LawmakerProvider.Lawmaker;

import java.sql.Types;
import java.time.LocalDateTime;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import com.barlow.storage.db.core.PartyName;

@Component
@StepScope
public class BillProposerWriter implements ItemWriter<BillProposer> {

	private static final String BILL_PROPOSER_TABLE_NAME = "bill_proposer";
	private static final String BILL_PROPOSER_PK = "bill_proposer_no";

	private final SimpleJdbcInsert simpleJdbcInsert;

	public BillProposerWriter(DataSource dataSource) {
		this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
			.withTableName(BILL_PROPOSER_TABLE_NAME)
			.usingGeneratedKeyColumns(BILL_PROPOSER_PK);
	}

	@Override
	public void write(Chunk<? extends BillProposer> chunk) {
		chunk.getItems().forEach(this::saveAllInBatch);
	}

	private void saveAllInBatch(BillProposer billProposer) {
		SqlParameterSource[] sqlParameterSources = billProposer.lawmakers()
			.stream()
			.map(lawmaker -> createMapSqlParameterSource(billProposer.billId(), lawmaker))
			.toArray(SqlParameterSource[]::new);
		simpleJdbcInsert.executeBatch(sqlParameterSources);
	}

	private MapSqlParameterSource createMapSqlParameterSource(String billId, Lawmaker lawmaker) {
		return new MapSqlParameterSource()
			.addValue("propose_bill_id", billId)
			.addValue("proposer_code", lawmaker.code())
			.addValue("proposer_name", lawmaker.name())
			.addValue("proposer_profile_image_path", lawmaker.profileImagePath())
			.addValue("party_name", PartyName.findByValue(lawmaker.partyName()))
			.addValue("created_at", LocalDateTime.now(), Types.TIMESTAMP)
			.addValue("updated_at", LocalDateTime.now(), Types.TIMESTAMP);
	}
}
