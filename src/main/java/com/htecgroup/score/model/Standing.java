package com.htecgroup.score.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "STANDING", uniqueConstraints = @UniqueConstraint(columnNames = { "TEAM", "LEAGUE_GROUP", "MATCHDAY"}))
@Getter
@Setter
public class Standing {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "TEAM", nullable = false)
	private Team team;
	@ManyToOne
	@JoinColumn(name = "LEAGUE_GROUP", nullable = false)
	private LeagueGroup leagueGroup;
	
	private Integer rank;
	private Integer playedGames;
	private Integer matchday;
	private Integer points;
	private Integer goals;
	private Integer goalsAgainst;
	private Integer goalDifference;
	private Integer win;
	private Integer lose;
	private Integer draw;

}
