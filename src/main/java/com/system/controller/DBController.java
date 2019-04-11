package com.system.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.system.entity.VersionInfo;
import com.system.service.DBService;
import com.system.utils.Result;

@RestController
public class DBController {

	@Autowired
	private DBService dBService;

	/**
	 * 获取当前信息
	 * @param environment
	 * @return
	 */
	@RequestMapping("/db/info")
	public Result getInfo() {
		List<VersionInfo> list = dBService.getInfo();
		return Result.success(list);
	}
	/**
	 * 删除文件
	 * @param environment
	 * @return
	 */
	@RequestMapping("/db/deleteFile/{fileName}/")
	public Result deleteFile(@PathVariable("fileName") String fileName) {
		 dBService.deleteFile(fileName);
		return Result.success("");
	}
	/**
	 * 上传
	 * @param environment
	 * @return
	 */
	@PostMapping(value = "/db/upload/sql")
	public Result upload(MultipartFile file) {
		dBService.upload(file);
		return Result.success("");
	}

	/**
	 * 下载指定版本的SQl 文件
	 * @param environment
	 * @return
	 */
	@RequestMapping("/db/download/unauth/sql/{version}")
	public Result download(@PathVariable("version") String version, HttpServletResponse response) {
		try {
			dBService.download(version, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Result.success("");
	}

	/**
	 * 获取所有版本的文件
	 * @param environment
	 * @return
	 */
	@RequestMapping("/db/filenames/sql")
	public Result filenames() {
		List<String> filenames = dBService.filenames();
		return Result.success(filenames);
	}

	/**
	 * 迁移
	 * @param environment
	 * @return
	 */
	@RequestMapping("/db/migrate")
	public Result migrate() {
		int a = dBService.migrate();
		return Result.success(a);
	}

}
