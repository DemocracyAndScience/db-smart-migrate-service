package com.system.entity;

import java.util.List;

/**
 * 表信息
 * 
 * @author noah
 */
public class TableInfo {

	private String tableName;
	private String tableType;
	private String engine; /* 引擎 */
	private String rowFormat; /* 行 */
	private Long autoIncrement;
	private String tableCollation; /* 表编码 */
	private String createOptions; /* 创建选项 */
	private String tableComment;

	private List<ColumnInfo> columnInfos;
	private List<IndexInfo> indexInfos;
	private List<ConstraintInfo> constraintInfos;
	private List<PartitionInfo> patitionInfos;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	public String getEngine() {
		return engine;
	}

	public void setEngine(String engine) {
		this.engine = engine;
	}

	public String getRowFormat() {
		return rowFormat;
	}

	public void setRowFormat(String rowFormat) {
		this.rowFormat = rowFormat;
	}

	public String getTableCollation() {
		return tableCollation;
	}

	public void setTableCollation(String tableCollation) {
		this.tableCollation = tableCollation;
	}

	public String getCreateOptions() {
		return createOptions;
	}

	public void setCreateOptions(String createOptions) {
		this.createOptions = createOptions;
	}

	public String getTableComment() {
		return tableComment;
	}

	public void setTableComment(String tableComment) {
		this.tableComment = tableComment;
	}

	public List<ColumnInfo> getColumnInfos() {
		return columnInfos;
	}

	public void setColumnInfos(List<ColumnInfo> columnInfos) {
		this.columnInfos = columnInfos;
	}

	public List<IndexInfo> getIndexInfos() {
		return indexInfos;
	}

	public void setIndexInfos(List<IndexInfo> indexInfos) {
		this.indexInfos = indexInfos;
	}

	public List<ConstraintInfo> getConstraintInfos() {
		return constraintInfos;
	}

	public void setConstraintInfos(List<ConstraintInfo> constraintInfos) {
		this.constraintInfos = constraintInfos;
	}

	public List<PartitionInfo> getPatitionInfos() {
		return patitionInfos;
	}

	public void setPatitionInfos(List<PartitionInfo> patitionInfos) {
		this.patitionInfos = patitionInfos;
	}

	public Long getAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(Long autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((autoIncrement == null) ? 0 : autoIncrement.hashCode());
		result = prime * result + ((createOptions == null) ? 0 : createOptions.hashCode());
		result = prime * result + ((engine == null) ? 0 : engine.hashCode());
		result = prime * result + ((tableComment == null) ? 0 : tableComment.hashCode());
		result = prime * result + ((tableType == null) ? 0 : tableType.hashCode());
		return result;
	}

	/**
	 *
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TableInfo other = (TableInfo) obj;
		if (autoIncrement == null) {
			if (other.autoIncrement != null)
				return false;
		} else if (!autoIncrement.equals(other.autoIncrement))
			return false;
		if (createOptions == null) {
			if (other.createOptions != null)
				return false;
		} else if (!createOptions.equals(other.createOptions))
			return false;
		if (engine == null) {
			if (other.engine != null)
				return false;
		} else if (!engine.equals(other.engine))
			return false;
		if (tableComment == null) {
			if (other.tableComment != null)
				return false;
		} else if (!tableComment.equals(other.tableComment))
			return false;
		return true;
	}

}
