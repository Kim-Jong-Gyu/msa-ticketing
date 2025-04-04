package com.ticketing.storage.performance.mongo.persistence;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import com.ticketing.util.exceptions.InvalidInputException;
import com.ticketing.util.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.error;

@Repository
@RequiredArgsConstructor
public class PerformanceRepositoryImpl implements PerformanceRepository {

    private final MongoDbRepository mongoDbRepository;

    @Override
    public Mono<PerformanceEntity> findByPerformanceId(Integer performanceId) {
        return mongoDbRepository.findByPerformanceId(performanceId)
				.switchIfEmpty(error(new NotFoundException("No Performance found for performanceOd: " + performanceId)))
				.log();
    }

    @Override
    public PerformanceEntity save(PerformanceEntity performanceEntity) {
        return mongoDbRepository.save(performanceEntity)
				.log()
                .onErrorMap(DuplicateKeyException.class,
                        ex -> new InvalidInputException("Duplicate key, Performance Id: " + performanceEntity.getPerformanceId()))
                .block();
    }

    @Override
    public void deleteAll() {
        mongoDbRepository.deleteAll()
				.log()
				.block();
    }

    @Override
    public Integer count() {
        return mongoDbRepository.count()
				.log()
				.map(Math::toIntExact)
				.block();
    }

    @Override
    public void delete(PerformanceEntity entity) {
        mongoDbRepository.delete(entity)
				.log()
				.block();
    }

    @Override
    public Mono<Boolean> existsById(String id) {
        return mongoDbRepository.existsById(id)
				.log();
    }
}
