package com.example.firstproject.controllers;


import com.example.firstproject.dto.ProjectDto;
import com.example.firstproject.services.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAllProjectList() {
        return ResponseEntity.ok(service.getAllProjects());
    }


    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable Long projectId) {
        return ResponseEntity.ok(service.getProjectById(projectId));
    }


    @PostMapping("/create")
    public ResponseEntity<ProjectDto> createNewProject(@RequestBody ProjectDto data) {
        return new ResponseEntity<>(service.createNewProject(data),HttpStatus.CREATED);
    }

    @PutMapping("/{proectId}")
    public ResponseEntity<ProjectDto> updateProject(@PathVariable Long proectId,@RequestBody ProjectDto data) {
        return ResponseEntity.ok(service.updateProject(proectId,data));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<ProjectDto> deleteProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(service.deleteProject(projectId));
    }

    @PatchMapping("/{proejctId}")
    public ResponseEntity<ProjectDto> patchUpdateProject(@PathVariable Long proejctId, @RequestBody Map<String,Object> data) {

        return ResponseEntity.ok( service.patchUpdateProject(proejctId,data));
    }


    @PatchMapping("/{projectId}/members/{empId}")
    public ResponseEntity<ProjectDto> assignMembersToProject(@PathVariable Long projectId,@PathVariable Long empId) {
        return ResponseEntity.ok(service.assignMembersToProject(projectId,empId));
    }

}
