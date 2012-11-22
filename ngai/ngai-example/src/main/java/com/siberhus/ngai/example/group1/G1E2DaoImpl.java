package com.siberhus.ngai.example.group1;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.siberhus.ngai.example.model.User;

public class G1E2DaoImpl implements G1E2DaoIf{
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public User getUserByUsername(String username){
		
		return (User)entityManager.createQuery("from User u where u.username=?")
			.setParameter(1, username).getSingleResult();
	}
	
}
