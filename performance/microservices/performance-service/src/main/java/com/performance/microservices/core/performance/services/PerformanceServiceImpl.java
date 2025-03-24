package com.performance.microservices.core.performance.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.performance.api.core.performance.Performance;
import com.performance.api.core.performance.PerformanceService;
import com.performance.api.core.performance.ScheduleDto;
import com.performance.util.exceptions.InvalidInputException;
import com.performance.util.exceptions.NotFoundException;
import com.performance.util.http.ServiceUtil;
import com.ticketing.performance.common.SeatType;

@RestController
public class PerformanceServiceImpl implements PerformanceService {

	private static final Logger LOG = LoggerFactory.getLogger(PerformanceServiceImpl.class);

	private final ServiceUtil serviceUtil;

	@Autowired
	public PerformanceServiceImpl(ServiceUtil serviceUtil) {
		this.serviceUtil = serviceUtil;
	}

	@Override
	public Performance getPerformance(int performanceId) {
		LOG.debug("/performance return the found performance for performanceId = {}", performanceId);
		if (performanceId < 1)
			throw new InvalidInputException("Invalid performanceId " + performanceId);
		if (performanceId == 13)
			throw new NotFoundException("No performance found for performanceId " + performanceId);

		Map<SeatType, Integer> pricePolicyArrayList = new HashMap<>();
		pricePolicyArrayList.put(SeatType.STANDARD, 10000);
		pricePolicyArrayList.put(SeatType.VIP, 13000);

		List<ScheduleDto> scheduleList = new ArrayList<>();
		scheduleList.add(new ScheduleDto(1, LocalDateTime.now()));

		return new Performance(performanceId, "title", pricePolicyArrayList, LocalDateTime.now().minusDays(1),
			LocalDateTime.now().plusDays(1), scheduleList,
			serviceUtil.getServiceAddress());
	}

	@Override
	public Performance createPerformance(Performance performance) {

	}

}
