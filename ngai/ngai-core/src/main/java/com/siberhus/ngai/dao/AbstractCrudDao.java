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
package com.siberhus.ngai.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import com.siberhus.ngai.QTO;
import com.siberhus.ngai.core.CrudHelper;
import com.siberhus.ngai.core.Ngai;
import com.siberhus.ngai.model.IUnremovableModel;


public abstract class AbstractCrudDao<T, ID extends Serializable> implements ICrudDao<T, ID>{
	
	public AbstractCrudDao(){}
	
	public EntityManager getEntityManager(){
		return Ngai.getEntityManager();
	}
	
	@Override
	public void delete(ID id) {
		delete(get(id));
	}
	
	@Override
	public void delete(T model) {
		
		if(model instanceof IUnremovableModel){
			((IUnremovableModel)model).setActive(true);
		}else{
			getEntityManager().remove(model);
		}
	}
	
	@Override
	public void wipe(T model){
		getEntityManager().remove(model);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByExample(T example) {
		
		return (List<T>)CrudHelper.findByExample(getEntityManager(),getModelClass(), example);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByQTO(QTO qto) {
		
		return (List<T>)CrudHelper.findByQTO(getEntityManager(), qto);
	}
	
	public Number count(QTO qto) {
		
		return CrudHelper.count(getEntityManager(), qto);
	}
	
	@Override
	public T get(ID id) {
		
		return getEntityManager().find(getModelClass(), id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> getAll() {
		
		return (List<T>)CrudHelper.getAll(getEntityManager(), getModelClass());
	}
	
	@Override
	public Number countAll() {
		
		return CrudHelper.countAll(getEntityManager(),getModelClass());
	}
	
	@Override
	public void refresh(T model) {
		
		getEntityManager().refresh(model);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T save(T model, Serializable user) {
		
		return (T)CrudHelper.save(getEntityManager(), model, user);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T save(T model) {
		
		return (T)CrudHelper.save(getEntityManager(), model);
	}
}
