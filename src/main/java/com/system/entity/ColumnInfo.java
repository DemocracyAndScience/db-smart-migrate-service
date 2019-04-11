package com.system.entity;

public class ColumnInfo {

	private String tableName;
	
	private String columnName;

	private Long ordinalPosition; /* 列的顺序 */

	private String columnDefault; /* 列的默认值 */

	private String isNullable; /* 是否为空 */

	private String columnKey ; /* 主键 */

	private String columnType; /* 列类型 带（64） */


	private String extra; /* 自增长 */

	private String columnComment; /* 备注 */

	public String getColumnKey() {
		return columnKey;
	}

	public void setColumnKey(String columnKey) {
		this.columnKey = columnKey;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Long getOrdinalPosition() {
		return ordinalPosition;
	}

	public void setOrdinalPosition(Long ordinalPosition) {
		this.ordinalPosition = ordinalPosition;
	}

	public String getColumnDefault() {
		return columnDefault;
	}

	public void setColumnDefault(String columnDefault) {
		this.columnDefault = columnDefault;
	}

	public String getIsNullable() {
		return isNullable;
	}

	public void setIsNullable(String isNullable) {
		this.isNullable = isNullable;
	}



	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getColumnComment() {
		return columnComment;
	}

	public void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((columnComment == null) ? 0 : columnComment.hashCode());
		result = prime * result + ((columnDefault == null) ? 0 : columnDefault.hashCode());
		result = prime * result + ((columnName == null) ? 0 : columnName.hashCode());
		result = prime * result + ((columnType == null) ? 0 : columnType.hashCode());
		result = prime * result + ((extra == null) ? 0 : extra.hashCode());
		result = prime * result + ((isNullable == null) ? 0 : isNullable.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ColumnInfo other = (ColumnInfo) obj;
		if (columnComment == null) {
			if (other.columnComment != null)
				return false;
		} else if (!columnComment.equals(other.columnComment))
			return false;
		if (columnDefault == null) {
			if (other.columnDefault != null)
				return false;
		} else if (!columnDefault.equals(other.columnDefault))
			return false;
		if (columnName == null) {
			if (other.columnName != null)
				return false;
		} else if (!columnName.equals(other.columnName))
			return false;
		if (columnType == null) {
			if (other.columnType != null)
				return false;
		} else if (!columnType.equals(other.columnType))
			return false;
		if (extra == null) {
			if (other.extra != null)
				return false;
		} else if (!extra.equals(other.extra))
			return false;
		if (isNullable == null) {
			if (other.isNullable != null)
				return false;
		} else if (!isNullable.equals(other.isNullable))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ColumnInfo [tableName=" + tableName + ", columnName=" + columnName + ", ordinalPosition="
				+ ordinalPosition + ", columnDefault=" + columnDefault + ", isNullable=" + isNullable + ", columnKey="
				+ columnKey + ", columnType=" + columnType + ", extra=" + extra + ", columnComment=" + columnComment
				+ "]";
	}

	
}
