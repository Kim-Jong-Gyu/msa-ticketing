package com.performance.microservices.core.performance;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.performance.api.core.performance.Performance;
import com.performance.api.core.performance.Schedule;
import com.performance.common.SeatType;
import com.performance.microservices.core.performance.services.PerformanceMapper;
import com.performance.microservices.core.performance.services.PerformanceMapperImpl;
import com.performance.microservices.core.performance.services.PerformanceServiceImpl;
import com.performance.storage.performance.mongo.persistence.PerformanceEntity;
import com.performance.storage.performance.mongo.persistence.PerformanceRepository;
import com.performance.util.http.ServiceUtil;

@WebFluxTest(controllers = PerformanceServiceImpl.class)
@Import({PerformanceMapperImpl.class})
@ContextConfiguration(classes = PerformanceTestApplication.class)
class PerformanceServiceTest {

	@Autowired
	private WebTestClient webTestClient;

	@MockitoBean
	private PerformanceRepository repository;

	@MockitoBean
	private ServiceUtil serviceUtil;

	@Autowired
	private PerformanceMapper mapper;

	@Test
	void getPerformance_success() {
		int performanceId = 1;

		PerformanceEntity entity = new PerformanceEntity(performanceId, "title", Map.of(), LocalDateTime.now(),
			LocalDateTime.now().plusDays(1), List.of());
		Performance apiResponse = mapper.entityToApi(entity);
		apiResponse.setServiceAddress("Mock Address");

		given(repository.findByPerformanceId(performanceId)).willReturn(entity);
		given(serviceUtil.getServiceAddress()).willReturn("Mock Address");

		webTestClient.get()
			.uri("/performance/{id}", performanceId)
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.performanceId").isEqualTo(performanceId)
			.jsonPath("$.serviceAddress").isEqualTo("Mock Address");

		then(repository).should(times(1)).findByPerformanceId(performanceId);
	}

	@Test
	void getPerformance_invalidId() {
		int invalidId = -1;

		webTestClient.get()
			.uri("/performance/{id}", invalidId)
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isEqualTo(422) // UNPROCESSABLE_ENTITY
			.expectBody()
			.jsonPath("$.message").isEqualTo("Invalid performanceId " + invalidId);

		then(repository).should(times(0)).findByPerformanceId(any());
	}

	@Test
	void createPerformance_success() {
		Performance inputPerformance = new Performance(2, "title", Map.of(SeatType.VIP, 14000), LocalDateTime.now(),
			LocalDateTime.now().plusDays(1), List.of(new Schedule(1, LocalDateTime.now().plusDays(3))));

		PerformanceEntity entity = mapper.apiToEntity(inputPerformance);
		PerformanceEntity savedEntity = new PerformanceEntity(entity.getPerformanceId(), entity.getTitle(),
			entity.getPricePolicies(), entity.getBookingStartDate(), entity.getBookingEndDate(),
			entity.getScheduleList());
		Performance apiResponse = mapper.entityToApi(savedEntity);

		given(repository.save(any(PerformanceEntity.class))).willReturn(savedEntity);

		webTestClient.post()
			.uri("/performance")
			.contentType(APPLICATION_JSON)
			.bodyValue(inputPerformance)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.performanceId").isEqualTo(apiResponse.getPerformanceId())
			.jsonPath("$.title").isEqualTo(apiResponse.getTitle());

		then(repository).should(times(1)).save(any(PerformanceEntity.class));
	}

}
