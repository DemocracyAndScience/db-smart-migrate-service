package com.system.entity;

import java.util.List;
import java.util.TreeSet;

/**
 * 主键 外间
 * @author noah
 *
 */
public class ConstraintInfo {
	private String constraintType; // PRI FOR
	private String constraintName;
	private String tableName;
	private String referencedTableSchema; //
	private String referencedTableName; //

	private List<ConstraintColumnInfo> constraintColumnInfos;

	public String getConstraintType() {
		return constraintType;
	}

	public void setConstraintType(String constraintType) {
		this.constraintType = constraintType;
	}

	public String getConstraintName() {
		return constraintName;
	}

	public void setConstraintName(String constraintName) {
		this.constraintName = constraintName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


	public String getReferencedTableSchema() {
		return referencedTableSchema;
	}

	public void setReferencedTableSchema(String referencedTableSchema) {
		this.referencedTableSchema = referencedTableSchema;
	}

	public String getReferencedTableName() {
		return referencedTableName;
	}

	public void setReferencedTableName(String referencedTableName) {
		this.referencedTableName = referencedTableName;
	}

	public List<ConstraintColumnInfo> getConstraintColumnInfos() {
		return constraintColumnInfos;
	}

	public void setConstraintColumnInfos(List<ConstraintColumnInfo> constraintColumnInfos) {
		this.constraintColumnInfos = constraintColumnInfos;
	}

	@Override
	public String toString() {
		return "ConstraintInfo [constraintType=" + constraintType + ", constraintName=" + constraintName
				+ ", tableName=" + tableName + ", referencedTableSchema=" + referencedTableSchema
				+ ", referencedTableName=" + referencedTableName + ", constraintColumnInfos=" + constraintColumnInfos
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((constraintColumnInfos == null) ? 0 : constraintColumnInfos.hashCode());
		result = prime * result + ((constraintName == null) ? 0 : constraintName.hashCode());
		result = prime * result + ((constraintType == null) ? 0 : constraintType.hashCode());
		result = prime * result + ((referencedTableName == null) ? 0 : referencedTableName.hashCode());
		result = prime * result + ((referencedTableSchema == null) ? 0 : referencedTableSchema.hashCode());
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
		ConstraintInfo other = (ConstraintInfo) obj;
		if (constraintColumnInfos == null) {
			if (other.constraintColumnInfos != null)
				return false;
		} else if (!constraintColumnInfos.equals(other.constraintColumnInfos))
			return false;
		if (constraintName == null) {
			if (other.constraintName != null)
				return false;
		} else if (!constraintName.equals(other.constraintName))
			return false;
		if (constraintType == null) {
			if (other.constraintType != null)
				return false;
		} else if (!constraintType.equals(other.constraintType))
			return false;
		if (referencedTableName == null) {
			if (other.referencedTableName != null)
				return false;
		} else if (!referencedTableName.equals(other.referencedTableName))
			return false;
		if (referencedTableSchema == null) {
			if (other.referencedTableSchema != null)
				return false;
		} else if (!referencedTableSchema.equals(other.referencedTableSchema))
			return false;
		return true;
	}


}
