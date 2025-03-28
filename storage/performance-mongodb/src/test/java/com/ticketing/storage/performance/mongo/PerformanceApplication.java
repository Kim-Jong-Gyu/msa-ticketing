package com.ticketing.storage.performance.mongo;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ActiveProfiles;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.ticketing.storage.performance.mongo")
@EntityScan(basePackages = "com.ticketing.storage.performance.mongo")
@ComponentScan("com.ticketing.storage.performance.mongo.persistence")
@ActiveProfiles("test")
public class PerformanceApplication {

}
