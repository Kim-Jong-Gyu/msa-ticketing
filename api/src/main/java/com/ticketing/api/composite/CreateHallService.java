package com.ticketing.api.composite;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ticketing.api.core.hall.Hall;
import com.ticketing.api.core.performance.Performance;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "composite REST API", description = "REST API for composite information.")
public interface CreateHallService {

	@Operation(
		summary = "${api.composite.create-hall.description}",
		description = "${api.composite.create-hall.notes}"
	)
	@ApiResponses(
		value = {
			@ApiResponse(responseCode = "400", description = "Bad Request, invalid format of the request. See response message for more information."),
			@ApiResponse(responseCode = "422", description = "Unprocessable entity, input parameters caused the processing to fails. See response message for more information.")
		})
	@PostMapping(
		value = "/composite/hall",
		consumes = "application/json",
		produces = "application/json")
	CreateHall createHall(@RequestBody Hall body);

}
