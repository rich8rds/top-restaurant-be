package com.richards.mealsapp.utils;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EntityToDtoMapper {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
