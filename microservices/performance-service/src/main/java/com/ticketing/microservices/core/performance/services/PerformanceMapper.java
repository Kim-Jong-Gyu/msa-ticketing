package com.ticketing.microservices.core.performance.services;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.ticketing.api.core.performance.Performance;
import com.ticketing.api.core.performance.PricePolicy;
import com.ticketing.api.core.performance.Schedule;
import com.ticketing.storage.performance.mongo.persistence.PerformanceEntity;
import com.ticketing.storage.performance.mongo.persistence.PricePolicyVo;
import com.ticketing.storage.performance.mongo.persistence.ScheduleVo;
import com.ticketing.common.SeatType;

@Mapper(componentModel = "spring")
public interface PerformanceMapper {


	// PerformanceEntity에 serviceAddress가 없으므로 무시
	@Mappings({
		@Mapping(target = "serviceAddress", ignore = true),
	})
	Performance entityToApi(PerformanceEntity entity);


	// Performance에 id, version이 없으므로 제거
	@Mappings({
		@Mapping(target = "id", ignore = true),
		@Mapping(target = "version", ignore = true)
	})
	PerformanceEntity apiToEntity(Performance performance);

	PricePolicy pricePolicyVoToPricePolicy(PricePolicyVo pricePolicyVO);

	PricePolicyVo pricePolicyToPricePolicyVo(PricePolicy pricePolicy);

	ScheduleVo scheduleToScheduleVo(Schedule schedule);

	Schedule scheduleVoToSchedule(ScheduleVo scheduleVo);

	default String toSeatType(SeatType seatType) {
		return seatType != null ? seatType.name() : null;
	}

	default SeatType toSeatType(String seatType) {
		return seatType != null ? SeatType.valueOf(seatType) : null;
	}

}
