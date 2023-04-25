package com.haiersoft.ccli.report.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.utils.PageUtils;
import com.haiersoft.ccli.report.entity.Stock;

@Repository
public class SteveDroingReportDao extends HibernateDao<Stock, String> {
	
	
	/**
	 * 获取入库装卸队 装载数量统计   
	 * @param clientId
	 * @param startTime
	 * @param endTime
	 * @param npage
	 * @param pageSize
	 * @return
	 */
	public Map<String, Object> getSteveReportInfo(String reportType,String clientId , String startTime,String endTime,int npage,int pageSize){
		Map<String,Object> returnMap = new HashMap<String,Object>(); 
		StringBuffer sql=new StringBuffer();
		HashMap<String,Object> parme=new HashMap<String,Object>();
		if(clientId!=null && !"".equals(clientId)) 
		   parme.put("CLIENTID", clientId); 		
		if(startTime!=null && !"".equals(startTime)) 	
		   parme.put("STARTIME", startTime);
		if(endTime!=null && !"".equals(endTime))  
		   parme.put("ENDTIME", endTime); 
			
		   sql.append(" select * from (");		      
		   if(reportType.trim().length()<=0 ||reportType.trim().equals("1") ) { //入库
				sql.append(" (select distinct '入库' as lx ,a.client,a.fee_plan,'' as loading_num,a.dromr,a.drostockinname,a.droenterstocktime,b.FEE_NAME,b.NUM,b.SHOULD_RMB,b.PRICE,b.LINK_ID as ASN,b.BILL_NUM "); 
				sql.append(" from ( select t.* from bis_enter_stevedoring t where"); 
				
				if(clientId!=null && !"".equals(clientId)) 
				   sql.append(" t.client_id=:CLIENTID and ");  
				sql.append(" t.IF_OK='1' and t.DROENTERSTOCKTIME is not null  ");			
				if(startTime!=null && !"".equals(startTime)){
				   //sql.append(" and  trunc(t.DROENTERSTOCKTIME,'mm')>= to_date(:STARTIME,'yyyy-mm') "); 
				   sql.append(" and t.DROENTERSTOCKTIME>=to_date(:STARTIME,'yyyy-mm-dd') "); 
				}
				if(endTime!=null && !"".equals(endTime)){
				   //sql.append(" and  trunc(t.DROENTERSTOCKTIME,'mm')<=to_date(:ENDTIME,'yyyy-mm')");
				   sql.append(" and t.DROENTERSTOCKTIME<=to_date(:ENDTIME,'yyyy-mm-dd')"); 
				}
				
				sql.append(" ) A join  (select t.STANDING_NUM, t.CUSTOMS_NUM ,t.BILL_NUM ,t.LINK_ID,t.FEE_CODE,t.FEE_NAME,t.bis_type,t.FEE_PLAN,t.ASN,t.IF_RECEIVE,t.NUM,t.SHOULD_RMB,t.PRICE");  
				sql.append(" from BIS_STANDING_BOOK t where t.if_receive=2 and t.bis_type in (5,6,7,8)  ) B on  B.STANDING_NUM in (nvl(A.drostanindbookids,0)) ) "); 
	        }			
			if(reportType.trim().length()<=0)  
				 sql.append(" union");
			if(reportType.trim().length()<=0 ||reportType.trim().equals("2") ) {//出库
				sql.append("(                                                                           ");
				sql.append("	SELECT DISTINCT                                                            ");
				sql.append("		'出库' AS lx,                                                            ");
				sql.append("		A .client,                                                               ");
				sql.append("		A .fee_plan,                                                             ");
				sql.append("		TO_CHAR (NVL(A .loading_num, '')) AS loading_num,                        ");
				sql.append("		A.LOADING_TRUCK_NUM AS dromr,                                            ");
				sql.append("		A .DROSTOCKIDNAME AS drostockinname,                                     ");
				sql.append("		A .DROLOADINGTIME AS droenterstocktime,                                  ");
				sql.append("		b.FEE_NAME,                                                              ");
				sql.append("        B.NUM,                                                                   ");
				sql.append("		B.NUM*b.PRICE SHOULD_RMB,                                                ");
				sql.append("		b.PRICE,                                                                 ");
				sql.append("		b.LINK_ID AS ASN,                                                        ");
				sql.append("		A.BILL_NUM AS BILL_NUM                                                   ");
				sql.append("	FROM                                                                       ");
				sql.append("		(                                                                      ");
				sql.append("			SELECT                                                             ");
				sql.append("				T.*,                                                           ");
				sql.append("                L.BILL_NUM,                                                    ");
				sql.append("                L.LOADING_TRUCK_NUM,                                           ");
				sql.append("                L.NUM                                                          ");
				sql.append("			FROM                                                               ");
				sql.append("				bis_out_stevedoring T                                          ");
				sql.append("      INNER JOIN                                                            ");
				sql.append("        (                                                                   ");
				sql.append("           SELECT                                                           ");
				sql.append("              L.BILL_NUM,                                                   ");
				sql.append("              L.LOADING_TRUCK_NUM,                                          ");
				sql.append("              sum(L.net_weight/1000) NUM                                    ");
				sql.append("           FROM                                                             ");
				sql.append("              BIS_LOADING_INFO L                                            ");
				sql.append("           WHERE                                                            ");
				sql.append("              L.LOADING_STATE='2'                                           ");
				sql.append("           GROUP BY                                                         ");
				sql.append("              L.BILL_NUM,L.LOADING_TRUCK_NUM                                ");
				sql.append("        ) L                                                                 ");
				sql.append("      ON  T.LOADING_NUM=L.LOADING_TRUCK_NUM                                 ");
				sql.append("			WHERE                                                           ");
				sql.append("				T .IF_OK = '1'                                              ");
				sql.append("			AND T .DROLOADINGTIME IS NOT NULL                               ");
				if(clientId!=null && !"".equals(clientId)){
					   sql.append("    AND T.client_id=:CLIENTID                                       "); 
				}
				if(startTime!=null && !"".equals(startTime)){
					   sql.append("  AND T.DROLOADINGTIME>= to_date(:STARTIME,'yyyy-mm-dd') "); 
				}
				if(endTime!=null && !"".equals(endTime)){
					   sql.append(" AND T.DROLOADINGTIME<=to_date(:ENDTIME,'yyyy-mm-dd')"); 
				}
				sql.append("		) A                                                                        ");
				sql.append("	JOIN (                                                                         ");
				sql.append("		SELECT                                                                     ");
				sql.append("			T .STANDING_NUM,                                                       ");
				sql.append("			T .CUSTOMS_NUM,                                                        ");
				sql.append("			T .BILL_NUM,                                                           ");
				sql.append("			T .LINK_ID,                                                            ");
				sql.append("			T .FEE_CODE,                                                           ");
				sql.append("			T .FEE_NAME,                                                           ");
				sql.append("			T .bis_type,                                                           ");
				sql.append("			T .FEE_PLAN,                                                           ");
				sql.append("			T .ASN,                                                                ");
				sql.append("			T .IF_RECEIVE,                                                         ");
				sql.append("			T .NUM,                                                                ");
				sql.append("			T .SHOULD_RMB,                                                     ");
				sql.append("			T .PRICE                                                           ");
				sql.append("		FROM                                                                   ");
				sql.append("			BIS_STANDING_BOOK T                                                ");
				sql.append("		WHERE                                                                  ");
				sql.append("			T .if_receive = 2                                                  ");
				sql.append("		AND T .bis_type IN (5, 6, 7, 8)                                        ");
				sql.append("	) B ON B.STANDING_NUM IN (A .drostanindbookids)                            ");
				sql.append(")                                                                              ");
			}
			if(reportType.trim().length()<=0)
				 sql.append(" union");
			if(reportType.trim().length()<=0 ||reportType.trim().equals("3") ) {//倒箱
				sql.append(" (select distinct  '倒箱' as lx ,a.client,a.fee_plan,'' as loading_num,a.mr as dromr,a.DROSTOCKIN as drostockinname,a.DROBACKDATE as droenterstocktime,b.FEE_NAME,b.NUM,b.SHOULD_RMB,b.PRICE,b.LINK_ID as ASN,a.BILL_NUM as BILL_NUM ");
				sql.append(" from ( select t.* from bis_back_stevedoring t where");

				if(clientId!=null && !"".equals(clientId)) 
				   sql.append(" t.client_id=:CLIENTID and ");  
				sql.append(" t.IF_OK='1' and t.IF_ALL_MAN=1 and t.DROBACKDATE is not null ");			
				if(startTime!=null && !"".equals(startTime)){
					//sql.append(" and  trunc(t.DROBACKDATE,'mm')>= to_date(:STARTIME,'yyyy-mm') "); 
					sql.append(" and t.DROBACKDATE>= to_date(:STARTIME,'yyyy-mm-dd') ");
				}
				if(endTime!=null && !"".equals(endTime)){
				    //sql.append(" and  trunc(t.DROBACKDATE,'mm')<=to_date(:ENDTIME,'yyyy-mm')");  
					sql.append(" and t.DROBACKDATE<= to_date(:ENDTIME,'yyyy-mm-dd') ");
				}
				sql.append(" ) A join (select t.STANDING_NUM, t.CUSTOMS_NUM ,t.BILL_NUM ,t.LINK_ID,t.FEE_CODE,t.FEE_NAME,t.bis_type,t.FEE_PLAN,t.ASN,t.IF_RECEIVE,t.NUM,t.SHOULD_RMB,t.PRICE ");
				sql.append(" from BIS_STANDING_BOOK t where t.if_receive=2 and t.bis_type=6) B on instr(A.STANDINGBOOKIDS,to_char(B.STANDING_NUM))>0 )");
			} 
			if(reportType.trim().length()<=0)
				 sql.append(" union");
			if(reportType.trim().length()<=0 ||reportType.trim().equals("4") ){//日工
				sql.append("  (select '日工'as lx ,d.CLIENT,d.SCHEME_NAME as fee_plan , '' as loading_num,'' as dromr ,'' as drostockinname ,d.loading_date as droenterstocktime,c.FEE_NAME ,sum(c.NUM) as NUM , sum(c.SHOULD_RMB) as SHOULD_RMB,c.PRICE,'' as ASN,'' as BILL_NUM");  
				sql.append(" from (  select t.* from bis_loading_team_daywork  t where t.state=1 and t.del_flag=0 and t.loading_date is not null ");  
				
		            if(clientId!=null && !"".equals(clientId))
		               sql.append(" and t.client_id=:CLIENTID "); 
					if(startTime!=null && !"".equals(startTime)){ 	 
					   //sql.append(" and trunc(t.loading_date,'mm')>= to_date(:STARTIME,'yyyy-mm') ");
						sql.append(" and t.loading_date>= to_date(:STARTIME,'yyyy-mm-dd') ");
					}
					if(endTime!=null && !"".equals(endTime)){   
					   //sql.append(" and trunc(t.loading_date,'mm')<= to_date(:ENDTIME,'yyyy-mm')   ");
						sql.append(" and t.loading_date>= to_date(:ENDTIME,'yyyy-mm-dd') ");
					}
				sql.append(" ) d  left join  (select t.STANDING_NUM, t.CUSTOMS_NUM ,t.BILL_NUM ,t.LINK_ID,t.FEE_CODE,t.FEE_NAME,t.bis_type,t.FEE_PLAN,t.ASN,t.IF_RECEIVE,t.NUM,t.SHOULD_RMB,t.PRICE ");
				sql.append(" from BIS_STANDING_BOOK t where t.if_receive=2 ) c on d.STANDING_NUM = c.STANDING_NUM ");
				sql.append(" group by d.client_id,d.CLIENT,d.SCHEME_NAME,d.loading_date,c.FEE_PLAN,c.FEE_NAME,c.PRICE) ");
			}
			//缠膜
			if(reportType.trim().length()<=0)
				 sql.append(" union");
			if(reportType.trim().length()<=0 ||"5".equals(reportType.trim())||"7".equals(reportType.trim())||"8".equals(reportType.trim())||"21".equals(reportType.trim())||"22".equals(reportType.trim())||"23".equals(reportType.trim())||"24".equals(reportType.trim())||"25".equals(reportType.trim())) {//缠膜
				sql.append("( select               ");
				sql.append("(CASE b.bis_type       ");
				sql.append(" WHEN '5' THEN '在库分拣' ");
				sql.append(" WHEN '7' THEN '缠膜'   ");
				sql.append(" WHEN '8' THEN '打包'   ");
				sql.append(" WHEN '21' THEN '内标签' ");
				sql.append(" WHEN '22' THEN '外标签' ");
				sql.append(" WHEN '23' THEN '码托'  ");
				sql.append(" WHEN '24' THEN '装铁架' ");
				sql.append(" WHEN '25' THEN '拆铁架' ");
				sql.append(" ELSE '其他' END) as lx,");
				sql.append(" a.client,a.fee_plan,'' as loading_num,a.mr as dromr,a.DROSTOCKIN as drostockinname,a.DROBACKDATE as droenterstocktime,b.FEE_NAME,b.NUM,b.SHOULD_RMB,b.PRICE,b.LINK_ID as ASN,a.BILL_NUM as BILL_NUM ");
				sql.append(" from ( select t.* from bis_back_stevedoring t where");
				if(clientId!=null && !"".equals(clientId)) 
				   sql.append(" t.client_id=:CLIENTID and ");  
				sql.append(" t.IF_OK='1' ");			
				if(startTime!=null && !"".equals(startTime)){  
				   //sql.append(" and  trunc(t.DROBACKDATE,'mm')>= to_date(:STARTIME,'yyyy-mm') ");
				   sql.append(" and  t.DROBACKDATE>= to_date(:STARTIME,'yyyy-mm-dd') ");
				}
				if(endTime!=null && !"".equals(endTime)){  
				   //sql.append(" and  trunc(t.DROBACKDATE,'mm')<=to_date(:ENDTIME,'yyyy-mm')");
				   sql.append(" and t.DROBACKDATE<=to_date(:ENDTIME,'yyyy-mm-dd')  ");
				}
				
				sql.append(" ) A join (select t.STANDING_NUM, t.CUSTOMS_NUM ,t.BILL_NUM ,t.LINK_ID,t.FEE_CODE,t.FEE_NAME,t.bis_type,t.FEE_PLAN,t.ASN,t.IF_RECEIVE,t.NUM,t.SHOULD_RMB,t.PRICE ");
				sql.append(" from BIS_STANDING_BOOK t where t.if_receive=2 ");
				if(reportType!=null&&!"".equals(reportType)){
		   			   parme.put("type", reportType);
			           sql.append(" and t.bis_type=:type ");
				}
				sql.append(" ) B on instr(A.STANDINGBOOKIDS,to_char(B.STANDING_NUM))>0  )");
			}
			sql.append(" ) order by CLIENT,lx,drostockinname,droenterstocktime,FEE_PLAN,FEE_NAME  ");
			
			SQLQuery sqlQuery=createSQLQuery(sql.toString(), parme);
			if(pageSize>0){
				long totalCount = countSqlResult(sql.toString(), parme);
				sqlQuery.setFirstResult(PageUtils.calBeginIndex(npage, pageSize,Integer.valueOf(String.valueOf(totalCount))));
				sqlQuery.setMaxResults(pageSize);
				returnMap.put("total", totalCount);
			}
			returnMap.put("rows", sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list());	 
		return returnMap;
	}
	/**
	 * 获取客户的名称集合
	 * @param reportType
	 * @param clientId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public List<String> findCustList(String reportType,String clientId, String startTime,String endTime){
		StringBuffer sb=new StringBuffer();
		HashMap<String,Object> parme=new HashMap<String,Object>();
		if(clientId!=null && !"".equals(clientId)) {
		   parme.put("CLIENTID", clientId); 
		}
		if(startTime!=null && !"".equals(startTime)){ 	
		   parme.put("STARTIME", startTime);
		}
		if(endTime!=null && !"".equals(endTime)){ 
		   parme.put("ENDTIME", endTime); 
		}
		sb.append(" SELECT                                                                             ");
		sb.append("  DISTINCT temp.CLIENT                                                              ");
		sb.append(" FROM                                                                               ");
		sb.append(" 	(                                                                              ");
		//入库
		if(reportType.trim().length()<=0 ||"1".equals(reportType.trim()) ) {
			sb.append(" 		(                                                                          ");
			sb.append(" 			SELECT                                                                 ");
			sb.append(" 				A .client                                                          ");
			sb.append(" 			FROM                                                                   ");
			sb.append(" 				(                                                                  ");
			sb.append(" 					SELECT                                                         ");
			sb.append(" 						T .*                                                       ");
			sb.append(" 					FROM                                                           ");
			sb.append(" 						bis_enter_stevedoring T                                    ");
			sb.append(" 					WHERE                                                          ");
			sb.append(" 						T .IF_OK = '1'                                             ");
			sb.append(" 					AND T .DROENTERSTOCKTIME IS NOT NULL                           ");
			if(clientId!=null && !"".equals(clientId)){
				   sb.append(" AND T.client_id=:CLIENTID                               ");
			}
			if(startTime!=null && !"".equals(startTime)){
				   sb.append(" AND T.DROENTERSTOCKTIME>=to_date(:STARTIME,'yyyy-mm-dd') "); 
			}
			if(endTime!=null && !"".equals(endTime)){
				   sb.append(" AND T.DROENTERSTOCKTIME<=to_date(:ENDTIME,'yyyy-mm-dd')                            "); 
			}
			sb.append(" 				) A                                                                       ");
			sb.append(" 		)                                                                                 ");
		}
		if(reportType.trim().length()<=0){  
			sb.append(" 		UNION                                                                             ");
		}
		//出库
		if(reportType.trim().length()<=0 ||"2".equals(reportType.trim()) ) {
			sb.append(" 			(                                                                             ");
			sb.append(" 				SELECT                                                                    ");
			sb.append(" 					A .client                                                             ");
			sb.append(" 				FROM                                                                      ");
			sb.append(" 					(                                                                     ");
			sb.append(" 						SELECT                                                            ");
			sb.append(" 							T .*                                                          ");
			sb.append(" 						FROM                                                              ");
			sb.append(" 							bis_out_stevedoring T                                         ");
			sb.append(" 						WHERE                                                             ");
			sb.append(" 							T .IF_OK = '1'                                                ");
			sb.append(" 						AND T .DROLOADINGTIME IS NOT NULL                                 ");
			if(clientId!=null && !"".equals(clientId)){ 
				   sb.append(" AND T.client_id=:CLIENTID  ");  
			}
		    if(startTime!=null && !"".equals(startTime)){
		    	   sb.append(" AND T.DROLOADINGTIME>= to_date(:STARTIME,'yyyy-mm-dd')                            "); 
			}
			if(endTime!=null && !"".equals(endTime)){
				   sb.append(" AND T.DROLOADINGTIME<=to_date(:ENDTIME,'yyyy-mm-dd')                              "); 
			}
			sb.append(" 					) A                                                                  ");
			sb.append(" 			)                                                                            ");
		}
		if(reportType.trim().length()<=0){  
			sb.append(" 		UNION                                                                             ");
		}
		//倒箱
		if(reportType.trim().length()<=0 ||"3".equals(reportType.trim()) ) {
			sb.append(" 			(                                                                            ");
			sb.append(" 				SELECT                                                                   ");
			sb.append(" 					A .client                                                            ");
			sb.append(" 				FROM                                                                     ");
			sb.append(" 					(                                                                    ");
			sb.append(" 						SELECT                                                           ");
			sb.append(" 							T .*                                                         ");
			sb.append(" 						FROM                                                             ");
			sb.append(" 							bis_back_stevedoring T                                       ");
			sb.append(" 						WHERE                                                            ");
			sb.append(" 							T .IF_OK = '1'                                               ");
			sb.append(" 						AND T .IF_ALL_MAN = 1                                            ");
			sb.append(" 						AND T .DROBACKDATE IS NOT NULL                                   ");
			if(clientId!=null && !"".equals(clientId)){
				   sb.append(" AND T.client_id=:CLIENTID   "); 
			}
			if(startTime!=null && !"".equals(startTime)){
				   sb.append(" AND T.DROBACKDATE>= to_date(:STARTIME,'yyyy-mm-dd') ");
			}
			if(endTime!=null && !"".equals(endTime)){
				   sb.append(" AND T.DROBACKDATE<= to_date(:ENDTIME,'yyyy-mm-dd') ");
			}
			sb.append(" 					) A                                                                  ");
			sb.append(" 			)                                                                            ");
		}
		if(reportType.trim().length()<=0){
			sb.append(" 		UNION                                                                            ");
		}
		//日工
		if(reportType.trim().length()<=0 ||"4".equals(reportType.trim()) ){
			sb.append(" 			(                                                                            ");
			sb.append(" 				SELECT                                                                   ");
			sb.append(" 					D .CLIENT                                                            ");
			sb.append(" 				FROM                                                                     ");
			sb.append(" 					(                                                                    ");
			sb.append(" 						SELECT                                                           ");
			sb.append(" 							T .*                                                         ");
			sb.append(" 						FROM                                                             ");
			sb.append(" 							bis_loading_team_daywork T                                   ");
			sb.append(" 						WHERE                                                            ");
			sb.append(" 							T .state = 1                                                 ");
			sb.append(" 						AND T .del_flag = 0                                              ");
			sb.append(" 						AND T .loading_date IS NOT NULL                                  ");
			if(clientId!=null && !"".equals(clientId)){
	                sb.append(" AND T.client_id=:CLIENTID ");
			}
			if(startTime!=null && !"".equals(startTime)){ 	 
					sb.append(" AND T.loading_date>= to_date(:STARTIME,'yyyy-mm-dd') ");
			}
			if(endTime!=null && !"".equals(endTime)){   
					sb.append(" AND T.loading_date>= to_date(:ENDTIME,'yyyy-mm-dd') ");
			}
			sb.append(" 					) D                                                                  ");
			sb.append(" 			)                                                                            ");
		}
		if(reportType.trim().length()<=0){
		  sb.append(" 		UNION                                                                            ");
		}
		//缠膜
		if(reportType.trim().length()<=0 ||"5".equals(reportType.trim())||"7".equals(reportType.trim())||"8".equals(reportType.trim())||"21".equals(reportType.trim())||"22".equals(reportType.trim())||"23".equals(reportType.trim())||"24".equals(reportType.trim())||"25".equals(reportType.trim())) {
			sb.append(" 			(                                                                            ");
			sb.append(" 				SELECT                                                                   ");
			sb.append(" 					A .client                                                            ");
			sb.append(" 				FROM                                                                     ");
			sb.append(" 					(                                                                    ");
			sb.append(" 						SELECT                                                                 ");
			sb.append(" 							T .*                                                                 ");
			sb.append(" 						FROM                                                                   ");
			sb.append(" 							bis_back_stevedoring T                                               ");
			sb.append(" 						WHERE                                                                  ");
			sb.append(" 							T .IF_OK = '1'                                                       ");
			if(clientId!=null && !"".equals(clientId)){ 
				   sb.append(" AND T.client_id=:CLIENTID ");
			}
			if(startTime!=null && !"".equals(startTime)){  
				   sb.append(" AND  T.DROBACKDATE>= to_date(:STARTIME,'yyyy-mm-dd') ");
			}
			if(endTime!=null && !"".equals(endTime)){  
				   sb.append(" AND T.DROBACKDATE<=to_date(:ENDTIME,'yyyy-mm-dd')  ");
			}
			sb.append(" 					) A                                                                      ");
			sb.append(" 			)                                                                            ");
		}
		sb.append(" 	) temp                                                                                  ");
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
		return sqlQuery.list();
	}
    
    @SuppressWarnings("unchecked")
	public List<Object[]> findSumList(List<String> custlist,String reportType,String clientId, String startTime,String endTime){
		StringBuffer sb=new StringBuffer();
		StringBuffer sum=new StringBuffer();
		StringBuffer rmb=new StringBuffer();
		StringBuffer zong=new StringBuffer();
		HashMap<String,Object> parme=new HashMap<String,Object>();
		if(clientId!=null && !"".equals(clientId)) {
		   parme.put("CLIENTID", clientId); 
		}
		if(startTime!=null && !"".equals(startTime)){ 	
		   parme.put("STARTIME", startTime);
		}
		if(endTime!=null && !"".equals(endTime)){ 
		   parme.put("ENDTIME", endTime); 
		}
		sb.append(" SELECT                            ");
		for (int i = 0; i < custlist.size(); i++) {
			zong.append(" SUM(NVL(ku"+i+"_NUM,0)), " );
			zong.append(" SUM(NVL(ku"+i+"_SHOULD_RMB, 0)), ");
		}
		sb.append( zong.toString() );
		sb.append("   SUM(NVL(sumnum, 0)),           ");
		sb.append("   SUM(NVL(sumrmb, 0))            ");
		sb.append(" FROM (                           ");
			sb.append(" SELECT                                                                                                            ");                                                 
			sb.append("   LX,                                                                                                             ");
			sb.append("   FEE_PLAN,                                                                                                       ");
			sb.append("   PRICE,                                                                                                          ");
			sb.append("   LABEL,                                                                                                          ");
			sum.append(" (                                                                                                                 ");
			rmb.append(" (                                                                                                                 ");
			for (int i = 0; i < custlist.size(); i++) {
				sb.append("ku"+i+"_NUM,");
				sb.append("ku"+i+"_SHOULD_RMB,");
				sum.append(" NVL(ku"+i+"_NUM,0) " );
				rmb.append(" NVL(ku"+i+"_SHOULD_RMB, 0) ");
				if(i!=custlist.size()-1){
					sum.append(" + " );
					rmb.append(" + " );
				}
			}
			sum.append(" )  sumnum ,                                                                                                     ");
			rmb.append(" )  sumrmb                                                                                                       ");
			//sb.append("   ku1_NUM,                                                                                                        ");
			//sb.append("   ku1_SHOULD_RMB,                                                                                                 ");
			//sb.append("   ku2_NUM,                                                                                                        ");
			//sb.append("   ku2_SHOULD_RMB,                                                                                                 ");
			//sb.append("   (NVL(ku1_NUM, 0)+NVL(ku2_NUM, 0)) sumnum,                                                                       ");
			//sb.append("   (NVL(ku1_SHOULD_RMB, 0)+NVL(ku2_SHOULD_RMB, 0)) sumrmb                                                          ");
			sb.append( sum.toString());
			sb.append( rmb.toString());
			sb.append(" FROM                                                                                                              ");
			sb.append(" 	(                                                                                                               ");
			sb.append(" 	  SELECT                                                                                                        ");
			sb.append(" 	  temp.LX,                                                                                                    ");
			sb.append("       temp.CLIENT,                                                                                                ");
			sb.append("       temp.FEE_PLAN,                                                                                              ");
			sb.append("       temp.PRICE,                                                                                                 ");
			sb.append("       unit.LABEL,                                                                                                 ");
			sb.append("       sum(temp.NUM) NUM,                                                                                          ");
			sb.append("       sum(temp.SHOULD_RMB) SHOULD_RMB                                                                             ");
			sb.append(" 	FROM                                                                                                          ");
			sb.append(" 			(                                                                                                           ");
			//入库
			if(reportType.trim().length()<=0 ||"1".equals(reportType.trim()) ) {
				sb.append(" 				(                                                                                                         ");
				sb.append(" 					SELECT DISTINCT                                                                                         ");
				sb.append(" 						'入库' AS lx,                                                                                         ");
				sb.append(" 						A .client,                                                                                            ");
				sb.append(" 						A .fee_plan,                                                                                          ");
				sb.append(" 						'' AS loading_num,                                                                                    ");
				sb.append(" 						A .dromr,                                                                                             ");
				sb.append(" 						A .drostockinname,                                                                                    ");
				sb.append(" 						A .droenterstocktime,                                                                                 ");
				sb.append(" 						b.FEE_NAME,                                                                                           ");
				sb.append(" 						b.NUM,                                                                                                ");
				sb.append(" 						b.SHOULD_RMB,                                                                                         ");
				sb.append(" 						b.PRICE,                                                                                              ");
				sb.append(" 						b.LINK_ID AS ASN,                                                                                     ");
				sb.append(" 						b.BILL_NUM                                                                                            ");
				sb.append(" 					FROM                                                                                                    ");
				sb.append(" 						(                                                                                                     ");
				sb.append(" 							SELECT                                                                                              ");
				sb.append(" 								T .*                                                                                              ");
				sb.append(" 							FROM                                                                                                ");
				sb.append(" 								bis_enter_stevedoring T                                                                           ");
				sb.append(" 							WHERE                                                                                               ");
				sb.append(" 								T .IF_OK = '1'                                                                                    ");
				sb.append(" 							AND T .DROENTERSTOCKTIME IS NOT NULL                                                                ");
				if(clientId!=null && !"".equals(clientId)){
					   sb.append(" AND T.client_id=:CLIENTID                               ");
				}
				if(startTime!=null && !"".equals(startTime)){
					   sb.append(" AND T.DROENTERSTOCKTIME>=to_date(:STARTIME,'yyyy-mm-dd') "); 
				}
				if(endTime!=null && !"".equals(endTime)){
					   sb.append(" AND T.DROENTERSTOCKTIME<=to_date(:ENDTIME,'yyyy-mm-dd')                            "); 
				}
				sb.append(" 						) A                                                                                                   ");
				sb.append(" 					JOIN (                                                                                                  ");
				sb.append(" 						SELECT                                                                                                ");
				sb.append(" 							T .STANDING_NUM,                                                                                    ");
				sb.append(" 							T .CUSTOMS_NUM,                                                                                     ");
				sb.append(" 							T .BILL_NUM,                                                                                        ");
				sb.append(" 							T .LINK_ID,                                                                                         ");
				sb.append(" 							T .FEE_CODE,                                                                                        ");
				sb.append(" 							T .FEE_NAME,                                                                                        ");
				sb.append(" 							T .bis_type,                                                                                        ");
				sb.append(" 							T .FEE_PLAN,                                                                                        ");
				sb.append(" 							T .ASN,                                                                                             ");
				sb.append(" 							T .IF_RECEIVE,                                                                                      ");
				sb.append(" 							T .NUM,                                                                                             ");
				sb.append(" 							T .SHOULD_RMB,                                                                                      ");
				sb.append(" 							T .PRICE                                                                                            ");
				sb.append(" 						FROM                                                                                                  ");
				sb.append(" 							BIS_STANDING_BOOK T                                                                                 ");
				sb.append(" 						WHERE                                                                                                 ");
				sb.append(" 							T .if_receive = 2                                                                                   ");
				sb.append(" 						AND T .bis_type IN (5, 6, 7, 8)                                                                       ");
				sb.append(" 					) B ON B.STANDING_NUM IN (NVL(A .drostanindbookids, 0))                                                 ");
				sb.append(" 				)                                                                                                         ");
			}
			if(reportType.trim().length()<=0){
			  sb.append(" 				UNION                                                                                                     ");
			}
			//出库
		    if(reportType.trim().length()<=0 ||"2".equals(reportType.trim()) ) {
				sb.append(" 					(                                                                                                       ");
				sb.append(" 						SELECT DISTINCT                                                                                       ");
				sb.append(" 							'出库' AS lx,                                                                                       ");
				sb.append(" 							A .client,                                                                                          ");
				sb.append(" 							A .fee_plan,                                                                                        ");
				sb.append(" 							TO_CHAR (NVL(A .loading_num, '')) AS loading_num,                                                   ");
				sb.append(" 							'' AS dromr,                                                                                        ");
				sb.append(" 							A .DROSTOCKIDNAME AS drostockinname,                                                                ");
				sb.append(" 							A .DROLOADINGTIME AS droenterstocktime,                                                             ");
				sb.append(" 							b.FEE_NAME,                                                                                         ");
				sb.append(" 							b.NUM,                                                                                              ");
				sb.append(" 							b.SHOULD_RMB,                                                                                       ");
				sb.append(" 							b.PRICE,                                                                                            ");
				sb.append(" 							b.LINK_ID AS ASN,                                                                                   ");
				sb.append(" 							b.BILL_NUM AS BILL_NUM                                                                              ");
				sb.append(" 						FROM                                                                                                  ");
				sb.append(" 							(                                                                                                   ");
				sb.append(" 								SELECT                                                                                            ");
				sb.append(" 									T .*                                                                                            ");
				sb.append(" 								FROM                                                                                              ");
				sb.append(" 									bis_out_stevedoring T                                                                           ");
				sb.append(" 								WHERE                                                                                             ");
				sb.append(" 									T .IF_OK = '1'                                                                                  ");
				sb.append(" 								AND T .DROLOADINGTIME IS NOT NULL                                                                 ");
				if(clientId!=null && !"".equals(clientId)){ 
					   sb.append(" AND T.client_id=:CLIENTID  ");  
				}
			    if(startTime!=null && !"".equals(startTime)){
			    	   sb.append(" AND T.DROLOADINGTIME>= to_date(:STARTIME,'yyyy-mm-dd')                            "); 
				}
				if(endTime!=null && !"".equals(endTime)){
					   sb.append(" AND T.DROLOADINGTIME<=to_date(:ENDTIME,'yyyy-mm-dd')                              "); 
				}
				sb.append(" 							) A                                                                                                 ");
				sb.append(" 						JOIN (                                                                                                ");
				sb.append(" 							SELECT                                                                                              ");
				sb.append(" 								T .STANDING_NUM,                                                                                  ");
				sb.append(" 								T .CUSTOMS_NUM,                                                                                   ");
				sb.append(" 								T .BILL_NUM,                                                                                      ");
				sb.append(" 								T .LINK_ID,                                                                                       ");
				sb.append(" 								T .FEE_CODE,                                                                                      ");
				sb.append(" 								T .FEE_NAME,                                                                                      ");
				sb.append(" 								T .bis_type,                                                                                      ");
				sb.append(" 								T .FEE_PLAN,                                                                                      ");
				sb.append(" 								T .ASN,                                                                                           ");
				sb.append(" 								T .IF_RECEIVE,                                                                                    ");
				sb.append(" 								T .NUM,                                                                                           ");
				sb.append(" 								T .SHOULD_RMB,                                                                                    ");
				sb.append(" 								T .PRICE                                                                                          ");
				sb.append(" 							FROM                                                                                                ");
				sb.append(" 								BIS_STANDING_BOOK T                                                                               ");
				sb.append(" 							WHERE                                                                                               ");
				sb.append(" 								T .if_receive = 2                                                                                 ");
				sb.append(" 							AND T .bis_type IN (5, 6, 7, 8)                                                                     ");
				sb.append(" 						) B ON B.STANDING_NUM IN (A .drostanindbookids)                                                       ");
				sb.append(" 					)                                                                                                       ");
		    }
		    if(reportType.trim().length()<=0){
				  sb.append(" 				UNION                                                                                                     ");
			}
			//倒箱
			if(reportType.trim().length()<=0 ||"3".equals(reportType.trim()) ) {
				sb.append(" 					(                                                                                                       ");
				sb.append(" 						SELECT DISTINCT                                                                                       ");
				sb.append(" 							'倒箱' AS lx,                                                                                       ");
				sb.append(" 							A .client,                                                                                          ");
				sb.append(" 							A .fee_plan,                                                                                        ");
				sb.append(" 							'' AS loading_num,                                                                                  ");
				sb.append(" 							A .mr AS dromr,                                                                                     ");
				sb.append(" 							A .DROSTOCKIN AS drostockinname,                                                                    ");
				sb.append(" 							A .DROBACKDATE AS droenterstocktime,                                                                ");
				sb.append(" 							b.FEE_NAME,                                                                                         ");
				sb.append(" 							b.NUM,                                                                                              ");
				sb.append(" 							b.SHOULD_RMB,                                                                                       ");
				sb.append(" 							b.PRICE,                                                                                            ");
				sb.append(" 							b.LINK_ID AS ASN,                                                                                   ");
				sb.append(" 							A .BILL_NUM AS BILL_NUM                                                                             ");
				sb.append(" 						FROM                                                                                                  ");
				sb.append(" 							(                                                                                                   ");
				sb.append(" 								SELECT                                                                                            ");
				sb.append(" 									T .*                                                                                            ");
				sb.append(" 								FROM                                                                                              ");
				sb.append(" 									bis_back_stevedoring T                                                                          ");
				sb.append(" 								WHERE                                                                                             ");
				sb.append(" 									T .IF_OK = '1'                                                                                  ");
				sb.append(" 								AND T .IF_ALL_MAN = 1                                                                             ");
				sb.append(" 								AND T .DROBACKDATE IS NOT NULL                                                                    ");
				if(clientId!=null && !"".equals(clientId)){
					   sb.append(" AND T.client_id=:CLIENTID   "); 
				}
				if(startTime!=null && !"".equals(startTime)){
					   sb.append(" AND T.DROBACKDATE>= to_date(:STARTIME,'yyyy-mm-dd') ");
				}
				if(endTime!=null && !"".equals(endTime)){
					   sb.append(" AND T.DROBACKDATE<= to_date(:ENDTIME,'yyyy-mm-dd') ");
				}
				sb.append(" 							) A                                                                                                 ");
				sb.append(" 						JOIN (                                                                                                ");
				sb.append(" 							SELECT                                                                                              ");
				sb.append(" 								T .STANDING_NUM,                                                                                  ");
				sb.append(" 								T .CUSTOMS_NUM,                                                                                   ");
				sb.append(" 								T .BILL_NUM,                                                                                      ");
				sb.append(" 								T .LINK_ID,                                                                                       ");
				sb.append(" 								T .FEE_CODE,                                                                                      ");
				sb.append(" 								T .FEE_NAME,                                                                                      ");
				sb.append(" 								T .bis_type,                                                                                      ");
				sb.append(" 								T .FEE_PLAN,                                                                                      ");
				sb.append(" 								T .ASN,                                                                                           ");
				sb.append(" 								T .IF_RECEIVE,                                                                                    ");
				sb.append(" 								T .NUM,                                                                                           ");
				sb.append(" 								T .SHOULD_RMB,                                                                                    ");
				sb.append(" 								T .PRICE                                                                                          ");
				sb.append(" 							FROM                                                                                                ");
				sb.append(" 								BIS_STANDING_BOOK T                                                                               ");
				sb.append(" 							WHERE                                                                                               ");
				sb.append(" 								T .if_receive = 2                                                                                 ");
				sb.append(" 							AND T .bis_type = 6                                                                                 ");
				sb.append(" 						) B ON INSTR (                                                                                        ");
				sb.append(" 							A .STANDINGBOOKIDS,                                                                                 ");
				sb.append(" 							TO_CHAR (B.STANDING_NUM)                                                                            ");
				sb.append(" 						) > 0                                                                                                 ");
				sb.append(" 					)                                                                                                       ");
			}
			if(reportType.trim().length()<=0){
				  sb.append(" 				UNION                                                                                                     ");
			}
			//日工
			if(reportType.trim().length()<=0 ||"4".equals(reportType.trim()) ){
				sb.append(" 					(                                                                                                       ");
				sb.append(" 						SELECT                                                                                                ");
				sb.append(" 							'日工' AS lx,                                                                                       ");
				sb.append(" 							D .CLIENT,                                                                                          ");
				sb.append(" 							D .SCHEME_NAME AS fee_plan,                                                                         ");
				sb.append(" 							'' AS loading_num,                                                                                  ");
				sb.append(" 							'' AS dromr,                                                                                        ");
				sb.append(" 							'' AS drostockinname,                                                                               ");
				sb.append(" 							D .loading_date AS droenterstocktime,                                                               ");
				sb.append(" 							c.FEE_NAME,                                                                                         ");
				sb.append(" 							SUM (c.NUM) AS NUM,                                                                                 ");
				sb.append(" 							SUM (c.SHOULD_RMB) AS SHOULD_RMB,                                                                   ");
				sb.append(" 							c.PRICE,                                                                                            ");
				sb.append(" 							'' AS ASN,                                                                                          ");
				sb.append(" 							'' AS BILL_NUM                                                                                      ");
				sb.append(" 						FROM                                                                                                  ");
				sb.append(" 							(                                                                                                   ");
				sb.append(" 								SELECT                                                                                            ");
				sb.append(" 									T .*                                                                                            ");
				sb.append(" 								FROM                                                                                              ");
				sb.append(" 									bis_loading_team_daywork T                                                                      ");
				sb.append(" 								WHERE                                                                                             ");
				sb.append(" 									T .state = 1                                                                                    ");
				sb.append(" 								AND T .del_flag = 0                                                                               ");
				sb.append(" 								AND T .loading_date IS NOT NULL                                                                   ");
				if(clientId!=null && !"".equals(clientId)){
	                    sb.append(" AND T.client_id=:CLIENTID ");
				}
				if(startTime!=null && !"".equals(startTime)){ 	 
						sb.append(" AND T.loading_date>= to_date(:STARTIME,'yyyy-mm-dd') ");
				}
				if(endTime!=null && !"".equals(endTime)){   
						sb.append(" AND T.loading_date>= to_date(:ENDTIME,'yyyy-mm-dd') ");
				}
				sb.append(" 							) D                                                                                                 ");
				sb.append(" 						LEFT JOIN (                                                                                           ");
				sb.append(" 							SELECT                                                                                              ");
				sb.append(" 								T .STANDING_NUM,                                                                                  ");
				sb.append(" 								T .CUSTOMS_NUM,                                                                                   ");
				sb.append(" 								T .BILL_NUM,                                                                                      ");
				sb.append(" 								T .LINK_ID,                                                                                       ");
				sb.append(" 								T .FEE_CODE,                                                                                      ");
				sb.append(" 								T .FEE_NAME,                                                                                      ");
				sb.append(" 								T .bis_type,                                                                                      ");
				sb.append(" 								T .FEE_PLAN,                                                                                      ");
				sb.append(" 								T .ASN,                                                                                           ");
				sb.append(" 								T .IF_RECEIVE,                                                                                    ");
				sb.append(" 								T .NUM,                                                                                           ");
				sb.append(" 								T .SHOULD_RMB,                                                                                    ");
				sb.append(" 								T .PRICE                                                                                          ");
				sb.append(" 							FROM                                                                                                ");
				sb.append(" 								BIS_STANDING_BOOK T                                                                               ");
				sb.append(" 							WHERE                                                                                               ");
				sb.append(" 								T .if_receive = 2                                                                                 ");
				sb.append(" 						) c ON D .STANDING_NUM = c.STANDING_NUM                                                               ");
				sb.append(" 						GROUP BY                                                                                              ");
				sb.append(" 							D .client_id,                                                                                       ");
				sb.append(" 							D .CLIENT,                                                                                          ");
				sb.append(" 							D .SCHEME_NAME,                                                                                     ");
				sb.append(" 							D .loading_date,                                                                                    ");
				sb.append(" 							c.FEE_PLAN,                                                                                         ");
				sb.append(" 							c.FEE_NAME,                                                                                         ");
				sb.append(" 							c.PRICE                                                                                             ");
				sb.append(" 					)                                                                                                       ");
			}
			if(reportType.trim().length()<=0){
				sb.append(" 				UNION                                                                                                     ");
			}
			//缠膜
			if(reportType.trim().length()<=0 ||"5".equals(reportType.trim())||"7".equals(reportType.trim())||"8".equals(reportType.trim())||"21".equals(reportType.trim())||"22".equals(reportType.trim())||"23".equals(reportType.trim())||"24".equals(reportType.trim())||"25".equals(reportType.trim())) {
				sb.append(" 					(                                                                                                       ");
				sb.append(" 						SELECT                                                                                                ");
				sb.append(" 							(                                                                                                   ");
				sb.append(" 								CASE b.bis_type                                                                                   ");
				sb.append(" 								WHEN '5' THEN                                                                                     ");
				sb.append(" 									'在库分拣'                                                                                      ");
				sb.append(" 								WHEN '7' THEN                                                                                     ");
				sb.append(" 									'缠膜'                                                                                          ");
				sb.append(" 								WHEN '8' THEN                                                                                     ");
				sb.append(" 									'打包'                                                                                          ");
				sb.append(" 								WHEN '21' THEN                                                                                    ");
				sb.append(" 									'内标签'                                                                                        ");
				sb.append(" 								WHEN '22' THEN                                                                                    ");
				sb.append(" 									'外标签'                                                                                        ");
				sb.append(" 								WHEN '23' THEN                                                                                    ");
				sb.append(" 									'码托'                                                                                          ");
				sb.append(" 								WHEN '24' THEN                                                                                    ");
				sb.append(" 									'装铁架'                                                                                        ");
				sb.append(" 								WHEN '25' THEN                                                                                    ");
				sb.append(" 									'拆铁架'                                                                                        ");
				sb.append(" 								ELSE                                                                                              ");
				sb.append(" 									'其他'                                                                                          ");
				sb.append(" 								END                                                                                               ");
				sb.append(" 							) AS lx,                                                                                            ");
				sb.append(" 							A .client,                                                                                          ");
				sb.append(" 							A .fee_plan,                                                                                        ");
				sb.append(" 							'' AS loading_num,                                                                                  ");
				sb.append(" 							A .mr AS dromr,                                                                                     ");
				sb.append(" 							A .DROSTOCKIN AS drostockinname,                                                                    ");
				sb.append(" 							A .DROBACKDATE AS droenterstocktime,                                                                ");
				sb.append(" 							b.FEE_NAME,                                                                                         ");
				sb.append(" 							b.NUM,                                                                                              ");
				sb.append(" 							b.SHOULD_RMB,                                                                                       ");
				sb.append(" 							b.PRICE,                                                                                            ");
				sb.append(" 							b.LINK_ID AS ASN,                                                                                   ");
				sb.append(" 							A .BILL_NUM AS BILL_NUM                                                                             ");
				sb.append(" 						FROM                                                                                                  ");
				sb.append(" 							(                                                                                                   ");
				sb.append(" 								SELECT                                                                                            ");
				sb.append(" 									T .*                                                                                            ");
				sb.append(" 								FROM                                                                                              ");
				sb.append(" 									bis_back_stevedoring T                                                                          ");
				sb.append(" 								WHERE                                                                                             ");
				sb.append(" 									T .IF_OK = '1'                                                                                  ");
				if(clientId!=null && !"".equals(clientId)){ 
					   sb.append(" AND T.client_id=:CLIENTID ");
				}
				if(startTime!=null && !"".equals(startTime)){  
					   sb.append(" AND  T.DROBACKDATE>= to_date(:STARTIME,'yyyy-mm-dd') ");
				}
				if(endTime!=null && !"".equals(endTime)){  
					   sb.append(" AND T.DROBACKDATE<=to_date(:ENDTIME,'yyyy-mm-dd')  ");
				}
				sb.append(" 							) A                                                                                                 ");
				sb.append(" 						JOIN (                                                                                                ");
				sb.append(" 							SELECT                                                                                              ");
				sb.append(" 								T .STANDING_NUM,                                                                                  ");
				sb.append(" 								T .CUSTOMS_NUM,                                                                                   ");
				sb.append(" 								T .BILL_NUM,                                                                                      ");
				sb.append(" 								T .LINK_ID,                                                                                       ");
				sb.append(" 								T .FEE_CODE,                                                                                      ");
				sb.append(" 								T .FEE_NAME,                                                                                      ");
				sb.append(" 								T .bis_type,                                                                                      ");
				sb.append(" 								T .FEE_PLAN,                                                                                      ");
				sb.append(" 								T .ASN,                                                                                           ");
				sb.append(" 								T .IF_RECEIVE,                                                                    ");
				sb.append(" 								T .NUM,                                                                           ");
				sb.append(" 								T .SHOULD_RMB,                                                                    ");
				sb.append(" 								T .PRICE                                                                          ");
				sb.append(" 							FROM                                                                                  ");
				sb.append(" 								BIS_STANDING_BOOK T                                                               ");
				sb.append(" 							WHERE                                                                                 ");
				sb.append(" 								T .if_receive = 2                                                                 ");
				if(reportType!=null&&!"".equals(reportType)){
		   			   parme.put("type", reportType);
			           sb.append(" and T.bis_type=:type ");
				}
				sb.append(" 						) B ON INSTR (                                                                            ");
				sb.append(" 							A .STANDINGBOOKIDS,                                                                   ");
				sb.append(" 							TO_CHAR (B.STANDING_NUM)                                                              ");
				sb.append(" 						) > 0                                                                                     ");
				sb.append(" 					)                                                                                             ");
			}
			sb.append(" 			) temp                                                                                                ");
			sb.append("     LEFT JOIN                                                                                                     ");
			sb.append("     (                                                                                                             ");
			sb.append(" 	  select                                                                                               ");
			sb.append("          feecode.NAME_C,                                                                                          ");
			sb.append("          unit.LABEL                                                                                               ");
			sb.append("        from                                                                                                       ");
			sb.append(" 		 BASE_EXPENSE_CATEGORY_INFO feecode                                                                ");
			sb.append(" 	   LEFT JOIN                                                                                            ");
			sb.append(" 		  (SELECT unit.LABEL,unit.VALUE from DICT unit where unit.TYPE='units') unit                        ");
			sb.append(" 		ON feecode.units = unit.VALUE                                                                         ");
			sb.append("     ) unit                                                                                                        ");
			sb.append("     ON temp.FEE_NAME=unit.NAME_C                                                                                 ");
			sb.append("     GROUP BY                                                                                                      ");
			sb.append("       TEMP.LX,                                                                                                    ");
			sb.append("       TEMP.CLIENT,                                                                                                ");
			sb.append("       TEMP.FEE_PLAN,                                                                                              ");
			sb.append("       TEMP.PRICE,                                                                                                 ");
			sb.append("       unit.LABEL                                                                                                  ");
			sb.append(" 	) pivot(sum(NUM) NUM,sum(SHOULD_RMB) SHOULD_RMB for CLIENT in(                                                ");
			for (int i = 0; i < custlist.size(); i++) {
				sb.append("'"+custlist.get(i)+"' ku"+i);
				if(i!=custlist.size()-1){
					sb.append(" ,");
				}
			}
			sb.append("  ))           ");
			sb.append("   WHERE LX<>'其他'                                                                                                ");
			sb.append("   ORDER BY                                                                                                        ");
			sb.append("       LX                                                                                                          ");
		sb.append(" ) temp                                                                                               ");
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
		return sqlQuery.list();
	}
    
    
    
    @SuppressWarnings("unchecked")
	public List<Object[]> findLxList(List<String> custlist,String reportType,String clientId, String startTime,String endTime){
		StringBuffer sb=new StringBuffer();
		StringBuffer sum=new StringBuffer();
		StringBuffer rmb=new StringBuffer();
		StringBuffer zong=new StringBuffer();
		HashMap<String,Object> parme=new HashMap<String,Object>();
		if(clientId!=null && !"".equals(clientId)) {
		   parme.put("CLIENTID", clientId); 
		}
		if(startTime!=null && !"".equals(startTime)){ 	
		   parme.put("STARTIME", startTime);
		}
		if(endTime!=null && !"".equals(endTime)){ 
		   parme.put("ENDTIME", endTime); 
		}
		sb.append(" SELECT                            ");
		sb.append("   LX,LABEL,                       ");
		sb.append("   COUNT(LX),LX||'-'||LABEL  MKEY, ");
		for (int i = 0; i < custlist.size(); i++) {
			zong.append(" SUM(NVL(ku"+i+"_NUM,0)), " );
			zong.append(" SUM(NVL(ku"+i+"_SHOULD_RMB, 0)), ");
		}
		sb.append( zong.toString() );
		sb.append("   SUM(NVL(sumnum, 0)),           ");
		sb.append("   SUM(NVL(sumrmb, 0))            ");
		sb.append(" FROM (                           ");
			sb.append(" SELECT                                                                                                            ");                                                 
			sb.append("   LX,                                                                                                             ");
			sb.append("   FEE_PLAN,                                                                                                       ");
			sb.append("   PRICE,                                                                                                          ");
			sb.append("   LABEL,                                                                                                          ");
			sum.append(" (                                                                                                                 ");
			rmb.append(" (                                                                                                                 ");
			for (int i = 0; i < custlist.size(); i++) {
				sb.append("ku"+i+"_NUM,");
				sb.append("ku"+i+"_SHOULD_RMB,");
				sum.append(" NVL(ku"+i+"_NUM,0) " );
				rmb.append(" NVL(ku"+i+"_SHOULD_RMB, 0) ");
				if(i!=custlist.size()-1){
					sum.append(" + " );
					rmb.append(" + " );
				}
			}
			sum.append(" )  sumnum ,                                                                                                     ");
			rmb.append(" )  sumrmb                                                                                                       ");
			//sb.append("   ku1_NUM,                                                                                                        ");
			//sb.append("   ku1_SHOULD_RMB,                                                                                                 ");
			//sb.append("   ku2_NUM,                                                                                                        ");
			//sb.append("   ku2_SHOULD_RMB,                                                                                                 ");
			//sb.append("   (NVL(ku1_NUM, 0)+NVL(ku2_NUM, 0)) sumnum,                                                                       ");
			//sb.append("   (NVL(ku1_SHOULD_RMB, 0)+NVL(ku2_SHOULD_RMB, 0)) sumrmb                                                          ");
			sb.append( sum.toString());
			sb.append( rmb.toString());
			sb.append(" FROM                                                                                                              ");
			sb.append(" 	(                                                                                                               ");
			sb.append(" 	  SELECT                                                                                                        ");
			sb.append(" 	  temp.LX,                                                                                                    ");
			sb.append("       temp.CLIENT,                                                                                                ");
			sb.append("       temp.FEE_PLAN,                                                                                              ");
			sb.append("       temp.PRICE,                                                                                                 ");
			sb.append("       unit.LABEL,                                                                                                 ");
			sb.append("       sum(temp.NUM) NUM,                                                                                          ");
			sb.append("       sum(temp.SHOULD_RMB) SHOULD_RMB                                                                             ");
			sb.append(" 	FROM                                                                                                          ");
			sb.append(" 			(                                                                                                           ");
			//入库
			if(reportType.trim().length()<=0 ||"1".equals(reportType.trim()) ) {
				sb.append(" 				(                                                                                                         ");
				sb.append(" 					SELECT DISTINCT                                                                                         ");
				sb.append(" 						'入库' AS lx,                                                                                         ");
				sb.append(" 						A .client,                                                                                            ");
				sb.append(" 						A .fee_plan,                                                                                          ");
				sb.append(" 						'' AS loading_num,                                                                                    ");
				sb.append(" 						A .dromr,                                                                                             ");
				sb.append(" 						A .drostockinname,                                                                                    ");
				sb.append(" 						A .droenterstocktime,                                                                                 ");
				sb.append(" 						b.FEE_NAME,                                                                                           ");
				sb.append(" 						b.NUM,                                                                                                ");
				sb.append(" 						b.SHOULD_RMB,                                                                                         ");
				sb.append(" 						b.PRICE,                                                                                              ");
				sb.append(" 						b.LINK_ID AS ASN,                                                                                     ");
				sb.append(" 						b.BILL_NUM                                                                                            ");
				sb.append(" 					FROM                                                                                                    ");
				sb.append(" 						(                                                                                                     ");
				sb.append(" 							SELECT                                                                                              ");
				sb.append(" 								T .*                                                                                              ");
				sb.append(" 							FROM                                                                                                ");
				sb.append(" 								bis_enter_stevedoring T                                                                           ");
				sb.append(" 							WHERE                                                                                               ");
				sb.append(" 								T .IF_OK = '1'                                                                                    ");
				sb.append(" 							AND T .DROENTERSTOCKTIME IS NOT NULL                                                                ");
				if(clientId!=null && !"".equals(clientId)){
					   sb.append(" AND T.client_id=:CLIENTID                               ");
				}
				if(startTime!=null && !"".equals(startTime)){
					   sb.append(" AND T.DROENTERSTOCKTIME>=to_date(:STARTIME,'yyyy-mm-dd') "); 
				}
				if(endTime!=null && !"".equals(endTime)){
					   sb.append(" AND T.DROENTERSTOCKTIME<=to_date(:ENDTIME,'yyyy-mm-dd')                            "); 
				}
				sb.append(" 						) A                                                                                                   ");
				sb.append(" 					JOIN (                                                                                                  ");
				sb.append(" 						SELECT                                                                                                ");
				sb.append(" 							T .STANDING_NUM,                                                                                    ");
				sb.append(" 							T .CUSTOMS_NUM,                                                                                     ");
				sb.append(" 							T .BILL_NUM,                                                                                        ");
				sb.append(" 							T .LINK_ID,                                                                                         ");
				sb.append(" 							T .FEE_CODE,                                                                                        ");
				sb.append(" 							T .FEE_NAME,                                                                                        ");
				sb.append(" 							T .bis_type,                                                                                        ");
				sb.append(" 							T .FEE_PLAN,                                                                                        ");
				sb.append(" 							T .ASN,                                                                                             ");
				sb.append(" 							T .IF_RECEIVE,                                                                                      ");
				sb.append(" 							T .NUM,                                                                                             ");
				sb.append(" 							T .SHOULD_RMB,                                                                                      ");
				sb.append(" 							T .PRICE                                                                                            ");
				sb.append(" 						FROM                                                                                                  ");
				sb.append(" 							BIS_STANDING_BOOK T                                                                                 ");
				sb.append(" 						WHERE                                                                                                 ");
				sb.append(" 							T .if_receive = 2                                                                                   ");
				sb.append(" 						AND T .bis_type IN (5, 6, 7, 8)                                                                       ");
				sb.append(" 					) B ON B.STANDING_NUM IN (NVL(A .drostanindbookids, 0))                                                 ");
				sb.append(" 				)                                                                                                         ");
			}
			if(reportType.trim().length()<=0){
			  sb.append(" 				UNION                                                                                                     ");
			}
			//出库
		    if(reportType.trim().length()<=0 ||"2".equals(reportType.trim()) ) {
				sb.append(" 					(                                                                                                       ");
				sb.append(" 						SELECT DISTINCT                                                                                       ");
				sb.append(" 							'出库' AS lx,                                                                                       ");
				sb.append(" 							A .client,                                                                                          ");
				sb.append(" 							A .fee_plan,                                                                                        ");
				sb.append(" 							TO_CHAR (NVL(A .loading_num, '')) AS loading_num,                                                   ");
				sb.append(" 							'' AS dromr,                                                                                        ");
				sb.append(" 							A .DROSTOCKIDNAME AS drostockinname,                                                                ");
				sb.append(" 							A .DROLOADINGTIME AS droenterstocktime,                                                             ");
				sb.append(" 							b.FEE_NAME,                                                                                         ");
				sb.append(" 							b.NUM,                                                                                              ");
				sb.append(" 							b.SHOULD_RMB,                                                                                       ");
				sb.append(" 							b.PRICE,                                                                                            ");
				sb.append(" 							b.LINK_ID AS ASN,                                                                                   ");
				sb.append(" 							b.BILL_NUM AS BILL_NUM                                                                              ");
				sb.append(" 						FROM                                                                                                  ");
				sb.append(" 							(                                                                                                   ");
				sb.append(" 								SELECT                                                                                            ");
				sb.append(" 									T .*                                                                                            ");
				sb.append(" 								FROM                                                                                              ");
				sb.append(" 									bis_out_stevedoring T                                                                           ");
				sb.append(" 								WHERE                                                                                             ");
				sb.append(" 									T .IF_OK = '1'                                                                                  ");
				sb.append(" 								AND T .DROLOADINGTIME IS NOT NULL                                                                 ");
				if(clientId!=null && !"".equals(clientId)){ 
					   sb.append(" AND T.client_id=:CLIENTID  ");  
				}
			    if(startTime!=null && !"".equals(startTime)){
			    	   sb.append(" AND T.DROLOADINGTIME>= to_date(:STARTIME,'yyyy-mm-dd')                            "); 
				}
				if(endTime!=null && !"".equals(endTime)){
					   sb.append(" AND T.DROLOADINGTIME<=to_date(:ENDTIME,'yyyy-mm-dd')                              "); 
				}
				sb.append(" 							) A                                                                                                 ");
				sb.append(" 						JOIN (                                                                                                ");
				sb.append(" 							SELECT                                                                                              ");
				sb.append(" 								T .STANDING_NUM,                                                                                  ");
				sb.append(" 								T .CUSTOMS_NUM,                                                                                   ");
				sb.append(" 								T .BILL_NUM,                                                                                      ");
				sb.append(" 								T .LINK_ID,                                                                                       ");
				sb.append(" 								T .FEE_CODE,                                                                                      ");
				sb.append(" 								T .FEE_NAME,                                                                                      ");
				sb.append(" 								T .bis_type,                                                                                      ");
				sb.append(" 								T .FEE_PLAN,                                                                                      ");
				sb.append(" 								T .ASN,                                                                                           ");
				sb.append(" 								T .IF_RECEIVE,                                                                                    ");
				sb.append(" 								T .NUM,                                                                                           ");
				sb.append(" 								T .SHOULD_RMB,                                                                                    ");
				sb.append(" 								T .PRICE                                                                                          ");
				sb.append(" 							FROM                                                                                                ");
				sb.append(" 								BIS_STANDING_BOOK T                                                                               ");
				sb.append(" 							WHERE                                                                                               ");
				sb.append(" 								T .if_receive = 2                                                                                 ");
				sb.append(" 							AND T .bis_type IN (5, 6, 7, 8)                                                                     ");
				sb.append(" 						) B ON B.STANDING_NUM IN (A .drostanindbookids)                                                       ");
				sb.append(" 					)                                                                                                       ");
		    }
		    if(reportType.trim().length()<=0){
				  sb.append(" 				UNION                                                                                                     ");
			}
			//倒箱
			if(reportType.trim().length()<=0 ||"3".equals(reportType.trim()) ) {
				sb.append(" 					(                                                                                                       ");
				sb.append(" 						SELECT DISTINCT                                                                                       ");
				sb.append(" 							'倒箱' AS lx,                                                                                       ");
				sb.append(" 							A .client,                                                                                          ");
				sb.append(" 							A .fee_plan,                                                                                        ");
				sb.append(" 							'' AS loading_num,                                                                                  ");
				sb.append(" 							A .mr AS dromr,                                                                                     ");
				sb.append(" 							A .DROSTOCKIN AS drostockinname,                                                                    ");
				sb.append(" 							A .DROBACKDATE AS droenterstocktime,                                                                ");
				sb.append(" 							b.FEE_NAME,                                                                                         ");
				sb.append(" 							b.NUM,                                                                                              ");
				sb.append(" 							b.SHOULD_RMB,                                                                                       ");
				sb.append(" 							b.PRICE,                                                                                            ");
				sb.append(" 							b.LINK_ID AS ASN,                                                                                   ");
				sb.append(" 							A .BILL_NUM AS BILL_NUM                                                                             ");
				sb.append(" 						FROM                                                                                                  ");
				sb.append(" 							(                                                                                                   ");
				sb.append(" 								SELECT                                                                                            ");
				sb.append(" 									T .*                                                                                            ");
				sb.append(" 								FROM                                                                                              ");
				sb.append(" 									bis_back_stevedoring T                                                                          ");
				sb.append(" 								WHERE                                                                                             ");
				sb.append(" 									T .IF_OK = '1'                                                                                  ");
				sb.append(" 								AND T .IF_ALL_MAN = 1                                                                             ");
				sb.append(" 								AND T .DROBACKDATE IS NOT NULL                                                                    ");
				if(clientId!=null && !"".equals(clientId)){
					   sb.append(" AND T.client_id=:CLIENTID   "); 
				}
				if(startTime!=null && !"".equals(startTime)){
					   sb.append(" AND T.DROBACKDATE>= to_date(:STARTIME,'yyyy-mm-dd') ");
				}
				if(endTime!=null && !"".equals(endTime)){
					   sb.append(" AND T.DROBACKDATE<= to_date(:ENDTIME,'yyyy-mm-dd') ");
				}
				sb.append(" 							) A                                                                                                 ");
				sb.append(" 						JOIN (                                                                                                ");
				sb.append(" 							SELECT                                                                                              ");
				sb.append(" 								T .STANDING_NUM,                                                                                  ");
				sb.append(" 								T .CUSTOMS_NUM,                                                                                   ");
				sb.append(" 								T .BILL_NUM,                                                                                      ");
				sb.append(" 								T .LINK_ID,                                                                                       ");
				sb.append(" 								T .FEE_CODE,                                                                                      ");
				sb.append(" 								T .FEE_NAME,                                                                                      ");
				sb.append(" 								T .bis_type,                                                                                      ");
				sb.append(" 								T .FEE_PLAN,                                                                                      ");
				sb.append(" 								T .ASN,                                                                                           ");
				sb.append(" 								T .IF_RECEIVE,                                                                                    ");
				sb.append(" 								T .NUM,                                                                                           ");
				sb.append(" 								T .SHOULD_RMB,                                                                                    ");
				sb.append(" 								T .PRICE                                                                                          ");
				sb.append(" 							FROM                                                                                                ");
				sb.append(" 								BIS_STANDING_BOOK T                                                                               ");
				sb.append(" 							WHERE                                                                                               ");
				sb.append(" 								T .if_receive = 2                                                                                 ");
				sb.append(" 							AND T .bis_type = 6                                                                                 ");
				sb.append(" 						) B ON INSTR (                                                                                        ");
				sb.append(" 							A .STANDINGBOOKIDS,                                                                                 ");
				sb.append(" 							TO_CHAR (B.STANDING_NUM)                                                                            ");
				sb.append(" 						) > 0                                                                                                 ");
				sb.append(" 					)                                                                                                       ");
			}
			if(reportType.trim().length()<=0){
				  sb.append(" 				UNION                                                                                                     ");
			}
			//日工
			if(reportType.trim().length()<=0 ||"4".equals(reportType.trim()) ){
				sb.append(" 					(                                                                                                       ");
				sb.append(" 						SELECT                                                                                                ");
				sb.append(" 							'日工' AS lx,                                                                                       ");
				sb.append(" 							D .CLIENT,                                                                                          ");
				sb.append(" 							D .SCHEME_NAME AS fee_plan,                                                                         ");
				sb.append(" 							'' AS loading_num,                                                                                  ");
				sb.append(" 							'' AS dromr,                                                                                        ");
				sb.append(" 							'' AS drostockinname,                                                                               ");
				sb.append(" 							D .loading_date AS droenterstocktime,                                                               ");
				sb.append(" 							c.FEE_NAME,                                                                                         ");
				sb.append(" 							SUM (c.NUM) AS NUM,                                                                                 ");
				sb.append(" 							SUM (c.SHOULD_RMB) AS SHOULD_RMB,                                                                   ");
				sb.append(" 							c.PRICE,                                                                                            ");
				sb.append(" 							'' AS ASN,                                                                                          ");
				sb.append(" 							'' AS BILL_NUM                                                                                      ");
				sb.append(" 						FROM                                                                                                  ");
				sb.append(" 							(                                                                                                   ");
				sb.append(" 								SELECT                                                                                            ");
				sb.append(" 									T .*                                                                                            ");
				sb.append(" 								FROM                                                                                              ");
				sb.append(" 									bis_loading_team_daywork T                                                                      ");
				sb.append(" 								WHERE                                                                                             ");
				sb.append(" 									T .state = 1                                                                                    ");
				sb.append(" 								AND T .del_flag = 0                                                                               ");
				sb.append(" 								AND T .loading_date IS NOT NULL                                                                   ");
				if(clientId!=null && !"".equals(clientId)){
	                    sb.append(" AND T.client_id=:CLIENTID ");
				}
				if(startTime!=null && !"".equals(startTime)){ 	 
						sb.append(" AND T.loading_date>= to_date(:STARTIME,'yyyy-mm-dd') ");
				}
				if(endTime!=null && !"".equals(endTime)){   
						sb.append(" AND T.loading_date>= to_date(:ENDTIME,'yyyy-mm-dd') ");
				}
				sb.append(" 							) D                                                                                                 ");
				sb.append(" 						LEFT JOIN (                                                                                           ");
				sb.append(" 							SELECT                                                                                              ");
				sb.append(" 								T .STANDING_NUM,                                                                                  ");
				sb.append(" 								T .CUSTOMS_NUM,                                                                                   ");
				sb.append(" 								T .BILL_NUM,                                                                                      ");
				sb.append(" 								T .LINK_ID,                                                                                       ");
				sb.append(" 								T .FEE_CODE,                                                                                      ");
				sb.append(" 								T .FEE_NAME,                                                                                      ");
				sb.append(" 								T .bis_type,                                                                                      ");
				sb.append(" 								T .FEE_PLAN,                                                                                      ");
				sb.append(" 								T .ASN,                                                                                           ");
				sb.append(" 								T .IF_RECEIVE,                                                                                    ");
				sb.append(" 								T .NUM,                                                                                           ");
				sb.append(" 								T .SHOULD_RMB,                                                                                    ");
				sb.append(" 								T .PRICE                                                                                          ");
				sb.append(" 							FROM                                                                                                ");
				sb.append(" 								BIS_STANDING_BOOK T                                                                               ");
				sb.append(" 							WHERE                                                                                               ");
				sb.append(" 								T .if_receive = 2                                                                                 ");
				sb.append(" 						) c ON D .STANDING_NUM = c.STANDING_NUM                                                               ");
				sb.append(" 						GROUP BY                                                                                              ");
				sb.append(" 							D .client_id,                                                                                       ");
				sb.append(" 							D .CLIENT,                                                                                          ");
				sb.append(" 							D .SCHEME_NAME,                                                                                     ");
				sb.append(" 							D .loading_date,                                                                                    ");
				sb.append(" 							c.FEE_PLAN,                                                                                         ");
				sb.append(" 							c.FEE_NAME,                                                                                         ");
				sb.append(" 							c.PRICE                                                                                             ");
				sb.append(" 					)                                                                                                       ");
			}
			if(reportType.trim().length()<=0){
				sb.append(" 				UNION                                                                                                     ");
			}
			//缠膜
			if(reportType.trim().length()<=0 ||"5".equals(reportType.trim())||"7".equals(reportType.trim())||"8".equals(reportType.trim())||"21".equals(reportType.trim())||"22".equals(reportType.trim())||"23".equals(reportType.trim())||"24".equals(reportType.trim())||"25".equals(reportType.trim())) {
				sb.append(" 					(                                                                                                       ");
				sb.append(" 						SELECT                                                                                                ");
				sb.append(" 							(                                                                                                   ");
				sb.append(" 								CASE b.bis_type                                                                                   ");
				sb.append(" 								WHEN '5' THEN                                                                                     ");
				sb.append(" 									'在库分拣'                                                                                      ");
				sb.append(" 								WHEN '7' THEN                                                                                     ");
				sb.append(" 									'缠膜'                                                                                          ");
				sb.append(" 								WHEN '8' THEN                                                                                     ");
				sb.append(" 									'打包'                                                                                          ");
				sb.append(" 								WHEN '21' THEN                                                                                    ");
				sb.append(" 									'内标签'                                                                                        ");
				sb.append(" 								WHEN '22' THEN                                                                                    ");
				sb.append(" 									'外标签'                                                                                        ");
				sb.append(" 								WHEN '23' THEN                                                                                    ");
				sb.append(" 									'码托'                                                                                          ");
				sb.append(" 								WHEN '24' THEN                                                                                    ");
				sb.append(" 									'装铁架'                                                                                        ");
				sb.append(" 								WHEN '25' THEN                                                                                    ");
				sb.append(" 									'拆铁架'                                                                                        ");
				sb.append(" 								ELSE                                                                                              ");
				sb.append(" 									'其他'                                                                                          ");
				sb.append(" 								END                                                                                               ");
				sb.append(" 							) AS lx,                                                                                            ");
				sb.append(" 							A .client,                                                                                          ");
				sb.append(" 							A .fee_plan,                                                                                        ");
				sb.append(" 							'' AS loading_num,                                                                                  ");
				sb.append(" 							A .mr AS dromr,                                                                                     ");
				sb.append(" 							A .DROSTOCKIN AS drostockinname,                                                                    ");
				sb.append(" 							A .DROBACKDATE AS droenterstocktime,                                                                ");
				sb.append(" 							b.FEE_NAME,                                                                                         ");
				sb.append(" 							b.NUM,                                                                                              ");
				sb.append(" 							b.SHOULD_RMB,                                                                                       ");
				sb.append(" 							b.PRICE,                                                                                            ");
				sb.append(" 							b.LINK_ID AS ASN,                                                                                   ");
				sb.append(" 							A .BILL_NUM AS BILL_NUM                                                                             ");
				sb.append(" 						FROM                                                                                                  ");
				sb.append(" 							(                                                                                                   ");
				sb.append(" 								SELECT                                                                                            ");
				sb.append(" 									T .*                                                                                            ");
				sb.append(" 								FROM                                                                                              ");
				sb.append(" 									bis_back_stevedoring T                                                                          ");
				sb.append(" 								WHERE                                                                                             ");
				sb.append(" 									T .IF_OK = '1'                                                                                  ");
				if(clientId!=null && !"".equals(clientId)){ 
					   sb.append(" AND T.client_id=:CLIENTID ");
				}
				if(startTime!=null && !"".equals(startTime)){  
					   sb.append(" AND  T.DROBACKDATE>= to_date(:STARTIME,'yyyy-mm-dd') ");
				}
				if(endTime!=null && !"".equals(endTime)){  
					   sb.append(" AND T.DROBACKDATE<=to_date(:ENDTIME,'yyyy-mm-dd')  ");
				}
				sb.append(" 							) A                                                                                                 ");
				sb.append(" 						JOIN (                                                                                                ");
				sb.append(" 							SELECT                                                                                              ");
				sb.append(" 								T .STANDING_NUM,                                                                                  ");
				sb.append(" 								T .CUSTOMS_NUM,                                                                                   ");
				sb.append(" 								T .BILL_NUM,                                                                                      ");
				sb.append(" 								T .LINK_ID,                                                                                       ");
				sb.append(" 								T .FEE_CODE,                                                                                      ");
				sb.append(" 								T .FEE_NAME,                                                                                      ");
				sb.append(" 								T .bis_type,                                                                                      ");
				sb.append(" 								T .FEE_PLAN,                                                                                      ");
				sb.append(" 								T .ASN,                                                                                           ");
				sb.append(" 								T .IF_RECEIVE,                                                                    ");
				sb.append(" 								T .NUM,                                                                           ");
				sb.append(" 								T .SHOULD_RMB,                                                                    ");
				sb.append(" 								T .PRICE                                                                          ");
				sb.append(" 							FROM                                                                                  ");
				sb.append(" 								BIS_STANDING_BOOK T                                                               ");
				sb.append(" 							WHERE                                                                                 ");
				sb.append(" 								T .if_receive = 2                                                                 ");
				if(reportType!=null&&!"".equals(reportType)){
		   			   parme.put("type", reportType);
			           sb.append(" and T.bis_type=:type ");
				}
				sb.append(" 						) B ON INSTR (                                                                            ");
				sb.append(" 							A .STANDINGBOOKIDS,                                                                   ");
				sb.append(" 							TO_CHAR (B.STANDING_NUM)                                                              ");
				sb.append(" 						) > 0                                                                                     ");
				sb.append(" 					)                                                                                             ");
			}
			sb.append(" 			) temp                                                                                                ");
			sb.append("     LEFT JOIN                                                                                                     ");
			sb.append("     (                                                                                                             ");
			sb.append(" 	  select                                                                                               ");
			sb.append("          feecode.NAME_C,                                                                                          ");
			sb.append("          unit.LABEL                                                                                               ");
			sb.append("        from                                                                                                       ");
			sb.append(" 		 BASE_EXPENSE_CATEGORY_INFO feecode                                                                ");
			sb.append(" 	   LEFT JOIN                                                                                            ");
			sb.append(" 		  (SELECT unit.LABEL,unit.VALUE from DICT unit where unit.TYPE='units') unit                        ");
			sb.append(" 		ON feecode.units = unit.VALUE                                                                         ");
			sb.append("     ) unit                                                                                                        ");
			sb.append("     ON temp.FEE_NAME=unit.NAME_C                                                                                 ");
			sb.append("     GROUP BY                                                                                                      ");
			sb.append("       TEMP.LX,                                                                                                    ");
			sb.append("       TEMP.CLIENT,                                                                                                ");
			sb.append("       TEMP.FEE_PLAN,                                                                                              ");
			sb.append("       TEMP.PRICE,                                                                                                 ");
			sb.append("       unit.LABEL                                                                                                  ");
			sb.append(" 	) pivot(sum(NUM) NUM,sum(SHOULD_RMB) SHOULD_RMB for CLIENT in(                                                ");
			for (int i = 0; i < custlist.size(); i++) {
				sb.append("'"+custlist.get(i)+"' ku"+i);
				if(i!=custlist.size()-1){
					sb.append(" ,");
				}
			}
			sb.append("  ))           ");
			sb.append("   WHERE LX<>'其他'                                                                                                ");
			sb.append("   ORDER BY                                                                                                        ");
			sb.append("       LX                                                                                                          ");
		sb.append(" ) temp GROUP BY LX,LABEL                                                                                              ");
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
		return sqlQuery.list();
	}
    
	@SuppressWarnings("unchecked")
	public List<Object[]> findSteveReport(String lx,String label,List<String> custlist,String reportType,String clientId, String startTime,String endTime){
		StringBuffer sb=new StringBuffer();
		StringBuffer sum=new StringBuffer();
		StringBuffer rmb=new StringBuffer();
		HashMap<String,Object> parme=new HashMap<String,Object>();
		if(clientId!=null && !"".equals(clientId)) {
		   parme.put("CLIENTID", clientId); 
		}
		if(startTime!=null && !"".equals(startTime)){ 	
		   parme.put("STARTIME", startTime);
		}
		if(endTime!=null && !"".equals(endTime)){ 
		   parme.put("ENDTIME", endTime); 
		}
		sb.append(" SELECT                                                                                                            ");                                                 
		sb.append("   LX,                                                                                                             ");
		sb.append("   FEE_PLAN,                                                                                                       ");
		sb.append("   PRICE,                                                                                                          ");
		sb.append("   LABEL,                                                                                                          ");
		sum.append(" (                                                                                                                 ");
		rmb.append(" (                                                                                                                 ");
		for (int i = 0; i < custlist.size(); i++) {
			sb.append("ku"+i+"_NUM,");
			sb.append("ku"+i+"_SHOULD_RMB,");
			sum.append(" NVL(ku"+i+"_NUM,0) " );
			rmb.append(" NVL(ku"+i+"_SHOULD_RMB, 0) ");
			if(i!=custlist.size()-1){
				sum.append(" + " );
				rmb.append(" + " );
			}
		}
		sum.append(" )  sumnum ,                                                                                                     ");
		rmb.append(" )  sumrmb                                                                                                        ");
		//sb.append("   ku1_NUM,                                                                                                        ");
		//sb.append("   ku1_SHOULD_RMB,                                                                                                 ");
		//sb.append("   ku2_NUM,                                                                                                        ");
		//sb.append("   ku2_SHOULD_RMB,                                                                                                 ");
		//sb.append("   (NVL(ku1_NUM, 0)+NVL(ku2_NUM, 0)) sumnum,                                                                       ");
		//sb.append("   (NVL(ku1_SHOULD_RMB, 0)+NVL(ku2_SHOULD_RMB, 0)) sumrmb                                                          ");
		sb.append( sum.toString());
		sb.append( rmb.toString());
		sb.append(" FROM                                                                                                              ");
		sb.append(" 	(                                                                                                               ");
		sb.append(" 	  SELECT                                                                                                        ");
		sb.append(" 	  temp.LX,                                                                                                    ");
		sb.append("       temp.CLIENT,                                                                                                ");
		sb.append("       temp.FEE_PLAN,                                                                                              ");
		sb.append("       temp.PRICE,                                                                                                 ");
		sb.append("       unit.LABEL,                                                                                                 ");
		sb.append("       sum(temp.NUM) NUM,                                                                                          ");
		sb.append("       sum(temp.SHOULD_RMB) SHOULD_RMB                                                                             ");
		sb.append(" 	FROM                                                                                                          ");
		sb.append(" 			(                                                                                                           ");
		//入库
		if(reportType.trim().length()<=0 ||"1".equals(reportType.trim()) ) {
			sb.append(" 				(                                                                                                         ");
			sb.append(" 					SELECT DISTINCT                                                                                         ");
			sb.append(" 						'入库' AS lx,                                                                                         ");
			sb.append(" 						A .client,                                                                                            ");
			sb.append(" 						A .fee_plan,                                                                                          ");
			sb.append(" 						'' AS loading_num,                                                                                    ");
			sb.append(" 						A .dromr,                                                                                             ");
			sb.append(" 						A .drostockinname,                                                                                    ");
			sb.append(" 						A .droenterstocktime,                                                                                 ");
			sb.append(" 						b.FEE_NAME,                                                                                           ");
			sb.append(" 						b.NUM,                                                                                                ");
			sb.append(" 						b.SHOULD_RMB,                                                                                         ");
			sb.append(" 						b.PRICE,                                                                                              ");
			sb.append(" 						b.LINK_ID AS ASN,                                                                                     ");
			sb.append(" 						b.BILL_NUM                                                                                            ");
			sb.append(" 					FROM                                                                                                    ");
			sb.append(" 						(                                                                                                     ");
			sb.append(" 							SELECT                                                                                              ");
			sb.append(" 								T .*                                                                                              ");
			sb.append(" 							FROM                                                                                                ");
			sb.append(" 								bis_enter_stevedoring T                                                                           ");
			sb.append(" 							WHERE                                                                                               ");
			sb.append(" 								T .IF_OK = '1'                                                                                    ");
			sb.append(" 							AND T .DROENTERSTOCKTIME IS NOT NULL                                                                ");
			if(clientId!=null && !"".equals(clientId)){
				   sb.append(" AND T.client_id=:CLIENTID                               ");
			}
			if(startTime!=null && !"".equals(startTime)){
				   sb.append(" AND T.DROENTERSTOCKTIME>=to_date(:STARTIME,'yyyy-mm-dd') "); 
			}
			if(endTime!=null && !"".equals(endTime)){
				   sb.append(" AND T.DROENTERSTOCKTIME<=to_date(:ENDTIME,'yyyy-mm-dd')                            "); 
			}
			sb.append(" 						) A                                                                                                   ");
			sb.append(" 					JOIN (                                                                                                  ");
			sb.append(" 						SELECT                                                                                                ");
			sb.append(" 							T .STANDING_NUM,                                                                                    ");
			sb.append(" 							T .CUSTOMS_NUM,                                                                                     ");
			sb.append(" 							T .BILL_NUM,                                                                                        ");
			sb.append(" 							T .LINK_ID,                                                                                         ");
			sb.append(" 							T .FEE_CODE,                                                                                        ");
			sb.append(" 							T .FEE_NAME,                                                                                        ");
			sb.append(" 							T .bis_type,                                                                                        ");
			sb.append(" 							T .FEE_PLAN,                                                                                        ");
			sb.append(" 							T .ASN,                                                                                             ");
			sb.append(" 							T .IF_RECEIVE,                                                                                      ");
			sb.append(" 							T .NUM,                                                                                             ");
			sb.append(" 							T .SHOULD_RMB,                                                                                      ");
			sb.append(" 							T .PRICE                                                                                            ");
			sb.append(" 						FROM                                                                                                  ");
			sb.append(" 							BIS_STANDING_BOOK T                                                                                 ");
			sb.append(" 						WHERE                                                                                                 ");
			sb.append(" 							T .if_receive = 2                                                                                   ");
			sb.append(" 						AND T .bis_type IN (5, 6, 7, 8)                                                                       ");
			sb.append(" 					) B ON B.STANDING_NUM IN (NVL(A .drostanindbookids, 0))                                                 ");
			sb.append(" 				)                                                                                                         ");
		}
		if(reportType.trim().length()<=0){
		  sb.append(" 				UNION                                                                                                     ");
		}
		//出库
	    if(reportType.trim().length()<=0 ||"2".equals(reportType.trim()) ) {
			sb.append(" 					(                                                                                                       ");
			sb.append(" 						SELECT DISTINCT                                                                                       ");
			sb.append(" 							'出库' AS lx,                                                                                       ");
			sb.append(" 							A .client,                                                                                          ");
			sb.append(" 							A .fee_plan,                                                                                        ");
			sb.append(" 							TO_CHAR (NVL(A .loading_num, '')) AS loading_num,                                                   ");
			sb.append(" 							'' AS dromr,                                                                                        ");
			sb.append(" 							A .DROSTOCKIDNAME AS drostockinname,                                                                ");
			sb.append(" 							A .DROLOADINGTIME AS droenterstocktime,                                                             ");
			sb.append(" 							b.FEE_NAME,                                                                                         ");
			sb.append(" 							b.NUM,                                                                                              ");
			sb.append(" 							b.SHOULD_RMB,                                                                                       ");
			sb.append(" 							b.PRICE,                                                                                            ");
			sb.append(" 							b.LINK_ID AS ASN,                                                                                   ");
			sb.append(" 							b.BILL_NUM AS BILL_NUM                                                                              ");
			sb.append(" 						FROM                                                                                                  ");
			sb.append(" 							(                                                                                                   ");
			sb.append(" 								SELECT                                                                                            ");
			sb.append(" 									T .*                                                                                            ");
			sb.append(" 								FROM                                                                                              ");
			sb.append(" 									bis_out_stevedoring T                                                                           ");
			sb.append(" 								WHERE                                                                                             ");
			sb.append(" 									T .IF_OK = '1'                                                                                  ");
			sb.append(" 								AND T .DROLOADINGTIME IS NOT NULL                                                                 ");
			if(clientId!=null && !"".equals(clientId)){ 
				   sb.append(" AND T.client_id=:CLIENTID  ");  
			}
		    if(startTime!=null && !"".equals(startTime)){
		    	   sb.append(" AND T.DROLOADINGTIME>= to_date(:STARTIME,'yyyy-mm-dd')                            "); 
			}
			if(endTime!=null && !"".equals(endTime)){
				   sb.append(" AND T.DROLOADINGTIME<=to_date(:ENDTIME,'yyyy-mm-dd')                              "); 
			}
			sb.append(" 							) A                                                                                                 ");
			sb.append(" 						JOIN (                                                                                                ");
			sb.append(" 							SELECT                                                                                              ");
			sb.append(" 								T .STANDING_NUM,                                                                                  ");
			sb.append(" 								T .CUSTOMS_NUM,                                                                                   ");
			sb.append(" 								T .BILL_NUM,                                                                                      ");
			sb.append(" 								T .LINK_ID,                                                                                       ");
			sb.append(" 								T .FEE_CODE,                                                                                      ");
			sb.append(" 								T .FEE_NAME,                                                                                      ");
			sb.append(" 								T .bis_type,                                                                                      ");
			sb.append(" 								T .FEE_PLAN,                                                                                      ");
			sb.append(" 								T .ASN,                                                                                           ");
			sb.append(" 								T .IF_RECEIVE,                                                                                    ");
			sb.append(" 								T .NUM,                                                                                           ");
			sb.append(" 								T .SHOULD_RMB,                                                                                    ");
			sb.append(" 								T .PRICE                                                                                          ");
			sb.append(" 							FROM                                                                                                ");
			sb.append(" 								BIS_STANDING_BOOK T                                                                               ");
			sb.append(" 							WHERE                                                                                               ");
			sb.append(" 								T .if_receive = 2                                                                                 ");
			sb.append(" 							AND T .bis_type IN (5, 6, 7, 8)                                                                     ");
			sb.append(" 						) B ON B.STANDING_NUM IN (A .drostanindbookids)                                                       ");
			sb.append(" 					)                                                                                                       ");
	    }
	    if(reportType.trim().length()<=0){
			  sb.append(" 				UNION                                                                                                     ");
		}
		//倒箱
		if(reportType.trim().length()<=0 ||"3".equals(reportType.trim()) ) {
			sb.append(" 					(                                                                                                       ");
			sb.append(" 						SELECT DISTINCT                                                                                       ");
			sb.append(" 							'倒箱' AS lx,                                                                                       ");
			sb.append(" 							A .client,                                                                                          ");
			sb.append(" 							A .fee_plan,                                                                                        ");
			sb.append(" 							'' AS loading_num,                                                                                  ");
			sb.append(" 							A .mr AS dromr,                                                                                     ");
			sb.append(" 							A .DROSTOCKIN AS drostockinname,                                                                    ");
			sb.append(" 							A .DROBACKDATE AS droenterstocktime,                                                                ");
			sb.append(" 							b.FEE_NAME,                                                                                         ");
			sb.append(" 							b.NUM,                                                                                              ");
			sb.append(" 							b.SHOULD_RMB,                                                                                       ");
			sb.append(" 							b.PRICE,                                                                                            ");
			sb.append(" 							b.LINK_ID AS ASN,                                                                                   ");
			sb.append(" 							A .BILL_NUM AS BILL_NUM                                                                             ");
			sb.append(" 						FROM                                                                                                  ");
			sb.append(" 							(                                                                                                   ");
			sb.append(" 								SELECT                                                                                            ");
			sb.append(" 									T .*                                                                                            ");
			sb.append(" 								FROM                                                                                              ");
			sb.append(" 									bis_back_stevedoring T                                                                          ");
			sb.append(" 								WHERE                                                                                             ");
			sb.append(" 									T .IF_OK = '1'                                                                                  ");
			sb.append(" 								AND T .IF_ALL_MAN = 1                                                                             ");
			sb.append(" 								AND T .DROBACKDATE IS NOT NULL                                                                    ");
			if(clientId!=null && !"".equals(clientId)){
				   sb.append(" AND T.client_id=:CLIENTID   "); 
			}
			if(startTime!=null && !"".equals(startTime)){
				   sb.append(" AND T.DROBACKDATE>= to_date(:STARTIME,'yyyy-mm-dd') ");
			}
			if(endTime!=null && !"".equals(endTime)){
				   sb.append(" AND T.DROBACKDATE<= to_date(:ENDTIME,'yyyy-mm-dd') ");
			}
			sb.append(" 							) A                                                                                                 ");
			sb.append(" 						JOIN (                                                                                                ");
			sb.append(" 							SELECT                                                                                              ");
			sb.append(" 								T .STANDING_NUM,                                                                                  ");
			sb.append(" 								T .CUSTOMS_NUM,                                                                                   ");
			sb.append(" 								T .BILL_NUM,                                                                                      ");
			sb.append(" 								T .LINK_ID,                                                                                       ");
			sb.append(" 								T .FEE_CODE,                                                                                      ");
			sb.append(" 								T .FEE_NAME,                                                                                      ");
			sb.append(" 								T .bis_type,                                                                                      ");
			sb.append(" 								T .FEE_PLAN,                                                                                      ");
			sb.append(" 								T .ASN,                                                                                           ");
			sb.append(" 								T .IF_RECEIVE,                                                                                    ");
			sb.append(" 								T .NUM,                                                                                           ");
			sb.append(" 								T .SHOULD_RMB,                                                                                    ");
			sb.append(" 								T .PRICE                                                                                          ");
			sb.append(" 							FROM                                                                                                ");
			sb.append(" 								BIS_STANDING_BOOK T                                                                               ");
			sb.append(" 							WHERE                                                                                               ");
			sb.append(" 								T .if_receive = 2                                                                                 ");
			sb.append(" 							AND T .bis_type = 6                                                                                 ");
			sb.append(" 						) B ON INSTR (                                                                                        ");
			sb.append(" 							A .STANDINGBOOKIDS,                                                                                 ");
			sb.append(" 							TO_CHAR (B.STANDING_NUM)                                                                            ");
			sb.append(" 						) > 0                                                                                                 ");
			sb.append(" 					)                                                                                                       ");
		}
		if(reportType.trim().length()<=0){
			  sb.append(" 				UNION                                                                                                     ");
		}
		//日工
		if(reportType.trim().length()<=0 ||"4".equals(reportType.trim()) ){
			sb.append(" 					(                                                                                                       ");
			sb.append(" 						SELECT                                                                                                ");
			sb.append(" 							'日工' AS lx,                                                                                       ");
			sb.append(" 							D .CLIENT,                                                                                          ");
			sb.append(" 							D .SCHEME_NAME AS fee_plan,                                                                         ");
			sb.append(" 							'' AS loading_num,                                                                                  ");
			sb.append(" 							'' AS dromr,                                                                                        ");
			sb.append(" 							'' AS drostockinname,                                                                               ");
			sb.append(" 							D .loading_date AS droenterstocktime,                                                               ");
			sb.append(" 							c.FEE_NAME,                                                                                         ");
			sb.append(" 							SUM (c.NUM) AS NUM,                                                                                 ");
			sb.append(" 							SUM (c.SHOULD_RMB) AS SHOULD_RMB,                                                                   ");
			sb.append(" 							c.PRICE,                                                                                            ");
			sb.append(" 							'' AS ASN,                                                                                          ");
			sb.append(" 							'' AS BILL_NUM                                                                                      ");
			sb.append(" 						FROM                                                                                                  ");
			sb.append(" 							(                                                                                                   ");
			sb.append(" 								SELECT                                                                                            ");
			sb.append(" 									T .*                                                                                            ");
			sb.append(" 								FROM                                                                                              ");
			sb.append(" 									bis_loading_team_daywork T                                                                      ");
			sb.append(" 								WHERE                                                                                             ");
			sb.append(" 									T .state = 1                                                                                    ");
			sb.append(" 								AND T .del_flag = 0                                                                               ");
			sb.append(" 								AND T .loading_date IS NOT NULL                                                                   ");
			if(clientId!=null && !"".equals(clientId)){
                    sb.append(" AND T.client_id=:CLIENTID ");
			}
			if(startTime!=null && !"".equals(startTime)){ 	 
					sb.append(" AND T.loading_date>= to_date(:STARTIME,'yyyy-mm-dd') ");
			}
			if(endTime!=null && !"".equals(endTime)){   
					sb.append(" AND T.loading_date>= to_date(:ENDTIME,'yyyy-mm-dd') ");
			}
			sb.append(" 							) D                                                                                                 ");
			sb.append(" 						LEFT JOIN (                                                                                           ");
			sb.append(" 							SELECT                                                                                              ");
			sb.append(" 								T .STANDING_NUM,                                                                                  ");
			sb.append(" 								T .CUSTOMS_NUM,                                                                                   ");
			sb.append(" 								T .BILL_NUM,                                                                                      ");
			sb.append(" 								T .LINK_ID,                                                                                       ");
			sb.append(" 								T .FEE_CODE,                                                                                      ");
			sb.append(" 								T .FEE_NAME,                                                                                      ");
			sb.append(" 								T .bis_type,                                                                                      ");
			sb.append(" 								T .FEE_PLAN,                                                                                      ");
			sb.append(" 								T .ASN,                                                                                           ");
			sb.append(" 								T .IF_RECEIVE,                                                                                    ");
			sb.append(" 								T .NUM,                                                                                           ");
			sb.append(" 								T .SHOULD_RMB,                                                                                    ");
			sb.append(" 								T .PRICE                                                                                          ");
			sb.append(" 							FROM                                                                                                ");
			sb.append(" 								BIS_STANDING_BOOK T                                                                               ");
			sb.append(" 							WHERE                                                                                               ");
			sb.append(" 								T .if_receive = 2                                                                                 ");
			sb.append(" 						) c ON D .STANDING_NUM = c.STANDING_NUM                                                               ");
			sb.append(" 						GROUP BY                                                                                              ");
			sb.append(" 							D .client_id,                                                                                       ");
			sb.append(" 							D .CLIENT,                                                                                          ");
			sb.append(" 							D .SCHEME_NAME,                                                                                     ");
			sb.append(" 							D .loading_date,                                                                                    ");
			sb.append(" 							c.FEE_PLAN,                                                                                         ");
			sb.append(" 							c.FEE_NAME,                                                                                         ");
			sb.append(" 							c.PRICE                                                                                             ");
			sb.append(" 					)                                                                                                       ");
		}
		if(reportType.trim().length()<=0){
			sb.append(" 				UNION                                                                                                     ");
		}
		//缠膜
		if(reportType.trim().length()<=0 ||"5".equals(reportType.trim())||"7".equals(reportType.trim())||"8".equals(reportType.trim())||"21".equals(reportType.trim())||"22".equals(reportType.trim())||"23".equals(reportType.trim())||"24".equals(reportType.trim())||"25".equals(reportType.trim())) {
			sb.append(" 					(                                                                                                       ");
			sb.append(" 						SELECT                                                                                                ");
			sb.append(" 							(                                                                                                   ");
			sb.append(" 								CASE b.bis_type                                                                                   ");
			sb.append(" 								WHEN '5' THEN                                                                                     ");
			sb.append(" 									'在库分拣'                                                                                      ");
			sb.append(" 								WHEN '7' THEN                                                                                     ");
			sb.append(" 									'缠膜'                                                                                          ");
			sb.append(" 								WHEN '8' THEN                                                                                     ");
			sb.append(" 									'打包'                                                                                          ");
			sb.append(" 								WHEN '21' THEN                                                                                    ");
			sb.append(" 									'内标签'                                                                                        ");
			sb.append(" 								WHEN '22' THEN                                                                                    ");
			sb.append(" 									'外标签'                                                                                        ");
			sb.append(" 								WHEN '23' THEN                                                                                    ");
			sb.append(" 									'码托'                                                                                          ");
			sb.append(" 								WHEN '24' THEN                                                                                    ");
			sb.append(" 									'装铁架'                                                                                        ");
			sb.append(" 								WHEN '25' THEN                                                                                    ");
			sb.append(" 									'拆铁架'                                                                                        ");
			sb.append(" 								ELSE                                                                                              ");
			sb.append(" 									'其他'                                                                                          ");
			sb.append(" 								END                                                                                               ");
			sb.append(" 							) AS lx,                                                                                            ");
			sb.append(" 							A .client,                                                                                          ");
			sb.append(" 							A .fee_plan,                                                                                        ");
			sb.append(" 							'' AS loading_num,                                                                                  ");
			sb.append(" 							A .mr AS dromr,                                                                                     ");
			sb.append(" 							A .DROSTOCKIN AS drostockinname,                                                                    ");
			sb.append(" 							A .DROBACKDATE AS droenterstocktime,                                                                ");
			sb.append(" 							b.FEE_NAME,                                                                                         ");
			sb.append(" 							b.NUM,                                                                                              ");
			sb.append(" 							b.SHOULD_RMB,                                                                                       ");
			sb.append(" 							b.PRICE,                                                                                            ");
			sb.append(" 							b.LINK_ID AS ASN,                                                                                   ");
			sb.append(" 							A .BILL_NUM AS BILL_NUM                                                                             ");
			sb.append(" 						FROM                                                                                                  ");
			sb.append(" 							(                                                                                                   ");
			sb.append(" 								SELECT                                                                                            ");
			sb.append(" 									T .*                                                                                            ");
			sb.append(" 								FROM                                                                                              ");
			sb.append(" 									bis_back_stevedoring T                                                                          ");
			sb.append(" 								WHERE                                                                                             ");
			sb.append(" 									T .IF_OK = '1'                                                                                  ");
			if(clientId!=null && !"".equals(clientId)){ 
				   sb.append(" AND T.client_id=:CLIENTID ");
			}
			if(startTime!=null && !"".equals(startTime)){  
				   sb.append(" AND  T.DROBACKDATE>= to_date(:STARTIME,'yyyy-mm-dd') ");
			}
			if(endTime!=null && !"".equals(endTime)){  
				   sb.append(" AND T.DROBACKDATE<=to_date(:ENDTIME,'yyyy-mm-dd')  ");
			}
			sb.append(" 							) A                                                                                                 ");
			sb.append(" 						JOIN (                                                                                                ");
			sb.append(" 							SELECT                                                                                              ");
			sb.append(" 								T .STANDING_NUM,                                                                                  ");
			sb.append(" 								T .CUSTOMS_NUM,                                                                                   ");
			sb.append(" 								T .BILL_NUM,                                                                                      ");
			sb.append(" 								T .LINK_ID,                                                                                       ");
			sb.append(" 								T .FEE_CODE,                                                                                      ");
			sb.append(" 								T .FEE_NAME,                                                                                      ");
			sb.append(" 								T .bis_type,                                                                                      ");
			sb.append(" 								T .FEE_PLAN,                                                                                      ");
			sb.append(" 								T .ASN,                                                                                           ");
			sb.append(" 								T .IF_RECEIVE,                                                                    ");
			sb.append(" 								T .NUM,                                                                           ");
			sb.append(" 								T .SHOULD_RMB,                                                                    ");
			sb.append(" 								T .PRICE                                                                          ");
			sb.append(" 							FROM                                                                                  ");
			sb.append(" 								BIS_STANDING_BOOK T                                                               ");
			sb.append(" 							WHERE                                                                                 ");
			sb.append(" 								T .if_receive = 2                                                                 ");
			if(reportType!=null&&!"".equals(reportType)){
	   			   parme.put("type", reportType);
		           sb.append(" and T.bis_type=:type ");
			}
			sb.append(" 						) B ON INSTR (                                                                            ");
			sb.append(" 							A .STANDINGBOOKIDS,                                                                   ");
			sb.append(" 							TO_CHAR (B.STANDING_NUM)                                                              ");
			sb.append(" 						) > 0                                                                                     ");
			sb.append(" 					)                                                                                             ");
		}
		sb.append(" 			) temp                                                                                                ");
		sb.append("     LEFT JOIN                                                                                                     ");
		sb.append("     (                                                                                                             ");
		sb.append(" 	  select                                                                                               ");
		sb.append("          feecode.NAME_C,                                                                                          ");
		sb.append("          unit.LABEL                                                                                               ");
		sb.append("        from                                                                                                       ");
		sb.append(" 		 BASE_EXPENSE_CATEGORY_INFO feecode                                                                ");
		sb.append(" 	   LEFT JOIN                                                                                            ");
		sb.append(" 		  (SELECT unit.LABEL,unit.VALUE from DICT unit where unit.TYPE='units') unit                        ");
		sb.append(" 		ON feecode.units = unit.VALUE                                                                         ");
		sb.append("     ) unit                                                                                                        ");
		sb.append("     ON temp.FEE_NAME=unit.NAME_C                                                                                 ");
		sb.append("     GROUP BY                                                                                                      ");
		sb.append("       TEMP.LX,                                                                                                    ");
		sb.append("       TEMP.CLIENT,                                                                                                ");
		sb.append("       TEMP.FEE_PLAN,                                                                                              ");
		sb.append("       TEMP.PRICE,                                                                                                 ");
		sb.append("       unit.LABEL                                                                                                  ");
		sb.append(" 	) pivot(sum(NUM) NUM,sum(SHOULD_RMB) SHOULD_RMB for CLIENT in(                                                ");
		for (int i = 0; i < custlist.size(); i++) {
			sb.append("'"+custlist.get(i)+"' ku"+i);
			if(i!=custlist.size()-1){
				sb.append(" ,");
			}
		}
		sb.append("  ))           ");
		sb.append(" where 1=1     ");
		if(lx!=null&&!"".equals(lx)){
		  sb.append(" and LX='"+lx+"'                                                                                                  ");
		}
		if(label!=null&&!"".equals(label)){
		  sb.append(" and LABEL='"+label+"'                                                                                                  ");
		}
		sb.append("   ORDER BY                                                                                                        ");
		sb.append("       LX                                                                                                          ");
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
		return sqlQuery.list();
	}
}
