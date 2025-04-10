package com.ticketing.api.core.hall;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

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
	Mono<HallWithSeat> getHallWithSeat(@PathVariable("hallId") Integer hallId);


	@PostMapping(
		value = "/hall",
		consumes = "application/json",
		produces = "application/json")
	Hall createHall(@RequestBody Hall hall);


	@GetMapping(
		value = "/hall/unavailable/{hallId}",
		produces = "application/json")
	Mono<HallWithUnavailable> getHallWithUnavailableList(@PathVariable("hallId") Integer hallId);

	@DeleteMapping(value = "/hall/clean-up")
	void deleteAllHall();
}
