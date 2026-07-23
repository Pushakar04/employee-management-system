package com.ems.dto;

import com.ems.entity.EmployeeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponse {
	
	private Long employeeId;
	
	private String employeeName;
	
	private String email;
	
	private String mobileNumber;
	
	private String designation;
	
	private Double salary;
	
	private LocalDate dateOfJoining;
	
	private EmployeeStatus status;
	
	private Long departmentId;
	
	private String departmentName;
	
}