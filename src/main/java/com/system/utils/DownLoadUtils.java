package com.system.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public class DownLoadUtils {

	/**
	 * 下载文件
	 */
	public static void download(HttpServletResponse response, InputStream inputStream, String name)
			throws UnsupportedEncodingException, IOException {
		if (StringUtils.isNotBlank(name)) {
			String fileType = name.substring(name.lastIndexOf(".") + 1, name.length());
			// application/x-sql
			if ("sql".equals(fileType)) {
				response.setContentType("application/x-sql" +";charset=UTF-8");
			} 
			// 需要其他的文件再加
		}
		response.setHeader("Content-Disposition",
				"attachment;filename=\"" + new String(name.getBytes(), "ISO8859-1") + "\"");
		byte[] buffer = new byte[4096];
		BufferedOutputStream output = null;
		BufferedInputStream input = null;
		try {
			output = new BufferedOutputStream(response.getOutputStream());
			input = new BufferedInputStream(inputStream);
			int n = -1;
			while ((n = input.read(buffer, 0, 4096)) > -1) {
				output.write(buffer, 0, n);
			}
			output.flush();
			response.flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
                input.close();
            }
			if (output != null) {
                output.close();
            }
		}
	}
}
