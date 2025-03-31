package com.ticketing.api.core.performance;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
	Performance getPerformance(@PathVariable Integer performanceId);

	@GetMapping(
		value = "/performance/schedule/{performanceId}",
		produces = "application/json")
	List<Schedule> getPerformanceSchedule(@PathVariable Integer performanceId);


	@PostMapping(
		value = "/performance",
		consumes = "application/json",
		produces = "application/json")
	Performance createPerformance(@RequestBody Performance performance);

	@DeleteMapping(value = "/performance/clean-up")
	void deleteAllPerformance();

}
