package com.performance.microservices.core.reservation;

import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.performance.api.core.reservation.Reservation;
import com.performance.api.core.reservation.ReservedLine;
import com.performance.common.ReservationStatus;
import com.performance.microservices.core.reservation.services.ReservationMapper;
import com.performance.microservices.core.reservation.services.ReservationMapperImpl;
import com.performance.microservices.core.reservation.services.ReservationServiceImpl;
import com.performance.storage.core.reservation.persistence.ReservationEntity;
import com.performance.storage.core.reservation.persistence.ReservationRepository;
import com.performance.storage.core.reservation.persistence.ReservedLineVO;
import com.performance.util.http.ServiceUtil;

@WebFluxTest(controllers = ReservationServiceImpl.class)
@Import({ReservationMapperImpl.class})
@ContextConfiguration(classes = ReservationTestApplication.class)
public class ReservationServiceTest {

	@Autowired
	private WebTestClient webTestClient;

	@MockitoBean
	private ReservationRepository repository;

	@Autowired
	private ReservationMapper mapper;

	@MockitoBean
	private ServiceUtil serviceUtil;

	@Test
	public void getReservation_ValidId() {
		Integer reservationId = 1;
		ReservationEntity mockEntity = new ReservationEntity(reservationId, 1, 1,
			List.of(new ReservedLineVO(1, "hallName", LocalDateTime.now(), 1, 'a', 14000)),
			ReservationStatus.RESERVED);

		Reservation expectedResponse = mapper.entityToApi(mockEntity);
		expectedResponse.setServiceAddress("localhost");

		given(repository.findByReservationId(reservationId)).willReturn(mockEntity);
		given(serviceUtil.getServiceAddress()).willReturn("localhost");

		webTestClient.get()
			.uri("/reservation/{reservationId}", reservationId)
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.reservationId").isEqualTo(reservationId)
			.jsonPath("$.serviceAddress").isEqualTo(expectedResponse.getServiceAddress());

		then(repository).should(times(1)).findByReservationId(reservationId);
	}

	@Test
	public void getReservation_InvalidId(){
		Integer invalidId = -1;

		webTestClient.get()
			.uri("/reservation/{reservationId}", invalidId)
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isEqualTo(422)
			.expectBody()
			.jsonPath("$.message").isEqualTo("Invalid reservationId: " + invalidId);
	}

	@Test
	public void getReservationListByPerformanceId(){
		Integer performanceId = 1;
		List<ReservationEntity> reservationEntityList = new ArrayList<>();
		reservationEntityList.add(new ReservationEntity(1, 1, performanceId,
			List.of(new ReservedLineVO(1, "hallName", LocalDateTime.now(), 1, 'a', 14000)),
			ReservationStatus.RESERVED));
		reservationEntityList.add(new ReservationEntity(2, 2, performanceId,
			List.of(new ReservedLineVO(1, "hallName", LocalDateTime.now(), 2, 'a', 14000)),
			ReservationStatus.RESERVED));

		List<Reservation> responseList = mapper.entityListToApiList(reservationEntityList);
		responseList.forEach(e -> e.setServiceAddress("localhost"));

		given(repository.findByPerformanceId(performanceId)).willReturn(reservationEntityList);
		given(serviceUtil.getServiceAddress()).willReturn("localhost");

		webTestClient.get()
			.uri("/reservation/performance/{performanceId}", performanceId)
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$[0].performanceId").isEqualTo(performanceId)
			.jsonPath("$[0].serviceAddress").isEqualTo("localhost");

		then(repository).should(times(1)).findByPerformanceId(performanceId);
	}

	@Test
	public void createReservation(){
		Integer reservationId = 1;
		Reservation body = new Reservation(reservationId, 1, 1,
			List.of(new ReservedLine(1, "hallName", LocalDateTime.now(), 1, 'a', 14000)),
			"RESERVED");
		ReservationEntity savedEntity = mapper.apiToEntity(body);

		given(repository.save(any(ReservationEntity.class))).willReturn(savedEntity);

		webTestClient.post()
			.uri("/reservation")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(body)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.reservationId").isEqualTo(reservationId)
			.jsonPath("$.reservationStatus").isEqualTo("RESERVED");

		then(repository).should(times(1)).save(any(ReservationEntity.class));

	}
}
