package com.bence.mate.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import com.hazelcast.config.IntegrityCheckerConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;

@Configuration
public class BudgetCachingConfig {

	@Bean
	public Config cacheConfig() {
		Config config = new Config().setIntegrityCheckerConfig(new IntegrityCheckerConfig().setEnabled(true));
		config.getJetConfig().setEnabled(true);

		return config.setInstanceName("hazel-instance")
				.addMapConfig(new MapConfig().setName("account-cache").setTimeToLiveSeconds(3000))
				.addMapConfig(new MapConfig().setName("user-cache").setTimeToLiveSeconds(5000));
	}
}
