package com.ticketing.storage.hall.mysql;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ticketing.common.SeatType;
import com.ticketing.storage.hall.mysql.persistence.HallEntity;
import com.ticketing.storage.hall.mysql.persistence.HallJpaRepository;
import com.ticketing.storage.hall.mysql.persistence.HallRepository;
import com.ticketing.storage.hall.mysql.persistence.HallRepositoryImpl;
import com.ticketing.storage.hall.mysql.persistence.SeatVO;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@ActiveProfiles("test")
@Import({HallRepositoryImpl.class})
public class PersistenceTests {

	@Autowired
	HallJpaRepository jpaRepository;


	@Autowired
	HallRepository repository;

	private static final Integer DEFAULT_HALL_ID = 1;

	@BeforeEach
	public void setUp() {
		// given
		repository.deleteAll();
		String hallName = "name";

		List<SeatVO> seatVOList = new ArrayList<>();
		char[] section = {'A', 'B', 'C', 'D'};
		for (char c : section) {
			for (int j = 1; j <= 10; j++) {
				seatVOList.add(new SeatVO(j, c, SeatType.STANDARD));
			}
		}
		HallEntity entity = new HallEntity(DEFAULT_HALL_ID, hallName, seatVOList,
			List.of(LocalDateTime.now(), LocalDateTime.now().plusDays(3)));
		repository.save(entity);
	}

	@Test
	public void create() {

		// given
		String hallName = "new name";

		List<SeatVO> seatVOList = new ArrayList<>();
		char[] section = {'A', 'B', 'C', 'D'};
		for (char c : section) {
			for (int j = 1; j <= 10; j++) {
				seatVOList.add(new SeatVO(j, c, SeatType.STANDARD));
			}
		}

		HallEntity entity = new HallEntity(2, hallName, seatVOList,
			List.of(LocalDateTime.now(), LocalDateTime.now().plusDays(3)));

		// when
		repository.save(entity);

		//then
		assertEquals(2, repository.count());
	}

	@Test
	public void update() {
		// given
		String newName = "new name";

		// when
		HallEntity savedEntity = repository.findByHallId(DEFAULT_HALL_ID);
		savedEntity.updateName(newName);
		repository.save(savedEntity);

		// then
		HallEntity foundEntity = repository.findByHallId(savedEntity.getHallId());

		assertEquals(1, foundEntity.getVersion());
		assertEquals("new name", foundEntity.getHallName());
	}

	@Test
	public void delete() {

		HallEntity hallEntity = repository.findByHallId(DEFAULT_HALL_ID);
		repository.delete(hallEntity);

		assertFalse(repository.existsByHallId(hallEntity.getHallId()));
	}

	@Test
	public void getByHallIdWithSeat() {
		HallEntity entity = repository.findByHallIdWithSeat(DEFAULT_HALL_ID);
		List<SeatVO> seatList = entity.getSeatList();
		assertEquals(40, seatList.size());
	}

	@Test
	public void getByHallIdWithUnavailableList() {
		HallEntity entity = repository.findByHallIdWithUnavailableDateList(DEFAULT_HALL_ID);
		List<LocalDateTime> dateTimeList = entity.getUnavailableDateList();
		assertEquals(2, dateTimeList.size());
	}

	@Test
	public void duplicateError() {
		// given
		String hallName = "new name";

		List<SeatVO> seatVOList = new ArrayList<>();
		char[] section = {'A', 'B', 'C', 'D'};
		for (char c : section) {
			for (int j = 1; j <= 10; j++) {
				seatVOList.add(new SeatVO(j, c, SeatType.STANDARD));
			}
		}
		HallEntity entity = new HallEntity(DEFAULT_HALL_ID, hallName, seatVOList,
			List.of(LocalDateTime.now(), LocalDateTime.now().plusDays(3)));

		// when
		assertThrows(DataIntegrityViolationException.class, () -> repository.save(entity));
	}

	@Test
	public void optimisticLockError() {
		HallEntity entity1 = repository.findByHallId(DEFAULT_HALL_ID);
		HallEntity entity2 = repository.findByHallId(DEFAULT_HALL_ID);

		// 첫 번째 객체 update
		entity1.updateName("test1");
		repository.save(entity1);

		// 두 번째 객체 update
		try {
			entity2.updateName("test2");
			repository.save(entity2);
			fail("Expected an OptimisticLockingFailureException");
		} catch (OptimisticLockingFailureException e) {
		}

		// 데이터베이스에서 업데이트된 엔티티를 가져와서 새로운 값을 확인한다.
		HallEntity updateEntity = repository.findByHallId(DEFAULT_HALL_ID);
		assertEquals(1, updateEntity.getVersion());
		assertEquals("test1", updateEntity.getHallName());
	}

}