package com.siberhus.ngai.guardian.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.siberhus.ngai.dao.AbstractCrudDao;
import com.siberhus.ngai.guardian.dao.IRolePermissionDao;
import com.siberhus.ngai.guardian.model.RolePermission;
import com.siberhus.ngai.util.JpaQueryUtil;

public class RolePermissionDao extends AbstractCrudDao<RolePermission, Long> implements IRolePermissionDao{

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Class<RolePermission> getModelClass() {
		return RolePermission.class;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RolePermission> findUniqueByRoleIdList(List<Long> roleIdList) {
		if(roleIdList==null || roleIdList.size()==0){
			return new ArrayList<RolePermission>();
		}
		return (List)JpaQueryUtil.getResultListFromNamedQuery(entityManager, 
				"RolePermission.findUniqueByRoleIdList", roleIdList);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RolePermission> findUniqueByRoleIdListAndActionUri(
			List<Long> roleIdList, String actionUri) {
		if(roleIdList==null || roleIdList.size()==0){
			return new ArrayList<RolePermission>();
		}
		return (List)JpaQueryUtil.getResultListFromNamedQuery(entityManager, 
				"RolePermission.findUniqueByRoleIdListAndActionUri", roleIdList,actionUri);
	}

	@Override
	public void deleteByRoleId(Long roleId) {
		getEntityManager().createNamedQuery("RolePermission.deleteByRoleId")
			.setParameter(1, roleId).executeUpdate();
	}
	
}
