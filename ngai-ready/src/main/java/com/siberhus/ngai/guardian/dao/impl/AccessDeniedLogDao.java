package com.siberhus.ngai.guardian.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.siberhus.ngai.dao.AbstractCrudDao;
import com.siberhus.ngai.guardian.dao.IAccessDeniedLogDao;
import com.siberhus.ngai.guardian.model.AccessDeniedLog;
import com.siberhus.ngai.util.JpaQueryUtil;

public class AccessDeniedLogDao extends AbstractCrudDao<AccessDeniedLog, Long> implements IAccessDeniedLogDao{
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Class<AccessDeniedLog> getModelClass() {
		return AccessDeniedLog.class;
	}
	
	@Override
	public AccessDeniedLog findMatchedRequest(String ipAddress,
			String requestURI, String userAgent) {
		return (AccessDeniedLog)JpaQueryUtil
			.getFirstResultFromNamedQuery(entityManager, 
			"AccessDeniedLog.findMatchedRequest", ipAddress, requestURI, userAgent);
	}
	
	
}
