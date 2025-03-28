package com.ticketing.microservices.composite.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.ticketing.api.composite.CreatePerformance;
import com.ticketing.api.composite.CreatePerformanceService;
import com.ticketing.api.composite.ServiceAddresses;
import com.ticketing.api.core.hall.HallWithUnavailable;
import com.ticketing.api.core.performance.Performance;
import com.ticketing.util.exceptions.InvalidInputException;
import com.ticketing.util.exceptions.NotFoundException;
import com.ticketing.util.http.ServiceUtil;

@RestController
public class CreatePerformanceServiceImpl implements CreatePerformanceService {

	private static final Logger LOG = LoggerFactory.getLogger(CreatePerformanceServiceImpl.class);

	private final ServiceUtil serviceUtil;

	private final CompositeIntegration integration;

	@Autowired
	public CreatePerformanceServiceImpl(ServiceUtil serviceUtil, CompositeIntegration integration) {
		this.serviceUtil = serviceUtil;
		this.integration = integration;
	}

	@Override
	public CreatePerformance createPerformance(Performance body) {
		List<String> hallAddress = body.getScheduleList().stream().map(schedule -> {
			HallWithUnavailable hall = integration.getHallWithUnavailableList(schedule.getHallId());
			if (hall == null) {
				throw new NotFoundException(
					"No hall found for hallId: " + schedule.getHallId());
			}
			if (hall.getUnavailableDateList().stream().anyMatch(date -> date.equals(schedule.getPerformanceDate()))) {
				throw new InvalidInputException(
					"Already existed at schedule in hall: " + schedule.getPerformanceDate());
			}
			return hall.getServiceAddress();
		}).toList();

		LOG.info("Success verify hallId for this performance " + body.getPerformanceId());
		Performance performance = integration.createPerformance(body);

		return new CreatePerformance(performance.getPerformanceId(), new ServiceAddresses(
			serviceUtil.getServiceAddress(),
			hallAddress.get(0),
			performance.getServiceAddress(),
			null
		));
	}
}
