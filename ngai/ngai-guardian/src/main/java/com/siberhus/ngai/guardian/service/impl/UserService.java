package com.siberhus.ngai.guardian.service.impl;

import java.io.Serializable;

import javax.persistence.EntityNotFoundException;

import com.siberhus.ngai.annot.InjectDao;
import com.siberhus.ngai.dao.ICrudDao;
import com.siberhus.ngai.guardian.dao.IUserDao;
import com.siberhus.ngai.guardian.dao.IUserPermissionDao;
import com.siberhus.ngai.guardian.dao.impl.UserDao;
import com.siberhus.ngai.guardian.dao.impl.UserPermissionDao;
import com.siberhus.ngai.guardian.exception.AuthenticationException;
import com.siberhus.ngai.guardian.model.User;
import com.siberhus.ngai.guardian.service.IUserService;
import com.siberhus.ngai.service.AbstractCrudDaoService;

public class UserService extends AbstractCrudDaoService<User, Long> implements IUserService{

	@InjectDao(implementation=UserDao.class)
	private IUserDao userDao;
	
	@InjectDao(implementation=UserPermissionDao.class)
	private IUserPermissionDao userPermDao;
	
	@Override
	public ICrudDao<User, Long> getCrudDao() {
		return userDao;
	}
	
	@Override
	public void save(User model, Serializable user) {
		if(model.isNew()){
			model.setPassword(AuthenticationService
				.encryptPassword(model.getUsername(),model.getPassword()));
		}
		super.save(model, user);
	}
	
	@Override
	public void save(User model) {
		if(model.isNew()){
			model.setPassword(AuthenticationService
				.encryptPassword(model.getUsername(),model.getPassword()));
		}
		super.save(model);
	}

	@Override
	public void wipe(User user){
		
		getEntityManager().getTransaction().begin();
		
		userDao.wipe(user);
		
		userPermDao.deleteByUserId(user.getId());
		
		getEntityManager().getTransaction().commit();
		
	}
	
	@Override
	public boolean isUsernameAvailable(String username) {
		
		User user = userDao.findByUsername(username);
		if(user == null){
			return true;
		}
		
		return false;
	}

	@Override
	public void changePassword(String username, String oldPassword,
			String newPassword) {
		getEntityManager().getTransaction().begin();
		
		User user = userDao.findByUsername(username);
		if(user==null){
			throw new EntityNotFoundException("User not found");
		}
		String encryptedOldPassword = AuthenticationService.encryptPassword(username, oldPassword);
		if(!user.getPassword().equals(encryptedOldPassword)){
			throw new AuthenticationException("Old password is incorrect");
		}
		
		String encryptedNewPassword = AuthenticationService.encryptPassword(username, newPassword);
		user.setPassword(encryptedNewPassword);
		
		getEntityManager().getTransaction().commit();
	}
	
}
