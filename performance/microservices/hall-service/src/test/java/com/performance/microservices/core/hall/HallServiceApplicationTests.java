package com.performance.microservices.core.hall;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;
import static org.springframework.http.HttpStatus.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.performance.util.exceptions.InvalidInputException;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class HallServiceApplicationTests {


	@Autowired
	private WebTestClient client;


	@Test
	public void getHallByHallId(){
		int hallId = 1;

		client.get()
			.uri("/hall/" + hallId)
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody()
			.jsonPath("$.hallId").isEqualTo(hallId);
	}

	@Test
	public void getHallInvalidateParameterNegativeValue() {
		int hallId = -1;

		client.get()
			.uri("/hall/" + hallId)
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody()
			.jsonPath("$.path").isEqualTo("/hall/" + hallId)
			.jsonPath("$.message").isEqualTo("Invalid hallId: " + hallId);
	}


	@Test
	public void getHallNotFound() {
		int hallId = 13;

		client.get()
			.uri("/hall/" + hallId)
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isEqualTo(NOT_FOUND)
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody()
			.jsonPath("$.path").isEqualTo("/hall/" + hallId)
			.jsonPath("$.message").isEqualTo("No hall found for hallId " + hallId);
	}

}
