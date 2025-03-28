package com.ticketing.api.composite;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ticketing.api.core.reservation.Reservation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "composite REST API", description = "REST API for composite information.")
public interface CreateReservationService {


	@Operation(
		summary = "${api.composite.create-reservation.description}",
		description = "${api.composite.create-reservation.notes}"
	)
	@ApiResponses(
		value = {
			@ApiResponse(responseCode = "400", description = "Bad Request, invalid format of the request. See response message for more information."),
			@ApiResponse(responseCode = "404", description = "Not found, the specified id does not exist."),
			@ApiResponse(responseCode = "422", description = "Unprocessable entity, input parameters caused the processing to fails. See response message for more information.")
		})
	@PostMapping(
		value = "/composite/reservation",
		consumes = "application/json",
		produces = "application/json")
	CreateReservation createReservation(@RequestBody Reservation body);

}
