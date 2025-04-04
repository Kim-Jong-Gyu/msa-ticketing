package com.ticketing.storage.performance.mongo.config;


import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mongo.ReactiveMongoClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


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
	public ReactiveMongoTemplate mongoTemplate() {
		return new ReactiveMongoTemplate(mongoClient(), database);
	}

	@Bean
	public MongoMappingContext mongoMappingContext() throws ClassNotFoundException {
		MongoMappingContext mappingContext = new MongoMappingContext();
		mappingContext.setAutoIndexCreation(true);
		return mappingContext;
	}
}
