package com.system.entity;

import java.util.List;

/**
 * STATISTICS 表
 * 
 * @author noah
 *
 */
public class IndexInfo {

	private String tableName; /* table_name */

	private Integer nonUnique; /* 是否唯一 1 为不唯一 ，在数据库里面用key */

	private String indexName; /* 索引名称 */

	private String indexType; /* 索引类型 */

	private String indexComment; /* 描述信息 */

	private List<IndexColumnInfo> indexColumnInfos;


	public List<IndexColumnInfo> getIndexColumnInfos() {
		return indexColumnInfos;
	}

	public void setIndexColumnInfos(List<IndexColumnInfo> indexColumnInfos) {
		this.indexColumnInfos = indexColumnInfos;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Integer getNonUnique() {
		return nonUnique;
	}

	public void setNonUnique(Integer nonUnique) {
		this.nonUnique = nonUnique;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public String getIndexType() {
		return indexType;
	}

	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}

	public String getIndexComment() {
		return indexComment;
	}

	public void setIndexComment(String indexComment) {
		this.indexComment = indexComment;
	}

	@Override
	public String toString() {
		return "IndexInfo [tableName=" + tableName + ", nonUnique=" + nonUnique + ", indexName=" + indexName
				+ ", indexType=" + indexType + ", indexComment=" + indexComment + ", indexColumnInfos="
				+ indexColumnInfos + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nonUnique == null) ? 0 : nonUnique.hashCode());
		result = prime * result + ((indexColumnInfos == null) ? 0 : indexColumnInfos.hashCode());
		result = prime * result + ((indexComment == null) ? 0 : indexComment.hashCode());
		result = prime * result + ((indexName == null) ? 0 : indexName.hashCode());
		result = prime * result + ((indexType == null) ? 0 : indexType.hashCode());
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
		IndexInfo other = (IndexInfo) obj;
		if (nonUnique == null) {
			if (other.nonUnique != null)
				return false;
		} else if (!nonUnique.equals(other.nonUnique))
			return false;
		if (indexColumnInfos == null) {
			if (other.indexColumnInfos != null)
				return false;
		} else if (!indexColumnInfos.equals(other.indexColumnInfos))
			return false;
		if (indexComment == null) {
			if (other.indexComment != null)
				return false;
		} else if (!indexComment.equals(other.indexComment))
			return false;
		if (indexName == null) {
			if (other.indexName != null)
				return false;
		} else if (!indexName.equals(other.indexName))
			return false;
		if (indexType == null) {
			if (other.indexType != null)
				return false;
		} else if (!indexType.equals(other.indexType))
			return false;
		return true;
	}



}
