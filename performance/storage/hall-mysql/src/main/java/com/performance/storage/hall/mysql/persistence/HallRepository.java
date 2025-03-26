package com.performance.storage.hall.mysql.persistence;

public interface HallRepository {


	void deleteAll();
	HallEntity findByHallId(Integer hallId);

	HallEntity save(HallEntity entity);

}
