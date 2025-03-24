package com.performance.storage.perforamnce.mogo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan(basePackages = "com.performance.storage.performance")
@SpringBootApplication
public class PerformanceMongoApplication {
	public static void main(String[] args) {
		SpringApplication.run(PerformanceMongoApplication.class, args);
	}

}
