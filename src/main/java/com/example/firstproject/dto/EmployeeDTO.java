package com.example.firstproject.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO extends AuditingAccessDTO {

    private Long empId;
    @NotBlank(message = "Enter a valid employee name")
    @Size(min = 3,max = 10,message = "Employee Name Must Be Under 3 - 10 character")
    private String empName;
    @NotBlank(message = "Enter a valid email address")
    @Email(message = "Please Enter a valid email Address")
    private String empEmail;



}
