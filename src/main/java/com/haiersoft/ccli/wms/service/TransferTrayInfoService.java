package com.haiersoft.ccli.wms.service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.base.dao.ClientDao;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.service.SkuInfoService;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.BigDecimalUtil;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.dao.TransferDao;
import com.haiersoft.ccli.wms.dao.TransferTrayInfoDao;
import com.haiersoft.ccli.wms.entity.AsnAction;
import com.haiersoft.ccli.wms.entity.BisTransferStock;
import com.haiersoft.ccli.wms.entity.BisTransferStockTrayInfo;
import com.haiersoft.ccli.wms.entity.TrayInfo;

/**
 * @ClassName: TransferInfoService
 * @Description: 货转托盘明细Service
 * @date 2016年3月4日 下午4:29:46
 */
@Service
public class TransferTrayInfoService extends BaseService<BisTransferStockTrayInfo, String> {

	@Autowired
	private TransferTrayInfoDao transferTrayInfoDao;
	//@Autowired
	//private AsnActionLogService asnActionLogService;
	@Autowired
	private TrayInfoService trayInfoService;//库存明细
	//@Autowired
	//private DismantleTrayDao dismantleTrayDao;//库存明细
	@Autowired
	private TransferDao transferDao;
	@Autowired
	private AsnActionService asnActionService;
	@Autowired
	private ClientDao clientDao;
	@Autowired
	private SkuInfoService skuInfoService;
	//@Autowired
	//private TrayInfoDao trayInfoDao;
	
	@Override
    public HibernateDao<BisTransferStockTrayInfo, String> getEntityDao() {
	    return transferTrayInfoDao;
    }
	
	/**
	 * 根据货转单号获取货转托盘明细
	 * @param transferId
	 * @return
	 */
	public List<BisTransferStockTrayInfo> getTransferStockTrayInfoList(String transferId){
		List<BisTransferStockTrayInfo> list=null;
		if(transferId!=null && !"".equals(transferId)){
			list=transferTrayInfoDao.findBy("transferId", transferId);
		}
		return list;
	}
	
	/**
	 * 保存 货转托盘明细
	 * @param trayIds 库存明细id
	 * @param transferId 货转单id
	 * @return
	 */
	@SuppressWarnings("unused")
	public Map<String, Object> saveTransferStockTrayInfo(String trayIds, String transferId) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("endStr", "error");
		Date now =new Date();
		//数量统计map
		Map<String, Object> actionMapOld = new HashMap<String, Object>();//原货主新生成的  asnAction区间
		Map<String, Object> actionMapNew = new HashMap<String, Object>();//新货主生成的  asnAction区间
		
		if (trayIds != null && !"".equals(trayIds) && transferId != null && !"".equals(transferId)) {
			User user = UserUtil.getCurrentUser();
			
			String userName = "";
			Date nowDate = new Date();
			
			if (user != null) {
				userName = user.getName();
			}
			
			String ids = trayIds.substring(0, trayIds.length() - 1);
			String[] idList = ids.split(",");
			if (idList != null && idList.length > 0) {
				
				TrayInfo trayInfoObj = null;
				BisTransferStockTrayInfo saveObj = null;
				
				BisTransferStock transferStock = transferDao.find(transferId);
				
				//获得客户对象
				BaseClientInfo clientInfo = clientDao.find(Integer.parseInt(transferStock.getReceiverOrgId()));
				
				for (int i = 0; i < idList.length; i++) {
					if(idList[i]==null || "".equals(idList[i].trim())){
						continue;
					}
					//根据id获取库存明细
					trayInfoObj = trayInfoService.get(Integer.valueOf(idList[i]));
					//判断托盘ID属于托盘来源（货转或原始）
 					int numT=trayInfoObj.getNowPiece();
					int numN=0;
					if (trayInfoObj != null && trayInfoObj.getId() > 0) {
						//托盘上货品数量要大于0
						if (trayInfoObj.getNowPiece() > 0) {
							saveObj = new BisTransferStockTrayInfo();
							saveObj.setTransferId(transferId);
							saveObj.setTrayInfoId(trayInfoObj.getId());
							saveObj.setBillNum(trayInfoObj.getBillNum());
							saveObj.setCtnNum(trayInfoObj.getCtnNum());
							saveObj.setSku(trayInfoObj.getSkuId());
							saveObj.setStockNum(trayInfoObj.getTrayId());
							saveObj.setPiece(Double.valueOf(trayInfoObj.getNowPiece()));
							saveObj.setGrossWeight(trayInfoObj.getGrossWeight());
							saveObj.setNetWeight(trayInfoObj.getNetWeight());
							saveObj.setCargoName(trayInfoObj.getCargoName());
							saveObj.setTypeSize(skuInfoService.get(trayInfoObj.getSkuId()).getTypeSize()!=null?skuInfoService.get(trayInfoObj.getSkuId()).getTypeSize().toString():"");
							saveObj.setUntis("1");
							saveObj.setEnterState(trayInfoObj.getEnterState());
							saveObj.setCrTime(nowDate);
							saveObj.setCrUser(userName);
							this.save(saveObj);
							
							if("S00117090508553940".equals(trayInfoObj.getSkuId()))
							{
							  int oo=1;
							  oo=2;
							}
							
							//若仓储费开始日期   不为空   则进行   计费区间表逻辑
							if(true){
 //								null != transferStock.getStartStoreDate() && transferStock.getIsBuyFee().equals("1")
								//添加  asnAction 计费区间表  逻辑
								List<PropertyFilter> actionPfs = new ArrayList<PropertyFilter>();
								actionPfs.add(new PropertyFilter("EQS_status", "1"));//状态，正常
								if(trayInfoObj.getAsn()==null||"".equals(trayInfoObj.getAsn())){
									actionPfs.add(new PropertyFilter("NULLS_asn", "")); //ASN
								}else{
									actionPfs.add(new PropertyFilter("EQS_asn", trayInfoObj.getAsn()));//ASN
								}
								actionPfs.add(new PropertyFilter("EQS_sku", trayInfoObj.getSkuId()));//sku
								actionPfs.add(new PropertyFilter("EQS_clientId", transferStock.getStockInId()));//客户
								actionPfs.add(new PropertyFilter("NULLS_chargeEndDate", ""));//时间  为 null
								actionPfs.add(new PropertyFilter("NULLS_outId", ""));//出库联系单  为 null
								List<AsnAction> asnActions = asnActionService.search(actionPfs);
								
								if(null != asnActions && asnActions.size() > 0){
//									if(asnActions.size()>1){
//										System.out.print("此处应有本子!（划掉）此处不应查出多条ASN区间表记录"+asnActions.size()+","+trayInfoObj.getAsn()+","+trayInfoObj.getSkuId());
//									}
									//获得 asnAction 区间表 对象
									int endSign=0;
									for(AsnAction asnAction : asnActions){
										if(endSign==1){
											break;
										}
										if(asnAction.getNum()==0){
											continue;
										}else if(asnAction.getNum()<numT){
											endSign=0;
											numN=asnAction.getNum();
											numT=numT-numN;
											System.out.print("此处不应查出出现托盘数量大于区间表记录数量的情况"+","+asnAction.getAsn()+","+asnAction.getSku());
										}else{
											numN=asnAction.getNum();
											endSign=1;
										}
									//查看 货转单 是否存在
									if(asnAction.getLinkTransferId() != null){
										
										//查询 是否存在  货转单
										List<PropertyFilter> pf = new ArrayList<PropertyFilter>();
										if(trayInfoObj.getAsn()==null||"".equals(trayInfoObj.getAsn())){
											pf.add(new PropertyFilter("NULLS_asn", "")); //ASN
										}else{
											pf.add(new PropertyFilter("EQS_asn", trayInfoObj.getAsn()));
										}
										pf.add(new PropertyFilter("EQS_sku", trayInfoObj.getSkuId()));//ASN
										pf.add(new PropertyFilter("EQS_linkTransferId", asnAction.getLinkTransferId()));
										pf.add(new PropertyFilter("EQS_status", "1"));
										pf.add(new PropertyFilter("NULLS_scrapCode", ""));
										pf.add(new PropertyFilter("NULLS_outId", ""));//出库联系单  为 null
										pf.add(new PropertyFilter("NULLS_outLinkId", ""));
										List<AsnAction> actions = asnActionService.search(pf);
										
										if(null != actions && actions.size() > 0){
											Boolean count = true;
											for(AsnAction action : actions){
												
												
												//原货主  修改
												if(endSign==0){
													action.setNum(0);
												}else{
													Integer num = numN - numT;
													action.setNum(num);
													 
												}
												action.setNetWeight(BigDecimalUtil.mul(trayInfoObj.getNetSingle() == null ? 0D : trayInfoObj.getNetSingle(), action.getNum()));
												action.setGrossWeight(BigDecimalUtil.mul(trayInfoObj.getGrossSingle() == null ? 0D : trayInfoObj.getGrossSingle(), action.getNum()));
												asnActionService.update(action);
												//原客户新拆托
												AsnAction newAsnAction = new AsnAction();
												BeanUtils.copyProperties(action, newAsnAction);//复制
												
												//定制  唯一 KEY
												String oldkey = trayInfoObj.getAsn() + "," + trayInfoObj.getSkuId() + "," + asnAction.getLinkTransferId() + "," + newAsnAction.getClientId()+","+newAsnAction.getChargeStaDate().toString();
												if(actionMapOld.get(oldkey) == null){
													if(endSign==0){
														newAsnAction.setNum(numN);
													}else{
														newAsnAction.setNum(numT);
													}
													newAsnAction.setNetWeight(BigDecimalUtil.mul(trayInfoObj.getNetSingle() == null ? 0D : trayInfoObj.getNetSingle(), newAsnAction.getNum()));
													newAsnAction.setGrossWeight(BigDecimalUtil.mul(trayInfoObj.getGrossSingle() == null ? 0D : trayInfoObj.getGrossSingle(), newAsnAction.getNum()));
													if(null==newAsnAction.getChargeEndDate()){
														newAsnAction.setChargeEndDate(DateUtils.addDay((transferStock.getIsBuyFee().equals("1")?transferStock.getStartStoreDate():now), -1));
													}
													newAsnAction.setTransferId(null);//货转单ID
													newAsnAction.setLinkTransferId(transferId);//关联货转单ID
													newAsnAction.setTransferSign(transferId);//货转存储费标记
													newAsnAction.setId(null);
												}else{
													newAsnAction = (AsnAction) actionMapOld.get(oldkey);
													if(endSign==0){
														newAsnAction.setNum(newAsnAction.getNum() + numN);
													}else{
														newAsnAction.setNum(newAsnAction.getNum() + numT);
													}
													Double net = BigDecimalUtil.mul(trayInfoObj.getNetSingle() == null ? 0D : trayInfoObj.getNetSingle(), newAsnAction.getNum());
													Double gross =  BigDecimalUtil.mul(trayInfoObj.getGrossSingle() == null ? 0D : trayInfoObj.getGrossSingle(), newAsnAction.getNum());
													newAsnAction.setNetWeight(net);
													newAsnAction.setGrossWeight(gross);
												}
												//将新货主对象  存放 Map中
												actionMapOld.put(oldkey, newAsnAction);
												
												
												//新客户新拆托
												AsnAction clientAsnAction = new AsnAction();
												BeanUtils.copyProperties(action, clientAsnAction);//复制
												
												//如果统计map == null 则录入 一个新货主的数据原   ;取出数据数据原进行累加
												//定制  唯一 KEY
												if(null==action.getChargeEndDate()){
													String newkey = trayInfoObj.getAsn() + "," + trayInfoObj.getSkuId() + "," + asnAction.getLinkTransferId() + "," + transferStock.getReceiver()+","+clientAsnAction.getChargeStaDate().toString();
													if(actionMapNew.get(newkey) == null){
														clientAsnAction.setClientId(transferStock.getReceiver());
														if(null!=transferStock.getFeeId()){
															clientAsnAction.setFeePlanId(transferStock.getFeeId());
														}
														if(endSign==0){
															clientAsnAction.setNum(numN);
														}else{
															clientAsnAction.setNum(numT);
														}
														clientAsnAction.setNetWeight(BigDecimalUtil.mul(trayInfoObj.getNetSingle() == null ? 0D : trayInfoObj.getNetSingle(), clientAsnAction.getNum()));
														clientAsnAction.setGrossWeight(BigDecimalUtil.mul(trayInfoObj.getGrossSingle() == null ? 0D : trayInfoObj.getGrossSingle(), clientAsnAction.getNum()));
														clientAsnAction.setChargeStaDate(transferStock.getIsBuyFee().equals("1")?transferStock.getStartStoreDate():now);
														clientAsnAction.setChargeEndDate(null);
														clientAsnAction.setTransferId(transferId);//货转单ID
														clientAsnAction.setLinkTransferId(transferId);//关联货转单ID
														clientAsnAction.setTransferSign(transferId);//货转存储费标记
														clientAsnAction.setJfClientId(transferStock.getReceiverOrgId());//结算客户
														clientAsnAction.setClientDay(clientInfo.getCheckDay());//客户结账日期
	  													count = false;
	  													clientAsnAction.setId(null);
	  													//将新货主对象  存放 actionMap中
	  													actionMapNew.put(newkey, clientAsnAction);
													}else{
														if (count){
															clientAsnAction = (AsnAction) actionMapNew.get(newkey);
															if(endSign==0){
																clientAsnAction.setNum(clientAsnAction.getNum() + numN);
															}else{
																clientAsnAction.setNum(clientAsnAction.getNum() + numT);
															}
															Double net = BigDecimalUtil.mul(trayInfoObj.getNetSingle() == null ? 0D : trayInfoObj.getNetSingle(), clientAsnAction.getNum());
															Double gross = BigDecimalUtil.mul(trayInfoObj.getGrossSingle() == null ? 0D : trayInfoObj.getGrossSingle(), clientAsnAction.getNum());
															clientAsnAction.setNetWeight(net);
															clientAsnAction.setGrossWeight(gross);
															
															count = false;
															//将新货主对象  存放 actionMap中
															actionMapNew.put(newkey, clientAsnAction);
														}
													}
												}
											}//end for asnaction
										}	
									}else{
										String oldkey = trayInfoObj.getAsn() + "," + trayInfoObj.getSkuId() + "," + asnAction.getClientId()+","+asnAction.getChargeStaDate().toString();
										//原客户新拆托
										AsnAction newAsnAction = new AsnAction();
										//新客户新拆托
										AsnAction clientAsnAction = new AsnAction();
										
										BeanUtils.copyProperties(asnAction, newAsnAction);//复制
										BeanUtils.copyProperties(asnAction, clientAsnAction);//复制
										
										//原货主  修改
										if(endSign==0){
											asnAction.setNum(0);
										}else{
											Integer num = numN - numT;
											asnAction.setNum(num);
										}
										asnAction.setNetWeight(BigDecimalUtil.mul(trayInfoObj.getNetSingle() == null ? 0D : trayInfoObj.getNetSingle(), asnAction.getNum()));
										asnAction.setGrossWeight(BigDecimalUtil.mul(trayInfoObj.getGrossSingle() == null ? 0D : trayInfoObj.getGrossSingle(), asnAction.getNum()));
										asnActionService.update(asnAction);
										if(actionMapOld.get(oldkey) == null){
										//原货主  新生成
//											newAsnAction.setId(null);
											if(endSign==0){
												newAsnAction.setNum(numN);
											}else{
												newAsnAction.setNum(numT);
											}
											newAsnAction.setNetWeight(BigDecimalUtil.mul(trayInfoObj.getNetSingle() == null ? 0D : trayInfoObj.getNetSingle(), newAsnAction.getNum()));
											newAsnAction.setGrossWeight(BigDecimalUtil.mul(trayInfoObj.getGrossSingle() == null ? 0D : trayInfoObj.getGrossSingle(), newAsnAction.getNum()));
											newAsnAction.setChargeEndDate(DateUtils.addDay((transferStock.getIsBuyFee().equals("1")?transferStock.getStartStoreDate():now), -1));
											newAsnAction.setTransferId(null);//货转单ID
											newAsnAction.setLinkTransferId(transferId);//关联货转单ID
											newAsnAction.setTransferSign(transferId);//货转存储费标记
											newAsnAction.setId(null);
										}else{
											newAsnAction = (AsnAction) actionMapOld.get(oldkey);
											if(endSign==0){
												newAsnAction.setNum(newAsnAction.getNum() + numN);
											}else{
												newAsnAction.setNum(newAsnAction.getNum() + numT);
											}
											Double net =  BigDecimalUtil.mul(trayInfoObj.getNetSingle() == null ? 0D : trayInfoObj.getNetSingle(), newAsnAction.getNum());
											Double gross = BigDecimalUtil.mul(trayInfoObj.getGrossSingle() == null ? 0D : trayInfoObj.getGrossSingle(), newAsnAction.getNum());
											newAsnAction.setNetWeight(net);
											newAsnAction.setGrossWeight(gross);
										}
										actionMapOld.put(oldkey, newAsnAction);
										
										//新货主  新生成
										String newkey = trayInfoObj.getAsn() + "," + trayInfoObj.getSkuId() + "," + transferStock.getReceiver()+","+asnAction.getChargeStaDate();
										if(actionMapNew.get(newkey) == null){
//											clientAsnAction.setId(null);
											clientAsnAction.setClientId(transferStock.getReceiver());
											if(null!=transferStock.getFeeId()){
												clientAsnAction.setFeePlanId(transferStock.getFeeId());
											}
											if(endSign==0){
												clientAsnAction.setNum(numN);
											}else{
												clientAsnAction.setNum(numT);
											}
											clientAsnAction.setNetWeight(BigDecimalUtil.mul(trayInfoObj.getNetSingle() == null ? 0D : trayInfoObj.getNetSingle(), clientAsnAction.getNum()));
											clientAsnAction.setGrossWeight(BigDecimalUtil.mul(trayInfoObj.getGrossSingle() == null ? 0D : trayInfoObj.getGrossSingle(), clientAsnAction.getNum()));
											clientAsnAction.setChargeStaDate(transferStock.getIsBuyFee().equals("1")?transferStock.getStartStoreDate():now);
											clientAsnAction.setChargeEndDate(null);
											clientAsnAction.setTransferId(transferId);//货转单ID
											clientAsnAction.setLinkTransferId(transferId);//关联货转单ID
											clientAsnAction.setTransferSign(transferId);//货转存储费标记
											clientAsnAction.setJfClientId(transferStock.getReceiverOrgId());//结算客户
											clientAsnAction.setClientDay(clientInfo.getCheckDay());//客户结账日期
											clientAsnAction.setId(null);
										}else{
											clientAsnAction = (AsnAction) actionMapNew.get(newkey);
											if(endSign==0){
												clientAsnAction.setNum(clientAsnAction.getNum() + numN);
											}else{
												clientAsnAction.setNum(clientAsnAction.getNum() + numT);
											}
											Double net =  BigDecimalUtil.mul(trayInfoObj.getNetSingle() == null ? 0D : trayInfoObj.getNetSingle(), clientAsnAction.getNum());
											Double gross = BigDecimalUtil.mul(trayInfoObj.getGrossSingle() == null ? 0D : trayInfoObj.getGrossSingle(), clientAsnAction.getNum());
											clientAsnAction.setNetWeight(net);
											clientAsnAction.setGrossWeight(gross);
										}
										//将新货主对象  存放 actionMap中
										actionMapNew.put(newkey, clientAsnAction);
									}
								}//end for try
								}
							}
							retMap.put("endStr", "success");
						}
					}
				}
				
				//将Map  进行操作  将存在的进行累加
				if(actionMapNew.size() > 0){
					for(Map.Entry<String, Object> entry : actionMapNew.entrySet()){
						AsnAction asnActionNew = new AsnAction();
						asnActionNew = (AsnAction) entry.getValue();
//						List<PropertyFilter> actionPfs = new ArrayList<PropertyFilter>();
//						if(asnAction.getAsn()==null||"".equals(asnAction.getAsn())){
//							actionPfs.add(new PropertyFilter("NULLS_asn", "")); //ASN
//						}else{
//							actionPfs.add(new PropertyFilter("EQS_asn", asnAction.getAsn()));
//						}
//						actionPfs.add(new PropertyFilter("EQS_sku", asnAction.getSku()));
//						actionPfs.add(new PropertyFilter("EQS_clientId", asnAction.getClientId()));
//						actionPfs.add(new PropertyFilter("EQS_linkTransferId", asnAction.getLinkTransferId()!=null?asnAction.getLinkTransferId():""));
//						actionPfs.add(new PropertyFilter("EQS_status", "1"));
//						List<AsnAction> actions = asnActionService.search(actionPfs);
//						
//						AsnAction inAction = new AsnAction();//将改变的asnAction
//						
//						if(null != actions && actions.size() > 0 ){
//							inAction = actions.get(0);
//							if(inAction.getTransferId()!=asnAction.getTransferId() || inAction.getChargeEndDate()!=asnAction.getChargeEndDate()){
//								inAction = asnAction;
//								inAction.setId(null);
//							}else{
//								inAction.setNum((inAction.getNum()!=null?inAction.getNum():0) + (asnAction.getNum()!=null?asnAction.getNum():0));
//								inAction.setNetWeight(BigDecimalUtil.add(inAction.getNetWeight(), asnAction.getNetWeight()));
//								inAction.setGrossWeight(BigDecimalUtil.mul(inAction.getGrossWeight(), asnAction.getGrossWeight()));
//							}
//						}else{
//							inAction = asnAction;
//							inAction.setId(null);
//						}
						asnActionService.merge(asnActionNew);
			//			asnActionLogService.saveLog(inAction, "6", 0, 0, "货转时原货主货转的货物生成的ASN区间表记录");
					}
				}
				
				if(actionMapOld.size() > 0){
					for(Map.Entry<String, Object> entry : actionMapOld.entrySet()){
						AsnAction asnActionOld = new AsnAction();
						asnActionOld = (AsnAction) entry.getValue();
						
//						List<PropertyFilter> actionPfs = new ArrayList<PropertyFilter>();
//						if(asnAction.getAsn()==null||"".equals(asnAction.getAsn())){
//							actionPfs.add(new PropertyFilter("NULLS_asn", "")); //ASN
//						}else{
//							actionPfs.add(new PropertyFilter("EQS_asn", asnAction.getAsn()));
//						}
//						actionPfs.add(new PropertyFilter("EQS_sku", asnAction.getSku()));
//						actionPfs.add(new PropertyFilter("EQS_clientId", asnAction.getClientId()));
//						actionPfs.add(new PropertyFilter("EQS_linkTransferId", asnAction.getLinkTransferId()!=null?asnAction.getLinkTransferId():""));
//						actionPfs.add(new PropertyFilter("EQS_status", "1"));
//						List<AsnAction> actions = asnActionService.search(actionPfs);
//						
//						AsnAction inAction = new AsnAction();//将改变的asnAction
//						
//						if(null != actions && actions.size() > 0){
//							inAction = actions.get(0);
//							if(inAction.getTransferId()!=asnAction.getTransferId() || inAction.getChargeEndDate()!=asnAction.getChargeEndDate()){
//								inAction = asnAction;
//								inAction.setId(null);
//							}else{
//								inAction.setNum( (inAction.getNum()!=null?inAction.getNum():0) + (asnAction.getNum()!=null?asnAction.getNum():0));
//								inAction.setNetWeight(BigDecimalUtil.add(inAction.getNetWeight(), asnAction.getNetWeight()));
//								inAction.setGrossWeight(BigDecimalUtil.add(inAction.getGrossWeight(), asnAction.getGrossWeight()));
//							}
//						}else{
//							inAction = asnAction;
//							inAction.setId(null);
//						}
						asnActionService.merge(asnActionOld);
					}
				}
				
				if(transferStock!=null){
					// 执行库存客户转换
					trayInfoService.UsrTrayList(trayIds,transferStock.getReceiver(),transferStock.getReceiverName());
				}
				
			}
		}
		return retMap;
	}

	
	//取消托盘货转时删除对应的asn action记录
	public void delAsnAction(String transferId,String oldId) {
		//查询 是否存在  货转单
//		Map<String,Object> reMaps=new HashMap<String,Object>();
		List<PropertyFilter> pf = new ArrayList<PropertyFilter>();
		pf.add(new PropertyFilter("EQS_linkTransferId", transferId));
		pf.add(new PropertyFilter("EQS_status", "1"));
		List<AsnAction> actions = asnActionService.search(pf);
		if(null!=actions && actions.size()>0){
			for(AsnAction obj:actions){
				if(!obj.getClientId().equals(oldId) && obj.getChargeEndDate()!=null){
					asnActionService.delete(obj);
//					if(obj.getTransferId()==null){
						List<PropertyFilter> actionRe = new ArrayList<PropertyFilter>();
						List<PropertyFilter> actionOri = new ArrayList<PropertyFilter>();
						actionRe.add(new PropertyFilter("EQS_status", "1"));//状态，正常
						if(obj.getAsn()==null||"".equals(obj.getAsn())){
							actionRe.add(new PropertyFilter("NULLS_asn", "")); //ASN
						}else{
							actionRe.add(new PropertyFilter("EQS_asn", obj.getAsn()));//ASN
						}
						actionRe.add(new PropertyFilter("EQS_sku", obj.getSku()));//sku
						actionRe.add(new PropertyFilter("EQS_clientId", obj.getClientId()));//客户ID
						actionRe.add(new PropertyFilter("EQD_chargeStaDate", obj.getChargeStaDate()));//计费开始日期
						actionRe.add(new PropertyFilter("EQD_chargeEndDate", obj.getChargeEndDate()));//计费结束日期
						List<AsnAction> asnActionRes = asnActionService.search(actionRe);
						if(null != asnActionRes && asnActionRes.size() > 0){
							Collections.sort(asnActionRes, new Comparator<AsnAction>(){
					 			@Override  
					            public int compare(AsnAction b1, AsnAction b2) {  
					                return b2.getId().compareTo(b1.getId());  
					            }  

					 		});
							AsnAction reAsn = new AsnAction();
							reAsn=asnActionRes.get(0);
							reAsn.setNum(reAsn.getNum()+obj.getNum());
							reAsn.setNetWeight(reAsn.getNetWeight()+obj.getNetWeight());
							reAsn.setGrossWeight(reAsn.getGrossWeight()+obj.getGrossWeight());
							asnActionService.merge(reAsn);
//							for(AsnAction reAsn:asnActionRes){
//								if(reAsn.getLinkTransferId()!=null){
//									if(!reAsn.getLinkTransferId().equals(transferId)){
//										reAsn.setNum(reAsn.getNum()+obj.getNum());
//										reAsn.setNetWeight(reAsn.getNetWeight()+obj.getNetWeight());
//										reAsn.setGrossWeight(reAsn.getGrossWeight()+obj.getGrossWeight());
//										asnActionService.save(reAsn);
//									}
//								}
//							}
						}else {
							actionOri.add(new PropertyFilter("EQS_status", "1"));//状态，正常
							if(obj.getAsn()==null||"".equals(obj.getAsn())){
								actionOri.add(new PropertyFilter("NULLS_asn", "")); //ASN
							}else{
								actionOri.add(new PropertyFilter("EQS_asn", obj.getAsn()));//ASN
							}
							actionOri.add(new PropertyFilter("EQS_sku", obj.getSku()));//sku
							actionOri.add(new PropertyFilter("EQS_clientId", obj.getClientId()));//客户ID
							actionOri.add(new PropertyFilter("EQD_chargeStaDate", obj.getChargeStaDate()));//计费开始日期
							actionOri.add(new PropertyFilter("NULLS_chargeEndDate", ""));//计费结束日期
							List<AsnAction> asnActionOris = asnActionService.search(actionOri);
							if(null != asnActionOris && asnActionOris.size() > 0){
								AsnAction oriAsn=new AsnAction();
								oriAsn=asnActionOris.get(0);
								oriAsn.setNum(oriAsn.getNum()+obj.getNum());
								oriAsn.setNetWeight(oriAsn.getNetWeight()+obj.getNetWeight());
								oriAsn.setGrossWeight(oriAsn.getGrossWeight()+obj.getGrossWeight());
								asnActionService.merge(oriAsn);
							}
						}
//					}
				}else if(obj.getClientId().equals(oldId) && obj.getChargeEndDate()!=null){
					asnActionService.delete(obj);
					List<PropertyFilter> actionRe = new ArrayList<PropertyFilter>();
					List<PropertyFilter> actionOri = new ArrayList<PropertyFilter>();
					actionRe.add(new PropertyFilter("EQS_status", "1"));//状态，正常
					if(obj.getAsn()==null||"".equals(obj.getAsn())){
						actionRe.add(new PropertyFilter("NULLS_asn", "")); //ASN
					}else{
						actionRe.add(new PropertyFilter("EQS_asn", obj.getAsn()));//ASN
					}
					actionRe.add(new PropertyFilter("EQS_sku", obj.getSku()));//sku
					actionRe.add(new PropertyFilter("EQS_clientId", obj.getClientId()));//客户ID
					actionRe.add(new PropertyFilter("EQD_chargeStaDate", obj.getChargeStaDate()));//计费开始日期
					actionRe.add(new PropertyFilter("EQD_chargeEndDate", obj.getChargeEndDate()));//计费结束日期
					List<AsnAction> asnActionRes = asnActionService.search(actionRe);
					if(null != asnActionRes && asnActionRes.size() > 0){
						Collections.sort(asnActionRes, new Comparator<AsnAction>(){
				 			@Override  
				            public int compare(AsnAction b1, AsnAction b2) {  
				                return b2.getId().compareTo(b1.getId());  
				            }  

				 		});
						AsnAction reAsn = new AsnAction();
						reAsn=asnActionRes.get(0);
						reAsn.setNum(reAsn.getNum()+obj.getNum());
						reAsn.setNetWeight(reAsn.getNetWeight()+obj.getNetWeight());
						reAsn.setGrossWeight(reAsn.getGrossWeight()+obj.getGrossWeight());
						asnActionService.merge(reAsn);
					}else {
						actionOri.add(new PropertyFilter("EQS_status", "1"));//状态，正常
						if(obj.getAsn()==null||"".equals(obj.getAsn())){
							actionOri.add(new PropertyFilter("NULLS_asn", "")); //ASN
						}else{
							actionOri.add(new PropertyFilter("EQS_asn", obj.getAsn()));//ASN
						}
						actionOri.add(new PropertyFilter("EQS_sku", obj.getSku()));//sku
						actionOri.add(new PropertyFilter("EQS_clientId", obj.getClientId()));//客户ID
						actionOri.add(new PropertyFilter("EQD_chargeStaDate", obj.getChargeStaDate()));//计费开始日期
						actionOri.add(new PropertyFilter("NULLS_chargeEndDate", ""));//计费结束日期
						//actionOri.add(new PropertyFilter("NEQS_linkTransferId", transferId));//计费结束日期
						List<AsnAction> asnActionOris = asnActionService.search(actionOri);
						if(null != asnActionOris && asnActionOris.size() > 0){
							AsnAction oriAsn=new AsnAction();
							oriAsn=asnActionOris.get(0);
							oriAsn.setNum(oriAsn.getNum()+obj.getNum());
							oriAsn.setNetWeight(oriAsn.getNetWeight()+obj.getNetWeight());
							oriAsn.setGrossWeight(oriAsn.getGrossWeight()+obj.getGrossWeight());
							asnActionService.merge(oriAsn);
						}
					}
				}else{
//					Integer num = obj.getNum();
//					Double gross = obj.getGrossWeight();
//					Double net = obj.getNetWeight();
//					//添加  asnAction 计费区间表  逻辑
//					List<PropertyFilter> actionPfs = new ArrayList<PropertyFilter>();
//					actionPfs.add(new PropertyFilter("EQS_status", "1"));//状态，正常
//					if(obj.getAsn()==null||"".equals(obj.getAsn())){
//						actionPfs.add(new PropertyFilter("NULLS_asn", "")); //ASN
//					}else{
//						actionPfs.add(new PropertyFilter("EQS_asn", obj.getAsn()));//ASN
//					}
//					actionPfs.add(new PropertyFilter("EQS_sku", obj.getSku()));//sku
//					actionPfs.add(new PropertyFilter("EQS_clientId", obj.getClientId()));//客户
//					actionPfs.add(new PropertyFilter("NULLS_chargeEndDate", ""));//时间  为 null
//					List<AsnAction> asnActions = asnActionService.search(actionPfs);
//					if(null != actions && actions.size() > 0){
//						AsnAction aa=asnActions.get(0);
//						//数量
//						num += aa.getNum();
//						aa.setNum(num);
//						//毛重
//						gross = BigDecimalUtil.add(gross, aa.getGrossWeight());
//						aa.setGrossWeight(gross);
//						//净重
//						net = BigDecimalUtil.add(net, aa.getNetWeight());
//						aa.setNetWeight(net);
//						asnActionService.update(aa);
//						asnActionService.delete(obj);
//				}
					asnActionService.delete(obj);
			}
		}
	}
}
	
//	//根据托盘号找出此托盘来源是否是货转
//	private Map<String,Object> getRealTrayId(String oldTrayCode){
//		String trayCode=oldTrayCode;
//		Map<String,Object> param = new HashMap<String,Object>();
//		int sign=0;
//		while(sign==0){
//			//现在拆托表中查找记录
//			List<BisDismantleTray> disTrayList = dismantleTrayDao.findBy("newTrayCode", trayCode);
//			//再寻找货转托盘信息表中的记录
//			List<Map<String,Object>> objList = transferTrayInfoDao.getLastTransfer(trayCode);
//			//拆托表无记录
//			if(disTrayList.isEmpty()){
//				if(objList.isEmpty()){//拆托表无记录 && 货转表无记录
//					param.put("trayCode", trayCode);
//					param.put("transferCode", "");
//					sign=1;
//				}else{ //拆托表无记录&&货转表有记录
////					String code=objList.get(0).get("TRANSFER_ID").toString();
//					param.put("trayCode", trayCode);
//					param.put("transferCode", objList.get(0).get("transferId"));
//					sign=1;
//				}
//			}else{
//				if(objList.isEmpty()){
//					trayCode=disTrayList.get(0).getOldTrayCode();
//				}else{
//					param.put("trayCode", trayCode);
//					param.put("transferCode", objList.get(0).get("transferId"));
//					sign=1;
//				}
//			}
//		}
//		return param;
//	}
//
//	public List<Map<String, Object>> getLastTransfer(String trayCode) {
//		return transferTrayInfoDao.getLastTransfer(trayCode);
//	}
//	
}
