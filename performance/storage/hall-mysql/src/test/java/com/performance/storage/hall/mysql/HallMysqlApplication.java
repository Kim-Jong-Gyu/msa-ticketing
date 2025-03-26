package com.performance.storage.hall.mysql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "com.performance.storage.hall")
@ActiveProfiles("test")
public class HallMysqlApplication {
	public static void main(String[] args) {
		SpringApplication.run(HallMysqlApplication.class, args);
	}
}
