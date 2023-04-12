package com.haiersoft.ccli.wms.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.wms.entity.Monitor;

/**
 * 
 * @author pyl
 * @ClassName: MonitorDao
 * @Description: 入库联系单DAO
 */
@Repository
public class MonitorDao extends HibernateDao<Monitor, String>{
	
	
	/**
	 * 
	 * @author GLZ
	 * @Description: 分页查询
	 * @param page
	 * @return
	 * @throws
	 */
	public Page<Monitor> searchMonitor(Page<Monitor> page, Monitor monitor){
		Map<String,Object> params = new HashMap<String, Object>();
		StringBuffer sb=new StringBuffer();
		sb.append(" SELECT                ");
		sb.append("   temp.asnNum,        ");
		sb.append("   temp.ctnNum,        ");
		sb.append("   temp.cargoName,     ");
		sb.append("   temp.clientName,    ");
		sb.append("   temp.billNum,       ");
		sb.append("   temp.linkId,        ");
		sb.append("   temp.operator1,     ");
		sb.append("   temp.operator2,     ");
		sb.append("   temp.strTime,       ");
		sb.append("   temp.endTime,       ");
		sb.append("   temp.ccTime,        ");
		sb.append("   temp.linkCreater,   ");
		sb.append("   temp.planNum,       ");
		sb.append("   temp.shNum,         ");
		sb.append("   temp.sjNum,         ");
		sb.append("   temp.planWeight,    ");
		sb.append("   temp.shWeight,      ");
		sb.append("   temp.sjWeight,      ");
		sb.append("   temp.allNum,        ");
		sb.append("   temp.allWeight,     ");
		sb.append("   temp.jobState       ");
		sb.append(" FROM(                 ");
		sb.append(" 	SELECT                                                                                    ");
		sb.append(" 	    ob.*, (ob.shNum + ob.sjNum) AS allNum,                                                ");
		sb.append(" 		(ob.shWeight + ob.sjWeight) AS allWeight,                                             ");
		sb.append(" 		(                                                                                     ");
		sb.append(" 		  CASE                                                                                ");
		sb.append("               WHEN (ob.bisType = '1' AND ob.jobType = '1') THEN '0'                           ");
		sb.append(" 			  WHEN (ob.shNum < ob.planNum AND ob.sjNum = 0 AND ob.shNum != 0) THEN '2'        ");
		sb.append(" 			  WHEN (ob.shNum = ob.planNum AND ob.sjNum = 0 AND ob.shNum != 0) THEN '3'        ");
		sb.append(" 			  WHEN (ob.shNum > 0 AND ob.sjNum > 0) THEN '4'                                   ");
		sb.append(" 			  WHEN (ob.shNum = 0 AND ob.sjNum > 0) THEN '5'                                   ");
		sb.append(" 			  ELSE '00'                                                                       ");
		sb.append(" 		  END                                                                                 ");
		sb.append(" 		  ) AS jobState                                                                       ");
		sb.append(" 	FROM                                                                          ");
		sb.append(" 		(                                                                         ");
		sb.append(" 		  SELECT                                                                ");
		sb.append(" 			ai.asn_id AS asnNum,                                              ");
		sb.append(" 			MIN (A .CTN_NUM) AS ctnNum,                                       ");
		sb.append(" 			MIN (ai.cargo_name) AS cargoName,                                ");
		sb.append(" 			min (A.stock_name) AS clientName,                                                   ");
		sb.append(" 			MAX (A.bill_num) AS billNum,                                                        ");
		sb.append(" 			MAX (A.link_id) AS linkId,                                                          ");
		sb.append("             MIN (car.BIS_TYPE) AS bisType,                                                      ");
		sb.append("             MIN (car.JOB_TYPE) AS jobType,                                                      ");
		sb.append(" 			MIN (tr.ENTER_TALLY_OPERSON) AS operator1,                                          ");
		sb.append(" 			MIN (tr.ENTER_OPERSON) AS operator2,                                                ");
		sb.append(" 			MIN (tr.ENTER_TALLY_TIME) AS strTime,                                               ");
		sb.append(" 			MAX (tr.ENTER_STOCK_TIME) AS endTime,                                               ");
		sb.append(" 			MIN (NVL(car.CREATE_DATE,ent.ETA_WAREHOUSE)) AS ccTime,                             ");
		sb.append(" 			MIN (ent. OPERATOR) AS linkCreater,                                                 ");
		sb.append(" 			NVL (MIN(ai.piece), 0) AS planNum,                                                  ");
		sb.append(" 			NVL (SUM (CASE WHEN tr.cargo_state = '00' THEN tr.now_piece ELSE 0 END),0) AS shNum,");
		sb.append(" 		    NVL (SUM (CASE WHEN tr.cargo_state != '00' THEN tr.original_piece ELSE 0 END ),0) AS sjNum, ");
		sb.append(" 		    NVL (MIN(ai.net_Weight), 0) AS planWeight,                                          ");
		sb.append(" 			NVL (SUM (CASE WHEN tr.cargo_state = '00' THEN tr.net_weight ELSE 0 END ),0 ) AS shWeight, ");
		sb.append(" 			NVL (SUM (CASE WHEN tr.cargo_state != '00' THEN tr.original_piece * tr.net_Single ELSE 0 END),0) AS sjWeight ");
		sb.append(" 		FROM          ");
		sb.append("             bis_asn_info ai                                                     ");
		sb.append("         INNER JOIN bis_asn A ON A .asn = ai.asn_id                              ");
		sb.append("         INNER JOIN bis_enter_stock ent ON ent.link_id =A.LINK_ID                ");
		sb.append("         INNER JOIN BIS_ENTER_STOCK_INFO info ON ent.link_id=info.link_id        ");
		sb.append("         LEFT JOIN BIS_GATE_CAR car ON car.CTN_NUM = info.CTN_NUM                ");
		sb.append(" 	    LEFT JOIN bis_tray_info tr ON tr.asn = ai.asn_id AND tr.sku_id = ai.sku_id ");
		sb.append(" 		GROUP BY              ");
		sb.append(" 			ai.asn_id,        ");
		sb.append(" 			ai.sku_id         ");
		sb.append(" 	) ob                      ");
		sb.append(" )temp                         ");                                                                                            
        sb.append(" where 1=1       ");
		if(!StringUtils.isNull(monitor.getBillNum())){
			sb.append(" and lower(temp.BILLNUM) like lower(:billNum) ");
			params.put("billNum", "%"+monitor.getBillNum()+"%");
		}
		if(!StringUtils.isNull(monitor.getAsnNum())){
			sb.append(" and lower(temp.ASNNUM) like lower(:asn) ");
			params.put("asn", "%"+monitor.getAsnNum()+"%");
		}
		if(!StringUtils.isNull(monitor.getCtnNum())){
			sb.append(" and lower(temp.CTNNUM) like lower(:ctnNum) ");
			params.put("ctnNum", "%"+monitor.getCtnNum()+"%");
		}
		if(!StringUtils.isNull(monitor.getClientName())){
			sb.append(" and temp.CLIENTNAME =:clientName  ");
			params.put("clientName", monitor.getClientName());
		}
		if(null!=monitor.getCcTimeS()){
			sb.append(" and temp.ccTime >=:ccTimeS  ");
			params.put("ccTimeS", monitor.getCcTimeS());
		}
		if(null!=monitor.getCcTimeE()){
			sb.append(" and temp.ccTime <=:ccTimeE  ");
			params.put("ccTimeE", monitor.getCcTimeE());
		}
		if(null!=monitor.getStrTimeS()){
			sb.append(" and temp.strTime >:strTimeS  ");
			params.put("strTimeS", monitor.getStrTimeS());
		}
		if(null!=monitor.getStrTimeE()){
			sb.append(" and temp.strTime <:strTimeE  ");
			params.put("strTimeE", monitor.getStrTimeE());
		}
		
		if(null!=monitor.getEndTimeS()){
			sb.append(" and temp.endTime >:endTimeS  ");
			params.put("endTimeS", monitor.getEndTimeS());
		}
		if(null!=monitor.getEndTimeE()){
			sb.append(" and temp.endTime <:endTimeE  ");
			params.put("endTimeE", monitor.getEndTimeE());
		}
		if(!StringUtils.isNull(monitor.getJobState())){
			sb.append(" and temp.jobState=:jobState  ");
			params.put("jobState", monitor.getJobState());
		}
		/*StringBuffer sql = new StringBuffer(""
				+ " SELECT ob.*,(ob.shNum+ob.sjNum) as allNum,(ob.shWeight+ob.sjWeight) as allWeight," 
				+ " (case when (ob.shNum=0 and ob.sjNum=0) then '0' when (ob.shNum<ob.planNum and ob.sjNum=0 and ob.shNum!=0) then '2' when (ob.shNum=ob.planNum and ob.sjNum=0  and ob.shNum!=0) then '3' when (ob.shNum>0 and ob.sjNum>0) then '4' when (ob.shNum=0 and ob.sjNum>0) then '5' else '00' end) as jobState " 
				+ " from ( "
				+ " SELECT ai.asn_id as asnNum, "
				+ " min(a.CTN_NUM) as ctnNum,"
                + " min(ai.cargo_name) as cargoName, "
                + " (select a.stock_name from bis_asn a where ai.asn_id=a.asn) as clientName, "
				+ " max(a.bill_num) as billNum,"
 //               + " max(a.tally_user) as rkUser,"
                + " max(a.link_id) as linkId, "
                + " min(tr.ENTER_TALLY_OPERSON) as operator1, "
                + " min(tr.ENTER_OPERSON) as operator2, "
                + " min(tr.ENTER_TALLY_TIME) as strTime, "
                + " max(tr.ENTER_STOCK_TIME) as endTime, "
                + " min(ent.ETA_WAREHOUSE) as ccTime, "
                + " min(ent.OPERATOR) as linkCreater, "
                + " nvl(min(ai.piece),0) as planNum, "
                + " nvl(sum(case when tr.cargo_state='00' then tr.now_piece else 0 end),0) as shNum, "
                + " nvl(sum(case when tr.cargo_state!='00' then tr.original_piece else 0 end),0) as sjNum,"
                + " nvl(min(ai.net_Weight),0) as planWeight, "
                + " nvl(sum(case when tr.cargo_state='00' then tr.net_weight else 0 end),0) as shWeight, "
                + " nvl(sum(case when tr.cargo_state!='00' then tr.original_piece * tr.net_Single else 0 end) ,0) as sjWeight "
                + " from bis_asn_info ai "
                + " inner join bis_asn a on a.asn=ai.asn_id "
                + " inner join bis_enter_stock ent on ent.link_id = a.LINK_ID "
                + " left join bis_tray_info tr on tr.asn=ai.asn_id and tr.sku_id=ai.sku_id  " 
                + " left join BIS_GATE_CAR gate on gate.ctn_num=a.ctn_num"
                + " WHERE 1 = 1 ");*/
		
		/*if(!StringUtils.isNull(monitor.getAsnNum())){
			sql.append(" and lower(ai.ASN_ID) like lower(:asn) ");
			params.put("asn", "%"+monitor.getAsnNum()+"%");
		}*/
		/*if(!StringUtils.isNull(monitor.getCtnNum())){
			sql.append(" and lower(a.CTN_NUM) like lower(:ctnNum) ");
			params.put("ctnNum", "%"+monitor.getCtnNum()+"%");
		}*/
		/*if(!StringUtils.isNull(monitor.getSku())){
			sql.append(" and lower(ai.SKU_ID) like lower(:sku) ");
			params.put("sku", "%"+monitor.getSku()+"%");
		}*/
		/*if(!StringUtils.isNull(monitor.getBillNum())){
			sql.append(" and lower(a.bill_num) like lower(:billNum) ");
			params.put("billNum", "%"+monitor.getBillNum()+"%");
		}*/
		/*if(!StringUtils.isNull(monitor.getClientName())){
			sql.append(" and a.STOCK_NAME =:clientName  ");
			params.put("clientName", monitor.getClientName());
		}*/
		/*if(null!=monitor.getCcTimeS()){
			sql.append(" and gate.CREATE_DATE >=:ccTimeS  ");
			//sql.append(" and ent.ETA_WAREHOUSE >=:ccTimeS  ");
			params.put("ccTimeS", monitor.getCcTimeS());
		}
		if(null!=monitor.getCcTimeE()){
			sql.append(" and gate.CREATE_DATE <:ccTimeE  ");
			//sql.append(" and ent.ETA_WAREHOUSE <:ccTimeE  ");
			params.put("ccTimeE", monitor.getCcTimeE());
		}*/
		/*if(null!=monitor.getStrTimeS()){
			sql.append(" and tr.ENTER_TALLY_TIME >:strTimeS  ");
			params.put("strTimeS", monitor.getStrTimeS());
		}
		if(null!=monitor.getStrTimeE()){
			sql.append(" and tr.ENTER_TALLY_TIME <:strTimeE  ");
			params.put("strTimeE", monitor.getStrTimeE());
		}
		if(null!=monitor.getEndTimeS()){
			sql.append(" and tr.ENTER_STOCK_TIME >:endTimeS  ");
			params.put("endTimeS", monitor.getStrTimeS());
		}
		if(null!=monitor.getEndTimeE()){
			sql.append(" and tr.ENTER_STOCK_TIME <:endTimeE  ");
			params.put("endTimeE", monitor.getStrTimeE());
		}
		sql.append(" group by ai.asn_id,ai.sku_id ) ob");*/
		//状态分类
		//sql.append(" where 1=1 ");
/*		if(!StringUtils.isNull(monitor.getJobState())){
			if(monitor.getJobState().equals("0")){
				sql.append(" and (ob.shNum=0 and ob.sjNum=0) ");
			}else if(monitor.getJobState().equals("2")){
				sql.append(" and (ob.shNum<ob.planNum and ob.shNum!=0 and ob.sjNum=0) ");
			}else if(monitor.getJobState().equals("3")){
				sql.append(" and (ob.shNum=ob.planNum  and ob.shNum!=0 and ob.sjNum=0) ");
			}else if(monitor.getJobState().equals("4")){
				sql.append(" and (ob.shNum>0 and ob.sjNum>0) ");
			}else if(monitor.getJobState().equals("5")){
				sql.append(" and (ob.shNum=0 and ob.sjNum>0) ");
			}
		}*/
		//查询对象属性转换
		Map<String, Object> parm = new HashMap<String, Object>();
		parm.put("asnNum", String.class);
		parm.put("jobState", String.class);
		parm.put("cargoName", String.class);
		parm.put("clientName", String.class);
		parm.put("billNum", String.class);
		parm.put("ctnNum", String.class);
		parm.put("linkCreater", String.class);
		parm.put("operator1", String.class);
		parm.put("operator2", String.class);
		parm.put("linkId", String.class);
		parm.put("planNum", Double.class);
		parm.put("shNum", Double.class);
		parm.put("sjNum", Double.class); 
		parm.put("allNum", Double.class);
		parm.put("allWeight", Double.class);
		parm.put("planWeight", Double.class);
		parm.put("shWeight", Double.class);
		parm.put("sjWeight", Double.class); 
		parm.put("strTime", Date.class); 
 		parm.put("endTime", Date.class); 
 		parm.put("ccTime", Date.class);
 		return findPageSql(page, sb.toString(), parm, params);
//		return findPageSql(page, sql.toString(), params);
	}
	
	
	/**
	 * 
	 * @author GLZ
	 * @Description: 出库作业监控分页查询
	 * @param page
	 * @return
	 * @throws
	 */
	public Page<Monitor> searchOutMonitor(Page<Monitor> page, Monitor monitor){
		Map<String,Object> params = new HashMap<String, Object>();
		StringBuffer sb=new StringBuffer();
		sb.append(" SELECT                                                                                                                   "); 
		sb.append("   temp.*                                                                                                                 ");
		sb.append(" FROM                                                                                                                     ");
		sb.append(" (                                                                                                                        ");
		sb.append(" 	SELECT                                                                                                                 ");
		sb.append(" 		ob.*, (ob.jhNum + ob.zcNum) AS allNum,                                                                               ");
		sb.append(" 		(ob.jhWeight + ob.zcWeight) AS allWeight,                                                                            ");
		sb.append(" 		(                                                                                                                    ");
		sb.append(" 			CASE                                                                                                               ");
		sb.append(" 				WHEN (ob.jhNum < ob.planNum AND ob.zcNum = 0 AND ob.jhNum != 0) THEN '8'                                         ");
		sb.append(" 				WHEN (ob.jhNum = ob.planNum AND ob.zcNum = 0 AND ob.jhNum != 0) THEN '9'                                         ");
		sb.append(" 				WHEN (ob.jhNum > 0 AND ob.zcNum > 0) THEN '10'                                                                   ");
		sb.append(" 				WHEN (ob.jhNum = 0 AND ob.zcNum > 0) THEN '11'                                                                   ");
		sb.append(" 			ELSE '00'                                                                                                          ");
		sb.append(" 			END                                                                                                                ");
		sb.append(" 		) AS jobState                                                                                                        ");
		sb.append(" 	FROM                                                                                                                   ");
		sb.append(" 		(                                                                                                                    ");
		sb.append(" 			SELECT                                                                                                             ");
		sb.append(" 				li.asn_id AS asnNum,                                                                                             ");
		sb.append(" 				li.LOADING_TRUCK_NUM AS loadingNum,                                                                              ");
		sb.append(" 				MIN (li.PLATFORM_NUM) AS platformNum,                                                                            ");
		sb.append(" 				MIN (li.CAR_NO) AS carNo,                                                                                        ");
		sb.append(" 				MIN (tr.stock_name) AS clientName,                                                                               ");
		sb.append(" 				MIN (li.cargo_name) AS cargoName,                                                                                ");
		sb.append(" 				MIN (tr.OUT_TALLY_OPERSON) AS operator1,                                                                         ");
		sb.append(" 				MAX (tr.OUT_OPERSON) AS operator2,                                                                               ");
		sb.append(" 				MIN (tr.OUT_TALLY_TIME) AS strTime,                                                                              ");
		sb.append(" 				MAX (tr.OUT_STOCK_TIME) AS endTime,                                                                              ");
		sb.append(" 				MIN (blo.plan_date_nopay) AS ccTime,                                                                             ");
		sb.append(" 				MIN (ord. OPERATOR) AS linkCreater,                                                                              ");
		sb.append(" 				SUM (li.piece) AS planNum,                                                                                       ");
		sb.append(" 				SUM (li.net_weight) AS planWeight,                                                                               ");
		sb.append(" 				NVL (SUM (CASE WHEN tr.cargo_state = '11' THEN tr.now_piece ELSE 0 END),0) AS jhNum,                             ");
		sb.append(" 				NVL (SUM (CASE WHEN tr.cargo_state = '11' THEN tr.net_weight ELSE 0 END),0) AS jhWeight,                         ");
		sb.append(" 				NVL (SUM (CASE WHEN tr.cargo_state = '12' THEN tr.original_piece ELSE 0 END),0) AS zcNum,                        ");
		sb.append(" 				NVL (SUM (CASE WHEN tr.cargo_state = '12' THEN tr.original_piece * tr.net_single ELSE 0 END),0) AS zcWeight      ");
		sb.append(" 			FROM                                                                                                               ");
		sb.append(" 				bis_loading_info li                                                                                              ");
		sb.append(" 			INNER JOIN bis_loading_order blo ON blo.order_num = li.loading_plan_num                                            ");
		sb.append(" 			INNER JOIN bis_out_stock ord ON ord.out_link_id = li.OUT_LINK_ID                                                   ");
		sb.append(" 			INNER JOIN bis_tray_info tr ON tr.sku_id = li.sku_id                                                               ");
		sb.append(" 			AND tr.asn = li.asn_id                                                                                             ");
		sb.append(" 			AND tr.tray_id = li.tray_id                                                                                        ");
		sb.append(" 			WHERE                                                                                                              ");
		sb.append(" 				1 = 1                                                                                                            ");
		sb.append(" 			GROUP BY                                                                                                           ");
		sb.append(" 				li.asn_id,                                                                                                       ");
		sb.append(" 				li.sku_id,                                                                                                       ");
		sb.append(" 				li.LOADING_TRUCK_NUM                                                                                             ");
		sb.append(" 		) ob                                                                                                                 ");
		sb.append(" 	 UNION ALL                                                                                                             ");
		sb.append(" 	 SELECT                                                                                                                ");
		sb.append(" 		ob.asnNum,                                                                                                           ");
		sb.append(" 		ob.loadingNum,                                                                                                       ");
		sb.append(" 		ob.platformNum,                                                                                                      ");
		sb.append(" 		CAR.CAR_NUM AS carNo,                                                                                                ");
		sb.append(" 		ob.clientName,                                                                                                       ");
		sb.append(" 		ob.cargoName,                                                                                                        ");
		sb.append(" 		ob.operator1,                                                                                                        ");
		sb.append(" 		ob.operator2,                                                                                                        ");
		sb.append(" 		ob.strTime,                                                                                                          ");
		sb.append(" 		ob.endTime,                                                                                                          ");
		sb.append(" 		car.CREATE_DATE AS ccTime,                                                                                           ");
		sb.append(" 		ob.linkCreater,                                                                                                      ");
		sb.append(" 		ob.planNum,                                                                                                          ");
		sb.append(" 		ob.planWeight,                                                                                                       ");
		sb.append(" 		ob.jhNum,                                                                                                            ");
		sb.append(" 		ob.jhWeight,                                                                                                         ");
		sb.append(" 		ob.zcNum,                                                                                                            ");
		sb.append(" 		ob.zcWeight,                                                                                                         ");
		sb.append(" 		(ob.jhNum + ob.zcNum) AS allNum,                                                                                     ");
		sb.append(" 		(ob.jhWeight + ob.zcWeight) AS allWeight,                                                                            ");
		sb.append(" 		'6' AS jobState                                                                                                      ");
		sb.append(" 	FROM                                                                                                                   ");
		sb.append(" 		BIS_GATE_CAR car                                                                                                     ");
		sb.append(" 	LEFT JOIN                                                                                                              ");
		sb.append(" 	(                                                                                                                      ");
		sb.append(" 			SELECT                                                                                                             ");
		sb.append(" 				li.asn_id AS asnNum,                                                                                             ");
		sb.append(" 				li.LOADING_TRUCK_NUM AS loadingNum,                                                                              ");
		sb.append(" 				MIN (li.PLATFORM_NUM) AS platformNum,                                                                            ");
		sb.append(" 				MIN (li.CAR_NO) AS carNo,                                                                                        ");
		sb.append(" 				MIN (tr.stock_name) AS clientName,                                                                               ");
		sb.append(" 				MIN (li.cargo_name) AS cargoName,                                                                                ");
		sb.append(" 				MIN (tr.OUT_TALLY_OPERSON) AS operator1,                                                                         ");
		sb.append(" 				MAX (tr.OUT_OPERSON) AS operator2,                                                                               ");
		sb.append(" 				MIN (tr.OUT_TALLY_TIME) AS strTime,                                                                              ");
		sb.append(" 				MAX (tr.OUT_STOCK_TIME) AS endTime,                                                                              ");
		sb.append(" 				MIN (blo.plan_date_nopay) AS ccTime,                                                                             ");
		sb.append(" 				MIN (ord. OPERATOR) AS linkCreater,                                                                              ");
		sb.append(" 				SUM (li.piece) AS planNum,                                                                                       ");
		sb.append(" 				SUM (li.net_weight) AS planWeight,                                                                               ");
		sb.append(" 				NVL (SUM (CASE WHEN tr.cargo_state = '11' THEN tr.now_piece ELSE 0 END),0) AS jhNum,                             ");
		sb.append(" 				NVL (SUM (CASE WHEN tr.cargo_state = '11' THEN tr.net_weight ELSE 0 END),0) AS jhWeight,                         ");
		sb.append(" 				NVL (SUM (CASE WHEN tr.cargo_state = '12' THEN tr.original_piece ELSE 0 END),0) AS zcNum,                        ");
		sb.append(" 				NVL (SUM (CASE WHEN tr.cargo_state = '12' THEN tr.original_piece * tr.net_single ELSE 0 END),0) AS zcWeight      ");
		sb.append(" 			FROM                                                                                                               ");
		sb.append(" 				bis_loading_info li                                                                                              ");
		sb.append(" 			INNER JOIN bis_loading_order blo ON blo.order_num = li.loading_plan_num                                            ");
		sb.append(" 			INNER JOIN bis_out_stock ord ON ord.out_link_id = li.OUT_LINK_ID                                                   ");
		sb.append(" 			INNER JOIN bis_tray_info tr ON tr.sku_id = li.sku_id                                                               ");
		sb.append(" 			AND tr.asn = li.asn_id                                                                                             ");
		sb.append(" 			AND tr.tray_id = li.tray_id                                                                                        ");
		sb.append(" 			WHERE                                                                                                              ");
		sb.append(" 				1 = 1                                                                                                         ");
		sb.append(" 			GROUP BY                                                                                                          ");
		sb.append(" 				li.asn_id,                                                                                                    ");
		sb.append(" 				li.sku_id,                                                                                                    ");
		sb.append(" 				li.LOADING_TRUCK_NUM                                                                                          ");
		sb.append(" 	)ob                                                                                                                       ");
		sb.append(" 	ON CAR.CAR_NUM=ob.carNo                                                                                                   ");
		sb.append("  WHERE CAR.BIS_TYPE ='2' AND CAR.JOB_TYPE ='1'                                                                                ");
		sb.append(" )temp                                                                                                                         ");
		sb.append(" where 1=1 ");
		if(!StringUtils.isNull(monitor.getAsnNum())){
			sb.append(" and lower(temp.ASNNUM) like lower(:asn) ");
			params.put("asn", "%"+monitor.getAsnNum()+"%");
		}
		if(!StringUtils.isNull(monitor.getLoadingNum())){
			sb.append(" and lower(temp.LOADINGNUM) like lower(:loadingNum) ");
			params.put("loadingNum", "%"+monitor.getLoadingNum()+"%");
		}
		if(!StringUtils.isNull(monitor.getPlatformNum())){
			sb.append(" and lower(temp.PLATFORMNUM) like lower(:platformNum) ");
			params.put("platformNum", "%"+monitor.getPlatformNum()+"%");
		}
		if(!StringUtils.isNull(monitor.getCarNo())){
			sb.append(" and lower(temp.CARNO) like lower(:carNo) ");
			params.put("carNo", "%"+monitor.getCarNo()+"%");
		}
		
		if(!StringUtils.isNull(monitor.getClientName())){
			sb.append(" and temp.CLIENTNAME =:clientName  ");
			params.put("clientName", monitor.getClientName());
		}
		if(null!=monitor.getCcTimeS()){
			sb.append(" and temp.CCTIME >=:ccTimeS  ");
			params.put("ccTimeS", monitor.getCcTimeS());
		}
		if(null!=monitor.getCcTimeE()){
			sb.append(" and temp.CCTIME <=:ccTimeE  ");
			params.put("ccTimeE", monitor.getCcTimeE());
		}
		if(null!=monitor.getStrTimeS()){
			sb.append(" and temp.STRTIME >:strTimeS  ");
			params.put("strTimeS", monitor.getStrTimeS());
		}
		if(null!=monitor.getStrTimeE()){
			sb.append(" and temp.STRTIME <:strTimeE  ");
			params.put("strTimeE", monitor.getStrTimeE());
		}
		
		if(null!=monitor.getEndTimeS()){
			sb.append(" and temp.endtime >:endTimeS  ");
			params.put("endTimeS", monitor.getEndTimeS());
		}
		if(null!=monitor.getEndTimeE()){
			sb.append(" and temp.endtime <:endTimeE  ");
			params.put("endTimeE", monitor.getEndTimeE());
		}
		if(!StringUtils.isNull(monitor.getJobState())){
			sb.append(" and temp.jobState=:jobState  ");
			params.put("jobState", monitor.getJobState());
		}
		sb.append(" ORDER BY temp.carno ");
		/*StringBuffer sql = new StringBuffer(""
				+ " select ob.*,(ob.jhNum+ob.zcNum) as allNum,(ob.jhWeight+ob.zcWeight) as allWeight," 
				+ " (case when (ob.jhNum=0 and ob.zcNum=0) then '6' when (ob.jhNum<ob.planNum and ob.zcNum=0 and ob.jhNum!=0) then '8' when (ob.jhNum=ob.planNum and ob.zcNum=0 and ob.jhNum!=0) then '9' when (ob.jhNum>0 and ob.zcNum>0) then '10' when (ob.jhNum=0 and ob.zcNum>0) then '11' else '00' end) as jobState " 
				+ " from ( "
				+ " SELECT li.asn_id as asnNum, "
                + " li.LOADING_TRUCK_NUM as loadingNum, "
				+ " min(li.PLATFORM_NUM) as platformNum,"
                + " min(li.CAR_NO) as carNo,"
                + " min(tr.stock_name) as clientName, "
				+ " min(li.cargo_name) as cargoName, "
				+ " min(tr.OUT_TALLY_OPERSON) as operator1, "
	            + " max(tr.OUT_OPERSON) as operator2, "
                + " min(tr.OUT_TALLY_TIME) as strTime, "
                + " max(tr.OUT_STOCK_TIME) as endTime, "
                + " min(blo.plan_date_nopay) as ccTime, "
                + " min(ord.OPERATOR) as linkCreater, "
                + " sum(li.piece) as planNum, "
                + " sum(li.net_weight) as planWeight, "
                + " nvl(sum(case when tr.cargo_state='11' then tr.now_piece else 0 end),0) as jhNum, "
                + " nvl(sum(case when tr.cargo_state='11' then tr.net_weight else 0 end),0) as jhWeight,"
                + " nvl(sum(case when tr.cargo_state='12' then tr.original_piece else 0 end),0) as zcNum, "
                + " nvl(sum(case when tr.cargo_state='12' then tr.original_piece * tr.net_single else 0 end),0) as zcWeight "
                + "  from bis_loading_info li "
                + " inner join bis_loading_order blo on blo.order_num = li.loading_plan_num "
                + " inner join bis_out_stock ord on ord.out_link_id = li.OUT_LINK_ID  "
                + " inner join bis_tray_info tr on tr.sku_id = li.sku_id and tr.asn = li.asn_id and tr.tray_id=li.tray_id "
                + " WHERE 1 = 1 ");*/
		/*if(!StringUtils.isNull(monitor.getAsnNum())){
			sql.append(" and lower(li.ASN_ID) like lower(:asn) ");
			params.put("asn", "%"+monitor.getAsnNum()+"%");
		}
		if(!StringUtils.isNull(monitor.getLoadingNum())){
			sql.append(" and lower(li.LOADING_TRUCK_NUM) like lower(:loadingNum) ");
			params.put("loadingNum", "%"+monitor.getLoadingNum()+"%");
		}
		if(!StringUtils.isNull(monitor.getPlatformNum())){
			sql.append(" and lower(li.PLATFORM_NUM) like lower(:platformNum) ");
			params.put("platformNum", "%"+monitor.getPlatformNum()+"%");
		}
		if(!StringUtils.isNull(monitor.getCarNo())){
			sql.append(" and lower(li.CAR_NO) like lower(:carNo) ");
			params.put("carNo", "%"+monitor.getCarNo()+"%");
		}*/
		/*if(!StringUtils.isNull(monitor.getClientName())){
			sql.append(" and tr.STOCK_NAME =:clientName  ");
			params.put("clientName", monitor.getClientName());
		}
		if(null!=monitor.getCcTimeS()){
			sql.append(" and blo.plan_date_nopay >=:ccTimeS  ");
			params.put("ccTimeS", monitor.getCcTimeS());
		}
		if(null!=monitor.getCcTimeE()){
			sql.append(" and blo.plan_date_nopay <:ccTimeE  ");
			params.put("ccTimeE", monitor.getCcTimeE());
		}
		if(null!=monitor.getStrTimeS()){
			sql.append(" and tr.ENTER_TALLY_TIME >:strTimeS  ");
			params.put("strTimeS", monitor.getStrTimeS());
		}
		if(null!=monitor.getStrTimeE()){
			sql.append(" and tr.ENTER_TALLY_TIME <:strTimeE  ");
			params.put("strTimeE", monitor.getStrTimeE());
		}
		
		if(null!=monitor.getEndTimeS()){
			sql.append(" and tr.ENTER_STOCK_TIME >:endTimeS  ");
			params.put("endTimeS", monitor.getStrTimeS());
		}
		if(null!=monitor.getEndTimeE()){
			sql.append(" and tr.ENTER_STOCK_TIME <:endTimeE  ");
			params.put("endTimeE", monitor.getStrTimeE());
		}*/
		//sql.append("  group by li.asn_id, li.sku_id,li.LOADING_TRUCK_NUM ) ob");
		
		//状态分类
		//sql.append(" where 1=1 ");
		/*if(!StringUtils.isNull(monitor.getJobState())){
			if(monitor.getJobState().equals("6")){
				sql.append(" and (ob.jhNum=0 and ob.zcNum=0) ");
			}else if(monitor.getJobState().equals("8")){
				sql.append(" and (ob.jhNum<ob.planNum and ob.jhNum!=0 and ob.zcNum=0) ");
			}else if(monitor.getJobState().equals("9")){
				sql.append(" and (ob.jhNum=ob.planNum and ob.jhNum!=0 and ob.zcNum=0) ");
			}else if(monitor.getJobState().equals("10")){
				sql.append(" and (ob.jhNum>0 and ob.zcNum>0) ");
			}else if(monitor.getJobState().equals("11")){
				sql.append(" and (ob.jhNum=0 and ob.zcNum>0) ");
			}
		}*/
		//查询对象属性转换
		Map<String, Object> parm = new HashMap<String, Object>();
		parm.put("asnNum", String.class);
		parm.put("jobState", String.class);
		parm.put("cargoName", String.class);
		parm.put("clientName", String.class);
		parm.put("loadingNum", String.class);
		parm.put("platformNum", String.class);
		parm.put("carNo", String.class);
		parm.put("linkCreater", String.class);
		parm.put("planNum", Double.class);
		parm.put("jhNum", Double.class);
		parm.put("jhNum", Double.class); 
		parm.put("planWeight", Double.class);
		parm.put("zcNum", Double.class);
		parm.put("zcWeight", Double.class); 
		parm.put("allNum", Double.class);
		parm.put("allWeight", Double.class); 
		parm.put("ccTime", Date.class); 
		parm.put("strTime", Date.class); 
		parm.put("endTime", Date.class); 
		parm.put("operator1", String.class);
		parm.put("operator2", String.class);
 		return findPageSql(page, sb.toString(), parm, params);
//		return findPageSql(page, sql.toString(), params);
	}
	
	/**
	 * 按条件查询入库联系单和货转联系单集合
	 * @param page
	 * @param billNum //提单id
	 * @param transNum//装车单号
	 * @param ctnNum //厢号
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String,Object>> findList(Page page,String linkNum,String billNum,String transNum,String ctnNum){
		StringBuffer sb=new StringBuffer();
		HashMap<String,Object> parme=new HashMap<String,Object>();
		if(linkNum!=null && !"".equals(linkNum)){
			sb.append("select r.link_id as codenum,0 as ntype,r.stock_in as stock ,r.stock_id as stockid,r.item_num as itemnum,r.operate_time,r.IF_BONDED,r.STOCK_ORG_ID,r.STOCK_ORG from bis_enter_stock r where r.link_id=:linkNum and r.DEL_FLAG='0'");
			parme.put("linkNum", linkNum);
		}else if(billNum!=null && !"".equals(billNum)){
			sb.append("select r.link_id as codenum,0 as ntype,r.stock_in as stock ,r.stock_id as stockid,r.item_num as itemnum,r.operate_time,r.IF_BONDED,r.STOCK_ORG_ID,r.STOCK_ORG from bis_enter_stock r where r.item_num=:ritem_num and r.DEL_FLAG='0'");
			//sb.append(" union ");
			//sb.append("select t.transfer_id as codenum,1 as ntype,t.stock_in as strock,t.stock_in_id as stockid ,t.item_num as itemnum,t.operate_time  from  bis_transfer_stock t where t.item_num=:titem_num");
			parme.put("ritem_num", billNum);
			//parme.put("titem_num", billNum);
		}else{
			if((transNum!=null && !"".equals(transNum)) || (ctnNum!=null && !"".equals(ctnNum))){
				sb.append("select r.link_id as codenum,0 as ntype,r.stock_in as stock ,r.stock_id as stockid,r.item_num as itemnum,r.operate_time,r.IF_BONDED  from bis_enter_stock r where r.item_num in (select loading_plan_num from bis_loading_info z where 1=1 ").append(transNum!=null && !"".equals(transNum)?" and z.loading_truck_num=:truckNum1":"").append((ctnNum!=null && !"".equals(ctnNum))?" and z.ctn_num=:ctnNum1":"").append(")");
				sb.append(" union ");
				sb.append("select t.transfer_id as codenum,1 as ntype,t.stock_in as strock,t.stock_in_id as stockid ,t.item_num as itemnum,t.operate_time  from  bis_transfer_stock t where t.item_num in (select loading_plan_num from bis_loading_info z where 1=1 ").append(transNum!=null && !"".equals(transNum)?" and z.loading_truck_num=:truckNum2":"").append(ctnNum!=null && !"".equals(ctnNum)?" and z.ctn_num=:ctnNum2":"").append(")") ; 
				if(transNum!=null && !"".equals(transNum)){
					parme.put("truckNum1", transNum);
					parme.put("truckNum2", transNum);
				}
				if(ctnNum!=null && !"".equals(ctnNum)){
					parme.put("ctnNum1", ctnNum);
					parme.put("ctnNum2", ctnNum);
				}
			}
		}
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	
	
}
