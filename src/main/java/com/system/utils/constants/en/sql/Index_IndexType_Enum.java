package com.system.utils.constants.en.sql;

public enum Index_IndexType_Enum {

	BTREE("BTREE", ""),
	FULLTEXT("FULLTEXT", "FULLTEXT"),
	SPATIAL("SPATIAL", "SPATIAL");
	private String code;
	private String value;

	Index_IndexType_Enum(String code, String value) {
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
