package com.siberhus.ngai.guardian.dao.impl;

import com.siberhus.ngai.dao.AbstractCrudDao;
import com.siberhus.ngai.guardian.dao.IRoleDao;
import com.siberhus.ngai.guardian.model.Role;

public class RoleDao extends AbstractCrudDao<Role, Long> implements IRoleDao{
	
	@Override
	public Class<Role> getModelClass() {
		return Role.class;
	}
	
	
}
