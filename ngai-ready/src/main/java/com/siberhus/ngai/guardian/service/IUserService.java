package com.siberhus.ngai.guardian.service;


import com.siberhus.ngai.guardian.model.User;
import com.siberhus.ngai.service.ICrudService;

public interface IUserService extends ICrudService<User, Long>{
	
	/**
	 * Check whether the username has already been taken or not.
	 * @param username
	 * @return
	 */
	public boolean isUsernameAvailable(String username);
	
	public void changePassword(String username, String oldPassword, String newPassword);
	
}
