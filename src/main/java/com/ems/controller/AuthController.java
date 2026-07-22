package com.ems.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ems.dto.AuthResponse;
import com.ems.dto.LoginRequest;
import com.ems.dto.RegisterRequest;
import com.ems.entity.AppUser;
import com.ems.entity.Role;
import com.ems.repository.UserRepository;
import com.ems.security.JwtUtil;
import com.ems.security.UserDetailsServiceImpl;
import com.ems.util.CommonResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/register")
	public ResponseEntity<CommonResponse<String>> register(@RequestBody RegisterRequest request) {

	    try {
	        log.info("Registration for username: {}", request.getUsername());

	        if (userRepository.existsByUsername(request.getUsername())) {

	            log.warn("Registration failed - username already exists: {}", request.getUsername());

	            return ResponseEntity.badRequest()
	                    .body(CommonResponse.failure("Username already exists"));
	        }

	        AppUser user = AppUser
	        				.builder()
	        				.username(request.getUsername())
	        				.password(passwordEncoder.encode(request.getPassword()))
	        				.role(request.getRole() != null ? request.getRole() : Role.USER)
	        				.build();

	        userRepository.save(user);

	        log.info("User registered successfully: {}", request.getUsername());

	        return ResponseEntity.ok(
	                CommonResponse.success("User registered successfully", null));

	    } catch (Exception e) {

	        log.error("Error while registering user: {}", request.getUsername(), e);

	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(CommonResponse.failure("Registration failed. Please try again later."));
	    }
	}


	@PostMapping("/login")
	public ResponseEntity<CommonResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
	    log.info("Login attempt for username: {}", request.getUsername());

	    try {
	        authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                        request.getUsername(),
	                        request.getPassword()
	                )
	        );

	        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

	        String token = jwtUtil.generateToken(userDetails);
	        String role = userDetails.getAuthorities().iterator().next().getAuthority();

	        log.info("Login successful for username: {}", request.getUsername());

	        return ResponseEntity.ok(
	                CommonResponse.success(
	                        "Login successful",
	                        new AuthResponse(token, request.getUsername(), role)
	                )
	        );

	    } catch (BadCredentialsException e) {
	        log.warn("Invalid login attempt for username: {}", request.getUsername());

	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body(CommonResponse.failure("Invalid username or password"));

	    } catch (Exception e) {
	        log.error("Login failed for username: {}", request.getUsername(), e);

	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(CommonResponse.failure("An unexpected error occurred"));
	    }
	}

}	