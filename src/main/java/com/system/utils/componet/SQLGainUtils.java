package com.system.utils.componet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.entity.ColumnInfo;
import com.system.entity.ConstraintInfo;
import com.system.entity.IndexInfo;
import com.system.entity.TableInfo;
import com.system.entity.record.dto.ColumnDto;
import com.system.entity.record.dto.ConstraintDto;
import com.system.entity.record.dto.IndexDto;
import com.system.entity.record.dto.TableDto;

/**
 * SQL 获得工具类
 * 
 * @author noah
 *
 */
@Component
public class SQLGainUtils {

	private static final Logger logger = LoggerFactory.getLogger(SQLGainUtils.class);

	@Autowired
	SQLGennerateUtils sQLGennerateUtils;

	@Autowired
	ConflictUtils conflictUtils;

	/**
	 * 获取没有在 目标表里存在的Sql ,目标创建
	 * 
	 * @param tableSchema
	 * @param sourceMap
	 * @param targetMap
	 * @param sqlSb
	 * @return
	 */
	public static StringBuilder getNotInTargetTablesSql(Map<String, TableInfo> sourceMap, StringBuilder sqlSb) {
		Set<String> sourceTableNames = sourceMap.keySet();
		logger.info("目标库中不存在，但是源表里存在的需要创建的表： {}", sourceTableNames);
		for (String sourceTable : sourceTableNames) {
			TableInfo tableInfo = sourceMap.get(sourceTable);
			String createTable = SQLGennerateUtils.createTable(tableInfo);
			createTable += "\r\n\r\n";
			sqlSb.append(createTable);
		}
		return sqlSb;
	}

	/**
	 * 如果一般情况的拼接SQL
	 * 
	 * @param tableSchema
	 * @param sourceMap
	 * @param targetMap
	 * @param sqlSb
	 * @param cuurentMaxVersion
	 * @return
	 */
	public StringBuilder getCommonTablesSql(Map<String, TableInfo> sourceMap, Map<String, TableInfo> targetMap,
			StringBuilder sqlSb, BigDecimal cuurentMaxVersion) {
		Set<String> sourceKeySet = sourceMap.keySet();
		for (String tableName : sourceKeySet) {
			TableInfo sourceTableInfo = sourceMap.get(tableName);
			TableInfo targetTableInfo = targetMap.get(tableName);
			if (targetTableInfo != null) {
				String sql = sQLGennerateUtils.getCommonTablesSql(sourceTableInfo, targetTableInfo, cuurentMaxVersion);
				sql += "\r\n\r\n";
				sqlSb.append(sql);
			}
		}
		return sqlSb;
	}

	/**
	 * 如果一般情况的拼接SQL
	 * 
	 * @param tableSchema
	 * @param sourceMap
	 * @param targetMap
	 * @param sqlSb
	 * @param cuurentMaxVersion
	 * @return
	 */
	public StringBuilder getCommonTablesSql(Map<String, TableInfo> sourceMap, Map<String, TableInfo> targetMap,
			StringBuilder sqlSb, List<TableDto> tableDtos, BigDecimal cuurentMaxVersion) {
		for (TableDto tableDto : tableDtos) {
			TableInfo sourceTableInfo = sourceMap.get(tableDto.getSourceTableName());
			TableInfo targetTableInfo = targetMap.get(tableDto.getTargetTableName());
			if (targetTableInfo != null) {
				String sql = sQLGennerateUtils.getCommonTablesSql(sourceTableInfo, targetTableInfo, cuurentMaxVersion);
				sql += "\r\n\r\n";
				sqlSb.append(sql);
			}
		}

		return sqlSb;
	}

	/**
	 * 删除
	 * 
	 * @param deleteTables
	 * @param sqlSb
	 * @return
	 */
	public static StringBuilder dropTargetTables(List<TableDto> deleteTables, StringBuilder sqlSb) {
		for (TableDto tableDto : deleteTables) {
			sqlSb.append(SQLGennerateUtils.dropTable(tableDto.getTargetTableName()));
		}
		sqlSb.append("\r\n\r\n");
		return sqlSb;
	}

	/**
	 * 更名
	 * 
	 * @param changeTables
	 * @param sqlSb
	 * @return
	 */
	public static StringBuilder changeNameTargetTables(List<TableDto> changeTables, StringBuilder sqlSb) {
		for (TableDto tableDto : changeTables) {
			sqlSb.append(SQLGennerateUtils.changeNameTargetTables(tableDto));
		}
		return sqlSb;
	}

	public static StringBuilder addTargetColumns(Map<String, TableInfo> sourceMap, List<ColumnDto> addColumns,
			StringBuilder sqlSb) {

		for (ColumnDto columnDto : addColumns) {
			// 查询table
			String tableName = columnDto.getTableName();
			TableInfo tableInfo = sourceMap.get(tableName);
			List<ColumnInfo> columnInfos = tableInfo.getColumnInfos();
			for (ColumnInfo columnInfo : columnInfos) {
				// 查询列
				String sourceColumnName = columnDto.getSourceColumnName();
				if (sourceColumnName.equals(columnInfo.getColumnName())) {
					// 拼接 SQL
					sqlSb.append(SQLGennerateUtils.addTargetColumns(columnInfo));
					break;
				}
			}
		}
		return sqlSb;
	}

	public static StringBuilder dropTargetColumns(List<ColumnDto> deleteColumns, StringBuilder sqlSb) {
		for (ColumnDto columnDto : deleteColumns) {
			sqlSb.append(SQLGennerateUtils.deleteTargetColumns(columnDto));
		}
		return sqlSb;
	}

	public static StringBuilder changeNameTargetColumns(Map<String, TableInfo> sourceMap, List<ColumnDto> changeColumns,
			StringBuilder sqlSb) {
		for (ColumnDto columnDto : changeColumns) {
			String tableName = columnDto.getTableName();
			TableInfo tableInfo = sourceMap.get(tableName);
			List<ColumnInfo> columnInfos = tableInfo.getColumnInfos();
			for (ColumnInfo columnInfo : columnInfos) {
				String sourceColumnName = columnDto.getSourceColumnName();
				if (columnInfo.getColumnName().equals(sourceColumnName)) {
					sqlSb.append(SQLGennerateUtils.changeNameTargetColumns(columnDto, columnInfo));
					break;
				}
			}
		}
		return sqlSb;
	}

	public void setDeleteIndexAndConstraintsByColumns(Map<String, TableInfo> targetMap,
			Map<String, String> packSourceTargetTables, List<ColumnDto> columnDtos, BigDecimal currentVerson) {
		List<Entry<String, Set<String>>> deleteIndexs = new ArrayList<>();
		List<Entry<String, Set<String>>> deleteConstriants = new ArrayList<>();
		for (Entry<String, String> sourceTargetTable : packSourceTargetTables.entrySet()) {
			String value = sourceTargetTable.getValue();
			TableInfo targetTableInfo = targetMap.get(value);
			if (targetTableInfo != null) {
				if (columnDtos != null && !columnDtos.isEmpty()) {

					Entry<String, Set<String>> setDeleteIndexsByColumns = sQLGennerateUtils
							.setDeleteIndexsByColumns(targetTableInfo, columnDtos, sourceTargetTable);
					if (setDeleteIndexsByColumns != null) {
						deleteIndexs.add(setDeleteIndexsByColumns);
					}

					Entry<String, Set<String>> setDeleteConstriantsByColumns = sQLGennerateUtils
							.setDeleteConstriantsByColumns(targetTableInfo, columnDtos, sourceTargetTable);
					if (setDeleteConstriantsByColumns != null) {
						deleteConstriants.add(setDeleteConstriantsByColumns);
					}
				}
			}
		}
		if (!deleteIndexs.isEmpty()) {
			conflictUtils.deleteIndexs(deleteIndexs, currentVerson);
		}
		if (!deleteConstriants.isEmpty()) {
			conflictUtils.deleteConstriants(deleteConstriants, currentVerson);
		}

	}

	public static StringBuilder addTargetIndexs(Map<String, TableInfo> sourceMap, List<IndexDto> addIndexs,
			StringBuilder sqlSb) {

		for (IndexDto indexDto : addIndexs) {
			// 查询table
			String tableName = indexDto.getTableName();
			TableInfo tableInfo = sourceMap.get(tableName);
			List<IndexInfo> indexInfos = tableInfo.getIndexInfos();
			for (IndexInfo indexInfo : indexInfos) {
				// 查询列
				if (indexDto.getSourceIndexName().equals(indexInfo.getIndexName())) {
					sqlSb.append(SQLGennerateUtils.addTargetIndexs(indexInfo));
					break;
				}
			}
		}
		return sqlSb;
	}

	public static StringBuilder dropTargetIndexs(List<IndexDto> deleteIndexs, StringBuilder sqlSb) {
		for (IndexDto indexDto : deleteIndexs) {
			sqlSb.append(SQLGennerateUtils.dropTargetIndexs(indexDto));
			break;
		}
		return sqlSb;
	}

	public static StringBuilder changeTargetIndexs(Map<String, TableInfo> sourceMap,  List<IndexDto> changeIndexs, StringBuilder sqlSb) {
		for (IndexDto indexDto : changeIndexs) {
			String tableName = indexDto.getTableName();
			TableInfo tableInfo = sourceMap.get(tableName);
			List<IndexInfo> indexInfos = tableInfo.getIndexInfos();
			for (IndexInfo indexInfo : indexInfos) {
				String indexName = indexInfo.getIndexName();
				String sourceIndexName = indexDto.getSourceIndexName();
				if (sourceIndexName.equals(indexName)) {
					sqlSb.append(SQLGennerateUtils.changeNameTargetIndexs(indexDto, indexInfo));
					break;
				}
			}
		}
		return sqlSb;
	}

	public static StringBuilder addTargetConstraints(Map<String, TableInfo> sourceMap,
			List<ConstraintDto> addConstraints, StringBuilder sqlSb) {
		for (ConstraintDto constraintDto : addConstraints) {
			// 查询table
			String tableName = constraintDto.getTableName();
			TableInfo tableInfo = sourceMap.get(tableName);
			List<ConstraintInfo> constraintInfos = tableInfo.getConstraintInfos();
			for (ConstraintInfo constraintInfo : constraintInfos) {
				// 查询列
				if (constraintDto.getSourceConstraintName().equals(constraintInfo.getConstraintName())) {
					sqlSb.append(SQLGennerateUtils.addTargetConstraints(constraintInfo));
					break;
				}
			}
		}
		return sqlSb;
	}

	public static StringBuilder dropTargetConstraints(List<ConstraintDto> deleteConstraints,Map<String, TableInfo> targetMap,  Map<String, String> packSourceTargetTables, StringBuilder sqlSb) {
		for (ConstraintDto constraintDto : deleteConstraints) {
			String sourceTableName = constraintDto.getTableName();
			String targetTableName = packSourceTargetTables.get(sourceTableName);
			TableInfo tableInfo = targetMap.get(targetTableName);
			sqlSb.append(SQLGennerateUtils.dropTargetConstraints(tableInfo , constraintDto));
			break;
		}
		return sqlSb ;
	}

	public static StringBuilder changeTargetConstraints(Map<String, TableInfo> sourceMap,
			Map<String, TableInfo> targetMap, List<ConstraintDto> changeConstraints, Map<String, String> packSourceTargetTables, StringBuilder sqlSb) {
		for (ConstraintDto constraintDto : changeConstraints) {
			String tableName = constraintDto.getTableName();
			TableInfo sourceTableInfo = sourceMap.get(tableName);
			List<ConstraintInfo> sourceConstraintInfos = sourceTableInfo.getConstraintInfos();
			String targetTableName = packSourceTargetTables.get(tableName);
			TableInfo targetTableInfo = targetMap.get(targetTableName);
			for (ConstraintInfo sourceConstraintInfo : sourceConstraintInfos) {
				String constraintName = sourceConstraintInfo.getConstraintName();
				String sourceConstraintName = constraintDto.getSourceConstraintName();
				if (constraintName.equals(sourceConstraintName)) {
					sqlSb.append(SQLGennerateUtils.changeNameTargetConstraints( targetTableInfo , constraintDto, sourceConstraintInfo));
					break;
				}
			}
		}
		return sqlSb;
	}


}
