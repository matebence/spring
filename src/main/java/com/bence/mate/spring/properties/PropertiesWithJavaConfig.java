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

@Component
@PropertySource("classpath:development.properties")
@PropertySource("classpath:application.properties")
//@PropertySource({"classpath:persistence-${envTarget:mysql}.properties"})
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
