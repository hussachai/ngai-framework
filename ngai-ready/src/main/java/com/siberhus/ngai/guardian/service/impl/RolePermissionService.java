package com.siberhus.ngai.guardian.service.impl;

import com.siberhus.ngai.annot.InjectDao;
import com.siberhus.ngai.dao.ICrudDao;
import com.siberhus.ngai.guardian.dao.IRolePermissionDao;
import com.siberhus.ngai.guardian.dao.impl.RolePermissionDao;
import com.siberhus.ngai.guardian.model.RolePermission;
import com.siberhus.ngai.guardian.service.IRolePermissionService;
import com.siberhus.ngai.service.AbstractCrudDaoService;

public class RolePermissionService extends AbstractCrudDaoService<RolePermission, Long> 
	implements IRolePermissionService{

	@InjectDao(implementation=RolePermissionDao.class)
	private IRolePermissionDao rolePermissionDao;
	
	@Override
	public ICrudDao<RolePermission, Long> getCrudDao() {
		
		return rolePermissionDao;
	}
	
	
}
