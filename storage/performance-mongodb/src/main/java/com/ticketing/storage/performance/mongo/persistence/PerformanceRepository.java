package com.ticketing.storage.performance.mongo.persistence;


import reactor.core.publisher.Mono;

public interface PerformanceRepository {

	Mono<PerformanceEntity> findByPerformanceId(Integer performanceId);

	PerformanceEntity save(PerformanceEntity performanceEntity);

	void deleteAll();

	Integer count();

	void delete(PerformanceEntity entity);

	Mono<Boolean> existsById(String id);
}
