package com.haiersoft.ccli.remoting.hand.goBack.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.Result;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.wms.dao.LoadingInfoDao;
import com.haiersoft.ccli.wms.dao.TrayInfoDao;
import com.haiersoft.ccli.wms.entity.BackHistoryInfo;
import com.haiersoft.ccli.wms.entity.BisLoadingInfo;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import com.haiersoft.ccli.wms.service.BackHistoryInfoService;

/**
 * 
 * @author Connor.M
 * @ClassName: GoBackStockWebService
 * @Description: 回库操作WebService
 * @date 2016年3月29日 上午10:42:22
 */
@WebService
@Service
public class GoBackStockWebService {
	
	@Autowired
	private TrayInfoDao trayInfoDao;
	@Autowired
	private LoadingInfoDao loadingInfofDao;
	//@Autowired
	//private ASNService asnService;
	@Autowired
	private BackHistoryInfoService backHistoryInfoService;
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 回库理货查询
	 * @date 2016年3月28日 上午11:18:50 
	 * @param trayCode 托盘号
	 * @return
	 * @throws
	 */
	public String getBackStockTallyByTrayCode(String trayCode){
		Result<TrayInfo> result = new Result<TrayInfo>();
		
		//查询库存 托盘数据
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_trayId", trayCode));
		filters.add(new PropertyFilter("EQS_cargoState", "20"));
		List<TrayInfo> trayInfos = trayInfoDao.find(filters);
		
		//判断是否存在
		if(null != trayInfos && trayInfos.size() == 1){
			TrayInfo trayInfo = trayInfos.get(0);
			result.setObj(trayInfo);
			result.setCode(0);
			result.setMsg("查询成功！");
		}else{
			result.setCode(1);
			result.setMsg("数据错误，该托盘状态错误！");
		}
		String res = JSON.toJSONString(result);
		return res;
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 回库理货确认
	 * @date 2016年3月28日 上午11:22:51 
	 * @param trayCode 托盘号
	 * @param userName 用户名
	 * @return
	 * @throws
	 */
	@Transactional(readOnly = false)
	public String backStockTallyConfirm(String trayCode, String userName){
		Result<TrayInfo> result = new Result<TrayInfo>();
		
		//查询库存 托盘数据
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_trayId", trayCode));
		filters.add(new PropertyFilter("EQS_cargoState", "20"));
		List<TrayInfo> trayInfos = trayInfoDao.find(filters);
		
		//判断是否存在
		if(null != trayInfos && trayInfos.size() == 1){
			
			//查询装车单数据
			List<PropertyFilter> pfs = new ArrayList<PropertyFilter>();
			pfs.add(new PropertyFilter("EQS_loadingState", "4"));
			pfs.add(new PropertyFilter("EQS_trayId", trayCode));
			List<BisLoadingInfo> loadingInfos = loadingInfofDao.find(pfs);
			
			if(null != loadingInfos && loadingInfos.size() == 1){
				TrayInfo trayInfo = trayInfos.get(0);
				trayInfo.setCargoState("21");
				trayInfo.setUpdateTime(new Date());
				trayInfoDao.save(trayInfo);
				
				BisLoadingInfo loadingInfo = loadingInfos.get(0);
				loadingInfo.setLoadingState("5");
				loadingInfo.setBackTallyPerson(userName);
				loadingInfo.setBackTallyTime(new Date());
				loadingInfofDao.save(loadingInfo);
				
				result.setCode(0);
				result.setMsg("操作成功！");
			}else{
				result.setCode(1);
				result.setMsg("数据错误，该托盘装车单状态错误！");	
			}
		}else{
			result.setCode(1);
			result.setMsg("数据错误，该托盘状态错误！");
		}
		String res = JSON.toJSONString(result);
		return res;
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 回库上架查询
	 * @date 2016年3月28日 上午11:33:53 
	 * @param trayCode 托盘号
	 * @return
	 * @throws
	 */
	public String getBackStockUpByTrayCode(String trayCode){
		Result<TrayInfo> result = new Result<TrayInfo>();
		
		//查询库存 托盘数据
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_trayId", trayCode));
		filters.add(new PropertyFilter("EQS_cargoState", "21"));
		List<TrayInfo> trayInfos = trayInfoDao.find(filters);
		
		//判断是否存在
		if(null != trayInfos && trayInfos.size() == 1){
			TrayInfo trayInfo = trayInfos.get(0);
			result.setObj(trayInfo);
			result.setCode(0);
			result.setMsg("查询成功！");
		}else{
			result.setCode(1);
			result.setMsg("数据错误，该托盘状态错误！");
		}
		String res = JSON.toJSONString(result);
		return res;
	}
	
	/**
	 * @throws ParseException 
	 * 
	 * @author Connor.M
	 * @Description: 回库上架   
	 * @date 2016年3月28日 下午1:43:55 
	 * @param trayCode 托盘号
	 * @param cargoLocation 库位
	 * @param userName 用户名
	 * @return
	 * @throws
	 */
	@Transactional(readOnly = false)
	public String getBackStockUpConfirm(String trayCode, String cargoLocation, String userName) throws ParseException{
		Result<TrayInfo> result = new Result<TrayInfo>();
	
		//查询库存 托盘数据
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_trayId", trayCode));
		filters.add(new PropertyFilter("EQS_cargoState", "21"));
		List<TrayInfo> trayInfos = trayInfoDao.find(filters);
		
		//判断是否存在
		if(null != trayInfos && trayInfos.size() == 1){
			
			//查询装车单数据
			List<PropertyFilter> pfs = new ArrayList<PropertyFilter>();
			pfs.add(new PropertyFilter("EQS_loadingState", "5"));
			pfs.add(new PropertyFilter("EQS_trayId", trayCode));
			List<BisLoadingInfo> loadingInfos = loadingInfofDao.find(pfs);
			
			if(null != loadingInfos && loadingInfos.size() == 1){
				
				//获得托盘库存对象
				TrayInfo trayInfo = trayInfos.get(0);
				
				if(StringUtils.isNull(cargoLocation)){
					cargoLocation = "00-00-000";
				}
				
				String[] num = cargoLocation.split("-");//库位号截取 注：库位号为三段式如：A1-01-18
				//判断是否为三段式   若非三段式补足两位
				if(num.length < 3){
					num = ArrayUtils.addAll(num, "00", "00");
				}
				
				String buildingNum = StringUtils.lpadStringLeft(2, num[0]);
				buildingNum = buildingNum.substring(0, 1);
				
				//修改库存
				trayInfo.setBuildingNum(buildingNum);
				trayInfo.setFloorNum(num[0]);
				trayInfo.setRoomNum(num[1]);
				trayInfo.setAreaNum(StringUtils.lpadStringLeft(3, num[2]));//三位补足
				trayInfo.setStoreroomNum(num[0] + num[1]);
				trayInfo.setCargoLocation(cargoLocation);
				trayInfo.setCargoState("01");
				trayInfo.setIsTruck("0");
				trayInfo.setUpdateTime(new Date());
				trayInfoDao.save(trayInfo);

				//获得  装车单 对象
				BisLoadingInfo loadingInfo = loadingInfos.get(0);
				
				//修改装车单数据
				loadingInfo.setLoadingState("6");
				loadingInfo.setBackUpPerson(userName);
				loadingInfo.setBackUpTime(new Date());
//				trayInfo.setAreaNum(StringUtils.lpadStringLeft(3, num[2]));//三位补足
//				trayInfo.setStoreroomNum(num[0] + num[1]);
				loadingInfo.setCargoLocation(cargoLocation);
				loadingInfofDao.save(loadingInfo);
				//回库生成一次性出入库费 20170927   注释掉：此处不收取出入库费
			/**	BisAsn asnObj = asnService.get(trayInfo.getAsn());
				String res = asnService.crFeeForBack(asnObj,trayInfo,userName);
				result.setCode(0);
				if(res.equals("success")){
					result.setMsg("操作成功！");
				}else{
					result.setMsg("操作成功！但未找到出费用明细，未能生成入库及一次性降温费！");
				}**/
				BackHistoryInfo back =new BackHistoryInfo();
				BeanUtils.copyProperties(trayInfo, back);//复制
				back.setId(null);
				back.setBackPerson(userName);
				back.setBackTime(new Date());
				//20170927 回库记录表追加装车单号--------------------------------------------------------
				back.setLoadingNum(loadingInfo.getLoadingTruckNum());
				backHistoryInfoService.save(back);
			}else{
				result.setCode(1);
				result.setMsg("数据错误，该托盘装车单状态错误！");	
			}
		}else{
			result.setCode(1);
			result.setMsg("数据错误，该托盘状态错误！");
		}
		String res = JSON.toJSONString(result);
		return res;
	}
}
