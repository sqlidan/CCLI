package com.haiersoft.ccli.wms.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntry;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName: PreEntryDao
 * @Description: 预报单DAO
 */
@Repository
public class PreEntryDao extends HibernateDao<BisPreEntry, String> {

    public List<Map<String,Object>> findOneBaseBounded(String putrecSeqno) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM  BASE_BOUNDED  where ACCOUNT_BOOK=:putrecSeqno ");
        HashMap<String,Object> parme=new HashMap<String,Object>();
        parme.put("putrecSeqno", putrecSeqno);
        SQLQuery sqlQuery = createSQLQuery(sql.toString(),parme);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public List<Map<String,Object>> getUserByName(String userName) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM  USERS  where NAME=:userName ");
        HashMap<String,Object> parme=new HashMap<String,Object>();
        parme.put("userName", userName);
        SQLQuery sqlQuery = createSQLQuery(sql.toString(),parme);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public List<Map<String,Object>> getDictDataByCode(String code) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM  BIS_PREENTRY_DICT_DATA  where CODE=:code and PARENT_ID != '0' order by SORT ASC");
        HashMap<String,Object> parme=new HashMap<String,Object>();
        parme.put("code", code);
        SQLQuery sqlQuery = createSQLQuery(sql.toString(),parme);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }
}
