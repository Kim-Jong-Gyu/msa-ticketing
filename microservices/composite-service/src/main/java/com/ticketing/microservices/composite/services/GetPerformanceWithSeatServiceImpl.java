package com.ticketing.microservices.composite.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.ticketing.api.composite.GetPerformanceWithSeatService;
import com.ticketing.api.composite.PerformanceWithSeat;

import com.ticketing.api.composite.PerformanceSeat;
import com.ticketing.api.composite.ScheduleSummaryWithSeats;
import com.ticketing.api.composite.ServiceAddresses;
import com.ticketing.api.core.hall.HallWithSeat;
import com.ticketing.api.core.hall.Seat;
import com.ticketing.api.core.performance.Performance;
import com.ticketing.api.core.performance.PricePolicy;
import com.ticketing.api.core.performance.Schedule;
import com.ticketing.api.core.reservation.ReservationSeat;
import com.ticketing.util.exceptions.NotFoundException;
import com.ticketing.util.http.ServiceUtil;

@RestController
public class GetPerformanceWithSeatServiceImpl implements GetPerformanceWithSeatService {

	private static final Logger LOG = LoggerFactory.getLogger(GetPerformanceWithSeatServiceImpl.class);

	private final ServiceUtil serviceUtil;

	private final CompositeIntegration integration;

	@Autowired
	public GetPerformanceWithSeatServiceImpl(ServiceUtil serviceUtil, CompositeIntegration integration) {
		this.serviceUtil = serviceUtil;
		this.integration = integration;
	}

	@Override
	public PerformanceWithSeat getPerformanceWithSeat(Integer performanceId) {

		// Performance
		Performance performance = integration.getPerformance(performanceId);
		if (performance == null)
			throw new NotFoundException("No performance found for performanceId: " + performanceId);

		// Hall By Schedule
		List<HallWithSeat> hallList = performance.getScheduleList().stream()
			.map(schedule -> {
				HallWithSeat hall = integration.getHallWithSeat(schedule.getHallId());
				if (hall == null) {
					throw new NotFoundException("No hall found for hallId: " + schedule.getHallId());
				}
				return hall;
			})
			.collect(Collectors.toList());

		// Reservation Seat By PerformanceId
		List<ReservationSeat> reservationSeatList = integration.getReservationSeatListByPerformanceId(performanceId);
		if (reservationSeatList == null)
			LOG.info("No reservation found for performanceId: " + performanceId);
		return createPerformanceAggregate(performance, hallList, reservationSeatList, serviceUtil.getServiceAddress());
	}


	private PerformanceWithSeat createPerformanceAggregate(Performance performance, List<HallWithSeat> hallList,
		List<ReservationSeat> reservationSeatList, String serviceAddress) {

		// return value
		List<ScheduleSummaryWithSeats> scheduleSummaryWithSeat = IntStream.range(0, performance.getScheduleList().size())
			.mapToObj(i -> createScheduleSummary(performance.getScheduleList().get(i), hallList.get(i), reservationSeatList, performance.getPricePolicies()))
			.collect(Collectors.toList());

		ServiceAddresses serviceAddresses = new ServiceAddresses(
			serviceAddress,
			hallList.stream().findFirst().map(HallWithSeat::getServiceAddress).orElse(""),
			performance.getServiceAddress(),
			reservationSeatList != null ? reservationSeatList.stream().findFirst().map(ReservationSeat::getServiceAddress).orElse("") : ""
		);

		return new PerformanceWithSeat(performance.getPerformanceId(), scheduleSummaryWithSeat, serviceAddresses);
	}

	private ScheduleSummaryWithSeats createScheduleSummary(Schedule schedule, HallWithSeat hall,
		List<ReservationSeat> reservationSeatList,
		List<PricePolicy> pricePolicies) {

		List<PerformanceSeat> performanceSeats = hall.getSeatList().stream()
			.map(seat -> new PerformanceSeat(
				seat.getSeatNumber(),
				seat.getSection(),
				reservationSeatList == null || isAvailableSeat(schedule.getPerformanceDate(), reservationSeatList, seat),
				selectPrice(pricePolicies, seat)
			))
			.collect(Collectors.toList());

		return new ScheduleSummaryWithSeats(
			schedule.getPerformanceDate(),
			hall.getHallId(),
			hall.getHallName(),
			performanceSeats
		);
	}

	private Boolean isAvailableSeat(LocalDateTime performanceDate, List<ReservationSeat> reservationSeatList, Seat seat) {
		return reservationSeatList.stream()
			.noneMatch(reservationSeat -> Objects.equals(reservationSeat.getSeatNumber(), seat.getSeatNumber())
				&& Objects.equals(reservationSeat.getSection(), seat.getSection())
				&& Objects.equals(reservationSeat.getPerformanceDate(), performanceDate));
	}

	private Integer selectPrice(List<PricePolicy> pricePolicies, Seat seat) {
		return pricePolicies.stream()
			.filter(pricePolicy -> pricePolicy.getSeatType().equals(seat.getType()))
			.map(PricePolicy::getPrice)
			.findFirst()
			.orElse(null);
	}
}
