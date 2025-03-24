package com.performance.storage.performance.mongo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceRepository extends MongoRepository<PerformanceEntity, String> {

	Optional<PerformanceEntity> findByPerformanceId(Integer performanceId);

}
