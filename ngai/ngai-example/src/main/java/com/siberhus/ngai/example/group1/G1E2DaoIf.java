package com.siberhus.ngai.example.group1;

import com.siberhus.ngai.dao.IDao;
import com.siberhus.ngai.example.model.User;

public interface G1E2DaoIf extends IDao{
	
	public User getUserByUsername(String username);
	
}
