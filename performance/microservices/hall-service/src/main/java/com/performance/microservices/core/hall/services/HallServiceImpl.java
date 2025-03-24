package com.performance.microservices.core.hall.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.performance.api.core.hall.Hall;
import com.performance.api.core.hall.HallService;
import com.performance.api.core.hall.Seat;
import com.performance.api.core.type.SeatType;
import com.performance.util.exceptions.InvalidInputException;
import com.performance.util.exceptions.NotFoundException;
import com.performance.util.http.ServiceUtil;

@RestController
public class HallServiceImpl implements HallService {

	private static final Logger LOG = LoggerFactory.getLogger(HallServiceImpl.class);

	private final ServiceUtil serviceUtil;

	@Autowired
	public HallServiceImpl(ServiceUtil serviceUtil) {
		this.serviceUtil = serviceUtil;
	}

	@Override
	public Hall getHall(int hallId) {

		if(hallId < 1)
			throw new InvalidInputException("Invalid hallId: " + hallId);

		if (hallId == 13)
			throw new NotFoundException("No hall found for hallId " + hallId);

		char[] section = {'A', 'B', 'C', 'D'};
		List<Seat> seatList = new ArrayList<>();

		for (char c : section) {
			for (int j = 1; j <= 10; j++) {
				seatList.add(new Seat(j, c, SeatType.STANDARD, true));
			}
		}

		Hall hall = new Hall(hallId, "예술의 전당", seatList, serviceUtil.getServiceAddress());

		LOG.debug("/hall response get seat size: {}", hall.seatList().size());
		return hall;
	}
}
