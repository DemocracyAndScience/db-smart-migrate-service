package com.system.entity;

import java.util.List;

/**
 * 存储过程函数
 * @author noah
 * Routines
 * Parameters 表
 */
public class RoutineInfo {
	private String routineName; 		/*函数名称*/
	private String routineType; 		/*函数还是 存储过程*/
	private String routineDefinition;   /*函数定义信息*/
	private String sqlMode;				/*暂时不知道该怎么用*/
	private String definer;				/*定义信息*/
	private List<ParameterInfo> parameterInfos ; /*参数信息*/
	
	
	public String getRoutineName() {
		return routineName;
	}
	public void setRoutineName(String routineName) {
		this.routineName = routineName;
	}
	public String getRoutineType() {
		return routineType;
	}
	public void setRoutineType(String routineType) {
		this.routineType = routineType;
	}
	public String getRoutineDefinition() {
		return routineDefinition;
	}
	public void setRoutineDefinition(String routineDefinition) {
		this.routineDefinition = routineDefinition;
	}
	public String getSqlMode() {
		return sqlMode;
	}
	public void setSqlMode(String sqlMode) {
		this.sqlMode = sqlMode;
	}
	public String getDefiner() {
		return definer;
	}
	public void setDefiner(String definer) {
		this.definer = definer;
	}
	public List<ParameterInfo> getParameterInfos() {
		return parameterInfos;
	}
	public void setParameterInfos(List<ParameterInfo> parameterInfos) {
		this.parameterInfos = parameterInfos;
	}

	

	
}
