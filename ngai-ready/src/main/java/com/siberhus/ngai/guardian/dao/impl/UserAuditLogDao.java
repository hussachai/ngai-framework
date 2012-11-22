package com.siberhus.ngai.guardian.dao.impl;

import com.siberhus.ngai.dao.AbstractCrudDao;
import com.siberhus.ngai.guardian.dao.IUserAuditLogDao;
import com.siberhus.ngai.guardian.model.UserAuditLog;

public class UserAuditLogDao extends AbstractCrudDao<UserAuditLog, Long> implements IUserAuditLogDao{

	@Override
	public Class<UserAuditLog> getModelClass() {
		return UserAuditLog.class;
	}
	
	
}
