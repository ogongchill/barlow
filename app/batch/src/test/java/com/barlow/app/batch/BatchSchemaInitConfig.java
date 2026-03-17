package com.barlow.app.batch;

import javax.sql.DataSource;

import org.springframework.batch.support.DatabaseType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
public class BatchSchemaInitConfig {

	@Bean
	BatchSchemaInitializer batchSchemaInitializer(
		@Qualifier("batchCoreDataSource") DataSource batchCoreDataSource) {
		return new BatchSchemaInitializer(batchCoreDataSource);
	}

	static class BatchSchemaInitializer {

		BatchSchemaInitializer(DataSource dataSource) {
			ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
			populator.addScript(new ClassPathResource(
				"org/springframework/batch/core/schema-" + DatabaseType.H2.toString().toLowerCase() + ".sql"));
			populator.setContinueOnError(true);
			DatabasePopulatorUtils.execute(populator, dataSource);
		}
	}
}
