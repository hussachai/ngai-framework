package com.siberhus.ngai.example.group1;

import com.siberhus.ngai.annot.JpaNativeQuery;
import com.siberhus.ngai.annot.JpaQuery;
import com.siberhus.ngai.dao.IAnnotatedQueryDao;
import com.siberhus.ngai.example.model.User;

public interface G1E3DaoIf extends IAnnotatedQueryDao{
	
	@JpaQuery(entityName=User.ENTITY_NAME)
	public User getUserByUsername(String username);
	
	@JpaQuery(query="select count(*) from User u where u.admin=true")
	public Number countAllAdminUser();
	
	@JpaNativeQuery(query="SELECT COUNT(*) FROM USERS u WHERE u.is_admin=false")
	public Number countAllNormalUser();
	
}
