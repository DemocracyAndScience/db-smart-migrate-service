package com.system.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Lang;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.system.dao.lang.ForEachLang;
import com.system.entity.record.SourceColumn;
import com.system.entity.record.SourceConstraint;
import com.system.entity.record.SourceIndex;
import com.system.entity.record.SourceTable;
import com.system.entity.record.TargetColumn;
import com.system.entity.record.TargetConstraint;
import com.system.entity.record.TargetIndex;
import com.system.entity.record.TargetTable;
import com.system.entity.record.dto.ColumnDto;
import com.system.entity.record.dto.ConstraintDto;
import com.system.entity.record.dto.IndexDto;
import com.system.entity.record.dto.TableDto;
import com.system.entity.record.vo.TableSourceColumn;
import com.system.entity.record.vo.TableSourceConstraint;
import com.system.entity.record.vo.TableSourceIndex;
import com.system.entity.record.vo.TableTargetColumn;
import com.system.entity.record.vo.TableTargetConstraint;
import com.system.entity.record.vo.TableTargetIndex;
import com.system.utils.annotations.TargetMapperScan;

@TargetMapperScan
public interface DBTargetConflictDao {

	@Insert("INSERT INTO version_management_source_table VALUES( " + "null , #{sourceTableName } ,#{method } , "
			+ "#{createTime}, #{updateTime} ,#{type }, #{deleteState },#{version} )")
	void insertSourceTable(SourceTable sourceTable);

	@Insert("INSERT INTO version_management_target_table VALUES( "
			+ "null , #{targetTableName } ,#{changeTableName } , #{method } ,"
			+ "#{createTime}, #{updateTime} ,#{type } ,#{deleteState },#{version} )")
	void insertTargetTable(TargetTable targetTable);

	@Insert("INSERT INTO version_management_source_column VALUES( "
			+ "null , #{sourceTableName } ,#{sourceColumnName } , #{method } , "
			+ "#{createTime}, #{updateTime} ,#{type } , #{deleteState },#{version} )")
	void insertSourceColumn(SourceColumn sourceColumn);

	@Insert("INSERT INTO version_management_target_column VALUES( "
			+ "null , #{targetTableName } , #{targetColumnName } , #{changeColumnName } , #{method } ,"
			+ "#{createTime}, #{updateTime} ,#{type } , #{deleteState },#{version} )")
	void insertTargetColumn(TargetColumn targetColumn);

	@Insert("INSERT INTO version_management_source_index VALUES( "
			+ "null , #{sourceTableName } , #{sourceIndexName }, #{method } , "
			+ "#{createTime}, #{updateTime} ,#{type } , #{deleteState },#{version} )")
	void insertSourceIndex(SourceIndex sourceIndex);

	@Insert("INSERT INTO version_management_target_index VALUES( "
			+ "null , #{targetTableName } , #{targetIndexName }, #{changeIndexName } , #{method } ,"
			+ "#{createTime}, #{updateTime} ,#{type } , #{deleteState },#{version} )")
	void insertTargetIndex(TargetIndex targetIndex);

	@Insert("INSERT INTO version_management_source_constraint VALUES( "
			+ "null , #{sourceTableName } , #{sourceConstraintName }, #{method } , "
			+ "#{createTime}, #{updateTime} ,#{type } , #{deleteState },#{version} )")
	void insertSourceConstraint(SourceConstraint sourceConstraint);

	@Insert("INSERT INTO version_management_target_constraint VALUES( "
			+ "null , #{targetTableName } , #{targetConstraintName }, #{changeConstraintName } , #{method } ,"
			+ "#{createTime}, #{updateTime} ,#{type } ,#{deleteState },#{version} )")
	void insertTargetConstraint(TargetConstraint targetConstraint);

	List<SourceTable> getSourceTables(@Param("version") String version, @Param("type") Integer type );

	List<TargetTable> getTargetTables(@Param("version") String version , @Param("type") Integer type , @Param("method") String method  , @Param("changeTableNames") Collection<String> changeTableNames );

	List<TableSourceColumn> getSourceColumns(@Param("version") String version  , @Param("type") Integer type );

	List<TableTargetColumn> getTargetColumns(@Param("version") String version, @Param("type") Integer type, @Param("tableName") String tableName);

	List<TableSourceIndex> getSourceIndexs(@Param("version") String version, @Param("type") Integer type );

	List<TableTargetIndex> getTargetIndexs(@Param("version") String version , @Param("type") Integer type, @Param("tableName")  String tableName);

	List<TableSourceConstraint> getSourceConstraints(@Param("version") String version , @Param("type") Integer type);

	List<TableTargetConstraint> getTargetConstraints(@Param("version") String version , @Param("type") Integer type, @Param("tableName")  String tableName);
	
	@Update("UPDATE version_management_source_table SET method = #{method } , update_time = #{updateTime} , type = #{type } WHERE version = #{version} AND source_table_name = #{sourceTableName }")
	int editSourceTables(TableDto tableDto);
	
	@Update("UPDATE version_management_target_table SET method = #{method } , update_time = #{updateTime} , type = #{type }  WHERE version = #{version} AND target_table_name = #{targetTableName }")
	int editTargetTables(TableDto tableDto);
	
	
	@Update("UPDATE version_management_target_table SET method = #{method } , update_time = #{updateTime} ,  type = #{type }  , change_table_name = #{sourceTableName }  WHERE version = #{version} AND target_table_name = #{targetTableName }")
	int changeTargetTables(TableDto tableDto);


	@Update("UPDATE version_management_target_column SET target_table_name = #{sourceTableName }  WHERE version = #{version}   AND type = #{type }  AND target_table_name = #{targetTableName }")
	int changeTargetColumnsTableName(@Param("version") String version , @Param("type")Integer type, @Param("targetTableName") String targetTableName, @Param("sourceTableName") String sourceTableName);
	
	@Update("UPDATE version_management_target_index SET target_table_name = #{sourceTableName }  WHERE version = #{version}   AND type = #{type }  AND target_table_name = #{targetTableName }")
	int changeTargetIndexsTableName(@Param("version") String version , @Param("type") Integer type, @Param("targetTableName") String targetTableName, @Param("sourceTableName") String sourceTableName);
	
	@Update("UPDATE version_management_target_constraint SET target_table_name = #{sourceTableName }  WHERE version = #{version}   AND type = #{type }  AND target_table_name = #{targetTableName }")
	int changeTargetConstraintsTableName(@Param("version") String version , @Param("type")Integer type, @Param("targetTableName") String targetTableName, @Param("sourceTableName") String sourceTableName);

	
	@Update("UPDATE version_management_source_column SET method = #{method } , update_time = #{updateTime} , type = #{type } WHERE version = #{version} AND source_table_name = #{tableName } AND  source_column_name = #{sourceColumnName }")
	void editSourceColumns(ColumnDto columnDto);
	
	@Update("UPDATE version_management_target_column SET method = #{method } , update_time = #{updateTime} , type = #{type } WHERE version = #{version} AND target_table_name = #{tableName } AND target_column_name = #{targetColumnName }")
	void editTargetColumns(ColumnDto columnDto);

	@Update("UPDATE version_management_target_column SET method = #{method } , update_time = #{updateTime} ,  type = #{type }  , change_column_name = #{sourceColumnName }  WHERE version = #{version} AND target_table_name = #{tableName } AND target_column_name = #{targetColumnName } ")
	void changeTargetColumns(ColumnDto columnDto);

	@Update("UPDATE version_management_target_index SET  update_time = #{updateTime} ,  delete_state = 0 WHERE version = #{version } AND type = #{type} AND target_table_name = #{tableName }  and target_index_name in (#{indexNames})")
	@Lang(ForEachLang.class)
	void deleteIndexs(@Param("tableName") String tableName, @Param("indexNames") Set<String> indexNames,  @Param("type") Integer type, @Param("version") String version , @Param("updateTime") Date updateTime ) ;

	@Update("UPDATE version_management_target_constraint SET  update_time = #{updateTime} ,   delete_state = 0 WHERE version = #{version } AND type = #{type} AND target_table_name = #{tableName }  and target_constraint_name in (#{constraintNames})")
	@Lang(ForEachLang.class)
	void deleteConstriants(@Param("tableName") String tableName, @Param("constraintNames") Set<String> constraintNames,  @Param("type") Integer type, @Param("version") String version , @Param("updateTime") Date updateTime );

	@Update("UPDATE version_management_source_index SET method = #{method } , update_time = #{updateTime} , type = #{type } WHERE version = #{version} AND source_table_name = #{tableName } AND  source_index_name = #{sourceIndexName }")
	void editSourceIndexs(IndexDto indexDto);

	@Update("UPDATE version_management_target_index SET method = #{method } , update_time = #{updateTime} , type = #{type } WHERE version = #{version} AND target_table_name = #{tableName } AND target_index_name = #{targetIndexName }")
	void editTargetIndexs(IndexDto indexDto);

	@Update("UPDATE version_management_target_index SET method = #{method } , update_time = #{updateTime} ,  type = #{type }  , change_index_name = #{sourceIndexName }  WHERE version = #{version} AND target_table_name = #{tableName } AND target_index_name = #{targetIndexName } ")
	void changeTargetIndexs(IndexDto indexDto);

	@Update("UPDATE version_management_source_constraint SET method = #{method } , update_time = #{updateTime} , type = #{type } WHERE version = #{version} AND source_table_name = #{tableName } AND  source_constraint_name = #{sourceConstraintName }")
	void editSourceConstraints(ConstraintDto constraintDto);

	@Update("UPDATE version_management_target_constraint SET method = #{method } , update_time = #{updateTime} , type = #{type } WHERE version = #{version} AND target_table_name = #{tableName } AND target_constraint_name = #{targetConstraintName }")
	void editTargetConstraints(ConstraintDto constraintDto);

	@Update("UPDATE version_management_target_constraint SET method = #{method } , update_time = #{updateTime} ,  type = #{type }  , change_constraint_name = #{sourceConstraintName }  WHERE version = #{version} AND target_table_name = #{tableName } AND target_constraint_name = #{targetConstraintName } ")
	void changeTargetConstraints(ConstraintDto constraintDto);
	
	
	@Delete("DELETE FROM version_management_source_table WHERE version = #{version}")
	void physicalDeleteSourceTables(@Param("version") String version);

	@Delete("DELETE FROM version_management_target_table WHERE version = #{version}")
	void physicalDeleteTargetTables(@Param("version") String version);

	@Delete("DELETE FROM version_management_source_column WHERE version = #{version}")
	void physicalDeleteSourceColumns(@Param("version") String version);

	@Delete("DELETE FROM version_management_target_column WHERE version = #{version}")
	void physicalDeleteTargetColumns(@Param("version") String version);

	@Delete("DELETE FROM version_management_source_index WHERE version = #{version}")
	void physicalDeleteSourceIndexs(@Param("version") String version);

	@Delete("DELETE FROM version_management_target_index WHERE version = #{version}")
	void physicalDeleteTargetIndexs(@Param("version") String version);

	@Delete("DELETE FROM version_management_source_constraint WHERE version = #{version}")
	void physicalDeleteSourceConstriants(@Param("version") String version);

	@Delete("DELETE FROM version_management_target_constraint WHERE version = #{version}")
	void physicalDeleteTargetConstriants(@Param("version") String version);

	@Delete("UPDATE version_management_source_table SET delete_state = 0 WHERE version = #{version}")
	void logicDeleteSourceTables(@Param("version") String verson);
	
	@Delete("UPDATE version_management_target_table SET delete_state = 0 WHERE version = #{version}")
	void logicDeleteTargetTables(@Param("version") String verson);

	@Delete("UPDATE version_management_source_column SET delete_state = 0 WHERE version = #{version}")
	void logicDeleteSourceColumns(@Param("version") String verson);

	@Delete("UPDATE version_management_target_column SET delete_state = 0 WHERE version = #{version}")
	void logicDeleteTargetColumns(@Param("version") String verson);

	@Delete("UPDATE version_management_source_index SET delete_state = 0 WHERE version = #{version}")
	void logicDeleteSourceIndexs(@Param("version") String verson);

	@Delete("UPDATE version_management_target_index SET delete_state = 0 WHERE version = #{version}")
	void logicDeleteTargetIndexs(@Param("version") String verson);

	@Delete("UPDATE version_management_source_constraint SET delete_state = 0 WHERE version = #{version}")
	void logicDeleteSourceConstriants(@Param("version") String verson);

	@Delete("UPDATE version_management_target_constraint SET delete_state = 0 WHERE version = #{version}")
	void logicDeleteTargetConstriants(@Param("version") String verson);
	
	
	

}
