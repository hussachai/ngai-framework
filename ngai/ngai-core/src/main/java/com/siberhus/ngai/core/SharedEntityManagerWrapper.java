package com.siberhus.ngai.core;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;

/**
 * <p>
 * Because most {@link EntityManager} implementation which provided by many providers is not thread-safe.<br/>
 * It may cause the concurrency problem when Service/DAO class is singleton or may be shared in serveral thread.<br/>
 * To deal with this issue the {@link EntityManager} must be extracted from individual thread store.<br/>
 * And SharedEntityManagerWrapper is come in to play.<br/>
 * 
 * </p>
 * @author hussachai
 * @since 0.9
 * 
 */
public class SharedEntityManagerWrapper implements EntityManager {
	
	private String persistenceUnit;
	
	public SharedEntityManagerWrapper(){}
	
	public SharedEntityManagerWrapper(String persistenceUnit){
		this.persistenceUnit = persistenceUnit;
	}
	
	@Override
	public void clear() {
		Ngai.getEntityManager(persistenceUnit).clear();
	}

	@Override
	public void close() {
		Ngai.getEntityManager(persistenceUnit).close();
	}
	
	@Override
	public boolean contains(Object entity) {
		return Ngai.getEntityManager(persistenceUnit).contains(entity);
	}

	@Override
	public Query createNamedQuery(String name) {
		return Ngai.getEntityManager(persistenceUnit).createNamedQuery(name);
	}
	
	@Override
	public Query createNativeQuery(String sqlString) {
		return Ngai.getEntityManager(persistenceUnit).createNativeQuery(sqlString);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Query createNativeQuery(String sqlString, Class resultClass) {
		return Ngai.getEntityManager(persistenceUnit).createNativeQuery(sqlString, resultClass);
	}
	
	@Override
	public Query createNativeQuery(String sqlString, String resultSetMapping) {
		return Ngai.getEntityManager(persistenceUnit).createNativeQuery(sqlString, resultSetMapping);
	}

	@Override
	public Query createQuery(String ejbqlString) {
		return Ngai.getEntityManager(persistenceUnit).createQuery(ejbqlString);
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey) {
		return Ngai.getEntityManager(persistenceUnit).find(entityClass, primaryKey);
	}

	@Override
	public void flush() {
		Ngai.getEntityManager(persistenceUnit).flush();
	}

	@Override
	public Object getDelegate() {
		return Ngai.getEntityManager(persistenceUnit).getDelegate();
	}

	@Override
	public FlushModeType getFlushMode() {
		return Ngai.getEntityManager(persistenceUnit).getFlushMode();
	}
	
	@Override
	public <T> T getReference(Class<T> entityClass, Object primaryKey) {
		return Ngai.getEntityManager(persistenceUnit).getReference(entityClass, primaryKey);
	}

	@Override
	public EntityTransaction getTransaction() {
		return Ngai.getEntityManager(persistenceUnit).getTransaction();
	}

	@Override
	public boolean isOpen() {
		return Ngai.getEntityManager(persistenceUnit).isOpen();
	}

	@Override
	public void joinTransaction() {
		Ngai.getEntityManager(persistenceUnit).joinTransaction();
	}

	@Override
	public void lock(Object entity, LockModeType lockMode) {
		Ngai.getEntityManager(persistenceUnit).lock(entity, lockMode);
	}

	@Override
	public <T> T merge(T entity) {
		return Ngai.getEntityManager(persistenceUnit).merge(entity);
	}

	@Override
	public void persist(Object entity) {
		Ngai.getEntityManager(persistenceUnit).persist(entity);
	}

	@Override
	public void refresh(Object entity) {
		Ngai.getEntityManager(persistenceUnit).refresh(entity);
	}

	@Override
	public void remove(Object entity) {
		Ngai.getEntityManager(persistenceUnit).remove(entity);
	}

	@Override
	public void setFlushMode(FlushModeType flushMode) {
		Ngai.getEntityManager(persistenceUnit).setFlushMode(flushMode);
	}
	
	
}





