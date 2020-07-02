package com.htecgroup.score.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.htecgroup.score.model.LeagueGroup;


public interface LeagueGroupRepository extends JpaRepository<LeagueGroup, Long>{

	/**
	 * Method for finding {@link LeagueGroup} by given name
	 * @param name
	 * @return
	 */
	public Optional<LeagueGroup> findByName(String name);

	/**
	 * Method for finding all league group names
	 * @return List of league group names
	 */
	@Query("select gr.name from LeagueGroup gr")
	public List<String> findAllGroupName();

}
