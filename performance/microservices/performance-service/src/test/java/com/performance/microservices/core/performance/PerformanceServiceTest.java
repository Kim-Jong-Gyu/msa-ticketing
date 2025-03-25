package com.performance.microservices.core.performance;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.performance.api.core.performance.Performance;
import com.performance.api.core.performance.Schedule;
import com.performance.microservices.core.performance.services.PerformanceMapper;
import com.performance.microservices.core.performance.services.PerformanceServiceImpl;
import com.performance.storage.performance.mongo.PerformanceEntity;
import com.performance.storage.performance.mongo.PerformanceRepository;
import com.performance.storage.performance.mongo.ScheduleVo;
import com.performance.util.exceptions.InvalidInputException;
import com.performance.util.http.ServiceUtil;
import com.ticketing.performance.common.SeatType;

@ExtendWith(MockitoExtension.class)
public class PerformanceServiceTest {

	@Mock
	private PerformanceRepository repository;

	@Mock
	private PerformanceMapper mapper;

	@Mock
	private ServiceUtil serviceUtil;

	@InjectMocks
	private PerformanceServiceImpl performanceService;

	@Test
	void getPerformance_validId_success() {
		// Given
		int performanceId = 1;
		PerformanceEntity mockEntity = createMockPerformanceEntity(performanceId);

		Performance expectedPerformance = createMockPerformance(performanceId);
		expectedPerformance.setServiceAddress("localhost");

		given(repository.findByPerformanceId(performanceId)).willReturn(mockEntity);
		given(mapper.entityToApi(mockEntity)).willReturn(expectedPerformance);
		given(serviceUtil.getServiceAddress()).willReturn("localhost");

		// When
		Performance result = performanceService.getPerformance(performanceId);

		// Then
		assertNotNull(result);
		assertEquals(performanceId, result.getPerformanceId());
		assertEquals("localhost", result.getServiceAddress());

		then(repository).should(times(1)).findByPerformanceId(performanceId);
		then(mapper).should(times(1)).entityToApi(mockEntity);
	}

	@Test
	void getPerformance_invalidId_throwsException() {
		// Given
		int invalidPerformanceId = -1;

		// When
		InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
			performanceService.getPerformance(invalidPerformanceId);
		});

		// Then
		assertEquals("Invalid performanceId " + invalidPerformanceId, exception.getMessage());

		then(repository).should(times(0)).findByPerformanceId(anyInt());
	}

	@Test
	void createPerformance_success() {
		// Given
		Integer performanceId = 2;
		Performance input = createMockPerformance(performanceId);

		PerformanceEntity entityToSave = createMockPerformanceEntity(performanceId);

		PerformanceEntity savedEntity = createMockPerformanceEntity(performanceId);

		Performance expectedPerformance = createMockPerformance(performanceId);

		given(mapper.apiToEntity(input)).willReturn(entityToSave);
		given(repository.save(entityToSave)).willReturn(savedEntity);
		given(mapper.entityToApi(savedEntity)).willReturn(expectedPerformance);

		// When
		Performance result = performanceService.createPerformance(input);

		// Then
		assertNotNull(result);
		assertEquals(input.getPerformanceId(), result.getPerformanceId());

		then(mapper).should(times(1)).apiToEntity(input);
		then(repository).should(times(1)).save(entityToSave);
		then(mapper).should(times(1)).entityToApi(savedEntity);
	}


	private PerformanceEntity createMockPerformanceEntity(Integer performanceId) {
		return new PerformanceEntity(
			performanceId,
			"title",
			Map.of(SeatType.STANDARD, 10000, SeatType.VIP, 14000),
			LocalDateTime.now(),
			LocalDateTime.now().plusDays(1),
			List.of(
				new ScheduleVo(1, LocalDateTime.now().plusDays(4)),
				new ScheduleVo(2, LocalDateTime.now().plusDays(5))
			)
		);
	}

	private Performance createMockPerformance(Integer performanceId) {
		return new Performance(
			performanceId,
			"title",
			Map.of(SeatType.STANDARD, 10000, SeatType.VIP, 14000),
			LocalDateTime.now(),
			LocalDateTime.now().plusDays(1),
			List.of(
				new Schedule(1, LocalDateTime.now().plusDays(4)),
				new Schedule(2, LocalDateTime.now().plusDays(5))
			)
		);
	}

}
