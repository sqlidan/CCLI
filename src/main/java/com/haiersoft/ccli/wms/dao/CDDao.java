package com.haiersoft.ccli.wms.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.wms.entity.customsDeclaration.BsCustomsDeclaration;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName: PreEntryDao
 * @Description: 报关单DAO
 */
@Repository
public class CDDao extends HibernateDao<BsCustomsDeclaration, String> {

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

    public List<Map<String,Object>> getCount(String checkListNo) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT FOR_ID FROM BS_CUSTOMS_DECLARATION  where CHECK_LIST_NO=:checkListNo and STATE >= 0");
        HashMap<String,Object> parme=new HashMap<String,Object>();
        parme.put("checkListNo", checkListNo);
        SQLQuery sqlQuery = createSQLQuery(sql.toString(),parme);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

}
