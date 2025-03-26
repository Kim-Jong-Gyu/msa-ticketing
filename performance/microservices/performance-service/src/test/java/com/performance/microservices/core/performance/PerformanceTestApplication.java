package com.performance.microservices.core.performance;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({TestConfiguration.class})
public class PerformanceTestApplication {
}
