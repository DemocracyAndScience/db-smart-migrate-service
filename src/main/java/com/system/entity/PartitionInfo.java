package com.system.entity;
/**
 * 分区
 * @author noah
 *
 */
public class PartitionInfo {
	private String tableName                     ;
	private String partitionName                 ;
	private String subpartitionName              ;
	private Long partitionOrdinalPosition        ;
	private Long subpartitionOrdinalPosition     ;
	private String partitionMethod               ;
	private String subpartitionMethod            ;
	private String partitionExpression           ;
	private String subpartitionExpression        ;
	private String partitionDescription          ;
	private String partitionComment              ;
	private String nodegroup                     ;
	private String tablespaceName                ;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getPartitionName() {
		return partitionName;
	}
	public void setPartitionName(String partitionName) {
		this.partitionName = partitionName;
	}
	public String getSubpartitionName() {
		return subpartitionName;
	}
	public void setSubpartitionName(String subpartitionName) {
		this.subpartitionName = subpartitionName;
	}
	public Long getPartitionOrdinalPosition() {
		return partitionOrdinalPosition;
	}
	public void setPartitionOrdinalPosition(Long partitionOrdinalPosition) {
		this.partitionOrdinalPosition = partitionOrdinalPosition;
	}
	public Long getSubpartitionOrdinalPosition() {
		return subpartitionOrdinalPosition;
	}
	public void setSubpartitionOrdinalPosition(Long subpartitionOrdinalPosition) {
		this.subpartitionOrdinalPosition = subpartitionOrdinalPosition;
	}
	public String getPartitionMethod() {
		return partitionMethod;
	}
	public void setPartitionMethod(String partitionMethod) {
		this.partitionMethod = partitionMethod;
	}
	public String getSubpartitionMethod() {
		return subpartitionMethod;
	}
	public void setSubpartitionMethod(String subpartitionMethod) {
		this.subpartitionMethod = subpartitionMethod;
	}
	public String getPartitionExpression() {
		return partitionExpression;
	}
	public void setPartitionExpression(String partitionExpression) {
		this.partitionExpression = partitionExpression;
	}
	public String getSubpartitionExpression() {
		return subpartitionExpression;
	}
	public void setSubpartitionExpression(String subpartitionExpression) {
		this.subpartitionExpression = subpartitionExpression;
	}
	public String getPartitionDescription() {
		return partitionDescription;
	}
	public void setPartitionDescription(String partitionDescription) {
		this.partitionDescription = partitionDescription;
	}
	public String getPartitionComment() {
		return partitionComment;
	}
	public void setPartitionComment(String partitionComment) {
		this.partitionComment = partitionComment;
	}
	public String getNodegroup() {
		return nodegroup;
	}
	public void setNodegroup(String nodegroup) {
		this.nodegroup = nodegroup;
	}
	public String getTablespaceName() {
		return tablespaceName;
	}
	public void setTablespaceName(String tablespaceName) {
		this.tablespaceName = tablespaceName;
	}
	@Override
	public String toString() {
		return "PartitionInfo [tableName=" + tableName + ", partitionName=" + partitionName + ", subpartitionName="
				+ subpartitionName + ", partitionOrdinalPosition=" + partitionOrdinalPosition
				+ ", subpartitionOrdinalPosition=" + subpartitionOrdinalPosition + ", partitionMethod="
				+ partitionMethod + ", subpartitionMethod=" + subpartitionMethod + ", partitionExpression="
				+ partitionExpression + ", subpartitionExpression=" + subpartitionExpression + ", partitionDescription="
				+ partitionDescription + ", partitionComment=" + partitionComment + ", nodegroup=" + nodegroup
				+ ", tablespaceName=" + tablespaceName + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodegroup == null) ? 0 : nodegroup.hashCode());
		result = prime * result + ((partitionComment == null) ? 0 : partitionComment.hashCode());
		result = prime * result + ((partitionDescription == null) ? 0 : partitionDescription.hashCode());
		result = prime * result + ((partitionExpression == null) ? 0 : partitionExpression.hashCode());
		result = prime * result + ((partitionMethod == null) ? 0 : partitionMethod.hashCode());
		result = prime * result + ((partitionName == null) ? 0 : partitionName.hashCode());
		result = prime * result + ((partitionOrdinalPosition == null) ? 0 : partitionOrdinalPosition.hashCode());
		result = prime * result + ((subpartitionExpression == null) ? 0 : subpartitionExpression.hashCode());
		result = prime * result + ((subpartitionMethod == null) ? 0 : subpartitionMethod.hashCode());
		result = prime * result + ((subpartitionName == null) ? 0 : subpartitionName.hashCode());
		result = prime * result + ((subpartitionOrdinalPosition == null) ? 0 : subpartitionOrdinalPosition.hashCode());
		result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
		result = prime * result + ((tablespaceName == null) ? 0 : tablespaceName.hashCode());
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
		PartitionInfo other = (PartitionInfo) obj;
		if (nodegroup == null) {
			if (other.nodegroup != null)
				return false;
		} else if (!nodegroup.equals(other.nodegroup))
			return false;
		if (partitionComment == null) {
			if (other.partitionComment != null)
				return false;
		} else if (!partitionComment.equals(other.partitionComment))
			return false;
		if (partitionDescription == null) {
			if (other.partitionDescription != null)
				return false;
		} else if (!partitionDescription.equals(other.partitionDescription))
			return false;
		if (partitionExpression == null) {
			if (other.partitionExpression != null)
				return false;
		} else if (!partitionExpression.equals(other.partitionExpression))
			return false;
		if (partitionMethod == null) {
			if (other.partitionMethod != null)
				return false;
		} else if (!partitionMethod.equals(other.partitionMethod))
			return false;
		if (partitionName == null) {
			if (other.partitionName != null)
				return false;
		} else if (!partitionName.equals(other.partitionName))
			return false;
		if (partitionOrdinalPosition == null) {
			if (other.partitionOrdinalPosition != null)
				return false;
		} else if (!partitionOrdinalPosition.equals(other.partitionOrdinalPosition))
			return false;
		if (subpartitionExpression == null) {
			if (other.subpartitionExpression != null)
				return false;
		} else if (!subpartitionExpression.equals(other.subpartitionExpression))
			return false;
		if (subpartitionMethod == null) {
			if (other.subpartitionMethod != null)
				return false;
		} else if (!subpartitionMethod.equals(other.subpartitionMethod))
			return false;
		if (subpartitionName == null) {
			if (other.subpartitionName != null)
				return false;
		} else if (!subpartitionName.equals(other.subpartitionName))
			return false;
		if (subpartitionOrdinalPosition == null) {
			if (other.subpartitionOrdinalPosition != null)
				return false;
		} else if (!subpartitionOrdinalPosition.equals(other.subpartitionOrdinalPosition))
			return false;
		if (tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!tableName.equals(other.tableName))
			return false;
		if (tablespaceName == null) {
			if (other.tablespaceName != null)
				return false;
		} else if (!tablespaceName.equals(other.tablespaceName))
			return false;
		return true;
	}

	  
	  
	  
	  
}
