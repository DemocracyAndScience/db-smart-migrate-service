package com.system.entity.record.vo;

import java.util.List;

import com.system.entity.record.SourceConstraint;

public class TableSourceConstraint {

	private String tableName ; 
	private List<SourceConstraint> sourceConstraints;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<SourceConstraint> getSourceConstraints() {
		return sourceConstraints;
	}
	public void setSourceConstraints(List<SourceConstraint> sourceConstraints) {
		this.sourceConstraints = sourceConstraints;
	}
	
}
