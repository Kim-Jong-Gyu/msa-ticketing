package com.ticketing.microservices.core.hall.services;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.ticketing.api.core.hall.Hall;
import com.ticketing.api.core.hall.HallWithSeat;
import com.ticketing.api.core.hall.HallWithUnavailable;
import com.ticketing.storage.core.hall.persistence.HallEntity;

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
}
