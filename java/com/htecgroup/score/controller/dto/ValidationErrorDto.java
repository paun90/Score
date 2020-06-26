package com.htecgroup.score.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorDto {
	
	Object error;
	StatusDto status;
	
	
	
}
