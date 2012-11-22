package com.siberhus.ngaiready.admin.service.impl;

import com.siberhus.ngai.annot.InjectDao;
import com.siberhus.ngai.dao.ICrudDao;
import com.siberhus.ngai.service.AbstractCrudDaoService;
import com.siberhus.ngaiready.admin.dao.impl.FileItemDao;
import com.siberhus.ngaiready.admin.model.DocFileItem;
import com.siberhus.ngaiready.admin.service.IFileManagerService;

public class FileManagerService extends AbstractCrudDaoService<DocFileItem, Long> 
	implements IFileManagerService{
	
	@InjectDao(implementation=FileItemDao.class)
	private ICrudDao<DocFileItem, Long> fileItemDao;
	
	@Override
	public ICrudDao<DocFileItem, Long> getCrudDao() {
		return fileItemDao;
	}
	
	
}
