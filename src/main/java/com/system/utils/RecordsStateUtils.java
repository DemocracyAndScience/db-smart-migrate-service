package com.system.utils;

import java.util.List;

import com.system.entity.VersionInfo;
import com.system.utils.constants.FlywayStateConstants;
/**
 * flyway 记录校验
 * @author noah
 */
public class RecordsStateUtils {

	public static void stateValidate(List<VersionInfo> info) {
		if(info != null && !info.isEmpty()) {
			for (VersionInfo versionInfo : info) {
				String state = versionInfo.getState();
				if(FlywayStateConstants.Failed.equals(state) || FlywayStateConstants.FutFail.equals(state) || FlywayStateConstants.MisFail.equals(state)) {
					throw new RuntimeException("有执行失败记录，请删除。");
				}
			}
		}
	}

}
