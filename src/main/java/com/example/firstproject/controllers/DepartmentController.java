package com.example.firstproject.controllers;


import com.example.firstproject.dto.DepartmentDto;
import com.example.firstproject.services.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentService service;

    public DepartmentController(DepartmentService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<?>> getAllDepartment() {
        return ResponseEntity.ok(service.getAllDepartment());
    }

    @GetMapping("/{depId}")
    public ResponseEntity<DepartmentDto> getDepartmentById (@PathVariable Long depId) {
        return ResponseEntity.ok(service.getDepartmentById(depId));
    }

    @PostMapping("/create")
    public ResponseEntity<DepartmentDto> createNewDepartment(@RequestBody DepartmentDto depDetails){
        return new ResponseEntity<>(service.createNewDepartment(depDetails), HttpStatus.CREATED);
    }

    @PutMapping("/{depId}")
    public ResponseEntity<DepartmentDto> updateDepartment(@PathVariable Long depId,@RequestBody DepartmentDto depDetails) {
        return ResponseEntity.ok(service.updateDepartment(depId,depDetails));
    }

    @DeleteMapping("/{depId}")
    public ResponseEntity<DepartmentDto> deleteDepartment(@PathVariable Long depId) {
        return ResponseEntity.ok(service.deleteDepartment(depId));
    }

    @PatchMapping("/{depId}")
    public ResponseEntity<DepartmentDto> patchUpdateDepartment(@PathVariable Long depId, @RequestBody Map<String , Object> depDetails) {
        return ResponseEntity.ok(service.patchUpdateDepartment(depId,depDetails));
    }

    @PatchMapping("/{depId}/worker/{empId}")
    public ResponseEntity<DepartmentDto> updateWorkerList (@PathVariable Long depId,@PathVariable Long empId) {
        return ResponseEntity.ok(service.updateWorkerList(depId,empId));
    }

    @PatchMapping("/{depId}/head/{empId}")
    public ResponseEntity<DepartmentDto> assignHeadOfDepartment(@PathVariable Long depId,@PathVariable Long empId) {
        return ResponseEntity.ok(service.assignHeadOfDepartment(depId,empId));
    }
}
