package com.ticketing.storage.performance.mongo.persistence;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleVo {

	private Integer hallId;

	private LocalDateTime performanceDate;

	public ScheduleVo(Integer hallId, LocalDateTime performanceDate) {
		this.hallId = hallId;
		this.performanceDate = performanceDate;
	}

}
