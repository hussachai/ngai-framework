package com.siberhus.ngai.example.group1;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.siberhus.ngai.dao.IDao;
import com.siberhus.ngai.example.model.User;

public class G1E1Dao implements IDao{
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public User getUserByUsername(String username){
		
		return (User)entityManager.createQuery("from User u where u.username=?")
			.setParameter(1, username).getSingleResult();
	}
	
}
