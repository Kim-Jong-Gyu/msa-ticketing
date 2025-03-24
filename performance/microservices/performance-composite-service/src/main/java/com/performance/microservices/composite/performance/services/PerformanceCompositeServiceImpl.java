package com.performance.microservices.composite.performance.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.performance.api.composite.performance.PerformanceAggregate;
import com.performance.api.composite.performance.PerformanceCompositeService;
import com.performance.api.composite.performance.PerformanceSeat;
import com.performance.api.composite.performance.ScheduleSummary;
import com.performance.api.composite.performance.ScheduleSummaryWithSeats;
import com.performance.api.composite.performance.ServiceAddresses;
import com.performance.api.core.hall.Hall;
import com.performance.api.core.hall.Seat;
import com.performance.api.core.performance.Performance;
import com.performance.api.core.performance.ScheduleDto;
import com.performance.util.exceptions.NotFoundException;
import com.performance.util.http.ServiceUtil;

@RestController
public class PerformanceCompositeServiceImpl implements PerformanceCompositeService {

	private final ServiceUtil serviceUtil;

	private final PerformanceCompositeIntegration integration;


	@Autowired
	public PerformanceCompositeServiceImpl(ServiceUtil serviceUtil, PerformanceCompositeIntegration integration) {
		this.serviceUtil = serviceUtil;
		this.integration = integration;
	}

	@Override
	public PerformanceAggregate getPerformance(int performanceId) {

		Performance performance = integration.getPerformance(performanceId);
		if (performance == null)
			throw new NotFoundException("No performance found for performanceId: " + performanceId);

		List<Hall> hallList = new ArrayList<>();
		for (ScheduleDto schedule : performance.scheduleList()) {
			Hall hall = integration.getHall(schedule.hallId());
			if (hall == null)
				throw new NotFoundException("No hall found for hallId: " + schedule.hallId());
			hallList.add(hall);
		}

		return createPerformanceAggregate(performance, hallList, serviceUtil.getServiceAddress());
	}

	private PerformanceAggregate createPerformanceAggregate(Performance performance, List<Hall> hallList,
		String serviceAddress) {
		List<ScheduleSummaryWithSeats> scheduleSummaryWithSeat = new ArrayList<>();
		for (int i = 0; i < hallList.size(); i++) {
			List<PerformanceSeat> performanceSeats = new ArrayList<>();
			String hallName = hallList.get(i).hallName();
			ScheduleDto nowSchedule = performance.scheduleList().get(i);
			LocalDateTime performanceDate = nowSchedule.performanceDate();
			Integer hallId = nowSchedule.hallId();
			for (Seat seat : hallList.get(i).seatList()) {
				Integer price = performance.pricePolicies().get(seat.type());
				PerformanceSeat performanceSeat = new PerformanceSeat(seat.seatNumber(), seat.section(),
					seat.isAvailable(), price);
				performanceSeats.add(performanceSeat);
			}
			scheduleSummaryWithSeat.add(
				new ScheduleSummaryWithSeats(new ScheduleSummary(performanceDate, hallId, hallName), performanceSeats));
		}
		String performanceAddress = performance.serviceAddress();
		String hallAddress = !hallList.isEmpty() ? hallList.get(0).serviceAddress() : "";
		ServiceAddresses serviceAddresses = new ServiceAddresses(serviceAddress, hallAddress, performanceAddress);
		return new PerformanceAggregate(performance.performanceId(), scheduleSummaryWithSeat,
			serviceAddresses);
	}
}
