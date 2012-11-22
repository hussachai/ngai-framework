package com.siberhus.ngai.guardian.dao;

import java.util.List;

import com.siberhus.ngai.dao.ICrudDao;
import com.siberhus.ngai.guardian.model.UserPermission;


public interface IUserPermissionDao extends ICrudDao<UserPermission, Long> {
	
	public List<UserPermission> findByUserId(Long userId);
	
	
	public List<UserPermission> findByUserIdAndActionUri(Long userId, String actionUri);
	
	public void deleteByUserId(Long userId);
	
}
