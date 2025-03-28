package com.ticketing.microservices.core.hall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.ticketing")
public class HallServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HallServiceApplication.class, args);
	}

}
