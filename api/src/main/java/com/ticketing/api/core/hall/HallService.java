package com.ticketing.api.core.hall;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface HallService {

	/**
	 * Sample usage: curl $HOST:$PORT/hall/1
	 *
	 * @Param hallId
	 * @return the GetHallResponse, if found, else null
	 */
	@GetMapping(
		value = "/hall/seat/{hallId}",
		produces = "application/json")
	HallWithSeat getHallWithSeat(@PathVariable Integer hallId);


	@PostMapping(
		value = "/hall",
		consumes = "application/json",
		produces = "application/json")
	Hall createHall(@RequestBody Hall hall);


	@GetMapping(
		value = "/hall/unavailable/{hallId}",
		produces = "application/json")
	HallWithUnavailable getHallWithUnavailableList(@PathVariable Integer hallId);

}
