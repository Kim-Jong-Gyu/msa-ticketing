package com.performance.microservices.core.performance.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.performance.api.core.performance.Performance;
import com.performance.api.core.performance.PerformanceService;
import com.performance.storage.performance.mongo.persistence.PerformanceEntity;
import com.performance.storage.performance.mongo.persistence.PerformanceRepository;
import com.performance.util.exceptions.InvalidInputException;
import com.performance.util.http.ServiceUtil;


@RestController
public class PerformanceServiceImpl implements PerformanceService {

	private static final Logger LOG = LoggerFactory.getLogger(PerformanceServiceImpl.class);

	private final ServiceUtil serviceUtil;

	private final PerformanceRepository repository;

	private final PerformanceMapper mapper;

	@Autowired
	public PerformanceServiceImpl(ServiceUtil serviceUtil, PerformanceRepository repository,
		PerformanceMapper mapper) {
		this.serviceUtil = serviceUtil;
		this.repository = repository;
		this.mapper = mapper;
	}

	@Override
	public Performance getPerformance(Integer performanceId) {
		if (performanceId < 1)
			throw new InvalidInputException("Invalid performanceId " + performanceId);

		PerformanceEntity performanceEntity = repository.findByPerformanceId(performanceId);

		Performance response = mapper.entityToApi(performanceEntity);
		response.setServiceAddress(serviceUtil.getServiceAddress());
		return response;
	}

	@Override
	public Performance createPerformance(Performance body) {
		PerformanceEntity entity = mapper.apiToEntity(body);
		PerformanceEntity newEntity = repository.save(entity);

		LOG.debug("createPerformance: entity created for performanceId: {}", body.getPerformanceId());
		return mapper.entityToApi(newEntity);
	}

}
