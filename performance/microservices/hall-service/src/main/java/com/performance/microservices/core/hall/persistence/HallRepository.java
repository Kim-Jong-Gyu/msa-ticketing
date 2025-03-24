package com.performance.microservices.core.hall.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface HallRepository extends JpaRepository<HallEntity, Integer> {

	@Transactional(readOnly = true)
	Optional<HallEntity> findByHallId(Integer hallId);

}
