package com.ems.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeRequest {

	@NotBlank(message = "Employee name must not be blank")
	private String employeeName;

	@NotBlank(message = "Email must not be blank")
	@Email(message = "Please provide a valid email address")
	private String email;

	@NotBlank(message = "Mobile number must not be blank")
	@Pattern(regexp = "\\d{10}", message = "Mobile number must contain exactly 10 digits")
	private String mobileNumber;

	private String designation;

	@NotNull(message = "Salary must not be null")
	@Positive(message = "Salary must be a positive value")
	private Double salary;

	@NotNull(message = "Date of joining must not be null")
	@PastOrPresent(message = "Date of joining cannot be in the future")
	private LocalDate dateOfJoining;

	@NotNull(message = "Department ID must not be null")
	private Long departmentId;
}