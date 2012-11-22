package com.siberhus.ngaiready.admin.dao.impl;

import com.siberhus.ngai.dao.AbstractCrudDao;
import com.siberhus.ngaiready.admin.model.DocFileItem;

public class FileItemDao extends AbstractCrudDao<DocFileItem, Long>{

	@Override
	public Class<DocFileItem> getModelClass() {
		return DocFileItem.class;
	}

	
}
