package com.example.firstproject.config;


import com.example.firstproject.config.auditors.CustomAware;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "getAuditorAware")
public class MapperConfig {
    @Bean
    public ModelMapper getModelMapper () {
        return new ModelMapper();
    }

    @Bean
    public AuditorAware getAuditorAware() {
        return new CustomAware();
    }
}
