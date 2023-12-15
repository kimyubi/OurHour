package com.ourhours.server.global.config.database.postgresql;

import static com.ourhours.server.global.config.database.postgresql.DataSourceConfiguration.*;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RoutingDataSourceConfiguration {

	private static final String ROUTING_DATA_SOURCE = "routingDataSource";
	private static final String DATA_SOURCE = "dataSource";

	private final JpaProperties jpaProperties;

	@Bean(ROUTING_DATA_SOURCE)
	public DataSource routingDataSource(@Qualifier(MAIN_DATA_SOURCE) final DataSource mainDataSource,
		@Qualifier(STANDBY_DATA_SOURCE) final DataSource standbyDataSource) {

		RoutingDataSource routingDataSource = new RoutingDataSource();

		Map<Object, Object> dataSourceMap = new HashMap<>();
		dataSourceMap.put(DataSourceType.MAIN, mainDataSource);
		dataSourceMap.put(DataSourceType.STANDBY, standbyDataSource);

		routingDataSource.setTargetDataSources(dataSourceMap);
		routingDataSource.setDefaultTargetDataSource(mainDataSource);

		return routingDataSource;
	}

	@Bean(DATA_SOURCE)
	public DataSource dataSource(@Qualifier(ROUTING_DATA_SOURCE) DataSource routingDataSource) {
		return new LazyConnectionDataSourceProxy(routingDataSource);
	}

	@Bean("entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier(DATA_SOURCE) DataSource dataSource) {
		EntityManagerFactoryBuilder entityManagerFactoryBuilder = generateEntityManagerFactoryBuilder(jpaProperties);

		return entityManagerFactoryBuilder
			.dataSource(dataSource)
			.packages("com.ourhours.server.domain.*.domain.entity")
			.build();
	}

	private EntityManagerFactoryBuilder generateEntityManagerFactoryBuilder(JpaProperties jpaProperties) {
		return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), jpaProperties.getProperties(), null);
	}

	@Bean("transactionManager")
	public PlatformTransactionManager platformTransactionManager(
		@Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
		return jpaTransactionManager;
	}

}
