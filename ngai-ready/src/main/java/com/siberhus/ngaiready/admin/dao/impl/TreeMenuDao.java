package com.siberhus.ngaiready.admin.dao.impl;

import java.util.List;

import com.siberhus.ngai.dao.AbstractCrudDao;
import com.siberhus.ngai.util.JpaQueryUtil;
import com.siberhus.ngaiready.admin.dao.ITreeMenuDao;
import com.siberhus.ngaiready.admin.model.TreeMenu;

public class TreeMenuDao extends AbstractCrudDao<TreeMenu, Long> 
	implements ITreeMenuDao{

	@Override
	public Class<TreeMenu> getModelClass() {
		
		return TreeMenu.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TreeMenu> getAllNoParentMenus() {
		return (List<TreeMenu>)JpaQueryUtil.getResultListFromNamedQuery(getEntityManager(), 
				"TreeMenu.getAllNoParentMenus");
	}
	
	@Override
	public TreeMenu findByLinkUrl(String url) {
		return (TreeMenu)JpaQueryUtil.getFirstResultFromNamedQuery(getEntityManager(), 
			"TreeMenu.findByLinkUrl", url);
	}

	@Override
	public TreeMenu findByParentIdAndPosition(Long parentId, Integer position) {
		return (TreeMenu)JpaQueryUtil.getFirstResultFromNamedQuery(getEntityManager(), 
				"TreeMenu.findByParentIdAndPosition", parentId, position);
	}
	
}
