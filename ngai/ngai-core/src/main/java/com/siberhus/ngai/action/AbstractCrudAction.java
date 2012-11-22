/* Copyright 2009 Hussachai Puripunpinyo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.siberhus.ngai.action;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontBind;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.ValidationErrorHandler;

import org.apache.commons.lang.StringUtils;

import com.siberhus.ngai.QTO;
import com.siberhus.ngai.SortKey;
import com.siberhus.ngai.service.ICrudService;
import com.siberhus.ngai.view.datagrid.DisplayTagGridHandler;
import com.siberhus.ngai.view.datagrid.IGridHandler;

/**
 * 
 * @author hussachai
 *
 * @param <T>
 * @param <ID>
 */
public abstract class AbstractCrudAction<T,ID extends Serializable> 
	extends AbstractAction implements ValidationErrorHandler, IAuditableAction {
	
	public static final String MODEL_NAME = "model";//this value cannot be changed
	
	private static final String HASHED_QUERY_ATTR = "hashedQuery";
	private static final String FULL_LIST_SIZE_ATTR = "fullListSize";
	/**
	 * change this default value by overriding {@link #getPageSize()} method
	 */
	public static final int DEFAULT_PAGE_SIZE = 20;
	
	private List<ID> idList;
	
	private Object paginatedList;
	
	public abstract QTO createQueryTransferObjectForSearch();
	
	public abstract String getEditPage();
	
	public abstract String getViewPage();
	
	public abstract ICrudService<T,ID> getCrudService();
	
	public abstract T getModel();
	
	public abstract void setModel(T model);
	
	public abstract Class<T> getModelClass();
	
	public abstract Class<ID> getIdClass();
	
	public IUserAware<Serializable> getUserAware(){
		return DefaultUserAware.getInstance();
	}
	
	public IGridHandler getGridService(){
		return DisplayTagGridHandler.getInstance();
	}
	
	public String getSearchResultPage(){
		return getIndexPage();
	}
	
	@DefaultHandler
	@DontValidate
	public Resolution index() {
		
		removePageAttribute(FULL_LIST_SIZE_ATTR);
		removePageAttribute(HASHED_QUERY_ATTR);
		return forward(getPathPrefix()+getIndexPage());
	}
	
	@DontBind
	public Resolution cancel(){
		setModel(null);
		return index();
	}
	
	@DontValidate
	public Resolution delete() {
		getCrudService().delete(getModel());
		return search();
	}
	
	@DontValidate
	public Resolution deleteCheckedItems() {
		if(getIdList()==null){
			return search();
		}
		getCrudService().delete(getIdList());
		return search();
	}
	
	@DontValidate
	public Resolution create(){
		setModel(null);
		return forward(getPathPrefix()+getEditPage());
	}
	
	@DontValidate
	public Resolution edit(){
		if(getModel()==null){
			if(getIdList()!=null && getIdList().size()==1){
				setModel(getCrudService().get(getIdList().get(0)));
			}else{
				throw new IllegalArgumentException("Id:"+getIdList());
			}
		}
		getContext().getRequest().setAttribute(MODEL_NAME, getModel());
		return forward(getPathPrefix()+getEditPage());
	}
	
	@DontValidate
	public Resolution view(){
		getContext().getRequest().setAttribute(MODEL_NAME, getModel());
		return forward(getPathPrefix()+getViewPage());
	}
	
	public Resolution save() {
		
		Serializable user = getUserAware().getUser(getContext().getRequest());
		if(user!=null){
			getCrudService().save(getModel(), user);
		}else{
			getCrudService().save(getModel());
		}
		removePageAttribute(FULL_LIST_SIZE_ATTR);
		removePageAttribute(HASHED_QUERY_ATTR);
		
		return redirect(getClass());
	}
	
	@DontValidate
	public Resolution search(){
		
		/* convert long to int because displaytag support int type only */
		QTO qto =  createQueryTransferObjectForSearch();
		int objectsPerPage = getPageSize();
		
		Integer fullListSize = (Integer)getPageAttribute(FULL_LIST_SIZE_ATTR);
		String prevHashedQuery = (String)getPageAttribute(HASHED_QUERY_ATTR);
		String currHashedQuery = qto.getHashString();
		if(fullListSize==null || ! StringUtils.equals(prevHashedQuery,currHashedQuery)){
			fullListSize = getCrudService()
				.getSearchResultCount(qto).intValue();
			setPageAttribute(FULL_LIST_SIZE_ATTR, fullListSize);
		}
		setPageAttribute(HASHED_QUERY_ATTR, currHashedQuery);
		
		IGridHandler gridService = getGridService();
		int pageNumber = gridService.getPageNumber(getContext().getRequest());
		int firstResult = (pageNumber-1)*objectsPerPage;
		int maxResult = objectsPerPage;
		SortKey sortKey = gridService.getSortKey(getContext().getRequest());
		if(sortKey!=null){
			qto.addSortKey(sortKey);
		}
		qto.setFirstResult(firstResult).setMaxResult(maxResult);
		
		List<T> modelList = getCrudService().search(qto);
		
		paginatedList = gridService.getPaginatedList(getContext().getRequest(), modelList
				, fullListSize , pageNumber, objectsPerPage);
		
		return gridService.getResolution(getPathPrefix()+getSearchResultPage());
	}

	public List<ID> getIdList() {
		return idList;
	}

	public void setIdList(List<ID> idList) {
		this.idList = idList;
	}
	
	public Object getPaginatedList() {
		return paginatedList;
	}
	
	public void setPaginatedList(Object paginatedList) {
		this.paginatedList = paginatedList;
	}
	
	/**
	 * Specify the number of items per page to show in the grid
	 * 
	 * @return
	 * @author hussachai
	 * @since 0.9
	 */
	public int getPageSize(){
		return DEFAULT_PAGE_SIZE;
	}
	
	
	
}
