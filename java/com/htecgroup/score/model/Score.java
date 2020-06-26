package com.htecgroup.score.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SCORE", uniqueConstraints = { @UniqueConstraint(columnNames = { "HOME_TEAM", "MATCHDAY" }),
											@UniqueConstraint(columnNames = { "AWAY_TEAM", "MATCHDAY" }) })
@Getter
@Setter
@NoArgsConstructor
public class Score {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "HOME_TEAM", nullable = false)
	private Team homeTeam;
	@ManyToOne
	@JoinColumn(name = "AWAY_TEAM", nullable = false)
	private Team awayTeam;
	@ManyToOne
	@JoinColumn(name = "LEAGUE_GROUP", nullable = false)
	private LeagueGroup leagueGroup;
	private Integer matchday;
	private Integer homeGoals;
	private Integer awayGoals;
	private Date kickOffAt;
	
	
	public Score(Team homeTeam, Team awayTeam, LeagueGroup leagueGroup, Integer matchday, Integer homeGoals,
			Integer awayGoals, Date kickOffAt) {
		super();
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.leagueGroup = leagueGroup;
		this.matchday = matchday;
		this.homeGoals = homeGoals;
		this.awayGoals = awayGoals;
		this.kickOffAt = kickOffAt;
	}
	
}
