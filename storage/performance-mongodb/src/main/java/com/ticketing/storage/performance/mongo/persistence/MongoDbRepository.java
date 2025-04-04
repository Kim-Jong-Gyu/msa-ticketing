package com.ticketing.storage.performance.mongo.persistence;

import java.util.Optional;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface MongoDbRepository extends ReactiveCrudRepository<PerformanceEntity, String> {

	Mono<PerformanceEntity> findByPerformanceId(Integer performanceId);

}
