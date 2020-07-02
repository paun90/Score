package com.htecgroup.score.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.htecgroup.score.controller.dto.RestResponseDto;
import com.htecgroup.score.controller.dto.StatusDto;
import com.htecgroup.score.controller.dto.ValidationErrorDto;

/**
 * ExceptionHandler object that contains methods that are handling exceptions caught in controllers
 * @author paunovicm
 *
 */
@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		  List<String> details = e.getBindingResult().getFieldErrors().stream()
	                .map(error -> error.getField() + ": " + error.getDefaultMessage())
	                .collect(Collectors.toList());
		ValidationErrorDto error = new ValidationErrorDto(details, StatusDto.STATUS_FORMAL_VALIDATION_ERROR_ARGUMENT);
		RestResponseDto response = new RestResponseDto("Arguments not valid", error, StatusDto.STATUS_FORMAL_VALIDATION);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Used to handle {@link ConstraintViolationException} 
	 * @param exception
	 * @return ResponseEntity<Object> 
	 */
	@ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
		Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
	    List<ValidationErrorDto> listErrors = new ArrayList<ValidationErrorDto>();
	    for (ConstraintViolation<?> violation : violations) {
	    	String fields[] = violation.getPropertyPath().toString().split("\\.");
	    	String position = fields[1].substring(fields[1].indexOf("["), fields[1].indexOf("]")+1);//+1 because endIndex exclusive.
	    	String field = fields[2];
	    	
	      ValidationErrorDto error = new ValidationErrorDto(position + " -> " + field + " : " + violation.getMessage(), StatusDto.STATUS_FORMAL_VALIDATION_ERROR_ARGUMENT);
	      listErrors.add(error);
	    }
	    RestResponseDto response = new RestResponseDto("Arguments not valid", listErrors, StatusDto.STATUS_FORMAL_VALIDATION);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
	
	/**
	 * Used to handle {@link BusinessValidationException}
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(BusinessValidationException.class)
    public final ResponseEntity<Object> handleNotFoundException(BusinessValidationException ex) {
        RestResponseDto response = new RestResponseDto("Validation error occured", ex.getErrors(), StatusDto.STATUS_BUSSINESS_VALIDATION);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
	
	
	
}
