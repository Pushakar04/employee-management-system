package com.ems.service;

import com.ems.dto.EmployeeRequest;
import com.ems.dto.EmployeeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
	
	EmployeeResponse createEmployee(EmployeeRequest request);

	EmployeeResponse getEmployeeById(Long id);

	Page<EmployeeResponse> getAllEmployees(Pageable pageable);

	EmployeeResponse updateEmployee(Long id, EmployeeRequest request);

	void deleteEmployee(Long id); 

	Page<EmployeeResponse> searchByName(String name, Pageable pageable);

	Page<EmployeeResponse> searchByDepartment(String departmentName, Pageable pageable);
}