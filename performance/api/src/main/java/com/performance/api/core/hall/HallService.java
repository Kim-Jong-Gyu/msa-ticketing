package com.performance.api.core.hall;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface HallService {

	/**
	 * Sample usage: curl $HOST:$PORT/hall/1
	 *
	 * @Param hallId
	 * @return the GetHallResponse, if found, else null
	 */
	@GetMapping(
		value = "/hall/{hallId}",
		produces = "application/json")
	Hall getHall(@PathVariable int hallId);
}
