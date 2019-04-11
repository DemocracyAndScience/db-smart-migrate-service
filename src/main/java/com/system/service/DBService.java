package com.system.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.system.entity.VersionInfo;

public interface DBService {

	List<VersionInfo> getInfo();

	void upload(MultipartFile file);
	
	void download(String version, HttpServletResponse response) throws IOException ;

	List<String> filenames();

	int migrate();

	void deleteFile(String fileName);
}
