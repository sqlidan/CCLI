package com.haiersoft.ccli.common.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;


/**
 * 基础service 所有service继承该类
 * 提供基础的实体操作方法
 * 使用HibernateDao<T,PK>进行业务对象的CRUD操作,子类需重载getEntityDao()函数提供该DAO.
 * @author ty
 * @date 2014年8月20日 上午11:21:14
 * @param <T>
 * @param <PK>
 */
public abstract class BaseService<T,PK extends Serializable > {

	protected static final String SUCCESS = "success";

	protected static final String FAILED = "failed";

	protected static final String ERROR = "error";

	/**
	 * 子类需要实现该方法，提供注入的dao
	 * @return
	 */
	public abstract HibernateDao<T, PK> getEntityDao();

	@Transactional(readOnly = true)
	public T get(final PK id) {
		return getEntityDao().find(id);
	}

	@Transactional(readOnly = true)
	public T find(String propertyName,Object value) {
		return getEntityDao().findUniqueBy(propertyName, value);
	}

	@Transactional(readOnly = false)
	public void save(final T entity) {
		getEntityDao().save(entity);
	}

	@Transactional(readOnly = false)
	public void merge(final T entity) {
		getEntityDao().merge(entity);
	}

	@Transactional(readOnly = false)
	public void update(final T entity){
		getEntityDao().update(entity);
	}

	@Transactional(readOnly = false)
	public void delete(final T entity){
		getEntityDao().delete(entity);
	}

	@Transactional(readOnly = false)
	public void delete(final PK id){
		getEntityDao().delete(id);
	}

	@Transactional(readOnly = true)
	public List<T> getAll(){
		return getEntityDao().findAll();
	}

	@Transactional(readOnly = true)
	public List<T> getAll(Boolean isCache){
		return getEntityDao().findAll(isCache);
	}

	public List<T> search(final List<PropertyFilter> filters){
		return getEntityDao().find(filters);
	}

	public List<T> search(final List<PropertyFilter> filters,String orderByProperty, boolean isAsc){
		return getEntityDao().findByOrder(filters,orderByProperty,isAsc);
	}



	@Transactional(readOnly = true)
	public Page<T> search(final Page<T> page, final List<PropertyFilter> filters) {
		return getEntityDao().findPage(page, filters);
	}

	//注掉
	@Transactional(readOnly = true)
	public Page<T> search(final Page<T> page, final String hql,final Map<String, ?> values) {
		return getEntityDao().findPage(page, hql, values);
	}
	//注掉
	@Transactional(readOnly = true)
	public Page<T> search(final Page<T> page, final String hql,final Object... values) {
		return getEntityDao().findPage(page, hql, values);
	}
}
