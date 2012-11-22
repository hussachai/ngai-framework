package com.siberhus.ngai.guardian.service.impl;

import com.siberhus.ngai.annot.InjectDao;
import com.siberhus.ngai.core.Ngai;
import com.siberhus.ngai.dao.ICrudDao;
import com.siberhus.ngai.guardian.dao.IAccessDeniedLogDao;
import com.siberhus.ngai.guardian.dao.impl.AccessDeniedLogDao;
import com.siberhus.ngai.guardian.model.AccessDeniedLog;
import com.siberhus.ngai.guardian.service.IAccessDeniedLogService;
import com.siberhus.ngai.service.AbstractCrudDaoService;

public class AccessDeniedLogService extends AbstractCrudDaoService<AccessDeniedLog, Long>
	implements IAccessDeniedLogService{
	
	@InjectDao(implementation=AccessDeniedLogDao.class)
	private IAccessDeniedLogDao accessDeniedLogDao;
	
	@Override
	public ICrudDao<AccessDeniedLog, Long> getCrudDao() {
		return accessDeniedLogDao;
	}
	
	@Override
	public void logRequest(String ipAddress, String requestURI, String userAgent) {
		
		Ngai.beginTransactionSafely();//TODO: Manual Trx will be removed in 1.0
		
		AccessDeniedLog accessDeniedLog = accessDeniedLogDao
			.findMatchedRequest(ipAddress, requestURI, userAgent);
		if(accessDeniedLog != null){
			accessDeniedLog.setRetryCount(accessDeniedLog.getRetryCount()+1);
		}else{
			accessDeniedLog = new AccessDeniedLog();
			accessDeniedLog.setIpAddress(ipAddress);
			accessDeniedLog.setRequestURI(requestURI);
			accessDeniedLog.setUserAgent(userAgent);
		}
		
		accessDeniedLogDao.save(accessDeniedLog);
		
		Ngai.getEntityManager().getTransaction().commit();//TODO: Manual Trx will be removed in 1.0
		
	}



	
	
	
}
