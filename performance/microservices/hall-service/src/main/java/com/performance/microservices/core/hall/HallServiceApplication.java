package com.performance.microservices.core.hall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "com.performance.storage.hall.mysql")
@ComponentScan("com.performance")
public class HallServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HallServiceApplication.class, args);
	}

}
