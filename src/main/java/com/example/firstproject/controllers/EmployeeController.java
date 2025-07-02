package com.example.firstproject.controllers;


import com.example.firstproject.dto.EmployeeDTO;
import com.example.firstproject.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    final private EmployeeService service;

    public EmployeeController (EmployeeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<?>> getAllEmployeeDetails() {
        List<EmployeeDTO> listOfEmployees = service.getAllEmployeeDetails();
        return new ResponseEntity<>(listOfEmployees, HttpStatus.OK);
    }

    @GetMapping("/{empId}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long empId) {

        EmployeeDTO data = service.findEmpById(empId);
        return ResponseEntity.ok(data);
    }

    @PostMapping("/create")
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody @Valid EmployeeDTO empDetails) {
        EmployeeDTO data = service.createEmployee(empDetails);
        return new ResponseEntity<>(data,HttpStatus.CREATED);
    }



    @PutMapping("/{empId}")
    public ResponseEntity<EmployeeDTO> updateEmployeeDetails(@PathVariable Long empId,@RequestBody @Valid EmployeeDTO empDetails) {
        EmployeeDTO data = service.updateEmployeeDetails(empId,empDetails);
        return ResponseEntity.ok(data);
    }

    @DeleteMapping("/{empId}")
    public ResponseEntity<EmployeeDTO> deleteEmployee(@PathVariable Long empId) {
        EmployeeDTO data = service.deleteEmployee(empId);
        return ResponseEntity.ok(data);

    }

    @PatchMapping("/{empId}")
    public ResponseEntity<EmployeeDTO> pathUpdateEmployeeData(@PathVariable Long empId, @RequestBody Map<String, Object> userData) {
        EmployeeDTO data = service.patchUpdateData(empId,userData);
        return ResponseEntity.ok(data);
    }

}
