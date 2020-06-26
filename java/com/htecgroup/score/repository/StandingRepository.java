package com.htecgroup.score.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.htecgroup.score.model.Standing;


public interface StandingRepository extends JpaRepository<Standing, Long>{

	/**
	 * Method for finding {@link Standing} by given team id and matchday (unique constraint)
	 * @param teamId
	 * @param matchday
	 * @return {@link Standing}
	 */
	public Standing findByTeam_IdAndMatchday(Long teamId, Integer matchday);

	/**
	 * Method for finding List of {@link Standing} by given group name
	 * @param groupName
	 * @return List of {@link Standing}
	 */
	@Query("from Standing st where st.leagueGroup.name = :groupName")
	public List<Standing> findByLeagueGroup(String groupName);

	/**
	 * Method for finding List of {@link Standing} by given group name and matchday
	 * descending ordered by points, then by goals, then by goal difference
	 * @param groupName
	 * @param matchday
	 * @return List of {@link Standing}
	 */
	@Query("from Standing st where st.leagueGroup.name = :groupName and st.matchday = :matchday "
			+ "order by st.points desc, st.goals desc, st.goalDifference desc")
	public List<Standing> findByLeagueGroupAndMatchday(String groupName, Integer matchday);

	
	/**
	 * Method for finding last matchday in Standing table by given league group name
	 * @param groupName
	 * @return Integer matchday
	 */
	@Query("select max(st.matchday) from Standing st where st.leagueGroup.name = :groupName")
	public Integer findLastMatchdayByGroup(String groupName);

}
