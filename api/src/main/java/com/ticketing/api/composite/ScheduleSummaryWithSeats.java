package com.ticketing.api.composite;

import java.time.LocalDateTime;
import java.util.List;

public record ScheduleSummaryWithSeats(

	LocalDateTime performanceDate,

	Integer hallId,

	String hallName,

	List<PerformanceSeat> performanceSeatList

) {
}
