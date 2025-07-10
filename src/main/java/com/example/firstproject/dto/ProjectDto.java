package com.example.firstproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {

    private Long projectId;
    private String title;


    private List<EmployeeDTO> members;
}
