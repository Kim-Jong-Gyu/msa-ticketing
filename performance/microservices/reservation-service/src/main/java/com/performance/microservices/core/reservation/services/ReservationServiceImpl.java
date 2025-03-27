package com.performance.microservices.core.reservation.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.performance.api.core.reservation.Reservation;
import com.performance.api.core.reservation.ReservationService;
import com.performance.storage.core.reservation.persistence.ReservationEntity;
import com.performance.storage.core.reservation.persistence.ReservationRepository;
import com.performance.util.exceptions.InvalidInputException;
import com.performance.util.http.ServiceUtil;

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
	public List<Reservation> getReservationListByPerformanceId(Integer performanceId) {
		if(performanceId < 1){
			throw new InvalidInputException("Invalid performanceId: " + performanceId);
		}
		List<ReservationEntity> findEntityList = repository.findByPerformanceId(performanceId);
		List<Reservation> responseList = mapper.entityListToApiList(findEntityList);
		responseList.forEach(e -> e.setServiceAddress(serviceUtil.getServiceAddress()));

		LOG.debug("getReservationListByPerformanceId: response size: {}", responseList.size());
		return responseList;
	}


	@Override
	public Reservation createReservation(Reservation body) {
		ReservationEntity entity = mapper.apiToEntity(body);
		ReservationEntity newEntity = repository.save(entity);

		LOG.debug("createReservation: create a reservation entity: {}/{}", body.getPerformanceId(), body.getReservationId());
		return mapper.entityToApi(newEntity);
	}
}
