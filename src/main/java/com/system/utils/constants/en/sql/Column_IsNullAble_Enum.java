package com.system.utils.constants.en.sql;

public enum Column_IsNullAble_Enum {
	YES("YES", ""),
	NO("NO", " NOT NULL ");
	private String code;
	private String value;

	Column_IsNullAble_Enum(String code, String value) {
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
