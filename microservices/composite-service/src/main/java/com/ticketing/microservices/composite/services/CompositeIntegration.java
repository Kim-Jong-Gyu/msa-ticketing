package com.ticketing.microservices.composite.services;

import static org.springframework.http.HttpStatus.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketing.api.core.hall.HallWithSeat;
import com.ticketing.api.core.hall.HallWithUnavailable;
import com.ticketing.api.core.performance.Performance;
import com.ticketing.api.core.performance.PerformanceService;
import com.ticketing.api.core.hall.Hall;
import com.ticketing.api.core.hall.HallService;
import com.ticketing.api.core.performance.Schedule;
import com.ticketing.api.core.reservation.Reservation;
import com.ticketing.api.core.reservation.ReservationSeat;
import com.ticketing.api.core.reservation.ReservationService;
import com.ticketing.util.exceptions.InvalidInputException;
import com.ticketing.util.exceptions.NotFoundException;
import com.ticketing.util.http.HttpErrorInfo;

@Component
public class CompositeIntegration implements PerformanceService, HallService, ReservationService {

	private static final Logger LOG = LoggerFactory.getLogger(CompositeIntegration.class);

	private final RestTemplate restTemplate;

	private final ObjectMapper mapper;

	private final String performanceServiceUrl;

	private final String hallServiceUrl;

	private final String reservationServiceUrl;

	@Autowired
	public CompositeIntegration(
		RestTemplate restTemplate,
		ObjectMapper objectMapper,

		@Value("${app.performance-service.host}") String performanceServiceHost,
		@Value("${app.performance-service.port}") Integer performanceServicePort,

		@Value("${app.hall-service.host}") String hallServiceHost,
		@Value("${app.hall-service.port}") Integer hallServicePort,

		@Value("${app.reservation-service.host}") String reservationServiceHost,
		@Value("${app.reservation-service.port}") Integer reservationServicePort
	) {
		this.restTemplate = restTemplate;
		this.mapper = objectMapper;

		performanceServiceUrl = "http://" + performanceServiceHost + ":" + performanceServicePort + "/performance";
		hallServiceUrl = "http://" + hallServiceHost + ":" + hallServicePort + "/hall";
		reservationServiceUrl = "http://" + reservationServiceHost + ":" + reservationServicePort + "/reservation";
	}

	@Override
	public HallWithSeat getHallWithSeat(Integer hallId) {
		try {
			String url = hallServiceUrl + "/seat/" + hallId;
			LOG.debug("Will call getHallWithSeat API on URL : {}", url);

			HallWithSeat response = restTemplate.getForObject(url, HallWithSeat.class);
			LOG.debug("Found a hall with id : {}", response.getHallId());

			return response;
		} catch (HttpClientErrorException ex) {
			throw handleHttpClientException(ex);
		}
	}

	@Override
	public Hall createHall(Hall body) {
		try {
			String url = hallServiceUrl;
			LOG.debug("Will post a new hall to URL: {}", url);

			Hall hall = restTemplate.postForObject(url, body, Hall.class);
			LOG.debug("Created a hall with id: {}", hall.getHallId());

			return hall;
		} catch (HttpClientErrorException ex) {
			throw handleHttpClientException(ex);
		}
	}

	@Override
	public HallWithUnavailable getHallWithUnavailableList(Integer hallId) {
		try {
			String url = hallServiceUrl + "/unavailable/" + hallId;
			LOG.debug("Will call getHallWithUnavailableList API on URL : {}", url);

			HallWithUnavailable response = restTemplate.getForObject(url, HallWithUnavailable.class);
			LOG.debug("Found a hall with id : {}", response.getHallId());

			return response;
		} catch (HttpClientErrorException ex) {
			throw handleHttpClientException(ex);
		}
	}

	@Override
	public void deleteAllHall() {
		try {
			String url = hallServiceUrl + "/clean-up";
			LOG.debug("Will call deleteAllHall API on URL : {}", url);
			restTemplate.delete(url);
		} catch (HttpClientErrorException ex) {
			throw handleHttpClientException(ex);
		}
	}

	@Override
	public Performance getPerformance(Integer performanceId) {
		try {
			String url = performanceServiceUrl + "/" + performanceId;
			LOG.debug("Will call getPerformance API on URL : {}", url);

			Performance performance = restTemplate.getForObject(url, Performance.class);
			LOG.debug("Found a  with id : {}", performance.getPerformanceId());

			return performance;
		} catch (HttpClientErrorException ex) {
			throw handleHttpClientException(ex);
		}
	}

	@Override
	public List<Schedule> getPerformanceSchedule(Integer performanceId) {
		try {
			String url = performanceServiceUrl + "/schedule/" + performanceId;
			LOG.debug("Will call Performance API on URL : {}", url);
			List<Schedule> scheduleList = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Schedule>>() {
				}).getBody();
			LOG.debug("Found a performance schedule list  with id : {}", performanceId);
			return scheduleList;
		}catch (HttpClientErrorException ex){
			throw handleHttpClientException(ex);
		}
	}

	@Override
	public Performance createPerformance(Performance body) {
		try {
			String url = performanceServiceUrl;
			LOG.debug("Will post a new performance to URL: {}", url);

			Performance performance = restTemplate.postForObject(url, body, Performance.class);
			LOG.debug("Created a performance with id: {}", performance.getPerformanceId());

			return performance;
		} catch (HttpClientErrorException ex) {
			throw handleHttpClientException(ex);
		}
	}

	@Override
	public void deleteAllPerformance() {
		try {
			String url = performanceServiceUrl + "/clean-up";
			LOG.debug("Will call deleteAllPerformance API on URL : {}", url);
			restTemplate.delete(url);
		} catch (HttpClientErrorException ex) {
			throw handleHttpClientException(ex);
		}
	}

	@Override
	public Reservation getReservation(Integer reservationId) {
		try {
			String url = reservationServiceUrl + "/" + reservationId;
			LOG.debug("Will call getReservation API on URL : {}", url);
			Reservation reservation = restTemplate.getForObject(url, Reservation.class);
			LOG.debug("Found a  with id : {}", reservation.getReservationId());
			return reservation;
		} catch (HttpClientErrorException ex) {
			throw handleHttpClientException(ex);
		}
	}

	@Override
	public List<ReservationSeat> getReservationSeatListByPerformanceId(Integer performanceId) {
		try {
			String url = reservationServiceUrl + "/" + "performance/" + performanceId;
			LOG.debug("Will call getReservationListByPerformanceId API on URL : {}", url);
			List<ReservationSeat> reservationSeatList = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<ReservationSeat>>() {
				}).getBody();

			LOG.debug("Found {} reservation seat for a performance with id: {}", reservationSeatList.size(),
				performanceId);
			return reservationSeatList;
		} catch (Exception ex) {
			LOG.warn("Got an exception while requesting reservation, return zero reservations: {}", ex.getMessage());
			return new ArrayList<>();
		}
	}

	@Override
	public Reservation createReservation(Reservation body) {
		try {
			String url = reservationServiceUrl;
			LOG.debug("Will post a new reservation to URL: {}", url);
			Reservation reservation = restTemplate.postForObject(url, body, Reservation.class);
			LOG.debug("Created a reservation with id: {}", reservation.getReservationId());
			return reservation;
		} catch (HttpClientErrorException ex) {
			throw handleHttpClientException(ex);
		}
	}

	@Override
	public void deleteAllReservation() {
		try {
			String url = reservationServiceUrl + "/clean-up";
			LOG.debug("Will call deleteAllReservation API on URL: {}", url);
			restTemplate.delete(url);
		} catch (HttpClientErrorException ex) {
			throw handleHttpClientException(ex);
		}
	}

	private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
		HttpStatusCode statusCode = ex.getStatusCode();
		if (statusCode.equals(NOT_FOUND)) {
			return new NotFoundException(getErrorMessage(ex));
		} else if (statusCode.equals(UNPROCESSABLE_ENTITY)) {
			return new InvalidInputException(getErrorMessage(ex));
		}
		LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
		LOG.warn("Error body: {}", ex.getResponseBodyAsString());
		return ex;
	}

	private String getErrorMessage(HttpClientErrorException ex) {
		try {
			return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
		} catch (IOException ioex) {
			return ex.getMessage();
		}
	}
}