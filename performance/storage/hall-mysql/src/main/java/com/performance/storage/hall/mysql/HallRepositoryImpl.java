package com.performance.storage.hall.mysql;


import org.springframework.stereotype.Repository;

import com.performance.util.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class HallRepositoryImpl implements HallRepository {

	private final HallJpaRepository hallJpaRepository;

	@Override
	public void deleteAll() {
		hallJpaRepository.deleteAll();
	}

	@Override
	public HallEntity findByHallId(Integer hallId) {
		return hallJpaRepository.findByHallId(hallId).orElseThrow(
			() -> new NotFoundException("No hall found for hallId " + hallId)
		);
	}

	@Override
	public HallEntity save(HallEntity entity) {
		return hallJpaRepository.save(entity);
	}

}
