package com.haiersoft.ccli.wms.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.wms.entity.BisTransferStockInfo;

@Repository
public class TransferInfoDao  extends HibernateDao<BisTransferStockInfo, String> {
	/**
	 * 根据货转单号删除对应所有货转单明细
	 * @param asnId
	 * @return
	 */
	public void deleteTransferStockInfo(String transferId){
		if(transferId!=null && !"".equals(transferId)){
			String hql="delete BisTransferStockInfo a where a.transferId=?0";
			batchExecute(hql, transferId);
		}
	}
	/**
	 * 根据货转单id，提单号，厢号，SKU，入库类型删除明细对象
	 * @param transferId //货转单id
	 * @param billNum  //提单号
	 * @param cnNum  //厢号
	 * @param skuNum //SKU
	 * @param isLab //入库类型
	 * @return void
	 */ 
	public void delTransferStockInfo(String transferId,String billNum,String cnNum,String skuNum,String isLab){
		if(transferId!=null && !"".equals(transferId) && billNum!=null &&  !"".equals(billNum) && cnNum!=null &&!"".equals(cnNum) && skuNum!=null && !"".equals(skuNum) && isLab!=null && !"".equals(isLab)){
			String hql="delete BisTransferStockInfo where transferId=:transferId and billNum=:billNum and ctnNum=:ctnNum and sku=:skuId and enterState=:isLab";
			HashMap<String,Object> parme=new HashMap<String,Object>();
			parme.put("transferId", transferId);
			parme.put("billNum", billNum);
			parme.put("ctnNum", cnNum);
			parme.put("skuId", skuNum);
			parme.put("isLab", isLab);
			batchExecute(hql, parme);
		}
	}
	
	/**
	 * 根据货转单id，提单号，厢号，SKU，入库类型获取明细对象
	 * @param transferId //货转单id
	 * @param billNum  //提单号
	 * @param cnNum  //厢号
	 * @param skuNum //SKU
	 * @param isLab //入库类型
	 * @return BisLoadingOrderInfo
	 */
	public BisTransferStockInfo findTransferInfoObj(String transferId,String billNum,String cnNum,String skuNum,String isLab){
		BisTransferStockInfo retObj=null;
		if(transferId!=null && !"".equals(transferId) && billNum!=null &&  !"".equals(billNum) && cnNum!=null &&!"".equals(cnNum) && skuNum!=null && !"".equals(skuNum) && isLab!=null && !"".equals(isLab)){
			String hql="from BisTransferStockInfo  where transferId=:transferId and billNum=:billNum and ctnNum=:ctnNum and sku=:skuId and enterState=:isLab";
			HashMap<String,Object> parme=new HashMap<String,Object>();
			parme.put("transferId", transferId);
			parme.put("billNum", billNum);
			parme.put("ctnNum", cnNum);
			parme.put("skuId", skuNum);
			parme.put("isLab", isLab);
			List<BisTransferStockInfo> getList=this.find(hql, parme);
			if(getList!=null && getList.size()>0){
				retObj=getList.get(0);
			}
		}
		return retObj;
	}
	
	/**
	 * 根据货转单号获取货转明细信息
	 * @param transferId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findInfoList(String transferId){
		if(transferId!=null && !"".equals(transferId)){
			HashMap<String,Object> parme=new HashMap<String,Object>();
			StringBuffer sb=new StringBuffer();
			sb.append("select                                                                              ");
			sb.append("       bill_num,                                                                    ");
			sb.append("       ctn_num,                                                                     ");
			sb.append("       o.cargo_name,                                                                ");
			sb.append("       o.sku_id,                                                                    ");
			sb.append("       o.piece,                                                                     ");
			sb.append("       o.gross_weight,                                                              ");
			sb.append("       o.net_weight,                                                                ");
			sb.append("       s.NET_SINGLE,                                                                ");
			sb.append("       s.GROSS_SINGLE,                                                              ");
			sb.append("       enter_state,                                                                 ");
			sb.append("       o.RK_NUM,                                                                    ");
			sb.append("       o.ENTER_STATE || '$$' || o.bill_num || '$$' || o.ctn_num || '$$' ||          ");
			sb.append("       o.SKU_ID as lab                                                              ");
			sb.append("  from (select                                                                      ");
			sb.append("               bill_num,                                                            ");
			sb.append("               ctn_num,                                                             ");
			sb.append("               cargo_name,                                                          ");
			sb.append("               sku_id,                                                              ");
			sb.append("               sum(piece) as piece,                                                 ");
			sb.append("               sum(gross_weight) as gross_weight,                                   ");
			sb.append("               sum(net_weight) as net_weight,                                       ");
			sb.append("               enter_state,                                                         ");
			sb.append("               rk_num                                                               ");
			sb.append("          from bis_transfer_stock_info                                              ");
			sb.append("         where transfer_link_id =:transferId                                        ");
			sb.append("          group by                                                                  ");
			sb.append("              bill_num,                                                             ");
			sb.append("               ctn_num,                                                             ");
			sb.append("               cargo_name,                                                          ");
			sb.append("               sku_id,                                                              ");
			sb.append("               enter_state,                                                         ");
			sb.append("               rk_num                                                               ");
			sb.append("         ) o                                                                        ");
			sb.append("  left join base_sku_base_info s                                                    ");
			sb.append("    on o.sku_id = s.sku_id                                                          ");
			sb.append(" where s.DEL_FLAG = 0                                                               ");
			parme.put("transferId", transferId);
			SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
			return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();	
		}
		return null;
	}
}
