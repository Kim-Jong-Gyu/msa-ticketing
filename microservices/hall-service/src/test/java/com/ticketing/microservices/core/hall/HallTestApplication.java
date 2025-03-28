package com.ticketing.microservices.core.hall;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({TestConfiguration.class})
public class HallTestApplication {
}
