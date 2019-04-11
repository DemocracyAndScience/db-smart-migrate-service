package com.system.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

	public static String MD5(String... args) { // 动态参数
		StringBuffer result = new StringBuffer();
		if (args == null || args.length == 0) {
			return "";
		} else {
			StringBuffer str = new StringBuffer();
			for (String string : args) {
				str.append(string);
			}
			try {
				MessageDigest digest = MessageDigest.getInstance("MD5");
				byte[] bytes = digest.digest(str.toString().getBytes());
				for (byte b : bytes) {
					String hex = Integer.toHexString(b & 0xff); // 转化十六进制
					if (hex.length() == 1) {
						result.append("0" + hex);
					} else {
						result.append(hex);
					}
				}
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return result.toString();
	}

}
