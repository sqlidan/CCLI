package com.haiersoft.ccli.cost.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.PropertiesUtil;
import com.haiersoft.ccli.cost.entity.BisVerifiBook;

@Repository
public class BisVerifiBookDao  extends HibernateDao<BisVerifiBook, String> {
	/**
	 * 检索核销银行信息列表
	 */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getBanklist() {
		String sql="select BANK_NAME,BANK_NUM,CURRENCY from base_bank";
		Map<String,Object> params = new HashMap<String,Object>();
		SQLQuery sqlQuery=createSQLQuery(sql.toString(), params);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	/**
	 * 根据核销号获取客户财务码
	 * @param code
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public Map<String, Object>  getClientJDNum(String code) {
		if(code!=null && !"".equals(code)){
			String sql="select CWNUM,GYSNUM from base_client_info c where EXISTS ( select 1 from bis_verifi_book b where b.verifi_num=:code and b.customid=c.ids )";
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("code", code);
			SQLQuery sqlQuery=createSQLQuery(sql.toString(), params);
			List<Map<String, Object>> getList= sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			if(getList!=null && getList.size()>0){
				return getList.get(0);
			}
		}
		return null;
	}
	
	/**
	 * 应收挂账、现结组合金蝶接口数据
	 * @param code 核销编码
	 * @param ntepy 标示挂账1或现结2
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getYSToJDGJDataList(String code,int ntepy) {
		/**
		 select trunc(sysdate) as dnow,(case  vb.CTYPE when 1 then  '转账凭证'  when 2 then  '银行凭证' end) as pztype,vb.cruser as cruser,vb.customname||'包干' as info,
		'1122'as yscode,vb.currency,vb.taxnum,vb.summoney as ymoney,'1' as fangxiang ,vb.summoney as jfmoney,0 as dfmoney
		       from ( select * from  bis_verifi_book  where   NTYPE=1 and NSTATE<2 and CTYPE<3 and  verifi_num='HX20160625113953618'
		         ) vb 
		       
		 union all
		
		select trunc(sysdate) as dnow ,(case min（vb.CTYPE） when 1 then  '转账凭证'  when 2 then  '银行凭证' end) as pztype,min(vb.cruser) as cruser,min(vb.customname)||feed.ysinfo as info,
		feed.yscode,min(vb.currency) as currency,min(vb.taxnum) as taxnum,sum(vbi.nowmoney/nvl(vbi.taxnum,1))/1.06 as ymoney,'-1' as fangxiang ,0 as jfmoney,sum(vbi.nowmoney/nvl(vbi.taxnum,1))/1.06 as dfmoney
		       from 
		        ( select * from  bis_verifi_book  where   NTYPE=1 and NSTATE<2 and CTYPE<3  and  verifi_num='HX20160625113953618'
		         ) vb 
		         left join bis_verifi_book_info vbi on vbi.verifi_num=vb.verifi_num
		         left join base_expense_category_info feed on feed.code=vbi.feedcode
		 
		 group by feed.yscode,feed.ysinfo  
		 
		   union all
		        
		 select trunc(sysdate) as dnow,(case  vb.CTYPE when 1 then  '转账凭证'  when 2 then  '银行凭证' end) as pztype,vb.cruser as cruser,vb.customname||'包干销项税' as info,
		'22210105'as yscode,vb.currency,vb.taxnum,vb.summoney-vb.summoney/1.06 as ymoney,'-1' as fangxiang ,0 as jfmoney,vb.summoney-vb.summoney/1.06 as dfmoney
		       from ( select * from  bis_verifi_book  where   NTYPE=1 and NSTATE<2 and CTYPE<3 and  verifi_num='HX20160625113953618'
		         ) vb 
		 
		 */
		
		if(code!=null && !"".equals(code)){
			String yszhangkuaninfo=PropertiesUtil.getPropertiesByName("yszhangkuaninfo", "jdbill");
			String yszhangkuancode=PropertiesUtil.getPropertiesByName("yszhangkuancode", "jdbill");
			String baoganxiaoxianginfo=PropertiesUtil.getPropertiesByName("baoganxiaoxianginfo", "jdbill");
			String baoganxiaoxiangcode=PropertiesUtil.getPropertiesByName("baoganxiaoxiangcode", "jdbill");
			String shoukuancode=PropertiesUtil.getPropertiesByName("shoukuancode", "jdbill");
			String shoukuaninfo=PropertiesUtil.getPropertiesByName("shoukuaninfo", "jdbill");
			if(2==ntepy){
				yszhangkuancode=shoukuancode;
				yszhangkuaninfo=shoukuaninfo;
			}
			String nowDate=DateUtils.getDate();
			StringBuffer sql=new StringBuffer();
			sql.append(" select '").append(nowDate).append("' as dnow,(case  "+ntepy+" when 1 then  '转账凭证'  when 2 then  '银行凭证' end) as pztype,vb.cruser as cruser,vb.customname||'").append(yszhangkuaninfo).append("' as info, ");
			sql.append(" '").append(yszhangkuancode).append("'as yscode,vb.currency,vb.taxnum,vb.summoney as ymoney,'1' as fangxiang ,vb.summoney as jfmoney,0 as dfmoney,'40-0002' as JDNUM  ");
			sql.append("        from ( select * from  bis_verifi_book  where   NTYPE=1 and NSTATE<2 and CTYPE<3 and  verifi_num=:code ");
			sql.append("          ) vb  ");
			sql.append("  union all ");
			sql.append(" select '").append(nowDate).append("' as dnow ,(case "+ntepy+" when 1 then  '转账凭证'  when 2 then  '银行凭证' end) as pztype,min(vb.cruser) as cruser,min(vb.customname)||feed.ysinfo as info, ");
			sql.append(" feed.yscode,min(vb.currency) as currency,min(vb.taxnum) as taxnum,sum(vbi.nowmoney/nvl(vbi.taxnum,1))/1.06 as ymoney,'-1' as fangxiang ,0 as jfmoney,sum(vbi.nowmoney/nvl(vbi.taxnum,1))/1.06 as dfmoney,min(JDNUM) as JDNUM ");
			sql.append("        from  ");
			sql.append("         ( select * from  bis_verifi_book  where   NTYPE=1 and NSTATE<2 and CTYPE<3  and  verifi_num=:code ");
			sql.append("          ) vb  ");
			sql.append("          left join bis_verifi_book_info vbi on vbi.verifi_num=vb.verifi_num ");
			sql.append("          left join base_expense_category_info feed on feed.code=vbi.feedcode ");
			sql.append("  group by feed.yscode,feed.ysinfo   ");
			sql.append("    union all ");
			sql.append("  select '").append(nowDate).append("' as dnow,(case  "+ntepy+" when 1 then  '转账凭证'  when 2 then  '银行凭证' end) as pztype,vb.cruser as cruser,vb.customname||'").append(baoganxiaoxianginfo).append("' as info, ");
			sql.append(" '").append(baoganxiaoxiangcode).append("'as yscode,vb.currency,vb.taxnum,vb.summoney-vb.summoney/1.06 as ymoney,'-1' as fangxiang ,0 as jfmoney,vb.summoney-vb.summoney/1.06 as dfmoney,'000004' as JDNUM  ");
			sql.append("   from ( select * from  bis_verifi_book  where   NTYPE=1 and NSTATE<2 and CTYPE<3 and  verifi_num=:code ");
			sql.append("      ) vb ");
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("code", code);
			SQLQuery sqlQuery=createSQLQuery(sql.toString(), params);
			return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		return null;
	}
	/**
	 * 应收冲账金蝶接口数据
	 * @param code 核销编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getYSToJDCDataList(String code,int ntepy) {
		/**
		  select trunc(sysdate) as dnow,(case  vb.CTYPE when 1 then  '转账凭证'  when 2 then  '银行凭证' end) as pztype,vb.cruser as cruser,vb.customname||'收款' as info,
		    '100218'as yscode,vb.currency,vb.taxnum,vb.summoney as ymoney,'1' as fangxiang ,vb.summoney as jfmoney,0 as dfmoney
		           from ( select * from  bis_verifi_book  where   NTYPE=1 and NSTATE<2 and CTYPE=3 --and  verifi_num='HX20160625113953618'
		             ) vb 
		 union all
		  select trunc(sysdate) as dnow,(case  vb.CTYPE when 1 then  '转账凭证'  when 2 then  '银行凭证' end) as pztype,vb.cruser as cruser,vb.customname||'收款' as info,
		    '1122'as yscode,vb.currency,vb.taxnum,vb.summoney as ymoney,'-1' as fangxiang ,0 as jfmoney,vb.summoney as dfmoney
		           from ( select * from  bis_verifi_book  where   NTYPE=1 and NSTATE<2 and CTYPE=3 --and  verifi_num='HX20160625113953618'
		             ) vb
		 */
		
		if(code!=null && !"".equals(code)){
			String shoukuaninfo=PropertiesUtil.getPropertiesByName("shoukuaninfo", "jdbill");
			String shoukuancode=PropertiesUtil.getPropertiesByName("shoukuancode", "jdbill");
			String yszhangkuancode=PropertiesUtil.getPropertiesByName("yszhangkuancode", "jdbill");
			String nowDate=DateUtils.getDate();
			StringBuffer sql=new StringBuffer();
			 
			sql.append("  select '").append(nowDate).append("' as dnow,  '银行凭证'  as pztype,vb.cruser as cruser,vb.customname||'").append(shoukuaninfo).append("' as info,  ");
			sql.append("     '").append(shoukuancode).append("'as yscode,vb.currency,vb.taxnum,vb.summoney as ymoney,'1' as fangxiang ,vb.summoney as jfmoney,0 as dfmoney  ");
			sql.append("           from ( select * from  bis_verifi_book  where   NTYPE=1 and NSTATE<2 and CTYPE=3 and  verifi_num=:code  ");
			sql.append("              ) vb   ");
			sql.append("  union all  ");
			sql.append("  select '").append(nowDate).append("' as dnow,  '银行凭证'   as pztype,vb.cruser as cruser,vb.customname||'").append(shoukuaninfo).append("' as info,  ");
			sql.append("  '").append(yszhangkuancode).append("'as yscode,vb.currency,vb.taxnum,vb.summoney as ymoney,'-1' as fangxiang ,0 as jfmoney,vb.summoney as dfmoney  ");
			sql.append("          from ( select * from  bis_verifi_book  where   NTYPE=1 and NSTATE<2 and CTYPE=3 and  verifi_num=:code  ");
			sql.append("            ) vb  ");
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("code", code);
			SQLQuery sqlQuery=createSQLQuery(sql.toString(), params);
			return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		return null;
	}
	
	/**
	 * 应付挂账、现结组合金蝶接口数据
	 * @param code 核销编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getYFToJDGJDataList(String code,int ntepy) {
		/**
		  select trunc(sysdate) as dnow ,(case min（vb.CTYPE） when 1 then  '转账凭证'  when 2 then  '银行凭证' end) as pztype,min(vb.cruser) as cruser,min(vb.customname)||feed.ysinfo as info,
		    feed.yscode,min(vb.currency) as currency,min(vb.taxnum) as taxnum,sum(vbi.nowmoney/nvl(vbi.taxnum,1))/1.06 as ymoney,'1' as fangxiang ,sum(vbi.nowmoney/nvl(vbi.taxnum,1))/1.06 as jfmoney, 0 as dfmoney
		           from 
		            ( select * from  bis_verifi_book  where   NTYPE=1 and NSTATE<2 and CTYPE=3  --and  verifi_num='HX20160625113953618'
		             ) vb 
		             left join bis_verifi_book_info vbi on vbi.verifi_num=vb.verifi_num
		             left join base_expense_category_info feed on feed.code=vbi.feedcode
		     
		     group by feed.yscode,feed.ysinfo  
		
		  union all
		   select trunc(sysdate) as dnow,(case  vb.CTYPE when 1 then  '转账凭证'  when 2 then  '银行凭证' end) as pztype,vb.cruser as cruser,vb.customname||'拖运费' as info,
				'22210101'as yscode,vb.currency,vb.taxnum,vb.summoney-vb.summoney/1.06 as ymoney,'1' as fangxiang ,vb.summoney-vb.summoney/1.06 as jfmoney,0 as dfmoney
				       from ( select * from  bis_verifi_book  where   NTYPE=1 and NSTATE<2 and CTYPE=3 --and  verifi_num='HX20160625113953618'
				         ) vb 
		   union all
		 
		 select trunc(sysdate) as dnow,(case  vb.CTYPE when 1 then  '转账凭证'  when 2 then  '银行凭证' end) as pztype,vb.cruser as cruser,vb.customname||'拖运费' as info,
		    '2202'as yscode,vb.currency,vb.taxnum,vb.summoney as ymoney,'-1' as fangxiang ,0 as jfmoney,vb.summoney  as dfmoney
		           from ( select * from  bis_verifi_book  where   NTYPE=1 and NSTATE<2 and CTYPE=3 --and  verifi_num='HX20160625113953618'
		 */
		
		
		if(code!=null && !"".equals(code)){
			String yingfuzhangkuancode=PropertiesUtil.getPropertiesByName("yingfuzhangkuancode", "jdbill");
			//String yingfuzhangkuaninfo1=PropertiesUtil.getPropertiesByName("yingfuzhangkuaninfo1", "jdbill");
			String yingfuzhangkuaninfo2=PropertiesUtil.getPropertiesByName("yingfuzhangkuaninfo2", "jdbill");
			String jinxiangshuicode=PropertiesUtil.getPropertiesByName("jinxiangshuicode", "jdbill");
			String jinxiangshuiinfo=PropertiesUtil.getPropertiesByName("jinxiangshuiinfo", "jdbill");
			String shoukuancode=PropertiesUtil.getPropertiesByName("shoukuancode", "jdbill");
			String shoukuaninfo=PropertiesUtil.getPropertiesByName("shoukuaninfo", "jdbill");
			if(2==ntepy){
				yingfuzhangkuancode=shoukuancode;
				yingfuzhangkuaninfo2=shoukuaninfo;
			}
			String nowDate=DateUtils.getDate();
			StringBuffer sql=new StringBuffer();
			 
			sql.append("  select '").append(nowDate).append("' as dnow ,(case "+ntepy+" when 1 then  '转账凭证'  when 2 then  '银行凭证' end) as pztype,min(vb.cruser) as cruser,min(vb.customname)||feed.yfinfo as info, ");
			sql.append("    feed.yfcode as yscode,min(vb.currency) as currency,min(vb.taxnum) as taxnum,sum(vbi.nowmoney/nvl(vbi.taxnum,1))/1.06 as ymoney,'1' as fangxiang ,sum(vbi.nowmoney/nvl(vbi.taxnum,1))/1.06 as jfmoney, 0 as dfmoney ");
			sql.append("            from  ");
			sql.append("             ( select * from  bis_verifi_book  where   NTYPE=2 and NSTATE<2 and CTYPE<3  and  verifi_num=:code ");
			sql.append("              ) vb  ");
			sql.append("              left join bis_verifi_book_info vbi on vbi.verifi_num=vb.verifi_num ");
			sql.append("              left join base_expense_category_info feed on feed.code=vbi.feedcode ");
			sql.append("      group by feed.yfcode, feed.yfinfo  ");

			sql.append("   union all ");
			sql.append("    select '").append(nowDate).append("' as dnow,(case  "+ntepy+" when 1 then  '转账凭证'  when 2 then  '银行凭证' end) as pztype,vb.cruser as cruser,vb.customname||'").append(yingfuzhangkuaninfo2+jinxiangshuiinfo).append("' as info, ");
			sql.append(" 		'").append(jinxiangshuicode).append("'as yscode,vb.currency,vb.taxnum,vb.summoney-vb.summoney/1.06 as ymoney,'1' as fangxiang ,vb.summoney-vb.summoney/1.06 as jfmoney,0 as dfmoney ");
			sql.append(" 		       from ( select * from  bis_verifi_book  where   NTYPE=2 and NSTATE<2 and CTYPE<3 and  verifi_num=:code ");
			sql.append(" 		         ) vb  ");
			sql.append("    union all ");

			sql.append("  select '").append(nowDate).append("' as dnow,(case  "+ntepy+" when 1 then  '转账凭证'  when 2 then  '银行凭证' end) as pztype,vb.cruser as cruser,(case  vb.CTYPE when 1 then '应付'  when 2 then  '付' end)||vb.customname||(case  vb.CTYPE when 1 then  ''  when 2 then  '代理' end)||'").append(yingfuzhangkuaninfo2).append("' as info, ");
			sql.append("    '").append(yingfuzhangkuancode).append("'as yscode,vb.currency,vb.taxnum,vb.summoney as ymoney,'-1' as fangxiang ,0 as jfmoney,vb.summoney  as dfmoney ");
			sql.append("     from ( select * from  bis_verifi_book  where   NTYPE=2 and NSTATE<2 and CTYPE<3 and  verifi_num=:code ) vb ");
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("code", code);
			SQLQuery sqlQuery=createSQLQuery(sql.toString(), params);
			return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		return null;
	}
	
	/**
	 * 应付冲账金蝶接口数据
	 * @param code 核销编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getYFToJDCDataList(String code,int ntepy) {
		/**
	 select trunc(sysdate) as dnow,(case  vb.CTYPE when 1 then  '转账凭证'  when 2 then  '银行凭证' end) as pztype,vb.cruser as cruser,'付'||vb.customname||'拖运费' as info,
    '2202'as yscode,vb.currency,vb.taxnum,vb.summoney as ymoney,'-1' as fangxiang ,0 as jfmoney,vb.summoney  as dfmoney
           from ( select * from  bis_verifi_book  where   NTYPE=1 and NSTATE<2 and CTYPE=3 --and  verifi_num='HX20160625113953618'
             ) vb 
union all
  select trunc(sysdate) as dnow,(case  vb.CTYPE when 1 then  '转账凭证'  when 2 then  '银行凭证' end) as pztype,vb.cruser as cruser,'付'||vb.customname||'拖运费' as info,
		'22210101'as yscode,vb.currency,vb.taxnum,vb.summoney-vb.summoney/1.06 as ymoney,'1' as fangxiang ,vb.summoney-vb.summoney/1.06 as jfmoney,0 as dfmoney
		       from ( select * from  bis_verifi_book  where   NTYPE=1 and NSTATE<2 and CTYPE=3 --and  verifi_num='HX20160625113953618'
		         ) vb 
		 */
		
		if(code!=null && !"".equals(code)){
			String yingfuzhangkuancode=PropertiesUtil.getPropertiesByName("yingfuzhangkuancode", "jdbill");
			//String yingfuzhangkuaninfo1=PropertiesUtil.getPropertiesByName("yingfuzhangkuaninfo1", "jdbill");
			String yingfuzhangkuaninfo2=PropertiesUtil.getPropertiesByName("yingfuzhangkuaninfo2", "jdbill");
			String shoukuancode=PropertiesUtil.getPropertiesByName("shoukuancode", "jdbill");
			String nowDate=DateUtils.getDate();
			StringBuffer sql=new StringBuffer();
			 
			sql.append("  select '").append(nowDate).append("' as dnow,'银行凭证'  as pztype,vb.cruser as cruser,'付'||vb.customname||'").append(yingfuzhangkuaninfo2).append("' as info, ");
			sql.append("    '").append(yingfuzhangkuancode).append("'as yscode,vb.currency,vb.taxnum,vb.summoney as ymoney,'1' as fangxiang ,0 as dfmoney,vb.summoney  as jfmoney ");
			sql.append("     from ( select * from  bis_verifi_book  where   NTYPE=2 and NSTATE<2 and CTYPE=3 and  verifi_num=:code ) vb");
			sql.append("   union all ");
			sql.append("    select '").append(nowDate).append("' as dnow,  '银行凭证'  as pztype,vb.cruser as cruser,'付'||vb.customname||'").append(yingfuzhangkuaninfo2).append("' as info, ");
			sql.append(" 		'").append(shoukuancode).append("'as yscode,vb.currency,vb.taxnum,vb.summoney as ymoney,'-1' as fangxiang ,vb.summoney as dfmoney,0 as  jfmoney");
			sql.append(" 		       from ( select * from  bis_verifi_book  where   NTYPE=2 and NSTATE<2 and CTYPE=3 and  verifi_num=:code ");
			sql.append(" 		         ) vb  ");
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("code", code);
			SQLQuery sqlQuery=createSQLQuery(sql.toString(), params);
			return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		return null;
	}
	
	public  void updateVerifiBook(String code,int nstate,String pzNum){
		Map<String,Object> params = new HashMap<String,Object>();
		String sql="update BIS_VERIFI_BOOK t set t.pz_num=:pzNum ,t.posttime=:nowdate ,t.poststate=:nstate where t.verifi_num=:code ";
		params.put("nowdate", new Date());
		params.put("pzNum", pzNum);
		params.put("nstate", nstate);
		params.put("code", code);
		SQLQuery sqlQuery=createSQLQuery(sql, params);
		sqlQuery.executeUpdate();
	}
}
