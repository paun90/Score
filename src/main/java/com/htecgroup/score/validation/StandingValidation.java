package com.htecgroup.score.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.htecgroup.score.controller.dto.StatusDto;
import com.htecgroup.score.controller.dto.ValidationErrorDto;
import com.htecgroup.score.exception.BusinessValidationException;
import com.htecgroup.score.model.Score;
import com.htecgroup.score.model.Standing;

@Component
public class StandingValidation {
	
	
	/**
	 * Used to validate adding new {@link Standing}
	 * @param score
	 * @param previousStanding
	 * @param isHomeTeam
	 */
	public void addNewStandingValidation(Score score, Standing previousStanding, boolean isHomeTeam) {
		
		List<ValidationErrorDto> errorList = new ArrayList<ValidationErrorDto>();
		// If it is not first matchday then standing for previous matchday must exist
		if(score.getMatchday() != 1 && previousStanding == null) {
			ValidationErrorDto error = new ValidationErrorDto("No results for previous matchday for team " + 
														(isHomeTeam ? score.getHomeTeam().getName() : score.getAwayTeam().getName()), 
														StatusDto.STATUS_BUSSINESS_VALIDATION_STANDING);
			errorList.add(error);
		}

		if (!errorList.isEmpty()) {
			throw new BusinessValidationException(errorList);
		}
	}
	
	
	/**
	 * Validation for rank updating
	 * @param standings
	 * @param leagueGroupName
	 * @param matchday
	 */
	public void updateRankValidation(List<Standing> standings, String leagueGroupName, Integer matchday) {
		List<ValidationErrorDto> errorList = new ArrayList<ValidationErrorDto>();
		// standings for group for matchday must exist before rank updating 
		if(standings == null) {
			ValidationErrorDto error = new ValidationErrorDto("No standings for group " + 
														leagueGroupName + " for matchday " + matchday, 
														StatusDto.STATUS_BUSSINESS_VALIDATION_STANDING);
			errorList.add(error);
		}

		if (!errorList.isEmpty()) {
			throw new BusinessValidationException(errorList);
		}
	}

}
