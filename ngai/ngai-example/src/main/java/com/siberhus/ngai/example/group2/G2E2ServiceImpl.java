package com.siberhus.ngai.example.group2;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class G2E2ServiceImpl implements G2E2ServiceIf{
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public String revealPassword(String username) {
		return (String)entityManager.createQuery("select u.password from User u where u.username=?")
		.setParameter(1, username).getSingleResult();
	}
	
}
