package com.ticketing.microservices.core.hall.services;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.function.Supplier;
import java.util.logging.Level;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.ticketing.api.core.hall.Hall;
import com.ticketing.api.core.hall.HallService;
import com.ticketing.api.core.hall.HallWithSeat;
import com.ticketing.api.core.hall.HallWithUnavailable;
import com.ticketing.storage.core.hall.persistence.HallEntity;
import com.ticketing.storage.core.hall.persistence.HallRepository;
import com.ticketing.util.exceptions.InvalidInputException;
import com.ticketing.util.http.ServiceUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@RestController
public class HallServiceImpl implements HallService {

	private static final Logger LOG = LoggerFactory.getLogger(HallServiceImpl.class);

	private final ServiceUtil serviceUtil;

	private final HallRepository repository;

	private final HallMapper mapper;

	private final Scheduler scheduler;

	@Autowired
	public HallServiceImpl(ServiceUtil serviceUtil, HallRepository repository, HallMapper mapper, Scheduler scheduler) {
		this.serviceUtil = serviceUtil;
		this.repository = repository;
		this.mapper = mapper;
        this.scheduler = scheduler;
    }

	@Override
	public Mono<HallWithSeat> getHallWithSeat(Integer hallId) {
		if (hallId < 1)
			return Mono.error(new InvalidInputException("Invalid hallId: " + hallId));

		return asyncMono(() -> findHallIdWithSeat(hallId)).log(null, Level.FINE);
	}

	private HallWithSeat findHallIdWithSeat(Integer hallId) {
		HallEntity entity = repository.findByHallIdWithSeat(hallId);
		HallWithSeat response = mapper.entityToHallWithSeat(entity);
		response.setServiceAddress(serviceUtil.getServiceAddress());
		return response;
	}

	@Override
	public Hall createHall(Hall body) {
		if(body.getUnavailableDateList().isEmpty()){
			body.setUnavailableDateList(new ArrayList<>());
		}
		HallEntity entity = mapper.apiToEntity(body);
		HallEntity newEntity = repository.save(entity);
		LOG.debug("createHall: entity created for hallId: {}", body.getHallId());
		return mapper.entityToApi(newEntity);
	}

	@Override
	public Mono<HallWithUnavailable> getHallWithUnavailableList(Integer hallId) {
		if (hallId < 1)
			return Mono.error(new InvalidInputException("Invalid hallId: " + hallId));
		return asyncMono(() -> findHallWithUnavailableList(hallId)).log(null, Level.FINE);
	}

	private HallWithUnavailable findHallWithUnavailableList(Integer hallId) {

		HallEntity entity = repository.findByHallIdWithUnavailableDateList(hallId);
		HallWithUnavailable response = mapper.entityToHallWithUnavailable(entity);
		response.setServiceAddress(serviceUtil.getServiceAddress());
		return response;
	}

	@Override
	public void deleteAllHall() {
		LOG.info("delete all data in hall DB");
		repository.deleteAll();
	}

	private <T> Mono<T> asyncMono(Callable<T> publisherSupplier) {
		return Mono.fromCallable(publisherSupplier).subscribeOn(scheduler);
	}

}