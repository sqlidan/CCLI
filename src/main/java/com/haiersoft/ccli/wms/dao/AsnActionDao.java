package com.haiersoft.ccli.wms.dao;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.wms.entity.AsnAction;
import com.haiersoft.ccli.wms.entity.TrayInfo;

/**
 * 
 * @author Connor.M
 * @ClassName: AsnActionDao
 * @Description: ASN计费区间DAO
 * @date 2016年3月4日 下午4:31:00
 */
@Repository
public class AsnActionDao extends HibernateDao<AsnAction, Integer> {
	
	@SuppressWarnings("unchecked")
	public List<AsnAction> getAsnActionObj(String asn,String sku){
		if(asn!=null && !"".equals(asn) &&  sku!=null && !"".equals(sku)){
			String hql=" from AsnAction where asn=?0 and sku=?1";
			Query query= createQuery(hql, asn,sku);
			return query.list();
		}
		return null; 
	}

	public void changePiece(Integer difference, TrayInfo obj) {
			String sql="update bis_asn_action a set a.num=a.num+"+difference+",a.gross_weight=a.gross_weight+"+obj.getGrossSingle()*difference+",a.net_weight=a.net_weight+"+obj.getNetSingle()*difference+" where a.asn=:asn and a.sku=:sku ";
			HashMap<String,Object> parme=new HashMap<String,Object>();
			parme.put("asn", obj.getAsn());
			parme.put("sku", obj.getSkuId());
			SQLQuery sqlQuery=createSQLQuery(sql, parme);
			sqlQuery.executeUpdate();
	}
}
