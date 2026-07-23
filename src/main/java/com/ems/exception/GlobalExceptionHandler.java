package com.ems.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.ems.dto.FieldErrorDetail;
import com.ems.util.CommonResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	// 404 — thrown when an Employee/Department isn't found by id
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<CommonResponse<Object>> handleResourceNotFound(ResourceNotFoundException ex,
			WebRequest request) {
		log.warn("Resource not found: {}", ex.getMessage());
		return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	// 409 — thrown when creating a duplicate email/department name
	@ExceptionHandler(DuplicateRecordException.class)
	public ResponseEntity<CommonResponse<Object>> handleDuplicateRecord(DuplicateRecordException ex,
			WebRequest request) {
		log.warn("Duplicate record: {}", ex.getMessage());
		return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
	}

	// 400 — custom validation exception (if you throw it manually anywhere)
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<CommonResponse<Object>> handleValidation(ValidationException ex, WebRequest request) {
		log.warn("Validation error: {}", ex.getMessage());
		return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	// 400 — @Valid on @RequestBody DTOs (EmployeeRequest, RegisterRequest, etc.)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<CommonResponse<Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			WebRequest request) {

		List<FieldErrorDetail> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
				.map(fe -> new FieldErrorDetail(fe.getField(), fe.getDefaultMessage())).collect(Collectors.toList());

		log.warn("Bean validation failed: {}", fieldErrors);

		CommonResponse<Object> response = CommonResponse.failure("Validation failed");
		response.setData(fieldErrors); // overrides null data with the actual field errors
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	// 401 — thrown by Spring Security when login credentials are wrong
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<CommonResponse<Object>> handleBadCredentials(BadCredentialsException ex, WebRequest request) {
		log.warn("Bad credentials attempt");
		return buildResponse(HttpStatus.UNAUTHORIZED, "Invalid username or password");
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<CommonResponse<Object>> handleUnauthorized(UnauthorizedException ex, WebRequest request) {
		log.warn("Unauthorized: {}", ex.getMessage());
		return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
	}

	// 403 — thrown by Spring Security when @PreAuthorize fails (USER hitting
	// ADMIN-only endpoint)
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<CommonResponse<Object>> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
		log.warn("Access denied: {}", ex.getMessage());
		return buildResponse(HttpStatus.FORBIDDEN, "You do not have permission to perform this action");
	}

	// 403 — your own custom forbidden cases, if thrown anywhere manually
	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<CommonResponse<Object>> handleForbidden(ForbiddenException ex, WebRequest request) {
		log.warn("Forbidden: {}", ex.getMessage());
		return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<CommonResponse<Object>> handleGenericException(Exception ex, WebRequest request) {
		log.error("Unhandled exception occurred", ex); // full stack trace, this one's a real bug if it fires
		return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
	}

	private ResponseEntity<CommonResponse<Object>> buildResponse(HttpStatus status, String message) {
		return ResponseEntity.status(status).body(CommonResponse.failure(message));
	}
}