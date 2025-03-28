package com.ticketing.api.composite;

import java.util.List;

public record PerformanceWithSeat(

	Integer performanceId,

	List<ScheduleSummaryWithSeats> scheduleSummaryWithSeatsList,
	ServiceAddresses serviceAddresses
) {
}