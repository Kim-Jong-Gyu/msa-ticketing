package com.ticketing.microservices.composite.services;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.ticketing.api.composite.CreateReservation;
import com.ticketing.api.composite.CreateReservationService;
import com.ticketing.api.composite.ServiceAddresses;
import com.ticketing.api.core.performance.Performance;
import com.ticketing.api.core.reservation.Reservation;
import com.ticketing.util.exceptions.NotFoundException;
import com.ticketing.util.http.ServiceUtil;

@RestController
public class CreateReservationServiceImpl implements CreateReservationService {

	private static final Logger LOG = LoggerFactory.getLogger(CreateReservationServiceImpl.class);

	private final ServiceUtil serviceUtil;

	private final CompositeIntegration integration;

	@Autowired
	public CreateReservationServiceImpl(ServiceUtil serviceUtil, CompositeIntegration integration) {
		this.serviceUtil = serviceUtil;
		this.integration = integration;
	}

	@Override
	public CreateReservation createReservation(Reservation body) {
		List<String> performanceAddress = body.getReservedLineList().stream().map(reservedLine -> {
			Performance performance = integration.getPerformance(reservedLine.getPerformanceId());
			if (performance == null) {
				throw new NotFoundException(
					"No performance found for performanceId: " + reservedLine.getPerformanceId());
			}
			boolean hallExists = performance.getScheduleList().stream()
				.anyMatch(schedule -> Objects.equals(schedule.getHallId(), reservedLine.getHallId()));

			if (!hallExists) {
				throw new NotFoundException("No matching hallId found for hallId: " + reservedLine.getHallId()
					+ " in performanceId: " + reservedLine.getPerformanceId());
			}
			return performance.getServiceAddress();
		}).toList();

		LOG.info("Success verify performanceId with hallId for this reservation " + body.getReservationId());

		Reservation response = integration.createReservation(body);

		ServiceAddresses serviceAddresses = new ServiceAddresses(
			serviceUtil.getServiceAddress(),
			null,
			performanceAddress.get(0),
			null);

		LOG.info("Success create a reservation : " + body.getReservationId());
		return new CreateReservation(response.getReservationId(), serviceAddresses);
	}
}
