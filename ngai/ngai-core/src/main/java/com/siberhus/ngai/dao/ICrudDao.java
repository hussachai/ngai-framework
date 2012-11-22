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

import com.siberhus.ngai.QTO;
import com.siberhus.ngai.exception.DaoException;

/**
 * 
 * @author hussachai
 * @since 0.9
 * @param <T>
 * @param <ID>
 */
public interface ICrudDao<T, ID extends Serializable> extends IDao {
	
	public Class<T> getModelClass();
	
	/**
     * Retrieve an persisted object using the given id as primary key.
     *
     * @param id object's primary key
     * @return object or null if not found
     * 
     * @author hussachai
	 * @throws DaoException 
     * @since 0.9
     */
	public T get(ID id);
	
	public List<T> getAll();
	
	public Number countAll();
	
	public List<T> findByExample(T example);
	
	public List<T> findByQTO(QTO qto);
	
	public Number count(QTO qto);
	
	public T save(T model, Serializable user);
	
	public T save(T model);
	
	public void delete(ID id);
	
	public void delete(T model);
	
	public void wipe(T model);
	
	public void refresh(T model);
	
}
