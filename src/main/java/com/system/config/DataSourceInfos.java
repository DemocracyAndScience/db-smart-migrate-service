package com.system.config;

import com.system.config.support.MyDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Map;

@Configuration
@Order(1)
@ConfigurationProperties(prefix="databases_sync")
public class DataSourceInfos {
    private Map<String ,Map<String, Map<String, MyDataSource>>> nameSpaces ;

    public Map<String ,Map<String, Map<String, MyDataSource>>>  getNameSpaces() {
        return nameSpaces;
    }

    public void setNameSpaces(Map<String ,Map<String, Map<String, MyDataSource>>> nameSpaces) {
        this.nameSpaces = nameSpaces;
    }
}
