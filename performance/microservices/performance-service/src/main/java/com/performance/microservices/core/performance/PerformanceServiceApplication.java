package com.performance.microservices.core.performance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.performance")
public class PerformanceServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PerformanceServiceApplication.class, args);
	}

}
