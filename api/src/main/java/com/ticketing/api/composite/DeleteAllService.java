package com.ticketing.api.composite;

import org.springframework.web.bind.annotation.DeleteMapping;

public interface DeleteAllService {

	@DeleteMapping(value = "/composite/clean-up")
	void deleteAll();
}
