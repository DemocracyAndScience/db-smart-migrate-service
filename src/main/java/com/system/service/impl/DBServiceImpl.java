package com.system.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationState;
import org.flywaydb.core.api.MigrationType;
import org.flywaydb.core.api.MigrationVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.system.entity.VersionInfo;
import com.system.service.DBService;
import com.system.utils.CollectionUtils;
import com.system.utils.DownLoadUtils;
import com.system.utils.FileUtil;
import com.system.utils.RecordsStateUtils;
import com.system.utils.componet.ConflictUtils;
import com.system.utils.componet.DBUtils;
import com.system.utils.componet.PathUtil;
import com.system.utils.componet.VersionManageUtils;
import com.system.utils.constants.SystemConstants;

@Service
public class DBServiceImpl implements DBService {
	protected final static Logger logger = LoggerFactory.getLogger(DBServiceImpl.class);
	@Autowired
	private Flyway flyway;
	@Autowired
	private DBUtils dBUtils;
	@Autowired
	private PathUtil pathUtil;
	@Autowired
	private HttpSession session;
	@Autowired
	private ConflictUtils conflictUtils;

	@Autowired
	private VersionManageUtils versionManageUtils ; 
	@Override
	public List<VersionInfo> getInfo() {
		String tableSchema = dBUtils.getTableSchema(session);
		flyway.setSchemas(tableSchema);
		flyway.setLocations(SystemConstants.FILE_STSTEM + pathUtil.getPath());
		MigrationInfo[] all = flyway.info().all();
		List<VersionInfo> ifnos = new ArrayList<>();
		if (all == null || all.length == 0) {
			return ifnos;
		}
		for (MigrationInfo migrationInfo : all) {
			VersionInfo e = new VersionInfo();
			e.setChecksum(migrationInfo.getChecksum());
			e.setDescription(migrationInfo.getDescription());
			e.setExecutionTime(migrationInfo.getExecutionTime());
			e.setInstalledOn(migrationInfo.getInstalledOn());
			e.setScript(migrationInfo.getScript());
			MigrationState migrationState = migrationInfo.getState();
			e.setState(migrationState.getDisplayName());
			MigrationType migrationType = migrationInfo.getType();
			e.setType(migrationType.name());
			MigrationVersion migrationVersion = migrationInfo.getVersion();
			e.setVersion(migrationVersion.getVersion());
			ifnos.add(e);
		}
		Collections.sort(ifnos, CollectionUtils.VERSION_INFO_COMPARATOR);
		return ifnos;
	}

	@Override
	public void deleteFile(String fileName) {
		List<VersionInfo> info = getInfo();
		VersionInfo versionInfo = null ; 
		for (VersionInfo versionInfo2 : info) {
			if(fileName.equals(versionInfo2.getScript())) {
				versionInfo = versionInfo2 ; 
				break;
			}
		}
		if(versionInfo == null) {
			throw new RuntimeException("未找到文件！");
		}
		versionManageUtils.deleteCurrentVersionErrorRecord(fileName, new BigDecimal(versionInfo.getVersion()), versionInfo.getState());
	}

	@Override
	public void upload(MultipartFile file) {
		List<VersionInfo> info = getInfo();
		RecordsStateUtils.stateValidate(info);
		String originalFilename = file.getOriginalFilename();
		String version = "0";
		if (!info.isEmpty()) {
			version = info.get(0).getVersion();
		}
		double cuurentMaxVersion = Double.parseDouble(version);
		FileUtil.validateFileName(originalFilename, cuurentMaxVersion);
		FileUtil.validateFileContent(file);
		originalFilename = originalFilename.replaceFirst("__", "__" + SystemConstants.FILE_CREATE_UPLOAD);
		try {
			FileUtil.saveFile(file.getInputStream(), originalFilename, pathUtil.getPath());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	@Override
	public void download(String version, HttpServletResponse response) throws IOException {
		File file = pathUtil.getFile(version);
		DownLoadUtils.download(response, new FileInputStream(file), version);
	}

	@Override
	public List<String> filenames() {
		String path = pathUtil.getPath();
		File file = new File(path);
		File[] listFiles = file.listFiles();
		ArrayList<String> arrayList = new ArrayList<>();
		if (listFiles == null || listFiles.length == 0) {
			return arrayList;
		}
		for (File file2 : listFiles) {
			arrayList.add(file2.getName());
		}
		Collections.sort(arrayList);
		Collections.reverse(arrayList);
		return arrayList;
	}

	@Override
	public int migrate() {
		String tableSchema = dBUtils.getTableSchema(session);
		flyway.setSchemas(tableSchema);
		flyway.setLocations(SystemConstants.FILE_STSTEM + pathUtil.getPath());
		VersionInfo info = getInfo().get(0);
		int migrate = flyway.migrate();
		if (migrate > 0) {
			boolean contains = info.getScript().contains(SystemConstants.FILE_CREATE_SYNCHRONOUS);
			if (contains) {
				// 逻辑删除表中所有数据
				conflictUtils.logicDeleteByVersion(info.getVersion());
			}
		}
		return migrate;
	}

}
