package com.system.utils.sqldetail;

import java.util.function.Predicate;

import org.apache.commons.lang.StringUtils;

import com.system.entity.record.dto.TableDto;
import com.system.utils.constants.cons.sql.TableOptionsConstants;

public class Table_Util {

	
	public static Predicate<TableDto> test(String method) {
		Predicate<TableDto> predicateColumn = new Predicate<TableDto>() {
			@Override
			public boolean test(TableDto t) {
				return t.getMethod().equals(method);
			}
		};
		return predicateColumn;  
	}
	
	
	/**
	 * 获取备注
	 * @param commentInfo
	 * @return
	 */
	public static String getComment(String commentInfo) {
		if(StringUtils.isBlank(commentInfo)) {
			return "" ;
		}
		return "COMMENT '"+commentInfo+"'" ; 
		
	}

	/**
	 * 获取Engine
	 * 
	 * @param comment
	 * @return
	 */
	public static String getEngine(String engine) {
		if (engine == null) {
			return "";
		}
		return "ENGINE=" + engine;
	}

	/**
	 * 获取 自动数
	 * 
	 * @param autoIncrement
	 * @return
	 */
	public static String getAutoIncrement(Long autoIncrement) {
		if (autoIncrement == null) {
			return "";
		}
		return "AUTO_INCREMENT=" + autoIncrement;
	}

	public static String getOptions(String createOptions) {
		if(createOptions == null ){
			return ""; 
		}
		return createOptions.replaceAll(TableOptionsConstants.PARTITIONED, "");
	}


}
