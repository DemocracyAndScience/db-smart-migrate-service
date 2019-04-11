package com.system.entity.record.vo;

import java.util.List;

import com.system.entity.record.TargetConstraint;

public class TableTargetConstraint {

	private String tableName ; 
	private List<TargetConstraint> targetConstraints;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<TargetConstraint> getTargetConstraints() {
		return targetConstraints;
	}
	public void setTargetConstraints(List<TargetConstraint> targetConstraints) {
		this.targetConstraints = targetConstraints;
	}
	
}
