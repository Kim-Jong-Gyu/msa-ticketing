package com.performance.microservices.core.performance;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.annotation.DirtiesContext;

import org.springframework.dao.DuplicateKeyException;
import com.performance.api.core.type.SeatType;
import com.performance.microservices.core.performance.persistence.MongoConfig;
import com.performance.microservices.core.performance.persistence.PerformanceEntity;
import com.performance.microservices.core.performance.persistence.PerformanceRepository;
import com.performance.microservices.core.performance.persistence.Schedule;

// Embedded MongoDB
@DataMongoTest(properties = "de.flapdoodle.mongodb.embedded.version=5.0.5")
@DirtiesContext
@Import(MongoConfig.class)
public class PersistenceTests {

	@Autowired
	private PerformanceRepository repository;


	private static final Integer DEFAULT_PERFORMANCE_ID = 1;
	@BeforeEach
	public void setUp(){
		// given
		repository.deleteAll();
		String title = "title";
		Map<SeatType, Integer> pricePolicies = new HashMap<>();
		pricePolicies.put(SeatType.STANDARD, 10000);
		pricePolicies.put(SeatType.VIP, 14000);
		LocalDateTime bookingStartDate = LocalDateTime.now();
		LocalDateTime bookingEndDate = LocalDateTime.now().plusDays(1);

		List<Schedule> scheduleList = new ArrayList<>();

		scheduleList.add(new Schedule(1, LocalDateTime.now().plusDays(4)));
		scheduleList.add(new Schedule(2, LocalDateTime.now().plusDays(5)));

		PerformanceEntity newEntity = new PerformanceEntity(DEFAULT_PERFORMANCE_ID, title, pricePolicies, bookingStartDate,
			bookingEndDate, scheduleList);

		repository.save(newEntity);
	}


	@Test
	public void create() {

		// given
		Integer performanceId = 2;
		String title = "title2";
		Map<SeatType, Integer> pricePolicies = new HashMap<>();
		pricePolicies.put(SeatType.STANDARD, 10000);
		pricePolicies.put(SeatType.VIP, 14000);
		LocalDateTime bookingStartDate = LocalDateTime.now();
		LocalDateTime bookingEndDate = LocalDateTime.now().plusDays(1);

		List<Schedule> scheduleList = new ArrayList<>();

		scheduleList.add(new Schedule(1, LocalDateTime.now().plusDays(4)));
		scheduleList.add(new Schedule(2, LocalDateTime.now().plusDays(5)));

		PerformanceEntity newEntity = new PerformanceEntity(performanceId, title, pricePolicies, bookingStartDate,
			bookingEndDate, scheduleList);

		// when
		repository.save(newEntity);

		//then
		assertEquals(2, repository.count());

	}

	@Test
	public void update() {
		// given
		String newTitle = "new title";

		// when

		PerformanceEntity savedEntity = repository.findByPerformanceId(1).get();
		savedEntity.updateTitle(newTitle);
		repository.save(savedEntity);

		// then
		PerformanceEntity foundEntity = repository.findById(savedEntity.getId()).get();

		assertEquals(1, foundEntity.getVersion());
		assertEquals("new title", foundEntity.getTitle());
	}

	@Test
	public void delete() {

		PerformanceEntity performanceEntity = repository.findByPerformanceId(1).get();
		repository.delete(performanceEntity);

		assertFalse(repository.existsById(performanceEntity.getId()));
	}

	@Test
	public void getByPerformanceId() {
		Optional<PerformanceEntity> entity = repository.findByPerformanceId(1);
		assertTrue(entity.isPresent());
	}

	@Test
	public void duplicateError() {
		// given
		String title = "title";
		Map<SeatType, Integer> pricePolicies = new HashMap<>();
		pricePolicies.put(SeatType.STANDARD, 10000);
		pricePolicies.put(SeatType.VIP, 14000);
		LocalDateTime bookingStartDate = LocalDateTime.now();
		LocalDateTime bookingEndDate = LocalDateTime.now().plusDays(1);

		List<Schedule> scheduleList = new ArrayList<>();

		scheduleList.add(new Schedule(1, LocalDateTime.now().plusDays(4)));
		scheduleList.add(new Schedule(2, LocalDateTime.now().plusDays(5)));

		PerformanceEntity entity = new PerformanceEntity(DEFAULT_PERFORMANCE_ID, title, pricePolicies,
			bookingStartDate,
			bookingEndDate, scheduleList);
		// when
		assertThrows(DuplicateKeyException.class, () -> repository.save(entity));
	}


	// 낙관적 Lock Test
	@Test
	public void optimisticLockError(){
		PerformanceEntity entity1 = repository.findByPerformanceId(DEFAULT_PERFORMANCE_ID).get();
		PerformanceEntity entity2 = repository.findByPerformanceId(DEFAULT_PERFORMANCE_ID).get();

		// 첫 번째 객체 update
		entity1.updateTitle("test1");
		repository.save(entity1);

		// 두 번째 객체 update
		try {
			entity2.updateTitle("test2");
			repository.save(entity2);
			fail("Expected an OptimisticLockingFailureException");
		}catch (OptimisticLockingFailureException e) {}

		// 데이터베이스에서 업데이트된 엔티티를 가져와서 새로운 값을 확인한다.
		PerformanceEntity updateEntity = repository.findByPerformanceId(DEFAULT_PERFORMANCE_ID).get();
		assertEquals(1, updateEntity.getVersion());
		assertEquals("test1", updateEntity.getTitle());

	}

}
