package com.system.entity.record.dto;

import java.io.Serializable;
import java.util.Date;

public class ConstraintDto implements Serializable{

	private static final long serialVersionUID = 1L;
	private String tableName ;  
	private String sourceConstraintName ; 
	private String targetConstraintName ; 
	private String method ;
	private String version ;
	
	/**
	 * 
	 */
	private Integer type ; 
	private Date updateTime  ; 
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getSourceConstraintName() {
		return sourceConstraintName;
	}
	public void setSourceConstraintName(String sourceConstraintName) {
		this.sourceConstraintName = sourceConstraintName;
	}
	public String getTargetConstraintName() {
		return targetConstraintName;
	}
	public void setTargetConstraintName(String targetConstraintName) {
		this.targetConstraintName = targetConstraintName;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	
}
