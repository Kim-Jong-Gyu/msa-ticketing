package com.performance.api.core.performance;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.performance.api.enums.SeatType;

public record Performance(
	Integer performanceId,
	String title,

	Map<SeatType, Integer> pricePolicies,

	LocalDateTime bookingStartDate,

	LocalDateTime bookingEndDate,

	List<Schedule> scheduleList,

	String serviceAddress

) {
}
