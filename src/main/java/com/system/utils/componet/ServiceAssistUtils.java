package com.system.utils.componet;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.dao.DBTargetConflictDao;
import com.system.entity.VersionInfo;
import com.system.entity.record.SourceTable;
import com.system.entity.record.TargetTable;
import com.system.entity.record.vo.TableSourceColumn;
import com.system.entity.record.vo.TableSourceConstraint;
import com.system.entity.record.vo.TableSourceIndex;
import com.system.service.DBService;
import com.system.utils.FileUtil;
import com.system.utils.constants.FlywayStateConstants;
import com.system.utils.constants.SystemConstants;
import com.system.utils.constants.cons.sql.MethodConstants;

/**
 * Service 层辅助工具类
 * 
 * @author noah
 *
 */
@Component
public class ServiceAssistUtils {

	private static final Logger logger = LoggerFactory.getLogger(ServiceAssistUtils.class);
	@Autowired
	private DBTargetConflictDao dBTargetConflictDao;

	@Autowired
	private DBService dBService;
	@Autowired
	private PathUtil pathUtil;

	@Autowired
	private VersionManageUtils versionManageUtils;

	/**
	 * 校验当前版本是都还有没有处理的数据
	 * 
	 * @param currentVerson
	 * @return
	 */
	public int validateSync(VersionInfo versionInfo) {
		
		String state = versionInfo.getState();
		boolean equals = state.equals(FlywayStateConstants.MisFail);
		boolean equals2 = state.equals(FlywayStateConstants.FutFail);
		boolean equals3 = state.equals(FlywayStateConstants.Failed);
		BigDecimal bigDecimal = new BigDecimal(versionInfo.getVersion());
		if (equals || equals2 || equals3) {
			versionManageUtils.deleteCurrentVersionErrorRecord(versionInfo.getScript(),bigDecimal, state);
		}
		List<SourceTable> sourceTables = dBTargetConflictDao.getSourceTables(bigDecimal + "", null);
		if (!sourceTables.isEmpty()) {
			return 0;
		}
		List<TableSourceColumn> sourceColumns = dBTargetConflictDao.getSourceColumns(bigDecimal + "", null);
		if (!sourceColumns.isEmpty()) {
			return 1;
		}
		List<TableSourceIndex> sourceIndexs = dBTargetConflictDao.getSourceIndexs(bigDecimal + "", null);
		if (!sourceIndexs.isEmpty()) {
			return 2;
		}
		List<TableSourceConstraint> sourceConstraints = dBTargetConflictDao.getSourceConstraints(bigDecimal + "",
				null);
		if (!sourceConstraints.isEmpty()) {
			return 3;
		}
		return 200;
	}

	/**
	 * 获取当前版本
	 * 
	 * @return
	 */
	public VersionInfo getCurrentVerson() {
		List<VersionInfo> info = dBService.getInfo();
		VersionInfo versionInfo = new VersionInfo();
		if(info.isEmpty()) {
			versionInfo.setVersion("1");
			return versionInfo ; 
		}
		return  info.get(0);
	}

	/**
	 * 获取新增的版本
	 */
	public BigDecimal addVersion(VersionInfo versionInfo) {
		BigDecimal bigDecimal = new BigDecimal(versionInfo.getVersion());
		if(!versionInfo.getVersion().equals("1")) {
			String state = versionInfo.getState();
			boolean equals = state.equals(FlywayStateConstants.MisFail);
			boolean equals2 = state.equals(FlywayStateConstants.FutFail);
			boolean equals3 = state.equals(FlywayStateConstants.Failed);
			if (equals || equals2 || equals3) {
				return bigDecimal;
			}
		}
		return bigDecimal.add(new BigDecimal("0.1"));
	}

	/**
	 * 封装源头表和目标表的对应关系。 因为更名操作
	 * 
	 * @param sourceTables
	 * @param targetTables
	 * @return
	 */
	public Map<String, String> packSourceTargetTables(Set<String> sourceTables, List<TargetTable> targetTables) {
		Map<String, String> sourceTargetTableNameMap = new HashMap<>();
		for (String sourceTableName : sourceTables) {
			for (TargetTable targetTable : targetTables) {
				if (sourceTableName.equals(targetTable.getChangeTableName())) {
					sourceTargetTableNameMap.put(sourceTableName, targetTable.getTargetTableName());
					break;
				}
			}
			if (sourceTargetTableNameMap.get(sourceTableName) == null) {
				sourceTargetTableNameMap.put(sourceTableName, sourceTableName);
			}
		}

		return sourceTargetTableNameMap;
	}

	public void createPathAndsaveFile(BigDecimal currentVerson, StringBuilder sqlSb) {
		String path = pathUtil.getPath();
		String fileName = "V" + currentVerson + "__" + SystemConstants.FILE_CREATE_SYNCHRONOUS
				+ path.replaceAll("/", "-") + ".sql";
		logger.info(path + "/" + fileName);
		FileUtil.saveFile(sqlSb, fileName, path);
	}


	/**
	 * 获取Source 和 Target 对应关系
	 * @param currentVersonBigDecimal
	 * @param sourceTableNames
	 * @return
	 */
	public Map<String, String> getSourceTargetTables(BigDecimal currentVersonBigDecimal,
			Set<String> sourceTableNames) {
		List<TargetTable> targetTables = dBTargetConflictDao.getTargetTables(currentVersonBigDecimal + "", 2,
				MethodConstants.CHANGE, sourceTableNames);
		Map<String, String> packSourceTargetTables = packSourceTargetTables(sourceTableNames,
				targetTables);
		return packSourceTargetTables;
	}
	
}
