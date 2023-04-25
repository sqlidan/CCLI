package com.haiersoft.ccli.report.service;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.report.dao.PaymentDetailReportDao;
/**
 * 
 * @author cuij
 * @ClassName: PaymentDetailReportService
 * @Description: 业务付款申请单明细表Service
 * @date 2016年6月24日17:05:47
 */
@Service
public class PaymentDetailReportService {
	
	@Autowired
	private PaymentDetailReportDao paymentDetailReportDao;	
	public List<Map<String, Object>> searchStockReport(String payId,String clientName,String payee,String billNum,String searchStrTime,String searchEndTime){
		return paymentDetailReportDao.searchStockReport(payId, clientName, payee, billNum, searchStrTime, searchEndTime);
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
				getlist=paymentDetailReportDao.findRepotPT(itemNum, cunNum, stockIn, linkId, strTime, endTime);
			}else if(2==ntype){
				getlist=paymentDetailReportDao.findRepotJP(itemNum, cunNum, stockIn, linkId, strTime, endTime);
			}else{
				getlist=paymentDetailReportDao.findRepotOTE(itemNum, cunNum, stockIn, linkId, strTime, endTime);
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
		List<Map<String,Object>> getlist=paymentDetailReportDao.findRepotInAndOut(itemNum, cunNum, stockIn, linkId, strTime, endTime);
		return getlist;
	}
}
