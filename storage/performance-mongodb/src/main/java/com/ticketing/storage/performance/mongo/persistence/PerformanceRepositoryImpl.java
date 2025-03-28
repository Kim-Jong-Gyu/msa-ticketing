package com.ticketing.storage.performance.mongo.persistence;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import com.ticketing.util.exceptions.InvalidInputException;
import com.ticketing.util.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PerformanceRepositoryImpl implements PerformanceRepository{

	private final MongoDbRepository mongoDbRepository;

	@Override
	public PerformanceEntity findByPerformanceId(Integer performanceId) {
		return mongoDbRepository.findByPerformanceId(performanceId).orElseThrow(
			() -> new NotFoundException("No Performance found for performanceId: " + performanceId)
		);
	}

	@Override
	public PerformanceEntity save(PerformanceEntity performanceEntity) {
		try{
			return mongoDbRepository.save(performanceEntity);
		}
		catch (DuplicateKeyException dao){
			throw new InvalidInputException("Duplicate key, Performance Id : " + performanceEntity.getPerformanceId());
		}
	}

	@Override
	public void deleteAll() {
		mongoDbRepository.deleteAll();
	}

	@Override
	public Integer count() {
		return Math.toIntExact(mongoDbRepository.count());
	}

	@Override
	public void delete(PerformanceEntity entity) {
		mongoDbRepository.delete(entity);
	}

	@Override
	public boolean existsById(String id) {
		return mongoDbRepository.existsById(id);
	}

}
