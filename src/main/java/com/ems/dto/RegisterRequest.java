package com.ems.dto;

import com.ems.entity.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
	
	@NotBlank(message = "Username must not be blank")
	private String username;

	@NotBlank(message = "Password must not be blank")
	private String password;

	private Role role;
}