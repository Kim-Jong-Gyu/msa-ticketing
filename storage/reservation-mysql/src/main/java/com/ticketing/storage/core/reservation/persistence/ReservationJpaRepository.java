package com.ticketing.storage.core.reservation.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Integer> {

	@Query("select r from ReservationEntity r "
		+ "left join fetch r.reservedLineList l "
		+ "where l.performanceId = :performanceId and l.hallId = :hallId and l.performanceDate = :performanceDate")
	List<ReservationEntity> findByPerformanceIdAndHallIdAndPerformanceDate(
		@Param("performanceId")Integer performanceId,
		@Param("hallId") Integer hallId,
		@Param("performanceDate") LocalDateTime performanceDate
	);

	@Query("select r from ReservationEntity r left join fetch r.reservedLineList where r.reservationId = :reservationId")
	Optional<ReservationEntity> findByReservationId(@Param("reservationId") Integer reservationId);

	@Query("select distinct r from ReservationEntity r "
		+ "join fetch r.reservedLineList l "
		+ "where l.performanceId = :performanceId and r.reservationStatus <> 'CANCELLED'")
	List<ReservationEntity> findByPerformanceId(@Param("performanceId") Integer performanceId);
}
