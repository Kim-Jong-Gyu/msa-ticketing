package com.performance.microservices.core.performance;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;
import static org.springframework.http.HttpStatus.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class PerformanceServiceApplicationTest {

	@Autowired
	private WebTestClient client;


	@Test
	public void getPerformanceByPerformanceId(){
		int performanceId = 1;

		client.get()
			.uri("/performance/" + performanceId)
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody()
			.jsonPath("$.performanceId").isEqualTo(performanceId);
	}

	@Test
	public void getPerformanceInvalidateParameterNegativeValue() {
		int performanceId = -1;

		client.get()
			.uri("/performance/" + performanceId)
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody()
			.jsonPath("$.path").isEqualTo("/performance/" + performanceId)
			.jsonPath("$.message").isEqualTo("Invalid performanceId " + performanceId);
	}


	@Test
	public void getPerformanceNotFound() {
		int performanceId = 13;

		client.get()
			.uri("/performance/" + performanceId)
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isEqualTo(NOT_FOUND)
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody()
			.jsonPath("$.path").isEqualTo("/performance/" + performanceId)
			.jsonPath("$.message").isEqualTo("No performance found for performanceId " + performanceId);
	}
}