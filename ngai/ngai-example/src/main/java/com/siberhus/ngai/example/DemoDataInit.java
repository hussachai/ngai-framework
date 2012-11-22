package com.siberhus.ngai.example;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;

import com.siberhus.ngai.config.NgaiConfiguration;
import com.siberhus.ngai.core.INgaiBootstrap;
import com.siberhus.ngai.core.Ngai;
import com.siberhus.ngai.example.model.Product;
import com.siberhus.ngai.example.model.Role;
import com.siberhus.ngai.example.model.User;

public class DemoDataInit implements INgaiBootstrap {

	@Override
	public void execute(NgaiConfiguration config) {
		
		Ngai.beginTransactionSafely();
		
		Role adminRole = new Role();
		Role userRole = new Role();
		adminRole.setName("admin");
		userRole.setName("user");
		
		for(int i=0;i<5;i++){
			User user = new User();
			user.setUsername("admin"+i);
			user.setPassword(RandomStringUtils.randomAlphanumeric(10));
			user.setAdmin(Boolean.TRUE);
			user.getRoleList().add(adminRole);
			Ngai.getEntityManager().persist(user);
		}
		User lastUser = null;
		for(int i=0;i<10;i++){
			User user = new User();
			user.setUsername("user"+i);
			user.setPassword(RandomStringUtils.randomAlphanumeric(6));
			user.getRoleList().add(userRole);
			Ngai.getEntityManager().persist(user);
			lastUser = user;
		}
		
		Product product = new Product();
		product.setSkuCode("DIS0134");
		product.setName("Mickey MousePad");
		product.setPrice(new BigDecimal("350"));
		product.setCreatedBy(lastUser);
		product.setCreatedAt(new Date());
		product.setLastModifiedBy(lastUser);
		product.setLastModifiedAt(new Date());
		Ngai.getEntityManager().persist(product);
		
		Ngai.getEntityManager().getTransaction().commit();
		
	}
	
}
