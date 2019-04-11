package com.system.config;


import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

	@Value("${flyway.baseline-on-migrate}")
	private boolean baselineOnMigrate ; 
	@Value("${flyway.validate-on-migrate}")
	private boolean validateOnMigrate ; 
	
	@Bean
	public Flyway flyway(@Autowired @Qualifier("targetDataSource") DataSource targetDataSource) {
		Flyway flyway = new Flyway();
		flyway.setDataSource(targetDataSource);
		flyway.setBaselineOnMigrate(baselineOnMigrate);
		flyway.setValidateOnMigrate(validateOnMigrate);
		return flyway ;
	}
}
