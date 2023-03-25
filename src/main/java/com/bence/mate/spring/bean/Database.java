package com.bence.mate.spring.bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Bean;

import com.bence.mate.spring.properties.Databaseconfiguration;
import com.bence.mate.spring.properties.AppConfiguartionProd;
import com.bence.mate.spring.properties.AppConfiguartionDev;

@Configuration
public class Database {

    @Profile("prod")
    @Bean
    public Databaseconfiguration getProdDatabaseConfiguration() {
        return new AppConfiguartionProd();
    }
    @Profile("dev")
    @Bean
    public Databaseconfiguration getDevDatabaseConfiguration() {
        return new AppConfiguartionDev();
    }
}
