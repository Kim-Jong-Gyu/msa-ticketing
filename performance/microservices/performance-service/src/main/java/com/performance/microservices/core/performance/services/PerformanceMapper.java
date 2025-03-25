package com.performance.microservices.core.performance.services;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.performance.api.core.performance.Performance;
import com.performance.api.core.performance.Schedule;
import com.performance.storage.performance.mongo.PerformanceEntity;
import com.performance.storage.performance.mongo.ScheduleVo;
import com.ticketing.performance.common.SeatType;

@Mapper(componentModel = "spring")
public interface PerformanceMapper {

	// PerformanceEntity에 serviceAddress가 없으므로 무시
	@Mappings({
		@Mapping(target = "serviceAddress", ignore = true)
	})
	Performance entityToApi(PerformanceEntity entity);


	// Performance에 id, version이 없으므로 제거
	@Mappings({
		@Mapping(target = "id", ignore = true),
		@Mapping(target = "version", ignore = true)
	})
	PerformanceEntity apiToEntity(Performance performance);

	ScheduleVo scheduleToScheduleVo(Schedule schedule);

	Schedule scheduleVoToSchedule(ScheduleVo scheduleVo);

	String stringToEnum(SeatType type);
	SeatType enumToString(String dto);
}
