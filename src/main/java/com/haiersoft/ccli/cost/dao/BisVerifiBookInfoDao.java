package com.haiersoft.ccli.cost.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.utils.PageUtils;
import com.haiersoft.ccli.cost.entity.BisVerifiBookInfo;
/**
 * 核销明细
 * @author LZG
 *
 */
@Repository
public class BisVerifiBookInfoDao  extends HibernateDao<BisVerifiBookInfo, String> {
	/***
	 * 根据核销编号获取核销明细对象
	 * @param num
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BisVerifiBookInfo> getVerifiBookInfoList(String num){
		if(num!=null && !"".equals(num)){
			String hql=" from BisVerifiBookInfo where verifiNum=?0";
			Query query= createQuery(hql, num);
			return query.list();
		}
		return null; 
	}
	
	/**
	 * 核销明细
	 * @param num 核销单编号
	 * @return
	 */
	public  Map<String, Object> getBisVerifiBookInfoList(String num,int nPage,int nPageSize) {
		if(num !=null && !"".equals(num)){
			Map<String,Object> returnMap = new HashMap<String,Object>();
			Map<String,Object> params = new HashMap<String,Object>();
			StringBuffer sb=new StringBuffer();
			sb.append("select b.customs_num,b.customs_name,trunc(b.BILL_DATE,'mm') BILL_DATE,b.link_id,b.bill_num,b.fee_name,b.price,sv.ysmoney,sv.nowmoney  from ");
			sb.append(" ( select v.standing_num,v.ysmoney,v.nowmoney from bis_verifi_book_info v  where v.verifi_num =:num )  sv left join bis_standing_book  b on sv.standing_num=b.standing_num ");
			params.put("num", num);
			long totalCount = countSqlResult(sb.toString(), params);
			SQLQuery sqlQuery=createSQLQuery(sb.toString(), params);
			sqlQuery.setFirstResult(PageUtils.calBeginIndex(nPage, nPageSize,Integer.valueOf(String.valueOf(totalCount))));
			sqlQuery.setMaxResults(nPageSize);
			returnMap.put("total", totalCount);
			returnMap.put("rows", sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list());
			return  returnMap;
		}
		return null;
	}
}
