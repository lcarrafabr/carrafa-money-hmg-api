package com.example.algamoney.api.config;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

@Configuration
public class LocaleConfig {
	
	@PostConstruct
	public void init() {
		
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

}
