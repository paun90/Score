package com.htecgroup.score.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestResponseDto {

	private String message;
	private Object data;
	private StatusDto status;
	
	public RestResponseDto(String message, Object data) {
		super();
		this.message = message;
		this.data = data;
	}
	
	
	
	public RestResponseDto(Object data) {
		this.data = data;
	}
	
	public RestResponseDto(String message) {
		this.message = message;
	}

	public RestResponseDto(Object data, StatusDto status) {
		this.data = data;
		this.status = status;
	}


	public RestResponseDto(String message, Object data, StatusDto status) {
		this.message = message;
		this.data = data;
		this.status = status;
	}
}
