package com.ticketing.microservices.composite.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.ticketing.api.composite.DeleteAllService;
import com.ticketing.util.http.ServiceUtil;

@RestController
public class DeleteAllServiceImpl implements DeleteAllService {

	private static final Logger LOG = LoggerFactory.getLogger(DeleteAllServiceImpl.class);

	private final ServiceUtil serviceUtil;

	private final CompositeIntegration integration;

	@Autowired
	public DeleteAllServiceImpl(ServiceUtil serviceUtil, CompositeIntegration integration) {
		this.serviceUtil = serviceUtil;
		this.integration = integration;
	}

	@Override
	public void deleteAll() {
		integration.deleteAllReservation();
		integration.deleteAllPerformance();
		integration.deleteAllHall();
		LOG.debug("complete delete all data");
	}
}
