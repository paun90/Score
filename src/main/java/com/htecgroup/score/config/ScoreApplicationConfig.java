package com.htecgroup.score.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class ScoreApplicationConfig {
	
	@Bean
	public Module dateTimeModule(){
	    return new JavaTimeModule();
	}

}
