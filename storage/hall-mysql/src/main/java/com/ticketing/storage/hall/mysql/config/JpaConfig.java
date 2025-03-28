package com.ticketing.storage.hall.mysql.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = "com.ticketing.storage.hall.mysql")
@EnableJpaRepositories(basePackages = "com.ticketing.storage.hall.mysql")
public class JpaConfig {
}
