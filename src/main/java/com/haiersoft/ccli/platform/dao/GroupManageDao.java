package com.haiersoft.ccli.platform.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.platform.entity.GroupManage;
import org.hibernate.SQLQuery;
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
public class GroupManageDao extends HibernateDao<GroupManage, Integer>{
	
    private static String like(String str) {
        return "%" + str + "%";
    }
	/**
	 * 获取信息
	 * @param page 页面
	 * @param params 参数
	 * @return
	 */
	public Page<GroupManage> seachCustomsClearanceSql(Page<GroupManage> page, GroupManage customsClearance) {
		StringBuffer buffer=new StringBuffer();
		buffer.append("select t.* from PLATFORM_GROUP_MANAGE t where 1=1");

        Map<String, Object> queryParams = new HashMap<>();
        if (StringUtils.nonNull(customsClearance.getGroupName())) {
        	buffer.append(" and t.GROUP_NAME like :groupName");
            queryParams.put("groupName",like(customsClearance.getGroupName()));
        }
        


		buffer.append(" order by t.CREATED_TIME desc");
//        Map<String, Object> paramType = new HashMap<>();
//        paramType.put("cdNum", String.class);
//		return findPageSql(page, buffer.toString(),paramType, queryParams);
        return findPageSql(page, buffer.toString(),queryParams);
	}
	



	
}
