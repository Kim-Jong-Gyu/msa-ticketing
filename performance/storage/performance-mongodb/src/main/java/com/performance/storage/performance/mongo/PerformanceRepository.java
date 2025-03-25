package com.performance.storage.performance.mongo;


public interface PerformanceRepository {

	PerformanceEntity findByPerformanceId(Integer performanceId);

	PerformanceEntity save(PerformanceEntity performanceEntity);

	void deleteAll();
}
