package com.siberhus.ngai.guardian.service.impl;

import com.siberhus.ngai.annot.InjectDao;
import com.siberhus.ngai.dao.ICrudDao;
import com.siberhus.ngai.guardian.dao.ILoginFailureLogDao;
import com.siberhus.ngai.guardian.dao.impl.LoginFailureLogDao;
import com.siberhus.ngai.guardian.model.LoginFailureLog;
import com.siberhus.ngai.guardian.service.ILoginFailureLogService;
import com.siberhus.ngai.service.AbstractCrudDaoService;

public class LoginFailureLogService extends AbstractCrudDaoService<LoginFailureLog, Long> implements ILoginFailureLogService{

	@InjectDao(implementation=LoginFailureLogDao.class)
	private ILoginFailureLogDao loginFailureLogDao;

	@Override
	public ICrudDao<LoginFailureLog, Long> getCrudDao() {
		return loginFailureLogDao;
	}
	
	
}
