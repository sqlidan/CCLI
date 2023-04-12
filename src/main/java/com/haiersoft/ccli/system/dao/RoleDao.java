package com.haiersoft.ccli.system.dao;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.system.entity.Role;
/**
 * 角色DAO
 * @author ty
 * @date 2015年1月13日
 */
@Repository
public class RoleDao extends HibernateDao<Role, Integer>{

	//删除权限角色表
	public void deleterp(Integer id) {
		StringBuffer deleteSql = new StringBuffer("DELETE FROM ROLE_PERMISSION s WHERE ");
		deleteSql.append(" (s.role_id = ?0) ");
		SQLQuery deleteQuery = createSQLQuery(deleteSql.toString(), id);
		deleteQuery.executeUpdate();
	}

}
