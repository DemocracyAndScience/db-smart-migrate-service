package com.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.system.config.support.DbContextHolder;
import com.system.entity.record.SourceTable;
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
import com.system.service.DBSyncService;
import com.system.utils.Result;
import com.system.utils.componet.InitTables;
import com.system.utils.constants.SystemConstants;
@SessionAttributes(SystemConstants.SESSION_DB_NAME)
@RestController
public class DBSyncController {

	@Autowired
	private DBSyncService dBSyncService;

	@Autowired
	private InitTables initTables ; 
	/**
	 * 设置当前的数据库
	 */
	@RequestMapping(value = "/setDatabaseName/{"+SystemConstants.SESSION_DB_NAME+"}")
    public Result  setDatabaseName(@PathVariable(SystemConstants.SESSION_DB_NAME)  String dbName ,Model model){
		model.addAttribute(SystemConstants.SESSION_DB_NAME, dbName);
		DbContextHolder.setDbType(dbName);
		String dbType = DbContextHolder.getDbType();
		initTables.run();
		return Result.success(dbType);
    }
	
	/**
	 * 同步数据库结构
	 * 
	 *
	 * @return
	 */
	@RequestMapping("/dbsync/syncFramework")
	public Result syncStructure() {
		dBSyncService.syncStructure();
		return Result.success("");
	}

	/**
	 * 获取source待处理的表
	 * 
	 *
	 * @return
	 */
	@RequestMapping("/dbsync/getSourceTables")
	public Result getSourceTables() {
		List<SourceTable> sourceTables = dBSyncService.getSourceTables(1);
		return Result.success(sourceTables);
	}

	/**
	 * 获取target待处理的表
	 *
	 * @return
	 */
	@RequestMapping("/dbsync/getTargetTables")
	public Result getTargetTables() {
		List<TargetTable> targetTables = dBSyncService.getTargetTables(1);
		return Result.success(targetTables);
	}
	
	/**
	 * 【1】获取target待处理的表  --第一次点击下一步
	 *
	 * @return
	 */
	@PostMapping(value=  "/dbsync/readyTables"  )
	public Result readyTables(@RequestBody List<TableDto> tablesArr) {
		if(tablesArr == null || tablesArr.isEmpty()) {
			return Result.success("参数为空");
		}
		int s =  dBSyncService.readyTables(tablesArr);
		return Result.success(s);
	}

	
	
	/**
	 * 获取source待处理的列
	 *
	 * @return
	 */
	@RequestMapping("/dbsync/getSourceColumns")
	public Result getSourceColumns() {
		List<TableSourceColumn> sourceColumns = dBSyncService.getSourceColumns(1);
		return Result.success(sourceColumns);
	}

	/**
	 * 获取target待处理的列
	 *
	 * @return
	 */
	@RequestMapping(value = { "/dbsync/getTargetColumns/{tableName}",  "/dbsync/getTargetColumns" } )
	public Result getTargetColumns(@PathVariable(value= "tableName" , required = false) String tableName ) {
		List<TableTargetColumn> targetColumns = dBSyncService.getTargetColumns(1 , tableName);
		return Result.success(targetColumns);
	}

	/**
	 * 【1】获取target待处理的表  --第二次点击下一步
	 *
	 * @return
	 */
	@PostMapping(value=  "/dbsync/readyColumns"  )
	public Result readyColumns(@RequestBody List<ColumnDto> columnDtos) {
		if(columnDtos == null || columnDtos.isEmpty()) {
			return Result.success("参数为空");
		}
		int s =  dBSyncService.readyColumns(columnDtos);
		return Result.success(s);
	}

	
	
	
	/**
	 * 获取source待处理的索引
	 *
	 * @return
	 */
	@RequestMapping("/dbsync/getSourceIndexs")
	public Result getSourceIndexs() {
		   List<TableSourceIndex> sourceIndexs = dBSyncService.getSourceIndexs(1);
		return Result.success(sourceIndexs);
	}

	/**
	 * 获取target待处理的索引
	 *
	 * @return
	 */
	@RequestMapping(value = {"/dbsync/getTargetIndexs/{tableName}" ,"/dbsync/getTargetIndexs"} )
	public Result getTargetIndexs(@PathVariable(value= "tableName" , required = false) String tableName ) {
		 List<TableTargetIndex> targetIndexs = dBSyncService.getTargetIndexs(1 , tableName);
		return Result.success(targetIndexs);
	}

	/**
	 * 【1】获取target待处理的表  --第三次点击下一步
	 *
	 * @return
	 */
	@PostMapping(value=  "/dbsync/readyIndexs"  )
	public Result readyIndexs(@RequestBody List<IndexDto> indexDtos) {
		if(indexDtos == null || indexDtos.isEmpty()) {
			return Result.success("参数为空");
		}
		int s =  dBSyncService.readyIndexs(indexDtos);
		return Result.success(s);
	}
	
	
	/**
	 * 获取source待处理的表
	 *
	 * @return
	 */
	@RequestMapping("/dbsync/getSourceConstraints")
	public Result getSourceConstraints() {
		 List<TableSourceConstraint> sourceConstraints = dBSyncService.getSourceConstraints(1);
		return Result.success(sourceConstraints);
	}

	/**
	 * 获取target待处理的表
	 *
	 * @return
	 */
	@RequestMapping(value = {"/dbsync/getTargetConstraints/{tableName}" ,"/dbsync/getTargetConstraints"} )
	public Result getTargetConstraints(@PathVariable(value= "tableName" , required = false) String tableName ) {
		 List<TableTargetConstraint> targetConstraints = dBSyncService.getTargetConstraints(1 , tableName);
		return Result.success(targetConstraints);
	}

	/**
	 * 【1】获取target待处理的表  --第四次点击下一步
	 *
	 * @return
	 */
	@PostMapping(value=  "/dbsync/readyConstraints"  )
	public Result readyConstraints(@RequestBody List<ConstraintDto> constraintDtos) {
		if(constraintDtos == null || constraintDtos.isEmpty()) {
			return Result.success("参数为空");
		}
		int s =  dBSyncService.readyConstraints(constraintDtos);
		return Result.success(s);
	}
	
}
