package com.system.entity.record.vo;

import java.util.List;

import com.system.entity.record.TargetColumn;

public class TableTargetColumn {

	private String tableName ; 
	private List<TargetColumn> targetColumns;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<TargetColumn> getTargetColumns() {
		return targetColumns;
	}
	public void setTargetColumns(List<TargetColumn> TargetColumns) {
		this.targetColumns = TargetColumns;
	}
	
}
