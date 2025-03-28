package com.ticketing.microservices.composite.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.ticketing.api.composite.CreateHall;
import com.ticketing.api.composite.CreateHallService;
import com.ticketing.api.composite.ServiceAddresses;
import com.ticketing.api.core.hall.Hall;
import com.ticketing.util.http.ServiceUtil;

@RestController
public class CreateHallServiceImpl implements CreateHallService {

	private static final Logger LOG = LoggerFactory.getLogger(CreateHallServiceImpl.class);

	private final ServiceUtil serviceUtil;

	private final CompositeIntegration integration;

	@Autowired
	public CreateHallServiceImpl(ServiceUtil serviceUtil, CompositeIntegration integration) {
		this.serviceUtil = serviceUtil;
		this.integration = integration;
	}

	@Override
	public CreateHall createHall(Hall body) {
		Hall hall = integration.createHall(body);
		return new CreateHall(hall.getHallId(),
			new ServiceAddresses(serviceUtil.getServiceAddress(), hall.getServiceAddress(), null, null));
	}
}
