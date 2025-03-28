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

import com.ticketing.api.core.hall.HallWithUnavailable;
import com.ticketing.api.core.performance.Performance;
import com.ticketing.api.core.performance.PricePolicy;
import com.ticketing.api.core.performance.Schedule;
import com.ticketing.microservices.composite.services.CompositeIntegration;
import com.ticketing.microservices.composite.services.CreateReservationServiceImpl;
import com.ticketing.util.http.ServiceUtil;

@WebFluxTest(controllers = CreateReservationServiceImpl.class)
public class CreatePerformanceServiceTest {

	private static final Integer DEFAULT_HALL_ID = 1;

	private static final LocalDateTime DEFAULT_DATE = LocalDateTime.now();

	@Autowired
	WebTestClient client;

	@MockitoBean
	private CompositeIntegration integration;

	@MockitoBean
	private ServiceUtil serviceUtil;

	@Test
	public void CreatePerformanceSeat() {
		Performance body = createPerformance(1);
		given(integration.getHallWithUnavailableList(DEFAULT_HALL_ID)).willReturn(new HallWithUnavailable(1, "name", List.of(
			DEFAULT_DATE), "mock address"));

		given(integration.createPerformance(any(Performance.class))).willReturn(body);

		client.post()
			.uri("/composite/performance")
			.contentType(APPLICATION_JSON)
			.bodyValue(body)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.performanceId").isEqualTo(1);
	}

	private Performance createPerformance(Integer performanceId) {
		return new Performance(performanceId, "title",
			List.of(new PricePolicy("VIP", 14000),
				new PricePolicy("STANDARD", 10000)),
			DEFAULT_DATE.minusDays(10), DEFAULT_DATE.minusDays(5),
			List.of(new Schedule(1, DEFAULT_DATE.plusDays(3))));
	}
}
