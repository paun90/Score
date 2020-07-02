package com.htecgroup.score.exception;

import java.util.ArrayList;
import java.util.List;

import com.htecgroup.score.controller.dto.ValidationErrorDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class BusinessValidationException extends RuntimeException {

	private static final long serialVersionUID = 2498877061832158032L;
	
	private List<ValidationErrorDto> errors = new ArrayList<>();

}
