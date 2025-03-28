package com.ticketing.storage.core.reservation;

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

import com.ticketing.common.ReservationStatus;
import com.ticketing.storage.core.reservation.persistence.ReservationEntity;
import com.ticketing.storage.core.reservation.persistence.ReservationJpaRepository;
import com.ticketing.storage.core.reservation.persistence.ReservationRepositoryImpl;
import com.ticketing.storage.core.reservation.persistence.ReservedLineVo;
import com.ticketing.util.exceptions.NotFoundException;

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
	public void findByPerformanceId_success() {
		// 2 번쨰
		List<ReservedLineVo> reservedLineVoList = new ArrayList<>();
		reservedLineVoList.add(
			new ReservedLineVo(4, DEFAULT_HALL_ID, DEFAULT_LOCALDATETIME, 1, 'a',
				12000));
		reservedLineVoList.add(
			new ReservedLineVo(10, DEFAULT_HALL_ID, DEFAULT_LOCALDATETIME, 2, 'b',
				12000));
		reservedLineVoList.add(
			new ReservedLineVo(3, DEFAULT_HALL_ID, DEFAULT_LOCALDATETIME, 2, 'b',
				12000));
		ReservationEntity newEntity = new ReservationEntity(5, 1, reservedLineVoList, ReservationStatus.RESERVED);
		repository.save(newEntity);

		// 3번째
		List<ReservedLineVo> reservedLineVoList2 = new ArrayList<>();
		reservedLineVoList2.add(
			new ReservedLineVo(DEFAULT_PERFORMANCE_ID, DEFAULT_HALL_ID, DEFAULT_LOCALDATETIME, 1, 'a',
				12000));
		reservedLineVoList2.add(
			new ReservedLineVo(DEFAULT_PERFORMANCE_ID, DEFAULT_HALL_ID, DEFAULT_LOCALDATETIME, 2, 'b',
				12000));
		reservedLineVoList2.add(
			new ReservedLineVo(3, DEFAULT_HALL_ID, DEFAULT_LOCALDATETIME, 2, 'b',
				12000));
		ReservationEntity newEntity2 = new ReservationEntity(3, 1, reservedLineVoList2, ReservationStatus.RESERVED);
		repository.save(newEntity2);

		List<ReservationEntity> findEntityList = repository.findByPerformanceId(DEFAULT_PERFORMANCE_ID);
		assertNotNull(findEntityList);
		assertEquals(2, findEntityList.size());
	}

	@Test
	public void optimisticLockError() {
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
		} catch (OptimisticLockingFailureException e) {
		}

		// 데이터베이스에서 업데이트된 엔티티를 가져와서 새로운 값을 확인한다.
		ReservationEntity updateEntity = repository.findByReservationId(DEFAULT_RESERVATION_ID);
		assertEquals(1, updateEntity.getVersion());
		assertEquals(3, updateEntity.getUserId());
	}

	private ReservationEntity makeReservation(Integer reservationId) {
		List<ReservedLineVo> reservedLineVoList = new ArrayList<>();
		reservedLineVoList.add(
			new ReservedLineVo(DEFAULT_PERFORMANCE_ID, DEFAULT_HALL_ID, DEFAULT_LOCALDATETIME, 1, 'a',
				12000));
		reservedLineVoList.add(
			new ReservedLineVo(DEFAULT_PERFORMANCE_ID, DEFAULT_HALL_ID, DEFAULT_LOCALDATETIME, 2, 'b',
				12000));
		reservedLineVoList.add(
			new ReservedLineVo(3, DEFAULT_HALL_ID, DEFAULT_LOCALDATETIME, 2, 'b',
				12000));
		return new ReservationEntity(reservationId, 1, reservedLineVoList,
			ReservationStatus.RESERVED);
	}

}
