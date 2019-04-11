package com.system.service;

import java.util.List;

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

public interface DBSyncService {

	int syncStructure();
	List<SourceTable> getSourceTables(Integer type);
	List<TargetTable> getTargetTables(Integer type);
	List<TableSourceColumn> getSourceColumns(Integer type);
	List<TableTargetColumn>  getTargetColumns(Integer type, String tableName);
	List<TableSourceIndex > getSourceIndexs(Integer type);
	List<TableTargetIndex > getTargetIndexs(Integer type, String tableName);
	List<TableSourceConstraint> getSourceConstraints(Integer type);
	List<TableTargetConstraint> getTargetConstraints(Integer type, String tableName);
	int readyTables(List<TableDto> tableDto);
	StringBuilder lookReadyTables(List<TableDto> tableDto , StringBuilder sqlSb );
	int changeColumnsIndexsAndConstraintsTableName( List<TableDto> tableDtos);
	int readyColumns(List<ColumnDto> columnDtos);
	StringBuilder lookReadyColumns(List<ColumnDto> tableDto , StringBuilder sqlSb);
	int readyIndexs(List<IndexDto> indexDtos);
	int readyConstraints(List<ConstraintDto> constraintDtos);
	
}
