package com.example.firstproject.entities;


import com.example.firstproject.dto.EmployeeDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long projectId;

    private String title;


    @ManyToMany
    @JoinTable(
            name = "project_members",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")

    )
    private List<EmployeeEntity> members;


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProjectEntity entity = (ProjectEntity) o;
        return Objects.equals(projectId, entity.projectId) && Objects.equals(title, entity.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, title);
    }
}
