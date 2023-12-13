package com.ourhours.server.global.config.database.postgresql;

import static com.ourhours.server.global.config.database.postgresql.DataSourceConfiguration.*;
import static org.springframework.orm.jpa.vendor.Database.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages = "com.ourhours.server.domain.*.repository")
public class RoutingDataSourceConfiguration {

	private static final String ROUTING_DATA_SOURCE = "routingDataSource";
	private static final String DATA_SOURCE = "dataSource";

	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String ddlPolicy;

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
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactory.setDataSource(dataSource);

		entityManagerFactory.setPackagesToScan("com.ourhours.server.domain.*.domain.entity");

		entityManagerFactory.setJpaVendorAdapter(this.jpaVendorAdapter());
		entityManagerFactory.setPersistenceUnitName("entityManager");
		entityManagerFactory.setJpaProperties(addJpaProperties());
		return entityManagerFactory;
	}

	private JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setShowSql(true);
		hibernateJpaVendorAdapter.setGenerateDdl(true);
		hibernateJpaVendorAdapter.setDatabase(POSTGRESQL);
		return hibernateJpaVendorAdapter;
	}

	@Bean("transactionManager")
	public PlatformTransactionManager platformTransactionManager(
		@Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
		return jpaTransactionManager;
	}

	private Properties addJpaProperties() {
		Properties properties = new Properties();
		properties.setProperty("spring.jpa.hibernate.ddl-auto", ddlPolicy);
		properties.setProperty("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		properties.setProperty("spring.jpa.properties.hibernate.format_sql", "true");
		return properties;
	}

}
