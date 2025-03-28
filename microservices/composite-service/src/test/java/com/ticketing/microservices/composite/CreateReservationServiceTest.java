package com.ticketing.microservices.composite;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.ticketing.api.composite.CreateReservation;
import com.ticketing.api.composite.ServiceAddresses;
import com.ticketing.api.core.performance.Performance;
import com.ticketing.api.core.performance.PricePolicy;
import com.ticketing.api.core.performance.Schedule;
import com.ticketing.api.core.reservation.Reservation;
import com.ticketing.api.core.reservation.ReservedLine;
import com.ticketing.microservices.composite.services.CompositeIntegration;
import com.ticketing.microservices.composite.services.CreateReservationServiceImpl;
import com.ticketing.util.http.ServiceUtil;

@WebFluxTest(controllers = CreateReservationServiceImpl.class)
public class CreateReservationServiceTest {

	private static final int PERFORMANCE_ID_OK = 1;

	private static final int PERFORMANCE_ID_NOT_FOUND = 10;

	private static final int RESERVATION_ID_OK = 1;

	private static final int DEFAULT_HALL_ID = 1;

	private static final LocalDateTime PERFORMANCE_DATE = LocalDateTime.now();

	@Autowired
	private WebTestClient client;

	@MockitoBean
	private CompositeIntegration integration;

	@MockitoBean
	private ServiceUtil serviceUtil;

	@Test
	public void createReservation() {
		// given
		Reservation body = createReservation(RESERVATION_ID_OK);
		given(integration.getPerformance(PERFORMANCE_ID_OK)).willReturn(createPerformance(PERFORMANCE_ID_OK));
		given(serviceUtil.getServiceAddress()).willReturn("mock com");
		given(integration.createReservation(any(Reservation.class))).willReturn(body);

		client.post()
			.uri("/composite/reservation")
			.contentType(APPLICATION_JSON)
			.bodyValue(body)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.reservationId").isEqualTo(RESERVATION_ID_OK);
	}

	private Performance createPerformance(Integer performanceId) {
		return new Performance(performanceId, "title",
			List.of(new PricePolicy("VIP", 14000),
				new PricePolicy("STANDARD", 10000)),
			PERFORMANCE_DATE.minusDays(10), PERFORMANCE_DATE.minusDays(5),
			List.of(new Schedule(DEFAULT_HALL_ID, PERFORMANCE_DATE)),
			"mock per");
	}

	private Reservation createReservation(Integer reservationId) {
		return new Reservation(reservationId, 1,
			List.of(
				new ReservedLine(PERFORMANCE_ID_OK, DEFAULT_HALL_ID, PERFORMANCE_DATE, 1, 'a', 14000),
				new ReservedLine(PERFORMANCE_ID_OK, DEFAULT_HALL_ID, PERFORMANCE_DATE, 2, 'a', 14000))
			, "RESERVED");
	}

}
