package com.performance.storage.hall.mysql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan(basePackages = "com.performance.storage.hall")
@SpringBootApplication
public class HallMysqlApplication {
	public static void main(String[] args) {
		SpringApplication.run(HallMysqlApplication.class, args);
	}
}
