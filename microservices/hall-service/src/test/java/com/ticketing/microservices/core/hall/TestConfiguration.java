package com.ticketing.microservices.core.hall;

import com.ticketing.microservices.core.hall.services.HallMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.ticketing.util")
public class TestConfiguration {

    @Bean
    public HallMapper hallMapper(){
        return Mappers.getMapper(HallMapper.class);
    }
}
