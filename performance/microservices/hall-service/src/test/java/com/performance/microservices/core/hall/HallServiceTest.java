package com.performance.microservices.core.hall;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.performance.api.core.hall.Hall;
import com.performance.api.core.hall.Seat;
import com.performance.microservices.core.hall.services.HallMapper;
import com.performance.microservices.core.hall.services.HallMapperImpl;
import com.performance.microservices.core.hall.services.HallServiceImpl;
import com.performance.storage.hall.mysql.persistence.HallEntity;
import com.performance.storage.hall.mysql.persistence.HallRepository;
import com.performance.storage.hall.mysql.persistence.SeatVO;
import com.performance.util.exceptions.InvalidInputException;
import com.performance.util.http.ServiceUtil;
import com.performance.common.SeatType;

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
	void getHall_validId_success(){
		// given
		Integer hallId = 1;
		HallEntity mockEntity = createMockHallEntity(hallId);

		Hall expectedHall = mapper.entityToApi(mockEntity);

		given(repository.findByHallId(hallId)).willReturn(mockEntity);
		given(serviceUtil.getServiceAddress()).willReturn("localhost");

		// when & then
		webTestClient.get()
			.uri("/hall/{id}", hallId)
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.hallId").isEqualTo(expectedHall.getHallId())
			.jsonPath("$.serviceAddress").isEqualTo("localhost");

		then(repository).should(times(1)).findByHallId(hallId);
	}


	@Test
	void getHall_invalidId_throwsException() {
		// Given
		int invalidHallId = -1;

		webTestClient.get()
			.uri("/hall/{hallId}", invalidHallId)
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isEqualTo(422) // UNPROCESSABLE_ENTITY
			.expectBody()
			.jsonPath("$.message").isEqualTo("Invalid hallId: " + invalidHallId);

		then(repository).should(times(0)).findByHallId(any());

	}

	@Test
	void createPerformance_success() {
		// Given
		Integer hallId = 2;
		Hall input = createMockHall(hallId);

		HallEntity entity = mapper.apiToEntity(input);

		HallEntity savedEntity = createMockHallEntity(hallId);

		Hall response = mapper.entityToApi(savedEntity);

		given(repository.save(any(HallEntity.class))).willReturn(savedEntity);


		// When
		webTestClient.post()
			.uri("/hall")
			.contentType(APPLICATION_JSON)
			.bodyValue(input)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.hallId").isEqualTo(response.getHallId())
			.jsonPath("$.hallName").isEqualTo(response.getHallName());

		then(repository).should(times(1)).save(any(HallEntity.class));
	}



	private HallEntity createMockHallEntity(Integer hallId) {
		String hallName = "name";
		List<SeatVO> seatVOList = new ArrayList<>();
		char[] section = {'A', 'B', 'C', 'D'};
		for (char c : section) {
			for (int j = 1; j <= 10; j++) {
				seatVOList.add(new SeatVO(j, c, SeatType.STANDARD));
			}
		}
		return new HallEntity(hallId, hallName, seatVOList);
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
		return new Hall(hallId, hallName, seatList);
	}
}
