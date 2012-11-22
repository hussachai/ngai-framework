package com.siberhus.ngai.guardian.dao;

import java.util.List;

import com.siberhus.ngai.dao.ICrudDao;
import com.siberhus.ngai.guardian.model.RolePermission;

public interface IRolePermissionDao extends ICrudDao<RolePermission, Long>{
	
	public List<RolePermission> findUniqueByRoleIdList(List<Long> roleIdList);
	
	public List<RolePermission> findUniqueByRoleIdListAndActionUri(List<Long> roleIdList, String actionUri);
	
	public void deleteByRoleId(Long roleId);
	
}
