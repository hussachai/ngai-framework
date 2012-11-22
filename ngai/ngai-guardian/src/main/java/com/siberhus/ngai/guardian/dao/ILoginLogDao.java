package com.siberhus.ngai.guardian.dao;

import com.siberhus.ngai.dao.ICrudDao;
import com.siberhus.ngai.guardian.model.LoginLog;


public interface ILoginLogDao extends ICrudDao<LoginLog, Long> {
	
	public LoginLog findLastLogByUserId(Long userId);
	
}
