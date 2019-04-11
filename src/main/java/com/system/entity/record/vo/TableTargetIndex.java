package com.system.entity.record.vo;

import java.util.List;

import com.system.entity.record.TargetIndex;

public class TableTargetIndex {

	private String tableName ; 
	private List<TargetIndex> targetIndexs;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<TargetIndex> getTargetIndexs() {
		return targetIndexs;
	}
	public void setTargetIndexs(List<TargetIndex> TargetIndexs) {
		this.targetIndexs = TargetIndexs;
	}
}
