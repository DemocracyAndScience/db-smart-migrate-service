package com.system.entity.record.dto;

import java.io.Serializable;
import java.util.Date;

public class TableDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sourceTableName ; 
	private String targetTableName ; 
	private String method ;
	private String version ;
	
	/**
	 * 
	 */
	private Integer type ; 
	private Date updateTime  ; 
	
	public String getSourceTableName() {
		return sourceTableName;
	}
	public void setSourceTableName(String sourceTableName) {
		this.sourceTableName = sourceTableName;
	}
	public String getTargetTableName() {
		return targetTableName;
	}
	public void setTargetTableName(String targetTableName) {
		this.targetTableName = targetTableName;
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
