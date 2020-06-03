package com.system.utils.componet;

import com.system.config.DataSourceInfos;
import com.system.config.support.MyDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 启动时创建表
 * @author noah
 *
 */
@Slf4j
@Component
public class InitTablesCommandLineRunner implements CommandLineRunner {

	@Autowired
	InitTables initTables ;
	@Autowired
	private DataSourceInfos dataSourceInfos;
	@Autowired
	private FlywayProperties flywayProperties;
	
	@Override
	public void run(String... args) throws Exception {
		initTables.run();
		Map<String, Map<String, Map<String, MyDataSource>>> nameSpaces = dataSourceInfos.getNameSpaces();
		Set<String> namePaths = nameSpaces.keySet();
		List<String> locations = flywayProperties.getLocations();
		for (String location : locations) {
			for (String namePath : namePaths) {
				location = location.substring(location.indexOf(":")+1)+"/"+namePath;
				log.info("flyway 读取路径: " + location);
				File file = new File(location);
				if (!file.exists()){
					boolean mkdirs = file.mkdirs();
					if(mkdirs)
					log.info("创建 flyway 读取路径成功  : " + location);
				}
			}

		}


	}

}
