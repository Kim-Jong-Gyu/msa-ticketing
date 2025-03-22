package com.performance.api.core.performance;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface PerformanceService {

	/**
	 * Sample usage: curl $HOST:$PORT/performance/1
	 *
	 * @param performanceId
	 * @return the performance, if found, else null
	 */
	@GetMapping(
		value = "/performance/{performanceId}",
		produces = "application/json")
	Performance getPerformance(@PathVariable int performanceId);
}
