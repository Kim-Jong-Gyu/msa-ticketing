package com.ticketing.microservices.core.hall.services;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.ticketing.api.core.hall.Hall;
import com.ticketing.api.core.hall.HallWithSeat;
import com.ticketing.api.core.hall.HallWithUnavailable;
import com.ticketing.api.core.hall.Seat;
import com.ticketing.storage.hall.mysql.persistence.HallEntity;
import com.ticketing.storage.hall.mysql.persistence.SeatVO;
import com.ticketing.common.SeatType;


@Mapper(componentModel = "spring")
public interface HallMapper {

	@Mappings({
		@Mapping(target = "serviceAddress", ignore = true)
	})
	HallWithSeat entityToHallWithSeat(HallEntity entity);

	@Mappings({
		@Mapping(target = "serviceAddress", ignore = true)
	})
	HallWithUnavailable entityToHallWithUnavailable(HallEntity entity);

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
