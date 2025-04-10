package com.ticketing.microservices.core.hall;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;


@SpringBootApplication
@ComponentScan("com.ticketing")
public class HallServiceApplication {

	private static final Logger LOG = LoggerFactory.getLogger(HallServiceApplication.class);


    @Bean
	public Scheduler jdbcScheduler() {
		LOG.info("Bounded Elastic 설정");
		return Schedulers.boundedElastic();
	}


	public static void main(String[] args) {
		SpringApplication.run(HallServiceApplication.class, args);
	}

}
