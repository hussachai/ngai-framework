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
package com.siberhus.ngai.service;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import com.siberhus.ngai.QTO;
import com.siberhus.ngai.core.CrudHelper;
import com.siberhus.ngai.core.NgaiCoreInterceptor;
import com.siberhus.ngai.model.IUnremovableModel;

public abstract class AbstractCrudService<T, ID extends Serializable> implements ICrudService<T, ID> {
	
	public abstract Class<T> getModelClass();
	
	public AbstractCrudService(){}
	
	public EntityManager getEntityManager(){
		
		return NgaiCoreInterceptor.getEntityManager();
	}

	@Override
	public T get(ID id){
		
		return getEntityManager().find(getModelClass(), id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> getAll(){
		return getEntityManager().createQuery("from "+getModelClass().getName())
			.getResultList();
	}
	
	@Override
	public void save(T model, Serializable user){
		
		getEntityManager().getTransaction().begin();//TODO: Manual Trx will be removed in 1.0
		
		CrudHelper.save(getEntityManager(), model, user);
		
		getEntityManager().getTransaction().commit();//TODO: Manual Trx will be removed in 1.0
	}
	
	@Override
	public void save(T model){
		
		getEntityManager().getTransaction().begin();//TODO: Manual Trx will be removed in 1.0
		
		CrudHelper.save(getEntityManager(), model);
		
		getEntityManager().getTransaction().commit();//TODO: Manual Trx will be removed in 1.0
	}
	
	@Override
	public Number getSearchResultCount(QTO qto){
		
		return CrudHelper.count(getEntityManager(),qto);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> search(QTO qto){
		
		return (List<T>)CrudHelper.findByQTO(getEntityManager(),qto);
	}
	
	@Override
	public void delete(ID id){
		
		getEntityManager().getTransaction().begin();//TODO: Manual Trx will be removed in 1.0
		
		Object model = getEntityManager().find(getModelClass(), id);
		if(model!=null){
			if(model instanceof IUnremovableModel){
				((IUnremovableModel)model).setActive(false);
			}else{
				getEntityManager().remove(model);
			}
		}
		
		getEntityManager().getTransaction().commit();//TODO: Manual Trx will be removed in 1.0
	}
	
	@Override
	public void delete(List<ID> idList){
		
		getEntityManager().getTransaction().begin();//TODO: Manual Trx will be removed in 1.0
		
		if(idList==null){
			return;
		}
		boolean isUnremovable = IUnremovableModel.class.isAssignableFrom(getModelClass());
		for(Object id : idList){
			Object model = getEntityManager().find(getModelClass(), id);
			if(model!=null){
				if(isUnremovable){
					((IUnremovableModel)model).setActive(false);
				}else{
					getEntityManager().remove(model);
				}
			}
		}
		
		getEntityManager().getTransaction().commit();//TODO: Manual Trx will be removed in 1.0
	}
	
	@Override
	public void delete(T model){
		
		getEntityManager().getTransaction().begin();//TODO: Manual Trx will be removed in 1.0
		if(model instanceof IUnremovableModel){
			((IUnremovableModel)model).setActive(false);
		}else{
			getEntityManager().remove(model);
		}
		getEntityManager().getTransaction().commit();//TODO: Manual Trx will be removed in 1.0
	}
	
	@Override
	public void wipe(T model){
		getEntityManager().getTransaction().begin();//TODO: Manual Trx will be removed in 1.0
		getEntityManager().remove(model);
		getEntityManager().getTransaction().commit();//TODO: Manual Trx will be removed in 1.0
	}
	
}
