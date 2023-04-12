package com.haiersoft.ccli.base.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.base.entity.BaseWorkGroup;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.StringUtils;
@Repository
public class WorkGroupDao  extends HibernateDao<BaseWorkGroup, Integer> {

	public Page<BaseWorkGroup> searchKG(Page<BaseWorkGroup> page,BaseWorkGroup work) {
		Map<String, Object> params = new HashMap<String, Object>();

        StringBuffer sql = new StringBuffer(""
                + " SELECT "
        		+ " t.id as id,"
                + " t.PERSON AS person, "
                + " (select count(*) from base_work_group l where l.work_type = '2' and l.parent_id = t.id) as lhNum, "
                + " (select count(*) from base_work_group l where l.work_type = '3' and l.parent_id = t.id) as ccNum "
                + " FROM base_work_group t "
                + " WHERE t.parent_id=0 ");

        if (!StringUtils.isNull(work.getKgPerson()) ) {
            sql.append(" and t.person=:kg ");
            params.put("kg", work.getKgPerson());
        }
        if (!StringUtils.isNull(work.getLhPerson()) ) {
            sql.append(" and t.id = (select s.parent_id from base_work_group s where s.person=:lh and s.work_type='2') ");
            params.put("lh", work.getLhPerson());
        }
        if (!StringUtils.isNull(work.getCcPerson()) ) {
            sql.append(" and t.id = (select s.parent_id from base_work_group s where s.person=:cc and s.work_type='3') ");
            params.put("cc", work.getCcPerson());
        }
        
        //查询对象属性转换
        Map<String, Object> parm = new HashMap<String, Object>();
        parm.put("person", String.class);
        parm.put("lhNum", Integer.class);
        parm.put("ccNum", Integer.class);
        parm.put("id", Integer.class);

        return findPageSql(page, sql.toString(), parm, params);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findKgByName(String name) {
		StringBuffer sb=new StringBuffer("select person from base_work_group where person like :name and parent_id=0 and work_type='1' ");
		HashMap<String,Object> parme=new HashMap<String,Object>();
		parme.put("name", "%"+name+"%");
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findUsersByName(String name) {
		StringBuffer sb=new StringBuffer("select name as person from users where name like :name ");
		HashMap<String,Object> parme=new HashMap<String,Object>();
		parme.put("name", "%"+name+"%");
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	public Page<BaseWorkGroup> searchOther(Page<BaseWorkGroup> page,BaseWorkGroup obj) {
		Map<String, Object> params = new HashMap<String, Object>();

        StringBuffer sql = new StringBuffer(""
                + " SELECT "
                + " t.PERSON AS person  "
                + " FROM base_work_group t  "
                + " WHERE t.work_type=:type and "
                + " t.parent_id in (select a.id from base_work_group a where a.parent_id=0 and a.person=:person) ");
        params.put("type", obj.getWorkType());
        params.put("person", obj.getPerson());

        //查询对象属性转换
        Map<String, Object> parm = new HashMap<String, Object>();
        parm.put("person", String.class);
        return findPageSql(page, sql.toString(), parm, params);
	}
	
	public Page<BaseWorkGroup> searchAllOther(Page<BaseWorkGroup> page,BaseWorkGroup obj) {
		Map<String, Object> params = new HashMap<String, Object>();

        StringBuffer sql = new StringBuffer(""
                + " SELECT "
                + " t.Name AS person  "
                + " FROM users t ");

        //查询对象属性转换
        Map<String, Object> parm = new HashMap<String, Object>();
        parm.put("person", String.class);
        return findPageSql(page, sql.toString(), parm, params);
	}

	
}
