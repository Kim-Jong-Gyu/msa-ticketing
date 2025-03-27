package com.performance.storage.core.reservation.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = "com.performance.storage.core.reservation")
@EnableJpaRepositories(basePackages = "com.performance.storage.core.reservation")
public class JpaConfig {
}
