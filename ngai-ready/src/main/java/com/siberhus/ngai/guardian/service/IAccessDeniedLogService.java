package com.siberhus.ngai.guardian.service;

import com.siberhus.ngai.guardian.model.AccessDeniedLog;
import com.siberhus.ngai.service.ICrudService;

public interface IAccessDeniedLogService extends ICrudService<AccessDeniedLog, Long>{
	
	public void logRequest(String ipAddress, String requestURI, String userAgent);
	
}
