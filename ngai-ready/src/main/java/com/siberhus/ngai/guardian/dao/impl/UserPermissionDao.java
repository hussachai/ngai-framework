package com.siberhus.ngai.guardian.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.siberhus.ngai.dao.AbstractCrudDao;
import com.siberhus.ngai.guardian.dao.IUserPermissionDao;
import com.siberhus.ngai.guardian.model.UserPermission;
import com.siberhus.ngai.util.JpaQueryUtil;

public class UserPermissionDao extends AbstractCrudDao<UserPermission, Long> implements IUserPermissionDao{

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Class<UserPermission> getModelClass() {
		return UserPermission.class;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<UserPermission> findByUserId(Long userId) {
		return (List)JpaQueryUtil.getResultListFromNamedQuery(
				entityManager, "UserPermission.findByUserId", userId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<UserPermission> findByUserIdAndActionUri(Long userId,
			String actionUri) {
		return (List)JpaQueryUtil.getResultListFromNamedQuery(
				entityManager, "UserPermission.findByUserIdAndActionUri", userId, actionUri);
	}
	
	@Override
	public void deleteByUserId(Long userId) {
		getEntityManager().createNamedQuery("UserPermission.deleteByUserId")
			.setParameter(1, userId).executeUpdate();
	}	
	
}
