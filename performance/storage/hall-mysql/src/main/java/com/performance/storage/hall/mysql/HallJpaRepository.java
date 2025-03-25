package com.performance.storage.hall.mysql;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


public interface HallJpaRepository extends JpaRepository<HallEntity, Integer> {

	@Query("SELECT h FROM HallEntity h LEFT JOIN FETCH h.seatList WHERE h.hallId = :id")
	Optional<HallEntity> findByHallId(@Param("id") Integer id);

}
