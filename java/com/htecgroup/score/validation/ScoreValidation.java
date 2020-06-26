package com.htecgroup.score.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.htecgroup.score.controller.dto.ScoreRequestDto;
import com.htecgroup.score.controller.dto.StatusDto;
import com.htecgroup.score.controller.dto.ValidationErrorDto;
import com.htecgroup.score.exception.BusinessValidationException;
import com.htecgroup.score.model.Score;
import com.htecgroup.score.repository.ScoreRepository;

@Component
public class ScoreValidation {
	
	@Autowired
	private ScoreRepository scoreRepository;


	
	/**
	 * Used to validate edit of existing {@link Score}
	 * @param scoreFromDb
	 * @param scoreRequestDto
	 */
	public void editScoreValidation(Score scoreFromDb, ScoreRequestDto scoreRequestDto) {
		List<ValidationErrorDto> errorList = new ArrayList<ValidationErrorDto>();
		if(scoreFromDb == null) {
			ValidationErrorDto error = new ValidationErrorDto("Result for the matchday " + scoreRequestDto.getMatchday()
														+ " and teams " + scoreRequestDto.getHomeTeam() 
														+ " and " + scoreRequestDto.getAwayTeam()
														+ " is not saved and can not be edited.", StatusDto.STATUS_BUSSINESS_VALIDATION_SCORE_EDIT);
			errorList.add(error);
		}
		if (!errorList.isEmpty()) {
			throw new BusinessValidationException(errorList);
		}
		
	}

	/**
	 * Used to validate {@link ScoreRequestDto}
	 * 
	 * @param scoreRequestDto
	 */
	public void addNewScoreValidation(ScoreRequestDto scoreRequestDto) {
		List<ValidationErrorDto> errorList = new ArrayList<ValidationErrorDto>();
		// Check does result for teams already exist for this phase of competition (group phase, 6 matchdays)
		if(scoreRequestDto.getMatchday() < 7) {
			Score scoreByTeams = scoreRepository.findByHomeTeamAwayTeamGroupPhase(scoreRequestDto.getHomeTeam(),scoreRequestDto.getAwayTeam());
			if(scoreByTeams != null) {
				ValidationErrorDto error = new ValidationErrorDto("Result for teams " + scoreRequestDto.getHomeTeam() 
															+ " and " + scoreRequestDto.getAwayTeam()
															+ " already saved for group phase of competition. "
															+ "Next possible game these teams can play in semi-final.", StatusDto.STATUS_BUSSINESS_VALIDATION_SCORE);
				errorList.add(error);
			}
			
			//If home team already played in matchday
			Score scoreByHomeTeamMatchday = scoreRepository.findByTeamNameAndMatchday(scoreRequestDto.getHomeTeam(), scoreRequestDto.getMatchday());
			if(scoreByHomeTeamMatchday != null) {
				ValidationErrorDto error = new ValidationErrorDto("Result for team " + scoreRequestDto.getHomeTeam() 
																+ " already saved for matchday " + scoreRequestDto.getMatchday(), StatusDto.STATUS_BUSSINESS_VALIDATION_SCORE);
				errorList.add(error);
			}
			//If away team already played in matchday
			Score scoreByAwayTeamMatchday = scoreRepository.findByTeamNameAndMatchday(scoreRequestDto.getAwayTeam(), scoreRequestDto.getMatchday());
			if(scoreByAwayTeamMatchday != null) {
				ValidationErrorDto error = new ValidationErrorDto("Result for team " + scoreRequestDto.getAwayTeam() 
																+ " already saved for matchday " + scoreRequestDto.getMatchday(), StatusDto.STATUS_BUSSINESS_VALIDATION_SCORE);
				errorList.add(error);
			}
		}

		if (!errorList.isEmpty()) {
			throw new BusinessValidationException(errorList);
		}
	}
	
}
