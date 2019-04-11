package com.system.utils.componet;

import com.system.entity.*;
import com.system.entity.record.dto.ColumnDto;
import com.system.entity.record.dto.ConstraintDto;
import com.system.entity.record.dto.IndexDto;
import com.system.entity.record.dto.TableDto;
import com.system.utils.EnumUtils;
import com.system.utils.HashEntry;
import com.system.utils.constants.cons.sql.ColumnExtraConstants;
import com.system.utils.constants.cons.sql.ConstraintTypeConstants;
import com.system.utils.sqldetail.Column_Util;
import com.system.utils.sqldetail.Constraint_Util;
import com.system.utils.sqldetail.Index_Util;
import com.system.utils.sqldetail.Table_Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;

/**
 * SQL 生成工具类
 * 
 * @author noah
 *
 */

@Component
public class SQLGennerateUtils {

	private static final Logger logger = LoggerFactory.getLogger(SQLGennerateUtils.class);
	@Autowired
	ConflictUtils conflictUtils;

	/**
	 * 删除 --更名
	 * 
	 * @param tableName
	 * @return
	 */
	public static String dropTable(String tableName) {
		return " DROP TABLE " + tableName + ";\r\n";
	}

	/**
	 * @param tableInfo
	 * @return
	 */
	public static String createTable(TableInfo tableInfo) {
		List<ColumnInfo> columnInfos = tableInfo.getColumnInfos();
		// 创建SQL 的列部分
		String sql = "CREATE TABLE " + tableInfo.getTableName() + "( \r\n";
		for (ColumnInfo columnInfo : columnInfos) {
			sql += "," + columnInfo.getColumnName() + " " + columnInfo.getColumnType() + " "
					+ Column_Util.getNull(columnInfo) + " ";
			sql += Column_Util.getDefault(columnInfo) + " " + columnInfo.getExtra() + " "
					+ Column_Util.getComment(columnInfo.getColumnComment()) + "\r\n";
		}
		sql = sql.replaceFirst(",", "");
		List<IndexInfo> indexInfos = tableInfo.getIndexInfos();
		// 创建SQL 的索引部分
		for (IndexInfo indexInfo : indexInfos) {
			List<IndexColumnInfo> indexColumnInfos = indexInfo.getIndexColumnInfos();
			sql += "," + EnumUtils.IndexIsNonUniqueEnum_MAP.get(indexInfo.getNonUnique()).getValue() + " KEY "
					+ EnumUtils.IndexIndexTypeEnumMAP.get(indexInfo.getIndexType()).getValue() + " "
					+ indexInfo.getIndexName() + " ( " + Index_Util.getStringByColumns(indexColumnInfos) + " ) "
					+ Index_Util.getComment(indexInfo.getIndexComment()) + " \r\n";
		}
		// 创建SQL 的约束部分
		List<ConstraintInfo> constraintInfos = tableInfo.getConstraintInfos();
		//

		for (ConstraintInfo constraintInfo : constraintInfos) {
			List<ConstraintColumnInfo> constraintColumnInfos = constraintInfo.getConstraintColumnInfos();
			Collections.sort(constraintColumnInfos, Constraint_Util.c);
			String constraintType = constraintInfo.getConstraintType();
			sql += ",";
			if (constraintType.equals(ConstraintTypeConstants.CONSTRAINT_TYPE_PRI)) {
				sql += " " + constraintType + " ( " + Constraint_Util.getStringByCurrentColumns(constraintColumnInfos)
						+ " ) " + "\r\n";
			}
		}

		// 增加表的尾部信息
		// ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC
		// COMMENT='作文成绩表'
		String createOptions = tableInfo.getCreateOptions();
		sql += " ) " + Table_Util.getEngine(tableInfo.getEngine()) + " "
				+ Table_Util.getAutoIncrement(tableInfo.getAutoIncrement()) + " DEFAULT CHARSET=utf8 "
				+ Table_Util.getOptions(createOptions) + " " + Table_Util.getComment(tableInfo.getTableComment())
				+ ";\r\n";
		return sql;
	}

	/**
	 * 改变表 SQL 生成
	 * 
	 * @param sourceTableInfo
	 * @param targetTableInfo
	 * @param cuurentMaxVersion
	 * @return
	 */
	public String getCommonTablesSql(TableInfo sourceTableInfo, TableInfo targetTableInfo,
			BigDecimal cuurentMaxVersion) {
		// 一般情况的表语句
		String sql = getTableInCommonSQL(sourceTableInfo, targetTableInfo);

		sql += getConstraintInCommonSQL(sourceTableInfo, targetTableInfo, cuurentMaxVersion);
		// 一般情况的列语句
		sql += getColumnInCommonSQL(sourceTableInfo, targetTableInfo, cuurentMaxVersion);
		// 一般情况下的索引的更改
		sql += getIndexInCommonSQL(sourceTableInfo, targetTableInfo, cuurentMaxVersion);
		// 一般情况下的约束数据的更改

		return sql;
	}

	/**
	 * 获取一般情况下的约束数据的更改
	 * 
	 * @param sourceTableInfo
	 * @param targetTableInfo
	 * @param cuurentMaxVersion
	 * @return
	 */
	private String getConstraintInCommonSQL(TableInfo sourceTableInfo, TableInfo targetTableInfo,
			BigDecimal cuurentMaxVersion) {
		String sql = "";
		List<ConstraintInfo> sourceConstraintInfos = sourceTableInfo.getConstraintInfos();
		List<ConstraintInfo> targetConstraintInfos = targetTableInfo.getConstraintInfos();
		if (!sourceConstraintInfos.equals(targetConstraintInfos)) {
			Map<String, ConstraintInfo> sourceConstraintInfosMap = new HashMap<>();
			for (ConstraintInfo constraintInfo : sourceConstraintInfos) {
				sourceConstraintInfosMap.put(constraintInfo.getConstraintName(), constraintInfo);
			}
			Map<String, ConstraintInfo> targetConstraintInfosMap = new HashMap<>();
			for (ConstraintInfo constraintInfo : targetConstraintInfos) {
				targetConstraintInfosMap.put(constraintInfo.getConstraintName(), constraintInfo);
			}
			 conflictUtils.saveConflictConstraint(sourceConstraintInfosMap,  targetConstraintInfosMap, cuurentMaxVersion);

			for (ConstraintInfo sourceConstraintInfo : sourceConstraintInfos) {
				String constraintName = sourceConstraintInfo.getConstraintName();

				ConstraintInfo targetconstraintInfo = targetConstraintInfosMap.get(constraintName);
				if (!sourceConstraintInfo.equals(targetconstraintInfo)) {
					String tableName = sourceConstraintInfo.getTableName();
					List<ConstraintColumnInfo> constraintColumnInfos = sourceConstraintInfo.getConstraintColumnInfos();
					
					String constraintType = sourceConstraintInfo.getConstraintType();
					if (ConstraintTypeConstants.CONSTRAINT_TYPE_PRI.equals(constraintType)) {
						// 如果 auto_increament ,则需要将其设置掉， 再执行下面的语句
						String insertSql = "";
						if(targetconstraintInfo != null ) {
							insertSql += "ALTER TABLE " + tableName + " DROP " + ConstraintTypeConstants.CONSTRAINT_TYPE_PRI
									+ ";\r\n";
						}
						
						insertSql += "ALTER TABLE " + tableName + " ADD ";

						insertSql += ConstraintTypeConstants.CONSTRAINT_TYPE_PRI + " ("
								+ Constraint_Util.getStringByCurrentColumns(constraintColumnInfos) + " );\r\n ";
						sql += getDropPrimaryKeySql(targetTableInfo, sql, tableName, insertSql);
					}
				}
			}

		}
		return sql;
	}

	/**
	 * DROP 主键
	 * 
	 * @param targetTableInfo
	 * @param sql
	 * @param tableName
	 * @return
	 */
	private static String getDropPrimaryKeySql(TableInfo targetTableInfo, String sql, String tableName,
			String insertSql) {
		// DROP 前 将 AutoIncrement 设置掉
		List<ColumnInfo> columnInfos = targetTableInfo.getColumnInfos();
		ColumnInfo yuanColumnInfo = null;
		for (ColumnInfo columnInfo : columnInfos) {
			String extra = columnInfo.getExtra();
			if (ColumnExtraConstants.AUTO_INCREMENT.equals(extra)) {
				yuanColumnInfo = new ColumnInfo();
				BeanUtils.copyProperties(columnInfo, yuanColumnInfo);
				columnInfo.setExtra(null);
				ColumnDto columnDto = new ColumnDto();
				columnDto.setTableName(tableName);
				columnDto.setTargetColumnName(columnInfo.getColumnName());
				columnDto.setSourceColumnName(columnInfo.getColumnName());
				sql += changeNameTargetColumns(columnDto, columnInfo);
				columnInfo.setExtra(extra);
				break;
			}
		}
		sql += insertSql;
		// DROP 后 将 AutoIncrement 设置掉
		if (yuanColumnInfo != null) {
			ColumnDto columnDto = new ColumnDto();
			columnDto.setTableName(tableName);
			columnDto.setTargetColumnName(yuanColumnInfo.getColumnName());
			columnDto.setSourceColumnName(yuanColumnInfo.getColumnName());
			sql += changeNameTargetColumns(columnDto, yuanColumnInfo);
		}
		return sql;
	}

	/**
	 * 获取一般情况下的索引的更改
	 * 
	 * @param sourceTableInfo
	 * @param targetTableInfo
	 * @param cuurentMaxVersion
	 * @return
	 */
	private String getIndexInCommonSQL(TableInfo sourceTableInfo, TableInfo targetTableInfo,
			BigDecimal cuurentMaxVersion) {
		String sql = "";
		List<IndexInfo> sourceIndexInfos = sourceTableInfo.getIndexInfos();
		List<IndexInfo> targetIndexInfos = targetTableInfo.getIndexInfos();

		if (!sourceIndexInfos.equals(targetIndexInfos)) {
			Map<String, IndexInfo> sourceIndexInfosMap = new HashMap<>();
			for (IndexInfo indexInfo : sourceIndexInfos) {
				sourceIndexInfosMap.put(indexInfo.getIndexName(), indexInfo);
			}
			Map<String, IndexInfo> targetIndexInfosMap = new HashMap<>();
			for (IndexInfo indexInfo : targetIndexInfos) {
				targetIndexInfosMap.put(indexInfo.getIndexName(), indexInfo);
			}
			conflictUtils.saveConflictIndex(sourceIndexInfosMap, targetIndexInfosMap, cuurentMaxVersion);
			for (IndexInfo sourceIndexInfo : sourceIndexInfos) {
				String indexName = sourceIndexInfo.getIndexName();
				IndexInfo targetIndexInfo = targetIndexInfosMap.get(indexName);
				if (targetIndexInfo != null) {
					if (!sourceIndexInfo.equals(targetIndexInfo)) {
						sql += "ALTER TABLE " + sourceIndexInfo.getTableName() + " DROP INDEX "
								+ targetIndexInfo.getIndexName() + ";\r\n";
						String indexType = sourceIndexInfo.getIndexType();
						Integer nonUnique = sourceIndexInfo.getNonUnique();
						List<IndexColumnInfo> indexColumnInfos = sourceIndexInfo.getIndexColumnInfos();
						String indexComment = sourceIndexInfo.getIndexComment();
						sql += " CREATE " + EnumUtils.IndexIsNonUniqueEnum_MAP.get(nonUnique).getValue() + " "
								+ EnumUtils.IndexIndexTypeEnumMAP.get(indexType).getValue() + " INDEX " + indexName
								+ "   ON " + sourceIndexInfo.getTableName() + " ( "
								+ Index_Util.getStringByColumns(indexColumnInfos) + " ) "
								+ Index_Util.getComment(indexComment) + "; \r\n";
					}
				}
			}
		}
		return sql;
	}

	/**
	 * 一般情况的列的更改
	 * 
	 * @param sourceTableInfo
	 * @param targetTableInfo
	 * @param cuurentMaxVersion
	 * @return
	 */
	private String getColumnInCommonSQL(TableInfo sourceTableInfo, TableInfo targetTableInfo,
			BigDecimal cuurentMaxVersion) {
		String sql = "";
		List<ColumnInfo> sourceColumnInfos = sourceTableInfo.getColumnInfos();
		List<ColumnInfo> targetColumnInfos = targetTableInfo.getColumnInfos();
		if (!sourceColumnInfos.equals(targetColumnInfos)) {
			Map<String, ColumnInfo> sourceColumnInfosMap = new HashMap<>();
			for (ColumnInfo columnInfo : sourceColumnInfos) {
				sourceColumnInfosMap.put(columnInfo.getColumnName(), columnInfo);
			}
			Map<String, ColumnInfo> targetColumnInfosMap = new HashMap<>();
			for (ColumnInfo columnInfo : targetColumnInfos) {
				targetColumnInfosMap.put(columnInfo.getColumnName(), columnInfo);
			}
			conflictUtils.saveConflictColumn(sourceColumnInfosMap, targetColumnInfosMap, cuurentMaxVersion);
			for (ColumnInfo sourceColumnInfo : sourceColumnInfos) {
				String columnName = sourceColumnInfo.getColumnName();
				ColumnInfo targetColumnInfo = targetColumnInfosMap.get(columnName);
				if (targetColumnInfo == null) {
					continue;
				}
				if (!sourceColumnInfo.equals(targetColumnInfo)) {

					String tableName = sourceColumnInfo.getTableName();

					String isNullable = sourceColumnInfo.getIsNullable();
					String extra = sourceColumnInfo.getExtra();
					String columnType = sourceColumnInfo.getColumnType();
					String columnDefault = sourceColumnInfo.getColumnDefault();
					String columnComment = sourceColumnInfo.getColumnComment();

					sql += "ALTER TABLE " + tableName + " CHANGE " + columnName + " " + columnName + " " + columnType;
					if (!isNullable.equals(targetColumnInfo.getIsNullable())) {
						sql += Column_Util.getNull(sourceColumnInfo);
					}
					String columnDefault2 = targetColumnInfo.getColumnDefault();
					if (columnDefault != columnDefault2 && !columnDefault.equals(columnDefault2)) {
						sql += " " + Column_Util.getDefault(sourceColumnInfo);
					} 
					String extra2 = targetColumnInfo.getExtra();
					if (extra != null && !extra.equals(extra2)) {
						sql += " " + extra;
					}

					if (!columnComment.equals(targetColumnInfo.getColumnComment())) {
						sql += " " + Column_Util.getComment(columnComment);
					}
					sql += "; \r\n";
				}
			}
		}
		return sql;
	}

	/**
	 * 一般情况的表更改
	 * 
	 * @param sourceTableInfo
	 * @param targetTableInfo
	 * @return
	 */
	private String getTableInCommonSQL(TableInfo sourceTableInfo, TableInfo targetTableInfo) {
		String sql = "";
		if (targetTableInfo == null) {
			return sql;
		}
		if (!sourceTableInfo.equals(targetTableInfo)) {
			String engine = sourceTableInfo.getEngine();
			Long autoIncrement = sourceTableInfo.getAutoIncrement();
			String createOptions = sourceTableInfo.getCreateOptions();
			String tableComment = sourceTableInfo.getTableComment();
			Long autoIncrement2 = targetTableInfo.getAutoIncrement();
			sql += " ALTER TABLE " + sourceTableInfo.getTableName();
			if (!engine.equals(targetTableInfo.getEngine())) {
				sql += " " + Table_Util.getEngine(engine);
			}
			if (  autoIncrement != null && !autoIncrement.equals(autoIncrement2)) {
				sql += " " + Table_Util.getAutoIncrement(autoIncrement);
			}

			if (!createOptions.equals(targetTableInfo.getCreateOptions())) {
				sql += " " + Table_Util.getOptions(createOptions);
			}
			if (!tableComment.equals(targetTableInfo.getTableComment())) {
				sql += " " + Table_Util.getComment(tableComment);
			}
			if (!engine.equals(targetTableInfo.getEngine())) {
				sql += " " + Table_Util.getEngine(engine);
			}
			sql += "; \r\n";
		}
		return sql;
	}

	/**
	 * rename table
	 * 
	 * @param tableDto
	 * @return
	 */
	public static String changeNameTargetTables(TableDto tableDto) {
		String sql = "RENAME TABLE " + tableDto.getTargetTableName() + " TO " + tableDto.getSourceTableName()
				+ "; \r\n";
		return sql;
	}

	public static String addTargetColumns(ColumnInfo columnInfo) {
		String sql = "ALTER TABLE " + columnInfo.getTableName() + " ADD " + columnInfo.getColumnName() + " "
				+ columnInfo.getColumnType();
		String isNullable = columnInfo.getIsNullable();
		if (isNullable != null) {
			sql += Column_Util.getNull(columnInfo);
		}
		String columnDefault = columnInfo.getColumnDefault();
		if (columnDefault != null) {
			sql += " " + Column_Util.getDefault(columnInfo);
		}
		String extra = columnInfo.getExtra();
		if (extra != null) {
			sql += " " + extra;
		}
		String columnComment = columnInfo.getColumnComment();
		if (columnComment != null) {
			sql += " " + Column_Util.getComment(columnComment);
		}
		sql += "; \r\n";
		return sql;
	}

	public static String deleteTargetColumns(ColumnDto columnDto) {
		String sql = " ALTER TABLE " + columnDto.getTableName() + " DROP " + columnDto.getTargetColumnName() + "; \r\n";
		return sql;
	}

	public static String changeNameTargetColumns(ColumnDto columnDto, ColumnInfo columnInfo) {
		String columnType = columnInfo.getColumnType();
		String sql = " ALTER TABLE " + columnDto.getTableName() + " CHANGE " + columnDto.getTargetColumnName() + " "
				+ columnDto.getSourceColumnName() + " " + columnType;
		String isNullable = columnInfo.getIsNullable();
		if (isNullable != null) {
			sql += Column_Util.getNull(columnInfo);
		}
		String columnDefault = columnInfo.getColumnDefault();
		if (columnDefault != null) {
			if(columnDefault.equals("0000-00-00 00:00:00")){
				logger.info("版本兼容问题 ：时间默认值： 0000-00-00 00:00:00 ");
			}else{
				sql += " " + Column_Util.getDefault(columnInfo);
			}

		}
		String extra = columnInfo.getExtra();
		if (extra != null) {
			sql += " " + extra;
		}
		String columnComment = columnInfo.getColumnComment();
		if (columnComment != null) {
			sql += " " + Column_Util.getComment(columnComment);
		}
		sql += "; \r\n";

		return sql;
	}

	/**
	 * 获取由于列变更导致的表内索引的更改
	 * 
	 * @return
	 */
	public Entry<String, Set<String>> setDeleteIndexsByColumns(TableInfo targetTableInfo, List<ColumnDto> columnDtos,
			Entry<String, String> sourceTargetTable) {
		Set<String> indexNames = new HashSet<>();
		for (ColumnDto columnDto : columnDtos) {
			boolean equals = columnDto.getTableName().equals(sourceTargetTable.getKey());
			boolean equals2 = sourceTargetTable.getValue().equals(targetTableInfo.getTableName());
			if (equals && equals2) {
				String targetColumnName = columnDto.getTargetColumnName();
				List<IndexInfo> indexInfos = targetTableInfo.getIndexInfos();
				for (IndexInfo indexInfo : indexInfos) {
					List<IndexColumnInfo> indexColumnInfos = indexInfo.getIndexColumnInfos();
					for (IndexColumnInfo indexColumnInfo : indexColumnInfos) {
						if (indexColumnInfo.getColumnName().equals(targetColumnName)) {
							indexNames.add(indexInfo.getIndexName());
						}
					}
				}
			}
		}
		if (!indexNames.isEmpty()) {
			return new HashEntry<String, Set<String>>(sourceTargetTable.getKey(), indexNames);
		}
		return null;
	}

	/**
	 * 获取由于列变更导致的表内的约束更改
	 * 
	 * @return
	 */
	public Entry<String, Set<String>> setDeleteConstriantsByColumns(TableInfo targetTableInfo,
			List<ColumnDto> columnDtos, Entry<String, String> sourceTargetTable) {

		Set<String> constriantNames = new HashSet<>();
		for (ColumnDto columnDto : columnDtos) {
			boolean equals = columnDto.getTableName().equals(sourceTargetTable.getKey());
			boolean equals2 = sourceTargetTable.getValue().equals(targetTableInfo.getTableName());
			if (equals && equals2) {
				String targetColumnName = columnDto.getTargetColumnName();
				List<ConstraintInfo> constraintInfos = targetTableInfo.getConstraintInfos();
				for (ConstraintInfo constraintInfo : constraintInfos) {
					List<ConstraintColumnInfo> constraintColumnInfos = constraintInfo.getConstraintColumnInfos();
					if (constraintColumnInfos != null) {
						Collections.sort(constraintColumnInfos, Constraint_Util.c);
					}
					for (ConstraintColumnInfo constraintColumnInfo : constraintColumnInfos) {
						if (constraintColumnInfo.getColumnName().equals(targetColumnName)) {
							constriantNames.add(constraintInfo.getConstraintName());
						}
					}
				}
			}
		}
		if (!constriantNames.isEmpty()) {
			return new HashEntry<String, Set<String>>(sourceTargetTable.getKey(), constriantNames);
		}
		return null;
	}

	public static String addTargetIndexs(IndexInfo indexInfo) {
		String indexType = indexInfo.getIndexType();
		Integer nonUnique = indexInfo.getNonUnique();
		List<IndexColumnInfo> indexColumnInfos = indexInfo.getIndexColumnInfos();
		String indexComment = indexInfo.getIndexComment();
		String indexName = indexInfo.getIndexName();
		String tableName = indexInfo.getTableName();
		String sql = " CREATE " + EnumUtils.IndexIsNonUniqueEnum_MAP.get(nonUnique).getValue() + " "
				+ EnumUtils.IndexIndexTypeEnumMAP.get(indexType).getValue() + " INDEX " + indexName + "   ON "
				+ tableName + " ( " + Index_Util.getStringByColumns(indexColumnInfos) + " ) "
				+ Index_Util.getComment(indexComment) + "; \r\n";
		return sql;
	}

	public static String dropTargetIndexs(IndexDto indexDto) {
		String sql = "ALTER TABLE " + indexDto.getTableName() + " DROP INDEX " + indexDto.getTargetIndexName()
				+ ";\r\n";
		return sql;
	}

	public static String changeNameTargetIndexs(IndexDto indexDto, IndexInfo indexInfo) {
		String sql = "ALTER TABLE " + indexInfo.getTableName() + " DROP INDEX " + indexInfo.getIndexName() + ";\r\n";

		String indexType = indexInfo.getIndexType();
		Integer nonUnique = indexInfo.getNonUnique();
		List<IndexColumnInfo> indexColumnInfos = indexInfo.getIndexColumnInfos();
		String indexComment = indexInfo.getIndexComment();

		sql += " CREATE " + EnumUtils.IndexIsNonUniqueEnum_MAP.get(nonUnique).getValue() + " "
				+ EnumUtils.IndexIndexTypeEnumMAP.get(indexType).getValue() + " INDEX " + indexDto.getSourceIndexName()
				+ "   ON " + indexDto.getTableName() + " ( " + Index_Util.getStringByColumns(indexColumnInfos) + " ) "
				+ Index_Util.getComment(indexComment) + "; \r\n";
		return sql;
	}

	public static String addTargetConstraints(ConstraintInfo constraintInfo) {
		String tableName = constraintInfo.getTableName();
		List<ConstraintColumnInfo> constraintColumnInfos = constraintInfo.getConstraintColumnInfos();
		if (constraintColumnInfos != null) {
			Collections.sort(constraintColumnInfos, Constraint_Util.c);
		}
		if (ConstraintTypeConstants.CONSTRAINT_TYPE_PRI.equals(constraintInfo.getConstraintType())) {
			String sql = "ALTER TABLE " + tableName + " ADD ";
			sql += ConstraintTypeConstants.CONSTRAINT_TYPE_PRI + " ("
					+ Constraint_Util.getStringByCurrentColumns(constraintColumnInfos) + " );\r\n ";
			return sql;
		}
		return "";
	}

	public static String dropTargetConstraints(TableInfo tableInfo, ConstraintDto constraintDto) {
		String tableName = constraintDto.getTableName();
		String sql = "";
		String insertSql = "ALTER TABLE " + tableName + " DROP " + ConstraintTypeConstants.CONSTRAINT_TYPE_PRI
				+ ";\r\n";
		sql += getDropPrimaryKeySql(tableInfo, sql, tableName, insertSql);
		return sql;
	}

	public static String changeNameTargetConstraints( TableInfo targetTableInfo, ConstraintDto constraintDto,
			ConstraintInfo constraintInfo) {
		String tableName = constraintInfo.getTableName();
		List<ConstraintColumnInfo> constraintColumnInfos = constraintInfo.getConstraintColumnInfos();
		if (constraintColumnInfos != null) {
			Collections.sort(constraintColumnInfos, Constraint_Util.c);
		}
		if (ConstraintTypeConstants.CONSTRAINT_TYPE_PRI.equals(constraintInfo.getConstraintType())) {
			String sql = "";
			String insertSql = "ALTER TABLE " + tableName + " DROP " + ConstraintTypeConstants.CONSTRAINT_TYPE_PRI
					+ ";\r\n";
			insertSql += "ALTER TABLE " + tableName + " ADD ";
			insertSql += ConstraintTypeConstants.CONSTRAINT_TYPE_PRI + " ("
					+ Constraint_Util.getStringByCurrentColumns(constraintColumnInfos) + " );\r\n ";
			sql += getDropPrimaryKeySql(targetTableInfo, sql, tableName, insertSql);

			return sql;
		}
		return "";
	}

}
