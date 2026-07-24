package com.ems.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ems.dto.DepartmentResponse;
import com.ems.entity.Department;
import com.ems.repository.DepartmentRepository;
import com.ems.util.CommonResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Employees", description = "APIs for managing department records")
@RestController
@RequestMapping("/api/department")
public class DepartmentController {

	@Autowired
	private DepartmentRepository departmentRepository;

	@Operation(summary = "Create department", description = "Creates a new department, requires ADMIN role.")
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CommonResponse<DepartmentResponse>> create(@RequestBody Department department) {
	    Department saved = departmentRepository.save(department);
	    DepartmentResponse response = new DepartmentResponse(saved.getDepartmentId(), saved.getDepartmentName());
	    return ResponseEntity.ok(CommonResponse.success("Department created successfully", response));
	}

	@Operation(summary = "Get all department")
	@GetMapping
	public ResponseEntity<CommonResponse<List<DepartmentResponse>>> getAll() {
		List<DepartmentResponse> response = departmentRepository.findAll().stream()
				.map(d -> new DepartmentResponse(d.getDepartmentId(), d.getDepartmentName())).toList();
		return ResponseEntity.ok(CommonResponse.success("Departments fetched successfully", response));
	}

}
