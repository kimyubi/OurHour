package com.ourhours.server;

import org.springframework.boot.SpringApplication;

public class TestOurHourApplication {

	public static void main(String[] args) {
		SpringApplication.from(OurHourApplication::main).with(TestOurHourApplication.class).run(args);
	}

}
