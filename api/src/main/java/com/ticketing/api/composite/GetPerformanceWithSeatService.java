package com.ticketing.api.composite;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "composite REST API", description = "REST API for composite information.")
public interface GetPerformanceWithSeatService {

	@Operation(
		summary = "${api.composite.get-performance-with-seat.description}",
		description = "${api.composite.get-performance-with-seat.notes}"
	)
	@ApiResponses(
		value = {
			@ApiResponse(responseCode = "400", description = "Bad Request, invalid format of the request. See response message for more information."),
			@ApiResponse(responseCode = "404", description = "Not found, the specified id does not exist."),
			@ApiResponse(responseCode = "422", description = "Unprocessable entity, input parameters caused the processing to fails. See response message for more information.")
		})
	@GetMapping(
		value = "/composite/performance-seat/{performanceId}",
		produces = "application/json")
	PerformanceWithSeat getPerformanceWithSeat(@PathVariable("performanceId") Integer performanceId);
}
