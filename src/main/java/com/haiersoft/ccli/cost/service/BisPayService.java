package com.haiersoft.ccli.cost.service;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.cost.dao.BisPayDao;
import com.haiersoft.ccli.cost.dao.BisPayInfoDao;
import com.haiersoft.ccli.cost.entity.BisPay;
@Service
public class BisPayService extends BaseService<BisPay, String> {
	
	@Autowired
	private BisPayDao bisPayDao;
	
	@Autowired
	private BisPayInfoDao bisPayInfoDao;
	
	@Override
	public HibernateDao<BisPay, String> getEntityDao() {
		return bisPayDao;
	}
	/**
	 * 批量审核业务付款明细
	 * @author jxk
	 * @param pay_id
	 * @date 2018-11-08
	 * @return
	 */
	@Transactional(readOnly = false)
	public String updatePays(String pay_id,String state){
		//更新主表
		BisPay bp = bisPayDao.find(pay_id);
        bp.setState(Integer.parseInt(state));//0：未审核 ，1：已审核
        bisPayDao.merge(bp);
        //更新业务付款明细表
        bisPayInfoDao.updatePayInfo(pay_id, state);
		//批量更新应付明细的审核状态
		bisPayInfoDao.updateStock(pay_id,state);
		return "success";
	}

    /**
	 * 
	 * @author pyl
	 * @Description: 获得业务付款单ID
	 * @date 2016年3月5日 下午12:08:24
	 * @return
	 * @throws
	 */
	public String getPayIdToString() {
 		int num = bisPayDao.getSequenceId("SEQ_PAY");
 		String payId =  DateUtils.formatDate(new Date(), "yyyyMMdd") + StringUtils.lpadInt(6, num);
		return payId;
	}
	
	@Transactional(readOnly = false)
	public String deletePayInfos(Integer pay_info_id){
		//批量更新应付明细的审核状态
		bisPayInfoDao.deleteStock(pay_info_id);
		return "success";
	}
}
