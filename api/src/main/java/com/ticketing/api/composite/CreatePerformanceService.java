package com.ticketing.api.composite;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ticketing.api.core.performance.Performance;
import com.ticketing.api.core.reservation.Reservation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface CreatePerformanceService {

	@Operation(
		summary = "${api.composite.create-performance.description}",
		description = "${api.composite.create-performance.notes}"
	)
	@ApiResponses(
		value = {
			@ApiResponse(responseCode = "400", description = "Bad Request, invalid format of the request. See response message for more information."),
			@ApiResponse(responseCode = "404", description = "Not found, the specified id does not exist."),
			@ApiResponse(responseCode = "422", description = "Unprocessable entity, input parameters caused the processing to fails. See response message for more information.")
		})
	@PostMapping(
		value = "/composite/performance",
		consumes = "application/json",
		produces = "application/json")
	CreatePerformance createPerformance(@RequestBody Performance body);

}
