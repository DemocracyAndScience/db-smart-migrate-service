package com.system.utils.componet;

import java.io.File;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.dao.DBTargetTableDao;
import com.system.utils.constants.FlywayStateConstants;
import com.system.utils.constants.SystemConstants;

@Component
public class VersionManageUtils {

	@Autowired
	private DBTargetTableDao dBTargetTableDao;
	@Autowired
	private ConflictUtils conflictUtils;
	@Autowired
	private PathUtil pathUtil;

	public void deleteCurrentVersionErrorRecord(String fileName, BigDecimal version, String state) {
		File file = pathUtil.getFile(fileName);
		conflictUtils.physicalDeleteByVersion(version);
		boolean equals = state.equals(FlywayStateConstants.MisFail);
		boolean equals2 = state.equals(FlywayStateConstants.FutFail);
		boolean equals3 = state.equals(FlywayStateConstants.Failed);
		if (equals || equals2 || equals3) {
			dBTargetTableDao.deleteMisFailVersion(version + "");
		}
		file.delete();
	}
}
