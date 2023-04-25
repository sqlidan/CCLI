package com.haiersoft.ccli.cost.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.cost.dao.AsnActionLogDao;
import com.haiersoft.ccli.cost.entity.BisAsnActionLog;
import com.haiersoft.ccli.wms.entity.AsnAction;
 
@Service
public class AsnActionLogService extends BaseService<BisAsnActionLog, Integer> {
	@Autowired
	private AsnActionLogDao asnActionLogDao;
	
	@Override
	public HibernateDao<BisAsnActionLog, Integer> getEntityDao() {
		return asnActionLogDao;
    }
	
	/**
	 * 记录ASN区间表日志
	 * @author GLZ
	 * @param obj   asnAction对象
	 * @param type   类型
	 * @param oldPiece 初始件数
	 * @param changePiece  修改件数
	 * @param remark  备注
	 */  
	public void saveLog(AsnAction obj,String type,Integer oldPiece,Integer changePiece,String remark){
//		 BisAsnActionLog logObj = new BisAsnActionLog();
//		 logObj.setAsnActionId(obj.getId());
//		 logObj.setOperateTime(new Date());
//		 User user=UserUtil.getCurrentUser();
//		 logObj.setOperator(user!=null?user.getName():"");
//		 logObj.setType(type);
//		 if("1".equals(type)||"2".equals(type)){
//			 logObj.setLinkId(obj.getEnterId());
//			 logObj.setLinkType(1);
//		 }else if("3".equals(type)||"4".equals(type)||"5".equals(type)){
//			 logObj.setLinkId(obj.getOutId());
//			 logObj.setLinkType(2);
//		 }else if("6".equals(type)||"7".equals(type)){
//			 logObj.setLinkId(obj.getTransferId());
//			 logObj.setLinkType(3);
//		 }
//		 logObj.setId(this.getSequenceId());
//		 logObj.setAsn(obj.getAsn());
//		 logObj.setOldPiece(oldPiece);
//		 logObj.setChangePiece(changePiece);
//		 logObj.setNowPiece(obj.getNum()!=null?obj.getNum():0);
//		 logObj.setNetWeight(obj.getNetWeight()!=null?obj.getNetWeight():0.00);
//		 logObj.setGrossWeight(obj.getGrossWeight()!=null?obj.getGrossWeight():0.00);
//		 logObj.setRemark(remark);
//		 asnActionLogDao.saveTheLog(logObj);
	}
	
	/**
	 * 记录ASN区间表日志(手持用)
	 * @author GLZ
	 * @param obj   asnAction对象
	 * @param type   类型
	 * @param oldPiece 初始件数
	 * @param changePiece  修改件数
	 * @param remark  备注
	 */  
	public void saveLog(AsnAction obj,String type,Integer oldPiece,Integer changePiece,String remark,String userName){
//		 BisAsnActionLog logObj = new BisAsnActionLog();
//		 logObj.setAsnActionId(obj.getId());
//		 logObj.setOperateTime(new Date());
//		 logObj.setOperator(userName);
//		 logObj.setType(type);
//		 if("1".equals(type)||"2".equals(type)){
//			 logObj.setLinkId(obj.getEnterId());
//			 logObj.setLinkType(1);
//		 }else if("3".equals(type)||"4".equals(type)||"5".equals(type)){
//			 logObj.setLinkId(obj.getOutId());
//			 logObj.setLinkType(2);
//		 }else if("6".equals(type)||"7".equals(type)){
//			 logObj.setLinkId(obj.getTransferId());
//			 logObj.setLinkType(3);
//		 }
//		 logObj.setId(this.getSequenceId());
//		 logObj.setAsn(obj.getAsn());
//		 logObj.setOldPiece(oldPiece);
//		 logObj.setChangePiece(changePiece);
//		 logObj.setNowPiece(obj.getNum()!=null?obj.getNum():0);
//		 logObj.setNetWeight(obj.getNetWeight()!=null?obj.getNetWeight():0.00);
//		 logObj.setGrossWeight(obj.getGrossWeight()!=null?obj.getGrossWeight():0.00);
//		 logObj.setRemark(remark);
//		 asnActionLogDao.saveTheLog(logObj);
	}

	/*
	 * 获得sequence
	 */
	public Integer getSequenceId() {
		return asnActionLogDao.getSequenceId("SEQ_ASN_ACTION_LOG");
		
	}
}
