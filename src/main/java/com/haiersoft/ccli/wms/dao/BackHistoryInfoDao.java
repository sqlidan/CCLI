package com.haiersoft.ccli.wms.dao;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.wms.entity.BackHistoryInfo;
/**
 * 
 * @ClassName: BackHistoryInfoDao
 * @Description: 回库记录DAO
 */

@Repository
public class BackHistoryInfoDao extends HibernateDao<BackHistoryInfo, Integer> {
	
	/*public List<BackHistoryInfo> findBackList(String loadingNum){
		
		String sql = "select lod.BILL_NUM,tr.CONTACT_NUM,tr.STOCK_IN,tr.STOCK_NAME,sum(lod.PIECE),sum(lod.NET_WEIGHT),sum(lod.GROSS_WEIGHT) " 
        +" from bis_loading_info lod " 
        +" left join bis_tray_info tr on tr.TRAY_ID=lod.TRAY_ID "
        +" where LOADING_TRUCK_NUM=loadingNum and loading_state='6' "
        +" group by lod.BILL_NUM,tr.CONTACT_NUM,tr.STOCK_IN,tr.STOCK_NAME ";
		
		
		return null;
	} */
	public Page<BackHistoryInfo> findBackPage(Page<BackHistoryInfo> page, BackHistoryInfo obj){
		
		Map<String, Object> params = new HashMap<>();
		String sql = "select lod.BILL_NUM as billNum,tr.CONTACT_NUM as ctnNum,tr.STOCK_IN as stockIn,tr.STOCK_NAME as stockName,sum(lod.PIECE) as nowPiece,sum(lod.NET_WEIGHT) as netWeight,sum(lod.GROSS_WEIGHT) as grossWeight" 
        +" from bis_loading_info lod " 
        +" left join bis_tray_info tr on tr.TRAY_ID=lod.TRAY_ID "
        +" where 1=1 ";
        
        if (isNotNull(obj.getLoadingNum())) {
            sql +=" and LOADING_TRUCK_NUM = :loadingNum ";
            params.put("loadingNum", obj.getLoadingNum());
        }
        
        sql+=" and loading_state='6' "
        +" group by lod.BILL_NUM,tr.CONTACT_NUM,tr.STOCK_IN,tr.STOCK_NAME ";
		
        Map<String, Object> paramType = new HashMap<>();
       
        paramType.put("billNum", String.class);
        paramType.put("ctnNum", String.class);
        paramType.put("stockIn", String.class);
        paramType.put("stockName", String.class);
        paramType.put("nowPiece", Integer.class);
        paramType.put("netWeight", Double.class);
        paramType.put("grossWeight", Double.class);
        

        return findPageSql(page, sql, paramType, params);
		
		
	} 
	
}
