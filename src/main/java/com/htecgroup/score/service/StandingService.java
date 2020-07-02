package com.htecgroup.score.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.htecgroup.score.controller.dto.GroupStandingResponseDto;
import com.htecgroup.score.controller.dto.StandingResponseDto;
import com.htecgroup.score.model.LeagueGroup;
import com.htecgroup.score.model.Score;
import com.htecgroup.score.model.Standing;
import com.htecgroup.score.repository.LeagueGroupRepository;
import com.htecgroup.score.repository.ScoreRepository;
import com.htecgroup.score.repository.StandingRepository;
import com.htecgroup.score.validation.StandingValidation;

@Service
public class StandingService {
	
	@Autowired
	private StandingRepository standingRepository;
	@Autowired
	private LeagueGroupRepository leagueGroupRepository;
	@Autowired
	private ScoreRepository scoreRepository;
	@Autowired
	private StandingValidation standingValidation;
	
	
	
	/**
	 * Used to add/update {@link Standing} for all groups and matchdays exist in list of {@link Score}
	 * @param scoreList - list of {@link Score}
	 */
	public void addOrUpdateStandings(List<Score> scoreList) {
		//Sort by matchday because result can not be added without result for previous matchday
		scoreList = scoreList.stream().sorted(Comparator.comparing(Score::getMatchday)).collect(Collectors.toList());
		for (Score score : scoreList) {
			
			Standing homeTeamStanding = standingRepository.findByTeam_IdAndMatchday(score.getHomeTeam().getId(), score.getMatchday());
			Standing previousHomeTeamStanding = standingRepository.findByTeam_IdAndMatchday(score.getHomeTeam().getId(), score.getMatchday()-1);
			this.addOrUpdateStanding(homeTeamStanding, previousHomeTeamStanding, true, score);
			
			Standing awayTeamStanding = standingRepository.findByTeam_IdAndMatchday(score.getAwayTeam().getId(), score.getMatchday());
			Standing previousAwayTeamStanding = standingRepository.findByTeam_IdAndMatchday(score.getAwayTeam().getId(), score.getMatchday()-1);
			this.addOrUpdateStanding(awayTeamStanding, previousAwayTeamStanding, false, score);
		}
		List<LeagueGroup> distinctGroups = scoreList.stream().map(e->e.getLeagueGroup()).distinct().collect(Collectors.toList());
		List<Integer> distinctMatchday = scoreList.stream().map(e->e.getMatchday()).distinct().collect(Collectors.toList());
		this.updateRank(distinctGroups, distinctMatchday);
		
	}

	/**
	 * Used to add/update {@link Standing} for given team and score
	 * @param teamStanding
	 * @param previousteamStanding
	 * @param isHomeStanding
	 * @param score
	 */
	private void addOrUpdateStanding(Standing teamStanding, Standing previousteamStanding, boolean isHomeStanding, Score score) {
		if(teamStanding == null) {
			this.addStanding(score, previousteamStanding, isHomeStanding);
		}else {
			this.populateStandingAndSaveOrUpdate(teamStanding, previousteamStanding, isHomeStanding, score);
			//recursively update results for home team for all next matchdays
			List<Score> scoresForNextMatchdays = scoreRepository.findByTeamForNextMatchdays(score.getHomeTeam().getId(), score.getMatchday());
			addOrUpdateStandings(scoresForNextMatchdays);
		}
	}

	/**
	 * Method for adding new {@link Standing}
	 * @param score
	 * @param previousStanding
	 * @param isHomeTeam
	 */
	private void addStanding(Score score, Standing previousStanding, boolean isHomeTeam) {
		standingValidation.addNewStandingValidation(score, previousStanding, isHomeTeam);
		Standing standing = new Standing();
		standing.setLeagueGroup(score.getLeagueGroup());
		standing.setMatchday(score.getMatchday());
		standing.setTeam(isHomeTeam ? score.getHomeTeam() : score.getAwayTeam());
		
		this.populateStandingAndSaveOrUpdate(standing, previousStanding, isHomeTeam, score);
	}


	/**
	 * Used to populate standing with newest data from score and save new {@link Standing} or update existing
	 * @param standing
	 * @param previousStanding
	 * @param isHomeTeam
	 * @param score
	 */
	private void populateStandingAndSaveOrUpdate(Standing standing, Standing previousStanding, boolean isHomeTeam, Score score) {
		//if it is not first matchday then standing for previous matchday must exist
		Integer draw = score.getMatchday() !=1  ? previousStanding.getDraw() : 0;
		Integer lose = score.getMatchday() !=1 ? previousStanding.getLose() : 0;
		Integer win = score.getMatchday() !=1 ? previousStanding.getWin() : 0;
		Integer points = score.getMatchday() !=1 ? previousStanding.getPoints() : 0;
		Integer payedGames = score.getMatchday() !=1 ? previousStanding.getPlayedGames() : 0;
		Integer goals = score.getMatchday() !=1 ? previousStanding.getGoals() : 0;
		Integer goalsAgainst = score.getMatchday() !=1 ? previousStanding.getGoalsAgainst() : 0;
		
		standing.setPlayedGames(payedGames+1);
		standing.setGoals(goals + (isHomeTeam ? score.getHomeGoals() : score.getAwayGoals()));
		standing.setGoalsAgainst(goalsAgainst + (isHomeTeam ? score.getAwayGoals() : score.getHomeGoals()));
		standing.setGoalDifference(standing.getGoals() - standing.getGoalsAgainst());
		if(score.getHomeGoals()>score.getAwayGoals()) {
			standing.setDraw(draw);
			standing.setLose(isHomeTeam ? lose : lose+1);
			standing.setPoints(isHomeTeam ? points+3 : points);
			standing.setWin(isHomeTeam ? win+1 : win);
		}else if(score.getHomeGoals()<score.getAwayGoals()) {
			standing.setDraw(draw);
			standing.setLose(isHomeTeam ? lose+1 : lose);
			standing.setPoints(isHomeTeam ? points : points+3);
			standing.setWin(isHomeTeam ? win : win+1);
		}else {
			standing.setDraw(draw+1);
			standing.setPoints(points+1);
			standing.setWin(win);
			standing.setLose(lose);
		}
		standingRepository.save(standing);
	}
	
	/**
	 * Used for updating rank for all groups from @param distinctGroups and for all matchdays from @param distinctMatchday
	 * @param distinctGroups
	 * @param distinctMatchday
	 */
	private void updateRank(List<LeagueGroup> distinctGroups, List<Integer> distinctMatchday) {
		for (LeagueGroup leagueGroup : distinctGroups) {
			for(Integer matchday : distinctMatchday) {
				this.updateRankForMatchdayAndGroup(leagueGroup, matchday);	
			}
		}
	}

	/**
	 * Used for updating rank for given group and given matchday
	 * @param leagueGroup
	 * @param matchday
	 */
	private void updateRankForMatchdayAndGroup(LeagueGroup leagueGroup, Integer matchday) {
		List<Standing> standings = standingRepository.findByLeagueGroupAndMatchday(leagueGroup.getName(), matchday);			
		standingValidation.updateRankValidation(standings, leagueGroup.getName(), matchday);
		//list is sorted by points, then by goals, then by goalDifference
		for(int i = 0; i < standings.size(); i++) {
			standings.get(i).setRank(i+1);
			standingRepository.save(standings.get(i));
		}
	}

	
	/**
	 * Method for finding standings by given group names and matchday. 
	 * String groupNames contains name of groups with ,(comma) as delimiter (e.g. "A,B,D") 
	 * If String groupNames is empty or null then standings for all groups will be returned,
	 * If Integer matchday is null then standings for last matchday will be returned.
	 * @param groupNames
	 * @param matchday
	 * @return List of {@link GroupStandingResponseDto}
	 */
	public List<GroupStandingResponseDto> findStandings(String groupNames, Integer matchday) {
		
		List<String> groupNameList = new ArrayList<String>();
		if(StringUtils.isEmpty(groupNames)) {
			//find all groups
			groupNameList = leagueGroupRepository.findAllGroupName();
		}else {
			groupNameList = Arrays.asList(groupNames.split(","));
		}
		
		List<GroupStandingResponseDto> listGroupStandings = this.findStandings(groupNameList, matchday);
		
		return listGroupStandings;
	}

	/**
	 * Method for finding standings by given groups names and matchday. 
	 * @param groupNameList
	 * @param matchday
	 * @return
	 */
	private List<GroupStandingResponseDto> findStandings(List<String> groupNameList, Integer matchday) {
		List<GroupStandingResponseDto> groupStandingList = new ArrayList<GroupStandingResponseDto>();
		List<Standing> standings = new ArrayList<Standing>();
		for (String groupName : groupNameList) {
			if(matchday == null) {
				//find last matchday
				Integer lastMatchday = standingRepository.findLastMatchdayByGroup(groupName);
				standings = standingRepository.findByLeagueGroupAndMatchday(groupName, lastMatchday);
			}else {
				standings = standingRepository.findByLeagueGroupAndMatchday(groupName, matchday);
			}
			
			if(standings != null && !standings.isEmpty()) {
				GroupStandingResponseDto groupStanding = this.mapGroupStandingResponse(standings);
				groupStandingList.add(groupStanding);
			}
		}
		
		return groupStandingList;
	}

	/**
	 * Used for map {@link GroupStandingResponseDto} by given {@link Standing}
	 * @param standings
	 * @return {@link GroupStandingResponseDto}
	 */
	private GroupStandingResponseDto mapGroupStandingResponse(List<Standing> standings) {
		GroupStandingResponseDto groupStandingResponse = new GroupStandingResponseDto();
		List<StandingResponseDto> standingResponseList = new ArrayList<StandingResponseDto>();
		for (Standing standing : standings) {
			StandingResponseDto standingResponse = this.mapStandingResponse(standing);
			standingResponseList.add(standingResponse);
			//group and matchday same in whole list of standings
			groupStandingResponse.setGroupName(standing.getLeagueGroup().getName());
			groupStandingResponse.setLeagueTitle(standing.getLeagueGroup().getLeagueTitle());
			groupStandingResponse.setMatchday(standing.getMatchday());
		}
		groupStandingResponse.setStandings(standingResponseList);
		return groupStandingResponse;
	}

	
	/**
	 * Used for map {@link StandingResponseDto} by given {@link Standing}
	 * @param standing
	 * @return {@link StandingResponseDto}
	 */
	private StandingResponseDto mapStandingResponse(Standing standing) {
		StandingResponseDto standingResponseDto = new StandingResponseDto();
		standingResponseDto.setDraw(standing.getDraw());
		standingResponseDto.setGoalDifference(standing.getGoalDifference());
		standingResponseDto.setGoals(standing.getGoals());
		standingResponseDto.setGoalsAgainst(standing.getGoalsAgainst());
		standingResponseDto.setId(standing.getId());
		standingResponseDto.setLose(standing.getLose());
		standingResponseDto.setPlayedGames(standing.getPlayedGames());
		standingResponseDto.setPoints(standing.getPoints());
		standingResponseDto.setRank(standing.getRank());
		standingResponseDto.setTeamName(standing.getTeam().getName());
		standingResponseDto.setWin(standing.getWin());
		return standingResponseDto;
	}

}
