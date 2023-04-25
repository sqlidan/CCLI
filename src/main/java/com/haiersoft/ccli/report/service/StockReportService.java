package com.haiersoft.ccli.report.service;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.report.dao.StockReportDao;
import com.haiersoft.ccli.report.entity.Stock;
import com.haiersoft.ccli.wms.entity.BisEnterStock;
/**
 * 
 * @author Connor.M
 * @ClassName: StockReportService
 * @Description: 库存报表Service
 * @date 2016年3月9日 下午2:41:09
 */
@Service
public class StockReportService {
	
	@Autowired
	private StockReportDao stockReportDao;
	
	public Page<Stock> searchStockReport(Page<Stock> page, Stock stock){
		return stockReportDao.searchStockReport(page, stock);
	}
	/**
	 * 在库明细
	 * @param itemNum 提单号
	 * @param cunNum 厢号
	 * @param stockIn 客户id
	 * @param linkId 联系单号
	 * @param strTime 入库时间开始
	 * @param endTime 入库时间结束
	 * @param locationTypen
	 */
	public List<Map<String, Object>> findRepot(Integer ntype,String ifBonded,String itemNum,String cunNum,String stockIn,String linkId,String strTime,String endTime, String locationType){
		List<Map<String,Object>> getlist=stockReportDao.findRepotPT(ntype,ifBonded,itemNum, cunNum, stockIn, linkId, strTime, endTime,locationType);
		/*if(ntype!=null){
			if(1==ntype){
				getlist=stockReportDao.findRepotPT(ntype,ifBonded,itemNum, cunNum, stockIn, linkId, strTime, endTime,locationType);
			}else if(2==ntype){
				getlist=stockReportDao.findRepotJP(ifBonded,itemNum, cunNum, stockIn, linkId, strTime, endTime,locationType);
			}else{
				getlist=stockReportDao.findRepotOTE(ifBonded,itemNum, cunNum, stockIn, linkId, strTime, endTime,locationType);
			}
		}*/
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
	public List<Object[]> findRepotInAndOutObject(Integer ntype,String isBonded,String itemNum,String cunNum,String stockIn,String linkId,String strTime,String endTime){
		return stockReportDao.findRepotInAndOutObject(ntype,isBonded,itemNum, cunNum, stockIn, linkId, strTime, endTime);
	}
	
	public List<Map<String, Object>> findRepotInAndOut(String isBonded,String itemNum,String cunNum,String stockIn,String linkId,String strTime,String endTime){
		return stockReportDao.findRepotInAndOut(isBonded,itemNum, cunNum, stockIn, linkId, strTime, endTime);
	}
	
	public List<Map<String, Object>> findRepotInAndOutByPage(String isBonded,String itemNum,String cunNum,String stockIn,String linkId,String strTime,String endTime,int page){
		return stockReportDao.findRepotInAndOutByPage(isBonded,itemNum, cunNum, stockIn, linkId, strTime, endTime,page);
	}
	/**
	 * 获取总数量
	 * @param isBonded
	 * @param itemNum
	 * @param cunNum
	 * @param stockIn
	 * @param linkId
	 * @param strTime
	 * @param endTime
	 * @return
	 */
	public BigDecimal countInAndOut(String isBonded,String itemNum,String cunNum,String stockIn,String linkId,String strTime,String endTime){
		return stockReportDao.countInAndOut(isBonded, itemNum, cunNum, stockIn, linkId, strTime, endTime);
	}
	
	public List<Map<String, Object>> findRepotInAndOutbyNum(String isBonded,String itemNum,String cunNum,String stockIn,String linkId,String strTime,String endTime,int start,int end){
		return stockReportDao.findRepotInAndOutbyNum(isBonded,itemNum, cunNum, stockIn, linkId, strTime, endTime,start,end);
	}
	/**
	 * 根据条件查出总条数进行分批处理
	 * @param isBonded
	 * @param itemNum
	 * @param cunNum
	 * @param stockIn
	 * @param linkId
	 * @param strTime
	 * @param endTime
	 * @return
	 */
	public Integer getRepotInAndOutCount(String isBonded,String itemNum,String cunNum,String stockIn,String linkId,String strTime,String endTime){
		return stockReportDao.getRepotInAndOutCount(isBonded, itemNum, cunNum, stockIn, linkId, strTime, endTime);
	}
	
	
	/**
	 * 根据提单号和日期获取肉类出入库统计
	 * @param billNum
	 * @param starTime
	 * @param endTime
	 * @param npage
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMeatBillNumTrayInfo(String billNum,String starTime,String endTime,int npage,int pageSize){
		Map<String, Object> getMap=null;
		getMap= stockReportDao.getMeatBillNumTrayInfo(billNum, starTime, endTime, npage, pageSize);
		if(getMap!=null && getMap.size()>0){
			List<Map<String, Object>> getList=(List<Map<String, Object>>) getMap.get("rows");
			if(getList!=null && getList.size()>0){
				Map<String, Object> rowMap=null;
				for(int i=0;i<getList.size();i++){
					rowMap=getList.get(i);
					rowMap.put("LAB", noDoubletStr(rowMap.get("LAB")!=null?rowMap.get("LAB").toString():""));
					getList.set(i, rowMap);
				}//end for
				getMap.put("rows", getList);
			}
		}
		return getMap;
	}
	/**
	 * 根据提单号和日期获取水产出入库统计
	 * @param starTime
	 * @param endTime
	 * @param npage
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getFisheriesBillNumTrayInfo(String starTime,String endTime,int npage,int pageSize){
		Map<String, Object> getMap=null;
		getMap=  stockReportDao.getFisheriesBillNumTrayInfo(starTime, endTime, npage, pageSize);
		if(getMap!=null && getMap.size()>0){
			List<Map<String, Object>> getList=(List<Map<String, Object>>) getMap.get("rows");
			if(getList!=null && getList.size()>0){
				Map<String, Object> rowMap=null;
				Double getKCNum=0d;
				Double getCKNum=0d;//出库数量
				String keys=null;
				for(int i=0;i<getList.size();i++){
					rowMap=getList.get(i);
					if(keys==null){ 
						getKCNum=Double.valueOf(rowMap.get("ISUM_NET_WEIGHT").toString());
						getCKNum=Double.valueOf(rowMap.get("OSUM_NET_WEIGHT").toString());
						getKCNum=getKCNum-getCKNum;
						rowMap.put("NUM", getKCNum);
					}else{
						if(keys.equals(rowMap.get("BILL_NUM").toString()+rowMap.get("SKU_ID").toString())){
							getCKNum=Double.valueOf(rowMap.get("OSUM_NET_WEIGHT").toString());
							getKCNum=getKCNum-getCKNum;
							rowMap.put("NUM", getKCNum);
						}else{
							getKCNum=Double.valueOf(rowMap.get("ISUM_NET_WEIGHT").toString());
							getCKNum=Double.valueOf(rowMap.get("OSUM_NET_WEIGHT").toString());
							getKCNum=getKCNum-getCKNum;
							rowMap.put("NUM", getKCNum);
						}
					}
					rowMap.put("LAB", noDoubletStr(rowMap.get("LAB")!=null?rowMap.get("LAB").toString():""));
					getList.set(i, rowMap);
					keys=rowMap.get("BILL_NUM").toString()+rowMap.get("SKU_ID").toString();
				}//end for
				getMap.put("rows", getList);
			}
		}
		return getMap;
	}
	/**
	 *排查
	 * @return
	 */
	private String noDoubletStr(String str){
		if(str!=null){
			String [] strList=str.split(",");
			if(strList.length==1){
				return str;
			}else{
				StringBuffer sb=new StringBuffer();
				HashMap<String,Integer> strMap=new HashMap<String,Integer>();
				for(String kid:strList){
					if(null==strMap.get(kid)){
						sb.append(kid).append(",");
					}
					strMap.put(kid, 1);
				}
				return sb.substring(0,sb.length()-1);
			}
		}
		return str;
	}
	
	/*
	 * 根据billNum从库存表中获取客户名
	 */
	public List<Map<String, Object>> getStockNameByBill(String billNum) {
		return stockReportDao.getStockNameByBill(billNum);
	}
	
	/*
	 * 根据trayId 从库存中取 对象
	 * */
	public List getTrayInfoByTrayId(String trayId){
		return stockReportDao.getTrayInfoByTrayId(trayId);
	}
	
	 public Page<Stock> getStockStocks(Page<Stock> page,Integer ntype,String ifBonded,String itemNum,String cunNum,String stockIn,String linkId,String strTime,String endTime, String locationType) {
	        return stockReportDao.getStockStocks(page,ifBonded,itemNum, cunNum, stockIn, linkId, strTime, endTime,locationType);
	 }
	 
	 public Page<Stock> getInOutStockStocks(Page<Stock> page,String isBonded,String itemNum,String cunNum,String stockIn,String linkId,String strTime,String endTime) {
	        return stockReportDao.getInOutStockStocks(page,isBonded,itemNum, cunNum, stockIn, linkId, strTime, endTime);
	 }
	 /**
	  * 倒箱查验明细调整
	  * @param page
	  * @param ifBonded
	  * @param itemNum
	  * @param cunNum
	  * @param stockIn
	  * @param strTime
	  * @param endTime
	  * @return
	  */
	 public Page<Stock> getBoxCheckStocks(Page<Stock> page,String ifBonded,String itemNum,String cunNum,String stockIn,String strTime,String endTime) {
	        return stockReportDao.getBoxCheckStocks(page,ifBonded,itemNum, cunNum, stockIn,strTime, endTime);
	 }
	 
	 public Page<Stock> getCompanyStocks(Page<Stock> page,String reportType,String itemNum,String stockIn,String strTime,String endTime) {
	        return stockReportDao.getCompanyStocks(page,reportType,itemNum,stockIn,strTime,endTime);
	 }
	 /**
	  * 导出倒箱跟查验明细
	  * @param ifBonded
	  * @param itemNum
	  * @param cunNum
	  * @param stockIn
	  * @param strTime
	  * @param endTime
	  * @return
	  */
	 public List<Map<String, Object>> boxCheckExcel(String ifBonded,String itemNum,String cunNum,String stockIn,String strTime,String endTime){
			return stockReportDao.boxCheckExcel(ifBonded,itemNum, cunNum, stockIn,strTime, endTime);
	 }
	 /**
	  * 导出报关报检明细
	  * @param reportType
	  * @param itemNum
	  * @param stockIn
	  * @param strTime
	  * @param endTime
	  * @return
	  */
	 public List<Map<String, Object>> companyExcel(String reportType,String itemNum,String stockIn,String strTime,String endTime){
			return stockReportDao.companyExcel(reportType,itemNum,stockIn,strTime, endTime);
	 }
/*	@Override
	public HibernateDao<Stock, String> getEntityDao() {
		// TODO Auto-generated method stub
		return stockReportDao;
	}*/


	/**
	 * @param itemNum  提单号
	 * @param ctnNum  箱号
	 * @param realClientName  客户名称
	 * @throws Exception
	 * @throws
	 * @Description: 在库明细  接口信息（海路通系统）
	 */
	public List<Map<String, Object>> inStockDetailReportInfo(String itemNum, String ctnNum, String realClientName){
		List<Map<String,Object>> getList=stockReportDao.inStockDetailReportInfo(itemNum, ctnNum, realClientName);

		return getList;
	}


}
