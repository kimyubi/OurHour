package com.ourhours.server;

import static com.ourhours.server.global.config.database.postgresql.DataSourceConfiguration.*;
import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@ActiveProfiles("dev")
class DataSourceConfigurationTest {

	@Autowired
	private Environment environment;

	@DisplayName("MainDataSource 설정 테스트")
	@Test
	void masterDataSourceTest(
		@Qualifier(MAIN_DATA_SOURCE) final DataSource mainDataSource) {

		// Given
		String driverClassName = environment.getProperty("spring.datasource.main.hikari.driver-class-name");
		String jdbcUrl = environment.getProperty("spring.datasource.main.hikari.jdbc-url");
		Boolean readOnly = Boolean.valueOf(environment.getProperty("spring.datasource.main.hikari.read-only"));
		String username = environment.getProperty("spring.datasource.main.hikari.username");

		// When
		try (HikariDataSource hikariDataSource = (HikariDataSource)mainDataSource) {
			// Then
			log.info("hikari DataSource : [{}]", hikariDataSource);
			assertEquals(hikariDataSource.getDriverClassName(), driverClassName);
			assertEquals(hikariDataSource.getJdbcUrl(), jdbcUrl);
			assertEquals(hikariDataSource.isReadOnly(), readOnly);
			assertEquals(hikariDataSource.getUsername(), username);
		}
	}

	@DisplayName("standbyDataSource 설정 테스트")
	@Test
	void slaveDataSourceTest(
		@Qualifier(STANDBY_DATA_SOURCE) final DataSource standbyDataSource) {

		// Given
		String driverClassName = environment.getProperty("spring.datasource.standby.hikari.driver-class-name");
		String jdbcUrl = environment.getProperty("spring.datasource.standby.hikari.jdbc-url");
		Boolean readOnly = Boolean.valueOf(environment.getProperty("spring.datasource.standby.hikari.read-only"));
		String username = environment.getProperty("spring.datasource.standby.hikari.username");

		// When
		try (HikariDataSource hikariDataSource = (HikariDataSource)standbyDataSource) {
			// Then
			log.info("hikari DataSource : [{}]", hikariDataSource);
			assertEquals(hikariDataSource.getDriverClassName(), driverClassName);
			assertEquals(hikariDataSource.getJdbcUrl(), jdbcUrl);
			assertEquals(hikariDataSource.isReadOnly(), readOnly);
			assertEquals(hikariDataSource.getUsername(), username);
		}
	}
}
