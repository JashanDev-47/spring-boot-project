package com.example.firstproject.services;

import com.example.firstproject.dto.ProjectDto;
import com.example.firstproject.entities.EmployeeEntity;
import com.example.firstproject.entities.ProjectEntity;
import com.example.firstproject.globalhandler.utils.customexceptions.ResourceNotFound;
import com.example.firstproject.repositories.EmployeeRepository;
import com.example.firstproject.repositories.ProjectRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository repo;
    private final EmployeeRepository empRepo;
    private final ModelMapper mapper;
    private final EmployeeService empService;


    //    Util Functions
    private void isValid(Long projectId) {
        boolean result = repo.existsById(projectId);
        if (!result) {
            throw new ResourceNotFound("Please Enter a valid project ID");
        }

    }


    public ProjectService(ProjectRepository repo, EmployeeRepository empRepo, ModelMapper mapper, EmployeeService empService) {
        this.repo = repo;
        this.empRepo = empRepo;
        this.mapper = mapper;
        this.empService = empService;
    }


    public List<ProjectDto> getAllProjects() {
        List<ProjectEntity> list = repo.findAll();
        return list.stream().map(
                projectEntity -> mapper.map(projectEntity, ProjectDto.class)
        ).collect(Collectors.toList());

    }

    public ProjectDto createNewProject(ProjectDto data) {
        ProjectEntity entity = mapper.map(data, ProjectEntity.class);
        ProjectEntity resutl = repo.save(entity);
        return mapper.map(resutl, ProjectDto.class);
    }


    public ProjectDto updateProject(Long proectId, ProjectDto data) {
        isValid(proectId);
        ProjectEntity entity = mapper.map(data, ProjectEntity.class);
        entity.setProjectId(proectId);
        ProjectEntity result = repo.save(entity);
        return mapper.map(result, ProjectDto.class);


    }

    public ProjectDto deleteProject(Long projectId) {
        isValid(projectId);
        ProjectEntity entity = repo.findById(projectId).orElse(null);
        ProjectDto result = mapper.map(entity, ProjectDto.class);
        repo.deleteById(projectId);
        return result;
    }

    public ProjectDto patchUpdateProject(Long projectId, Map<String, Object> data) {
        isValid(projectId);
        Optional<ProjectEntity> entity = repo.findById(projectId);

        return entity.map(
                projectEntity -> {
                    data.forEach(
                            (key, value) -> {
                                Field field = ReflectionUtils.findField(ProjectEntity.class, key);
                                field.setAccessible(true);
                                ReflectionUtils.setField(field, projectEntity, value);
                            }
                    );
                    return mapper.map(projectEntity, ProjectDto.class);
                }
        ).orElse(null);

    }

    public ProjectDto assignMembersToProject(Long projectId, Long empId) {
        isValid(projectId);
        empService.doesExist(empId);

        Optional<ProjectEntity> projectEntity = repo.findById(projectId);
        Optional<EmployeeEntity> employeeEntity = empRepo.findById(empId);

        return projectEntity.flatMap(
                projectEntity1 ->
                        employeeEntity.map(
                                employeeEntity1 -> {
                                    projectEntity1.getMembers().add(employeeEntity1);
                                    ProjectEntity result = repo.save(projectEntity1);

                                    return mapper.map(result, ProjectDto.class);
                                })).orElse(null);

    }

    public ProjectDto getProjectById(Long projectId) {
        Optional<ProjectEntity> entity = repo.findById(projectId);

        return entity.map(
                projectEntity -> mapper.map(projectEntity, ProjectDto.class)
        ).orElse(null);

    }
}
