package com.haiersoft.ccli.api.dao;

import com.haiersoft.ccli.api.entity.ApiCustomerQueryApply;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jhy
 * 质押监管
 */
@Repository
public class FreightInquiryDao extends HibernateDao<TrayInfo, String> {

    public List<Map<String,Object>> findTrayInfoByStock(Map<String, String> data) {
        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("custCode",data.get("CustCode"));
        params.put("custName",data.get("CustName"));

        StringBuffer sb = new StringBuffer("SELECT api.ID as AID,tray.ID,tray.TRAY_ID,tray.STOCK_NAME,tray.GROSS_WEIGHT,tray.BILL_NUM,tray.CTN_NUM,tray.CARGO_NAME,tray.NOW_PIECE,tray.PLEDGE_PIECE,tray.PLEDGE_GROSS_WEIGHT,tray.UNITS,tray.IF_TRANSFER,tray.CARGO_LOCATION,tray.ENTER_TALLY_TIME ");
        sb.append(" FROM BIS_TRAY_INFO tray LEFT JOIN API_PLEDGE api ON api.TRAYINFO_ID = tray.ID WHERE 1=1 ");
        sb.append(" AND  STOCK_IN =  '"+params.get("custCode").toString()+"'");
        sb.append(" AND  STOCK_NAME = '"+params.get("custName").toString()+"'");
        if(data.get("goodsID") != null){
            sb.append(" AND  ID = '"+data.get("goodsID")+"'");
        }

        //限制库存大于0
        sb.append(" AND NOW_PIECE > 0 ");
        //联系单类型为 入库
//        sb.append(" AND CONTACT_TYPE = '1' ");
        SQLQuery sqlQuery = createSQLQuery(sb.toString());
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public int addLogBulk(Map<String,Object> params){
        String sql = "INSERT INTO BULKCOMM_LOG(ID,OPER_CODE, STOCK_IN, STOCK_NAME, TYPE,REQ_PARAM,REQ_DD,RES_PARAM) VALUES "+
         " ('"+params.get("ID")+"','"+(params.get("operaCode")==null?"":params.get("operaCode").toString())+"', '"+(params.get("CustCode")==null?"":params.get("CustCode"))+"', '"+params.get("CustName")+"', '"+(params.get("operaType")==null?"":params.get("operaType").toString())+"','"+params.get("params")+"',to_date('"+(params.get("reqDd")==null?"":params.get("reqDd"))+"','yyyy-mm-dd hh24:mi:ss'),'"+(params.get("reponse")==null?"":params.get("reponse").toString())+"')";
        SQLQuery sqlQuery = createSQLQuery(sql);
        int result = sqlQuery.executeUpdate();
        return result;
    }

    public int addAudit(Map<String,Object> params){
        String sql = "INSERT INTO AUDIT_RECORD(CUSTOMER_CODE,CUSTOMER_NAME,OPER_CODE,OPER_TYPE,MK_DD,EXEC_DD,OPER_STATE) VALUES "+
                " ('"+params.get("CustCode")+"','"+params.get("CustName")+"', '"+params.get("operaCode")+"', '"+params.get("operaType")+"', to_date('"+params.get("reqDd")+"','yyyy-mm-dd hh24:mi:ss'),to_date('"+params.get("execDd")+"','yyyy-mm-dd hh24:mi:ss'),'"+params.get("operStatue")+"')";
        SQLQuery sqlQuery = createSQLQuery(sql);
        int result = sqlQuery.executeUpdate();
        return result;
    }

    public  int addAuditDetail(Map<String,Object> params){
        String sql = "INSERT INTO AUDIT_RECORD_DETAIL(ZID,ID,TRAY_ID,API_PLEDGE_ID,OPER_QTY,OPER_WEIGHT,BILL_NUM,CTN_NUM) VALUES "+
                " ('"+params.get("ZID")+"','"+params.get("ID")+"','"+params.get("TRAY_ID")+"', '"+params.get("AID")+"', '"+params.get("pledgePiece")+"', '"+params.get("pledgeGrossWeight")+"','"+params.get("billNum")+"','"+params.get("ctnNum")+"')";
        SQLQuery sqlQuery = createSQLQuery(sql);
        int result = sqlQuery.executeUpdate();
        return result;
    }
}
