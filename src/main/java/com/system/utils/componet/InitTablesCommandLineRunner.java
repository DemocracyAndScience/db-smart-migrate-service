package com.system.utils.componet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 启动时创建表
 * @author noah
 *
 */
@Component
public class InitTablesCommandLineRunner implements CommandLineRunner {

	@Autowired
	InitTables initTables ; 
	
	@Override
	public void run(String... args) throws Exception {
		initTables.run();
	}

}
