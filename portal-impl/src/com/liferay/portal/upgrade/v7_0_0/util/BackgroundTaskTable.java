/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.upgrade.v7_0_0.util;

import java.sql.Types;

/**
 * @author	  Cristina González
 * @generated
 */
public class BackgroundTaskTable {

	public static final String TABLE_NAME = "BackgroundTask";

	public static final Object[][] TABLE_COLUMNS = {
		{"mvccVersion", Types.BIGINT},
		{"backgroundTaskId", Types.BIGINT},
		{"groupId", Types.BIGINT},
		{"companyId", Types.BIGINT},
		{"userId", Types.BIGINT},
		{"userName", Types.VARCHAR},
		{"createDate", Types.TIMESTAMP},
		{"modifiedDate", Types.TIMESTAMP},
		{"name", Types.VARCHAR},
		{"servletContextNames", Types.VARCHAR},
		{"taskExecutorClassName", Types.VARCHAR},
		{"taskContextMap", Types.VARCHAR},
		{"completed", Types.BOOLEAN},
		{"completionDate", Types.TIMESTAMP},
		{"status", Types.INTEGER},
		{"statusMessage", Types.CLOB}
	};

	public static final String TABLE_SQL_CREATE = "create table BackgroundTask (mvccVersion LONG default 0,backgroundTaskId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,name VARCHAR(75) null,servletContextNames VARCHAR(255) null,taskExecutorClassName VARCHAR(200) null,taskContextMap TEXT null,completed BOOLEAN,completionDate DATE null,status INTEGER,statusMessage TEXT null)";

	public static final String TABLE_SQL_DROP = "drop table BackgroundTask";

	public static final String[] TABLE_SQL_ADD_INDEXES = {
		"create index IX_C5A6C78F on BackgroundTask (companyId)",
		"create index IX_579C63B0 on BackgroundTask (groupId, name, taskExecutorClassName, completed)",
		"create index IX_C71C3B7 on BackgroundTask (groupId, status)",
		"create index IX_7A9FF471 on BackgroundTask (groupId, taskExecutorClassName, completed)",
		"create index IX_7E757D70 on BackgroundTask (groupId, taskExecutorClassName, status)",
		"create index IX_75638CDF on BackgroundTask (status)",
		"create index IX_2FCFE748 on BackgroundTask (taskExecutorClassName, status)"
	};

}