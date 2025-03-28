package com.ticketing.microservices.composite;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.ticketing.api.core.hall.HallWithSeat;
import com.ticketing.api.core.hall.Seat;
import com.ticketing.api.core.performance.Performance;
import com.ticketing.api.core.performance.PricePolicy;
import com.ticketing.api.core.performance.Schedule;
import com.ticketing.api.core.reservation.ReservationSeat;
import com.ticketing.microservices.composite.services.CompositeIntegration;
import com.ticketing.microservices.composite.services.GetPerformanceWithSeatServiceImpl;
import com.ticketing.util.exceptions.NotFoundException;
import com.ticketing.util.http.ServiceUtil;

@WebFluxTest(controllers = GetPerformanceWithSeatServiceImpl.class)
class GetPerformanceWithSeatServiceTest {
	private static final int PERFORMANCE_ID_OK = 1;
	private static final int PERFORMANCE_ID_NOT_FOUND = 10;

	private static final int PERFORMANCE_ID_OK_BUT_NOT_FOUND_HALL = 20;

	private static final int HALL_ID_OK = 1;

	private static final int HALL_ID_NOT_FOUND = 10;

	private static final int RESERVATION_ID_OK = 1;

	private static final LocalDateTime PERFORMANCE_DATE = LocalDateTime.now();

	@Autowired
	private WebTestClient client;

	@MockitoBean
	private CompositeIntegration integration;

	@MockitoBean
	private ServiceUtil serviceUtil;

	@BeforeEach
	public void setUp() {

		given(integration.getHallWithSeat(HALL_ID_OK)).willReturn(createHallWithSeat());

		given(integration.getPerformance(PERFORMANCE_ID_OK)).willReturn(
			createPerformance(HALL_ID_OK));

		given(integration.getReservationSeatListByPerformanceId(RESERVATION_ID_OK)).willReturn(
			createReservationSeat());

		given(integration.getPerformance(PERFORMANCE_ID_OK_BUT_NOT_FOUND_HALL)).willReturn(
			createPerformance(HALL_ID_NOT_FOUND)
		);
		given(integration.getHallWithSeat(HALL_ID_NOT_FOUND)).willThrow(
			new NotFoundException("No hall found for hallId: " + HALL_ID_NOT_FOUND));

		given(integration.getPerformance(PERFORMANCE_ID_NOT_FOUND)).willThrow(
			new NotFoundException("No performance found for performanceId: " + PERFORMANCE_ID_NOT_FOUND)
		);
	}

	@Test
	public void getPerformanceWithSeat() {
		getAndVerify(PERFORMANCE_ID_OK, HttpStatus.OK)
			.jsonPath("$.performanceId").isEqualTo(PERFORMANCE_ID_OK)
			.jsonPath("$.scheduleSummaryWithSeatsList.length()").isEqualTo(1)
			.jsonPath("$.scheduleSummaryWithSeatsList[0].hallId").isEqualTo(HALL_ID_OK)
			.jsonPath("$.scheduleSummaryWithSeatsList[0].performanceSeatList.length()").isEqualTo(40);
	}

	@Test
	public void getPerformanceNotFoundHall() {

		getAndVerify(PERFORMANCE_ID_OK_BUT_NOT_FOUND_HALL, HttpStatus.NOT_FOUND)
			.jsonPath("$.path").isEqualTo("/composite/performance-seat/" + PERFORMANCE_ID_OK_BUT_NOT_FOUND_HALL)
			.jsonPath("$.message").isEqualTo("No hall found for hallId: " + HALL_ID_NOT_FOUND);
	}

	@Test
	public void getPerformanceNotFound() {

		// when & then
		getAndVerify(PERFORMANCE_ID_NOT_FOUND, HttpStatus.NOT_FOUND)
			.jsonPath("$.path").isEqualTo("/composite/performance-seat/" + PERFORMANCE_ID_NOT_FOUND)
			.jsonPath("$.message").isEqualTo("No performance found for performanceId: " + PERFORMANCE_ID_NOT_FOUND);
	}


	private WebTestClient.BodyContentSpec getAndVerify(Integer performanceId, HttpStatus expectedStatus) {
		return client.get()
			.uri("/composite/performance-seat/" + performanceId)
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isEqualTo(expectedStatus)
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody();
	}

	private List<ReservationSeat> createReservationSeat() {
		return List.of(new ReservationSeat(RESERVATION_ID_OK, HALL_ID_OK,
				PERFORMANCE_DATE, 1, 'a', "mock address"),
			new ReservationSeat(RESERVATION_ID_OK, HALL_ID_OK,
				PERFORMANCE_DATE, 2, 'a', "mock address"),
			new ReservationSeat(RESERVATION_ID_OK, HALL_ID_OK,
				PERFORMANCE_DATE, 3, 'a', "mock address"));
	}

	private Performance createPerformance(Integer hallId) {
		return new Performance(PERFORMANCE_ID_OK, "title",
			List.of(new PricePolicy("VIP", 14000),
				new PricePolicy("STANDARD", 10000)),
			PERFORMANCE_DATE.minusDays(10), PERFORMANCE_DATE.minusDays(5),
			List.of(new Schedule(hallId, PERFORMANCE_DATE)));
	}

	private HallWithSeat createHallWithSeat() {
		String hallName = "name";
		List<Seat> seatList = new ArrayList<>();
		char[] section = {'a', 'b', 'c', 'd'};
		for (char c : section) {
			for (int j = 1; j <= 5; j++) {
				seatList.add(new Seat(j, c, "STANDARD"));
			}
			for (int k = 6; k <= 10; k++) {
				seatList.add(new Seat(k, c, "VIP"));
			}
		}
		return new HallWithSeat(GetPerformanceWithSeatServiceTest.HALL_ID_OK, hallName, seatList);
	}
}