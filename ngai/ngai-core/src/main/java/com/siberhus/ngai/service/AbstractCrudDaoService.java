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
import com.siberhus.ngai.core.NgaiCoreInterceptor;
import com.siberhus.ngai.dao.ICrudDao;

public abstract class AbstractCrudDaoService<T, ID extends Serializable> implements ICrudService<T, ID> {
	
	public abstract ICrudDao<T, ID> getCrudDao();
	
	public EntityManager getEntityManager(){
		return NgaiCoreInterceptor.getEntityManager();
	}
	
	@Override
	public T get(ID id){
		return getCrudDao().get(id);
	}
	
	@Override
	public List<T> getAll(){
		return getCrudDao().getAll();
	}
	
	@Override
	public void save(T model, Serializable user) {
		
		getEntityManager().getTransaction().begin();//TODO: Manual Trx will be removed in 1.0
		
		getCrudDao().save(model, user);
		
		getEntityManager().getTransaction().commit();//TODO: Manual Trx will be removed in 1.0
	}
	
	@Override
	public void save(T model){
		
		getEntityManager().getTransaction().begin();//TODO: Manual Trx will be removed in 1.0
		
		getCrudDao().save(model);
		
		getEntityManager().getTransaction().commit();//TODO: Manual Trx will be removed in 1.0
	}
	
	@Override
	public Number getSearchResultCount(QTO qto){
		return getCrudDao().count(qto);
	}
	
	@Override
	public List<T> search(QTO qto){
		return getCrudDao().findByQTO(qto);
	}
	
	@Override
	public void delete(ID id){
		
		getEntityManager().getTransaction().begin();//TODO: Manual Trx will be removed in 1.0
		
		getCrudDao().delete(id);
		
		getEntityManager().getTransaction().commit();//TODO: Manual Trx will be removed in 1.0
	}
	
	@Override
	public void delete(T model){
		
		getEntityManager().getTransaction().begin();//TODO: Manual Trx will be removed in 1.0
		
		getCrudDao().delete(model);
		
		getEntityManager().getTransaction().commit();//TODO: Manual Trx will be removed in 1.0
	}
	
	@Override
	public void wipe(T model){
		
		getEntityManager().getTransaction().begin();//TODO: Manual Trx will be removed in 1.0
		
		getCrudDao().wipe(model);
		
		getEntityManager().getTransaction().commit();//TODO: Manual Trx will be removed in 1.0
	}
	
	@Override
	public void delete(List<ID> idList){
		
		getEntityManager().getTransaction().begin();//TODO: Manual Trx will be removed in 1.0
		
		for(ID id : idList){
			getCrudDao().delete(id);
		}
		
		getEntityManager().getTransaction().commit();//TODO: Manual Trx will be removed in 1.0
	}
	
}
