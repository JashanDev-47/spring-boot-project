package com.example.firstproject.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(
        name = "departmentEntity",
        uniqueConstraints = {
                @UniqueConstraint(name = "dep_unique", columnNames = {"depTitle","depHead"})
        }
)
public class DepartmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long depId;

    @Column(
            name = "depTitle"
    )
    private String depTitle;


    @OneToOne
    @JoinColumn(name = "depHead")
    private EmployeeEntity depHead;

    @OneToMany(mappedBy = "department")
    private List<EmployeeEntity> workers;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DepartmentEntity entity = (DepartmentEntity) o;
        return Objects.equals(depId, entity.depId) && Objects.equals(depTitle, entity.depTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(depId, depTitle);
    }
}
