package com.example.firstproject.services;

import com.example.firstproject.dto.ProjectDto;
import com.example.firstproject.entities.EmployeeEntity;
import com.example.firstproject.entities.ProjectEntity;
import com.example.firstproject.globalhandler.utils.customexceptions.ResourceNotFound;
import com.example.firstproject.repositories.EmployeeRepository;
import com.example.firstproject.repositories.ProjectRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger log = LoggerFactory.getLogger(ProjectService.class);

    //    Util Functions
    private void isValid(Long projectId) {
        log.debug("Checking Whether Project exists or not");
        boolean result = repo.existsById(projectId);
        if (!result) {
            log.debug("The Project Was not found with provided id");
            throw new ResourceNotFound("Please Enter a valid project ID");
        }
        log.debug("The Project Was Found with provided id :)");
    }


    public ProjectService(ProjectRepository repo, EmployeeRepository empRepo, ModelMapper mapper, EmployeeService empService) {
        this.repo = repo;
        this.empRepo = empRepo;
        this.mapper = mapper;
        this.empService = empService;
    }


    public List<ProjectDto> getAllProjects() {
        log.info("Called Get All Project Function");
        log.debug("Trying to get All project Information");
        List<ProjectEntity> list = repo.findAll();
        List<ProjectDto> result = list.stream().map(
                projectEntity -> mapper.map(projectEntity, ProjectDto.class)
        ).collect(Collectors.toList());
        log.trace("Successfully gathered Project List : {} " ,result);
        log.info("Returning The gathered Project List :)");

        return result;

    }

    public ProjectDto createNewProject(ProjectDto data) {
        log.info("Called Create New Project Function");
        log.debug("Trying to Create new project ");

        ProjectEntity entity = mapper.map(data, ProjectEntity.class);
        ProjectEntity result = repo.save(entity);
        log.trace("Successfully Created new Project : {} ",result);
        log.info("Returning new created project :)");
        return mapper.map(result, ProjectDto.class);
    }


    public ProjectDto updateProject(Long proectId, ProjectDto data) {
        log.info("Called Update Project Function");

        isValid(proectId);
        log.debug("Trying to Update project ");

        ProjectEntity entity = mapper.map(data, ProjectEntity.class);
        entity.setProjectId(proectId);
        ProjectEntity result = repo.save(entity);
        log.trace("Successfully updated project : {} ",result);
        log.info("Returning Updated Project :)");
        return mapper.map(result, ProjectDto.class);


    }

    public ProjectDto deleteProject(Long projectId) {
        log.info("Called Delete Project Function");
        isValid(projectId);
        log.debug("Trying to Delete project ");

        ProjectEntity entity = repo.findById(projectId).orElse(null);
        ProjectDto result = mapper.map(entity, ProjectDto.class);
        repo.deleteById(projectId);
        log.trace("Successfully Deleted Project : {}",result);
        log.info("Returning The Deleted Project info :)");
        return result;
    }

    public ProjectDto patchUpdateProject(Long projectId, Map<String, Object> data) {
        log.info("Called Path Update Project function");
        isValid(projectId);
        log.debug("Trying to Patch Update new project ");

        Optional<ProjectEntity> entity = repo.findById(projectId);

        ProjectDto result = entity.map(
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
        log.trace("Successfully Patch Updated Project : {}",result);
        log.info("Returning The patch Updated Project :)");
        return result;
    }

    public ProjectDto assignMembersToProject(Long projectId, Long empId) {
        log.info("Called Assign Member to Project Function");
        log.debug("Trying to Assign new Member to project ");

        isValid(projectId);
        empService.doesExist(empId);

        Optional<ProjectEntity> projectEntity = repo.findById(projectId);
        Optional<EmployeeEntity> employeeEntity = empRepo.findById(empId);

        ProjectDto project = projectEntity.flatMap(
                projectEntity1 ->
                        employeeEntity.map(
                                employeeEntity1 -> {
                                    projectEntity1.getMembers().add(employeeEntity1);
                                    ProjectEntity result = repo.save(projectEntity1);

                                    return mapper.map(result, ProjectDto.class);
                                })).orElse(null);
        log.trace("Successfully Assigned Member to Project :{}",project);
        log.info("Returning  Project to which member assigned :)");

        return project;

    }

    public ProjectDto getProjectById(Long projectId) {
        log.info("Called Get Project By Id Function");
        log.debug("Trying to Get Project By Id ");

        Optional<ProjectEntity> entity = repo.findById(projectId);

        ProjectDto result =  entity.map(
                projectEntity -> mapper.map(projectEntity, ProjectDto.class)
        ).orElse(null);


        log.trace("Successfully Gathered Project Info By Id : {} ",result);
        log.info("Returning Project Finded By Id");

        return result;
    }
}
