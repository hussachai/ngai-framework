package com.siberhus.ngai.example.group2;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.siberhus.ngai.service.IService;

public class G2E1Service implements IService{

	@PersistenceContext
	private EntityManager entityManager;
	
	public String revealPassword(String username){
		return (String)entityManager.createQuery("select u.password from User u where u.username=?")
		.setParameter(1, username).getSingleResult();
	}
	
	
}
