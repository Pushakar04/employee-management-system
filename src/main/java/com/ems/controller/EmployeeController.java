package com.ems.controller;

import com.ems.dto.EmployeeRequest;
import com.ems.dto.EmployeeResponse;
import com.ems.service.EmployeeService;
import com.ems.util.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@Slf4j
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CommonResponse<EmployeeResponse>> createEmp(@RequestBody EmployeeRequest request) {

		try {

			EmployeeResponse response = employeeService.createEmployee(request);

			return ResponseEntity.ok(CommonResponse.success("Employee created successfully", response));

		} catch (Exception e) {
			log.error("Error while creating employee", e);
			return ResponseEntity.internalServerError().body(CommonResponse.failure("Error while processing request"));
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<CommonResponse<EmployeeResponse>> getEmpById(@PathVariable Long id) {
		try {

			EmployeeResponse response = employeeService.getEmployeeById(id);

			return ResponseEntity.ok(CommonResponse.success("Employee fetched successfully", response));

		} catch (Exception e) {
			log.error("Error while fetching employee", e);
			return ResponseEntity.internalServerError().body(CommonResponse.failure("Error while processing request"));
		}
	}

	@GetMapping
	public ResponseEntity<CommonResponse<Page<EmployeeResponse>>> getAllEmp(
			@PageableDefault(size = 10, sort = "employeeId") Pageable pageable) {
		try {

			Page<EmployeeResponse> response = employeeService.getAllEmployees(pageable);

			return ResponseEntity.ok(CommonResponse.success("Employees fetched successfully", response));

		} catch (Exception e) {
			log.error("Error while fetching employees", e);
			return ResponseEntity.internalServerError().body(CommonResponse.failure("Error while processing request"));
		}
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CommonResponse<EmployeeResponse>> updateEmp(@PathVariable Long id,
			@RequestBody EmployeeRequest request) {
		try {

			EmployeeResponse response = employeeService.updateEmployee(id, request);

			return ResponseEntity.ok(CommonResponse.success("Employee updated successfully", response));

		} catch (Exception e) {
			log.error("Error while updating employee", e);
			return ResponseEntity.internalServerError().body(CommonResponse.failure("Error while processing request"));
		}
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CommonResponse<Void>> deleteEmp(@PathVariable Long id) {
		try {

			employeeService.deleteEmployee(id);

			return ResponseEntity.ok(CommonResponse.success("Employee deleted successfully", null));

		} catch (Exception e) {
			log.error("Error while deleting employee", e);
			return ResponseEntity.internalServerError().body(CommonResponse.failure("Error while processing request"));
		}
	}

	@GetMapping("/search/name")
	public ResponseEntity<CommonResponse<Page<EmployeeResponse>>> searchEmpByName(@RequestParam String name,
			@PageableDefault(size = 10, sort = "employeeId") Pageable pageable) {
		try {

			Page<EmployeeResponse> response = employeeService.searchByName(name, pageable);

			return ResponseEntity.ok(CommonResponse.success("Search results fetched", response));

		} catch (Exception e) {
			log.error("Error while searching employees by name", e);
			return ResponseEntity.internalServerError().body(CommonResponse.failure("Error while processing request"));
		}
	}

	@GetMapping("/search/department")
	public ResponseEntity<CommonResponse<Page<EmployeeResponse>>> searchEmpByDepartment(
			@RequestParam String departmentName, @PageableDefault(size = 10, sort = "employeeId") Pageable pageable) {
		try {

			Page<EmployeeResponse> response = employeeService.searchByDepartment(departmentName, pageable);

			return ResponseEntity.ok(CommonResponse.success("Search results fetched", response));

		} catch (Exception e) {
			log.error("Error while searching employees by department", e);
			return ResponseEntity.internalServerError().body(CommonResponse.failure("Error while processing request"));
		}
	}
}