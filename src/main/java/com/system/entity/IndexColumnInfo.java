package com.system.entity;

public class IndexColumnInfo {

	private Integer seqInIndex; /* 联合索引的第几顺序 */

	private String columnName;
	
	private Long subPart ; 

	public Integer getSeqInIndex() {
		return seqInIndex;
	}

	public void setSeqInIndex(Integer seqInIndex) {
		this.seqInIndex = seqInIndex;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}


	public Long getSubPart() {
		return subPart;
	}

	public void setSubPart(Long subPart) {
		this.subPart = subPart;
	}

	@Override
	public String toString() {
		return "IndexColumnInfo [seqInIndex=" + seqInIndex + ", columnName=" + columnName + ", subPart=" + subPart
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((columnName == null) ? 0 : columnName.hashCode());
		result = prime * result + ((seqInIndex == null) ? 0 : seqInIndex.hashCode());
		result = prime * result + ((subPart == null) ? 0 : subPart.hashCode());
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
		IndexColumnInfo other = (IndexColumnInfo) obj;
		if (columnName == null) {
			if (other.columnName != null)
				return false;
		} else if (!columnName.equals(other.columnName))
			return false;
		if (seqInIndex == null) {
			if (other.seqInIndex != null)
				return false;
		} else if (!seqInIndex.equals(other.seqInIndex))
			return false;
		if (subPart == null) {
			if (other.subPart != null)
				return false;
		} else if (!subPart.equals(other.subPart))
			return false;
		return true;
	}

	
	
	
}
