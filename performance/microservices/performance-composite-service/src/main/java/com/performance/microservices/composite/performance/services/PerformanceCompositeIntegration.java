package com.performance.microservices.composite.performance.services;

import static org.springframework.http.HttpStatus.*;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.performance.api.core.performance.Performance;
import com.performance.api.core.performance.PerformanceService;
import com.performance.api.core.hall.Hall;
import com.performance.api.core.hall.HallService;
import com.performance.util.exceptions.InvalidInputException;
import com.performance.util.exceptions.NotFoundException;
import com.performance.util.http.HttpErrorInfo;

@Component
public class PerformanceCompositeIntegration implements PerformanceService, HallService {

	private static final Logger LOG = LoggerFactory.getLogger(PerformanceCompositeIntegration.class);

	private final RestTemplate restTemplate;

	private final ObjectMapper mapper;

	private final String performanceServiceUrl;

	private final String hallServiceUrl;

	@Autowired
	public PerformanceCompositeIntegration(
		RestTemplate restTemplate,
		ObjectMapper objectMapper,

		@Value("${app.performance-service.host}") String performanceServiceHost,
		@Value("${app.performance-service.port}") Integer performanceServicePort,

		@Value("${app.hall-service.host}") String hallServiceHost,
		@Value("${app.hall-service.port}") Integer hallServicePort
	) {
		this.restTemplate = restTemplate;
		this.mapper = objectMapper;

		performanceServiceUrl = "http://" + performanceServiceHost + ":" + performanceServicePort + "/performance/";
		hallServiceUrl = "http://" + hallServiceHost + ":" + hallServicePort + "/hall/";
	}

	@Override
	public Hall getHall(int hallId) {

		try {
			String url = hallServiceUrl + hallId;
			LOG.debug("Will call getHall API on URL : {}", url);

			Hall hall = restTemplate.getForObject(url, Hall.class);
			LOG.debug("Found a hall with id : {}", hall.hallId());

			return hall;
		}
		catch (HttpClientErrorException ex) {
			HttpStatusCode statusCode = ex.getStatusCode();
			if (statusCode.equals(NOT_FOUND)) {
				throw new NotFoundException(getErrorMessage(ex));
			} else if (statusCode.equals(UNPROCESSABLE_ENTITY)) {
				throw new InvalidInputException(getErrorMessage(ex));
			}
			LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
			LOG.warn("Error body: {}", ex.getResponseBodyAsString());
			throw ex;
		}
	}

	private String getErrorMessage(HttpClientErrorException ex) {
		try {
			return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
		} catch (IOException ioex) {
			return ex.getMessage();
		}
	}

	@Override
	public Performance getPerformance(int performanceId) {
		try {
			String url = performanceServiceUrl + performanceId;
			LOG.debug("Will call getPerformance API on URL : {}", url);

			Performance performance = restTemplate.getForObject(url, Performance.class);
			LOG.debug("Found a  with id : {}", performance.performanceId());

			return performance;
		} catch (HttpClientErrorException ex) {
			HttpStatusCode statusCode = ex.getStatusCode();
			if (statusCode.equals(NOT_FOUND)) {
				throw new NotFoundException(getErrorMessage(ex));
			} else if (statusCode.equals(UNPROCESSABLE_ENTITY)) {
				throw new InvalidInputException(getErrorMessage(ex));
			}
			LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
			LOG.warn("Error body: {}", ex.getResponseBodyAsString());
			throw ex;
		}
	}
}