package com.system.config.support;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MultiTargetDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return DbContextHolder.getDbType();
	}

}