package com.htecgroup.score.service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.htecgroup.score.controller.dto.ScoreRequestDto;
import com.htecgroup.score.controller.dto.ScoreResponseDto;
import com.htecgroup.score.model.LeagueGroup;
import com.htecgroup.score.model.Score;
import com.htecgroup.score.model.Team;
import com.htecgroup.score.repository.ScoreRepository;
import com.htecgroup.score.validation.ScoreValidation;

@Service
public class ScoreService {

	@Autowired
	private ScoreRepository scoreRepository;
	@Autowired
	private TeamsGroupsService teamsGroupsService;
	@Autowired
	private StandingService standingService;
	@Autowired
	private ScoreValidation scoreValidation;

	/**
	 * Method for adding new Scores in DB and adding/updating standings 
	 * for all groups and matchdays exist in list of {@link ScoreRequestDto}
	 * @param listScoreRequests
	 */
	@Transactional
	public void addScores(List<ScoreRequestDto> listScoreRequests) {
		List<Score> scoreList = new ArrayList<Score>();
		for (ScoreRequestDto scoreRequestDto : listScoreRequests) {
			scoreValidation.addNewScoreValidation(scoreRequestDto);
			Score score = this.mapScoreRequest(scoreRequestDto);
			score = scoreRepository.save(score);
			scoreList.add(score);
		}
		standingService.addOrUpdateStandings(scoreList);
	}
	
	/**
	 * Method for editing existing Scores in DB and adding/updating standings 
	 * for all groups and matchdays exist in list of {@link ScoreRequestDto}
	 * @param listScoreRequests
	 */
	@Transactional
	public void editScores(List<ScoreRequestDto> listScoreRequests) {
		List<Score> scoreList = new ArrayList<Score>();
		for (ScoreRequestDto scoreRequestDto : listScoreRequests) {
			Score scoreFromDb = scoreRepository.findByHomeTeamAwayTeamAndMatchday(scoreRequestDto.getHomeTeam(), scoreRequestDto.getAwayTeam(), scoreRequestDto.getMatchday());
			scoreValidation.editScoreValidation(scoreFromDb, scoreRequestDto);
			String[] goals = scoreRequestDto.getScore().split(":");
			scoreFromDb.setHomeGoals(Integer.parseInt(goals[0]));
			scoreFromDb.setAwayGoals(Integer.parseInt(goals[1]));
			scoreFromDb.setKickOffAt(scoreRequestDto.getKickoffAt());
			scoreList.add(scoreFromDb);
		}
		standingService.addOrUpdateStandings(scoreList);
	}

	/**
	 * Used for finding {@link Score} by given criteria. 
	 * If some parameter is null it will not be taken into consideration
	 * @param fromDate
	 * @param toDate
	 * @param groupName
	 * @param teamName
	 * @return List of {@link ScoreResponseDto}
	 */
	public List<ScoreResponseDto> findScoresByCriteria(ZonedDateTime fromDate, ZonedDateTime toDate, String groupName, String teamName) {
		List<Score> listScores = scoreRepository.findByGroupTeamAndDate(groupName, teamName, fromDate, toDate);
		List<ScoreResponseDto> listScoreResponse = new ArrayList<ScoreResponseDto>();
		for (Score score : listScores) {
			ScoreResponseDto scoreResponseDto = this.mapScoreResponse(score);
			listScoreResponse.add(scoreResponseDto);
		}
		return listScoreResponse;
	}
	
	/**
	 * Used for map {@link Score} with given {@link ScoreRequestDto}
	 * 
	 * @param scoreRequestDto
	 * @return {@link Score}
	 */
	private Score mapScoreRequest(ScoreRequestDto scoreRequestDto) {
		Team homeTeam = teamsGroupsService.findTeamByName(scoreRequestDto.getHomeTeam(), scoreRequestDto.getGroup());
		Team awayTeam = teamsGroupsService.findTeamByName(scoreRequestDto.getAwayTeam(), scoreRequestDto.getGroup());
		LeagueGroup group = teamsGroupsService.findGroupByName(scoreRequestDto.getGroup());
		String[] goals = scoreRequestDto.getScore().split(":");
		Score score = new Score(homeTeam, awayTeam, group, scoreRequestDto.getMatchday(), Integer.parseInt(goals[0]),
				Integer.parseInt(goals[1]), scoreRequestDto.getKickoffAt());
		return score;
	}

	/**
	 * Used for map {@link ScoreResponseDto} by given {@link Score}
	 * @param score
	 * @return {@link ScoreResponseDto}
	 */
	private ScoreResponseDto mapScoreResponse(Score score) {
		ScoreResponseDto scoreResponseDto = new ScoreResponseDto();
		scoreResponseDto.setAwayGoals(score.getAwayGoals());
		scoreResponseDto.setAwayTeamName(score.getAwayTeam().getName());
		scoreResponseDto.setHomeGoals(score.getHomeGoals());
		scoreResponseDto.setHomeTeamName(score.getHomeTeam().getName());
		scoreResponseDto.setId(score.getId());
		scoreResponseDto.setKickOffAt(score.getKickOffAt());
		scoreResponseDto.setLeagueGroupName(score.getLeagueGroup().getName());
		scoreResponseDto.setMatchday(score.getMatchday());
		return scoreResponseDto;
	}

	
	
}
