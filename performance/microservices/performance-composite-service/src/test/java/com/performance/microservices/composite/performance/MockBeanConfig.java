package com.performance.microservices.composite.performance;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.performance.microservices.composite.performance.services.PerformanceCompositeIntegration;

@TestConfiguration
public class MockBeanConfig {

	@Bean(name = "integration")
	public PerformanceCompositeIntegration compositeIntegration(){
		return Mockito.mock(PerformanceCompositeIntegration.class);
	}
}
