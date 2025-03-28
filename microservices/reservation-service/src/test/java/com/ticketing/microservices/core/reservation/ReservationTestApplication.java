package com.ticketing.microservices.core.reservation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({TestConfiguration.class})
class ReservationTestApplication {

	@Test
	void contextLoads() {
	}

}
