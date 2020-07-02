package com.htecgroup.score.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StatusDto {

	public static final StatusDto STATUS_SUCCESS = new StatusDto("scr_200", "success");

	public static final StatusDto STATUS_BUSSINESS_VALIDATION = new StatusDto("scr_400", "bad request");

	public static final StatusDto STATUS_FORMAL_VALIDATION = new StatusDto("scr_422", "bad arguments");
	
	public static final StatusDto STATUS_FORMAL_VALIDATION_ERROR_ARGUMENT = new StatusDto("scr_4001", "argument not valid");

	public static final StatusDto STATUS_BUSSINESS_VALIDATION_GROUPS = new StatusDto("scr_4002", "group does not exist");

	public static final StatusDto STATUS_BUSSINESS_VALIDATION_SCORE = new StatusDto("scr_4003", "score already saved");

	public static final StatusDto STATUS_BUSSINESS_VALIDATION_STANDING = new StatusDto("scr_4004", "no previous standing");

	public static final StatusDto STATUS_BUSSINESS_VALIDATION_SCORE_EDIT = new StatusDto("scr_4005", "score does not exist");

	public static final StatusDto STATUS_BUSSINESS_VALIDATION_TEAMS = new StatusDto("scr_4006", "team does not exist");
	
	private String code;
	private String descritpion;
	
}
