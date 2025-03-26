package com.performance.storage.hall.mysql.persistence;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HallJpaRepository extends JpaRepository<HallEntity, Integer> {

	@Query("SELECT h FROM HallEntity h LEFT JOIN FETCH h.seatList WHERE h.hallId = :id")
	Optional<HallEntity> findByHallId(@Param("id") Integer id);

}
