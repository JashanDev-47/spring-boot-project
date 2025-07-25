package com.example.firstproject.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class AuditingAccessDTO {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

}
