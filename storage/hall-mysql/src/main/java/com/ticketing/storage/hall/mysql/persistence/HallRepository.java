package com.ticketing.storage.hall.mysql.persistence;

public interface HallRepository {


	void deleteAll();
	HallEntity findByHallIdWithSeat(Integer hallId);

	HallEntity save(HallEntity entity);

	HallEntity findByHallIdWithUnavailableDateList(Integer hallId);

	HallEntity findByHallId(Integer hallId);

	void delete(HallEntity hallEntity);

	boolean existsByHallId(Integer hallId);

	int count();
}
