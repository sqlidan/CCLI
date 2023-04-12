package com.haiersoft.ccli.system.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.system.entity.Permission;


/**
 * 权限DAO
 * @author ty
 * @date 2015年1月13日
 */
@Repository
public class PermissionDao extends HibernateDao<Permission, Integer>{

	/**
	 * 查询用户拥有的权限
	 * @param userId 用户id
	 * @return 结果集合
	 */
	@SuppressWarnings("unchecked")
	public List<Permission> findPermissions(Integer userId){
		StringBuffer sb=new StringBuffer();
		sb.append("select distinct p.ID id,p.PID pid,p.NAME name,p.TYPE type,p.SORT sort,p.URL url,p.PERM_CODE permCode,p.ICON icon,p.STATE state from permission p ");
		sb.append("INNER JOIN role_permission rp on p.ID=rp.PERMISSION_ID ");
		sb.append("INNER JOIN role r ON r.id=rp.ROLE_ID ");
		sb.append("INNER JOIN user_role ur ON ur.ROLE_ID =rp.ROLE_ID ");
		sb.append("INNER JOIN users u ON u.id = ur.USER_ID ");
		sb.append("where u.id=?0 and p.IF_SHOW ='0' order by p.sort ");
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), userId);
		sqlQuery.addScalar("id",StandardBasicTypes.INTEGER );
		sqlQuery.addScalar("pid", StandardBasicTypes.INTEGER);
		sqlQuery.addScalar("name",StandardBasicTypes.STRING);
		sqlQuery.addScalar("type",StandardBasicTypes.STRING);
		sqlQuery.addScalar("sort",StandardBasicTypes.INTEGER);
		sqlQuery.addScalar("url",StandardBasicTypes.STRING);
		sqlQuery.addScalar("permCode",StandardBasicTypes.STRING);
		sqlQuery.addScalar("icon",StandardBasicTypes.STRING);
		sqlQuery.addScalar("sort",StandardBasicTypes.INTEGER);
		sqlQuery.setResultTransformer(Transformers.aliasToBean(Permission.class));
		//sqlQuery.setCacheable(true);
		return sqlQuery.list();
	}
	
	/**
	 * 查询用户拥有的权限
	 * @param userId 用户id
	 * @return 结果集合
	 */
	@SuppressWarnings("unchecked")
	public List<Permission> findPermissionsCW(Integer userId){
		StringBuffer sb=new StringBuffer();
		sb.append("select distinct p.ID id,p.PID pid,p.NAME name from permission p ");
		sb.append("INNER JOIN role_permission rp on p.ID=rp.PERMISSION_ID ");
		sb.append("INNER JOIN role r ON r.id=rp.ROLE_ID ");
		sb.append("INNER JOIN user_role ur ON ur.ROLE_ID =rp.ROLE_ID ");
		sb.append("INNER JOIN users u ON u.id = ur.USER_ID ");
		sb.append("where u.id=?0");
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), userId);
		sqlQuery.addScalar("id",StandardBasicTypes.INTEGER );
		sqlQuery.addScalar("pid", StandardBasicTypes.INTEGER);
		sqlQuery.addScalar("name",StandardBasicTypes.STRING);
		sqlQuery.setResultTransformer(Transformers.aliasToBean(Permission.class));
		//sqlQuery.setCacheable(true);
		return sqlQuery.list();
	}
	/**
	 * 查询所有的菜单
	 * @param userId
	 * @return 菜单集合
	 */
	@SuppressWarnings("unchecked")
	public List<Permission> findMenus(){
		StringBuffer sb=new StringBuffer();
		sb.append("select p.ID id,p.PID pid,p.NAME name,p.URL url,p.ICON icon,p.SORT sort,p.DESCRIPTION description from permission p ");
		sb.append("where p.TYPE='F' order by p.sort");
		SQLQuery sqlQuery=createSQLQuery(sb.toString());
		sqlQuery.addScalar("id",StandardBasicTypes.INTEGER );
		sqlQuery.addScalar("pid", StandardBasicTypes.INTEGER);
		sqlQuery.addScalar("name",StandardBasicTypes.STRING);
		sqlQuery.addScalar("url",StandardBasicTypes.STRING);
		sqlQuery.addScalar("icon",StandardBasicTypes.STRING);
		sqlQuery.addScalar("sort",StandardBasicTypes.INTEGER);
		sqlQuery.addScalar("description",StandardBasicTypes.STRING);
		sqlQuery.setResultTransformer(Transformers.aliasToBean(Permission.class));
		//sqlQuery.setCacheable(true);
		return sqlQuery.list();
	}
	
	
	/**
	 * 查询用户拥有的菜单权限
	 * @param userId
	 * @return 结果集合
	 */
	@SuppressWarnings("unchecked")
	public List<Permission> findMenus(Integer userId){
		StringBuffer sb=new StringBuffer();
		sb.append("select p.ID id,p.PID pid,p.NAME name,p.URL url,p.ICON icon,p.SORT sort,p.DESCRIPTION description from permission p ");
		sb.append("INNER JOIN role_permission rp on p.ID=rp.PERMISSION_ID ");
		sb.append("INNER JOIN role r ON r.id=rp.ROLE_ID ");
		sb.append("INNER JOIN user_role ur ON ur.ROLE_ID =rp.ROLE_ID ");
		sb.append("INNER JOIN users u ON u.id = ur.USER_ID ");
		sb.append("where p.TYPE='F' and u.id=?0 order by p.sort");
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), userId);
		sqlQuery.addScalar("id",StandardBasicTypes.INTEGER );
		sqlQuery.addScalar("pid", StandardBasicTypes.INTEGER);
		sqlQuery.addScalar("name",StandardBasicTypes.STRING);
		sqlQuery.addScalar("url",StandardBasicTypes.STRING);
		sqlQuery.addScalar("icon",StandardBasicTypes.STRING);
		sqlQuery.addScalar("sort",StandardBasicTypes.INTEGER);
		sqlQuery.addScalar("description",StandardBasicTypes.STRING);
		sqlQuery.setResultTransformer(Transformers.aliasToBean(Permission.class));
		//sqlQuery.setCacheable(true);
		return sqlQuery.list();
	}
	
	/**
	 * 查询菜单下的操作权限
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Permission> findMenuOperation(Integer pid){
		StringBuffer sb=new StringBuffer();
		sb.append("select p.ID id,p.NAME name,p.URL url,p.PERM_CODE permCode,p.DESCRIPTION description from permission p ");
		sb.append("where p.TYPE='O' and p.PID=?0 order by p.SORT");
		SQLQuery sqlQuery=createSQLQuery(sb.toString(),pid);
		sqlQuery.addScalar("id",StandardBasicTypes.INTEGER );
		sqlQuery.addScalar("name",StandardBasicTypes.STRING);
		sqlQuery.addScalar("url",StandardBasicTypes.STRING);
		sqlQuery.addScalar("permCode",StandardBasicTypes.STRING);
		sqlQuery.addScalar("description",StandardBasicTypes.STRING);
		sqlQuery.setResultTransformer(Transformers.aliasToBean(Permission.class));
		//sqlQuery.setCacheable(true);
		return sqlQuery.list();
	}
	
	/**
	 * 查询用户手持权限
	 * @param userId
	 * @return 结果集合
	 */
	@SuppressWarnings("unchecked")
	public List<Permission> findHand(String loginName){
		StringBuffer sb=new StringBuffer();
		sb.append("select p.PERM_CODE permCode from permission p ");
		sb.append("INNER JOIN role_permission rp on p.ID=rp.PERMISSION_ID ");
		sb.append("INNER JOIN role r ON r.id=rp.ROLE_ID ");
		sb.append("INNER JOIN user_role ur ON ur.ROLE_ID =rp.ROLE_ID ");
		sb.append("INNER JOIN users u ON u.id = ur.USER_ID ");
		sb.append("where u.LOGIN_NAME=?0 and p.IF_SHOW='1' order by p.sort");
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), loginName);
		sqlQuery.addScalar("permCode",StandardBasicTypes.STRING);
		sqlQuery.setResultTransformer(Transformers.aliasToBean(Permission.class));
		return sqlQuery.list();
	}

	//删除权限角色表
	public void deleterp(Integer id) {
		StringBuffer deleteSql = new StringBuffer("DELETE FROM ROLE_PERMISSION s WHERE ");
		deleteSql.append(" (s.permission_id = ?0) ");
		SQLQuery deleteQuery = createSQLQuery(deleteSql.toString(), id);
		deleteQuery.executeUpdate();
	}
	
	
	/**
	 * 查询用户拥有的权限
	 * @param userId 用户id
	 * @return 结果集合
	 */
	@SuppressWarnings("unchecked")
	public List<Permission> ifzq(Integer userId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("name", "展期报警");
		StringBuffer sb=new StringBuffer();
		sb.append("select p.ID id,p.PID pid,p.NAME name from permission p ");
		sb.append("INNER JOIN role_permission rp on p.ID=rp.PERMISSION_ID ");
		sb.append("INNER JOIN role r ON r.id=rp.ROLE_ID ");
		sb.append("INNER JOIN user_role ur ON ur.ROLE_ID =rp.ROLE_ID ");
		sb.append("INNER JOIN users u ON u.id = ur.USER_ID ");
		sb.append("where u.id=:userId and p.NAME=:name and p.IF_SHOW='0' order by p.id ");
		params.put("userId", userId);
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), params);
		sqlQuery.addScalar("id",StandardBasicTypes.INTEGER );
		sqlQuery.addScalar("pid", StandardBasicTypes.INTEGER);
		sqlQuery.addScalar("name",StandardBasicTypes.STRING);
		sqlQuery.setResultTransformer(Transformers.aliasToBean(Permission.class));
		//sqlQuery.setCacheable(true);
		return sqlQuery.list();
	}
}
