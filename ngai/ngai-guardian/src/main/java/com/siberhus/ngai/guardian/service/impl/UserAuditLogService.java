package com.siberhus.ngai.guardian.service.impl;

import com.siberhus.ngai.annot.InjectDao;
import com.siberhus.ngai.dao.ICrudDao;
import com.siberhus.ngai.guardian.dao.IUserAuditLogDao;
import com.siberhus.ngai.guardian.dao.impl.UserAuditLogDao;
import com.siberhus.ngai.guardian.model.UserAuditLog;
import com.siberhus.ngai.guardian.service.IUserAuditLogService;
import com.siberhus.ngai.service.AbstractCrudDaoService;

public class UserAuditLogService extends AbstractCrudDaoService<UserAuditLog, Long> implements IUserAuditLogService{

	@InjectDao(implementation=UserAuditLogDao.class)
	private IUserAuditLogDao userAuditLogDao;
	
	@Override
	public ICrudDao<UserAuditLog, Long> getCrudDao() {
		return userAuditLogDao;
	}

}
