package com.performance.api.composite.performance;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface PerformanceCompositeService {

	/**
	 * Sample usage: curl $HOST:$PORT/performance-composite/1
	 *
	 * @Param performanceId
	 * @return the composite performance info, if found, else null
	 */

	@GetMapping(
		value = "/performance-composite/performance/{performanceId}",
		produces = "application/json")
	PerformanceAggregate getPerformance(@PathVariable int performanceId);
}
