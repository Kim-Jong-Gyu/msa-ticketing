package com.ticketing.storage.performance.mongo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
@EnableMongoRepositories(basePackages = "com.ticketing.storage.performance.mongo")
@EntityScan(basePackages = "com.ticketing.storage.performance.mongo")
public class MongoDbConfig {


	@Value("${storage.datasource.core.host}")
	private String host;

	@Value("${storage.datasource.core.port}")
	private String port;

	@Value("${storage.datasource.core.database}")
	private String database;

	@Bean
	public MongoClient mongoClient() {
		String uri = String.format("mongodb://%s:%d", host, Integer.parseInt(port));
		return MongoClients.create(uri); // 기본 커넥션
	}

	@Bean
	public MongoTemplate mongoTemplate() {
		return new MongoTemplate(mongoClient(), database);
	}

	@Bean
	public MongoMappingContext mongoMappingContext() throws ClassNotFoundException {
		MongoMappingContext mappingContext = new MongoMappingContext();
		mappingContext.setAutoIndexCreation(true);
		return mappingContext;
	}
}
