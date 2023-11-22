package com.ourhours.server;

import static com.ourhours.server.domain.ModuleConstant.*;
import static org.testcontainers.containers.PostgreSQLContainer.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;

import com.ourhours.server.global.util.jwt.JwtProvider;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(initializers = IntegrationTestSupporter.ContainerPropertyInitializer.class)
public abstract class IntegrationTestSupporter {

	@Autowired
	protected Environment environment;

	@Autowired
	protected JwtProvider jwtProvider;

	@Container
	static final DockerComposeContainer<?> postgresContainer;

	static String POSTGRES_MAIN_SERVICE_NAME = POSTGRES_MAIN_SERVICE.getValue();
	static String POSTGRES_STANDBY_SERVICE_NAME = POSTGRES_STANDBY_SERVICE.getValue();

	static String POSTGRES_MAIN_LOG_REGEX = POSTGRES_MAIN_LOG.getValue();
	static String POSTGRES_STANDBY_LOG_REGEX = POSTGRES_STANDBY_LOG.getValue();

	static Integer POSTGRES_MAIN_PORT;
	static Integer POSTGRES_STAND_BY_PORT;

	static String POSTGRES_MAIN_HOST;
	static String POSTGRES_STAND_BY_HOST;

	static String POSTGRES_MAIN_PREFIX;
	static String POSTGRES_STAND_BY_PREFIX;

	static String POSTGRES_SUFFIX = POSTGRES_JDBC_URL_SUFFIX.getValue();

	static {

		postgresContainer = new DockerComposeContainer<>(
			new File("src/test/resources/docker-compose-test.yml"))
			.withExposedService(POSTGRES_MAIN_SERVICE_NAME, POSTGRESQL_PORT,
				Wait.forLogMessage(POSTGRES_MAIN_LOG_REGEX, 1))

			.withExposedService(POSTGRES_STANDBY_SERVICE_NAME, POSTGRESQL_PORT,
				Wait.forLogMessage(POSTGRES_STANDBY_LOG_REGEX, 1));

		postgresContainer.start();

		POSTGRES_MAIN_PORT = postgresContainer.getServicePort(POSTGRES_MAIN_SERVICE_NAME, POSTGRESQL_PORT);
		POSTGRES_STAND_BY_PORT = postgresContainer.getServicePort(POSTGRES_STANDBY_SERVICE_NAME, POSTGRESQL_PORT);

		POSTGRES_MAIN_HOST = postgresContainer.getServiceHost(POSTGRES_MAIN_SERVICE_NAME, POSTGRES_MAIN_PORT);
		POSTGRES_STAND_BY_HOST = postgresContainer.getServiceHost(POSTGRES_STANDBY_SERVICE_NAME,
			POSTGRES_STAND_BY_PORT);

		POSTGRES_MAIN_PREFIX = "jdbc:postgresql://" + POSTGRES_MAIN_HOST + ":";
		POSTGRES_STAND_BY_PREFIX = "jdbc:postgresql://" + POSTGRES_STAND_BY_HOST + ":";

	}

	static class ContainerPropertyInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		@Override
		public void initialize(ConfigurableApplicationContext context) {
			Map<String, String> properties = new HashMap<>();
			properties.put("spring.datasource.main.hikari.jdbc-url",
				POSTGRES_MAIN_PREFIX + POSTGRES_MAIN_PORT + POSTGRES_SUFFIX);
			properties.put("spring.datasource.standby.hikari.jdbc-url",
				POSTGRES_STAND_BY_PREFIX + POSTGRES_STAND_BY_PORT + POSTGRES_SUFFIX);
			TestPropertyValues.of(properties)
				.applyTo(context);
		}
	}
}

