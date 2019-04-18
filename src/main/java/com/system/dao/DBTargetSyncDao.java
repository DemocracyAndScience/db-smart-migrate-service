package com.system.dao;

import com.system.dao.lang.ForEachLang;
import com.system.entity.ColumnInfo;
import com.system.entity.ConstraintInfo;
import com.system.entity.IndexInfo;
import com.system.entity.TableInfo;
import com.system.utils.annotations.TargetMapperScan;
import org.apache.ibatis.annotations.Lang;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@TargetMapperScan
@Repository
public interface DBTargetSyncDao {

	@Select(" SELECT table_name , table_type , engine , row_format , auto_increment "
			+"    table_collation , create_options , table_comment " 
			+ " FROM  information_schema.tables " + 
			"  	WHERE  table_schema = #{tableSchema} and table_name not in (#{netns})  ")
	@Lang(ForEachLang.class)
	List<TableInfo> queryTargetTableInfos(@Param("tableSchema") String tableSchema, @Param("netns") List<String> netns);

	
	List<ColumnInfo> queryTargetColumnInfos(@Param("tableSchema") String tableSchema, @Param("netns") List<String> netns);
	
	List<IndexInfo> queryTargetIndexInfos(@Param("tableSchema") String tableSchema, @Param("netns") List<String> netns   );

	List<ConstraintInfo> queryTargetConstraintInfos(@Param("tableSchema") String tableSchema, @Param("netns") List<String> netns , @Param("constraintTypes")String [] constraintTypes ) ;

	/**
	@Select(" SELECT * FROM information_schema.PARTITIONS "
			+ "WHERE table_schema = #{tableSchema} "
			+ "	AND table_name NOT IN (#{netns}) ")
	@Lang(ForEachLang.class)
	List<PartitionInfo> queryTargetPartitionInfos(@Param("tableSchema") String tableSchema, @Param("netns") List<String>netns   ) ;

	List<RoutineInfo> queryTargetRoutineInfos(@Param("tableSchema") String tableSchema ); 
	*/
	
	@Select(" SELECT table_name , table_type , engine , row_format , auto_increment "
			+"    table_collation , create_options , table_comment " 
			+ " FROM  information_schema.tables " + 
			"  	WHERE  table_schema = #{tableSchema} and table_name  in (#{itns})  ")
	@Lang(ForEachLang.class)
	List<TableInfo> queryLookTargetTableInfos(@Param("tableSchema") String tableSchema, @Param("itns") Collection<String> collection);
	
	@Select(" SELECT  table_name , column_name , ordinal_position , column_default ,is_nullable , column_key ,  column_type , extra , column_comment "
			+ " FROM  information_schema.columns " + 
			"  	WHERE  table_schema = #{tableSchema} and table_name  in (#{itns})  ")
	@Lang(ForEachLang.class)
	List<ColumnInfo> queryLookTargetColumnInfos(@Param("tableSchema") String tableSchema, @Param("itns") Collection<String> itns);
	
	List<IndexInfo> queryLookTargetIndexInfos(@Param("tableSchema") String tableSchema, @Param("itns") Collection<String> targetTableNames   );

	List<ConstraintInfo> queryLookTargetConstraintInfos(@Param("tableSchema") String tableSchema, @Param("itns") Collection<String> itns , @Param("constraintTypes")String [] constraintTypes ) ;

}
