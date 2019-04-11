package com.system.entity;
/**
 * 约束列信息
 * @author noah
 *
 */
public class ConstraintColumnInfo  {

	 private String  columnName 				;
	 private Long  ordinalPosition 			; // 1,2 
	 private String  referencedColumnName 	;	 
	 private String  updateRule  	            ; //casecade 
	 private String  deleteRule 	            ; //casecade 
	 
	 
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
	public String getReferencedColumnName() {
		return referencedColumnName;
	}
	public void setReferencedColumnName(String referencedColumnName) {
		this.referencedColumnName = referencedColumnName;
	}
	public String getUpdateRule() {
		return updateRule;
	}
	public void setUpdateRule(String updateRule) {
		this.updateRule = updateRule;
	}
	public String getDeleteRule() {
		return deleteRule;
	}
	public void setDeleteRule(String deleteRule) {
		this.deleteRule = deleteRule;
	}
	@Override
	public String toString() {
		return "ConstaintColumnInfo [columnName=" + columnName + ", ordinalPosition=" + ordinalPosition
				+ ", referencedColumnName=" + referencedColumnName + ", updateRule=" + updateRule + ", deleteRule="
				+ deleteRule + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((columnName == null) ? 0 : columnName.hashCode());
		result = prime * result + ((deleteRule == null) ? 0 : deleteRule.hashCode());
		result = prime * result + ((ordinalPosition == null) ? 0 : ordinalPosition.hashCode());
		result = prime * result + ((referencedColumnName == null) ? 0 : referencedColumnName.hashCode());
		result = prime * result + ((updateRule == null) ? 0 : updateRule.hashCode());
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
		ConstraintColumnInfo other = (ConstraintColumnInfo) obj;
		if (columnName == null) {
			if (other.columnName != null)
				return false;
		} else if (!columnName.equals(other.columnName))
			return false;
		if (deleteRule == null) {
			if (other.deleteRule != null)
				return false;
		} else if (!deleteRule.equals(other.deleteRule))
			return false;
		if (ordinalPosition == null) {
			if (other.ordinalPosition != null)
				return false;
		} else if (!ordinalPosition.equals(other.ordinalPosition))
			return false;
		if (referencedColumnName == null) {
			if (other.referencedColumnName != null)
				return false;
		} else if (!referencedColumnName.equals(other.referencedColumnName))
			return false;
		if (updateRule == null) {
			if (other.updateRule != null)
				return false;
		} else if (!updateRule.equals(other.updateRule))
			return false;
		return true;
	}
	 
	 
	 
}
