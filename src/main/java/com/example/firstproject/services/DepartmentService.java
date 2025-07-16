package com.example.firstproject.services;


import com.example.firstproject.dto.DepartmentDto;
import com.example.firstproject.entities.DepartmentEntity;
import com.example.firstproject.entities.EmployeeEntity;
import com.example.firstproject.globalhandler.utils.customexceptions.ResourceNotFound;
import com.example.firstproject.repositories.DepartmentRepository;
import com.example.firstproject.repositories.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class DepartmentService {

    private final DepartmentRepository repo;
    private final ModelMapper mapper;
    private final EmployeeRepository empRepo;

    public DepartmentService(DepartmentRepository repo,ModelMapper mapper,EmployeeRepository empRepo) {
        this.repo = repo;
        this.mapper = mapper;
        this.empRepo = empRepo;
    }

    private void depExist(Long depId) {
        boolean result =repo.existsById(depId);
        if (!result) {
            throw new ResourceNotFound("Department ID not found :) ");
        }
    }


    public List<DepartmentDto> getAllDepartment() {
        List<DepartmentEntity> depDetails = repo.findAll();
        List<DepartmentDto> allDepDetails = depDetails.stream().map(
                departmentEntity -> mapper.map(departmentEntity, DepartmentDto.class)
        ).toList();
        return allDepDetails;
    }

    public DepartmentDto createNewDepartment(DepartmentDto depDetails) {
        DepartmentEntity entity = mapper.map(depDetails,DepartmentEntity.class);
        DepartmentEntity result = repo.save(entity);

        return mapper.map(result,DepartmentDto.class);

    }

    public DepartmentDto updateDepartment(Long depId, DepartmentDto depDetails) {

        depExist(depId);
        DepartmentEntity preEntity = repo.findById(depId).orElse(null);
        DepartmentEntity entity = mapper.map(depDetails,DepartmentEntity.class);

        entity.setDepId(depId);
        entity.setDepHead(preEntity.getDepHead());

        DepartmentEntity savedEntity = repo.save(entity);

        return mapper.map(savedEntity, DepartmentDto.class);
    }

    public DepartmentDto deleteDepartment(Long depId) {
        depExist(depId);

        DepartmentEntity deletedDep = repo.findById(depId).orElse(null);
        DepartmentDto result = mapper.map(deletedDep, DepartmentDto.class);

        repo.deleteById(depId);

        return result;
    }


    public DepartmentDto patchUpdateDepartment(Long depId, Map<String, Object> depDetails) {
        depExist(depId);

        Optional<DepartmentEntity> entity = repo.findById(depId);

        return entity.map(
                departmentEntity -> {
                    depDetails.forEach(
                            (key,value) -> {
                                Field field = ReflectionUtils.findField(DepartmentEntity.class,key);
                                field.setAccessible(true);
                                ReflectionUtils.setField(field,departmentEntity,value);
                            }
                    );
                    return mapper.map(repo.save(departmentEntity), DepartmentDto.class);
                }

        ).orElse(null);
    }

    public DepartmentDto updateWorkerList(Long depId, Long empId) {

        Optional<DepartmentEntity> department = repo.findById(depId);
        Optional<EmployeeEntity> employee = empRepo.findById(empId);

        DepartmentEntity entity = department.flatMap(
                departmentEntity ->
                employee.map(
                        employeeEntity -> {
                            List<EmployeeEntity> workerList = departmentEntity.getWorkers();
                            workerList.add(employeeEntity);
                            employeeEntity.setDepartment(departmentEntity);
                            empRepo.save(employeeEntity);
                            departmentEntity.getWorkers().add(employeeEntity);
                            return departmentEntity;
                        }
                )
        ).orElse(null);

        return mapper.map(entity,DepartmentDto.class);
    }

    public DepartmentDto assignHeadOfDepartment(Long depId, Long empId) {
        Optional<DepartmentEntity> department = repo.findById(depId);
        Optional<EmployeeEntity> employee = empRepo.findById(empId);

        return department.flatMap(
                departmentEntity ->
                        employee.map(
                                employeeEntity -> {
                                    departmentEntity.setDepHead(employeeEntity);
                                    DepartmentEntity entity = repo.save(departmentEntity);
                                    return mapper.map(entity, DepartmentDto.class);
                                }
                        )
        ).orElse(null);
    }

    public DepartmentDto getDepartmentById(Long depId) {

        Optional<DepartmentEntity> entity = repo.findById(depId);

        return entity.map(
                departmentEntity -> mapper.map(departmentEntity, DepartmentDto.class)
        ).orElse(null);

    }
}
