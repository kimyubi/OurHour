package com.ourhours.server;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ourhours.server.global.config.database.postgresql.DataSourceType;
import com.ourhours.server.global.config.database.postgresql.RoutingDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@ActiveProfiles("dev")
class RoutingDataSourceConfigTest {

	private static final String DETERMINE_CURRENT_LOOKUP_KEY = "determineCurrentLookupKey";

	@Transactional
	@DisplayName("MainDataSource Replication 설정 테스트")
	@Test
	void testMasterDataSourceReplication() throws Exception {
		// Given
		RoutingDataSource routingDataSource = new RoutingDataSource();

		// When
		Method declaredMethod = RoutingDataSource.class.getDeclaredMethod(DETERMINE_CURRENT_LOOKUP_KEY);
		declaredMethod.setAccessible(true);

		Object object = declaredMethod.invoke(routingDataSource);

		// Then
		log.info("object : [{}]", object);
		assertEquals(DataSourceType.MAIN.toString(), object.toString());
	}

	@Transactional(readOnly = true)
	@DisplayName("StandbyDataSource Replication 설정 테스트")
	@Test
	void testStandbyDataSourceReplication() throws Exception {

		// Given
		RoutingDataSource routingDataSource = new RoutingDataSource();

		// When
		Method declaredMethod = RoutingDataSource.class.getDeclaredMethod(DETERMINE_CURRENT_LOOKUP_KEY);
		declaredMethod.setAccessible(true);

		Object object = declaredMethod.invoke(routingDataSource);

		// Then
		log.info("object : [{}]", object);
		assertEquals(DataSourceType.STANDBY.toString(), object.toString());
	}
}
