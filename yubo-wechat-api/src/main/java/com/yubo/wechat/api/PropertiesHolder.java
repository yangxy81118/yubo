package com.yubo.wechat.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:app-core.properties")
public class PropertiesHolder {
	
	@Value("${yubo.url}")
	private String yuboUrl;
	
	@Value("${pet.born.step.point}")
	private Double petBornStepPoint;
	
	@Value("${functiontext.random.chance}")
	private Double functionTextRandomChance;

	public String getYuboUrl() {
		return yuboUrl;
	}

	public Double getPetBornStepPoint() {
		return petBornStepPoint;
	}

	public Double getFunctionTextRandomChance() {
		return functionTextRandomChance;
	}
	
}
