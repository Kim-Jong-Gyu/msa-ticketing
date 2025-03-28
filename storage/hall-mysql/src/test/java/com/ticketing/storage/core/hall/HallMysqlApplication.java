package com.ticketing.storage.core.hall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.ActiveProfiles;

@SpringBootApplication
@ActiveProfiles("test")
public class HallMysqlApplication {
	public static void main(String[] args) {
		SpringApplication.run(HallMysqlApplication.class, args);
	}
}
