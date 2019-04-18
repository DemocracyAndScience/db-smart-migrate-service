package com.system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(2)
//@ConfigurationProperties("databases")
public class DatabasesNames {


    @Autowired
    private DataSourceInfos databaseConfigs;


    //  private List<String> names ;
    public List<String> getNames() {
        //  return names ;
        return new ArrayList<String>(databaseConfigs.getNameSpaces().keySet());
    }
    /*public void  setNames(List<String> names  ){
        this.names = names;
    }
*/


}
