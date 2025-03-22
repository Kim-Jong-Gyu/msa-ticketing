package com.performance.api.composite.performance;

import java.util.List;

public record ScheduleSummaryWithSeats(
	ScheduleSummary scheduleSummary,
	List<PerformanceSeat> performanceSeatList

) {
}
