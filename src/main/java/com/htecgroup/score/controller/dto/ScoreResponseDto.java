package com.htecgroup.score.controller.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScoreResponseDto {
	
	private Long id;
	private String homeTeamName;
	private String awayTeamName;
	private String leagueGroupName;
	private Integer matchday;
	private Integer homeGoals;
	private Integer awayGoals;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Date kickOffAt;

}
