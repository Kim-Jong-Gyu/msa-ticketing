package com.performance.storage.core.reservation;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.performance.common.ReservationStatus;
import com.performance.storage.core.reservation.persistence.ReservationEntity;
import com.performance.storage.core.reservation.persistence.ReservationJpaRepository;
import com.performance.storage.core.reservation.persistence.ReservationRepositoryImpl;
import com.performance.storage.core.reservation.persistence.ReservedLineVO;
import com.performance.util.exceptions.NotFoundException;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@ActiveProfiles("test")
@Import({ReservationRepositoryImpl.class})
public class PersistenceTests {

	@Autowired
	private ReservationJpaRepository jpaRepository;

	@Autowired
	private ReservationRepositoryImpl repository;

	private static final Integer DEFAULT_RESERVATION_ID = 1;

	private static final LocalDateTime DEFAULT_LOCALDATETIME = LocalDateTime.now();

	private static final Integer DEFAULT_PERFORMANCE_ID = 1;

	private static final Integer DEFAULT_HALL_ID = 1;

	@BeforeEach
	public void setUp() {
		repository.deleteAll();
		ReservationEntity entity = makeReservation(DEFAULT_RESERVATION_ID);
		repository.save(entity);
	}

	@Test
	public void create() {
		ReservationEntity entity = makeReservation(2);
		ReservationEntity savedEntity = repository.save(entity);

		assertEquals(savedEntity.getReservationId(), 2);
	}

	@Test
	public void update() {
		ReservationEntity findEntity = repository.findByReservationId(DEFAULT_RESERVATION_ID);
		findEntity.updateUser(2);
		repository.save(findEntity);

		ReservationEntity result = repository.findByReservationId(DEFAULT_RESERVATION_ID);
		assertEquals(result.getUserId(), 2);
	}

	@Test
	public void delete() {
		ReservationEntity findEntity = repository.findByReservationId(DEFAULT_RESERVATION_ID);
		repository.delete(findEntity);

		assertThrows(NotFoundException.class, () -> repository.findByReservationId(DEFAULT_RESERVATION_ID));
	}

	@Test
	public void findByPerformanceIdAndHallIdAndPerformanceDate() {
		ReservationEntity newEntity = makeReservation(2);
		repository.save(newEntity);
		List<ReservationEntity> findEntity = repository.findByPerformanceIdAndHallIdAndPerformanceDate(
			DEFAULT_PERFORMANCE_ID, DEFAULT_HALL_ID, DEFAULT_LOCALDATETIME);
		assertEquals(findEntity.size(), 2);
	}

	@Test
	public void findByReservationId_success() {

		ReservationEntity findEntity = repository.findByReservationId(DEFAULT_RESERVATION_ID);
		assertNotNull(findEntity);
	}

	@Test
	public void findByPerformanceId_success(){
		List<ReservationEntity> findEntityList = repository.findByPerformanceId(DEFAULT_PERFORMANCE_ID);

		assertNotNull(findEntityList);
		assertEquals(1, findEntityList.size());
	}

	@Test
	public void optimisticLockError(){
		ReservationEntity entity1 = repository.findByReservationId(DEFAULT_RESERVATION_ID);
		ReservationEntity entity2 = repository.findByReservationId(DEFAULT_RESERVATION_ID);

		// 첫 번째 객체 update
		entity1.updateUser(3);
		repository.save(entity1);

		// 두 번째 객체 update
		try {
			entity2.updateUser(2);
			repository.save(entity2);
			fail("Expected an OptimisticLockingFailureException");
		}catch (OptimisticLockingFailureException e) {}

		// 데이터베이스에서 업데이트된 엔티티를 가져와서 새로운 값을 확인한다.
		ReservationEntity updateEntity = repository.findByReservationId(DEFAULT_RESERVATION_ID);
		assertEquals(1, updateEntity.getVersion());
		assertEquals(3, updateEntity.getUserId());
	}


	private ReservationEntity makeReservation(Integer reservationId) {
		List<ReservedLineVO> reservedLineVOList = new ArrayList<>();
		reservedLineVOList.add(new ReservedLineVO(DEFAULT_HALL_ID, "hallName", DEFAULT_LOCALDATETIME, 1, 'a', 12000));
		reservedLineVOList.add(new ReservedLineVO(DEFAULT_HALL_ID, "hallName", DEFAULT_LOCALDATETIME, 2, 'b', 12000));
		return new ReservationEntity(reservationId, 1, DEFAULT_PERFORMANCE_ID, reservedLineVOList,
			ReservationStatus.RESERVED);
	}

}
