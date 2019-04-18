package com.system.dao;

import org.apache.ibatis.annotations.Update;

import com.system.utils.annotations.TargetMapperScan;
import org.springframework.stereotype.Repository;

@TargetMapperScan
@Repository
public interface DBTargetCreateTablesDao {

	@Update("CREATE TABLE IF NOT EXISTS `version_management_source_table` (\n" + 
			"  `id` int(32) NOT NULL AUTO_INCREMENT,\n" + 
			"  `source_table_name` varchar(255) DEFAULT NULL COMMENT '待处理的表',\n" + 
			"  `method` varchar(20) DEFAULT NULL COMMENT '处理方法 ：ADD CHANGE',\n" + 
			"  `create_time` datetime DEFAULT NULL COMMENT '创建时间',\n" + 
			"  `update_time` datetime DEFAULT NULL COMMENT '更新时间',\n" + 
			"  `type` int(1)  COMMENT '类型： 第一次遍历时 1 ， 第二次人工操作 2  ' ,\n" + 
			"  `delete_state` int(1) DEFAULT '1',\n" + 
			"  `version` varchar(255) ,\n"+
			"  PRIMARY KEY (`id`)\n" + 
			") ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='source 表变更信息表';")
	int createSourceTable();
	
	@Update(" CREATE TABLE IF NOT EXISTS  `version_management_source_column` (\n" + 
			"  `id` int(32) NOT NULL AUTO_INCREMENT,\n" + 
			"  `source_table_name` varchar(255) DEFAULT NULL COMMENT '待处理的表',\n" + 
			"  `source_column_name` varchar(255) DEFAULT NULL COMMENT '待处理的列',\n" + 
			"  `method` varchar(20) DEFAULT NULL COMMENT '处理方法 ：ADD CHANGE',\n" + 
			"  `create_time` datetime DEFAULT NULL COMMENT '创建时间',\n" + 
			"  `update_time` datetime DEFAULT NULL COMMENT '更新时间',\n" + 
			"  `type` int(1)  COMMENT '类型： 第一次遍历时 1 ， 第二次人工操作 2  ' ,\n" + 
			"  `delete_state` int(1) DEFAULT '1',\n" + 
			"  `version` varchar(255) ,\n"+
			"  PRIMARY KEY (`id`)\n" + 
			") ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='source 表变更信息表';")
	int createSourceColumn();
	
	@Update("CREATE TABLE IF NOT EXISTS `version_management_source_index` (\n" + 
			"  `id` int(32) NOT NULL AUTO_INCREMENT,\n" + 
			"  `source_table_name` varchar(255) DEFAULT NULL COMMENT '待处理的表',\n" + 
			"  `source_index_name` varchar(255) DEFAULT NULL COMMENT '待处理的索引',\n" + 
			"  `method` varchar(20) DEFAULT NULL COMMENT '处理方法 ：ADD CHANGE',\n" + 
			"  `create_time` datetime DEFAULT NULL COMMENT '创建时间',\n" + 
			"  `update_time` datetime DEFAULT NULL COMMENT '更新时间',\n" + 
			"  `type` int(1)  COMMENT '类型： 第一次遍历时 1 ， 第二次人工操作 2  ' ,\n" + 
			"  `delete_state` int(1) DEFAULT '1',\n" + 
			"  `version` varchar(255) ,\n"+
			"  PRIMARY KEY (`id`)\n" + 
			") ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='source 表变更信息表';")
	int createSourceIndex();
	@Update("CREATE TABLE IF NOT EXISTS `version_management_source_constraint` (\n" + 
			"  `id` int(32) NOT NULL AUTO_INCREMENT,\n" + 
			"  `source_table_name` varchar(255) DEFAULT NULL COMMENT '待处理的表',\n" + 
			"  `source_constraint_name` varchar(255) DEFAULT NULL COMMENT '待处理的约束',\n" + 
			"  `method` varchar(20) DEFAULT NULL COMMENT '处理方法 ：ADD CHANGE',\n" + 
			"  `create_time` datetime DEFAULT NULL COMMENT '创建时间',\n" + 
			"  `update_time` datetime DEFAULT NULL COMMENT '更新时间',\n" + 
			"  `type` int(1)  COMMENT '类型： 第一次遍历时 1 ， 第二次人工操作 2  ' ,\n" + 
			"  `delete_state` int(1) DEFAULT '1',\n" +
			"  `version` varchar(255) ,\n"+
			"  PRIMARY KEY (`id`)\n" + 
			") ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='source 表变更信息表';")
	int createSourceConstraint();
	
	@Update("CREATE TABLE IF NOT EXISTS `version_management_target_table` (\n" + 
			"  `id` int(32) NOT NULL AUTO_INCREMENT,\n" + 
			"  `target_table_name` varchar(255) DEFAULT NULL COMMENT '待处理的表',\n" + 
			"  `change_table_name` varchar(255) DEFAULT NULL COMMENT '改变后的名字',\n" + 
			"  `method` varchar(20) DEFAULT NULL COMMENT '处理方法 ：CHANGE , DELETE',\n" + 
			"  `create_time` datetime DEFAULT NULL COMMENT '创建时间',\n" + 
			"  `update_time` datetime DEFAULT NULL COMMENT '更新时间',\n" + 
			"  `type` int(1)  COMMENT '类型： 第一次遍历时 1 ， 第二次人工操作 2 ' ,\n" + 
			"  `delete_state` int(1) DEFAULT '1',\n" + 
			"  `version` varchar(255) ,\n"+
			"  PRIMARY KEY (`id`)\n" + 
			") ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='target 表变更信息表';")
	int createTargetTable();
	
	@Update("CREATE TABLE IF NOT EXISTS  `version_management_target_column` (\n" + 
			"  `id` int(32) NOT NULL AUTO_INCREMENT,\n" + 
			"  `target_table_name` varchar(255) DEFAULT NULL COMMENT '待处理的表',\n" + 
			"  `target_column_name` varchar(255) DEFAULT NULL COMMENT '待处理的列',\n" + 
			"  `change_column_name` varchar(255) DEFAULT NULL COMMENT '改变后的名字',\n" + 
			"  `method` varchar(20) DEFAULT NULL COMMENT '处理方法 ：CHANGE , DELETE',\n" + 
			"  `create_time` datetime DEFAULT NULL COMMENT '创建时间',\n" + 
			"  `update_time` datetime DEFAULT NULL COMMENT '更新时间',\n" + 
			"  `type` int(1)  COMMENT '类型： 第一次遍历时 1 ， 第二次人工操作 2 ' ,\n" + 
			"  `delete_state` int(1) DEFAULT '1',\n" + 
			"  `version` varchar(255) ,\n"+
			"  PRIMARY KEY (`id`)\n" + 
			") ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='target 表变更信息表';")
	int createTargetColumn();
	@Update("CREATE TABLE IF NOT EXISTS  `version_management_target_index` (\n" + 
			"  `id` int(32) NOT NULL AUTO_INCREMENT,\n" + 
			"  `target_table_name` varchar(255) DEFAULT NULL COMMENT '待处理的表',\n" + 
			"  `target_index_name` varchar(255) DEFAULT NULL COMMENT '待处理的索引',\n" + 
			"  `change_index_name` varchar(255) DEFAULT NULL COMMENT '改变后的名字',\n" + 
			"  `method` varchar(20) DEFAULT NULL COMMENT '处理方法 ：CHANGE , DELETE',\n" + 
			"  `create_time` datetime DEFAULT NULL COMMENT '创建时间',\n" + 
			"  `update_time` datetime DEFAULT NULL COMMENT '更新时间',\n" + 
			"  `type` int(1)  COMMENT '类型： 第一次遍历时 1 ， 第二次人工操作 2 ' ,\n" + 
			"  `delete_state` int(1) DEFAULT '1',\n" + 
			"  `version` varchar(255) ,\n"+
			"  PRIMARY KEY (`id`)\n" + 
			") ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='target 表变更信息表';")
	int createTargetIndex();
	
	@Update("CREATE TABLE IF NOT EXISTS `version_management_target_constraint` (\n" + 
			"  `id` int(32) NOT NULL AUTO_INCREMENT,\n" + 
			"  `target_table_name` varchar(255) DEFAULT NULL COMMENT '待处理的表',\n" + 
			"  `target_constraint_name` varchar(255) DEFAULT NULL COMMENT '待处理的约束',\n" + 
			"  `change_constraint_name` varchar(255) DEFAULT NULL COMMENT '改变后的名字',\n" + 
			"  `method` varchar(20) DEFAULT NULL COMMENT '处理方法 ：CHANGE , DELETE',\n" + 
			"  `create_time` datetime DEFAULT NULL COMMENT '创建时间',\n" + 
			"  `update_time` datetime DEFAULT NULL COMMENT '更新时间',\n" + 
			"  `type` int(1)  COMMENT '类型： 第一次遍历时 1 ， 第二次人工操作 2 ' ,\n" + 
			"  `delete_state` int(1) DEFAULT '1',\n" + 
			"  `version` varchar(255) ,\n"+
			"  PRIMARY KEY (`id`)\n" + 
			") ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='target 表变更信息表';")
	int createTargetConstraint();
}
