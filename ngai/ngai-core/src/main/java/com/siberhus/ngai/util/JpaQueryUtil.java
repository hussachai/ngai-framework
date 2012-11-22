package com.siberhus.ngai.util;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * 
 * @author hussachai
 *
 */
public class JpaQueryUtil {
	
	// Query =======================================================
	public static Object getSingleResultFromQuery(EntityManager em, 
			String jpaQuery, Object... params){
		Query query = em.createQuery(jpaQuery);
		return JpaQueryUtil.getSingleResult(query, params);
	}
	
	public static Object getFirstResultFromQuery(EntityManager em, 
			String jpaQuery, Object... params){
		Query query = em.createQuery(jpaQuery);
		return JpaQueryUtil.getFirstResult(query, params);
	}
	
	public static Object getResultListFromQuery(EntityManager em, 
			String jpaQuery, Object... params){
		Query query = em.createQuery(jpaQuery);
		return JpaQueryUtil.getResultList(query, params);
	}
	
	// Named Query =======================================================
	public static Object getSingleResultFromNamedQuery(EntityManager em, 
			String queryName, Object... params){
		Query query = em.createNamedQuery(queryName);
		return JpaQueryUtil.getSingleResult(query, params);
	}
	
	public static Object getFirstResultFromNamedQuery(EntityManager em, 
			String queryName, Object... params){
		Query query = em.createNamedQuery(queryName);
		return JpaQueryUtil.getFirstResult(query, params);
	}
	
	public static Object getResultListFromNamedQuery(EntityManager em, 
			String queryName, Object... params){
		Query query = em.createNamedQuery(queryName);
		return JpaQueryUtil.getResultList(query, params);
	}
	
	// Native Query =======================================================
	public static Object getSingleResultFromNativeQuery(EntityManager em, 
			String nativeQuery, Object... params){
		Query query = em.createNativeQuery(nativeQuery);
		return JpaQueryUtil.getSingleResult(query, params);
	}
	
	public static Object getFirstResultFromNativeQuery(EntityManager em, 
			String nativeQuery, Object... params){
		Query query = em.createNativeQuery(nativeQuery);
		return JpaQueryUtil.getFirstResult(query, params);
	}
	
	public static Object getResultListFromNativeQuery(EntityManager em, 
			String nativeQuery, Object... params){
		Query query = em.createNativeQuery(nativeQuery);
		return JpaQueryUtil.getResultList(query, params);
	}
	
	//=====================================================================
	public static Object getSingleResult(Query query, Object... params){
		if(params!=null){
			for(int i=0;i<params.length;i++){
				query.setParameter(i+1, params[i]);
			}
		}
		return query.getSingleResult();
	}
	
	public static Object getFirstResult(Query query, Object... params){
		
		List<?> resultList = JpaQueryUtil.getResultList(query, params);
		if(resultList!=null && resultList.size()!=0){
			return resultList.get(0);
		}
		return null;
	}
	
	public static List<?> getResultList(Query query, Object... params){
		if(params!=null){
			for(int i=0;i<params.length;i++){
				query.setParameter(i+1, params[i]);
			}
		}
		return query.getResultList();
	}
	
}
