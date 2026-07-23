package com.ems.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ems.dto.EmployeeRequest;
import com.ems.dto.EmployeeResponse;
import com.ems.entity.Department;
import com.ems.entity.Employee;
import com.ems.entity.EmployeeStatus;
import com.ems.exception.DuplicateRecordException;
import com.ems.exception.ResourceNotFoundException;
import com.ems.repository.DepartmentRepository;
import com.ems.repository.EmployeeRepository;
import com.ems.service.EmployeeService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Override
	public EmployeeResponse createEmployee(EmployeeRequest request) {
		log.info("Creating employee : {}", request.getEmployeeName());

		List<Employee> employees = employeeRepository.findAll();

		for (Employee employee : employees) {

			if (employee.getEmail().equalsIgnoreCase(request.getEmail())) {

				log.warn("Duplicate email attempted: {}", request.getEmail());
				throw new DuplicateRecordException("Employee with email " + request.getEmail() + " already exists");

			}
		}

		Department department = departmentRepository.findById(request.getDepartmentId()).orElseThrow(
				() -> new ResourceNotFoundException("Department not found with id: " + request.getDepartmentId()));
		
		Employee emp = Employee.builder()
				.employeeName(request.getEmployeeName())
				.email(request.getEmail())
				.mobileNumber(request.getMobileNumber())
				.department(department)
				.dateOfJoining(request.getDateOfJoining())
				.designation(request.getDesignation())
				.salary(request.getSalary())
				.status(EmployeeStatus.ACTIVE)
				.build();
		
		Employee saved = employeeRepository.save(emp);
		log.info("Employee saved successfully with id: {}", saved.getEmployeeId());
		
		return mapToResponse(saved);
	}
	
	@Override
	public EmployeeResponse getEmployeeById(Long id) {
		log.info("Fetching Employee detail for employee Id : {}", id);
		
		Employee emp = employeeRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Employee not found with id: " + id));
		
		return mapToResponse(emp);
		
	}
	
    @Override
	public Page<EmployeeResponse> getAllEmployees(Pageable pageable) {
    	log.info("Fetching Employee list");
    	
	    return employeeRepository.findAll(pageable).map(this::mapToResponse);
	}
    
    @Override
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
    	log.info("Update emp details request for empId : {}", id);
    	
    	Employee emp = employeeRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Employee not found with id: " + id));
    	
    	Department dept = departmentRepository.findById(request.getDepartmentId())
    			.orElseThrow(()-> new ResourceNotFoundException("Department not found with id: " + id));
    	
    	emp.setEmployeeName(request.getEmployeeName());
    	emp.setEmail(request.getEmail());
    	emp.setMobileNumber(request.getMobileNumber());
    	emp.setDesignation(request.getDesignation());
    	emp.setSalary(request.getSalary());
    	emp.setDateOfJoining(request.getDateOfJoining());
    	emp.setDepartment(dept);
    	
    	Employee updated = employeeRepository.save(emp);
    	log.info("Employee updated successfully with id: {}", updated.getEmployeeId());
    	
    	return mapToResponse(updated);
    }
    
    @Override
    public void deleteEmployee(Long id) {
    	log.info("Employee delete request for id : {}", id);
    	
    	Employee emp = employeeRepository.findById(id)
    			.orElseThrow(()->new ResourceNotFoundException("Employee not found with id: " + id));
    	
    	emp.setStatus(EmployeeStatus.INACTIVE);
    	
    	employeeRepository.save(emp);
    	
    	log.info("Empoyee marked INACTIVE with id : {}", id);
    }
    
    @Override
    public Page<EmployeeResponse> searchByName(String name, Pageable pageable) {
    	log.info("Fetching Employee detail for emp : {} ", name);
    	
        return employeeRepository.searchByName(name, pageable).map(this::mapToResponse);
    }

    @Override
    public Page<EmployeeResponse> searchByDepartment(String departmentName, Pageable pageable) {
    	log.info("Fetching detail for dept name : {}", departmentName);
    	
        return employeeRepository.searchByDepartment(departmentName, pageable).map(this::mapToResponse);
    }


	
	private EmployeeResponse mapToResponse(Employee employee) {
        return EmployeeResponse.builder()
                .employeeId(employee.getEmployeeId())
                .employeeName(employee.getEmployeeName())
                .email(employee.getEmail())
                .mobileNumber(employee.getMobileNumber())
                .designation(employee.getDesignation())
                .salary(employee.getSalary())
                .dateOfJoining(employee.getDateOfJoining())
                .status(employee.getStatus())
                .departmentId(employee.getDepartment().getDepartmentId())
                .departmentName(employee.getDepartment().getDepartmentName())
                .build();
    }

}
