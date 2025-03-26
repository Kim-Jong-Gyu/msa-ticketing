package com.performance.storage.performance.mongo;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

@TestConfiguration
public class MongoTestServerConfig {

	@Bean
	public MongoClientSettings mongoClientSettings() {
		return MongoClientSettings.builder()
			.applyConnectionString(new ConnectionString("mongodb://localhost:" + embeddedMongoPort()))
			.build();
	}
	private int embeddedMongoPort() {
		return 27017; // 원하는 포트 지정 (사용 가능한 포트로 지정)
	}
	@Bean
	public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDbFactory) {
		return new MongoTemplate(mongoDbFactory);
	}

	@Bean
	public MongoDatabaseFactory mongoDatabaseFactory(MongoServer mongoServer) {
		String connection = mongoServer.getConnectionString();
		return new SimpleMongoClientDatabaseFactory(connection + "/test");
	}

	@Bean(destroyMethod = "shutdown")
	public MongoServer mongoServer() {
		MongoServer mongoServer = new MongoServer(new MemoryBackend());
		mongoServer.bind();
		return mongoServer;
	}
}
