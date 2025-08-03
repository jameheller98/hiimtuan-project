package com.hiimtuan.mail_service.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(Exception.class)
	public ProblemDetail handleSecurityException(Exception exception) {
		exception.printStackTrace();

		ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), exception.getMessage());

        errorDetail.setProperty("message", "Unknown interval server error");

        return errorDetail;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleException(MethodArgumentNotValidException exception) {
		ProblemDetail errorDetail =  ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());

		Map<String, String> errors = new HashMap<>();

		exception.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});

		errorDetail.setProperty("message", "Invalid argument error.");
		errorDetail.setProperty("errors", errors);

		return errorDetail;
	}
}
