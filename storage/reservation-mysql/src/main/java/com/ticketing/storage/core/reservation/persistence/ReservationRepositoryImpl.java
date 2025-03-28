package com.ticketing.storage.core.reservation.persistence;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ticketing.util.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepository{

	private final ReservationJpaRepository repository;

	@Override
	public ReservationEntity findByReservationId(Integer reservationId) {
		return repository.findByReservationId(reservationId).orElseThrow(
			() -> new NotFoundException("No reservation found for reservationId: " + reservationId)
		);
	}

	@Override
	public ReservationEntity save(ReservationEntity entity) {
		return repository.save(entity);
	}

	@Override
	public List<ReservationEntity> findByPerformanceId(Integer performanceId) {
		return repository.findByPerformanceId(performanceId);
	}

	@Override
	public List<ReservationEntity> findByPerformanceIdAndHallIdAndPerformanceDate(Integer performanceId, Integer hallId,
		LocalDateTime performanceDate) {
		return repository.findByPerformanceIdAndHallIdAndPerformanceDate(performanceId, hallId, performanceDate);
	}

	@Override
	public void deleteAll() {
		repository.deleteAll();
	}

	@Override
	public void delete(ReservationEntity entity) {
		repository.delete(entity);
	}
}
