package com.system.service.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.system.dao.DBSourceSyncDao;
import com.system.dao.DBTargetConflictDao;
import com.system.dao.DBTargetSyncDao;
import com.system.entity.ColumnInfo;
import com.system.entity.ConstraintInfo;
import com.system.entity.IndexInfo;
import com.system.entity.TableInfo;
import com.system.entity.VersionInfo;
import com.system.entity.record.SourceTable;
import com.system.entity.record.TargetTable;
import com.system.entity.record.dto.ColumnDto;
import com.system.entity.record.dto.ConstraintDto;
import com.system.entity.record.dto.IndexDto;
import com.system.entity.record.dto.TableDto;
import com.system.entity.record.vo.TableSourceColumn;
import com.system.entity.record.vo.TableSourceConstraint;
import com.system.entity.record.vo.TableSourceIndex;
import com.system.entity.record.vo.TableTargetColumn;
import com.system.entity.record.vo.TableTargetConstraint;
import com.system.entity.record.vo.TableTargetIndex;
import com.system.service.DBSyncService;
import com.system.utils.PackageUtils;
import com.system.utils.componet.ConflictUtils;
import com.system.utils.componet.DBUtils;
import com.system.utils.componet.SQLGainUtils;
import com.system.utils.componet.ServiceAssistUtils;
import com.system.utils.constants.DBConstants;
import com.system.utils.constants.cons.sql.ConstraintTypeConstants;
import com.system.utils.constants.cons.sql.MethodConstants;
import com.system.utils.sqldetail.Column_Util;
import com.system.utils.sqldetail.Constraint_Util;
import com.system.utils.sqldetail.Index_Util;
import com.system.utils.sqldetail.Table_Util;

@Service
public class DBSyncServiceImpl implements DBSyncService {

	@Autowired
	private DBSourceSyncDao dBSourceSyncDao;
	@Autowired
	private DBTargetSyncDao dBTargetSyncDao;

	@Autowired
	private DBTargetConflictDao dBTargetConflictDao;
	@Autowired
	private DBUtils dBUtils;
	@Autowired
	private ConflictUtils conflictUtils;
	@Autowired
	private SQLGainUtils sQLGainUtils;
	@Autowired
	private HttpSession session;

	@Autowired
	private ServiceAssistUtils serviceAssistUtils;

	//@Transactional
	@Override
	public int syncStructure() {
		// 条件： 当上一个版本处理完成之后， 查询 source 表
		VersionInfo currentVerson = serviceAssistUtils.getCurrentVerson();
		String version = currentVerson.getVersion();
		BigDecimal currentVersonBigDecimal = new BigDecimal(version);

		String[] constraintTypes = { ConstraintTypeConstants.CONSTRAINT_TYPE_PRI };
		String tableSchema = dBUtils.getTableSchema(session);
		if(!version.equals("1")) {
			int validateSync = serviceAssistUtils.validateSync(currentVerson);
			if (validateSync != 200) {
				return validateSync;
			}
		}
		// 1. 获取 source ,target 的所有表信息
		List<String> notQueryTables = DBConstants.getAllTables();
		List<TableInfo> querySourceTableInfos = dBSourceSyncDao.querySourceTableInfos(tableSchema, notQueryTables);
		if (querySourceTableInfos == null || querySourceTableInfos.isEmpty()) {
			throw new RuntimeException("源数据库没有数据");
		}

		List<TableInfo> queryTargetTableInfos = dBTargetSyncDao.queryTargetTableInfos(tableSchema, notQueryTables);
		// 2. 查询source ,target表的列信息
		List<ColumnInfo> querySourceColumnInfos = dBSourceSyncDao.querySourceColumnInfos(tableSchema, notQueryTables);
		List<ColumnInfo> queryTargetColumnInfos = dBTargetSyncDao.queryTargetColumnInfos(tableSchema, notQueryTables);
		
		// 3. 查询source ,target 表的索引信息
		List<IndexInfo> querySourceIndexInfos = dBSourceSyncDao.querySourceIndexInfos(tableSchema, notQueryTables);
		List<IndexInfo> queryTargetIndexInfos = dBTargetSyncDao.queryTargetIndexInfos(tableSchema, notQueryTables);
		// 4. 查询source ,target 表的约束信息
		List<ConstraintInfo> querySourceConstraintInfos = dBSourceSyncDao.querySourceConstraintInfos(tableSchema,
				notQueryTables, constraintTypes);
		List<ConstraintInfo> queryTargetConstraintInfos = dBTargetSyncDao.queryTargetConstraintInfos(tableSchema,
				notQueryTables, constraintTypes);

		// 5. 封装数据
		Map<String, TableInfo> sourceMap = PackageUtils.packTables(querySourceTableInfos, querySourceColumnInfos,
				querySourceIndexInfos, querySourceConstraintInfos);
		Map<String, TableInfo> targetMap = PackageUtils.packTables(queryTargetTableInfos, queryTargetColumnInfos,
				queryTargetIndexInfos, queryTargetConstraintInfos);
		// 7.2 生成名称
		currentVersonBigDecimal = serviceAssistUtils.addVersion(currentVerson);
		// 6.1. 如果左表右表中不存在
		conflictUtils.saveConflictTable(sourceMap, targetMap, currentVersonBigDecimal);
		// 6.2 如果左表的表信息 ，与其不对应，拼接
		StringBuilder sqlSb = new StringBuilder();
		sqlSb = sQLGainUtils.getCommonTablesSql(sourceMap, targetMap, sqlSb, currentVersonBigDecimal);
		serviceAssistUtils.createPathAndsaveFile(currentVersonBigDecimal, sqlSb);
		
		return 200;

	}

	@Override
	public List<SourceTable> getSourceTables(Integer type) {
		VersionInfo currentVerson = serviceAssistUtils.getCurrentVerson();
		return dBTargetConflictDao.getSourceTables(currentVerson.getVersion(), type);
	}

	@Override
	public List<TargetTable> getTargetTables(Integer type) {
		VersionInfo currentVerson = serviceAssistUtils.getCurrentVerson();
		return dBTargetConflictDao.getTargetTables(currentVerson.getVersion(), type, null, null);
	}

	@Override
	public List<TableSourceColumn> getSourceColumns(Integer type) {
		VersionInfo currentVerson = serviceAssistUtils.getCurrentVerson();
		return dBTargetConflictDao.getSourceColumns(currentVerson.getVersion(), type);
	}

	/**
	 * tableName 可能是targetName ， 也可能是sourceName
	 */
	@Override
	public List<TableTargetColumn> getTargetColumns(Integer type, String tableName) {
		VersionInfo currentVerson = serviceAssistUtils.getCurrentVerson();
		return dBTargetConflictDao.getTargetColumns(currentVerson.getVersion(), type, tableName);
	}

	@Override
	public List<TableSourceIndex> getSourceIndexs(Integer type) {
		VersionInfo currentVerson = serviceAssistUtils.getCurrentVerson();
		return dBTargetConflictDao.getSourceIndexs(currentVerson.getVersion(), type);
	}

	@Override
	public List<TableTargetIndex> getTargetIndexs(Integer type, String tableName) {
		VersionInfo currentVerson = serviceAssistUtils.getCurrentVerson();
		return dBTargetConflictDao.getTargetIndexs(currentVerson.getVersion(), type, tableName);
	}

	@Override
	public List<TableSourceConstraint> getSourceConstraints(Integer type) {
		VersionInfo currentVerson = serviceAssistUtils.getCurrentVerson();
		return dBTargetConflictDao.getSourceConstraints(currentVerson.getVersion(), type);
	}

	@Override
	public List<TableTargetConstraint> getTargetConstraints(Integer type, String tableName) {
		VersionInfo currentVerson = serviceAssistUtils.getCurrentVerson();
		return dBTargetConflictDao.getTargetConstraints(currentVerson.getVersion(), type, tableName);
	}

	@Transactional
	@Override
	public int readyTables(List<TableDto> tableDtos) {
		List<TableDto> addTables = tableDtos.stream().filter(Table_Util.test(MethodConstants.ADD))
				.collect(Collectors.toList());
		List<TableDto> deleteTables = tableDtos.stream().filter(Table_Util.test(MethodConstants.DELETE))
				.collect(Collectors.toList());
		List<TableDto> changeTables = tableDtos.stream().filter(Table_Util.test(MethodConstants.CHANGE))
				.collect(Collectors.toList());
		Date date = new Date();
		VersionInfo currentVerson = serviceAssistUtils.getCurrentVerson();
		BigDecimal currentVersonBigDecimal = new BigDecimal(currentVerson.getVersion());

		StringBuilder sqlSb = new StringBuilder();
		if (addTables != null && !addTables.isEmpty()) {
			for (TableDto tableDto : addTables) {
				tableDto.setType(2);
				tableDto.setUpdateTime(date);
				tableDto.setVersion(currentVersonBigDecimal + "");
				dBTargetConflictDao.editSourceTables(tableDto);
			}
			// 添加SQL
			String tableSchema = dBUtils.getTableSchema(session);
			List<String> sourceTables = addTables.stream().map(table -> {
				return table.getSourceTableName();
			}).collect(Collectors.toList());
			List<TableInfo> queryLookSourceTableInfos = dBSourceSyncDao.queryLookSourceTableInfos(tableSchema,
					sourceTables);
			List<ColumnInfo> queryLookSourceColumnInfos = dBSourceSyncDao.queryLookSourceColumnInfos(tableSchema,
					sourceTables);
			List<IndexInfo> queryLookSourceIndexInfos = dBSourceSyncDao.queryLookSourceIndexInfos(tableSchema,
					sourceTables);
			String[] constraintTypes = { ConstraintTypeConstants.CONSTRAINT_TYPE_PRI };
			List<ConstraintInfo> queryLookSourceConstraintInfos = dBSourceSyncDao
					.queryLookSourceConstraintInfos(tableSchema, sourceTables, constraintTypes);
			Map<String, TableInfo> sourceMap = PackageUtils.packTables(queryLookSourceTableInfos,
					queryLookSourceColumnInfos, queryLookSourceIndexInfos,
					queryLookSourceConstraintInfos /* , querySourcePartitionInfos */ );
			sqlSb = SQLGainUtils.getNotInTargetTablesSql(sourceMap, sqlSb);
		}
		if (deleteTables != null && !deleteTables.isEmpty()) {
			for (TableDto tableDto : deleteTables) {
				tableDto.setType(2);
				tableDto.setUpdateTime(date);
				tableDto.setVersion(currentVersonBigDecimal + "");
				dBTargetConflictDao.editTargetTables(tableDto);
			}
			sqlSb = SQLGainUtils.dropTargetTables(deleteTables, sqlSb);

		}
		if (changeTables != null && !changeTables.isEmpty()) {
			for (TableDto tableDto : changeTables) {
				tableDto.setType(2);
				tableDto.setUpdateTime(date);
				tableDto.setVersion(currentVersonBigDecimal + "");
				dBTargetConflictDao.editSourceTables(tableDto);
				dBTargetConflictDao.changeTargetTables(tableDto);
			}
			sqlSb = SQLGainUtils.changeNameTargetTables(changeTables, sqlSb);
		}
		// 查询改变的表 的 列，索引和 约束是否不一样， 如果不一样记录，存表
		if (changeTables != null && !changeTables.isEmpty()) {
			sqlSb = lookReadyTables(changeTables, sqlSb);
			// 将target 所在的列， 索引，约束，改更一下// 版本 -- type 1 -- target名称 --
			changeColumnsIndexsAndConstraintsTableName(changeTables);
		}
		// 7. 将所有 SQL 追加到文件，
		if (sqlSb != null && sqlSb.length() > 0) {
			serviceAssistUtils.createPathAndsaveFile(currentVersonBigDecimal, sqlSb);
		}
		return 200;
	}

	@Override
	public StringBuilder lookReadyTables(List<TableDto> tableDtos, StringBuilder sqlSb) {

		String tableSchema = dBUtils.getTableSchema(session);
		List<String> sourceTables = tableDtos.stream().map(table -> {
			return table.getSourceTableName();
		}).collect(Collectors.toList());
		List<String> targetTables = tableDtos.stream().map(table -> {
			return table.getTargetTableName();
		}).collect(Collectors.toList());

		List<TableInfo> queryLookSourceTableInfos = dBSourceSyncDao.queryLookSourceTableInfos(tableSchema,
				sourceTables);
		List<TableInfo> queryLookTargetTableInfos = dBTargetSyncDao.queryLookTargetTableInfos(tableSchema,
				targetTables);

		List<ColumnInfo> queryLookSourceColumnInfos = dBSourceSyncDao.queryLookSourceColumnInfos(tableSchema,
				sourceTables);
		List<ColumnInfo> queryLookTargetColumnInfos = dBTargetSyncDao.queryLookTargetColumnInfos(tableSchema,
				targetTables);

		List<IndexInfo> queryLookSourceIndexInfos = dBSourceSyncDao.queryLookSourceIndexInfos(tableSchema,
				sourceTables);
		List<IndexInfo> queryLookTargetIndexInfos = dBTargetSyncDao.queryLookTargetIndexInfos(tableSchema,
				targetTables);

		String[] constraintTypes = { ConstraintTypeConstants.CONSTRAINT_TYPE_PRI  };
		List<ConstraintInfo> queryLookSourceConstraintInfos = dBSourceSyncDao
				.queryLookSourceConstraintInfos(tableSchema, sourceTables, constraintTypes);
		List<ConstraintInfo> queryLookTargetConstraintInfos = dBTargetSyncDao
				.queryLookTargetConstraintInfos(tableSchema, targetTables, constraintTypes);

		Map<String, TableInfo> sourceMap = PackageUtils.packTables(queryLookSourceTableInfos,
				queryLookSourceColumnInfos, queryLookSourceIndexInfos, queryLookSourceConstraintInfos);
		Map<String, TableInfo> targetMap = PackageUtils.packTables(queryLookTargetTableInfos,
				queryLookTargetColumnInfos, queryLookTargetIndexInfos, queryLookTargetConstraintInfos);

		VersionInfo currentVerson = serviceAssistUtils.getCurrentVerson();
		BigDecimal currentVersonBigDecimal = new BigDecimal(currentVerson.getVersion());

		sqlSb = sQLGainUtils.getCommonTablesSql(sourceMap, targetMap, sqlSb, tableDtos, currentVersonBigDecimal);
		return sqlSb;
	}

	@Override
	public int changeColumnsIndexsAndConstraintsTableName(List<TableDto> tableDtos) {
		VersionInfo currentVerson = serviceAssistUtils.getCurrentVerson();
		BigDecimal currentVersonBigDecimal = new BigDecimal(currentVerson.getVersion());

		String currentVersonStr = currentVersonBigDecimal + "";
		for (TableDto tableDto : tableDtos) {
			dBTargetConflictDao.changeTargetColumnsTableName(currentVersonStr, 1, tableDto.getTargetTableName(),
					tableDto.getSourceTableName());
			dBTargetConflictDao.changeTargetIndexsTableName(currentVersonStr, 1, tableDto.getTargetTableName(),
					tableDto.getSourceTableName());
			dBTargetConflictDao.changeTargetConstraintsTableName(currentVersonStr, 1, tableDto.getTargetTableName(),
					tableDto.getSourceTableName());
		}
		return 200;
	}

	@Override
	public int readyColumns(List<ColumnDto> columnDtos) {
		List<ColumnDto> addColumns = columnDtos.stream().filter(Column_Util.test(MethodConstants.ADD))
				.collect(Collectors.toList());
		List<ColumnDto> deleteColumns = columnDtos.stream().filter(Column_Util.test(MethodConstants.DELETE))
				.collect(Collectors.toList());
		List<ColumnDto> changeColumns = columnDtos.stream().filter(Column_Util.test(MethodConstants.CHANGE))
				.collect(Collectors.toList());
		Date date = new Date();
		VersionInfo currentVerson = serviceAssistUtils.getCurrentVerson();
		BigDecimal currentVersonBigDecimal = new BigDecimal(currentVerson.getVersion());

		String tableSchema = dBUtils.getTableSchema(session);

		StringBuilder sqlSb = new StringBuilder();
		if (addColumns != null && !addColumns.isEmpty()) {
			for (ColumnDto columnDto : addColumns) {
				columnDto.setType(2);
				columnDto.setUpdateTime(date);
				columnDto.setVersion(currentVersonBigDecimal + "");
				dBTargetConflictDao.editSourceColumns(columnDto);
			}

			// 生成SQL ， 但是添加， 需要原来的已经改变的表的信息，
			Set<String> sourceTables = addColumns.stream().map(column -> {
				return column.getTableName();
			}).collect(Collectors.toSet());
			List<TableInfo> queryLookSourceTableInfos = dBSourceSyncDao.queryLookSourceTableInfos(tableSchema,
					sourceTables);
			List<ColumnInfo> queryLookSourceColumnInfos = dBSourceSyncDao.queryLookSourceColumnInfos(tableSchema,
					sourceTables);
			List<IndexInfo> queryLookSourceIndexInfos = dBSourceSyncDao.queryLookSourceIndexInfos(tableSchema,
					sourceTables);
			String[] constraintTypes = { ConstraintTypeConstants.CONSTRAINT_TYPE_PRI };
			List<ConstraintInfo> queryLookSourceConstraintInfos = dBSourceSyncDao
					.queryLookSourceConstraintInfos(tableSchema, sourceTables, constraintTypes);
			Map<String, TableInfo> sourceMap = PackageUtils.packTables(queryLookSourceTableInfos,
					queryLookSourceColumnInfos, queryLookSourceIndexInfos, queryLookSourceConstraintInfos);
			sqlSb = SQLGainUtils.addTargetColumns(sourceMap, addColumns, sqlSb);
		}
		if (deleteColumns != null && !deleteColumns.isEmpty()) {
			for (ColumnDto columnDto : deleteColumns) {
				columnDto.setType(2);
				columnDto.setUpdateTime(date);
				columnDto.setVersion(currentVersonBigDecimal + "");
				dBTargetConflictDao.editTargetColumns(columnDto);
			}
			sqlSb = SQLGainUtils.dropTargetColumns(deleteColumns, sqlSb);

		}
		if (changeColumns != null && !changeColumns.isEmpty()) {
			for (ColumnDto columnDto : changeColumns) {
				columnDto.setType(2);
				columnDto.setUpdateTime(date);
				columnDto.setVersion(currentVersonBigDecimal + "");
				dBTargetConflictDao.editSourceColumns(columnDto);
				dBTargetConflictDao.changeTargetColumns(columnDto);
			}
			Set<String> sourceTables = changeColumns.stream().map(column -> {
				return column.getTableName();
			}).collect(Collectors.toSet());
			List<TableInfo> queryLookSourceTableInfos = dBSourceSyncDao.queryLookSourceTableInfos(tableSchema,
					sourceTables);
			List<ColumnInfo> queryLookSourceColumnInfos = dBSourceSyncDao.queryLookSourceColumnInfos(tableSchema,
					sourceTables);
			List<IndexInfo> queryLookSourceIndexInfos = dBSourceSyncDao.queryLookSourceIndexInfos(tableSchema,
					sourceTables);
			String[] constraintTypes = { ConstraintTypeConstants.CONSTRAINT_TYPE_PRI  };
			List<ConstraintInfo> queryLookSourceConstraintInfos = dBSourceSyncDao
					.queryLookSourceConstraintInfos(tableSchema, sourceTables, constraintTypes);
			Map<String, TableInfo> sourceMap = PackageUtils.packTables(queryLookSourceTableInfos,
					queryLookSourceColumnInfos, queryLookSourceIndexInfos, queryLookSourceConstraintInfos);
			sqlSb = SQLGainUtils.changeNameTargetColumns(sourceMap, changeColumns, sqlSb);
		}

		// 查询改变的表 的 列，索引和 约束是否不一样， 如果不一样记录，存表
		if ((deleteColumns != null && !deleteColumns.isEmpty())) {
			lookReadyColumns(deleteColumns, sqlSb);
		}
		// 7. 将所有 SQL 追加到文件，
		if (sqlSb != null && sqlSb.length() > 0) {
			serviceAssistUtils.createPathAndsaveFile(currentVersonBigDecimal, sqlSb);
		}

		return 200;
	}

	/**
	 * 因为如果更改了列 ， 对应的索引和约束，都会自动失效， 所以不必要写 drop语句 ， 需要从表中将索引和约束进行delete ，以便于不显示，
	 */
	@Override
	public StringBuilder lookReadyColumns(List<ColumnDto> columnDtos, StringBuilder sqlSb) {
		String tableSchema = dBUtils.getTableSchema(session);
		VersionInfo currentVerson = serviceAssistUtils.getCurrentVerson();
		BigDecimal currentVersonBigDecimal = new BigDecimal(currentVerson.getVersion());
		Set<String> sourceTableNames = columnDtos.stream().map(columnDto -> {
			return columnDto.getTableName();
		}).collect(Collectors.toSet());
		
		Map<String, String> packSourceTargetTables = serviceAssistUtils.getSourceTargetTables(currentVersonBigDecimal,
				sourceTableNames);

		Collection<String> targetTableNames = packSourceTargetTables.values();
		List<TableInfo> queryLookTargetTableInfos = dBTargetSyncDao.queryLookTargetTableInfos(tableSchema,
				targetTableNames);

		List<IndexInfo> queryLookTargetIndexInfos = dBTargetSyncDao.queryLookTargetIndexInfos(tableSchema,
				targetTableNames);

		String[] constraintTypes = { ConstraintTypeConstants.CONSTRAINT_TYPE_PRI  };
		List<ConstraintInfo> queryLookTargetConstraintInfos = dBTargetSyncDao
				.queryLookTargetConstraintInfos(tableSchema, targetTableNames, constraintTypes);

		Map<String, TableInfo> targetMap = PackageUtils.packTables(queryLookTargetTableInfos, null,
				queryLookTargetIndexInfos, queryLookTargetConstraintInfos);
		sQLGainUtils.setDeleteIndexAndConstraintsByColumns(targetMap, packSourceTargetTables, columnDtos,
				currentVersonBigDecimal);
		return null;
	}

	@Override
	public int readyIndexs(List<IndexDto> indexDtos) {
		List<IndexDto> addIndexs = indexDtos.stream().filter(Index_Util.test(MethodConstants.ADD))
				.collect(Collectors.toList());
		List<IndexDto> deleteIndexs = indexDtos.stream().filter(Index_Util.test(MethodConstants.DELETE))
				.collect(Collectors.toList());
		List<IndexDto> changeIndexs = indexDtos.stream().filter(Index_Util.test(MethodConstants.CHANGE))
				.collect(Collectors.toList());
		Date date = new Date();
		VersionInfo currentVerson = serviceAssistUtils.getCurrentVerson();
		BigDecimal currentVersonBigDecimal = new BigDecimal(currentVerson.getVersion());

		String tableSchema = dBUtils.getTableSchema(session);
		StringBuilder sqlSb = new StringBuilder();
		if (addIndexs != null && !addIndexs.isEmpty()) {
			for (IndexDto indexDto : addIndexs) {
				indexDto.setType(2);
				indexDto.setUpdateTime(date);
				indexDto.setVersion(currentVersonBigDecimal + "");
				dBTargetConflictDao.editSourceIndexs(indexDto);
			}
			// 生成SQL ， 但是添加， 需要原来的已经改变的表的信息，
			Set<String> sourceTables = addIndexs.stream().map(index -> {
				return index.getTableName();
			}).collect(Collectors.toSet());
			List<TableInfo> queryLookSourceTableInfos = dBSourceSyncDao.queryLookSourceTableInfos(tableSchema,
					sourceTables);
			List<IndexInfo> queryLookSourceIndexInfos = dBSourceSyncDao.queryLookSourceIndexInfos(tableSchema,
					sourceTables);
			Map<String, TableInfo> sourceMap = PackageUtils.packTables(queryLookSourceTableInfos, null,
					queryLookSourceIndexInfos, null);
			sqlSb = SQLGainUtils.addTargetIndexs(sourceMap, addIndexs, sqlSb);
		}
		if (deleteIndexs != null && !deleteIndexs.isEmpty()) {
			for (IndexDto indexDto : deleteIndexs) {
				indexDto.setType(2);
				indexDto.setUpdateTime(date);
				indexDto.setVersion(currentVersonBigDecimal + "");
				dBTargetConflictDao.editTargetIndexs(indexDto);
			}
			sqlSb = SQLGainUtils.dropTargetIndexs(deleteIndexs, sqlSb);

		}
		if (changeIndexs != null && !changeIndexs.isEmpty()) {

			for (IndexDto indexDto : changeIndexs) {
				indexDto.setType(2);
				indexDto.setUpdateTime(date);
				indexDto.setVersion(currentVersonBigDecimal + "");
				dBTargetConflictDao.editSourceIndexs(indexDto);
				dBTargetConflictDao.changeTargetIndexs(indexDto);
			}
			Set<String> sourceTables = changeIndexs.stream().map(column -> {
				return column.getTableName();
			}).collect(Collectors.toSet());
			List<TableInfo> queryLookSourceTableInfos = dBSourceSyncDao.queryLookSourceTableInfos(tableSchema,
					sourceTables);
			List<IndexInfo> queryLookSourceIndexInfos = dBSourceSyncDao.queryLookSourceIndexInfos(tableSchema,
					sourceTables);
			Map<String, TableInfo> sourceMap = PackageUtils.packTables(queryLookSourceTableInfos, null,
					queryLookSourceIndexInfos, null);
			sqlSb = SQLGainUtils.changeTargetIndexs(sourceMap, deleteIndexs, sqlSb);
		}
		// 7. 将所有 SQL 追加到文件，
		if (sqlSb != null && sqlSb.length() > 0) {
			serviceAssistUtils.createPathAndsaveFile(currentVersonBigDecimal, sqlSb);
		}
		return 200;
	}

	@Override
	public int readyConstraints(List<ConstraintDto> constraintDtos) {
		List<ConstraintDto> addConstraints = constraintDtos.stream().filter(Constraint_Util.test(MethodConstants.ADD))
				.collect(Collectors.toList());
		List<ConstraintDto> deleteConstraints = constraintDtos.stream()
				.filter(Constraint_Util.test(MethodConstants.DELETE)).collect(Collectors.toList());
		List<ConstraintDto> changeConstraints = constraintDtos.stream()
				.filter(Constraint_Util.test(MethodConstants.CHANGE)).collect(Collectors.toList());

		Date date = new Date();
		VersionInfo currentVerson = serviceAssistUtils.getCurrentVerson();
		BigDecimal currentVersonBigDecimal = new BigDecimal(currentVerson.getVersion());
		String tableSchema = dBUtils.getTableSchema(session);
		StringBuilder sqlSb = new StringBuilder();
		if (addConstraints != null && !addConstraints.isEmpty()) {
			for (ConstraintDto constraintDto : addConstraints) {
				constraintDto.setType(2);
				constraintDto.setUpdateTime(date);
				constraintDto.setVersion(currentVersonBigDecimal + "");
				dBTargetConflictDao.editSourceConstraints(constraintDto);
			}
			// 生成SQL ， 但是添加， 需要原来的已经改变的表的信息，
			Set<String> sourceTables = addConstraints.stream().map(index -> {
				return index.getTableName();
			}).collect(Collectors.toSet());
			List<TableInfo> queryLookSourceTableInfos = dBSourceSyncDao.queryLookSourceTableInfos(tableSchema,
					sourceTables);
			
			String[] constraintTypes = { ConstraintTypeConstants.CONSTRAINT_TYPE_PRI  };
			List<ConstraintInfo> queryLookSourceConstraintInfos = dBSourceSyncDao
					.queryLookSourceConstraintInfos(tableSchema, sourceTables, constraintTypes);
			Map<String, TableInfo> sourceMap = PackageUtils.packTables(queryLookSourceTableInfos, null, null,
					queryLookSourceConstraintInfos);
			sqlSb = SQLGainUtils.addTargetConstraints(sourceMap, addConstraints, sqlSb);
		}
		if (deleteConstraints != null && !deleteConstraints.isEmpty()) {
			for (ConstraintDto constraintDto : deleteConstraints) {
				constraintDto.setType(2);
				constraintDto.setUpdateTime(date);
				constraintDto.setVersion(currentVersonBigDecimal + "");
				dBTargetConflictDao.editTargetConstraints(constraintDto);
			}
			
			Set<String> sourceTableNames = deleteConstraints.stream().map(columnDto -> {
				return columnDto.getTableName();
			}).collect(Collectors.toSet());
			Map<String, String> packSourceTargetTables = serviceAssistUtils.getSourceTargetTables(currentVersonBigDecimal,
					sourceTableNames);
			Collection<String> targetTableNames = packSourceTargetTables.values();
			List<TableInfo> queryLookTargetTableInfos = dBTargetSyncDao.queryLookTargetTableInfos(tableSchema,
					targetTableNames);
			List<ColumnInfo> queryLookTargetColumnInfos = dBTargetSyncDao.queryLookTargetColumnInfos(tableSchema, targetTableNames);
			Map<String, TableInfo> targetMap = PackageUtils.packTables(queryLookTargetTableInfos, queryLookTargetColumnInfos, null, null);
			
			sqlSb = SQLGainUtils.dropTargetConstraints(deleteConstraints, targetMap , packSourceTargetTables , sqlSb);
		}
		if (changeConstraints != null && !changeConstraints.isEmpty()) {

			for (ConstraintDto constraintDto : changeConstraints) {
				constraintDto.setType(2);
				constraintDto.setUpdateTime(date);
				constraintDto.setVersion(currentVersonBigDecimal + "");
				dBTargetConflictDao.editSourceConstraints(constraintDto);
				dBTargetConflictDao.changeTargetConstraints(constraintDto);
			}
			Set<String> sourceTables = changeConstraints.stream().map(column -> {
				return column.getTableName();
			}).collect(Collectors.toSet());
			List<TableInfo> queryLookSourceTableInfos = dBSourceSyncDao.queryLookSourceTableInfos(tableSchema,
					sourceTables);
			List<IndexInfo> queryLookSourceIndexInfos = dBSourceSyncDao.queryLookSourceIndexInfos(tableSchema,
					sourceTables);
			Map<String, TableInfo> sourceMap = PackageUtils.packTables(queryLookSourceTableInfos, null,
					queryLookSourceIndexInfos, null);
			
			Set<String> sourceTableNames = changeConstraints.stream().map(columnDto -> {
				return columnDto.getTableName();
			}).collect(Collectors.toSet());
			Map<String, String> packSourceTargetTables = serviceAssistUtils.getSourceTargetTables(currentVersonBigDecimal,
					sourceTableNames);
			Collection<String> targetTableNames = packSourceTargetTables.values();
			List<TableInfo> queryLookTargetTableInfos = dBTargetSyncDao.queryLookTargetTableInfos(tableSchema,
					targetTableNames);
			List<ColumnInfo> queryLookTargetColumnInfos = dBTargetSyncDao.queryLookTargetColumnInfos(tableSchema, targetTableNames);
			Map<String, TableInfo> targetMap = PackageUtils.packTables(queryLookTargetTableInfos, queryLookTargetColumnInfos, null, null);
			
			
			sqlSb = SQLGainUtils.changeTargetConstraints(sourceMap,targetMap , changeConstraints,packSourceTargetTables,  sqlSb);
		}
		// 7. 将所有 SQL 追加到文件，
		if (sqlSb != null && sqlSb.length() > 0) {
			serviceAssistUtils.createPathAndsaveFile(currentVersonBigDecimal, sqlSb);
		}

		return 200;
	}


}
