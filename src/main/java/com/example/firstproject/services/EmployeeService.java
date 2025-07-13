package com.example.firstproject.services;


import com.example.firstproject.dto.EmployeeDTO;
import com.example.firstproject.entities.EmployeeEntity;
import com.example.firstproject.globalhandler.utils.customexceptions.ResourceNotFound;
import com.example.firstproject.repositories.EmployeeRepository;
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
public class EmployeeService {

    private final EmployeeRepository repo;
    private final ModelMapper mapper;

    private final Logger log = LoggerFactory.getLogger(EmployeeService.class);


    public EmployeeService(EmployeeRepository repo, ModelMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }


//    Util Function

    public void doesExist(Long empId) {
        log.debug("Checking Whether Employee exists or not");
        boolean exist = repo.existsById(empId);
        if (!exist) {
            log.debug("The employee doesn't exists");
            throw new ResourceNotFound("Invalid ID passed");
        }
        log.debug("Employee Finded :) ");
    }


//    Rest Controller Main Function

    public List<EmployeeDTO> getAllEmployeeDetails() {
        log.info("Called Get All Employee function");
        log.debug("Getting all employee information");
        List<EmployeeEntity> empDetails = repo.findAll();
        List<EmployeeDTO> list = empDetails.stream().map(
                employeeEntity -> mapper.map(employeeEntity, EmployeeDTO.class)
        ).collect(Collectors.toList());
        log.trace("Successfully gathered All employee details : {}",list);
        log.info("Returning All Employee's details :)");
        return list;
    }


    public EmployeeDTO findEmpById(Long empId) {
        log.info("Called Find Employee By id function");
        log.debug("Finding employee By Id");
        Optional<EmployeeEntity> userDetail = repo.findById(empId);

        EmployeeDTO result = userDetail.map(
                employeeEntity -> mapper.map(employeeEntity, EmployeeDTO.class)
        ).orElse(null);

        log.trace("Successfully Finded employee By Id : {}",result);
        log.info("Returning The Employee's details :)");

        return result;
    }


    public EmployeeDTO createEmployee(EmployeeDTO empDetails) {
        log.info("Called Create Employee Function");
        log.debug("Trying To create employee ");
        EmployeeEntity userDetails = mapper.map(empDetails, EmployeeEntity.class);
        EmployeeEntity saveResult = repo.save(userDetails);
        log.trace("Successfully Created New Employee : {}",saveResult);
        log.info("Returning new employee Details :)");
        return mapper.map(saveResult, EmployeeDTO.class);
    }

    public EmployeeDTO updateEmployeeDetails(Long empId, EmployeeDTO empDetails) {
        log.info("Called Update Employee function");
        doesExist(empId);
        log.debug("Trying to Update Employee");

        empDetails.setEmpId(empId);
        EmployeeEntity empNewData = mapper.map(empDetails, EmployeeEntity.class);
        EmployeeEntity empSavedData = repo.save(empNewData);
        log.trace("Successfully Updated Employee : {}",empSavedData);
        log.info("Returning Updated Employee :)");
        return mapper.map(empSavedData, EmployeeDTO.class);
    }

    public EmployeeDTO deleteEmployee(Long empId) {
        log.info("Called Delete Employee Function");

        doesExist(empId);
        log.debug("Trying To delete employee ");

        EmployeeEntity deletedEmp = repo.findById(empId).orElse(null);
        EmployeeDTO result = mapper.map(deletedEmp, EmployeeDTO.class);
        repo.deleteById(empId);
        log.trace("Successfully Deleted Employee : {} " ,result);
        log.info("Returning Deleted Employee :)");
        return result;
    }

    public EmployeeDTO patchUpdateData(Long empId, Map<String, Object> userData) {
        log.info("Called Patch Update Employee Function");
        doesExist(empId);
        log.debug("Trying to patch update employee ");
        Optional<EmployeeEntity> empEntity = repo.findById(empId);

        EmployeeDTO result = empEntity.map(
                employeeEntity -> {
                    userData.forEach(
                            (key, value) -> {
                                Field field = ReflectionUtils.findField(EmployeeEntity.class, key);
                                field.setAccessible(true);
                                ReflectionUtils.setField(field, employeeEntity, value);
                            }
                    );
                    return mapper.map(repo.save(employeeEntity), EmployeeDTO.class);
                }
        ).orElse(null);
        log.trace("Successfully Patch Update The Employee : {}",result);
        log.info("Returning The patch updated employee :)");
        return result;
    }
}
