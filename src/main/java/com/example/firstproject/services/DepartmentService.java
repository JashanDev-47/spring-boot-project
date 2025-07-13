package com.example.firstproject.services;


import com.example.firstproject.dto.DepartmentDto;
import com.example.firstproject.entities.DepartmentEntity;
import com.example.firstproject.entities.EmployeeEntity;
import com.example.firstproject.globalhandler.utils.customexceptions.ResourceNotFound;
import com.example.firstproject.repositories.DepartmentRepository;
import com.example.firstproject.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository repo;
    private final ModelMapper mapper;
    private final EmployeeRepository empRepo;


    private Logger log = LoggerFactory.getLogger(DepartmentService.class);




    private void depExist(Long depId) {
        log.debug("Checking Whether the dep exists or not");
        boolean result =repo.existsById(depId);
        if (!result) {
            log.debug("The department with provided id not found :(");
            throw new ResourceNotFound("Department ID not found :) ");
        }
        log.debug("Department Successfully Found :)");
    }


    public List<DepartmentDto> getAllDepartment() {
        log.info("Called Get All Department Function Called");
        log.debug("Making API call to get all deparment info");
        List<DepartmentEntity> depDetails = repo.findAll();
        List<DepartmentDto> allDepDetails = depDetails.stream().map(
                departmentEntity -> mapper.map(departmentEntity, DepartmentDto.class)
        ).toList();
        log.trace("Successfully gathered all department infomation : {} " , allDepDetails);

        log.info("Returning department info :)");
        return allDepDetails;
    }

    public DepartmentDto createNewDepartment(DepartmentDto depDetails) {
        log.info("Called Create New Department Function");
        DepartmentEntity entity = mapper.map(depDetails,DepartmentEntity.class);
        log.debug("Trying to Create new deapartment");
        DepartmentEntity result = repo.save(entity);
        log.trace("Successfylly created new department : {} ", result);
        log.info("Returning new Department info :)");
        return mapper.map(result,DepartmentDto.class);

    }

    public DepartmentDto updateDepartment(Long depId, DepartmentDto depDetails) {
        log.info("Called Update Department information function");
        depExist(depId);
        depDetails.setDepId(depId);
        DepartmentEntity entity = mapper.map(depDetails,DepartmentEntity.class);
        log.debug("Trying to update Department info");
        DepartmentEntity savedEntity = repo.save(entity);
        log.trace("Successfully Update Department info :{}",savedEntity);
        log.info("Returning updated Department info :)");
        return mapper.map(savedEntity, DepartmentDto.class);
    }

    public DepartmentDto deleteDepartment(Long depId) {
        log.info("Called Delete Department information function");
        depExist(depId);

        DepartmentEntity deletedDep = repo.findById(depId).orElse(null);
        DepartmentDto result = mapper.map(deletedDep, DepartmentDto.class);
        log.debug("Trying to delete Department info");
        repo.deleteById(depId);
        log.trace("Successfully deleted Department info : {}",result);
        log.info("Returning deleted Department info :)");
        return result;
    }


    public DepartmentDto patchUpdateDepartment(Long depId, Map<String, Object> depDetails) {
        log.info("Called Patch Update Department information function");
        depExist(depId);

        Optional<DepartmentEntity> entity = repo.findById(depId);
        log.debug("Trying to Patch Department info");
        DepartmentDto result = entity.map(
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
        log.trace("Successfully Patch updated Department info : {}",result);
        log.info("Returning Patch Updated Department info :)");
        return result;
    }

    public DepartmentDto addNewWorker(Long depId, Long empId) {
        log.info("Called Add New Worker function");
        Optional<DepartmentEntity> department = repo.findById(depId);
        Optional<EmployeeEntity> employee = empRepo.findById(empId);
        log.debug("Trying to Add new Worker");
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
        log.trace("Successfully added new Worker : {} ",entity);
        log.info("Returning  added Worker :)");
        return mapper.map(entity,DepartmentDto.class);
    }

    public DepartmentDto assignHeadOfDepartment(Long depId, Long empId) {
        log.info("Called Assign Department Head function");
        Optional<DepartmentEntity> department = repo.findById(depId);
        Optional<EmployeeEntity> employee = empRepo.findById(empId);
        log.debug("Trying to assign Department Head ");
        DepartmentDto result =  department.flatMap(
                departmentEntity ->
                        employee.map(
                                employeeEntity -> {
                                    departmentEntity.setDepHead(employeeEntity);
                                    DepartmentEntity entity = repo.save(departmentEntity);
                                    return mapper.map(entity, DepartmentDto.class);
                                }
                        )
        ).orElse(null);
        log.trace("Successfully assigned Department Head  : {} ",result);
        log.info("Returning  Assigned Department Head :)");
        return result;
    }

    public DepartmentDto getDepartmentById(Long depId) {
        log.info("Called Get Department By Id");
        log.debug("Tryping to find department By id ");
        Optional<DepartmentEntity> entity = repo.findById(depId);


        DepartmentDto result =  entity.map(
                departmentEntity -> mapper.map(departmentEntity, DepartmentDto.class)
        ).orElse(null);
        log.trace("Department Found : {}" , result);
        log.info("Returning Deparment info");
        return result;
    }
}
