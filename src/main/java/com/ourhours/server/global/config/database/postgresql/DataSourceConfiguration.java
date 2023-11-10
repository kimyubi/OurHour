package com.ourhours.server.global.config.database.postgresql;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfiguration {

	public static final String MAIN_DATA_SOURCE = "MAIN_DATA_SOURCE";
	public static final String STANDBY_DATA_SOURCE = "STANDBY_DATA_SOURCE";

	@Bean(MAIN_DATA_SOURCE)
	@ConfigurationProperties(prefix = "spring.datasource.main.hikari")
	public DataSource mainDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	@Bean(STANDBY_DATA_SOURCE)
	@ConfigurationProperties(prefix = "spring.datasource.standby.hikari")
	public DataSource standbyDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

}
