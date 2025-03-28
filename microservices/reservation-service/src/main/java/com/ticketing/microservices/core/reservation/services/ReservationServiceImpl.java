package com.ticketing.microservices.core.reservation.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.ticketing.api.core.reservation.Reservation;
import com.ticketing.api.core.reservation.ReservationSeat;
import com.ticketing.api.core.reservation.ReservationService;
import com.ticketing.storage.core.reservation.persistence.ReservationEntity;
import com.ticketing.storage.core.reservation.persistence.ReservationRepository;
import com.ticketing.util.exceptions.InvalidInputException;
import com.ticketing.util.http.ServiceUtil;

@RestController
public class ReservationServiceImpl implements ReservationService {

	private static final Logger LOG = LoggerFactory.getLogger(ReservationServiceImpl.class);
	private final ReservationRepository repository;

	private final ReservationMapper mapper;

	private final ServiceUtil serviceUtil;

	@Autowired
	public ReservationServiceImpl(ReservationRepository repository,
		ReservationMapper mapper, ServiceUtil serviceUtil) {
		this.repository = repository;
		this.mapper = mapper;
		this.serviceUtil = serviceUtil;
	}

	@Override
	public Reservation getReservation(Integer reservationId) {
		if(reservationId < 1){
			throw new InvalidInputException("Invalid reservationId: " + reservationId);
		}
		ReservationEntity findEntity = repository.findByReservationId(reservationId);
		Reservation response = mapper.entityToApi(findEntity);
		response.setServiceAddress(serviceUtil.getServiceAddress());
		return response;
	}

	@Override
	public List<ReservationSeat> getReservationSeatListByPerformanceId(Integer performanceId) {
		if(performanceId < 1){
			throw new InvalidInputException("Invalid performanceId: " + performanceId);
		}
		List<ReservationEntity> reservationEntities = repository.findByPerformanceId(performanceId);

		// ReservationSeat로 변환
		List<ReservationSeat> reservationSeats = reservationEntities.stream()
			.flatMap(r -> r.getReservedLineList().stream())
			.filter(line -> line.getPerformanceId().equals(performanceId))
			.map(mapper::reservedLineVoToReservationSeat)
			.toList();

		reservationSeats.forEach(e -> e.setServiceAddress(serviceUtil.getServiceAddress()));
		LOG.debug("getReservationListByPerformanceId: response size: {}", reservationSeats.size());
		return reservationSeats;
	}


	@Override
	public Reservation createReservation(Reservation body) {
		ReservationEntity entity = mapper.apiToEntity(body);
		ReservationEntity newEntity = repository.save(entity);
		LOG.debug("createReservation: create a reservation entity: {}/{}", body.getUserId(), body.getReservationId());
		return mapper.entityToApi(newEntity);
	}
}
