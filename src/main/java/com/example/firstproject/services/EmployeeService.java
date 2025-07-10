package com.example.firstproject.services;


import com.example.firstproject.dto.EmployeeDTO;
import com.example.firstproject.entities.EmployeeEntity;
import com.example.firstproject.globalhandler.utils.customexceptions.ResourceNotFound;
import com.example.firstproject.repositories.EmployeeRepository;
import org.modelmapper.ModelMapper;
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

    public EmployeeService(EmployeeRepository repo, ModelMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }


//    Util Function

    public void doesExist(Long empId) {
        boolean exist = repo.existsById(empId);
        if (!exist) {
            throw new ResourceNotFound("Invalid ID passed");
        }

    }


//    Rest Controller Main Function

    public List<EmployeeDTO> getAllEmployeeDetails() {
        List<EmployeeEntity> empDetails = repo.findAll();
        return empDetails.stream().map(
                employeeEntity -> mapper.map(employeeEntity, EmployeeDTO.class)
        ).collect(Collectors.toList());
    }


    public EmployeeDTO findEmpById(Long empId) {
        Optional<EmployeeEntity> userDetail = repo.findById(empId);

        return userDetail.map(
                employeeEntity -> mapper.map(employeeEntity, EmployeeDTO.class)
        ).orElse(null);
    }


    public EmployeeDTO createEmployee(EmployeeDTO empDetails) {

        EmployeeEntity userDetails = mapper.map(empDetails, EmployeeEntity.class);
        EmployeeEntity saveResult = repo.save(userDetails);
        return mapper.map(saveResult, EmployeeDTO.class);
    }

    public EmployeeDTO updateEmployeeDetails(Long empId, EmployeeDTO empDetails) {

        doesExist(empId);


        empDetails.setEmpId(empId);
        EmployeeEntity empNewData = mapper.map(empDetails, EmployeeEntity.class);
        EmployeeEntity empSavedData = repo.save(empNewData);
        return mapper.map(empSavedData, EmployeeDTO.class);
    }

    public EmployeeDTO deleteEmployee(Long empId) {

        doesExist(empId);

        EmployeeEntity deletedEmp = repo.findById(empId).orElse(null);
        EmployeeDTO result = mapper.map(deletedEmp, EmployeeDTO.class);
        repo.deleteById(empId);

        return result;
    }

    public EmployeeDTO patchUpdateData(Long empId, Map<String, Object> userData) {
        doesExist(empId);

        Optional<EmployeeEntity> empEntity = repo.findById(empId);

        return empEntity.map(
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
    }
}
