package com.example.firstproject.config.auditors;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class CustomAware implements AuditorAware<String> {


    @Override
    public Optional<String> getCurrentAuditor() {
//    TODO : Use Spring Security Features to originally gather information about auditor
        return Optional.of("Jashan Preet Singh");
    }
}
