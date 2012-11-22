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
package com.siberhus.ngai.core;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import net.sourceforge.stripes.util.ReflectUtil;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import com.siberhus.ngai.QTO;
import com.siberhus.ngai.TemporalObject;
import com.siberhus.ngai.exception.NgaiRuntimeException;
import com.siberhus.ngai.model.IAuditableModel;
import com.siberhus.ngai.model.IModel;

public final class CrudHelper {
	
	private static final Map<Class<?>, EntityInfo> ENTITY_INFO_CACHE 
		= new ConcurrentHashMap<Class<?>, EntityInfo>();
	
	private static final class EntityInfo {
		private String entityName;
		private Set<Field> fieldSet = new HashSet<Field>();
		public String getEntityName() {
			return entityName;
		}
		public void setEntityName(String entityName) {
			this.entityName = entityName;
		}
		public Set<Field> getFieldSet() {
			return fieldSet;
		}
		public void setFieldSet(Set<Field> fieldSet) {
			this.fieldSet = fieldSet;
		}
		
	}
	
	private CrudHelper(){}
	
	/**
	 * 
	 * @param entityClass
	 * @return
	 */
	private synchronized final static EntityInfo getEntityInfo(Class<?> entityClass){
		
		EntityInfo entityInfo = ENTITY_INFO_CACHE.get(entityClass);
		if(entityInfo!=null){
			return entityInfo;
		}
		entityInfo = new EntityInfo();
		Entity entityAnnot = (Entity)entityClass.getAnnotation(Entity.class);
		if(!"".equals(entityAnnot.name())){
			entityInfo.setEntityName(entityAnnot.name());
		}else{
			entityInfo.setEntityName(entityClass.getSimpleName());
		}
		Collection<Field> entityFields = ReflectUtil.getFields(entityClass);
		for(Field field : entityFields){
			if(Modifier.isStatic(field.getModifiers())){
				continue;
			}
			if(field.getName().equals("id")){
				continue;
			}
			if(!field.isAccessible()){
				field.setAccessible(true);
			}
			entityInfo.getFieldSet().add(field);
		}
		ENTITY_INFO_CACHE.put(entityClass, entityInfo);
		return entityInfo;
	}
	
	public final static Number count(EntityManager em, QTO qto){
		String queryString = StringUtils.trim(qto.getQueryString());
		if(StringUtils.startsWithIgnoreCase(queryString, "select")){
			throw new UnsupportedOperationException("Query string cannot begin with \"select\"." +
					"It should start with \"from\"");
		}
		return ((Number)CrudHelper.createQueryObject(em, "select count(*) "
				+queryString,qto.getParameterList())
				.getSingleResult()).intValue();
	}
	
	@SuppressWarnings("unchecked")
	public final static List<Object> findByQTO(EntityManager em, QTO qto){
		Query query = createQueryObject(em, qto.buildQueryString()
				,qto.getParameterList());
		if(qto.getFirstResult()!=null){
			query.setFirstResult(qto.getFirstResult());
		}
		if(qto.getMaxResult()!=null){
			query.setMaxResults(qto.getMaxResult());
		} 
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public final static List<Object> findByExample(EntityManager em, Class<?> entityClass, Object example) {
		if(em == null){
			throw new IllegalArgumentException("EntityManager is null");
		}
		EntityInfo entityInfo = getEntityInfo(entityClass);
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder(255);
		sql.append("from ").append(entityInfo.getEntityName()).append(" e where 1=1 ");
		try{
			for(Field field : entityInfo.getFieldSet()){
				sql.append("and e.").append(field.getName()).append("=? ");
				params.add(field.get(example));
			}
		}catch(IllegalAccessException e){
			throw new NgaiRuntimeException("Unable to get field value from example model: "
					+example, e);
		}
		Query query = em.createQuery(sql.toString());
		for(int i=0;i<params.size();i++){
			query.setParameter(i+1, params.get(i));
		}
		return query.getResultList();
	}
	
	public final static Query createQueryObject(EntityManager em, String queryString, List<Object> paramList){
		if(em == null){
			throw new IllegalArgumentException("EntityManager is null");
		}
		Query query = em.createQuery(queryString);
		for(int i=0; i< paramList.size(); i++){
			Object param = paramList.get(i);
			if(param instanceof TemporalObject){
				TemporalObject tempParam = ((TemporalObject)param);
				query.setParameter(i+1,tempParam.getTemporalObject()
						,tempParam.getTemporalType());
			}else{
				query.setParameter(i+1, param);
			}
		}
		return query;
	}
	
	@SuppressWarnings("unchecked")
	public final static Object save(EntityManager em, Object model, Serializable user) {
		if(model instanceof IAuditableModel){
//			if(user==null){
//				throw new AuditException("User model is required due to " +
//						"model object is instance of IAuditable");
//			}
			IAuditableModel<Serializable,Serializable> auditableModel = (IAuditableModel)model;
			if(auditableModel.isNew()){
				auditableModel.setCreatedBy(user);
				auditableModel.setCreatedAt(new Date());
			}else{
				auditableModel.setLastModifiedBy(user);
				auditableModel.setLastModifiedAt(new Date());
			}
		}
		return CrudHelper.save(em, model);
	}
	
	@SuppressWarnings("unchecked")
	public final static Object save(EntityManager em, Object model) {
		if(em == null){
			throw new IllegalArgumentException("EntityManager is null");
		}
		if(model instanceof IModel){
			if(((IModel<Serializable>)(model)).isNew()){
				em.persist(model);
				return model;
			}
		}else{
			Object id = null;
			try {
				id = PropertyUtils.getProperty(model, "id");
			} catch (Exception e) {
				throw new NgaiRuntimeException("Unable to find property 'id' fom model: "
						+model, e);
			}
			if(id == null){
				em.persist(model);
				return model;
			}
		}
		return em.merge(model);
	}
	
	@SuppressWarnings("unchecked")
	public final static List<Object> getAll(EntityManager em, Class<?> entityClass) {
		if(em == null){
			throw new IllegalArgumentException("EntityManager is null");
		}
		EntityInfo entityInfo = CrudHelper.getEntityInfo(entityClass);
		String jpql = "from "+entityInfo.getEntityName();
		return em.createQuery(jpql).getResultList();
	}
	
	
	public final static Number countAll(EntityManager em, Class<?> entityClass) {
		if(em == null){
			throw new IllegalArgumentException("EntityManager is null");
		}
		EntityInfo entityInfo = CrudHelper.getEntityInfo(entityClass);
		String jpql = "select count(*) from "+entityInfo.getEntityName();
		return (Number)em.createQuery(jpql).getSingleResult();
	}
	
}
