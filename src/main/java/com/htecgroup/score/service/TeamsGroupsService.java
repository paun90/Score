package com.htecgroup.score.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.htecgroup.score.controller.dto.StatusDto;
import com.htecgroup.score.controller.dto.ValidationErrorDto;
import com.htecgroup.score.exception.BusinessValidationException;
import com.htecgroup.score.model.LeagueGroup;
import com.htecgroup.score.model.Team;
import com.htecgroup.score.repository.LeagueGroupRepository;
import com.htecgroup.score.repository.TeamRepository;


@Service
public class TeamsGroupsService {

	@Autowired
	private TeamRepository teamRepository;
	@Autowired
	private LeagueGroupRepository leagueGroupRepository;
	
	/**
	 * Used for finding {@link Team} by given name or to insert new one in DB
	 * @param teamName
	 * @param groupName
	 * @return {@link Team}
	 */
	public Team findTeamByName(String teamName, String groupName) {
		/*If teams are not predefined in DB
		Optional<Team> teamOpt = teamRepository.findByName(teamName);
		if(teamOpt.isPresent()) {
			return teamOpt.get();
		}else {
			Team team = new Team(teamName, this.findGroupByName(groupName));
			return teamRepository.save(team);
		}
		*/
		
		// If teams are predefined in DB
		Team team = teamRepository.findByName(teamName)
				.orElseThrow(() -> new BusinessValidationException(Arrays.asList(new ValidationErrorDto("Team " + teamName + " does not exist", StatusDto.STATUS_BUSSINESS_VALIDATION_TEAMS))));
		
		return team;
		
	}
	
	/**
	 * Used for finding {@link LeagueGroup} by given name
	 * @param groupName
	 * @return {@link LeagueGroup} or throw {@link BusinessValidationException} if not exist
	 */
	public LeagueGroup findGroupByName(String groupName) {
		LeagueGroup group = leagueGroupRepository.findByName(groupName)
				.orElseThrow(() -> new BusinessValidationException(Arrays.asList(new ValidationErrorDto("group " + groupName + " does not exist", StatusDto.STATUS_BUSSINESS_VALIDATION_GROUPS))));;
		return group;
	}
}
