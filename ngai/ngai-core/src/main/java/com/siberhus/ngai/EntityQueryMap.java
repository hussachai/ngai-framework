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
package com.siberhus.ngai;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.siberhus.ngai.core.Ngai;

/**
 * <p>
 * This map is not for use in business logic that need some key-value pair feature.<br/>
 * It's just the simple map that has special {@link EntityListMap#get(Object)} method.
 * </p>
 * <p> 
 * This map alone has nothing special but when combine it with EL (Expression Language) - <br/>
 * the magic will happen.
 * </p>
 * @see com.siberhus.ngai.service.DefaultDirectQueryService
 * @author hussachai
 * @since 0.9
 */
public class EntityQueryMap implements Map<String, List<Object>>{
	
	private HttpServletRequest request;
	
	/**
	 * 
	 */
	public EntityQueryMap(HttpServletRequest request){
		this.request = request;
	}
	
	/**
	 * 
	 * @return
	 */
	public EntityManager getEntityManager() {
		return Ngai.getEntityManager();
	}
	
	/**
	 * 
	 * <p>
	 * Pass query string as parameter then this method will create {@link javax.persistence.Query} <br/>
	 * object from that query, execute the query and return the entity list as the result.
	 * </p>
	 * 
	 * @param queryString (JPQL)
	 * @return List of entity
	 * @since 0.9
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> get(Object q) {
		
		String queryStr = q+" ";
		Query query = getEntityManager().createQuery(queryStr);
		String paramNames[] = StringUtils.substringsBetween(queryStr, ":", " ");
		if(paramNames!=null){
			for(String paramName : paramNames){
				query.setParameter(paramName, findAttribute(paramName));
			}
		}
		return query.getResultList();
	}
	
	protected Object findAttribute(String name){
		Object value = null;
		value = request.getAttribute(name);
		if(value != null) return value;
		value = request.getSession().getAttribute(name);
		if(value != null) return value;
		value = request.getSession().getServletContext().getAttribute(name);
		if(value != null) return value;
		return null;
	}
	
	@Override
	public void clear() {}

	@Override
	public boolean containsKey(Object key) {return false;}

	@Override
	public boolean containsValue(Object value) {return false;}

	@Override
	public Set<java.util.Map.Entry<String, List<Object>>> entrySet() {return null;}
	
	@Override
	public boolean isEmpty() {return false;}

	@Override
	public Set<String> keySet() {return null;}

	@Override
	public List<Object> put(String key, List<Object> value) {return null;}

	@Override
	public void putAll(Map<? extends String, ? extends List<Object>> m) {}

	@Override
	public List<Object> remove(Object key) {return null;}

	@Override
	public int size() {return 0;}

	@Override
	public Collection<List<Object>> values() {return null;}
	
}
