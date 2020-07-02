package com.htecgroup.score.repository;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.htecgroup.score.model.Score;


public interface ScoreRepository extends JpaRepository<Score, Long>{

	/**
	 * Method for finding {@link Score} by given home team name, away team name and matchday (unique constarint)
	 * @param homeTeam
	 * @param awayTeam
	 * @param matchday
	 * @return {@link Score}
	 */
	@Query("from Score sc where sc.homeTeam.name = :homeTeam and sc.awayTeam.name = :awayTeam and sc.matchday = :matchday")
	public Score findByHomeTeamAwayTeamAndMatchday(String homeTeam, String awayTeam, Integer matchday);

	/**
	 * Method for finding List of {@link Score} by given criteria
	 * null parameters will not be taken into consideration
	 * @param groupName
	 * @param teamName
	 * @param fromDate
	 * @param toDate
	 * @return List of {@link Score}
	 */
	@Query("from Score sc where "
			+ "sc.leagueGroup.name = nvl(:groupName, sc.leagueGroup.name) and "
			+ "sc.kickOffAt between nvl(:fromDate, to_date('01.01.1900', 'dd.mm.yyyy')) and nvl(:toDate, to_date('31.12.2999', 'dd.mm.yyyy')) and "
			+ "(sc.homeTeam.name = nvl(:teamName, sc.homeTeam.name) or sc.awayTeam.name = nvl(:teamName, sc.awayTeam.name))")
	public List<Score> findByGroupTeamAndDate(String groupName, String teamName, ZonedDateTime fromDate, ZonedDateTime toDate);
	
	/**
	 * Method for finding {@link Score} by given home team name and away team name.
	 * There is at most one result in the first stage of the competition (group phase, 6 matchdays)
	 * @param homeTeam
	 * @param awayTeam
	 * @return
	 */
	@Query("from Score sc where sc.homeTeam.name = :homeTeam and sc.awayTeam.name = :awayTeam and sc.matchday < 7")
	public Score findByHomeTeamAwayTeamGroupPhase(String homeTeam, String awayTeam);

	/**
	 * Method for finding {@link Score} by given team id for matchdays bigger than given 
	 * @param teamId
	 * @param matchday
	 * @return
	 */
	@Query("from Score sc where (sc.homeTeam.id = :teamId or sc.awayTeam.id = :teamId) and sc.matchday > :matchday")
	public List<Score> findByTeamForNextMatchdays(Long teamId, Integer matchday);

	/**
	 * Method for finding {@link Score} by given team name and matchday
	 * @param homeTeam
	 * @param matchday
	 * @return
	 */
	@Query("from Score sc where (sc.homeTeam.name = :teamName or sc.awayTeam.name = :teamName) and sc.matchday = :matchday")
	public Score findByTeamNameAndMatchday(String teamName, Integer matchday);

}
