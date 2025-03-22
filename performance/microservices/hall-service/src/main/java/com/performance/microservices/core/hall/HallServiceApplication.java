package com.performance.microservices.core.hall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.performance")
public class HallServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HallServiceApplication.class, args);
	}

}
