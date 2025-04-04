package com.ticketing.storage.performance.mongo;

import com.ticketing.common.SeatType;
import com.ticketing.storage.performance.mongo.persistence.PerformanceEntity;
import com.ticketing.storage.performance.mongo.persistence.PerformanceRepository;
import com.ticketing.storage.performance.mongo.persistence.PricePolicyVo;
import com.ticketing.storage.performance.mongo.persistence.ScheduleVo;
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
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@DataMongoTest
@AutoConfigureDataMongo
@TestPropertySource(properties = "de.flapdoodle.mongodb.embedded.version=5.0.5")
@DirtiesContext
@ExtendWith(SpringExtension.class)
public class PersistenceTests {

    @Autowired
    private PerformanceRepository repository;

    private static final Integer DEFAULT_PERFORMANCE_ID = 1;

    private PerformanceEntity savedEntity;

    @BeforeEach
    public void setUp() {
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

        savedEntity = repository.save(newEntity);
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
        StepVerifier.create(repository.findByPerformanceId(DEFAULT_PERFORMANCE_ID))
                .expectNextMatches(foundEntity -> arePerformanceEqual(savedEntity, foundEntity))
                .verifyComplete();

        savedEntity.updateTitle(newTitle);
        repository.save(savedEntity);

        // then
        StepVerifier.create(repository.findByPerformanceId(DEFAULT_PERFORMANCE_ID))
                .expectNextMatches(foundEntity ->
                        foundEntity.getVersion() == 1 && foundEntity.getTitle().equals("new title"))
                .verifyComplete();
    }

    @Test
    public void delete() {
        PerformanceEntity foundEntity = repository.findByPerformanceId(DEFAULT_PERFORMANCE_ID).block();
        repository.delete(foundEntity);
        StepVerifier.create(repository.existsById(Objects.requireNonNull(foundEntity).getId()))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    public void getByPerformanceId() {
        StepVerifier.create(repository.findByPerformanceId(savedEntity.getPerformanceId()))
                .expectNextMatches(foundEntity -> arePerformanceEqual(savedEntity, foundEntity))
                .verifyComplete();
    }


    // 낙관적 Lock Test
    @Test
    public void optimisticLockError() {
        PerformanceEntity entity1 = repository.findByPerformanceId(DEFAULT_PERFORMANCE_ID).block();
        PerformanceEntity entity2 = repository.findByPerformanceId(DEFAULT_PERFORMANCE_ID).block();

        // 첫 번째 객체 update
        Objects.requireNonNull(entity1).updateTitle("test1");
        repository.save(entity1);

        // 두 번째 객체 update
        try {
            Objects.requireNonNull(entity2).updateTitle("test2");
            repository.save(entity2);
            fail("Expected an OptimisticLockingFailureException");
        } catch (OptimisticLockingFailureException e) {
        }

        // 데이터베이스에서 업데이트된 엔티티를 가져와서 새로운 값을 확인한다.
        PerformanceEntity updateEntity = repository.findByPerformanceId(DEFAULT_PERFORMANCE_ID).block();
        assertEquals(1, Objects.requireNonNull(updateEntity).getVersion());
        assertEquals("test1", updateEntity.getTitle());

    }

    private boolean arePerformanceEqual(PerformanceEntity expectedEntity, PerformanceEntity actualEntity) {
        return (expectedEntity.getId().equals(actualEntity.getId())) &&
                (Objects.equals(expectedEntity.getVersion(), actualEntity.getVersion())) &&
                (Objects.equals(expectedEntity.getPerformanceId(), actualEntity.getPerformanceId())) &&
                (expectedEntity.getTitle().equals(actualEntity.getTitle())) &&
                (areScheduleListEqual(expectedEntity.getScheduleList(), actualEntity.getScheduleList()));
    }

    private boolean areScheduleListEqual(List<ScheduleVo> expected, List<ScheduleVo> actual) {
        if (expected.size() != actual.size()) return false;

        return IntStream.range(0, expected.size())
                .allMatch(i -> Objects.equals(expected.get(i).getHallId(), actual.get(i).getHallId()));
    }

}
