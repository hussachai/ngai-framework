package com.siberhus.ngai.guardian.service.impl;

import com.siberhus.ngai.annot.InjectDao;
import com.siberhus.ngai.dao.ICrudDao;
import com.siberhus.ngai.guardian.dao.ILoginLogDao;
import com.siberhus.ngai.guardian.dao.impl.LoginLogDao;
import com.siberhus.ngai.guardian.model.LoginLog;
import com.siberhus.ngai.guardian.service.ILoginLogService;
import com.siberhus.ngai.service.AbstractCrudDaoService;

public class LoginLogService extends AbstractCrudDaoService<LoginLog, Long>
	implements ILoginLogService{

	@InjectDao(implementation=LoginLogDao.class)
	private ILoginLogDao loginLogDao;
	
	@Override
	public ICrudDao<LoginLog, Long> getCrudDao() {
		
		return loginLogDao;
	}

}
