package com.system.utils.sqldetail;

import com.system.entity.ColumnInfo;
import com.system.entity.record.dto.ColumnDto;
import com.system.utils.EnumUtils;
import com.system.utils.constants.cons.sql.ColumnKeyConstants;
import com.system.utils.constants.en.sql.Column_IsNullAble_Enum;
import org.apache.commons.lang.StringUtils;

import java.util.Comparator;
import java.util.function.Predicate;

/**
 * 列 SQL 片段生成
 * @author noah
 *
 */
public class Column_Util {

	
	public static Predicate<ColumnDto> test(String method) {
		Predicate<ColumnDto> predicateColumn = new Predicate<ColumnDto>() {
			@Override
			public boolean test(ColumnDto t) {
				return t.getMethod().equals(method);
			}
		};
		return predicateColumn;  
	}
	public static Predicate<ColumnInfo> equalsTableName(String tableName) {
		Predicate<ColumnInfo> predicateColumn = new Predicate<ColumnInfo>() {
			@Override	
			public boolean test(ColumnInfo t) {
				return t.getTableName().equals(tableName);
			}
		};
		return predicateColumn;  
	}
	
	public static final Comparator<ColumnInfo> c = new Comparator<ColumnInfo>() {
		@Override
		public int compare(ColumnInfo o1, ColumnInfo o2) {
			Long seqInIndex = o1.getOrdinalPosition();
			Long seqInIndex2 = o2.getOrdinalPosition();
			if (seqInIndex < seqInIndex2) {
				return -1;
			} else if (seqInIndex.equals(seqInIndex2)) {
				return 0;
			} else {
				return 1;
			}
		}

	};
	
	
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
	 * 获取默认值
	 * @return
	 */
	public static String getDefault(ColumnInfo columnInfo) {
		
		String isNullable = columnInfo.getIsNullable();
		String columnDefault = columnInfo.getColumnDefault();
		boolean equals = Column_IsNullAble_Enum.NO.getCode().equals(isNullable);
		if (equals) {
			if(StringUtils.isBlank(columnDefault)) {
				return "" ;
			}
		}
		if (ColumnKeyConstants.PRI.equals(columnInfo.getColumnKey()) &&  StringUtils.isBlank(columnDefault) ) {
			return "" ;
		}
		// 5.7 不支持
		if ("0000-00-00".equals(columnDefault)) {
			return "" ;
		}// 5.7 不支持
		if ("0000-00-00 00:00:00".equals(columnDefault)) {
			return "" ;
		}
		String columnType = columnInfo.getColumnType();
		if(columnType.contains("date") ||columnType.contains("time")) {
			if(columnDefault != null && columnDefault.equals("CURRENT_TIMESTAMP")) {
				return "DEFAULT "+columnDefault ; 
			}
		}
		if(StringUtils.isBlank(columnDefault)) {
			return "DEFAULT  NULL " ;
		}
		return "DEFAULT '"+columnDefault+"'" ; 
	}
	/**
	 * 获取 NOT  NULL
	 *  
	 */
	public static String getNull(ColumnInfo columnInfo) {
		boolean equals = columnInfo.getColumnType().equals("timestamp");
		String isNullable = columnInfo.getIsNullable();
		if(equals && isNullable.equals(Column_IsNullAble_Enum.YES.getCode())) {
			return " NULL " ;
		}
		return EnumUtils.ColumnIsNullAbleEnum_MAP.get(isNullable).getValue(); 
	}
	
}
