package com.performance.storage.performance.mongo;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableMongoTestServer
@EnableMongoRepositories(basePackages = "com.performance.storage.performance.mongo")
@EntityScan(basePackages = "com.performance.storage.performance.mongo")
@ComponentScan("com.performance.storage.performance.mongo.persistence")
public class PerformanceApplication {

}
