package com.performance.microservices.composite.performance;

import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.performance.api.core.hall.Hall;
import com.performance.api.core.hall.Seat;
import com.performance.api.core.performance.Performance;
import com.performance.api.core.performance.Schedule;
import com.performance.api.enums.SeatType;
import com.performance.microservices.composite.performance.services.PerformanceCompositeIntegration;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(MockBeanConfig.class)
class PerformanceCompositeServiceApplicationTest {
	private static final int PERFORMANCE_ID_OK = 1;
	private static final int PERFORMANCE_ID_NOT_FOUND = 2;

	private static final int PERFORMANCE_ID_OK_BUT_NOT_FOUND_HALL = 3;

	private static final int HALL_ID_OK = 1;

	private static final int HALL_ID_NOT_FOUND = 2;


	@Autowired
	private WebTestClient client;

	@Autowired
	private PerformanceCompositeIntegration integration;

	@Test
	public void getPerformanceById() {

		// given
		Map<SeatType, Integer> map = new HashMap<>();
		map.put(SeatType.VIP, 14000);
		map.put(SeatType.STANDARD, 10000);

		List<Schedule> scheduleList = new ArrayList<>();
		scheduleList.add(new Schedule(HALL_ID_OK, LocalDateTime.now().plusDays(4)));

		char[] section = {'A', 'B', 'C', 'D'};
		List<Seat> seatList = new ArrayList<>();

		for (char c : section) {
			for (int j = 1; j <= 10; j++) {
				seatList.add(new Seat(j, c, SeatType.STANDARD, true));
			}
		}

		when(integration.getPerformance(PERFORMANCE_ID_OK)).thenReturn(
			new Performance(PERFORMANCE_ID_OK, "title", map, LocalDateTime.now(), LocalDateTime.now().plusDays(1),
				scheduleList, "mock Address")
		);

		when(integration.getHall(HALL_ID_OK)).thenReturn(
			new Hall(HALL_ID_OK, "name", seatList, "mock Addrss")
		);

		// when & then
		client.get()
			.uri("/performance-composite/performance/" + PERFORMANCE_ID_OK)
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody()
			.jsonPath("$.performanceId").isEqualTo(PERFORMANCE_ID_OK)
			.jsonPath("$.scheduleSummaryWithSeatsList.length()").isEqualTo(1)
			.jsonPath("$.scheduleSummaryWithSeatsList[0].scheduleSummary.hallId").isEqualTo(HALL_ID_OK)
			.jsonPath("$.scheduleSummaryWithSeatsList[0].performanceSeatList.length()").isEqualTo(40);
	}

	@Test
	public void getPerformanceNotFoundHall(){
		// given
		List<Schedule> scheduleList = new ArrayList<>();
		scheduleList.add(new Schedule(HALL_ID_NOT_FOUND, LocalDateTime.now().plusDays(4)));
		Map<SeatType, Integer> pricePolicy = Map.of(SeatType.VIP, 14000);

		when(integration.getPerformance(PERFORMANCE_ID_OK_BUT_NOT_FOUND_HALL)).thenReturn(
			new Performance(
				PERFORMANCE_ID_OK_BUT_NOT_FOUND_HALL,
				"title",
				pricePolicy,
				LocalDateTime.now(),
				LocalDateTime.now().plusDays(1),
				scheduleList,
				"mockAddress"
			));

		when(integration.getHall(HALL_ID_NOT_FOUND)).thenReturn(null);

		// when & then
		client.get()
			.uri("/performance-composite/performance/" + PERFORMANCE_ID_OK_BUT_NOT_FOUND_HALL)
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isNotFound()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody()
			.jsonPath("$.path").isEqualTo("/performance-composite/performance/" + PERFORMANCE_ID_OK_BUT_NOT_FOUND_HALL)
			.jsonPath("$.message").isEqualTo("No hall found for hallId: " + HALL_ID_NOT_FOUND);
	}


	@Test
	public void getPerformanceNotFound(){

		//given
		when(integration.getPerformance(PERFORMANCE_ID_NOT_FOUND)).thenReturn(null);


		// when & then
		client.get()
			.uri("/performance-composite/performance/" + PERFORMANCE_ID_NOT_FOUND)
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isNotFound()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody()
			.jsonPath("$.path").isEqualTo("/performance-composite/performance/" + PERFORMANCE_ID_NOT_FOUND)
			.jsonPath("$.message").isEqualTo("No performance found for performanceId: " + PERFORMANCE_ID_NOT_FOUND);
	}
}