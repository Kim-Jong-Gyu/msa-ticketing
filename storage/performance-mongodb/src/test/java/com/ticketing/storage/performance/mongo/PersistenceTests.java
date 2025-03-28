package com.ticketing.storage.performance.mongo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ticketing.common.SeatType;
import com.ticketing.storage.performance.mongo.persistence.PerformanceEntity;
import com.ticketing.storage.performance.mongo.persistence.PerformanceRepository;
import com.ticketing.storage.performance.mongo.persistence.PricePolicyVo;
import com.ticketing.storage.performance.mongo.persistence.ScheduleVo;

@DataMongoTest
@AutoConfigureDataMongo
@TestPropertySource(properties = "de.flapdoodle.mongodb.embedded.version=5.0.5")
@DirtiesContext
@ExtendWith(SpringExtension.class)
public class PersistenceTests {

	@Autowired
	private PerformanceRepository repository;

	private static final Integer DEFAULT_PERFORMANCE_ID = 1;


	@BeforeEach
	public void setUp(){
		// given
		repository.deleteAll();
		String title = "title";
		List<PricePolicyVo> pricePolicies = List.of(new PricePolicyVo(SeatType.STANDARD, 10000), new PricePolicyVo(SeatType.VIP, 14000));
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
		List<PricePolicyVo> pricePolicies = List.of(new PricePolicyVo(SeatType.STANDARD, 10000), new PricePolicyVo(SeatType.VIP, 14000));
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
		PerformanceEntity savedEntity = repository.findByPerformanceId(1);
		savedEntity.updateTitle(newTitle);
		repository.save(savedEntity);

		// then
		PerformanceEntity foundEntity = repository.findByPerformanceId(savedEntity.getPerformanceId());

		assertEquals(1, foundEntity.getVersion());
		assertEquals("new title", foundEntity.getTitle());
	}

	@Test
	public void delete() {

		PerformanceEntity performanceEntity = repository.findByPerformanceId(1);
		repository.delete(performanceEntity);

		assertFalse(repository.existsById(performanceEntity.getId()));
	}

	@Test
	public void getByPerformanceId() {
		PerformanceEntity entity = repository.findByPerformanceId(1);
		assertNotNull(entity);
	}


	// 낙관적 Lock Test
	@Test
	public void optimisticLockError(){
		PerformanceEntity entity1 = repository.findByPerformanceId(DEFAULT_PERFORMANCE_ID);
		PerformanceEntity entity2 = repository.findByPerformanceId(DEFAULT_PERFORMANCE_ID);

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
		PerformanceEntity updateEntity = repository.findByPerformanceId(DEFAULT_PERFORMANCE_ID);
		assertEquals(1, updateEntity.getVersion());
		assertEquals("test1", updateEntity.getTitle());

	}

}
