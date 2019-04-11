package com.system.entity;

import java.util.Date;

public class VersionInfo {

	private String type;

	private Integer checksum;

	private String version;

	private String description;

	private String script;

	private String state;

	private Date installedOn;

	private Integer executionTime;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getChecksum() {
		return checksum;
	}

	public void setChecksum(Integer checksum) {
		this.checksum = checksum;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getInstalledOn() {
		return installedOn;
	}

	public void setInstalledOn(Date installedOn) {
		this.installedOn = installedOn;
	}

	public Integer getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(Integer executionTime) {
		this.executionTime = executionTime;
	}
	
	

}
