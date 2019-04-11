package com.system.utils;

import java.util.HashMap;
import java.util.Map;
import com.system.utils.constants.en.sql.Column_IsNullAble_Enum;
import com.system.utils.constants.en.sql.Index_IndexType_Enum;
import com.system.utils.constants.en.sql.Index_NonUnique_Enum;

public class EnumUtils {

	/**
	 * 列 是否允许为空，
	 */
	public static final Map<String, Column_IsNullAble_Enum> ColumnIsNullAbleEnum_MAP ;
	static {
		ColumnIsNullAbleEnum_MAP = new HashMap<>();
		Column_IsNullAble_Enum[] values = Column_IsNullAble_Enum.values();
		for (Column_IsNullAble_Enum columnIsNullAbleEnum : values) {
			ColumnIsNullAbleEnum_MAP.put(columnIsNullAbleEnum.getCode(), columnIsNullAbleEnum);
		}
		
	}
	
	
	/**
	 * 索引 是否允许为空，
	 */
	public static final Map<Integer, Index_NonUnique_Enum> IndexIsNonUniqueEnum_MAP ;
	static {
		IndexIsNonUniqueEnum_MAP = new HashMap<>();
		Index_NonUnique_Enum[] values = Index_NonUnique_Enum.values();
		for (Index_NonUnique_Enum nonUniqueEnum : values) {
			IndexIsNonUniqueEnum_MAP.put(nonUniqueEnum.getCode(), nonUniqueEnum);
		}
		
	}
	
	/**
	 * 索引 类型
	 */
	public static final Map<String, Index_IndexType_Enum> IndexIndexTypeEnumMAP ;
	static {
		IndexIndexTypeEnumMAP = new HashMap<>();
		Index_IndexType_Enum[] values = Index_IndexType_Enum.values();
		for (Index_IndexType_Enum index_IndexType_Enum : values) {
			IndexIndexTypeEnumMAP.put(index_IndexType_Enum.getCode(), index_IndexType_Enum);
		}
		
	}
	

	
	
}
