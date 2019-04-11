package com.system.entity.record.vo;

import java.util.List;

import com.system.entity.record.SourceColumn;

public class TableSourceColumn {

	private String tableName ; 
	private List<SourceColumn> sourceColumns;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<SourceColumn> getSourceColumns() {
		return sourceColumns;
	}
	public void setSourceColumns(List<SourceColumn> sourceColumns) {
		this.sourceColumns = sourceColumns;
	}
	
	
}
