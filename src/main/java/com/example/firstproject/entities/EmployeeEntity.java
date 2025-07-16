package com.example.firstproject.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(
        name = "employeeEntity",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "empEmail")
        }
)
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long empId;

    @Column(name = "empName")
    private String empName;

    @Column(name = "empEmail")
    private String empEmail;

    @OneToOne(mappedBy = "depHead")
    private DepartmentEntity headDepartment;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "department")
    private DepartmentEntity department;


    @ManyToMany(mappedBy = "members")
    private List<ProjectEntity> projects;


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeEntity that = (EmployeeEntity) o;
        return Objects.equals(empId, that.empId) && Objects.equals(empName, that.empName) && Objects.equals(empEmail, that.empEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empId, empName, empEmail);
    }
}
