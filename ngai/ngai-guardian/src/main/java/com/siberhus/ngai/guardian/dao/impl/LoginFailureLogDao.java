package com.siberhus.ngai.guardian.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.siberhus.ngai.dao.AbstractCrudDao;
import com.siberhus.ngai.guardian.dao.ILoginFailureLogDao;
import com.siberhus.ngai.guardian.model.LoginFailureLog;
import com.siberhus.ngai.util.JpaQueryUtil;


public class LoginFailureLogDao extends AbstractCrudDao<LoginFailureLog, Long> implements ILoginFailureLogDao{

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Class<LoginFailureLog> getModelClass() {
		return LoginFailureLog.class;
	}
	
	@Override
	public LoginFailureLog findByIpAddress(String ipAddress) {
		
		return (LoginFailureLog)JpaQueryUtil.getFirstResultFromNamedQuery(
				entityManager, "LoginFailureLog.findByIpAddress", ipAddress);
	}
	
}
