package com.haiersoft.ccli.wms.service.tallying;

import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.base.dao.ClientDao;
import com.haiersoft.ccli.base.dao.SkuInfoDao;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.BaseSkuBaseInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.BigDecimalUtil;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.Result;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.wms.dao.*;
import com.haiersoft.ccli.wms.dao.tallying.LoadingManageDao;
import com.haiersoft.ccli.wms.entity.*;
import com.haiersoft.ccli.wms.entity.PreEntryInvtQuery.BisPreEntryInvtQuery;
import com.haiersoft.ccli.wms.service.OutStockService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 装车理货
 * @author 
 */
@Service
@Transactional(readOnly = true)
public class LoadingManageService extends BaseService<BisPreEntryInvtQuery, String> {
	@Autowired
	private OutStockService outStockService;
	@Autowired
	private LoadingManageDao loadingDao;
	@Autowired
	private TrayInfoDao trayInfoDao;
	@Autowired
	private LoadingOrderDao loadingOrderDao;
	@Autowired
	private LoadingInfoDao loadingInfofDao;
	@Autowired
	private LoadingOrderInfoDao loadingOrderInfoDao;
	@Autowired
	private OutStockDao outStockDao;
	@Autowired
	private AsnActionDao asnActionDao;
	@Autowired
	private SkuInfoDao skuInfoDao;
	@Autowired
	private ClientDao clientDao;

	@Override
	public HibernateDao<BisPreEntryInvtQuery, String> getEntityDao() {
		return loadingDao;
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
					tray.setActualStoreroomX("0");
					tray.setActualStoreroomZ("0");
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
}
