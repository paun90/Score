package com.htecgroup.score.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.htecgroup.score.model.Team;


public interface TeamRepository extends JpaRepository<Team, Long>{

	/**
	 * Method for finding {@link Team} by given name
	 * @param name
	 * @return {@link Team}
	 */
	public Optional<Team> findByName(String name);

}
