package com.ticketing.storage.core.hall.persistence;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HallJpaRepository extends JpaRepository<HallEntity, Integer> {

	@Query("SELECT h FROM HallEntity h LEFT JOIN FETCH h.seatList WHERE h.hallId = :id")
	Optional<HallEntity> findByHallIdWithSeat(@Param("id") Integer id);

	@Query("SELECT h FROM HallEntity h LEFT JOIN FETCH h.unavailableDateList WHERE h.hallId = :hallId")
	Optional<HallEntity> findByHallIdWithUnavailableDateList(@Param("hallId") Integer hallId);

	Optional<HallEntity> findByHallId(Integer hallId);

	boolean existsByHallId(Integer id);

}
