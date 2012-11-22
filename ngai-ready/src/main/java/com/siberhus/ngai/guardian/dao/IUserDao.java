package com.siberhus.ngai.guardian.dao;


import com.siberhus.ngai.dao.ICrudDao;
import com.siberhus.ngai.guardian.model.User;

public interface IUserDao extends ICrudDao<User, Long>{
	
	public void wipe(User user);
	
	public User findByUsername(String username);
	
}
