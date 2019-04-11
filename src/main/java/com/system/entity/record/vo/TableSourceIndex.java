package com.system.entity.record.vo;

import java.util.List;

import com.system.entity.record.SourceIndex;

public class TableSourceIndex {

	private String tableName ; 
	private List<SourceIndex> sourceIndexs;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<SourceIndex> getSourceIndexs() {
		return sourceIndexs;
	}
	public void setSourceIndexs(List<SourceIndex> sourceIndexs) {
		this.sourceIndexs = sourceIndexs;
	}
	
}
