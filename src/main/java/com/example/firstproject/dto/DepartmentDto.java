package com.example.firstproject.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDto extends AuditingAccessDTO {

    private Long depId;
    private String depTitle;
    private EmployeeDTO depHead;
    private List<EmployeeDTO> workers;
}
