package com.ticketing.api.composite;

import org.springframework.web.bind.annotation.GetMapping;

public interface CheckTestService {

	@GetMapping(
		value = "/composite/test",
		produces = "application/json"
	)
	String checkServerTest();
}
