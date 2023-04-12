package com.haiersoft.ccli.remoting.hand.out.service;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jws.WebService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.base.dao.ClientDao;
import com.haiersoft.ccli.base.dao.SkuInfoDao;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.BaseSkuBaseInfo;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.BigDecimalUtil;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.Result;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.wms.dao.AsnActionDao;
import com.haiersoft.ccli.wms.dao.DismantleTrayDao;
import com.haiersoft.ccli.wms.dao.LoadingInfoDao;
import com.haiersoft.ccli.wms.dao.LoadingOrderDao;
import com.haiersoft.ccli.wms.dao.LoadingOrderInfoDao;
import com.haiersoft.ccli.wms.dao.OutStockDao;
import com.haiersoft.ccli.wms.dao.TrayInfoDao;
import com.haiersoft.ccli.wms.entity.AsnAction;
import com.haiersoft.ccli.wms.entity.BisDismantleTray;
import com.haiersoft.ccli.wms.entity.BisLoadingInfo;
import com.haiersoft.ccli.wms.entity.BisLoadingOrder;
import com.haiersoft.ccli.wms.entity.BisLoadingOrderInfo;
import com.haiersoft.ccli.wms.entity.BisOutStock;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import com.haiersoft.ccli.wms.service.OutStockService;

/**
 * 
 * @author Connor.M
 * @ClassName: OutboundWebService
 * @Description: 出库webService接口
 * @date 2016年3月9日 下午7:18:25
 */
@WebService
@Service
public class OutboundWebService {
	@Autowired
	private OutStockService outStockService;
	@Autowired
	private LoadingInfoDao loadingInfofDao;
	@Autowired
	private TrayInfoDao trayInfoDao;
	@Autowired
	private AsnActionDao asnActionDao;
	@Autowired
	private LoadingOrderInfoDao loadingOrderInfoDao;
	@Autowired
	private LoadingOrderDao loadingOrderDao;
	@Autowired
	private DismantleTrayDao dismantleTrayDao;
	@Autowired
	private OutStockDao outStockDao;
	@Autowired
	private ClientDao clientDao;
	@Autowired
	private SkuInfoDao skuInfoDao;
	/**
	 * 
	 * @author Connor.M
	 * @Description: 获取装车单 分拣出库数据
	 * @date 2016年3月11日 上午9:57:16
	 * @param carOrderCode 装车单号
	 * @param stockCode 库房号
	 * @param pageNo  第几页
	 * @param pageSize 每页条数
	 * @return
	 * @throws
	 */
	public String getOutSortingMessage(String carOrderCode, String stockCode, String pageNo, String pageSize) {
		Result<BisLoadingInfo> result = new Result<BisLoadingInfo>();
		
		if(StringUtils.isNull(pageNo) || !StringUtils.isNumeric(pageNo)){
			pageNo = "1";
		}
		
		if(StringUtils.isNull(pageSize) || !StringUtils.isNumeric(pageSize)){
			pageSize = "1";
		}
		
		//查询该装车单的  货物数据
		Page<BisLoadingInfo> page = new Page<BisLoadingInfo>(Integer.parseInt(pageNo), Integer.parseInt(pageSize), "id", Page.ASC);
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_loadingTruckNum", carOrderCode));
		filters.add(new PropertyFilter("EQS_storeroomNum", stockCode));
		filters.add(new PropertyFilter("INAS_loadingState", new String[] {"0", "1", "2", "4", "5", "6"}));//状态：0 已分配 1 已拣货 2 已装车 3已置换,4待回库,5回库理货,6已回库
		List<BisLoadingInfo> infos = loadingInfofDao.find(filters);
		
		if (null != infos && infos.size() > 0) {
			int count = infos.size(); 
			
			PropertyFilter filter2 = new PropertyFilter("EQS_loadingState", "0");
			filters.add(filter2);
			page = loadingInfofDao.findPage(page, filters);
			
			long residue = page.getTotalCount();
			
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
	 * @Description: 出库拣货确认
	 * @date 2016年3月11日 下午1:31:21
	 * @param carOrderCode 装车单
	 * @param trayCode 托牌号
	 * @param oldTrayCode 原托牌号
	 * @param userName 用户名
	 * @return
	 * @throws
	 */
	@Transactional(readOnly = false)
	public String outSortingConfirm(String carOrderCode, String trayCode, String oldTrayCode, String userName) {
		Date now = new Date();//统一时间
		Result<BisLoadingInfo> result = new Result<BisLoadingInfo>();
		//查询  该装车单内  是否存在  扫描的托盘号  数据
		List<PropertyFilter> loadfilters = new ArrayList<PropertyFilter>();
		loadfilters.add(new PropertyFilter("EQS_loadingTruckNum", carOrderCode));
		loadfilters.add(new PropertyFilter("EQS_trayId", trayCode));
		List<BisLoadingInfo> loadings = loadingInfofDao.find(loadfilters);//获得原托盘的信息
		//判断扫描的托盘号是否存在
		if (null != loadings && loadings.size() > 0) {
			//查询库存信息
			List<PropertyFilter> trayFilters = new ArrayList<PropertyFilter>();
			trayFilters.add(new PropertyFilter("EQS_trayId", trayCode));
			List<TrayInfo> trayInfoList = trayInfoDao.find(trayFilters);
			if (null != trayInfoList && trayInfoList.size() == 1) {
				TrayInfo trayInfo = trayInfoList.get(0);//获得库存对象
				if ("10".equals(trayInfo.getCargoState())) {
					BisLoadingInfo loadingInfo = loadings.get(0);
					if("0".equals(loadingInfo.getLoadingState())){
						//修改出车单
						loadingInfo.setLoadingState("1");
						loadingInfo.setTallyClerk(userName);
						loadingInfo.setTallyOpeTime(now);
						loadingInfofDao.save(loadingInfo);
						//修改库存状态
						trayInfo.setCargoState("11");//出库理货
						trayInfo.setOutTallyOperson(userName);
						trayInfo.setOutTallyTime(now);
						trayInfo.setUpdateTime(now);
						trayInfoDao.save(trayInfo);
						//修改出库订单
						BisLoadingOrder loadingOrder = loadingOrderDao.find(loadingInfo.getLoadingPlanNum());
						loadingOrder.setOrderState("3");
						loadingOrderDao.save(loadingOrder);
						result.setCode(0);
						result.setMsg("操作成功！");
					}else{
						result.setCode(1);
						result.setMsg("该托盘状态错误！");
					}
				} else {
					result.setCode(1);
					result.setMsg("操作失败！该托盘库存状态不是出库中，请处理！");
				}
			} else {
				result.setCode(1);
				result.setMsg("该托盘数据错误！");
			}
		} else {
			//置换逻辑Start
			//获得原托盘的信息
			List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
			filters.add(new PropertyFilter("EQS_loadingTruckNum", carOrderCode));
			filters.add(new PropertyFilter("EQS_trayId", oldTrayCode));
			List<BisLoadingInfo> loadingInfos = loadingInfofDao.find(filters);
			if (null != loadingInfos && loadingInfos.size() == 1) {
				BisLoadingInfo entity = loadingInfos.get(0);//获取对象
				//判断  装车单托盘状态  是否是 已分配
				if("0".equals(entity.getLoadingState())){
					List<PropertyFilter> filterss = new ArrayList<PropertyFilter>();
					filterss.add(new PropertyFilter("EQS_trayId", trayCode));//托盘号
					filterss.add(new PropertyFilter("EQS_stockIn", entity.getStockId()));//客户
					filterss.add(new PropertyFilter("EQS_skuId", entity.getSkuId()));//SKU
					filterss.add(new PropertyFilter("EQI_nowPiece", entity.getPiece().toString()));//数量
					filterss.add(new PropertyFilter("EQS_ctnNum", entity.getCtnNum()));//箱号
					filterss.add(new PropertyFilter("EQS_billNum", entity.getBillNum()));//提单号
					filterss.add(new PropertyFilter("EQS_cargoLocation", entity.getCargoLocation()));//库位号
					filterss.add(new PropertyFilter("EQS_cargoState", "01"));//上架状态
					List<TrayInfo> trayInfos = trayInfoDao.find(filterss);
					//存在满足以上条件的   可以置换
					if (null != trayInfos && trayInfos.size() > 0) {
						//查询  旧托盘  库存信息
						List<PropertyFilter> oldPy = new ArrayList<PropertyFilter>();
						oldPy.add(new PropertyFilter("EQS_trayId", oldTrayCode));
						oldPy.add(new PropertyFilter("EQS_cargoState", "10"));
						List<TrayInfo> oldTrayInfos = trayInfoDao.find(oldPy);
						if (null != oldTrayInfos && oldTrayInfos.size() == 1) {
							  TrayInfo oldTrayInfo = oldTrayInfos.get(0);//获得旧托盘 库存 对象
							  //修改  旧托盘  库存信息
							  oldTrayInfo.setCargoState("01");
							  oldTrayInfo.setUpdateTime(now);
							  oldTrayInfo.setIsTruck("0");
							  trayInfoDao.save(oldTrayInfo);
									
							  //修改  旧装车单 信息
							  entity.setLoadingState("3");
							  entity.setChangeTrayId(trayCode);
							  loadingInfofDao.save(entity);
									
							  //修改  新托盘  库存 状态
							  TrayInfo trayInfo = trayInfos.get(0);
							  trayInfo.setCargoState("11");//出库理货
							  trayInfo.setOutTallyOperson(userName);
							  trayInfo.setOutTallyTime(now);
							  trayInfo.setUpdateTime(now);
							  trayInfo.setIsTruck("1");
							  trayInfoDao.save(trayInfo);
									
									//添加 新装车单 信息
									BisLoadingInfo newLoadingInfo = new BisLoadingInfo();
									BeanUtils.copyProperties(entity, newLoadingInfo);//复制对象属性
									
									newLoadingInfo.setId(null);
									newLoadingInfo.setTrayId(trayCode);
									newLoadingInfo.setChangeTrayId(null);
									newLoadingInfo.setLoadingState("1");
									newLoadingInfo.setLoadingTruckNum(carOrderCode);
									newLoadingInfo.setNetWeight(trayInfo.getNetWeight());
									newLoadingInfo.setGrossWeight(trayInfo.getGrossWeight());
									newLoadingInfo.setLibraryManager(trayInfo.getEnterOperson());//装车单中库管员等于托盘的入库操作人员
									newLoadingInfo.setLibraryOpeTime(now);
									newLoadingInfo.setTallyClerk(userName);
									newLoadingInfo.setTallyOpeTime(now);
									loadingInfofDao.save(newLoadingInfo);
									
									result.setCode(0);
									result.setMsg("存在符合置换的数据，已置换成功！");
						} else {
							result.setCode(1);
							result.setMsg("原托盘在库存数据有误");
						}
					} else {
						result.setCode(1);
						result.setMsg("托盘扫描有误，并无可置换的托盘！");
					}
				}else{
					result.setCode(1);
					result.setMsg("该托盘已操作！");
				}
			}else{
				result.setCode(1);
				result.setMsg("该托盘在装车单数据错误！");
			}
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 根据托盘号查询  装车信息
	 * @date 2016年3月12日 下午1:37:06 
	 * @param trayCode 托盘号
	 * @return
	 * @throws
	 */
	public String getLoadingCarMessage(String trayCode){
		Result<BisLoadingInfo> result = new Result<BisLoadingInfo>();
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_loadingState", "1"));
		filters.add(new PropertyFilter("EQS_trayId", trayCode));
		List<BisLoadingInfo> loadingInfos = loadingInfofDao.find(filters);
		if(null != loadingInfos && loadingInfos.size() == 1){
			//获得装车单对象
			BisLoadingInfo loadingInfo = loadingInfos.get(0);
			result.setObj(loadingInfo);
			result.setCode(0);
			result.setMsg("查询成功！");
		}else{
			result.setCode(1);
			result.setMsg("数据错误，该托盘状态有误！");
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description:  装车出库  操作
	 * @date 2016年3月12日 下午1:51:25 
	 * @param carOrderCode 装车单
	 * @param trayCode 托盘号
	 * @param platform 月台号
	 * @param userName 用户名
	 * @param carNo 车牌号
	 * @return
	 * @throws
	 */
	@Transactional(readOnly = false)
	public String outStorageLoadingCar(String carOrderCode, String trayCode, String platform, String carNo, String userName){
		Date now = new Date();//统一时间
		Result<BisLoadingInfo> result = new Result<BisLoadingInfo>();
		List<PropertyFilter> loadfilters = new ArrayList<PropertyFilter>();
		loadfilters.add(new PropertyFilter("EQS_loadingTruckNum", carOrderCode));
		loadfilters.add(new PropertyFilter("EQS_loadingState", "1"));
		loadfilters.add(new PropertyFilter("EQS_trayId", trayCode));
		List<BisLoadingInfo> loadings = loadingInfofDao.find(loadfilters);//获得原托盘的信息
		if(null != loadings && loadings.size() == 1){
			//查询库存数据
			List<PropertyFilter> trayPfs = new ArrayList<PropertyFilter>();
			trayPfs.add(new PropertyFilter("EQS_trayId", trayCode));
			trayPfs.add(new PropertyFilter("EQS_cargoState", "11"));
			List<TrayInfo> trayInfos = trayInfoDao.find(trayPfs);
			//判断库存托盘是否存在
			if(null != trayInfos && trayInfos.size() == 1){
				//获得装车单对象
				BisLoadingInfo loadingInfo = loadings.get(0);
				//查询 订单明细表数据
				List<PropertyFilter> loadOrderInfoPfs = new ArrayList<PropertyFilter>();
				loadOrderInfoPfs.add(new PropertyFilter("EQS_loadingPlanNum", loadingInfo.getLoadingPlanNum()));//出库订单
				List<BisLoadingOrderInfo> loadingOrderInfos = loadingOrderInfoDao.find(loadOrderInfoPfs);
				if(null != loadingOrderInfos && loadingOrderInfos.size() > 0){
					//获得托盘对象
					TrayInfo tray = trayInfos.get(0);
					//修改 装车单数据
					loadingInfo.setCarNo(carNo);
					loadingInfo.setLoadingState("2");
					loadingInfo.setPlatformNum(platform);
					loadingInfo.setLoadingPerson(userName);
					//20170927注释掉：计件报表取装车单托盘数量计算，此处不单独记录数量
				/**	if(!StringUtils.isNull(loadingInfo.getRuleJobType())){
						List<BasePieceworkRule> ruleList=pieceworkRuleService.findby("jobType",loadingInfo.getRuleJobType());
						if(!ruleList.isEmpty()){
							for(BasePieceworkRule rule:ruleList){
								if(rule.getPersonType().equals("库管人员")){
									loadingInfo.setKgNumber(BigDecimalUtil.mul(rule.getRatio(), loadingInfo.getGrossWeight()));
								}else if(rule.getPersonType().equals("理货人员")){
									loadingInfo.setLhNumber(BigDecimalUtil.mul(rule.getRatio(), loadingInfo.getGrossWeight()));
								}else if(rule.getPersonType().equals("叉车人员")){
									loadingInfo.setCcNumber(BigDecimalUtil.mul(rule.getRatio(), loadingInfo.getGrossWeight()));
								}
							}
						}
					}**/
					//获得出库订单数据
					BisLoadingOrder loadingOrder = loadingOrderDao.find(loadingInfo.getLoadingPlanNum());
					loadingOrder.setCarNum(carNo);
					loadingOrderDao.save(loadingOrder);//修改出库订单车牌号
					if(loadingOrder.getPlanTime()!=null){
						loadingInfo.setLoadingTime(loadingOrder.getPlanTime());
					}else{
						loadingInfo.setLoadingTime(now);
					}
					loadingInfofDao.save(loadingInfo);
					
					//出库联系单回写
					BisOutStock outObj = outStockDao.find(loadingInfo.getOutLinkId());
					if(StringUtils.isNull(outObj.getCkTime())){
						outObj.setCkTime(DateUtils.formatDateTime2(now));
					}else{
						String[] ckList = outObj.getCkTime().split(",");
						int size=ckList.length;
						int a=1;
						for(int i=size-1;i>=0;i--){
							if(DateUtils.formatDateTime2(now).equals(ckList[i])){
								a=0;
								break;
							}
						}
						if(a==1){
							outObj.setCkTime(outObj.getCkTime()+","+DateUtils.formatDateTime2(now));
						}
					}
					outStockService.update(outObj);
					//修改 库存数据
					tray.setNowPiece(0);
					tray.setNetWeight(0.00);
					tray.setGrossWeight(0.00);
					tray.setCargoState("12");
					tray.setOutOperson(userName);
					tray.setOutStockTime(now);
					tray.setUpdateTime(now);
					trayInfoDao.save(tray);
					
					Date fastDate = now;
					
					for(BisLoadingOrderInfo info : loadingOrderInfos){
						//修改  订单明细表   装车时间
						if(info.getLoadingTiem() == null){
							info.setLoadingTiem(loadingInfo.getLoadingTime());
							loadingOrderInfoDao.save(info);
						}else{
							fastDate = info.getLoadingTiem();
						}
					}
					
					//判断是否是 最后一托盘
					this.judgeLastTrayOne(carOrderCode, loadingInfo.getOutLinkId(), fastDate);
					
					result.setCode(0);
					result.setMsg("操作成功！");
				}else{
					result.setCode(1);
					result.setMsg("无出库订单明细数据！请核实！");
				}
			}else{
				result.setCode(1);
				result.setMsg("数据错误，库存托盘数据有误！");
			}
		}else{
			result.setCode(1);
			result.setMsg("数据错误，该托盘状态有误！");
		}
		return JSON.toJSONString(result);
	}
	
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 回库  确认
	 * @date 2016年3月26日 下午2:50:11 
	 * @param trayCode 托盘号
	 * @param userName 用户名
	 * @return
	 * @throws
	 */
	@Transactional(readOnly = false)
	public String comeBackStockConfirm(String trayCode, String userName){
		Result<BisLoadingInfo> result = new Result<BisLoadingInfo>();

		//查询装车单表数据
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_loadingState", "1"));
		filters.add(new PropertyFilter("EQS_trayId", trayCode));
		List<BisLoadingInfo> loadingInfos = loadingInfofDao.find(filters);
		
		if(null != loadingInfos && loadingInfos.size() == 1){
			//获得装车单对象
			BisLoadingInfo loadingInfo = loadingInfos.get(0);
			BisLoadingOrder loadingOrder=loadingOrderDao.find(loadingInfo.getLoadingPlanNum());
			if(loadingOrder.getIfHasCleared().equals(0)){
				//查询托盘数据
				List<PropertyFilter> pfs = new ArrayList<PropertyFilter>();
				pfs.add(new PropertyFilter("EQS_trayId", trayCode));
				pfs.add(new PropertyFilter("EQS_cargoState", "11"));
				List<TrayInfo> trayInfos = trayInfoDao.find(pfs);
				
				if(trayInfos != null && trayInfos.size() == 1){
					
					
					//查询 订单明细表数据
					List<PropertyFilter> loadOrderInfoPfs = new ArrayList<PropertyFilter>();
					loadOrderInfoPfs.add(new PropertyFilter("EQS_loadingPlanNum", loadingInfo.getLoadingPlanNum()));//出库订单
					List<BisLoadingOrderInfo> loadingOrderInfos = loadingOrderInfoDao.find(loadOrderInfoPfs);
					
					if(null != loadingOrderInfos && loadingOrderInfos.size() > 0){
						loadingInfo.setLoadingState("5");
						loadingInfo.setBackStockPerson(userName);
						loadingInfo.setBackStockTime(new Date());
						loadingInfofDao.save(loadingInfo);
						
						//修改托盘库存表
						TrayInfo trayInfo = trayInfos.get(0);
						trayInfo.setCargoState("21");
						trayInfo.setUpdateTime(new Date());
	//					trayInfo.setIsTruck(isTruck)
						trayInfoDao.save(trayInfo);
	
						Date fastDate = new Date();
						for(BisLoadingOrderInfo info : loadingOrderInfos){
							if(info.getLoadingTiem() != null){
								fastDate = info.getLoadingTiem();
							}
						}
						
						//判断是都否是 最后一托盘
						this.judgeLastTrayOne(loadingInfo.getLoadingTruckNum(), loadingInfo.getOutLinkId(), fastDate);
						
						result.setCode(0);
						result.setMsg("操作成功！");
					}else{
						result.setCode(1);
						result.setMsg("无出库订单明细数据！请核实！");
					}
				}else{
					result.setCode(1);
					result.setMsg("库存无该对应状态的托盘数据！");
				}
			}else{
				result.setCode(1);
				result.setMsg("此货物已进行清库结算，无法回库！");
			}
		}else{
			result.setCode(1);
			result.setMsg("数据错误，未获得该托盘信息！");
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 判断是否是最后一托盘
	 * @date 2016年3月28日 上午10:47:00 
	 * @param loadingCode 装车单
	 * @throws
	 */
	public void judgeLastTrayOne(String loadingCode, String outLinkId, Date nowDate){
		//判断 装车单  是否是 最后一车
		List<PropertyFilter> loadPFs = new ArrayList<PropertyFilter>();
		loadPFs.add(new PropertyFilter("EQS_loadingTruckNum", loadingCode));
		//		loadPFs.add(new PropertyFilter("EQS_loadingTruckNum", "00000823"));
 		loadPFs.add(new PropertyFilter("INAS_loadingState", new String[] {"0","1"}));
		List<BisLoadingInfo> infos = loadingInfofDao.find(loadPFs);//获得原托盘的信息
		
		//判断是不是最后一个托盘    事物在上一个提交过则 ==0   不能==1
		if(null != infos && infos.size() == 0){
			
			//获得装车单对象
//			BisLoadingInfo loadingInfo = infos.get(0);
			
			//统计 装车单数据 数量，净重，毛重
			List<BisLoadingInfo> loadingInfos = loadingInfofDao.getSumLoadingCode(loadingCode);
			
			//修改出库订单
			String orderNum = loadingInfofDao.findBy("loadingTruckNum", loadingCode).get(0).getLoadingPlanNum();
			BisLoadingOrder loadingOrder =  loadingOrderDao.find(orderNum);
			loadingOrderDao.updateState(orderNum);
			
			//获得出库联系单数据
			BisOutStock outStock = outStockDao.find(outLinkId);
			
			BaseClientInfo clientInfo = clientDao.find(Integer.parseInt(outStock.getSettleOrgId()));
			//BaseClientInfo clientInfoR = clientDao.find(Integer.parseInt(outStock.getReceiverId()));
			//BaseClientInfo clientInfoS = clientDao.find(Integer.parseInt(outStock.getStockInId()));
			if(null != loadingInfos && loadingInfos.size() > 0){
				if(outStock.getIfBuyerPay().equals("0") || null==outStock.getStartStoreTiem()){//非卖货
					for(BisLoadingInfo info : loadingInfos){
						BaseSkuBaseInfo skuObj=skuInfoDao.find(info.getSkuId());
						//查询  asnAction区间表数据
						List<PropertyFilter> actionPfs = new ArrayList<PropertyFilter>();
						actionPfs.add(new PropertyFilter("EQS_status", "1"));//状态，正常
						if(info.getAsnId()==null||"".equals(info.getAsnId())){
							actionPfs.add(new PropertyFilter("NULLS_asn", "")); //ASN
						}else{
							actionPfs.add(new PropertyFilter("EQS_asn", info.getAsnId()));//ASN
						}
						actionPfs.add(new PropertyFilter("EQS_sku", info.getSkuId()));//sku
						actionPfs.add(new PropertyFilter("EQS_clientId", info.getStockId()));//客户
						actionPfs.add(new PropertyFilter("NULLS_chargeEndDate", ""));//时间  为 null
						List<AsnAction> asnActions = asnActionDao.find(actionPfs);
						//判断是否是清库结算过的
						List<PropertyFilter> actionQK = new ArrayList<PropertyFilter>();
						actionQK.add(new PropertyFilter("EQS_status", "1"));//状态，正常
						if(info.getAsnId()==null||"".equals(info.getAsnId())){
							actionQK.add(new PropertyFilter("NULLS_asn", "")); //ASN
						}else{
							actionQK.add(new PropertyFilter("EQS_asn", info.getAsnId()));//ASN
						}
						actionQK.add(new PropertyFilter("EQS_sku", info.getSkuId()));//sku
						actionQK.add(new PropertyFilter("EQS_clientId", info.getStockId()));//客户
						actionQK.add(new PropertyFilter("EQS_outId", outLinkId));//出库联系单ID
						actionQK.add(new PropertyFilter("EQS_outLinkId", orderNum));//出库订单ID
						List<AsnAction> asnActionsQK = asnActionDao.find(actionQK);
						if(null != asnActions && asnActions.size() > 0){
							if(asnActionsQK.isEmpty()){
								int numT=info.getPiece();//出库数量
								int numA=0;//此ASNACTION要减少的数量
								int sign=0;//循环结束标志
								//获得 asnAction 区间表 对象
	//							AsnAction asnAction = asnActions.get(0);
									for(AsnAction asnAction:asnActions){
										if(sign==0){
											if(asnAction.getNum()==0){
												continue;
											}
											//查看 货转单 是否存在
											if(asnAction.getNum()<numT){
												numA=asnAction.getNum();
												numT=numT-asnAction.getNum();
											}else{
												sign=1;
												numA=numT;
											}
											if(asnAction.getLinkTransferId() != null){
												//查询 是否存在  货转单
												List<PropertyFilter> pf = new ArrayList<PropertyFilter>();
												if(info.getAsnId()==null||"".equals(info.getAsnId())){
													pf.add(new PropertyFilter("NULLS_asn", "")); //ASN
												}else{
													pf.add(new PropertyFilter("EQS_asn", info.getAsnId()));
												}
												pf.add(new PropertyFilter("EQS_sku", info.getSkuId()));//sku
												pf.add(new PropertyFilter("EQS_linkTransferId", asnAction.getLinkTransferId()));
												pf.add(new PropertyFilter("NULLS_outId", ""));
												pf.add(new PropertyFilter("NULLS_outLinkId", ""));
												pf.add(new PropertyFilter("EQS_status", "1"));
												pf.add(new PropertyFilter("NULLS_scrapCode", ""));
												List<AsnAction> actions = asnActionDao.find(pf);
												
												if(null != actions && actions.size() > 0){
													
													for(AsnAction action : actions){
														//修改 主 AsnAction的数据
														action.setNum(action.getNum()-numA);
														action.setNetWeight(BigDecimalUtil.mul(skuObj.getNetSingle(), action.getNum()));
														action.setGrossWeight(BigDecimalUtil.mul(skuObj.getGrossSingle(), action.getNum()));
														asnActionDao.save(action);
			
														//复制 生成  子  AsnAction的数据
														AsnAction action2  = new AsnAction();
														BeanUtils.copyProperties(action, action2);//复制
														
														action2.setId(null);//主键
														//判断 计费结束日期
														if(null!=loadingOrder.getPlanTime()){															
															nowDate=loadingOrder.getPlanTime();
														}
														
														if(action2.getChargeEndDate() == null || nowDate.getTime() < action2.getChargeEndDate().getTime()){
															action2.setChargeEndDate(nowDate);//出库时间
														}
														
														action2.setNum(numA);
														action2.setNetWeight(BigDecimalUtil.mul(skuObj.getNetSingle(), action2.getNum()));
														action2.setGrossWeight(BigDecimalUtil.mul(skuObj.getGrossSingle(), action2.getNum()));
														action2.setTransferId(null);//货转单
														action2.setLinkTransferId(null);
														action2.setOutLinkId(info.getLoadingPlanNum());//出库订单
					
														//只有存货方客户的区间记录才更新出库单号、出库订单、结算客户，其他客户的货转记录保持单号是原来的状态，即原来的数据记录在入库联系单上，不记录在出库联系单上，出库联系单只记录当前收货方的存储费
														if(action2.getClientId().equals(outStock.getStockInId())){
															action2.setOutId(info.getOutLinkId());//出库联系单
															action2.setJfClientId(outStock.getSettleOrgId()); //结算单位ID
															action2.setClientDay(clientInfo.getCheckDay());//结算日
														}
														asnActionDao.save(action2);
													}
												}
											}else{
													//修改 主 AsnAction的数据
													asnAction.setNum(asnAction.getNum()-numA);
													asnAction.setNetWeight(BigDecimalUtil.mul(skuObj.getNetSingle(), asnAction.getNum()));
													asnAction.setGrossWeight(BigDecimalUtil.mul(skuObj.getGrossSingle(), asnAction.getNum()));
													asnActionDao.save(asnAction);
													//复制生成  子  AsnAction的数据
													AsnAction asnAction2  = new AsnAction();
													BeanUtils.copyProperties(asnAction, asnAction2);//复制
													
													asnAction2.setId(null);//主键
													//判断 计费结束日期         20170906 补充添加判断planTime的条件 yhn
													if(null!=loadingOrder.getPlanTime()){
														nowDate=loadingOrder.getPlanTime();
													}
													if(asnAction2.getChargeEndDate() == null || nowDate.getTime() < asnAction2.getChargeEndDate().getTime()){
														asnAction2.setChargeEndDate(nowDate);//出库时间
													}
													asnAction2.setNum(numA);
													asnAction2.setNetWeight(BigDecimalUtil.mul(skuObj.getNetSingle(), asnAction2.getNum()));
													asnAction2.setGrossWeight(BigDecimalUtil.mul(skuObj.getGrossSingle(), asnAction2.getNum()));
													asnAction2.setTransferId(null);//货转单
													asnAction2.setLinkTransferId(null);
													asnAction2.setOutLinkId(info.getLoadingPlanNum());//出库订单
													
													//只有存货方客户的区间记录才更新出库单号、出库订单、结算客户，其他客户的货转记录保持单号是原来的状态，即原来的数据记录在入库联系单上，不记录在出库联系单上，出库联系单只记录当前收货方的存储费
													if(asnAction2.getClientId().equals(outStock.getStockInId())){
														asnAction2.setOutId(info.getOutLinkId());//出库联系单
														asnAction2.setJfClientId(outStock.getSettleOrgId()); //结算单位ID
														asnAction2.setClientDay(clientInfo.getCheckDay());//结算日
													}
													asnActionDao.save(asnAction2);
											}
										}else{//end if sign
											break;
										}
									}//end for asnAction
							}
						}
					}//end for loadingInfos
				}//end for if 非卖货
				else{//卖货
					//卖货
					for(BisLoadingInfo info : loadingInfos){
						BaseSkuBaseInfo skuObj=skuInfoDao.find(info.getSkuId());
						//查询  asnAction区间表数据
						List<PropertyFilter> actionPfs = new ArrayList<PropertyFilter>();
						actionPfs.add(new PropertyFilter("EQS_status", "1"));//状态，正常
						if(info.getAsnId()==null||"".equals(info.getAsnId())){
							actionPfs.add(new PropertyFilter("NULLS_asn", "")); //ASN
						}else{
							actionPfs.add(new PropertyFilter("EQS_asn", info.getAsnId()));//ASN
						}
						actionPfs.add(new PropertyFilter("EQS_sku", info.getSkuId()));//sku
						actionPfs.add(new PropertyFilter("EQS_clientId", info.getStockId()));//客户
					    actionPfs.add(new PropertyFilter("EQS_outId", outStock.getOutLinkId()));//出库联系单 (20180226注掉)
						actionPfs.add(new PropertyFilter("NULLS_outLinkId", ""));//出库订单为空
						List<AsnAction> asnActions = asnActionDao.find(actionPfs);
						if(null != asnActions && asnActions.size() > 0){
								int numT=info.getPiece();//出库数量
								int numA=0;//此ASNACTION要减少的数量
								int sign=0;//循环结束标志
								//获得 asnAction 区间表 对象
									for(AsnAction asnAction:asnActions){
										if(sign==0){
											if(asnAction.getNum()==0){
												continue;
											}
											//查看 货转单 是否存在
											if(asnAction.getNum()<numT){
												numA=asnAction.getNum();
												numT=numT-asnAction.getNum();
											}else{
												sign=1;
												numA=numT;
											}
											if(asnAction.getLinkTransferId() != null){
												//查询 是否存在  货转单
												List<PropertyFilter> pf = new ArrayList<PropertyFilter>();
												if(info.getAsnId()==null||"".equals(info.getAsnId())){
													pf.add(new PropertyFilter("NULLS_asn", "")); //ASN
												}else{
													pf.add(new PropertyFilter("EQS_asn", info.getAsnId()));
												}
												pf.add(new PropertyFilter("EQS_sku", info.getSkuId()));//sku
												pf.add(new PropertyFilter("EQS_linkTransferId", asnAction.getLinkTransferId()));
												pf.add(new PropertyFilter("EQS_status", "1"));
												pf.add(new PropertyFilter("NULLS_scrapCode", ""));
												pf.add(new PropertyFilter("EQS_outId", outStock.getOutLinkId()));//客户
												pf.add(new PropertyFilter("NULLS_outLinkId", ""));//  为 null
												pf.add(new PropertyFilter("EQS_clientId", info.getStockId()));//客户
												List<AsnAction> actions = asnActionDao.find(pf);
												
												if(null != actions && actions.size() > 0){
													
													for(AsnAction action : actions){
														//修改 主 AsnAction的数据
														action.setNum(action.getNum()-numA);
														action.setNetWeight(BigDecimalUtil.mul(skuObj.getNetSingle(), action.getNum()));
														action.setGrossWeight(BigDecimalUtil.mul(skuObj.getGrossSingle(), action.getNum()));
														asnActionDao.save(action);
			
														//复制 生成  子  AsnAction的数据
														AsnAction action2  = new AsnAction();
														BeanUtils.copyProperties(action, action2);//复制
														
														action2.setId(null);//主键
														//判断 计费结束日期
														if(null!=loadingOrder.getPlanTime()){															
															nowDate=loadingOrder.getPlanTime();
														}
														
														if(action2.getChargeEndDate() == null || nowDate.getTime() < action2.getChargeEndDate().getTime()){
															action2.setChargeEndDate(nowDate);//出库时间
														}	
														action2.setNum(numA);
														action2.setNetWeight(BigDecimalUtil.mul(skuObj.getNetSingle(), action2.getNum()));
														action2.setGrossWeight(BigDecimalUtil.mul(skuObj.getGrossSingle(), action2.getNum()));
														action2.setTransferId(null);//货转单
														action2.setLinkTransferId(null);
														action2.setOutLinkId(info.getLoadingPlanNum());//出库订单
					
														asnActionDao.save(action2);
													}
												}
											}else{
												//修改 主 AsnAction的数据
												asnAction.setNum(asnAction.getNum()-numA);
												asnAction.setNetWeight(BigDecimalUtil.mul(skuObj.getNetSingle(), asnAction.getNum()));
												asnAction.setGrossWeight(BigDecimalUtil.mul(skuObj.getGrossSingle(), asnAction.getNum()));
												asnActionDao.save(asnAction);
												//复制生成  子  AsnAction的数据
												AsnAction asnAction2  = new AsnAction();
												BeanUtils.copyProperties(asnAction, asnAction2);//复制
												
												asnAction2.setId(null);//主键
												//判断 计费结束日期         20170906 补充添加判断planTime的条件 yhn
												if(null!=loadingOrder.getPlanTime()){
													nowDate=loadingOrder.getPlanTime();
												}
												if(asnAction2.getChargeEndDate() == null || nowDate.getTime() < asnAction2.getChargeEndDate().getTime()){
													asnAction2.setChargeEndDate(nowDate);//出库时间
												}
												asnAction2.setNum(numA);
												asnAction2.setNetWeight(BigDecimalUtil.mul(skuObj.getNetSingle(), asnAction2.getNum()));
												asnAction2.setGrossWeight(BigDecimalUtil.mul(skuObj.getGrossSingle(), asnAction2.getNum()));
												asnAction2.setTransferId(null);//货转单
												asnAction2.setLinkTransferId(null);
												asnAction2.setOutLinkId(info.getLoadingPlanNum());//出库订单
												
												asnActionDao.save(asnAction2);
 											}
										}else{//end if sign
											break;
										}
									}//end for asnAction
						}
					}//end for loadingInfos
				}
			}
		}
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 出库 装车  拆托确认 操作
	 * @date 2016年3月21日 下午2:20:10 
	 * @param oldTrayCode 老托盘号
	 * @param newTrayCode 新托盘号
	 * @param num 数量
	 * @param userName 用户名
	 * @return
	 * @throws
	 */
	@Transactional(readOnly = false)
	public String dismantleTrayConfirm(String oldTrayCode, String newTrayCode, String num, String userName){
		Result<TrayInfo> result = new Result<TrayInfo>();

		List<PropertyFilter> pfs = new ArrayList<PropertyFilter>();
		pfs.add(new PropertyFilter("EQS_loadingState", "1"));
		pfs.add(new PropertyFilter("EQS_trayId", oldTrayCode));
		List<BisLoadingInfo> loadingInfos = loadingInfofDao.find(pfs);
		
		if(null != loadingInfos && loadingInfos.size() > 0){
			
			BisLoadingInfo loadingInfo = loadingInfos.get(0);
			
			//判断两个托盘数量
			if(loadingInfo.getPiece() > Integer.parseInt(num)){
				
				//查询库存 托盘数据
				List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
				filters.add(new PropertyFilter("EQS_trayId", oldTrayCode));
				filters.add(new PropertyFilter("EQS_cargoState", "11"));
				List<TrayInfo> trayInfos = trayInfoDao.find(filters);
				
				//判断托盘状态
				if(null != trayInfos && trayInfos.size() == 1){
					//查询新托盘数据
					List<PropertyFilter> trayfilters = new ArrayList<PropertyFilter>();
					PropertyFilter filter = new PropertyFilter("EQS_trayId", newTrayCode);
					trayfilters.add(filter);
					List<TrayInfo> infos = trayInfoDao.find(trayfilters);
					
					//判断 新托盘号是否存在
					if(null != infos && infos.size() > 0){
						result.setCode(1);
						result.setMsg("新托盘号已存在，请重新扫描！");
					}else{
						Date now = new Date();//时间统一

						TrayInfo trayInfo = trayInfos.get(0);//获得老托盘对象
						TrayInfo info = new TrayInfo();//新托盘对象
						
						BeanUtils.copyProperties(trayInfo, info);//复制
						
						//修改  原   托盘库存数据
						trayInfo.setNowPiece(trayInfo.getNowPiece() - Integer.parseInt(num));
						trayInfo.setRemovePiece(Integer.parseInt(num)+trayInfo.getRemovePiece());
						trayInfo.setNetWeight(BigDecimalUtil.mul(trayInfo.getNetSingle() == null ? 0.0 : trayInfo.getNetSingle(), Double.parseDouble(trayInfo.getNowPiece().toString())));
						trayInfo.setGrossWeight(BigDecimalUtil.mul(trayInfo.getGrossSingle() == null ? 0.0 : trayInfo.getGrossSingle(), Double.parseDouble(trayInfo.getNowPiece().toString())));
						trayInfo.setUpdateTime(now);
						trayInfoDao.save(trayInfo);
						
						//添加  新   托盘库存数据
						info.setId(null);
						info.setTrayId(newTrayCode);
						info.setRemovePiece(0);
						info.setOriginalPiece(Integer.parseInt(num));
						info.setNowPiece(Integer.parseInt(num));
						info.setNetWeight(BigDecimalUtil.mul(info.getNetSingle() == null ? 0.0 : info.getNetSingle(), Double.parseDouble(num)));
						info.setGrossWeight(BigDecimalUtil.mul(info.getGrossSingle() == null ? 0.0 : info.getGrossSingle(), Double.parseDouble(num)));
						info.setUpdateTime(now);
						trayInfoDao.save(info);
						
						//修改  原   托盘装车单信息
						loadingInfo.setPiece(trayInfo.getNowPiece());
						loadingInfo.setNetWeight(trayInfo.getNetWeight());
						loadingInfo.setGrossWeight(trayInfo.getGrossWeight());
						loadingInfofDao.save(loadingInfo);
						
						//生成  新   托盘装车单信息
						BisLoadingInfo loadingInfo2 = new BisLoadingInfo();
						BeanUtils.copyProperties(loadingInfo, loadingInfo2);//复制
						
						loadingInfo2.setId(null);
						loadingInfo2.setTrayId(newTrayCode);
						loadingInfo2.setPiece(info.getNowPiece());
						loadingInfo2.setNetWeight(info.getNetWeight());
						loadingInfo2.setGrossWeight(info.getGrossWeight());
						loadingInfofDao.save(loadingInfo2);
						
						//保存拆托数据
						BisDismantleTray dismantleTray = new BisDismantleTray();
						dismantleTray.setOldTrayCode(trayInfo.getTrayId());
						dismantleTray.setNewTrayCode(newTrayCode);
						dismantleTray.setNum(Integer.parseInt(num));
						dismantleTray.setDismantleType("4");
						dismantleTray.setDismantleUser(userName);
						dismantleTray.setDismantleTime(now);
						dismantleTrayDao.save(dismantleTray);
						
						result.setCode(0);
						result.setMsg("操作成功！");
					}
				}else{
					result.setCode(1);
					result.setMsg("该托盘数据状态有误！");
				}
			}else{
				result.setCode(1);
				result.setMsg("新托盘数量不得大于原托盘数量，请重新输入！");
			}
		}else{
			result.setCode(1);
			result.setMsg("该托盘在装车单中状态错误！");
		}
		return JSON.toJSONString(result);
	}
	
}
