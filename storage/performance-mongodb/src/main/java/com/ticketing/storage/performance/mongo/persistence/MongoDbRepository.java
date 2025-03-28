package com.ticketing.storage.performance.mongo.persistence;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoDbRepository extends MongoRepository<PerformanceEntity, String> {

	Optional<PerformanceEntity> findByPerformanceId(Integer performanceId);

}
