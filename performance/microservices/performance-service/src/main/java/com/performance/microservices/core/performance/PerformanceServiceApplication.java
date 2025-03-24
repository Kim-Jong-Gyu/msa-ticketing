package com.performance.microservices.core.performance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.performance")
public class PerformanceServiceApplication {

	private static final Logger LOG = LoggerFactory.getLogger(PerformanceServiceApplication.class);

	// 어떤 DB를 쓰는지 Log 추가
	public static void main(String[] args) {

		ConfigurableApplicationContext ctx = SpringApplication.run(PerformanceServiceApplication.class, args);

		String mongoDbHost = ctx.getEnvironment().getProperty("spring.data.mongodb.host");
		String mongoDbPort = ctx.getEnvironment().getProperty("spring.data.mongodb.port");
		LOG.info("Connected to MongoDb: " + mongoDbHost + ":" + mongoDbPort);
	}


}
