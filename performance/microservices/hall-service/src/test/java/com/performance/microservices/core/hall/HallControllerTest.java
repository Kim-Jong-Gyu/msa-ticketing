// package com.performance.microservices.core.hall;
//
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.BDDMockito.*;
// import static org.mockito.Mockito.times;
// import static org.springframework.http.HttpStatus.*;
// import static org.springframework.http.MediaType.*;
// import static reactor.core.publisher.Mono.*;
//
// import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Map;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
// import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
// import org.springframework.context.annotation.Import;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
// import org.springframework.stereotype.Repository;
// import org.springframework.test.context.bean.override.mockito.MockitoBean;
// import org.springframework.test.web.reactive.server.WebTestClient;
//
// import com.performance.api.core.hall.Hall;
// import com.performance.api.core.hall.HallService;
// import com.performance.api.core.hall.Seat;
// import com.performance.api.core.performance.Performance;
// import com.performance.api.core.performance.Schedule;
// import com.performance.microservices.core.hall.services.HallMapper;
// import com.performance.microservices.core.hall.services.HallMapperImpl;
// import com.performance.microservices.core.hall.services.HallServiceImpl;
// import com.performance.storage.hall.mysql.HallEntity;
// import com.performance.storage.hall.mysql.HallRepository;
// import com.performance.storage.hall.mysql.SeatVO;
// import com.performance.util.http.ServiceUtil;
// import com.ticketing.performance.common.SeatType;
//
//
// @WebFluxTest(controllers = HallService.class)
// @Import({HallMapperImpl.class})
// class HallControllerTest {
//
//
// 	@Autowired
// 	private WebTestClient webTestClient;
//
// 	@MockitoBean
// 	private HallRepository repository;
//
// 	@MockitoBean
// 	private ServiceUtil serviceUtil;
//
// 	@Autowired
// 	private HallMapper mapper;
//
// 	@Test
// 	void getHall_success() {
// 		Integer hallId = 1;
//
// 		HallEntity entity = new HallEntity(hallId, "name", List.of());
// 		Hall apiResponse = mapper.entityToApi(entity);
// 		apiResponse.setServiceAddress("Mock Address");
//
// 		given(repository.findByHallId(hallId)).willReturn(entity);
// 		given(serviceUtil.getServiceAddress()).willReturn("Mock Address");
//
// 		webTestClient.get()
// 			.uri("/hall/{hallId}", hallId)
// 			.accept(APPLICATION_JSON)
// 			.exchange()
// 			.expectStatus().isOk()
// 			.expectBody()
// 			.jsonPath("$.hallId").isEqualTo(hallId)
// 			.jsonPath("$.serviceAddress").isEqualTo("Mock Address");
//
// 		then(repository).should(times(1)).findByHallId(hallId);
// 	}
//
// 	@Test
// 	void getPerformance_invalidId() {
// 		int invalidId = -1;
//
// 		webTestClient.get()
// 			.uri("/hall/{hallId}", invalidId)
// 			.accept(APPLICATION_JSON)
// 			.exchange()
// 			.expectStatus().isEqualTo(422) // UNPROCESSABLE_ENTITY
// 			.expectBody()
// 			.jsonPath("$.message").isEqualTo("Invalid performanceId " + invalidId);
//
// 		then(repository).should(times(0)).findByHallId(any());
// 	}
//
// 	@Test
// 	void createPerformance_success() {
// 		Hall inputHall = new Hall(2, "name", List.of(new Seat(1, 'a', "STANDARD", true)));
//
// 		HallEntity entity = mapper.apiToEntity(inputHall);
// 		HallEntity savedEntity = new HallEntity(entity.getHallId(), entity.getHallName(), entity.getSeatList());
// 		Hall apiResponse = mapper.entityToApi(savedEntity);
//
// 		given(repository.save(any(HallEntity.class))).willReturn(savedEntity);
//
// 		webTestClient.post()
// 			.uri("/hall")
// 			.contentType(APPLICATION_JSON)
// 			.bodyValue(inputHall)
// 			.exchange()
// 			.expectStatus().isOk()
// 			.expectBody()
// 			.jsonPath("$.hallId").isEqualTo(apiResponse.getHallId())
// 			.jsonPath("$.hallName").isEqualTo(apiResponse.getHallName());
//
// 		then(repository).should(times(1)).save(any(HallEntity.class));
// 	}
//
// }
