package com.ourhours.server.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ModuleConstant {

	POSTGRES_MAIN_SERVICE("test-main"),
	POSTGRES_STANDBY_SERVICE("test-standby"),
	POSTGRES_MAIN_LOG(".*database system is ready to accept connection.*"),
	POSTGRES_STANDBY_LOG(".*database system is ready to accept read-only connections.*"),
	POSTGRES_JDBC_URL_SUFFIX("/test_db"),
	DETERMINE_CURRENT_LOOKUP_KEY("determineCurrentLookupKey");

	private final String value;

}
