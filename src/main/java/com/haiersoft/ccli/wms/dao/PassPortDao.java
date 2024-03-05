package com.haiersoft.ccli.wms.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.wms.entity.passPort.BisPassPort;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntry;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName: PassPortDao
 */
@Repository
public class PassPortDao extends HibernateDao<BisPassPort, String> {
    public List<Map<String,Object>> getDictDataByCode(String code) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM  BIS_PREENTRY_DICT_DATA  where CODE=:code and PARENT_ID != '0' order by SORT ASC");
        HashMap<String,Object> parme=new HashMap<String,Object>();
        parme.put("code", code);
        SQLQuery sqlQuery = createSQLQuery(sql.toString(),parme);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public List<Map<String,Object>> getDataByVehicleNo(String vehicleNo) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM (SELECT TOTAL_WT,CREATE_TIME FROM BIS_PASSPORT where VEHICLE_NO=:vehicleNo order by CREATE_TIME desc ) x where ROWNUM = 1 ");
        HashMap<String,Object> parme=new HashMap<String,Object>();
        parme.put("vehicleNo", vehicleNo);
        SQLQuery sqlQuery = createSQLQuery(sql.toString(),parme);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }
}
