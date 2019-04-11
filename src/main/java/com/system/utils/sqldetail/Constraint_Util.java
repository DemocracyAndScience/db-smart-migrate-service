package com.system.utils.sqldetail;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.system.entity.ConstraintColumnInfo;
import com.system.entity.ConstraintInfo;
import com.system.entity.record.dto.ConstraintDto;

public class Constraint_Util {

	public static Predicate<ConstraintDto> test(String method) {
		Predicate<ConstraintDto> predicateConstraint = new Predicate<ConstraintDto>() {
			@Override
			public boolean test(ConstraintDto t) {
				return t.getMethod().equals(method);
			}
		};
		return predicateConstraint ;  
	}
	
	
	public static  Predicate<ConstraintInfo> equalsTableName(String tableName){
		Predicate<ConstraintInfo> predicateConstraint = new Predicate<ConstraintInfo>() {
			@Override
			public boolean test(ConstraintInfo t) {
				return t.getTableName().equals(tableName);
			}
		};
		return predicateConstraint ;
	}
	
	public static final Comparator<ConstraintColumnInfo> c = new Comparator<ConstraintColumnInfo>() {
		@Override
		public int compare(ConstraintColumnInfo o1, ConstraintColumnInfo o2) {
			Long seqInIndex = o1.getOrdinalPosition();
			Long seqInIndex2 = o2.getOrdinalPosition();
			if (seqInIndex < seqInIndex2) {
				return -1;
			} else if (seqInIndex.equals(seqInIndex2)) {
				return 0;
			} else {
				return 1;
			}
		}

	};

	/**
	 * 获取 当前表 单多列的 逗号拼接字符串
	 * 
	 * @param indexColumnInfos
	 * @return
	 */
	public static String getStringByCurrentColumns(List<ConstraintColumnInfo> constraintColumnInfos) {
		Collections.sort(constraintColumnInfos ,Constraint_Util.c);
		String string = constraintColumnInfos.stream().map(constraintColumnInfo -> {
			return constraintColumnInfo.getColumnName();
		}).collect(Collectors.toList()).toString();
				
		 return string.replaceAll("\\[", "").replaceAll("\\]", "");
	}
	/**
	 * 获取 外键表 单多列的 逗号拼接字符串
	 * 
	 * @param indexColumnInfos
	 * @return
	 */
	public static String getStringByReferenceColumns(List<ConstraintColumnInfo> constraintColumnInfos) {
		Collections.sort(constraintColumnInfos, c);
		return constraintColumnInfos.stream().map(constraintColumnInfo -> {
			return constraintColumnInfo.getReferencedColumnName();
		}).collect(Collectors.toList()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
	}
}
