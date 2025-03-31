package com.ticketing.api.core.reservation;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface ReservationService {

	@GetMapping(
		value = "/reservation/{reservationId}",
		produces = "application/json")
	Reservation getReservation(@PathVariable Integer reservationId);

	@GetMapping(
		value = "/reservation/performance/{performanceId}",
		produces = "application/json")
	List<ReservationSeat> getReservationSeatListByPerformanceId(
		@PathVariable Integer performanceId
	);

	@PostMapping(
		value = "/reservation",
		consumes = "application/json",
		produces = "application/json")
	Reservation createReservation(@RequestBody Reservation body);

	@DeleteMapping(value = "/reservation/clean-up")
	void deleteAllReservation();
}
