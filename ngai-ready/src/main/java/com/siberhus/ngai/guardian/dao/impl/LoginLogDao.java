package com.siberhus.ngai.guardian.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.siberhus.ngai.annot.DaoBean;
import com.siberhus.ngai.annot.Scope;
import com.siberhus.ngai.dao.AbstractCrudDao;
import com.siberhus.ngai.guardian.dao.ILoginLogDao;
import com.siberhus.ngai.guardian.model.LoginLog;
import com.siberhus.ngai.util.JpaQueryUtil;

@DaoBean(scope=Scope.Singleton)
public class LoginLogDao extends AbstractCrudDao<LoginLog, Long> implements ILoginLogDao{
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Class<LoginLog> getModelClass() {
		return LoginLog.class;
	}
	
	@Override
	public LoginLog findLastLogByUserId(Long userId) {
		
		return (LoginLog)JpaQueryUtil.getFirstResultFromNamedQuery(
				entityManager, "LoginLog.findLastLogByUserId", userId);
	}
	
	
}
