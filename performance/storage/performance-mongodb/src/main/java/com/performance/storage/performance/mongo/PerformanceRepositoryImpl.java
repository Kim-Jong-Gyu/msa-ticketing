package com.performance.storage.performance.mongo;

import java.util.Optional;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import com.performance.util.exceptions.InvalidInputException;
import com.performance.util.exceptions.NotFoundException;

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
			PerformanceEntity ret = mongoDbRepository.save(performanceEntity);
			return ret;
		}
		catch (DuplicateKeyException dao){
			throw new InvalidInputException("Duplicate key, Performance Id : " + performanceEntity.getPerformanceId());
		}
	}

	@Override
	public void deleteAll() {
		mongoDbRepository.deleteAll();
	}

}
