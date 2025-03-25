package com.performance.microservices.core.hall.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.performance.api.core.hall.Hall;
import com.performance.api.core.hall.HallService;
import com.performance.storage.hall.mysql.HallEntity;
import com.performance.storage.hall.mysql.HallRepository;
import com.performance.util.exceptions.InvalidInputException;
import com.performance.util.http.ServiceUtil;

@RestController
public class HallServiceImpl implements HallService {

	private static final Logger LOG = LoggerFactory.getLogger(HallServiceImpl.class);

	private final ServiceUtil serviceUtil;

	private final HallRepository repository;

	private final HallMapper mapper;

	@Autowired
	public HallServiceImpl(ServiceUtil serviceUtil, HallRepository repository, HallMapper mapper) {
		this.serviceUtil = serviceUtil;
		this.repository = repository;
		this.mapper = mapper;
	}

	@Override
	public Hall getHall(Integer hallId) {
		if (hallId < 1)
			throw new InvalidInputException("Invalid hallId: " + hallId);

		HallEntity entity = repository.findByHallId(hallId);
		Hall response = mapper.entityToApi(entity);
		response.setServiceAddress(serviceUtil.getServiceAddress());
		return response;
	}

	@Override
	public Hall createHall(Hall body) {

		HallEntity entity = mapper.apiToEntity(body);
		HallEntity newEntity = repository.save(entity);

		LOG.debug("createHall: entity created for hallId: {}", body.getHallId());
		return mapper.entityToApi(newEntity);
	}
}