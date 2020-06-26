package com.htecgroup.score.controller.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupStandingResponseDto {
	
	private String leagueTitle;
	private Integer matchday;
	private String groupName;
	private List<StandingResponseDto> standings;
	
	

}
