package com.bence.mate.spring.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("database")
public class AppConfiguartionProd extends Databaseconfiguration {
}