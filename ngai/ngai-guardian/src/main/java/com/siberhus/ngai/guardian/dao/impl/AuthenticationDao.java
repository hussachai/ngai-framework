package com.siberhus.ngai.guardian.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

import com.siberhus.ngai.annot.DaoBean;
import com.siberhus.ngai.annot.Scope;
import com.siberhus.ngai.guardian.dao.IAuthenticationDao;
import com.siberhus.ngai.guardian.exception.AuthenticationException;
import com.siberhus.ngai.guardian.model.User;

@DaoBean(scope=Scope.Singleton)
public class AuthenticationDao implements IAuthenticationDao {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public User findUserByUsername(String username) {
		
		User user = null;
		try{
			 user = (User)entityManager
			 	.createQuery("from User u where u.username=?").setParameter(1, username)
				.getSingleResult();
		}catch(NoResultException e){
			throw new AuthenticationException("Username: "+username+" not found");
		}catch(NonUniqueResultException e){
			throw new AuthenticationException("Username: "+username+" is not unique"); 
		}
		return user;
	}
	
	
}
