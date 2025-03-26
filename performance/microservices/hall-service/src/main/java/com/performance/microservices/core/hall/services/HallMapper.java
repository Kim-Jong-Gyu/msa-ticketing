package com.performance.microservices.core.hall.services;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.performance.api.core.hall.Hall;
import com.performance.api.core.hall.Seat;
import com.performance.storage.hall.mysql.persistence.HallEntity;
import com.performance.storage.hall.mysql.persistence.SeatVO;
import com.performance.common.SeatType;

@Mapper(componentModel = "spring")
public interface HallMapper {


	@Mappings({
		@Mapping(target = "serviceAddress", ignore = true)
	})
	Hall entityToApi(HallEntity entity);


	@Mappings({
		@Mapping(target = "id", ignore = true),
		@Mapping(target = "version", ignore = true)
	})
	HallEntity apiToEntity(Hall hall);

	Seat seatVotoSeat(SeatVO seatVO);

	SeatVO seatToSeatVo(Seat seat);

	List<SeatVO> seatsToSeatVOs(List<Seat> seats);
	List<Seat> seatVOsToSeats(List<SeatVO> seatVOs);

	String entityToDto(SeatType type);
	SeatType dtoToEntity(String dto);
}
