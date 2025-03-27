package com.performance.storage.core.reservation.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Integer> {

	@Query("select r from ReservationEntity r "
		+ "left join fetch r.reservedLineList l "
		+ "where r.performanceId = :performanceId and l.hallId = :hallId and l.performanceDate = :performanceDate")
	List<ReservationEntity> findByPerformanceIdAndHallIdAndPerformanceDate(
		@Param("performanceId")Integer performanceId,
		@Param("hallId") Integer hallId,
		@Param("performanceDate") LocalDateTime performanceDate
	);

	Optional<ReservationEntity> findByReservationId(Integer reservationId);

	List<ReservationEntity> findByPerformanceId(Integer performanceId);
}
