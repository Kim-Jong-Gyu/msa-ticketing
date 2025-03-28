package com.ticketing.storage.performance.mongo.persistence;


public interface PerformanceRepository {

	PerformanceEntity findByPerformanceId(Integer performanceId);

	PerformanceEntity save(PerformanceEntity performanceEntity);

	void deleteAll();

	Integer count();

	void delete(PerformanceEntity entity);

	boolean existsById(String id);
}
