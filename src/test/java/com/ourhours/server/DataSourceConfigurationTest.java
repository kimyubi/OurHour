package com.ourhours.server;

import static com.ourhours.server.global.config.database.postgresql.DataSourceConfiguration.*;
import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;

import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class DataSourceConfigurationTest extends IntegrationTestSupporter {

	@DisplayName("MainDataSource 설정 테스트")
	@Test
	void mainDataSourceTest(
		@Qualifier(MAIN_DATA_SOURCE) final DataSource mainDataSource) {

		// Given
		String driverClassName = environment.getProperty("spring.datasource.main.hikari.driver-class-name");
		String jdbcUrl = environment.getProperty("spring.datasource.main.hikari.jdbc-url");
		String username = environment.getProperty("spring.datasource.main.hikari.username");

		// When
		try (HikariDataSource hikariDataSource = (HikariDataSource)mainDataSource) {
			// Then
			log.info("hikari DataSource : [{}]", hikariDataSource);
			assertEquals(hikariDataSource.getDriverClassName(), driverClassName);
			assertEquals(hikariDataSource.getJdbcUrl(), jdbcUrl);
			assertEquals(hikariDataSource.getUsername(), username);
		}
	}

	@DisplayName("standbyDataSource 설정 테스트")
	@Test
	void standbyDataSourceTest(
		@Qualifier(STANDBY_DATA_SOURCE) final DataSource standbyDataSource) {

		// Given
		String driverClassName = environment.getProperty("spring.datasource.standby.hikari.driver-class-name");
		String jdbcUrl = environment.getProperty("spring.datasource.standby.hikari.jdbc-url");
		String username = environment.getProperty("spring.datasource.standby.hikari.username");

		// When
		try (HikariDataSource hikariDataSource = (HikariDataSource)standbyDataSource) {
			// Then
			log.info("hikari DataSource : [{}]", hikariDataSource);
			assertEquals(hikariDataSource.getDriverClassName(), driverClassName);
			assertEquals(hikariDataSource.getJdbcUrl(), jdbcUrl);
			assertEquals(hikariDataSource.getUsername(), username);
		}
	}
}
