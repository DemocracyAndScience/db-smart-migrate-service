package com.system.dao;


import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import com.system.utils.annotations.TargetMapperScan;

@TargetMapperScan
public interface DBTargetTableDao {
	@Delete("DELETE FROM schema_version WHERE success = 0 AND version = #{version}")
	void deleteMisFailVersion(@Param("version") String version );
	
	
}
