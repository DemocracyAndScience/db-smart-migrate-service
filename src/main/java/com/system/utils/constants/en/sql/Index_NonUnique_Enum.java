package com.system.utils.constants.en.sql;

public enum Index_NonUnique_Enum {

	NonUnique(1, ""),
	Unique(0, " UNIQUE ");
	private Integer code;
	private String value;

	Index_NonUnique_Enum(Integer code, String value) {
		this.code = code;
		this.value = value;
	}


	public Integer getCode() {
		return code;
	}


	public void setCode(Integer code) {
		this.code = code;
	}


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
