package com.siberhus.ngaiready.admin.dao;

import java.util.List;

import com.siberhus.ngai.dao.ICrudDao;
import com.siberhus.ngaiready.admin.model.TreeMenu;

public interface ITreeMenuDao extends ICrudDao<TreeMenu, Long> {
	
	public List<TreeMenu> getAllNoParentMenus();
	
	public TreeMenu findByLinkUrl(String url);
	
	public TreeMenu findByParentIdAndPosition(Long parentId, Integer position);
}
