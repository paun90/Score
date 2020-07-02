package com.htecgroup.score.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class Team {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "NAME", nullable = false)
	private String name;
	@ManyToOne
	@JoinColumn(name = "LEAGUE_GROUP", nullable = false)
	private LeagueGroup leagueGroup;
	
	public Team() {
	
	}
	
	public Team(String name, LeagueGroup leagueGroup) {
		super();
		this.name = name;
		this.leagueGroup = leagueGroup;
	}
	
	

}
