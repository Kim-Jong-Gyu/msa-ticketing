package com.performance.storage.core.reservation.persistence;

import java.time.LocalDateTime;
import java.util.List;


public interface ReservationRepository {

	ReservationEntity findByReservationId(Integer reservationId);

	ReservationEntity save(ReservationEntity entity);

	List<ReservationEntity> findByPerformanceId(Integer performanceId);

	List<ReservationEntity> findByPerformanceIdAndHallIdAndPerformanceDate(Integer performanceId, Integer hallId,
		LocalDateTime performanceDate);

	void deleteAll();

	void delete(ReservationEntity entity);

}
