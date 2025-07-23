    package com.example.firstproject.services;


    import com.example.firstproject.dto.DepartmentDto;
    import com.example.firstproject.dto.EmployeeDTO;
    import com.example.firstproject.dto.ProjectDto;
    import com.example.firstproject.entities.DepartmentEntity;
    import com.example.firstproject.entities.EmployeeEntity;
    import com.example.firstproject.entities.ProjectEntity;
    import com.example.firstproject.repositories.DepartmentRepository;
    import com.example.firstproject.repositories.EmployeeRepository;
    import com.example.firstproject.repositories.ProjectRepository;
    import jakarta.persistence.EntityManager;
    import lombok.RequiredArgsConstructor;
    import org.hibernate.envers.AuditReader;
    import org.hibernate.envers.AuditReaderFactory;
    import org.modelmapper.ModelMapper;
    import org.springframework.stereotype.Service;

    import java.util.List;

    @Service
    @RequiredArgsConstructor
    public class AdminService {

        private final DepartmentRepository depRepo;
        private final EmployeeRepository empRepo;
        private final ProjectRepository projectRepo;
        private final ModelMapper mapper;
        private final EntityManager manager;

        private AuditReader reader;
        public List<DepartmentDto> getDepartmentChangesByID(Long depId) {
            reader = AuditReaderFactory.get(manager);
            List<Number> numsList = reader.getRevisions(DepartmentEntity.class,depId);
            return numsList.stream().map(
                    number -> mapper.map(reader.find(DepartmentEntity.class,depId,number), DepartmentDto.class)
            ).toList();
        }

        public List<EmployeeDTO> getEmployeeChangesByID(Long empId) {
            reader = AuditReaderFactory.get(manager);
            List<Number> numsList = reader.getRevisions(EmployeeEntity.class,empId);
            return numsList.stream().map(
                    number -> mapper.map(reader.find(EmployeeEntity.class,empId,number), EmployeeDTO.class)
            ).toList();
        }

        public List<ProjectDto> getProjectChangesById(Long projectId) {
            reader = AuditReaderFactory.get(manager);
            List<Number> numsList = reader.getRevisions(ProjectEntity.class,projectId);
            return numsList.stream().map(
                    number -> mapper.map(reader.find(ProjectEntity.class,projectId,number), ProjectDto.class)
            ).toList();
        }
    }
