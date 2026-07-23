package com.ems.controller;

import com.ems.dto.EmployeeRequest;
import com.ems.dto.EmployeeResponse;
import com.ems.service.EmployeeService;
import com.ems.util.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

	private final EmployeeService employeeService;

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CommonResponse<EmployeeResponse>> createEmp( @RequestBody EmployeeRequest request) {
		log.info("Incoming request to create employee: {}", request.getEmail());
		
		EmployeeResponse response = employeeService.createEmployee(request);
		
		return ResponseEntity.ok(CommonResponse.success("Employee created successfully", response));
	}

	@GetMapping("/{id}")
	public ResponseEntity<CommonResponse<EmployeeResponse>> getEmpById(@PathVariable Long id) {
		log.info("Incoming request to fetch employee id: {}", id);
		
		EmployeeResponse response = employeeService.getEmployeeById(id);
		
		return ResponseEntity.ok(CommonResponse.success("Employee fetched successfully", response));
	}

	@GetMapping
	public ResponseEntity<CommonResponse<Page<EmployeeResponse>>> getAllEmp(
			@PageableDefault(size = 10, sort = "employeeId") Pageable pageable) {
		log.info("Incoming request to fetch all employees, page: {}", pageable.getPageNumber());
		
		Page<EmployeeResponse> response = employeeService.getAllEmployees(pageable);
		
		return ResponseEntity.ok(CommonResponse.success("Employees fetched successfully", response));
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CommonResponse<EmployeeResponse>> updateEmp(@PathVariable Long id,
			@Valid @RequestBody EmployeeRequest request) {
		log.info("Incoming request to update employee id: {}", id);
		
		EmployeeResponse response = employeeService.updateEmployee(id, request);
		
		return ResponseEntity.ok(CommonResponse.success("Employee updated successfully", response));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CommonResponse<Void>> deleteEmp(@PathVariable Long id) {
		
		log.info("Incoming request to delete employee id: {}", id);
		
		employeeService.deleteEmployee(id);
		
		return ResponseEntity.ok(CommonResponse.success("Employee deleted successfully", null));
	}

	@GetMapping("/search/name")
	public ResponseEntity<CommonResponse<Page<EmployeeResponse>>> searchEmpByName(@RequestParam String name,
			@PageableDefault(size = 10, sort = "employeeId") Pageable pageable) {
		log.info("Incoming request to search employees by name: {}", name);
		
		Page<EmployeeResponse> response = employeeService.searchByName(name, pageable);
		
		return ResponseEntity.ok(CommonResponse.success("Search results fetched", response));
	}

	@GetMapping("/search/department")
	public ResponseEntity<CommonResponse<Page<EmployeeResponse>>> searchEmpByDepartment(
			@RequestParam String departmentName, @PageableDefault(size = 10, sort = "employeeId") Pageable pageable) {
		log.info("Incoming request to search employees by department: {}", departmentName);
		
		Page<EmployeeResponse> response = employeeService.searchByDepartment(departmentName, pageable);
		
		return ResponseEntity.ok(CommonResponse.success("Search results fetched", response));
	}
}