package com.example.firstproject.repositories;

import com.example.firstproject.entities.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity,Long> {
}
