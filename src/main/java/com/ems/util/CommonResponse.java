package com.ems.util;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {

	private boolean success;
	private String message;
	private T data;
	private LocalDateTime timestamp;

	public static <T> CommonResponse<T> success(String message, T data) {
		return new CommonResponse<>(true, message, data, LocalDateTime.now());
	}

	public static <T> CommonResponse<T> failure(String message) {
		return new CommonResponse<>(false, message, null, LocalDateTime.now());
	}
	
}
