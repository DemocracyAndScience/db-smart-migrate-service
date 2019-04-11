package com.system.entity.record.dto;

import java.io.Serializable;
import java.util.Date;

public class IndexDto implements Serializable{

	private static final long serialVersionUID = 1L;
	private String tableName ;  
	private String sourceIndexName ; 
	private String targetIndexName ; 
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
	public String getSourceIndexName() {
		return sourceIndexName;
	}
	public void setSourceIndexName(String sourceIndexName) {
		this.sourceIndexName = sourceIndexName;
	}
	public String getTargetIndexName() {
		return targetIndexName;
	}
	public void setTargetIndexName(String targetIndexName) {
		this.targetIndexName = targetIndexName;
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
