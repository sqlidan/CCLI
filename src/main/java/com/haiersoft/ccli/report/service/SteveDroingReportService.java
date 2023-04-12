package com.haiersoft.ccli.report.service;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.report.dao.SteveDroingReportDao;

/**
 * 
 * @author slh 
 * @ClassName: SteveDroingReportService 装卸队 装卸数量统计
 * @date 2016年6月28日  
 */
@Service
public class SteveDroingReportService {
	
	@Autowired
	private SteveDroingReportDao steveDroingReportDao;
	
	 /*
	 * 入库装卸队统计  by slh 20160628
	 */
	public Map<String, Object> getSteveReportInfo(String reportType,String clientId, String startTime,String endTime,int npage,int pageSize){
		return steveDroingReportDao.getSteveReportInfo(reportType,clientId, startTime, endTime, npage, pageSize);
	}
	/**
	 * 获取客户list名称
	 * @param reportType
	 * @param clientId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<String> findCustList(String reportType,String clientId, String startTime,String endTime){
		return steveDroingReportDao.findCustList(reportType, clientId, startTime, endTime);
	}
	/**
	 * 获取类型集合
	 * @param reportType
	 * @param clientId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<Object[]> findLxList(List<String> custlist,String reportType,String clientId, String startTime,String endTime){
		return steveDroingReportDao.findLxList(custlist, reportType, clientId, startTime, endTime);
	}
	/**
	 * 总合计
	 * @param custlist
	 * @param reportType
	 * @param clientId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<Object[]> findSumList(List<String> custlist,String reportType,String clientId, String startTime,String endTime){
		return steveDroingReportDao.findSumList(custlist, reportType, clientId, startTime, endTime);
	}
	/**
	 * 根据库房类别进行存储对应的list集合
	 * @param reportType
	 * @param clientId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<Object[]> findSteveReport(String lx,String label,List<String> custlist,String reportType,String clientId, String startTime,String endTime){
		return steveDroingReportDao.findSteveReport(lx,label,custlist,reportType, clientId, startTime, endTime);
	}
	
}
