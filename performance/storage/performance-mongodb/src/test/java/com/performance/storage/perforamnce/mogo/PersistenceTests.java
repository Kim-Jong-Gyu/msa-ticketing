package com.performance.storage.perforamnce.mogo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.annotation.DirtiesContext;

import org.springframework.dao.DuplicateKeyException;

import com.performance.storage.performance.mongo.config.ConvertConfig;
import com.performance.storage.performance.mongo.PerformanceEntity;
import com.performance.storage.performance.mongo.MongoDbRepository;
import com.performance.storage.performance.mongo.ScheduleVo;
import com.performance.storage.performance.mongo.config.MongoDbConfig;
import com.ticketing.performance.common.SeatType;

// Embedded MongoDB
@DataMongoTest(properties = "de.flapdoodle.mongodb.embedded.version=5.0.5")
@DirtiesContext
@Import({ConvertConfig.class, MongoDbConfig.class})
public class PersistenceTests {

	@Autowired
	private MongoDbRepository repository;

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

		List<ScheduleVo> scheduleVoList = new ArrayList<>();

		scheduleVoList.add(new ScheduleVo(1, LocalDateTime.now().plusDays(4)));
		scheduleVoList.add(new ScheduleVo(2, LocalDateTime.now().plusDays(5)));

		PerformanceEntity newEntity = new PerformanceEntity(DEFAULT_PERFORMANCE_ID, title, pricePolicies, bookingStartDate,
			bookingEndDate, scheduleVoList);

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

		List<ScheduleVo> scheduleVoList = new ArrayList<>();

		scheduleVoList.add(new ScheduleVo(1, LocalDateTime.now().plusDays(4)));
		scheduleVoList.add(new ScheduleVo(2, LocalDateTime.now().plusDays(5)));

		PerformanceEntity newEntity = new PerformanceEntity(performanceId, title, pricePolicies, bookingStartDate,
			bookingEndDate, scheduleVoList);

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

		List<ScheduleVo> scheduleVoList = new ArrayList<>();

		scheduleVoList.add(new ScheduleVo(1, LocalDateTime.now().plusDays(4)));
		scheduleVoList.add(new ScheduleVo(2, LocalDateTime.now().plusDays(5)));

		PerformanceEntity entity = new PerformanceEntity(DEFAULT_PERFORMANCE_ID, title, pricePolicies,
			bookingStartDate,
			bookingEndDate, scheduleVoList);
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
