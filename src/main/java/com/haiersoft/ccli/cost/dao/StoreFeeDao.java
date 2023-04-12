package com.haiersoft.ccli.cost.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.common.persistence.HibernateDao;

/**
 * 存储费用
 * @author LZG
 *
 */
@Repository
public class StoreFeeDao extends HibernateDao<Object,String> {
	/**
	 * 查找需要进行存储费计算的记录
	 * select 联系单号，联系单状态，方案id，sum(A.[件数]*计费天数）as 件数数量，sum(A.[毛重]*计费天数）as 毛重数量，sum(A.[净重]*计费天数）as 净重
	 * 	from
	 * 	   (select 托盘计费区间表.*,  
	 * 			(case when 上次结算日 is null then 计费开始日期 when 上次结算日>=计费开始日期 then 上次结算日 else 计费开始日期 end)as 【计费开始1】，
	 * 			(case when  计费结束日期  is null then  now-1 when now<=计费结束日期 then （now-1天） else 计费结束日期 end) as 【计费结束1】，
	 * 			(case when 【计费结束1】<【计费开始1】 then 0 else 【计费结束1】-【计费开始1】+1天天 end ) as 【计费天数】
	 * 			NVL(NVL(出库联系单号,货转联系单号),入库联系单号)  as 【联系单号】 --优先顺序：出库、货转、入库
	 * 	  	from 托盘计费区间表  
	 *    	where 客户结账日=now and 状态=正常 and 清库标记=正常
	 *    	) A
	 *   where 【计费天数】>0 group by【联系单号】，方案id
	 * @param nDay 日 当天 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findStortFeeList(int nDay){
		if(nDay>0){
			StringBuffer sb=new StringBuffer();
			HashMap<String,Object> parme=new HashMap<String,Object>();
			sb.append(" select linknum,FEE_PLAN_ID,sum(NUM*daynum) as picnum, sum(GROSS_WEIGHT*daynum) as sumgross,sum(NET_WEIGHT*daynum) as sumnet ");
			sb.append(" from( ");
			sb.append("  select FEE_PLAN_ID,NUM,GROSS_WEIGHT,NET_WEIGHT,OUT_ID,TRANSFER_ID,ENTER_ID,linknum , (case when endtim<statime then 0 else floor(endtim-statime+1) end ) as daynum ");
			sb.append("  from( ");
			sb.append("   select FEE_PLAN_ID,NUM,GROSS_WEIGHT,NET_WEIGHT,OUT_ID,TRANSFER_ID,ENTER_ID, ");
			sb.append("   (case  ");
			sb.append("    	when LAST_SETTL_DATE is null then CHARGE_STA_DATE ");
			sb.append("    	when LAST_SETTL_DATE>=CHARGE_STA_DATE then LAST_SETTL_DATE ");
			sb.append("    	else CHARGE_STA_DATE ");
			sb.append("    	end) as statime, ");
			sb.append("   (case when CHARGE_END_DATE is null then  trunc(sysdate)-1 when  trunc(sysdate)<=CHARGE_END_DATE then   trunc(sysdate)-1 else CHARGE_END_DATE end) as endtim, ");
			sb.append("   NVL(NVL(OUT_ID,TRANSFER_ID),ENTER_ID)  as linknum ");
			sb.append("   from bis_asn_action aa where aa.client_day=:nday and aa.status=1 and aa.clean_sign='0'  and num is not null ");
			sb.append("   ) bb ");
			sb.append("  ) cc where daynum>0 ");
			sb.append(" group by linknum,FEE_PLAN_ID ");
			parme.put("nday", nDay);
			SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
			return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		return null;
	}
	/**
	 * 根据联系单号和联系单类型 获取计费天数
	 * @param LinkNum
	 * @param ntype
	 * @param nDay
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Integer findStortFeeDaynum(String LinkNum,int ntype,int nDay){
		List<Map<String,Object>> getList=null;
		if(LinkNum!=null && ntype>0 && nDay>0){
			HashMap<String,Object> parme=new HashMap<String,Object>();
			/**
			select linknum,FEE_PLAN_ID,sum(NUM*daynum) as picnum, sum(GROSS_WEIGHT*daynum) as sumgross,sum(NET_WEIGHT*daynum) as sumnet
			from(
			  select bb.* , (case when endtim<statime then 0 else ceil(endtim-statime) end ) as daynum 
			  from(
			      select aa.*,
			      (case 
			        when LAST_SETTL_DATE is null then CHARGE_STA_DATE
			        when LAST_SETTL_DATE>=CHARGE_STA_DATE then LAST_SETTL_DATE
			        else CHARGE_STA_DATE
			      end) as statime,
			      （case when sysdate<=CHARGE_END_DATE then  sysdate-1 else CHARGE_END_DATE end） as endtim,
			       NVL(NVL(OUT_ID,TRANSFER_ID),ENTER_ID)  as linknum
			       from bis_asn_action aa where aa.client_day=23 and aa.status=1 and aa.clean_sign='0'
			   ) bb
			) cc where daynum>0
			group by linknum,FEE_PLAN_ID
			*/
			StringBuffer sb=new StringBuffer("select (case when endtim<statime then 0 else floor(endtim-statime+1) end ) as daynum ");
			sb.append(" from ( select ");
			sb.append(" (case when LAST_SETTL_DATE is null then CHARGE_STA_DATE  when LAST_SETTL_DATE>=CHARGE_STA_DATE then LAST_SETTL_DATE else CHARGE_STA_DATE  end) as statime, ");
			sb.append(" (case when trunc(sysdate)<=CHARGE_END_DATE then  trunc(sysdate)-1 else CHARGE_END_DATE end)  as endtim ");
			sb.append(" from bis_asn_action aa where aa.client_day=:nday and aa.status=1 and aa.clean_sign='0' ");
			if(1==ntype){
				sb.append(" and ENTER_ID=:linknum");
			}else if(2==ntype){
				sb.append(" and OUT_ID=:linknum");
			}else if(3==ntype){
				sb.append(" and TRANSFER_ID=:linknum");
			}
			sb.append(" )");
			parme.put("nday", nDay);
			parme.put("linknum", LinkNum);
			SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
			getList= sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			if(getList!=null && getList.size()>0){
				Map<String,Object> getMap=getList.get(0);
				return Integer.valueOf(getMap.get("DAYNUM").toString());
			}
		}
		return null;
	}
	
	
	/**
	 * 根据联系单号更新内容
	 * 将已结算的记录标记上次结算日期(计费结束日期大于计费开始日期的费用)
	 * @param ordCode 订单id
	 */
	public void upASNAction(Map<String,Object> getUpMap,int nDay){
		if(getUpMap!=null && getUpMap.size()>0){
			String getLinkNum=getUpMap.get("linknum").toString();
			int ntype=Integer.valueOf(getUpMap.get("type").toString());
			if(getLinkNum!=null && ntype>0){
				HashMap<String,Object> parme=new HashMap<String,Object>();
		/**
	   	update bis_asn_action set charge_day= nvl(charge_day,0) + (case when(ceil((case when sysdate<=CHARGE_END_DATE then  sysdate-1 else CHARGE_END_DATE end)-(case 
	        when LAST_SETTL_DATE is null then CHARGE_STA_DATE
	        when LAST_SETTL_DATE>=CHARGE_STA_DATE then LAST_SETTL_DATE
	        else CHARGE_STA_DATE
	      end))<0) then 0 
	      when nvl(ceil((case when CHARGE_END_DATE is null then  sysdate-1 when sysdate<=CHARGE_END_DATE then  sysdate-1 else CHARGE_END_DATE end)-(case 
	        when LAST_SETTL_DATE is null then CHARGE_STA_DATE
	        when LAST_SETTL_DATE>=CHARGE_STA_DATE then LAST_SETTL_DATE
	        else CHARGE_STA_DATE
	      end)),0)=0 then 0 
	      else (ceil((case when CHARGE_END_DATE is null then  sysdate-1 when sysdate<=CHARGE_END_DATE then  sysdate-1 else CHARGE_END_DATE end)-(case 
	        when LAST_SETTL_DATE is null then CHARGE_STA_DATE
	        when LAST_SETTL_DATE>=CHARGE_STA_DATE then LAST_SETTL_DATE
	        else CHARGE_STA_DATE
	      end))) end)
	     where client_day=23 and status=1 and clean_sign='0' and ENTER_ID='EYZH1603101002322'
		*/
				StringBuffer sql=new StringBuffer();
				sql.append("update bis_asn_action set LAST_SETTL_DATE=trunc(sysdate), ");
				sql.append(" charge_day= nvl(charge_day,0) + (case when(ceil((case when trunc(sysdate)<=CHARGE_END_DATE then  trunc(sysdate)-1 else CHARGE_END_DATE end)-(case  when LAST_SETTL_DATE is null then CHARGE_STA_DATE when LAST_SETTL_DATE>=CHARGE_STA_DATE then LAST_SETTL_DATE else CHARGE_STA_DATE end))<0) then 0 ");
				sql.append("  when nvl(ceil((case when CHARGE_END_DATE is null then  trunc(sysdate)-1 when trunc(sysdate)<=CHARGE_END_DATE then  trunc(sysdate)-1 else CHARGE_END_DATE end)-(case when LAST_SETTL_DATE is null then CHARGE_STA_DATE  when LAST_SETTL_DATE>=CHARGE_STA_DATE then LAST_SETTL_DATE else CHARGE_STA_DATE end)),0)=0 then 0 ");
				sql.append("  else (ceil((case when CHARGE_END_DATE is null then  trunc(sysdate)-1 when trunc(sysdate)<=CHARGE_END_DATE then  trunc(sysdate)-1 else CHARGE_END_DATE end)-(case  when LAST_SETTL_DATE is null then CHARGE_STA_DATE when LAST_SETTL_DATE>=CHARGE_STA_DATE then LAST_SETTL_DATE else CHARGE_STA_DATE end))) end)");
				sql.append(" where client_day=:nDay and status=1 and clean_sign='0' and num is not null ");
				if(1==ntype){
					sql.append(" and ENTER_ID=:linknum");
				}else if(2==ntype){
					sql.append(" and OUT_ID=:linknum");
				}else if(3==ntype){
					sql.append(" and TRANSFER_ID=:linknum");
				} 
				parme.put("nDay", nDay);
				parme.put("linknum", getLinkNum);
				SQLQuery sqlQuery=createSQLQuery(sql.toString(), parme);
				sqlQuery.executeUpdate();
			}
		}//end if
	}
	
}
