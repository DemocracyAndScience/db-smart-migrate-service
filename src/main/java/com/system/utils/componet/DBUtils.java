package com.system.utils.componet;

import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.config.DatabasesNames;
import com.system.config.support.DbContextHolder;
import com.system.utils.constants.SystemConstants;

@Component
public class DBUtils {

	@Autowired
	private DatabasesNames databasesNames ;
	/**
	 * 获取名称
	 * @return
	 * @throws SQLException
	 */
	public  String getTableSchema(HttpSession session)  {
		String dbType = DbContextHolder.getDbType();
		if(dbType == null ) {
			dbType = (String)session.getAttribute(SystemConstants.SESSION_DB_NAME);
		}
		if(StringUtils.isBlank(dbType)) {
			dbType = databasesNames.getNames().get(0);
		}
		return dbType; 
	} 
}
