package com.haiersoft.ccli.platform.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.platform.entity.PlatformUser;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author 
 *
 */
@Repository
public class PlatformUserDao extends HibernateDao<PlatformUser, String>{
	
    private static String like(String str) {
        return "%" + str + "%";
    }
	/**
	 * 获取信息
	 * @param page 页面
	 * @param params 参数
	 * @return
	 */
/*	public Page<PlatformUser> seachCustomsClearanceSql(Page<PlatformUser> page, PlatformUser customsClearance) {
		StringBuffer buffer=new StringBuffer();
		buffer.append("select t.* from PLATFORM_GROUP_MANAGE t where 1=1");

        Map<String, Object> queryParams = new HashMap<>();
        if (StringUtils.nonNull(customsClearance.getPlatformNo())) {
        	buffer.append(" and t.GROUP_NAME like :groupName");
            queryParams.put("groupName",like(customsClearance.getPlatformNo()));
        }
        


		buffer.append(" order by t.CREATED_TIME desc");
//        Map<String, Object> paramType = new HashMap<>();
//        paramType.put("cdNum", String.class);
//		return findPageSql(page, buffer.toString(),paramType, queryParams);
        return findPageSql(page, buffer.toString(),queryParams);
	}*/


	//获取client(无视大小写)
	public List<Map<String,Object>> findUser(String param) {

		StringBuffer sb=new StringBuffer("select id,name from users where lower(name) like lower(:name)  ");
		HashMap<String,Object> parme=new HashMap<String,Object>();
		parme.put("name", "%"+param+"%");



		SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}


	

	
}
