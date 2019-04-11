package com.system.entity.record.dto;

import java.io.Serializable;
import java.util.Date;

public class ColumnDto  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tableName ;  
	private String sourceColumnName ; 
	private String targetColumnName ; 
	private String method ;
	private String version ;
	
	/**
	 * 
	 */
	private Integer type ; 
	private Date updateTime  ; 
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getSourceColumnName() {
		return sourceColumnName;
	}
	public void setSourceColumnName(String sourceColumnName) {
		this.sourceColumnName = sourceColumnName;
	}
	public String getTargetColumnName() {
		return targetColumnName;
	}
	public void setTargetColumnName(String targetColumnName) {
		this.targetColumnName = targetColumnName;
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
	
	
	
}
