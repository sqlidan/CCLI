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

    public List<Map<String,Object>> getDataByVehicleNo(String vehicleNo,String ioTypecd) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM (SELECT SEQ_NO,PASSPORT_NO,TOTAL_WT,IO_TYPECD,nvl(LOCKAGE,0) as LOCKAGE,CREATE_TIME FROM BIS_PASSPORT where VEHICLE_NO=:vehicleNo and IO_TYPECD=:ioTypecd order by CREATE_TIME desc ) x where ROWNUM = 1 ");
        HashMap<String,Object> parme=new HashMap<String,Object>();
        parme.put("vehicleNo", vehicleNo);
        parme.put("ioTypecd", ioTypecd);
        SQLQuery sqlQuery = createSQLQuery(sql.toString(),parme);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public List<Map<String,Object>> passButNotPassGate(String vehicleNo,String ioTypecd) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT * FROM ( ");
        sql.append(" SELECT SEQ_NO,PASSPORT_NO,IO_TYPECD,VEHICLE_WT,VEHICLE_FRAME_WT,CONTAINER_WT,TOTAL_GROSS_WT,TOTAL_NET_WT,TOTAL_WT ");
        sql.append(" FROM BIS_PASSPORT ");
        sql.append(" where VEHICLE_NO=:vehicleNo and IO_TYPECD=:ioTypecd and (state = 'B' or state = 'Y') and (nvl(LOCKAGE,0) = '0' or nvl(LOCKAGE,0) = '1') ");
        sql.append(" and CREATE_TIME BETWEEN SYSDATE - 3 AND SYSDATE ");
        sql.append(" order by CREATE_TIME desc ");
        sql.append(" ) x ");
        sql.append(" where ROWNUM = 1 ");
        HashMap<String,Object> parme=new HashMap<String,Object>();
        parme.put("vehicleNo", vehicleNo);
        parme.put("ioTypecd", ioTypecd);
        SQLQuery sqlQuery = createSQLQuery(sql.toString(),parme);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }
}
