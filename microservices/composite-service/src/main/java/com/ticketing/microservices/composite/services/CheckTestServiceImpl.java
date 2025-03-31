package com.ticketing.microservices.composite.services;

import org.springframework.web.bind.annotation.RestController;

import com.ticketing.api.composite.CheckTestService;

@RestController
public class CheckTestServiceImpl implements CheckTestService {

	@Override
	public String checkServerTest() {
		return "Service start!!";
	}
}
