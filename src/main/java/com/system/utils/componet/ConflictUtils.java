package com.system.utils.componet;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.dao.DBTargetConflictDao;
import com.system.entity.ColumnInfo;
import com.system.entity.ConstraintInfo;
import com.system.entity.IndexInfo;
import com.system.entity.TableInfo;
import com.system.entity.record.SourceColumn;
import com.system.entity.record.SourceConstraint;
import com.system.entity.record.SourceIndex;
import com.system.entity.record.SourceTable;
import com.system.entity.record.TargetColumn;
import com.system.entity.record.TargetConstraint;
import com.system.entity.record.TargetIndex;
import com.system.entity.record.TargetTable;
import com.system.utils.CollectionUtils;

/**
 * 冲突
 * 
 * @author noah
 *
 */
@Component
public class ConflictUtils {

	@Autowired
	DBTargetConflictDao dBTargetConflictDao;

	/**
	 * 保存冲突表
	 * 
	 * @param sourceMap
	 * @param targetMap
	 * @param cuurentMaxVersion
	 */
	public void saveConflictTable(Map<String, TableInfo> sourceMap, Map<String, TableInfo> targetMap,
			BigDecimal cuurentMaxVersion) {
		Set<String> sourceTableNames = sourceMap.keySet();
		Set<String> targeTableNames = targetMap.keySet();

		CopyOnWriteArraySet<String> cwSetSource = new CopyOnWriteArraySet<>(sourceTableNames);
		Iterator<String> iterator = cwSetSource.iterator();
		while (iterator.hasNext()) {
			String next = iterator.next();
			Optional<String> findFirst = targeTableNames.stream().filter( CollectionUtils.stringPredicate(next)).findFirst();
			if (findFirst.isPresent()) {
				cwSetSource.remove(next);
			}
		}
		// save sourceTable
		for (String sourceTableName : cwSetSource) {
			SourceTable sourceTable = new SourceTable();
			sourceTable.setCreateTime(new Date());
			sourceTable.setType(1);
			sourceTable.setDeleteState(1);
			sourceTable.setSourceTableName(sourceTableName);
			sourceTable.setVersion(cuurentMaxVersion.toString());
			dBTargetConflictDao.insertSourceTable(sourceTable);
		}
		// save targetTable
		CopyOnWriteArraySet<String> cwSetTarget = new CopyOnWriteArraySet<>(targeTableNames);
		Iterator<String> iterator2 = cwSetTarget.iterator();
		while (iterator2.hasNext()) {
			String next = iterator2.next();
			boolean contains = sourceTableNames.contains(next);
			if (contains) {
				cwSetTarget.remove(next);
			}
		}

		for (String targetTableName : cwSetTarget) {
			TargetTable targetTable = new TargetTable();
			targetTable.setCreateTime(new Date());
			targetTable.setDeleteState(1);
			targetTable.setType(1);
			targetTable.setTargetTableName(targetTableName);
			targetTable.setVersion(cuurentMaxVersion + "");
			dBTargetConflictDao.insertTargetTable(targetTable);
		}
	}

	/**
	 * 保存冲突列
	 * 
	 * @param sourceColumnInfosMap
	 * @param targetColumnInfosMap
	 * @param cuurentMaxVersion
	 */
	public void saveConflictColumn(Map<String, ColumnInfo> sourceColumnInfosMap,
			Map<String, ColumnInfo> targetColumnInfosMap, BigDecimal cuurentMaxVersion) {
		Set<String> keySource = sourceColumnInfosMap.keySet();
		Set<String> keyTarget = targetColumnInfosMap.keySet();

		CopyOnWriteArraySet<String> cwSetSource = new CopyOnWriteArraySet<>(keySource);
		Iterator<String> iterator = cwSetSource.iterator();
		while (iterator.hasNext()) {
			String next = iterator.next();
			boolean contains = keyTarget.contains(next);
			if (contains) {
				cwSetSource.remove(next);
			}
		}

		// save source column
		for (String sourceColumnName : cwSetSource) {
			SourceColumn sourceColumn = new SourceColumn();
			sourceColumn.setSourceColumnName(sourceColumnName);
			ColumnInfo columnInfo = sourceColumnInfosMap.get(sourceColumnName);
			sourceColumn.setSourceTableName(columnInfo.getTableName());
			sourceColumn.setCreateTime(new Date());
			sourceColumn.setType(1);
			sourceColumn.setDeleteState(1);
			sourceColumn.setVersion(cuurentMaxVersion + "");
			dBTargetConflictDao.insertSourceColumn(sourceColumn);
		}
		// save target column
		CopyOnWriteArraySet<String> cwSetTarget = new CopyOnWriteArraySet<>(keyTarget);
		Iterator<String> iterator2 = cwSetTarget.iterator();
		while (iterator2.hasNext()) {
			String next = iterator2.next();
			boolean contains = keySource.contains(next);
			if (contains) {
				cwSetTarget.remove(next);
			}
		}

		for (String targetColumnName : cwSetTarget) {
			TargetColumn targetColumn = new TargetColumn();
			targetColumn.setCreateTime(new Date());
			targetColumn.setTargetColumnName(targetColumnName);
			ColumnInfo columnInfo = targetColumnInfosMap.get(targetColumnName);
			targetColumn.setTargetTableName(columnInfo.getTableName());
			targetColumn.setType(1);
			targetColumn.setDeleteState(1);
			targetColumn.setVersion(cuurentMaxVersion + "");
			dBTargetConflictDao.insertTargetColumn(targetColumn);
		}
	}

	/**
	 * 保存冲突Index
	 * 
	 * @param sourceIndexInfosMap
	 * @param targetIndexInfosMap
	 */
	public void saveConflictIndex(Map<String, IndexInfo> sourceIndexInfosMap,
			Map<String, IndexInfo> targetIndexInfosMap, BigDecimal cuurentMaxVersion) {
		Set<String> keySource = sourceIndexInfosMap.keySet();
		Set<String> keyTarget = targetIndexInfosMap.keySet();

		CopyOnWriteArraySet<String> cwSetSource = new CopyOnWriteArraySet<>(keySource);
		Iterator<String> iterator = cwSetSource.iterator();
		while (iterator.hasNext()) {
			String next = iterator.next();
			boolean contains = keyTarget.contains(next);
			if (contains) {
				cwSetSource.remove(next);
			}
		}
		// save sourceIndex
		for (String sourceIndexName : cwSetSource) {
			SourceIndex sourceIndex = new SourceIndex();
			sourceIndex.setSourceIndexName(sourceIndexName);
			sourceIndex.setCreateTime(new Date());
			IndexInfo indexInfo = sourceIndexInfosMap.get(sourceIndexName);
			sourceIndex.setSourceTableName(indexInfo.getTableName());
			sourceIndex.setDeleteState(1);
			sourceIndex.setType(1);
			sourceIndex.setVersion(cuurentMaxVersion + "");
			dBTargetConflictDao.insertSourceIndex(sourceIndex);
		}

		// save targetIndex
		CopyOnWriteArraySet<String> cwSetTarget = new CopyOnWriteArraySet<>(keyTarget);
		Iterator<String> iterator2 = cwSetTarget.iterator();
		while (iterator2.hasNext()) {
			String next = iterator2.next();
			boolean contains = keySource.contains(next);
			if (contains) {
				cwSetTarget.remove(next);
			}
		}

		for (String targetIndexName : cwSetTarget) {
			TargetIndex targetIndex = new TargetIndex();
			targetIndex.setCreateTime(new Date());
			targetIndex.setType(1);
			targetIndex.setDeleteState(1);
			targetIndex.setTargetIndexName(targetIndexName);
			IndexInfo indexInfo = targetIndexInfosMap.get(targetIndexName);
			targetIndex.setTargetTableName(indexInfo.getTableName());
			targetIndex.setVersion(cuurentMaxVersion + "");
			dBTargetConflictDao.insertTargetIndex(targetIndex);
		}
	}

	/**
	 * 保存冲突Constriant
	 * 
	 * @param sourceIndexInfosMap
	 * @param targetIndexInfosMap
	 */
	public void saveConflictConstraint(Map<String, ConstraintInfo> sourceConstraintInfosMap,
			Map<String, ConstraintInfo> targetConstraintInfosMap, BigDecimal cuurentMaxVersion) {
		Set<String> keySource = sourceConstraintInfosMap.keySet();
		Set<String> keyTarget = targetConstraintInfosMap.keySet();
		/*
		CopyOnWriteArraySet<String> cwSetSource = new CopyOnWriteArraySet<>(keySource);
		Iterator<String> iterator = cwSetSource.iterator();
		while (iterator.hasNext()) {
			String next = iterator.next();
			boolean contains = keyTarget.contains(next);
			if (contains) {
				cwSetSource.remove(next);
			}
		}

		// save sourceConstraint
		for (String sourceConstraintName : cwSetSource) {
			SourceConstraint sourceConstraint = new SourceConstraint();
			sourceConstraint.setCreateTime(new Date());
			sourceConstraint.setDeleteState(1);
			sourceConstraint.setType(1);
			sourceConstraint.setSourceConstraintName(sourceConstraintName);
			ConstraintInfo constraintInfo = sourceConstraintInfosMap.get(sourceConstraintName);
			sourceConstraint.setSourceTableName(constraintInfo.getTableName());
			sourceConstraint.setVersion(cuurentMaxVersion + "");
			dBTargetConflictDao.insertSourceConstraint(sourceConstraint);
		}*/

		// save targetConstraint
		CopyOnWriteArraySet<String> cwSetTarget = new CopyOnWriteArraySet<>(keyTarget);
		Iterator<String> iterator2 = cwSetTarget.iterator();
		while (iterator2.hasNext()) {
			String next = iterator2.next();
			boolean contains = keySource.contains(next);
			if (contains) {
				cwSetTarget.remove(next);
			}
		}


		for (String targetConstraintName : cwSetTarget) {
			TargetConstraint targetConstraint = new TargetConstraint();
			targetConstraint.setCreateTime(new Date());
			targetConstraint.setType(1);
			targetConstraint.setDeleteState(1);
			targetConstraint.setTargetConstraintName(targetConstraintName);
			ConstraintInfo constraintInfo = targetConstraintInfosMap.get(targetConstraintName);
			targetConstraint.setTargetTableName(constraintInfo.getTableName());
			targetConstraint.setVersion(cuurentMaxVersion + "");
			dBTargetConflictDao.insertTargetConstraint(targetConstraint);
		}

	}

	public void deleteIndexs(List<Entry<String, Set<String>>> deleteIndexs, BigDecimal cuurentMaxVersion) {
		for (Entry<String, Set<String>> entry : deleteIndexs) {
			dBTargetConflictDao.deleteIndexs(entry.getKey(), entry.getValue(), 1, cuurentMaxVersion + "", new Date());
		}
	}

	public void deleteConstriants(List<Entry<String, Set<String>>> deleteConstriants, BigDecimal cuurentMaxVersion) {
		for (Entry<String, Set<String>> entry : deleteConstriants) {
			dBTargetConflictDao.deleteConstriants(entry.getKey(), entry.getValue(), 1, cuurentMaxVersion + "",
					new Date());
		}
	}

	public void physicalDeleteByVersion(BigDecimal currentVerson) {
		dBTargetConflictDao.physicalDeleteSourceTables(currentVerson + "");
		dBTargetConflictDao.physicalDeleteTargetTables(currentVerson + "");

		dBTargetConflictDao.physicalDeleteSourceColumns(currentVerson + "");
		dBTargetConflictDao.physicalDeleteTargetColumns(currentVerson + "");

		dBTargetConflictDao.physicalDeleteSourceIndexs(currentVerson + "");
		dBTargetConflictDao.physicalDeleteTargetIndexs(currentVerson + "");

		dBTargetConflictDao.physicalDeleteSourceConstriants(currentVerson + "");
		dBTargetConflictDao.physicalDeleteTargetConstriants(currentVerson + "");

	}

	public void logicDeleteByVersion(String currentVerson) {
		dBTargetConflictDao.logicDeleteSourceTables(currentVerson);
		dBTargetConflictDao.logicDeleteTargetTables(currentVerson);
		dBTargetConflictDao.logicDeleteSourceColumns(currentVerson);
		dBTargetConflictDao.logicDeleteTargetColumns(currentVerson);
		dBTargetConflictDao.logicDeleteSourceIndexs(currentVerson);
		dBTargetConflictDao.logicDeleteTargetIndexs(currentVerson);
		dBTargetConflictDao.logicDeleteSourceConstriants(currentVerson);
		dBTargetConflictDao.logicDeleteTargetConstriants(currentVerson);
	}

}
