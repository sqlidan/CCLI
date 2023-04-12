package com.haiersoft.ccli.remoting.hand.allocation.service;
import java.util.List;
import java.util.Map;
import javax.jws.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.common.utils.Result;
import com.haiersoft.ccli.wms.dao.ASNDao;
import com.haiersoft.ccli.wms.dao.LoadingInfoDao;
/**
 * 
 * @author PYL
 * @ClassName: AllocationWebService
 * @Description: 作业监控webService接口
 */
@WebService
@Service
public class AllocationWebService {
	//@Autowired
	//private TrayInfoDao trayInfoDao;
	@Autowired
	private ASNDao asnDao;
	//@Autowired
	//private EnterStockDao enterStockDao;
	//@Autowired
	//private OutStockDao outStockDao;
	@Autowired
	private LoadingInfoDao loadingInfoDao;
	//@Autowired
	//private ExpenseSchemeDao expenseSchemeDao;
	//@Autowired
	//private ExpenseContractDao expenseContractDao;
	//@Autowired
	//private ExpenseShareDao expenseShareDao;
	//@Autowired
	//private EnterStevedoringDao enterStevedoringDao;
	//@Autowired
	//private EnterStevedoringService enterStevedoringService;
	//@Autowired
	//private OutStevedoringDao outStevedoringDao;
	//@Autowired
	//private OutStevedoringService outStevedoringService;
	//@Autowired
	//private ClientDao clientDao;
	
	/**
	 * 
	 * @author GLZ
	 * @Description: 获得用户入库作业任务
	 * @param userName  用户名称
	 * @return
	 * @throws
	 */
	public String getEnterJobList(String userName) {
		Result<Object> result = new Result<Object>();
		
		List<Map<String,Object>> asnList = asnDao.getAsnWithRemind(userName);
		if(!asnList.isEmpty()){
			result.setCode(0);
			result.setMsg("查询成功！");
			result.setList(asnList);
		} else {
			result.setCode(1);
			result.setMsg("无此用户的入库作业任务！");
		}
		String res = JSON.toJSONString(result);
		return res;
	}
	
	
	/**
	 * 
	 * @author GLZ
	 * @Description: 获得用户出库作业任务
	 * @param userName  用户名称
	 * @return
	 * @throws
	 */
	public String getOutJobList(String userName) {
	Result<Object> result = new Result<Object>();
		
		List<Map<String,Object>> loadingList = loadingInfoDao.getLoadingWithRemind(userName);
		if(!loadingList.isEmpty()){
			result.setCode(0);
			result.setMsg("查询成功！");
			result.setList(loadingList);
		} else {
			result.setCode(1);
			result.setMsg("无此用户的入库作业任务！");
		}
		String res = JSON.toJSONString(result);
		return res;
	}
	
	 
}
