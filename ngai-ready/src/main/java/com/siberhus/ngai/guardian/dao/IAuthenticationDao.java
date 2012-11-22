package com.siberhus.ngai.guardian.dao;

import com.siberhus.ngai.dao.IDao;
import com.siberhus.ngai.guardian.model.User;

public interface IAuthenticationDao extends IDao {
	
	public User findUserByUsername(String username);
	
}
