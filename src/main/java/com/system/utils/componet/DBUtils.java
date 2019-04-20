package com.system.utils.componet;

import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.system.config.DataSourceInfos;
import com.system.config.support.MyDataSource;
import com.system.utils.UrlUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.config.DatabasesNames;
import com.system.config.support.DbContextHolder;
import com.system.utils.constants.SystemConstants;

@Component
public class DBUtils {

    @Autowired
    private DatabasesNames databasesNames;

    @Autowired
    private DataSourceInfos databaseConfigs;

    /**
     * 获取名称
     *
     * @return
     * @throws SQLException
     */
    public String getTableSchema(HttpSession session) {
        String dbType = DbContextHolder.getDbType();
        if (dbType == null) {
            dbType = (String) session.getAttribute(SystemConstants.SESSION_DB_NAME);
        }
        if (StringUtils.isBlank(dbType)) {
            dbType = databasesNames.getNames().get(0);
        }
        return dbType;
    }


    /**
     * SourceTableSchema 名称
     *
     * @return
     * @throws SQLException
     */
    public String getSourceTableSchema(HttpSession session) {
        String tableSchema = getTableSchema(session);

        Map<String, Map<String, Map<String, MyDataSource>>> nameSpaces = databaseConfigs.getNameSpaces();
        String url = nameSpaces.get(tableSchema).get(SystemConstants.SOURCE).get(SystemConstants.DATASOURCE).getUrl();
        String schema = UrlUtils.getSchema(url);
        return schema ;
    }


    /**
     * 获取名称
     *
     * @return
     * @throws SQLException
     */
    public String getTargetTableSchema(HttpSession session) {
        String tableSchema = getTableSchema(session);
        Map<String, Map<String, Map<String, MyDataSource>>> nameSpaces = databaseConfigs.getNameSpaces();
        String url = nameSpaces.get(tableSchema).get(SystemConstants.TARGET).get(SystemConstants.DATASOURCE).getUrl();
        String schema = UrlUtils.getSchema(url);
        return schema ;
    }
}
