package com.performance.microservices.core.reservation.services;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.performance.api.core.reservation.Reservation;
import com.performance.api.core.reservation.ReservedLine;
import com.performance.common.ReservationStatus;
import com.performance.storage.core.reservation.persistence.ReservationEntity;
import com.performance.storage.core.reservation.persistence.ReservedLineVO;

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

	ReservedLine reservedLineValueToReservedLine(ReservedLineVO reservedLineVO);

	ReservedLineVO reservedLineToReservedLineValue(ReservedLine reservedLine);

	ReservationStatus stringStatusToEnumStatus(String status);

	String enumStatusToStringStatus(ReservationStatus reservationStatus);


}
