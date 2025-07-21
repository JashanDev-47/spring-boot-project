package com.example.firstproject.services;


import com.example.firstproject.dto.DepartmentDto;
import com.example.firstproject.dto.EmployeeDTO;
import com.example.firstproject.dto.ProjectDto;
import com.example.firstproject.repositories.DepartmentRepository;
import com.example.firstproject.repositories.EmployeeRepository;
import com.example.firstproject.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final DepartmentRepository depRepo;
    private final EmployeeRepository empRepo;
    private final ProjectRepository projectRepo;


    public List<DepartmentDto> getDepartmentChangesByID(Long depId) {
        return null;
    }

    public List<EmployeeDTO> getEmployeeChangesByID(Long empId) {
        return null;
    }

    public List<ProjectDto> getProjectChangesById(Long projectId) {
        return null;
    }
}
