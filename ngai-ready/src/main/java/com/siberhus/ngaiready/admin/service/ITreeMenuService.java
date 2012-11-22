package com.siberhus.ngaiready.admin.service;

import java.util.List;

import com.siberhus.ngai.service.ICrudService;
import com.siberhus.ngaiready.admin.model.TreeMenu;

public interface ITreeMenuService extends ICrudService<TreeMenu, Long>{
	
	public List<TreeMenu> getFirstLevelMenus();
	
	public void move(Long id, Long newParentId, Integer newPosition);
	 
	public List<TreeMenu> getParentUntilRootByUri(String uri);
	
}
