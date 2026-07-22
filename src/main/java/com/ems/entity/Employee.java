package com.ems.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "employee_id")
	private Long employeeId;

	@Column(name = "employee_name", nullable = false)
	private String employeeName;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "mobile_number", nullable = false, length = 10)
	private String mobileNumber;

	@Column(name = "designation")
	private String designation;

	@Column(name = "salary", nullable = false)
	private Double salary;

	@Column(name = "date_of_joining", nullable = false)
	private LocalDate dateOfJoining;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private EmployeeStatus status;

	// Many Employees -> One Department
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id", nullable = false)
	private Department department;
}