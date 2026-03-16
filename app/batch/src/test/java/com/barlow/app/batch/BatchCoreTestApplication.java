package com.barlow.app.batch;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import com.barlow.infra.storage.batch.config.BatchCoreDatasourceConfig;
import com.barlow.infra.storage.batch.config.BatchCoreJpaConfig;

@ConfigurationPropertiesScan
@SpringBootApplication
@Import({BatchCoreDatasourceConfig.class, BatchCoreJpaConfig.class})
public class BatchCoreTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchCoreTestApplication.class, args);
	}

	@Bean
	public DataSourceInitializer batchSchemaInitializer(@Qualifier("batchCoreDataSource") DataSource dataSource) {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("org/springframework/batch/core/schema-h2.sql"));
		populator.setContinueOnError(true);
		DataSourceInitializer initializer = new DataSourceInitializer();
		initializer.setDataSource(dataSource);
		initializer.setDatabasePopulator(populator);
		return initializer;
	}
}
