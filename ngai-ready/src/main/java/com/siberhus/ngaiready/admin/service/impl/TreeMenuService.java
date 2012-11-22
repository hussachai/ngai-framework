package com.siberhus.ngaiready.admin.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.siberhus.ngai.annot.InjectDao;
import com.siberhus.ngai.dao.ICrudDao;
import com.siberhus.ngai.service.AbstractCrudDaoService;
import com.siberhus.ngaiready.admin.dao.ITreeMenuDao;
import com.siberhus.ngaiready.admin.dao.impl.TreeMenuDao;
import com.siberhus.ngaiready.admin.model.TreeMenu;
import com.siberhus.ngaiready.admin.service.ITreeMenuService;

public class TreeMenuService extends AbstractCrudDaoService<TreeMenu, Long> 
	implements ITreeMenuService{
	
	@InjectDao(implementation=TreeMenuDao.class)
	private ITreeMenuDao treeMenuDao;
	
	@Override
	public ICrudDao<TreeMenu, Long> getCrudDao() {
		
		return treeMenuDao;
	}

	@Override
	public List<TreeMenu> getFirstLevelMenus() {
		
		return treeMenuDao.getAllNoParentMenus();
	}
	
	@Override
	public void move(Long id, Long newParentId, Integer newPosition) {
		getEntityManager().getTransaction().begin();// will be removed in 1.0
		TreeMenu movedMenu = treeMenuDao.get(id);
		if(movedMenu==null){
			throw new IllegalArgumentException("TreeMenu does not exist");
		}
//		TreeMenu prevMenu = treeMenuDao.findByParentIdAndPosition(newParentId, newPosition);
//		if(prevMenu!=null){
//			if(prevMenu.getPosition()!=null){
//				int pvPos = prevMenu.getPosition();
//				if(pvPos!=0){
//					prevMenu.setPosition(pvPos-1);
//				}
//			}else{
//				prevMenu.setPosition(0);
//			}
//		}
		movedMenu.setParent(treeMenuDao.get(newParentId));
		movedMenu.setPosition(newPosition);
		getEntityManager().getTransaction().commit();// will be removed in 1.0
	}

	@Override
	public List<TreeMenu> getParentUntilRootByUri(String uri) {
		TreeMenu treeMenu = treeMenuDao.findByLinkUrl(uri);
		if(treeMenu==null){
			return null;
		}
		List<TreeMenu> tmList = new ArrayList<TreeMenu>();
		tmList.add(treeMenu);
		
		while( (treeMenu = treeMenu.getParent()) != null){
			tmList.add(treeMenu);
		}
		return tmList;
	}
	
}
