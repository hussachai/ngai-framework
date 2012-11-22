package com.siberhus.ngai.guardian.dao.impl;

import javax.persistence.NoResultException;

import com.siberhus.ngai.dao.AbstractCrudDao;
import com.siberhus.ngai.guardian.dao.IUserDao;
import com.siberhus.ngai.guardian.model.User;
import com.siberhus.ngai.util.JpaQueryUtil;

public class UserDao extends AbstractCrudDao<User, Long> implements IUserDao{
	
	@Override
	public Class<User> getModelClass() {
		return User.class;
	}
	
	public void wipe(User user){
		if(user!=null){
			getEntityManager().remove(user);
		}
	}
	
	public User findByUsername(String username){
		User user = null;
		try{
			user = (User)JpaQueryUtil.getSingleResultFromNamedQuery(getEntityManager()
				, "User.findByUsername", username);
		}catch(NoResultException e){}
		return user;
	}
	
	
}
