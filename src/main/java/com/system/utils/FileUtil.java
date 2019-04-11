package com.system.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.system.utils.constants.DBConstants;
import com.system.utils.constants.SystemConstants;

/**
 * 文件处理工具类
 * 
 * @author lvjiarui
 * @date 2018/5/8 15:07
 */
public class FileUtil {
	protected final static Logger logger = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * 校验文件名称
	 */
	public static void validateFileName(String name, double cuurentMaxVersion) {
		boolean containsSql = name.contains(".sql");
		if (!containsSql) {
			throw new RuntimeException("文件类型错误！");
		}
		boolean contains = name.contains("__");
		if (!contains) {
			throw new RuntimeException("文件名称错误！");
		}
		boolean startsWith = name.startsWith("V");
		if (!startsWith) {
			throw new RuntimeException("文件格式错误，请用V开头。");
		}
		boolean contains2 = name.contains(SystemConstants.FILE_CREATE_SYNCHRONOUS);
		if(contains2) {
			throw new RuntimeException("文件格式错误，请不要使用"+SystemConstants.FILE_CREATE_SYNCHRONOUS);
		}
		String replace = name.split("__")[0].replace("V", "");
		double parseDouble = Double.parseDouble(replace);
		if (parseDouble <= cuurentMaxVersion) {
			throw new RuntimeException("文件名称版本错误！");
		}

	}

	/**
	 * 校验文件名称
	 * 
	 * @throws IOException
	 */
	public static void validateFileContent(MultipartFile file) {

		InputStream inputStream = null;
		try {
			inputStream = file.getInputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = null;
		String line = "";
		try {
			bufferedReader = new BufferedReader(inputStreamReader);
			while ((line = bufferedReader.readLine()) != null) {// 如果之前文件为空，则不执 行输出
				List<String> allTables = DBConstants.getAllTables();
				for (String tableName : allTables) {
					boolean contains = line.contains(tableName);
					if(contains) {
						throw new RuntimeException("文件内包含 " + tableName + " 字符");
					}
				}
			}
		} catch (IOException e) {
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
			if (inputStreamReader != null) {
				try {
					inputStreamReader.close();
				} catch (IOException e) {
				}
			}
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * 保存文件
	 * 
	 * @param file
	 * @param fileName
	 * @return
	 */
	public static boolean saveFile(InputStream inputStream, String fileName, String path) {
		OutputStream os = null;
		byte[] bs = new byte[1024];
		try {
			File tempFile = new File(path);
			if (!tempFile.exists()) {
				tempFile.mkdirs();
			}
			try {
				os = new FileOutputStream(tempFile.getAbsolutePath() + "/" + fileName);
			} catch (FileNotFoundException e) {
				throw new RuntimeException("该文件没有访问权限，请执行文件目录设置权限命令。或设置环境变量为程序有权限的目录。变量值为：locations_pre_path");
			}
			// 开始读取
			int len;
			while ((len = inputStream.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
		} catch (IOException e) {
			logger.error("文件处理失败", e);
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return true;
	}

	/**
	 * 保存文件
	 * 
	 * @param file
	 * @param fileName
	 * @return
	 */
	public static boolean saveFile(StringBuilder sb, String fileName, String path) {
		FileWriter fileWriter = null;
		try {
			File tempFile = new File(path);
			if (!tempFile.exists()) {
				tempFile.mkdirs();
			}
			try {
				fileWriter = new FileWriter(tempFile.getAbsolutePath() + "/" + fileName, true);
			} catch (FileNotFoundException e) {
				throw new RuntimeException("该文件没有访问权限，请执行文件目录设置权限命令。或设置环境变量为程序有权限的目录。变量值为：locations_pre_path");
			}

			fileWriter.write(sb.toString());
			fileWriter.flush();
		} catch (IOException e) {
			logger.error("文件处理失败", e);
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (fileWriter != null) {
					fileWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return true;
	}

}
