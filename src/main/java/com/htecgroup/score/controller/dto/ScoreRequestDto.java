package com.htecgroup.score.controller.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ScoreRequestDto {

	@NotBlank
	private String homeTeam;
	@NotBlank
	private String awayTeam;
	@NotBlank
	private String group;
	@NotNull
	@Positive
	private Integer matchday;
	@Pattern(regexp = "^[0-9]*:[0-9]*", message = "must match 'homeTeam goals : awayTeam goals' (e.g. 3:2)")
	private String score;
	
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
	@NotNull
	private Date kickoffAt;

	
	
}
