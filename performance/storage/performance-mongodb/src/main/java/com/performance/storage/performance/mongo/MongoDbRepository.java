package com.performance.storage.performance.mongo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface MongoDbRepository extends MongoRepository<PerformanceEntity, String> {

	Optional<PerformanceEntity> findByPerformanceId(Integer performanceId);

}
