package com.performance.microservices.core.hall;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.performance.api.core.hall.Hall;
import com.performance.api.core.hall.Seat;
import com.performance.api.core.performance.Performance;
import com.performance.microservices.core.hall.services.HallMapper;
import com.performance.microservices.core.hall.services.HallServiceImpl;
import com.performance.storage.hall.mysql.HallEntity;
import com.performance.storage.hall.mysql.HallRepository;
import com.performance.storage.hall.mysql.SeatVO;
import com.performance.util.exceptions.InvalidInputException;
import com.performance.util.http.ServiceUtil;
import com.ticketing.performance.common.SeatType;

@ExtendWith(MockitoExtension.class)
public class HallServiceTest {

	@Mock
	private HallRepository repository;

	@Mock
	private HallMapper mapper;

	@Mock
	private ServiceUtil serviceUtil;

	@InjectMocks
	private HallServiceImpl hallService;


	@Test
	void getHall_validId_success(){
		// given
		Integer hallId = 1;
		HallEntity mockEntity = createMockHallEntity(hallId);

		Hall expectedHall = createMockHall(hallId);
		expectedHall.setServiceAddress("localhost");

		given(repository.findByHallId(hallId)).willReturn(mockEntity);
		given(mapper.entityToApi(mockEntity)).willReturn(expectedHall);
		given(serviceUtil.getServiceAddress()).willReturn("localhost");

		// when

		Hall result = hallService.getHall(hallId);

		// then
		assertNotNull(result);
		assertEquals(hallId, result.getHallId());
		assertEquals("localhost", result.getServiceAddress());

		then(repository).should(times(1)).findByHallId(hallId);
		then(mapper).should(times(1)).entityToApi(mockEntity);
	}


	@Test
	void getPerformance_invalidId_throwsException() {
		// Given
		int invalidPerformanceId = -1;

		// When
		InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
			hallService.getHall(invalidPerformanceId);
		});

		// Then
		assertEquals("Invalid performanceId " + invalidPerformanceId, exception.getMessage());

		then(repository).should(times(0)).findByHallId(anyInt());
	}

	@Test
	void createPerformance_success() {
		// Given
		Integer hallId = 2;
		Hall input = createMockHall(hallId);

		HallEntity entityToSave = createMockHallEntity(hallId);

		HallEntity savedEntity = createMockHallEntity(hallId);

		Hall expectedPerformance = createMockHall(hallId);

		given(mapper.apiToEntity(input)).willReturn(entityToSave);
		given(repository.save(entityToSave)).willReturn(savedEntity);
		given(mapper.entityToApi(savedEntity)).willReturn(expectedPerformance);

		// When
		Hall result = hallService.createHall(input);

		// Then
		assertNotNull(result);
		assertEquals(input.getHallId(), result.getHallId());
		then(mapper).should(times(1)).apiToEntity(input);
		then(repository).should(times(1)).save(entityToSave);
		then(mapper).should(times(1)).entityToApi(savedEntity);
	}



	private HallEntity createMockHallEntity(Integer hallId) {
		String hallName = "name";
		List<SeatVO> seatVOList = new ArrayList<>();
		char[] section = {'A', 'B', 'C', 'D'};
		for (char c : section) {
			for (int j = 1; j <= 10; j++) {
				seatVOList.add(new SeatVO(j, c, SeatType.STANDARD, true));
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
				seatList.add(new Seat(j, c, "STANDARD", true));
			}
		}
		return new Hall(hallId, hallName, seatList);
	}


}
