package com.performance.microservices.core.hall;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.performance.api.core.hall.Hall;
import com.performance.api.core.hall.Seat;
import com.performance.microservices.core.hall.services.HallMapper;
import com.performance.storage.hall.mysql.HallEntity;
import com.ticketing.performance.common.SeatType;

public class MapperTests {

	private HallMapper mapper = Mappers.getMapper(HallMapper.class);


	@Test
	public void mapperTest(){

		assertNotNull(mapper);

		Integer hallId = 1;
		String hallName = "name";
		List<Seat> seatList = new ArrayList<>();
		char[] section = {'A', 'B', 'C', 'D'};
		for (char c : section) {
			for (int j = 1; j <= 10; j++) {
				seatList.add(new Seat(j, c, "STANDARD", true));
			}
		}
		Hall api = new Hall(hallId, hallName, seatList);

		HallEntity entity = mapper.apiToEntity(api);

		assertEquals(api.getHallId(), entity.getHallId());
		assertEquals(api.getHallName(), entity.getHallName());
		assertEquals(api.getSeatList().size(), entity.getSeatList().size());

	}
}
