package com.system.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.system.entity.ColumnInfo;
import com.system.entity.ConstraintInfo;
import com.system.entity.IndexInfo;
import com.system.entity.TableInfo;
import com.system.utils.sqldetail.Column_Util;
import com.system.utils.sqldetail.Constraint_Util;
import com.system.utils.sqldetail.Index_Util;

public class PackageUtils {

	/**
	 * 封装table 对象
	 * 
	 * @param queryTableInfos
	 * @param queryColumnInfos
	 * @param queryIndexInfos
	 * @param queryConstraintInfos
	 * @param queryPartitionInfos
	 * @return
	 */
	public static Map<String, TableInfo> packTables(List<TableInfo> queryTableInfos, List<ColumnInfo> queryColumnInfos,
			List<IndexInfo> queryIndexInfos,
			List<ConstraintInfo> queryConstraintInfos /* , List<PartitionInfo> queryPartitionInfos */) {
		Map<String, TableInfo> map = new HashMap<>();
		for (TableInfo tableInfo : queryTableInfos) {
			String tableName = tableInfo.getTableName();
			if (queryColumnInfos != null) {
				List<ColumnInfo> columnInfos = queryColumnInfos.stream().filter(Column_Util.equalsTableName(tableName)).collect(Collectors.toList());
				if (columnInfos != null && !columnInfos.isEmpty()) {
					Collections.sort(columnInfos, Column_Util.c);
				}
				tableInfo.setColumnInfos(columnInfos);
			}
			if (queryIndexInfos != null) {
				List<IndexInfo> indexInfos = queryIndexInfos.stream().filter(Index_Util.equalsTableName(tableName)).collect(Collectors.toList());
				tableInfo.setIndexInfos(indexInfos);
			}
			
			if (queryConstraintInfos != null) {
				List<ConstraintInfo> constraintInfos = queryConstraintInfos.stream().filter(Constraint_Util.equalsTableName(tableName)).collect(Collectors.toList());
				tableInfo.setConstraintInfos(constraintInfos);
			}
			map.put(tableName, tableInfo);
		}
		return map;
	}
}
