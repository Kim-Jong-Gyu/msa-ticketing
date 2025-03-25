package com.performance.storage.hall.mysql;

import java.util.Optional;

import com.performance.storage.hall.mysql.HallEntity;

public interface HallRepository {


	void deleteAll();
	HallEntity findByHallId(Integer hallId);

	HallEntity save(HallEntity entity);

}
