package com.system.utils.componet;

import java.io.File;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.system.utils.constants.SystemConstants;

@Component
public class PathUtil {

	@Value("${flyway.locations}")
	private   String locations ;
	@Autowired
	HttpSession session ; 
	@Autowired
	DBUtils dBUtils ;
	
	public String getPath() {
		String tableSchema = dBUtils.getTableSchema(session);
		return locations.replace(SystemConstants.FILE_STSTEM, "")+"/"+ tableSchema; 
	}
	
	
	
	public File getFile(String name) {
		File pathFile = new File(getPath()+"/"+name);
		return pathFile ;
	}
	
	
}
