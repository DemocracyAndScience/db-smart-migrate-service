package com.system.utils.constants;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DBConstants {
	public static  String NOT_EXITIS_TABLE_NAME_1   = "schema_version";
	public static final String NOT_EXITIS_TABLE_NAME_2 = "flyway_schema_history";
	public static final String NOT_EXITIS_TABLE_NAME_3 = "version_management_source_column";
	public static final String NOT_EXITIS_TABLE_NAME_4 = "version_management_target_column";
	public static final String NOT_EXITIS_TABLE_NAME_5 = "version_management_source_constraint";
	public static final String NOT_EXITIS_TABLE_NAME_6 = "version_management_target_constraint";
	public static final String NOT_EXITIS_TABLE_NAME_7 = "version_management_source_index";
	public static final String NOT_EXITIS_TABLE_NAME_8 = "version_management_target_index";
	public static final String NOT_EXITIS_TABLE_NAME_9 = "version_management_source_table";
	public static final String NOT_EXITIS_TABLE_NAME_10 = "version_management_target_table";
	
	public static List<String> getAllTables() {
		List<String> arrayList = new ArrayList<>();
		Class<DBConstants> clazz = DBConstants.class;
		Field[] declaredFields = clazz.getDeclaredFields();
		for (Field field : declaredFields) {
			try {
				String string = field.get(null).toString();
				arrayList.add(string);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return arrayList ;
		
	}
	public static void main(String[] args) {
		System.out.println(getAllTables());
	}
}
