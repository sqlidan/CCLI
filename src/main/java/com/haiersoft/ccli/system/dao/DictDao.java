package com.haiersoft.ccli.system.dao;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.system.entity.Dict;

/**
 * 字典DAO
 * @author ty
 * @date 2015年1月13日
 */
@Repository
public class DictDao extends HibernateDao<Dict, Integer>{
	   /**
	    * 根据类型以及vlaue查出字典对象
	    * @param type
	    * @param value
	    * @return
	    */
	   public Dict findDict(String type,String value){
    	   return findUnique(Restrictions.and(Restrictions.eq("type", type),Restrictions.eq("value", value)));
       }
}
