package com.haiersoft.ccli.report.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.report.dao.PaymentReportDao;
import com.haiersoft.ccli.report.entity.PaymentReportStock;

/**
 * 
 * @author cuij
 * @ClassName: PaymentReportService
 * @Description: 业务付款申请单汇总表Service
 * @date 2016年6月24日09:38:25
 */
@Service
public class PaymentReportService {
	
	@Autowired
	private PaymentReportDao paymentReportDao;	
	public Page<PaymentReportStock> searchStockReport(Page<PaymentReportStock> page, PaymentReportStock paymentReportStock){
		
		return paymentReportDao.searchStockReport(page, paymentReportStock);
	}
	/**
	 * 在库明细
	 * @param itemNum 提单号
	 * @param cunNum 厢号
	 * @param stockIn 客户id
	 * @param linkId 联系单号
	 * @param strTime 入库时间开始
	 * @param endTime 入库时间结束
	 * @return
	 */
	public List<Map<String, Object>> findRepot(Integer ntype,String itemNum,String cunNum,String stockIn,String linkId,String strTime,String endTime){
		List<Map<String,Object>> getlist=null;
		if(ntype!=null){
			if(1==ntype){
				getlist=paymentReportDao.findRepotPT(itemNum, cunNum, stockIn, linkId, strTime, endTime);
			}else if(2==ntype){
				getlist=paymentReportDao.findRepotJP(itemNum, cunNum, stockIn, linkId, strTime, endTime);
			}else{
				getlist=paymentReportDao.findRepotOTE(itemNum, cunNum, stockIn, linkId, strTime, endTime);
			}
		}
		return getlist;
	}
	/**
	 * 出入库明细
	 * @param itemNum 提单号
	 * @param cunNum 厢号
	 * @param stockIn 客户id
	 * @param linkId 联系单号
	 * @param strTime 入库时间开始
	 * @param endTime 入库时间结束
	 * @return
	 */
	public List<Map<String, Object>> findRepotInAndOut(String itemNum,String cunNum,String stockIn,String linkId,String strTime,String endTime){
		List<Map<String,Object>> getlist=paymentReportDao.findRepotInAndOut(itemNum, cunNum, stockIn, linkId, strTime, endTime);
		return getlist;
	}
}
