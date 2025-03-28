package com.ticketing.microservices.composite;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.ticketing.api.core.hall.Hall;
import com.ticketing.api.core.hall.HallWithSeat;
import com.ticketing.api.core.hall.Seat;
import com.ticketing.microservices.composite.services.CompositeIntegration;
import com.ticketing.microservices.composite.services.CreateReservationServiceImpl;
import com.ticketing.util.http.ServiceUtil;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;

import java.util.ArrayList;
import java.util.List;

@WebFluxTest(controllers = CreateReservationServiceImpl.class)
public class CreateHallServiceTest {

	@Autowired
	WebTestClient client;

	@MockitoBean
	private CompositeIntegration integration;

	@MockitoBean
	private ServiceUtil serviceUtil;

	@Test
	public void createHall(){

		Hall body = mockHall();
		given(integration.createHall(any(Hall.class))).willReturn(body);

		client.post()
			.uri("/composite/hall")
			.contentType(APPLICATION_JSON)
			.bodyValue(body)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.hallId").isEqualTo(1);
	}


	private Hall mockHall() {
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
		return new Hall(1, hallName, seatList, new ArrayList<>());
	}
}
