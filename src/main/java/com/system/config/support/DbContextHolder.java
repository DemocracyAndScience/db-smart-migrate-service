package com.system.config.support;

public class DbContextHolder {

	 // ThreadLocal是线程安全的，并且不能在多线程之间共享。  
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	
	public static void setDbType(String dbType) {
		contextHolder.set(dbType);
	}

	public static String getDbType() {
		String string = (String) contextHolder.get();
		return string;
	}

	public static void clearDbType() {
		contextHolder.remove();
	}
}
