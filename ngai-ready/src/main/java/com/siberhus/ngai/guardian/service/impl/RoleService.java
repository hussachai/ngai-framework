package com.siberhus.ngai.guardian.service.impl;

import com.siberhus.ngai.annot.InjectDao;
import com.siberhus.ngai.dao.ICrudDao;
import com.siberhus.ngai.guardian.dao.IRoleDao;
import com.siberhus.ngai.guardian.dao.IRolePermissionDao;
import com.siberhus.ngai.guardian.dao.impl.RoleDao;
import com.siberhus.ngai.guardian.dao.impl.RolePermissionDao;
import com.siberhus.ngai.guardian.model.Role;
import com.siberhus.ngai.guardian.service.IRoleService;
import com.siberhus.ngai.service.AbstractCrudDaoService;

public class RoleService extends AbstractCrudDaoService<Role, Long> implements IRoleService{
	
	@InjectDao(implementation=RolePermissionDao.class)
	private IRolePermissionDao rolePermDao;
	
	@InjectDao(implementation=RoleDao.class)
	private IRoleDao roleDao;
	
	@Override
	public ICrudDao<Role, Long> getCrudDao() {
		return roleDao;
	}
	
	@Override
	public void delete(Role model) {
		
		super.delete(model);
		
		rolePermDao.deleteByRoleId(model.getId());
		
	}
	
}
