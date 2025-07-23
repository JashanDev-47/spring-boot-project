package com.example.firstproject.controllers;


import com.example.firstproject.dto.DepartmentDto;
import com.example.firstproject.dto.EmployeeDTO;
import com.example.firstproject.dto.ProjectDto;
import com.example.firstproject.entities.DepartmentEntity;
import com.example.firstproject.entities.EmployeeEntity;
import com.example.firstproject.entities.ProjectEntity;
import com.example.firstproject.services.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService service;

    @GetMapping("/dep/{depId}")
    public List<DepartmentDto> getDepartmentChangesByID(@PathVariable Long depId) {
        return service.getDepartmentChangesByID(depId);
    }

    @GetMapping("/emp/{empId}")
    public List<EmployeeDTO> getEmployeeChangesByID(@PathVariable Long empId) {
        return service.getEmployeeChangesByID(empId);
    }

    @GetMapping("/project/{projectId}")
    public List<ProjectDto> getProjectChangesById(@PathVariable Long projectId) {
        return service.getProjectChangesById(projectId);
    }

}
