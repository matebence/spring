package com.bence.mate.spring.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.annotation.PropertySource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/*
Before we go into more advanced configuration options for properties, let's spend some time looking at the new properties support in Spring Boot.
Generally speaking, this new support involves less configuration compared to standard Spring, which is of course one of the main goals of Boot.
application.properties: the Default Property File
*/

@Component
@PropertySource("classpath:development.properties")
@PropertySource("classpath:application.properties")
// @PropertySource("classpath:/application-${spring.profiles.active}.properties")
//@PropertySources({
//    @PropertySource("classpath:foo.properties"),
//    @PropertySource("classpath:bar.properties")
//})
public class PropertiesWithJavaConfig {

	private static Logger logger = LoggerFactory.getLogger(PropertiesWithJavaConfig.class);

	@Autowired
	private Environment env;

	@Autowired
	private ApiConfig apiConfig;

	@Value("${app.property}")
	private String name;

	@PostConstruct
	public void postConstruct() {
		logger.info("{}", name);
		logger.info("{}", apiConfig.toString());
		logger.info("{}", env.getProperty("app.dev.property"));
		logger.info("{}", env.getProperty("app.property"));
	}
}
