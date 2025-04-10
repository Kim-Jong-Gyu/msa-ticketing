package com.ticketing.microservices.core.hall;

import com.ticketing.microservices.core.hall.services.HallMapper;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@SpringBootApplication
@Import({TestConfiguration.class})
public class HallTestApplication {

    @Bean
    public Scheduler jdbcScheduler() {
        return Schedulers.boundedElastic();
    }
}
