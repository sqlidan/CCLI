package com.haiersoft.ccli.platform.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.platform.entity.VehicleQueue;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @author A0047794
 * @日期 2021/11/29
 * @描述
 */
@Repository
public class VehicleQueueDao extends HibernateDao<VehicleQueue,String> {
    public VehicleQueue getMinQueueTime() {

        String sql2="select {pq.*} from PLATFORM_QUEUE pq WHERE pq.STATUS_FLAG ='0' and rownum= 1 order by queue_time asc ";
        SQLQuery sqlQuery = createSQLQuery(sql2);
        sqlQuery.addEntity("pq",VehicleQueue.class);
        return (VehicleQueue) sqlQuery.uniqueResult();
    }

    public List<VehicleQueue> getTodayQueue() {

        String hql = "  from VehicleQueue where 1=1 and to_char(QUEUE_TIME,'yyyy-MM-dd') = '" + DateUtils.formatDate(new Date(), "yyyy-MM-dd")+"' order by queue_time desc";
        Query query = createQuery(hql);
      return query.list();

    }

    public int updatePLATFORM_CURRENT_INFO(VehicleQueue queue) {
        String billNo = "";
        if (queue.getInoutBoundFlag()!=null && queue.getInoutBoundFlag().trim().length() > 0){
            if ("1".equals(queue.getInoutBoundFlag())){
                billNo = getBillNo(queue.getYyid());
            }else{
                billNo = getBillNo2(queue.getYyid());
            }
        }
        StringBuffer sb= new StringBuffer();
        sb.append(" update PLATFORM_CURRENT_INFO set " +
                "QUEUE_STATUS = :queueStatus," +
                "OPERATION_FLAG = :operationFlag," +
                "CAR_NUMBER = :carNumber," +
                "BILL_NO = :billNo," +
                "CONTAINER_NO = :containerNo," +
                "YYID = :yyid," +
                "UPDATED_TIME =:updatedTime where PLATFORM_NAME = :platformName ");
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("queueStatus",queue.getStatusFlag());
        queryParams.put("operationFlag",queue.getStatusFlag());
        queryParams.put("carNumber",queue.getPlatNo());
        queryParams.put("billNo",billNo);
        queryParams.put("containerNo",queue.getContainerNo());
        queryParams.put("yyid",queue.getYyid());
        queryParams.put("updatedTime",new Date());
        queryParams.put("platformName",queue.getPlatformName());

        SQLQuery sqlQuery = createSQLQuery(sb.toString(),queryParams);
        return sqlQuery.executeUpdate();

    }
    public String getBillNo(String id) {
        String billNo = "";
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT BILL_NO FROM PLATFORM_RESERVATION_INBOUND  where ID=:id ");
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id", id);
        SQLQuery sqlQuery=createSQLQuery(sql.toString(), params);
        List<Map<String,Object>> mapList = new ArrayList<>();
        mapList = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        if (mapList!=null && mapList.size() > 0){
            billNo = mapList.get(0).get("BILL_NO").toString();
        }
        return billNo;
    }
    public String getBillNo2(String id) {
        String billNo = "";
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT BILL_NO FROM PLATFORM_RESERVATION_OUTBOUND  where ID=:id ");
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id", id);
        SQLQuery sqlQuery=createSQLQuery(sql.toString(), params);
        List<Map<String,Object>> mapList = new ArrayList<>();
        mapList = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        if (mapList!=null && mapList.size() > 0){
            billNo = mapList.get(0).get("BILL_NO").toString();
        }
        return billNo;
    }
}
