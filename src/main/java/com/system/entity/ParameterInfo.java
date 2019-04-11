package com.system.entity;
/**
 * 参数
 * @author noah
 *
 */
public class ParameterInfo {

	private Integer ordinalPosition; 		/*参数位置*/
	private String parameterMode; 			/*参数IN OUT*/
	private String parameterName;			/*参数名称*/
	private String dtdIdentifier;			/*参数类型  int(50) */
	
	public Integer getOrdinalPosition() {
		return ordinalPosition;
	}
	public void setOrdinalPosition(Integer ordinalPosition) {
		this.ordinalPosition = ordinalPosition;
	}
	public String getParameterMode() {
		return parameterMode;
	}
	public void setParameterMode(String parameterMode) {
		this.parameterMode = parameterMode;
	}
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public String getDtdIdentifier() {
		return dtdIdentifier;
	}
	public void setDtdIdentifier(String dtdIdentifier) {
		this.dtdIdentifier = dtdIdentifier;
	}

	
}
