package com.haiersoft.ccli.remoting.hand.interior.service;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jws.WebService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.BigDecimalUtil;
import com.haiersoft.ccli.common.utils.Result;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.wms.dao.AsnActionDao;
import com.haiersoft.ccli.wms.dao.ScrapTrayDao;
import com.haiersoft.ccli.wms.dao.TrayInfoDao;
import com.haiersoft.ccli.wms.entity.AsnAction;
import com.haiersoft.ccli.wms.entity.BisScrapTray;
import com.haiersoft.ccli.wms.entity.TrayInfo;

/**
 * 
 * @author Connor.M
 * @ClassName: WarehouseInteriorWebService
 * @Description: 库内操作WebService
 * @date 2016年3月16日 上午9:59:11
 */
@WebService
@Service
public class WarehouseInteriorWebService {
	
	@Autowired
	private TrayInfoDao trayInfoDao;
	@Autowired
	private AsnActionDao asnActionDao;
	@Autowired
	private ScrapTrayDao scrapTrayDao;
	/**
	 * 
	 * @author Connor.M
	 * @Description: 根据托盘号  获得库存数据 
	 * @date 2016年3月16日 上午10:16:52 
	 * @param trayCode 托盘号
	 * @return
	 * @throws
	 */
	public String getStorageMessageByTrayCode(String trayCode){
		Result<TrayInfo> result = new Result<TrayInfo>();
		//查询库存 托盘数据
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_trayId", trayCode));
		filters.add(new PropertyFilter("EQS_cargoState", "01"));//上架 状态
		List<TrayInfo> trayInfos = trayInfoDao.find(filters);
		//判断是否存在
		if(null != trayInfos && trayInfos.size() == 1){
			TrayInfo trayInfo = trayInfos.get(0);
			result.setObj(trayInfo);
			result.setCode(0);
			result.setMsg("查询成功！");
		}else{
			result.setCode(1);
			result.setMsg("数据错误，该托盘数据状态有误！");
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * 
	 * @author gaozq
	 * @Description: 根据托盘号  获得托盘信息数据（所有状态）
	 * @date 2016年6月23日 上午9:36:52 
	 * @param trayCode 托盘号
	 * @return
	 * @throws
	 */
	public String getTrayInfo(String trayCode){
		Result<TrayInfo> result = new Result<TrayInfo>();
		//查询库存 托盘数据
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_trayId", trayCode));
		//filters.add(new PropertyFilter("EQS_cargoState", "01"));//上架 状态，注释掉此行查询所有状态的托盘
		List<TrayInfo> trayInfos = trayInfoDao.find(filters);
		//判断是否存在
		if(null != trayInfos && trayInfos.size() == 1){
			TrayInfo trayInfo = trayInfos.get(0);
			String ctnNum = trayInfo.getCtnNum();
			//获得托盘号对应箱数的库存件数
			List<Map<String,Object>> ctnList = trayInfoDao.getCtnList(ctnNum);
			result.setMap(ctnList.get(0));
			result.setObj(trayInfo);
			result.setCode(0);
			result.setMsg("查询成功！");
		}else{
			result.setCode(1);
			result.setMsg("该托盘信息不存在！");
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: 根据区位号  获得托盘信息数据（在库）
	 * @date 2016年7月7日 下午17:03
	 * @param cargoLocation 库位号
	 * @param pageNo  第几页
	 * @param pageSize 每页条数
	 * @return
	 * @throws
	 */
	public String getTrayInfoByCargo(String cargoLocation, String pageNo, String pageSize){
		Result<TrayInfo> result = new Result<TrayInfo>();
		if(StringUtils.isNull(pageNo) || !StringUtils.isNumeric(pageNo)){
			pageNo = "1";
		}
		if(StringUtils.isNull(pageSize) || !StringUtils.isNumeric(pageSize)){
			pageSize = "1";
		}
		//查询库存 托盘数据
		Page<TrayInfo> page = new Page<TrayInfo>(Integer.parseInt(pageNo), Integer.parseInt(pageSize), "id", Page.ASC);
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_cargoLocation", cargoLocation));
 		filters.add(new PropertyFilter("EQS_cargoState", "01"));//上架 状态，注释掉此行查询所有状态的托盘
 		List<TrayInfo> trayInfos = trayInfoDao.find(filters);
		//判断是否存在
		if(null != trayInfos && trayInfos.size() > 0){
			int count = trayInfos.size();
			page = trayInfoDao.findPage(page, filters);
			long residue = page.getTotalCount();
			Map<String, Object> parm = new HashMap<String, Object>();
			parm.put("count", count);//总数
			parm.put("residue", residue);//剩余数量
			result.setMap(parm);
			result.setPage(page);
			result.setCode(0);
			result.setMsg("查询成功！");
		}else{
			result.setCode(1);
			result.setMsg("该区位信息不存在！");
		}
		return JSON.toJSONString(result);
	}
	
	
	/**
	 * 
	 * @author pyl
	 * @Description: 根据ASN号 显示该ASN收货的品名及件数
	 * @date 2016年7月8日 下午10:03
	 * @param asn   ASN号
	 * @param pageNo  第几页
	 * @param pageSize 每页条数
	 * @return
	 * @throws
	 */
	public String getTrayInfoByASN(String asn, String pageNo, String pageSize){
		Result<TrayInfo> result = new Result<TrayInfo>();
		if(StringUtils.isNull(pageNo) || !StringUtils.isNumeric(pageNo)){
			pageNo = "1";
		}
		if(StringUtils.isNull(pageSize) || !StringUtils.isNumeric(pageSize)){
			pageSize = "1";
		}
		//查询库存 托盘数据
		Page<TrayInfo> page = new Page<TrayInfo>(Integer.parseInt(pageNo), Integer.parseInt(pageSize), "id", Page.ASC);
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_asn", asn));
 		filters.add(new PropertyFilter("EQS_cargoState", "01"));//上架 状态，注释掉此行查询所有状态的托盘
 		List<TrayInfo> trayInfos = trayInfoDao.find(filters);
		//判断是否存在
		if(null != trayInfos && trayInfos.size() > 0){
			int count = trayInfos.size();
			page = trayInfoDao.findPage(page, filters);
			long residue = page.getTotalCount();
			Map<String, Object> parm = new HashMap<String, Object>();
			parm.put("count", count);//总数
			parm.put("residue", residue);//剩余数量
			result.setMap(parm);
			result.setPage(page);
			result.setCode(0);
			result.setMsg("查询成功！");
		}else{
			result.setCode(1);
			result.setMsg("该ASN信息不存在！");
		}
		return JSON.toJSONString(result);
	}
	
	
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 移库确认 操作
	 * @date 2016年3月16日 上午10:28:06 
	 * @param trayCode 托盘号
	 * @param cargoLocation 库位号
	 * @param userName 用户名
	 * @return
	 * @throws
	 */
	@Transactional(readOnly = false)
	public String moveStorageConfirm(String trayCode, String cargoLocation, String userName){
		Result<TrayInfo> result = new Result<TrayInfo>();
		
		//查询库存 托盘数据
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_trayId", trayCode));
		filters.add(new PropertyFilter("EQS_cargoState", "01"));
		List<TrayInfo> trayInfos = trayInfoDao.find(filters);
		
		//判断是否存在
		if(null != trayInfos && trayInfos.size() == 1){
			TrayInfo trayInfo = trayInfos.get(0);
			//移库时更新货品存放信息 gzq 20160630 添加
			if(StringUtils.isNull(cargoLocation)){
				cargoLocation = "00-00-000";
			}
			String[] num = cargoLocation.split("-");//库位号截取 注：库位号为三段式如：A1-01-18
			//判断是否为三段式   若非三段式补足两位
			if(num.length < 3){
				num = ArrayUtils.addAll(num, "00", "00");
			}
			String buildingNum = StringUtils.lpadStringLeft(2, num[0]);
			//修改库存
			trayInfo.setBuildingNum(buildingNum.substring(0, 1));//楼
			trayInfo.setFloorNum(buildingNum.substring(1, 2));//层
			trayInfo.setRoomNum(num[1]);
			trayInfo.setAreaNum(StringUtils.lpadStringLeft(3, num[2]));//三位补足
			trayInfo.setStoreroomNum(num[0] + num[1]);//库房号
			//End 移库后更新库存信息 gzq 20160630 添加
			trayInfo.setCargoLocation(cargoLocation);
			trayInfo.setUpdateTime(new Date());
			trayInfoDao.save(trayInfo);
			result.setCode(0);
			result.setMsg("移库成功！");
		}else{
			result.setCode(1);
			result.setMsg("数据错误，该托盘数据状态有误！");
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 货损分页查询
	 * @date 2016年5月6日 下午6:44:21 
	 * @param scrapCode 货损单号
	 * @param pageNo 第几页
	 * @param pageSize 每页条数
	 * @return
	 * @throws
	 */
	public String showDamageStorage(String scrapCode, String pageNo, String pageSize){
		Result<BisScrapTray> result = new Result<BisScrapTray>();
		if(StringUtils.isNull(pageNo) || !StringUtils.isNumeric(pageNo)){
			pageNo = "1";
		}
		if(StringUtils.isNull(pageSize) || !StringUtils.isNumeric(pageSize)){
			pageSize = "1";
		}
		Page<BisScrapTray> page = new Page<BisScrapTray>(Integer.parseInt(pageNo), Integer.parseInt(pageSize), "trayCode", Page.ASC);
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_scrapCode", scrapCode));
		List<BisScrapTray> scrapTrays = scrapTrayDao.find(filters);
		if (null != scrapTrays && scrapTrays.size() > 0) {
			int count = scrapTrays.size();//总数量
			PropertyFilter filter2 = new PropertyFilter("EQS_scrapState", "0");
			filters.add(filter2);
			page = scrapTrayDao.findPage(page, filters);
			long residue = page.getTotalCount();//剩余的数量
			Map<String, Object> parm = new HashMap<String, Object>();
			parm.put("count", count);//总数
			parm.put("residue", residue);//剩余数量
			result.setMap(parm);
			result.setPage(page);
			result.setCode(0);
			result.setMsg("查询成功！");
		} else {
			result.setCode(1);
			result.setMsg("无此装车单的货物数据！");
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 货损确认 操作
	 * @date 2016年3月16日 上午10:46:12 
	 * @param scrapCode 货损单号
	 * @param trayCode 托盘号
	 * @param userName 用户名
	 * @return
	 * @throws
	 */
	@Transactional(readOnly = false)
	public String damageStorageConfirm(String scrapCode, String trayCode, String userName){
		Result<BisScrapTray> result = new Result<BisScrapTray>();
		//查询货损单
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_scrapCode", scrapCode));
		filters.add(new PropertyFilter("EQS_trayCode", trayCode));
		List<BisScrapTray> scrapTrays = scrapTrayDao.find(filters);
		if(null != scrapTrays && scrapTrays.size() > 0){
			//获得货损数据
			BisScrapTray scrapTray = scrapTrays.get(0);
			if(scrapTray.getScrapState().equals("0")){
				//获取托盘信息
				List<PropertyFilter> trayPfs = new ArrayList<PropertyFilter>();
				trayPfs.add(new PropertyFilter("EQS_trayId", trayCode));
				List<TrayInfo> trays = trayInfoDao.find(trayPfs);
				if(null != trays && trays.size() > 0){
					//获得托盘信息
					TrayInfo trayInfo = trays.get(0);
					if(trayInfo.getCargoState().equals("01")){
						List<PropertyFilter> actionPfs = new ArrayList<PropertyFilter>();
						if(trayInfo.getAsn()==null||"".equals(trayInfo.getAsn())){
							actionPfs.add(new PropertyFilter("NULLS_asn", "")); //ASN
						}else{
							actionPfs.add(new PropertyFilter("EQS_asn", trayInfo.getAsn()));
						}
						actionPfs.add(new PropertyFilter("EQS_sku", trayInfo.getSkuId()));
						actionPfs.add(new PropertyFilter("EQS_clientId", trayInfo.getStockIn()));
						actionPfs.add(new PropertyFilter("EQS_status", "1"));
						actionPfs.add(new PropertyFilter("NULLS_chargeEndDate", ""));
						actionPfs.add(new PropertyFilter("NULLS_outId", ""));
						List<AsnAction> asnActions = asnActionDao.find(actionPfs);
						
						if(null != asnActions && asnActions.size() > 0){
							//获得 asnAction 区间表 对象
							AsnAction asnAction = asnActions.get(0);
							//报废类型
							if("1".equals(scrapTray.getScrapType())){//普通报废
								//关联 货转单
								if(asnAction.getLinkTransferId() != null){
									//查询 是否存在  货转单
									List<PropertyFilter> pf = new ArrayList<PropertyFilter>();
									if(trayInfo.getAsn()==null||"".equals(trayInfo.getAsn())){
										pf.add(new PropertyFilter("NULLS_asn", "")); //ASN
									}else{
										pf.add(new PropertyFilter("EQS_asn", trayInfo.getAsn()));
									}
									pf.add(new PropertyFilter("EQS_sku", trayInfo.getSkuId()));
									pf.add(new PropertyFilter("EQS_linkTransferId", asnAction.getLinkTransferId()));
									pf.add(new PropertyFilter("NULLS_outId", ""));
									pf.add(new PropertyFilter("NULLS_outLinkId", ""));
									pf.add(new PropertyFilter("EQS_status", "1"));
									List<AsnAction> actions = asnActionDao.find(pf);
									
									if(null != actions && actions.size() > 0){
										//遍历 区间表 对象
										for(AsnAction action : actions){
											//判断货转
											int count = action.getNum() - trayInfo.getNowPiece();
											action.setNum(count);
											action.setNetWeight(BigDecimalUtil.sub(action.getNetWeight(), trayInfo.getNetWeight()));
											action.setGrossWeight(BigDecimalUtil.sub(action.getGrossWeight(), trayInfo.getGrossWeight()));
											asnActionDao.save(action);
//											asnActionLogService.saveLog(action, "8",count+trayInfo.getNowPiece(), 0-trayInfo.getNowPiece(), "普通货损时操作ASN区间表（对有货转单的ＡＳＮ区间操作）",userName);
										}
									}
								}else{//若没有货转单  则直接修改 此asnAction的区间表数据
									int num = asnAction.getNum() - trayInfo.getNowPiece();
									asnAction.setNum(num);
									asnAction.setNetWeight(BigDecimalUtil.sub(asnAction.getNetWeight(), trayInfo.getNetWeight()));
									asnAction.setGrossWeight(BigDecimalUtil.sub(asnAction.getGrossWeight(), trayInfo.getGrossWeight()));
									asnActionDao.save(asnAction);
//									asnActionLogService.saveLog(asnAction, "8", asnAction.getNum()+trayInfo.getNowPiece(), 0-trayInfo.getNowPiece(), "普通货损时操作ASN区间表（对无货转单的ＡＳＮ区间操作）",userName);
								}
								
							}else if("2".equals(scrapTray.getScrapType())){//库内分拣报损
								
								//关联 货转单
								if(asnAction.getLinkTransferId() != null){
									//查询 是否存在  货转单
									List<PropertyFilter> pf = new ArrayList<PropertyFilter>();
									if(trayInfo.getAsn()==null||"".equals(trayInfo.getAsn())){
										pf.add(new PropertyFilter("NULLS_asn", "")); //ASN
									}else{
										pf.add(new PropertyFilter("EQS_asn", trayInfo.getAsn()));
									}
									pf.add(new PropertyFilter("EQS_sku", trayInfo.getSkuId()));
									pf.add(new PropertyFilter("EQS_linkTransferId", asnAction.getLinkTransferId()));
									pf.add(new PropertyFilter("NULLS_outId", ""));
									pf.add(new PropertyFilter("NULLS_outLinkId", ""));
									pf.add(new PropertyFilter("EQS_status", "1"));
									pf.add(new PropertyFilter("NULLS_scrapCode", ""));
									List<AsnAction> actions = asnActionDao.find(pf);
									
									if(null != actions && actions.size() > 0){
										//遍历 区间表 对象
										for(AsnAction action : actions){
											//判断货转
											int count = action.getNum() - trayInfo.getNowPiece();
											action.setNum(count);
											action.setNetWeight(BigDecimalUtil.sub(action.getNetWeight(), trayInfo.getNetWeight()));
											action.setGrossWeight(BigDecimalUtil.sub(action.getGrossWeight(), trayInfo.getGrossWeight()));
											asnActionDao.save(action);
//											asnActionLogService.saveLog(action, "8",count+trayInfo.getNowPiece(), 0-trayInfo.getNowPiece(), "分拣货损时操作ASN区间表（对有货转单的ＡＳＮ区间操作，此条为原ASN区间对象）",userName);
											//复制  保存 asnAction区间表
											AsnAction action2  = new AsnAction();
											BeanUtils.copyProperties(action, action2);//复制
											
											action2.setId(null);
											action2.setChargeEndDate(scrapTray.getScrapDate());
											action2.setNum(trayInfo.getNowPiece());
											action2.setNetWeight(trayInfo.getNetWeight());
											action2.setGrossWeight(trayInfo.getGrossWeight());
											action2.setScrapCode(scrapCode);
											asnActionDao.save(action2);
//											asnActionLogService.saveLog(action2, "8",0, trayInfo.getNowPiece(), "分拣货损时操作ASN区间表（对有货转单的ＡＳＮ区间操作，此条为新生成的ASN区间对象）",userName);
										}
									}
								}else{//若没有货转单  则直接修改 此asnAction的区间表数据
									int num = asnAction.getNum() - trayInfo.getNowPiece();
									asnAction.setNum(num);
									asnAction.setNetWeight(BigDecimalUtil.sub(asnAction.getNetWeight(), trayInfo.getNetWeight()));
									asnAction.setGrossWeight(BigDecimalUtil.sub(asnAction.getGrossWeight(), trayInfo.getGrossWeight()));
									asnActionDao.save(asnAction);
//									asnActionLogService.saveLog(asnAction, "8",num+trayInfo.getNowPiece(), 0-trayInfo.getNowPiece(), "分拣货损时操作ASN区间表（对无货转单的ＡＳＮ区间操作，此条为原ASN区间对象）",userName);
									//复制  保存 asnAction区间表
									AsnAction asnAction2  = new AsnAction();
									BeanUtils.copyProperties(asnAction, asnAction2);//复制
									
									asnAction2.setId(null);
									asnAction2.setChargeEndDate(scrapTray.getScrapDate());
									asnAction2.setNum(trayInfo.getNowPiece());
									asnAction2.setNetWeight(trayInfo.getNetWeight());
									asnAction2.setGrossWeight(trayInfo.getGrossWeight());
									asnAction2.setScrapCode(scrapCode);
									asnActionDao.save(asnAction2);
//									asnActionLogService.saveLog(asnAction2, "8",0, trayInfo.getNowPiece(), "分拣货损时操作ASN区间表（对有货转单的ＡＳＮ区间操作，此条为新生成的ASN区间对象）",userName);
								}
							}
							
							//修改 托盘
							trayInfo.setCargoState("99");
							trayInfo.setNowPiece(0);
							trayInfo.setUpdateTime(new Date());
							trayInfoDao.save(trayInfo);
							
							scrapTray.setScrapState("1");
							scrapTray.setConfirmPserson(userName);
							scrapTray.setConfirmDate(new Date());
							scrapTrayDao.save(scrapTray);
							
							result.setCode(0);
							result.setMsg("操作成功！");
						}else{
							result.setCode(1);
							result.setMsg("该计费区间数据不存在，不可操作！");
						}
					}else{
						result.setCode(1);
						result.setMsg("该托盘信息状态不是已上架！");
					}
				}else{
					result.setCode(1);
					result.setMsg("该托盘信息不存在！");
				}
			}else{
				result.setCode(1);
				result.setMsg("该货损单的托盘已处理！");
			}
		}else{
			result.setCode(1);
			result.setMsg("该货损单不存在此托盘号！");
		}
		return JSON.toJSONString(result);
	}
	
	
	/**
	 * 
	 * @author pyl
	 * @Description: 修改托盘件数
	 * @date 2016年7月28日  下午17:03
	 * @param trayId 托盘号
	 * @param newNum 修改后件数   
	 * @param clientName 用户姓名
	 * @return
	 * @throws
	 */
	public String changeTrayNum(String trayId, Integer newNum, String clientName){
		//查询库存 托盘数据
		Result<TrayInfo> result = new Result<TrayInfo>();
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_trayId", trayId));
 		List<TrayInfo> trayInfos = trayInfoDao.find(filters);
		
		//判断是否存在
		if(null != trayInfos && trayInfos.size() == 1){
			TrayInfo obj = trayInfos.get(0);
			if("00".equals(obj.getCargoState())){
				Integer difference=newNum-obj.getNowPiece();
				obj.setNowPiece(newNum);
				obj.setOriginalPiece(newNum);
				Double net = obj.getNetSingle()*newNum;
				Double gross = obj.getGrossSingle()*newNum;
				trayInfoDao.changeTrayNum(trayId,newNum,net,gross);
				asnActionDao.changePiece(difference,obj);
				result.setCode(0);
				result.setMsg("修改成功！");
			}else{
				result.setCode(1);
				result.setMsg("该托盘不是已收货状态，无法修改件数！");
			}
		}else{
			result.setCode(1);
			result.setMsg("该托盘信息不存在！");
		}
		return JSON.toJSONString(result);
	}
}
