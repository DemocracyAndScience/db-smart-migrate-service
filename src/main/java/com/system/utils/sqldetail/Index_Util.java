package com.system.utils.sqldetail;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.lang.StringUtils;

import com.system.entity.IndexColumnInfo;
import com.system.entity.IndexInfo;
import com.system.entity.record.dto.IndexDto;

/**
 * 索引 SQL 片段生成
 * 
 * @author noah
 *
 */
public class Index_Util {

	public static Predicate<IndexDto> test(String method) {
		Predicate<IndexDto> predicateIndex = new Predicate<IndexDto>() {
			@Override
			public boolean test(IndexDto t) {
				return t.getMethod().equals(method);
			}
		};
		return predicateIndex;  
	}
	
	public static  Predicate<IndexInfo> equalsTableName(String tableName){
		Predicate<IndexInfo> predicateIndex = new Predicate<IndexInfo>() {
			@Override
			public boolean test(IndexInfo t) {
				return t.getTableName().equals(tableName);
			}
		};
		return predicateIndex ;
	}
	
	
	
	public static final Comparator<IndexColumnInfo> c = new Comparator<IndexColumnInfo>() {
		@Override
		public int compare(IndexColumnInfo o1, IndexColumnInfo o2) {
			Integer seqInIndex = o1.getSeqInIndex();
			Integer seqInIndex2 = o2.getSeqInIndex();
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
	 * 获取备注
	 * @param commentInfo
	 * @return
	 */
	public static String getComment(String commentInfo) {
		if(StringUtils.isBlank(commentInfo)) {
			return "" ;
		}
		return "COMMENT '"+commentInfo+"'" ; 
		
	}

	/**
	 * 获取 Index 多列的 逗号拼接字符串
	 * 
	 * @param indexColumnInfos
	 * @return
	 */
	public static String getStringByColumns(List<IndexColumnInfo> indexColumnInfos) {
		Collections.sort(indexColumnInfos, c);
		String sql = "";
		for (IndexColumnInfo indexColumnInfo2 : indexColumnInfos) {
			sql += " , " + indexColumnInfo2.getColumnName();
			if (indexColumnInfo2.getSubPart() != null) {
				sql += "(" + indexColumnInfo2.getSubPart() + ")";
			}
		}
		sql = sql.replaceFirst(",", "");
		return sql;
	}

}
