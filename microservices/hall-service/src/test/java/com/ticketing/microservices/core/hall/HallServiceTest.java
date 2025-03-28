package com.ticketing.microservices.core.hall;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.ticketing.api.core.hall.Hall;
import com.ticketing.api.core.hall.HallWithSeat;
import com.ticketing.api.core.hall.HallWithUnavailable;
import com.ticketing.api.core.hall.Seat;
import com.ticketing.microservices.core.hall.services.HallMapper;
import com.ticketing.microservices.core.hall.services.HallMapperImpl;
import com.ticketing.microservices.core.hall.services.HallServiceImpl;
import com.ticketing.storage.hall.mysql.persistence.HallEntity;
import com.ticketing.storage.hall.mysql.persistence.HallRepository;
import com.ticketing.storage.hall.mysql.persistence.SeatVO;
import com.ticketing.util.http.ServiceUtil;
import com.ticketing.common.SeatType;

@WebFluxTest(controllers = HallServiceImpl.class)
@Import({HallMapperImpl.class})
@ContextConfiguration(classes = HallTestApplication.class)
public class HallServiceTest {

	@Autowired
	private WebTestClient webTestClient;

	@MockitoBean
	private HallRepository repository;

	@Autowired
	private HallMapper mapper;

	@MockitoBean
	private ServiceUtil serviceUtil;

	@Test
	void getHallWithSeat_validId_success(){
		// given
		Integer hallId = 1;
		HallEntity mockEntity = creatHallEntity(hallId);

		HallWithSeat expectedHall = mapper.entityToHallWithSeat(mockEntity);

		given(repository.findByHallIdWithSeat(hallId)).willReturn(mockEntity);
		given(serviceUtil.getServiceAddress()).willReturn("localhost");

		// when & then
		webTestClient.get()
			.uri("/hall/seat/{hallId}", hallId)
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.hallId").isEqualTo(expectedHall.getHallId())
			.jsonPath("$.seatList.length()").isEqualTo(40)
			.jsonPath("$.serviceAddress").isEqualTo("localhost");

		then(repository).should(times(1)).findByHallIdWithSeat(hallId);
	}


	@Test
	void getHallWithSeat_invalidId_throwsException() {
		// Given
		int invalidHallId = -1;

		webTestClient.get()
			.uri("/hall/seat/{hallId}", invalidHallId)
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isEqualTo(422) // UNPROCESSABLE_ENTITY
			.expectBody()
			.jsonPath("$.message").isEqualTo("Invalid hallId: " + invalidHallId);

		then(repository).should(times(0)).findByHallId(any());
	}

	@Test
	void getHallWithUnavailable_success() {
		// given
		Integer hallId = 1;
		HallEntity mockEntity = creatHallEntityWithUnavailable(hallId);

		HallWithUnavailable expectedHall = mapper.entityToHallWithUnavailable(mockEntity);

		given(repository.findByHallIdWithUnavailableDateList(hallId)).willReturn(mockEntity);
		given(serviceUtil.getServiceAddress()).willReturn("localhost");

		webTestClient.get()
			.uri("/hall/unavailable/{hallId}", hallId)
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.hallId").isEqualTo(expectedHall.getHallId())
			.jsonPath("$.unavailableDateList.length()").isEqualTo(expectedHall.getUnavailableDateList().size())
			.jsonPath("$.serviceAddress").isEqualTo("localhost");

		then(repository).should(times(1)).findByHallIdWithUnavailableDateList(hallId);
	}

	@Test
	void createHall_success() {
		// Given
		Integer hallId = 2;
		Hall input = createMockHall(hallId);
		HallEntity savedEntity = mapper.apiToEntity(input);

		given(repository.save(any(HallEntity.class))).willReturn(savedEntity);

		// When
		webTestClient.post()
			.uri("/hall")
			.contentType(APPLICATION_JSON)
			.bodyValue(input)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.hallId").isEqualTo(savedEntity.getHallId())
			.jsonPath("$.hallName").isEqualTo(savedEntity.getHallName());

		then(repository).should(times(1)).save(any(HallEntity.class));
	}


	private HallEntity creatHallEntityWithUnavailable(Integer hallId) {
		String hallName = "name";
		return new HallEntity(hallId, hallName, new ArrayList<>(),List.of(LocalDateTime.now(), LocalDateTime.now().plusDays(2)));
	}


	private HallEntity creatHallEntity(Integer hallId) {
		String hallName = "name";
		List<SeatVO> seatVOList = new ArrayList<>();
		char[] section = {'A', 'B', 'C', 'D'};
		for (char c : section) {
			for (int j = 1; j <= 10; j++) {
				seatVOList.add(new SeatVO(j, c, SeatType.STANDARD));
			}
		}
		return new HallEntity(hallId, hallName, seatVOList, new ArrayList<>());
	}

	private Hall createMockHall(Integer hallId) {
		String hallName = "name";
		List<Seat> seatList = new ArrayList<>();
		char[] section = {'A', 'B', 'C', 'D'};
		for (char c : section) {
			for (int j = 1; j <= 10; j++) {
				seatList.add(new Seat(j, c, "STANDARD"));
			}
		}
		return new Hall(hallId, hallName, seatList, new ArrayList<>());
	}
}
