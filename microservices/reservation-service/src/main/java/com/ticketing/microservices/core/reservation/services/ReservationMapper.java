package com.ticketing.microservices.core.reservation.services;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.ticketing.api.core.reservation.Reservation;
import com.ticketing.api.core.reservation.ReservationSeat;
import com.ticketing.api.core.reservation.ReservedLine;
import com.ticketing.common.ReservationStatus;
import com.ticketing.storage.core.reservation.persistence.ReservationEntity;
import com.ticketing.storage.core.reservation.persistence.ReservedLineVo;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

	@Mappings({
		@Mapping(target = "serviceAddress", ignore = true),
		@Mapping(target = "reservationStatus", source = "entity.reservationStatus")
	})
	Reservation entityToApi(ReservationEntity entity);


	@Mappings({
		@Mapping(target = "id", ignore = true),
		@Mapping(target = "version", ignore = true),
		@Mapping(target = "reservationStatus", source = "reservation.reservationStatus")
	})
	ReservationEntity apiToEntity(Reservation reservation);

	List<Reservation> entityListToApiList(List<ReservationEntity> entity);

	List<ReservationEntity> apiListToEntityList(List<Reservation> api);

	ReservedLine reservedLineValueToReservedLine(ReservedLineVo reservedLineVO);

	ReservedLineVo reservedLineToReservedLineValue(ReservedLine reservedLine);

	ReservationStatus stringStatusToEnumStatus(String status);

	String enumStatusToStringStatus(ReservationStatus reservationStatus);

	@Mappings({
		@Mapping(target = "serviceAddress", ignore = true),
	})
	ReservationSeat reservedLineVoToReservationSeat(ReservedLineVo reservedLineVO);

}
