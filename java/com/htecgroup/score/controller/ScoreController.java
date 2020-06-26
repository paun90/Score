package com.htecgroup.score.controller;

import java.time.ZonedDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.htecgroup.score.controller.dto.GroupStandingResponseDto;
import com.htecgroup.score.controller.dto.RestResponseDto;
import com.htecgroup.score.controller.dto.ScoreRequestDto;
import com.htecgroup.score.controller.dto.ScoreResponseDto;
import com.htecgroup.score.controller.dto.StatusDto;
import com.htecgroup.score.model.Score;
import com.htecgroup.score.service.ScoreService;
import com.htecgroup.score.service.StandingService;

@Validated
@RestController
public class ScoreController {

	@Autowired
	private ScoreService scoreService;
	@Autowired
	private StandingService standingService;

	/**
	 * Method for adding new Scores in DB and adding/updating standings for all
	 * groups and matchdays exist in list of {@link ScoreRequestDto}
	 * @param listScores
	 * @return
	 */
	@PostMapping("/results")
	public ResponseEntity<RestResponseDto> addResults(@Valid @RequestBody List<ScoreRequestDto> listScores) {
		scoreService.addScores(listScores);
		List<GroupStandingResponseDto> groupStandingsList = standingService.findStandings(null, null);
		return new ResponseEntity<>(new RestResponseDto("Standings for last matchday for group", groupStandingsList, StatusDto.STATUS_SUCCESS), HttpStatus.OK);
	}

	/**
	 * Method for editing existing Scores in DB and adding/updating standings 
	 * for all groups and matchdays exist in list of {@link ScoreRequestDto}
	 * @param listScores
	 * @return
	 */
	@PutMapping("/results")
	public ResponseEntity<RestResponseDto> editResults(@Valid @RequestBody List<ScoreRequestDto> listScores) {
		scoreService.editScores(listScores);
		List<GroupStandingResponseDto> groupStandingsList = standingService.findStandings(null, null);
		return new ResponseEntity<>(new RestResponseDto("Standings for last matchday for group", groupStandingsList, StatusDto.STATUS_SUCCESS), HttpStatus.OK);
	}
	
	/**
	 * Method for finding standings by given group names and matchday. 
	 * String groupNames contains name of groups with ,(comma) as delimiter (e.g. "A,B,D") 
	 * If String groupNames is empty or null then standings for all groups will be returned
	 * If Integer matchday is null then standings for last matchday will be returned.
	 * @param matchday
	 * @param groupNames
	 * @return
	 */
	@GetMapping("/standings")
	public ResponseEntity<RestResponseDto> getStandings(@RequestParam(required = false) Integer matchday, @RequestParam(required = false) String groupNames) {
		List<GroupStandingResponseDto> groupStandingsList = standingService.findStandings(groupNames, matchday);
		return new ResponseEntity<>(new RestResponseDto(groupStandingsList, StatusDto.STATUS_SUCCESS), HttpStatus.OK);
	}
	
	/**
	 * Used for finding {@link Score} by given criteria. 
	 * If some parameter is null it will not be taken into consideration
	 * @param fromDate
	 * @param toDate
	 * @param groupName
	 * @param teamName
	 * @return
	 */
	@GetMapping("/scores")
	public ResponseEntity<RestResponseDto> getScores(@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE_TIME) ZonedDateTime fromDate,
													@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE_TIME) ZonedDateTime toDate,
													@RequestParam(required = false) String groupName,
													@RequestParam(required = false) String teamName) {
		
		List<ScoreResponseDto> listScoreResponse = scoreService.findScoresByCriteria(fromDate, toDate, groupName, teamName);
		return new ResponseEntity<>(new RestResponseDto(listScoreResponse, StatusDto.STATUS_SUCCESS), HttpStatus.OK);
	}
	
}
