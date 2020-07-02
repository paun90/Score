package com.htecgroup.score.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StandingResponseDto {
	
	private Long id;
	private String teamName;
	private Integer rank;
	private Integer playedGames;
	private Integer points;
	private Integer goals;
	private Integer goalsAgainst;
	private Integer goalDifference;
	private Integer win;
	private Integer lose;
	private Integer draw;

}
