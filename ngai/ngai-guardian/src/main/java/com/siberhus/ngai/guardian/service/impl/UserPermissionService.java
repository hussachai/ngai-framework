package com.siberhus.ngai.guardian.service.impl;

import com.siberhus.ngai.annot.InjectDao;
import com.siberhus.ngai.dao.ICrudDao;
import com.siberhus.ngai.guardian.dao.IUserPermissionDao;
import com.siberhus.ngai.guardian.dao.impl.UserPermissionDao;
import com.siberhus.ngai.guardian.model.UserPermission;
import com.siberhus.ngai.guardian.service.IUserPermissionService;
import com.siberhus.ngai.service.AbstractCrudDaoService;

public class UserPermissionService extends AbstractCrudDaoService<UserPermission, Long> 
	implements IUserPermissionService{
	
	@InjectDao(implementation=UserPermissionDao.class)
	private IUserPermissionDao userPermissionDao;
	
	@Override
	public ICrudDao<UserPermission, Long> getCrudDao() {
		
		return userPermissionDao;
	}
	
	

}
