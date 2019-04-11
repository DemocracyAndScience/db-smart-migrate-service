package com.system.utils.componet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.dao.DBTargetCreateTablesDao;

@Component
public class InitTables {

	@Autowired
	DBTargetCreateTablesDao dBTargetCreateTablesDao;

	public void run() {
		try {
			@SuppressWarnings("unused")
			int createSourceTable = dBTargetCreateTablesDao.createSourceTable();
			dBTargetCreateTablesDao.createSourceColumn();
			dBTargetCreateTablesDao.createSourceIndex();
			dBTargetCreateTablesDao.createSourceConstraint();

			dBTargetCreateTablesDao.createTargetTable();
			dBTargetCreateTablesDao.createTargetColumn();
			dBTargetCreateTablesDao.createTargetIndex();
			dBTargetCreateTablesDao.createTargetConstraint();
		} catch (Exception e) {
			throw new RuntimeException("数据库设置失败！");
		}
	}
}
