package com.siberhus.ngai.guardian.service;

import com.siberhus.ngai.guardian.LoginContext;
import com.siberhus.ngai.guardian.SessionUser;
import com.siberhus.ngai.guardian.exception.AuthenticationException;
import com.siberhus.ngai.guardian.model.User;
import com.siberhus.ngai.service.IService;

public interface IAuthenticationService extends IService{
	
	public SessionUser login(LoginContext loginContext) throws AuthenticationException;
	
	public void logout(SessionUser sessUser);

	public User getAuthenticatedUser(SessionUser sessionUser);
	
}
