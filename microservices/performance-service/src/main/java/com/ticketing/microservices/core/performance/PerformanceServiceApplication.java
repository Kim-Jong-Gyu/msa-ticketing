package com.ticketing.microservices.core.performance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.ticketing")
public class PerformanceServiceApplication {

	private static final Logger LOG = LoggerFactory.getLogger(PerformanceServiceApplication.class);

	// 어떤 DB를 쓰는지 Log 추가
	public static void main(String[] args) {

		ConfigurableApplicationContext ctx = SpringApplication.run(PerformanceServiceApplication.class, args);
		MongoProperties mongoProperties = ctx.getBean(MongoProperties.class);
		LOG.info("Connected to MongoDb: " + mongoProperties.getHost() + ":" + mongoProperties.getPort());
	}


}
