package com.siberhus.ngai.guardian.dao;

import com.siberhus.ngai.dao.ICrudDao;
import com.siberhus.ngai.guardian.model.AccessDeniedLog;


public interface IAccessDeniedLogDao extends ICrudDao<AccessDeniedLog, Long>{
	
	
	public AccessDeniedLog findMatchedRequest(String ipAddress, 
			String requestURI, String userAgent);
	
	
}
