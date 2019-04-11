package com.system.config.support;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MultiSourceDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return DbContextHolder.getDbType();
	}


	
}