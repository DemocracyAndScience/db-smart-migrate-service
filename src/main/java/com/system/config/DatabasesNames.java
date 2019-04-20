package com.system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(2)
public class DatabasesNames {


    @Autowired
    private DataSourceInfos databaseConfigs;


    public List<String> getNames() {
        return new ArrayList<String>(databaseConfigs.getNameSpaces().keySet());
    }


}
