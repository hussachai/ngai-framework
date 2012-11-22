package com.siberhus.ngai.guardian.service;

import com.siberhus.ngai.guardian.SessionUser;
import com.siberhus.ngai.service.IService;

public interface IAuthorizationService extends IService{
	
	public void checkPermission(String actionUri, String eventName, SessionUser sessionUser);
	
	public void clearCache();
	
}
