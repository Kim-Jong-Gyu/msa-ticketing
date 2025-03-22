package com.performance.api.composite.performance;

import java.util.List;
import java.util.Map;

public record PerformanceAggregate(

	Integer performanceId,

	List<ScheduleSummaryWithSeats> scheduleSummaryWithSeatsList,
	ServiceAddresses serviceAddresses
) {
}