package com.siberhus.ngai.guardian.dao;

import com.siberhus.ngai.dao.ICrudDao;
import com.siberhus.ngai.guardian.model.LoginFailureLog;


public interface ILoginFailureLogDao extends ICrudDao<LoginFailureLog, Long>{
	
	public LoginFailureLog findByIpAddress(String ipAddress);
	
}
