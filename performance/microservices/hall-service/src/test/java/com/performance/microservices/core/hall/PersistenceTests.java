package com.performance.microservices.core.hall;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.performance.api.core.type.SeatType;
import com.performance.microservices.core.hall.persistence.HallEntity;
import com.performance.microservices.core.hall.persistence.HallRepository;
import com.performance.microservices.core.hall.persistence.SeatVO;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class PersistenceTests {


	@Autowired
	private HallRepository repository;

	private static final Integer DEFAULT_HALL_ID = 1;

	@BeforeEach
	public void setUp(){
		// given
		repository.deleteAll();
		String hallName = "name";

		List<SeatVO> seatVOList = new ArrayList<>();
		char[] section = {'A', 'B', 'C', 'D'};
		for (char c : section) {
			for (int j = 1; j <= 10; j++) {
				seatVOList.add(new SeatVO(j, c, SeatType.STANDARD, true));
			}
		}
		HallEntity entity = new HallEntity(DEFAULT_HALL_ID, hallName, seatVOList);
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
				seatVOList.add(new SeatVO(j, c, SeatType.STANDARD, true));
			}
		}

		HallEntity entity = new HallEntity(2, hallName, seatVOList);

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
		HallEntity savedEntity = repository.findByHallId(DEFAULT_HALL_ID).get();
		savedEntity.updateName(newName);
		repository.save(savedEntity);

		// then
		HallEntity foundEntity = repository.findById(savedEntity.getId()).get();

		assertEquals(1, foundEntity.getVersion());
		assertEquals("new name", foundEntity.getHallName());
	}

	@Test
	public void delete() {

		HallEntity hallEntity = repository.findByHallId(DEFAULT_HALL_ID).get();
		repository.delete(hallEntity);

		assertFalse(repository.existsById(hallEntity.getId()));
	}

	@Test
	public void getByPerformanceId() {
		Optional<HallEntity> entity = repository.findByHallId(DEFAULT_HALL_ID);
		assertTrue(entity.isPresent());
	}

	@Test
	public void duplicateError() {
		// given
		String hallName = "new name";

		List<SeatVO> seatVOList = new ArrayList<>();
		char[] section = {'A', 'B', 'C', 'D'};
		for (char c : section) {
			for (int j = 1; j <= 10; j++) {
				seatVOList.add(new SeatVO(j, c, SeatType.STANDARD, true));
			}
		}
		HallEntity entity = new HallEntity(DEFAULT_HALL_ID, hallName, seatVOList);

		// when
		assertThrows(DataIntegrityViolationException.class, () -> repository.save(entity));
	}

	@Test
	public void optimisticLockError(){
		HallEntity entity1 = repository.findByHallId(DEFAULT_HALL_ID).get();
		HallEntity entity2 = repository.findByHallId(DEFAULT_HALL_ID).get();

		// 첫 번째 객체 update
		entity1.updateName("test1");
		repository.save(entity1);

		// 두 번째 객체 update
		try {
			entity2.updateName("test2");
			repository.save(entity2);
			fail("Expected an OptimisticLockingFailureException");
		}catch (OptimisticLockingFailureException e) {}

		// 데이터베이스에서 업데이트된 엔티티를 가져와서 새로운 값을 확인한다.
		HallEntity updateEntity = repository.findByHallId(DEFAULT_HALL_ID).get();
		assertEquals(1, updateEntity.getVersion());
		assertEquals("test1", updateEntity.getHallName());
	}

}
