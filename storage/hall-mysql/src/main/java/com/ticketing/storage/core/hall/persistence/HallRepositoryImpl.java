package com.ticketing.storage.core.hall.persistence;


import org.springframework.stereotype.Repository;

import com.ticketing.util.exceptions.NotFoundException;

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
	public HallEntity findByHallIdWithSeat(Integer hallId) {
		return hallJpaRepository.findByHallIdWithSeat(hallId).orElseThrow(
			() -> new NotFoundException("No hall found for hallId " + hallId)
		);
	}

	@Override
	public HallEntity save(HallEntity entity) {
		return hallJpaRepository.save(entity);
	}


	@Override
	public HallEntity findByHallIdWithUnavailableDateList(Integer hallId) {
		return hallJpaRepository.findByHallIdWithUnavailableDateList(hallId).orElseThrow(
			() -> new NotFoundException("No hall found for hallId " + hallId)
		);
	}

	@Override
	public HallEntity findByHallId(Integer hallId) {
		return hallJpaRepository.findByHallId(hallId).orElseThrow(
			() -> new NotFoundException("No hall found for hallId " + hallId)
		);
	}

	@Override
	public void delete(HallEntity hallEntity) {
		hallJpaRepository.delete(hallEntity);
	}

	@Override
	public boolean existsByHallId(Integer id) {
		return hallJpaRepository.existsByHallId(id);
	}

	public int count() {
		return Math.toIntExact(hallJpaRepository.count());
	}
}
