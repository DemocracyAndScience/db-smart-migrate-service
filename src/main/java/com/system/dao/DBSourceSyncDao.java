package com.system.dao;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Lang;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.system.dao.lang.ForEachLang;
import com.system.entity.ColumnInfo;
import com.system.entity.ConstraintInfo;
import com.system.entity.IndexInfo;
import com.system.entity.TableInfo;
import com.system.utils.annotations.SourceMapperScan;
import org.springframework.stereotype.Repository;

@SourceMapperScan
@Repository
public interface DBSourceSyncDao {

	/*
	 * @param tableSchema
	 * @param netns
	 * @return
	 */
	@Select(" SELECT table_name , table_type , engine , row_format , auto_increment "
			+"    table_collation , create_options , table_comment " 
			+ "FROM  information_schema.tables " + 
			"  WHERE  table_schema = #{tableSchema} and table_name not in (#{netns})  ")
	@Lang(ForEachLang.class)
	List<TableInfo> querySourceTableInfos(@Param("tableSchema") String tableSchema, @Param("netns") List<String> netns);
	/**
	 * @param tableSchema
	 * @param netns
	 * @return
	 */
	List<ColumnInfo> querySourceColumnInfos(@Param("tableSchema") String tableSchema, @Param("netns") List<String> netns);

	List<IndexInfo> querySourceIndexInfos(@Param("tableSchema") String tableSchema, @Param("netns") List<String> netns  ) ;

	List<ConstraintInfo> querySourceConstraintInfos(@Param("tableSchema") String tableSchema, @Param("netns") List<String> netns , @Param("constraintTypes")String [] constraintTypes ) ;
/*	 
	@Select(" SELECT * FROM information_schema.PARTITIONS "
			+ "WHERE table_schema = #{tableSchema} "
			+ "	AND table_name NOT IN (#{netns}) ")
	@Lang(ForEachLang.class)
	List<PartitionInfo> querySourcePartitionInfos(@Param("tableSchema") String tableSchema, @Param("netns") List<String> netns   ) ;

	List<RoutineInfo> querySourceRoutineInfos(@Param("tableSchema") String tableSchema ); */

	/*
	 * @param tableSchema
	 * @param netns
	 * @return
	 */
	@Select(" SELECT table_name , table_type , engine , row_format , auto_increment "
			+"    table_collation , create_options , table_comment " 
			+ "FROM  information_schema.tables " + 
			"  WHERE  table_schema = #{tableSchema} and table_name  in (#{itns})  ")
	@Lang(ForEachLang.class)
	List<TableInfo> queryLookSourceTableInfos(@Param("tableSchema") String tableSchema, @Param("itns") Collection<String> netns);
	
	
	@Select(" SELECT table_name , column_name , ordinal_position , column_default ,is_nullable ,  column_key , column_type , extra , column_comment "
			+ "     FROM  information_schema.columns " + 
			"  	WHERE  table_schema = #{tableSchema} and table_name  in (#{itns})  ")
	@Lang(ForEachLang.class)
	List<ColumnInfo> queryLookSourceColumnInfos(@Param("tableSchema") String tableSchema, @Param("itns") Collection<String> itns);

	List<IndexInfo> queryLookSourceIndexInfos(@Param("tableSchema") String tableSchema, @Param("itns") Collection<String> itns  ) ;

	List<ConstraintInfo> queryLookSourceConstraintInfos(@Param("tableSchema") String tableSchema, @Param("itns") Collection<String> itns , @Param("constraintTypes")String [] constraintTypes ) ;

	
	
}
