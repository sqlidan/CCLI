package com.haiersoft.ccli.wms.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.AsnActionDao;
import com.haiersoft.ccli.wms.entity.AsnAction;

/**
 * 
 * @author Connor.M
 * @ClassName: AsnActionService
 * @Description: ASN计费区间Service
 * @date 2016年3月4日 下午4:29:46
 */
@Service
public class AsnActionService extends BaseService<AsnAction, Integer> {

	@Autowired
	private AsnActionDao asnActionDao;
	
	@Override
    public HibernateDao<AsnAction, Integer> getEntityDao() {
	    return asnActionDao;
    }
	/**
	 * 按字段ASN_ID,SKU查询对象信息
	 * @param queryStr 查询内容
	 * @return 计费对象
	 */
	public AsnAction getAsnActionObj(String asn,String sku) {
		AsnAction getObj=null;
		if(asn!=null && !"".equals(asn) &&  sku!=null && !"".equals(sku)){
			List<AsnAction> getList=asnActionDao.getAsnActionObj(asn, sku);
			if(getList!=null && getList.size()>0){
				getObj=getList.get(0);
			}
		}
		return getObj;
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 获得SEQ
	 * @date 2016年3月23日 下午3:24:41 
	 * @return
	 * @throws
	 */
	public int getSeqId(){
		return asnActionDao.getSequenceId("SEQ_ASN_ACTION");
	}
	
	//获得ASN区间表数据
	public List<AsnAction> getAsnAction(List<PropertyFilter> actionPfs) {
		return asnActionDao.find(actionPfs);
	}
	//查询是否存在货转单
	public List<AsnAction> findhz(List<PropertyFilter> pf) {
		return asnActionDao.find(pf);
	}
	
}
