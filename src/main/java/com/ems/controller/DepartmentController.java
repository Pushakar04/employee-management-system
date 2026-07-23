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

import com.ems.entity.Department;
import com.ems.repository.DepartmentRepository;
import com.ems.util.CommonResponse;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

	@Autowired
	private DepartmentRepository departmentRepository;

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CommonResponse<Department>> create(@RequestBody Department department) {

        Department saved = departmentRepository.save(department);
        return ResponseEntity.ok(CommonResponse.success("Department created successfully", saved));
	}

	@GetMapping
	public ResponseEntity<CommonResponse<List<Department>>> getAll() {

		return ResponseEntity
				.ok(CommonResponse.success("Departments fetched successfully", departmentRepository.findAll()));
	}

}
