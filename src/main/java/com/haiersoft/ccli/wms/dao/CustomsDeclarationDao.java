package com.haiersoft.ccli.wms.dao;

import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.wms.entity.BisCustomsDeclaration;

/**
 * 
 * @author pyl
 * @ClassName: CustomsDeclarationDao
 * @Description: 入库报关单DAO
 * @date 2016年4月16日 下午3:52:06
 */
@Repository
public class CustomsDeclarationDao extends HibernateDao<BisCustomsDeclaration, String>{
	
//	/**
//	 * 按条件查询入库联系单和货转联系单集合
//	 * @param page
//	 * @param billNum //提单id
//	 * @param transNum//装车单号
//	 * @param ctnNum //厢号
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public List<Map<String,Object>> findList(Page page,String linkNum,String billNum,String transNum,String ctnNum){
//		StringBuffer sb=new StringBuffer();
//		HashMap<String,Object> parme=new HashMap<String,Object>();
//		if(linkNum!=null && !"".equals(linkNum)){
//			sb.append("select r.link_id as codenum,0 as ntype,r.stock_in as stock ,r.stock_id as stockid,r.item_num as itemnum,r.operate_time  from bis_enter_stock r where r.link_id=:linkNum");
//			parme.put("linkNum", linkNum);
//		}else if(billNum!=null && !"".equals(billNum)){
//			sb.append("select r.link_id as codenum,0 as ntype,r.stock_in as stock ,r.stock_id as stockid,r.item_num as itemnum,r.operate_time  from bis_enter_stock r where r.item_num=:ritem_num");
//			sb.append(" union ");
//			sb.append("select t.transfer_id as codenum,1 as ntype,t.stock_in as strock,t.stock_in_id as stockid ,t.item_num as itemnum,t.operate_time  from  bis_transfer_stock t where t.item_num=:titem_num");
//			parme.put("ritem_num", billNum);
//			parme.put("titem_num", billNum);
//		}else{
//			if((transNum!=null && !"".equals(transNum)) || (ctnNum!=null && !"".equals(ctnNum))){
//				sb.append("select r.link_id as codenum,0 as ntype,r.stock_in as stock ,r.stock_id as stockid,r.item_num as itemnum,r.operate_time  from bis_enter_stock r where r.item_num in (select loading_plan_num from bis_loading_info z where 1=1 ").append(transNum!=null && !"".equals(transNum)?" and z.loading_truck_num=:truckNum1":"").append((ctnNum!=null && !"".equals(ctnNum))?" and z.ctn_num=:ctnNum1":"").append(")");
//				sb.append(" union ");
//				sb.append("select t.transfer_id as codenum,1 as ntype,t.stock_in as strock,t.stock_in_id as stockid ,t.item_num as itemnum,t.operate_time  from  bis_transfer_stock t where t.item_num in (select loading_plan_num from bis_loading_info z where 1=1 ").append(transNum!=null && !"".equals(transNum)?" and z.loading_truck_num=:truckNum2":"").append(ctnNum!=null && !"".equals(ctnNum)?" and z.ctn_num=:ctnNum2":"").append(")") ; 
//				if(transNum!=null && !"".equals(transNum)){
//					parme.put("truckNum1", transNum);
//					parme.put("truckNum2", transNum);
//				}
//				if(ctnNum!=null && !"".equals(ctnNum)){
//					parme.put("ctnNum1", ctnNum);
//					parme.put("ctnNum2", ctnNum);
//				}
//			}
//		}
//		SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
//		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
//	}
//	
}
