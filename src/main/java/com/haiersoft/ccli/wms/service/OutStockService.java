package com.haiersoft.ccli.wms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haiersoft.ccli.wms.dao.AsnActionDao;
import com.haiersoft.ccli.wms.dao.OutStockDao;
import com.haiersoft.ccli.wms.dao.OutStockInfoDao;
import com.haiersoft.ccli.wms.entity.AsnAction;
import com.haiersoft.ccli.wms.entity.BisOutStock;
import com.haiersoft.ccli.wms.entity.BisOutStockInfo;
import com.haiersoft.ccli.base.dao.ClientDao;
import com.haiersoft.ccli.base.dao.SkuInfoDao;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.BaseSkuBaseInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.BigDecimalUtil;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;

/**
 * 
 * @author pyl
 * @ClassName: OutStockService
 * @Description: 出库联系单Service
 * @date 2016年3月11日 下午4:16:37
 */
@Service
@Transactional(readOnly = true)
public class OutStockService extends BaseService<BisOutStock, String> {
	
	@Autowired
	private OutStockDao outStockDao;
	@Autowired
	private OutStockInfoDao outStockInfoDao;
	@Autowired
	private SkuInfoDao skuInfoDao;
	@Autowired
	private AsnActionDao asnActionDao;
	@Autowired
	private ClientDao clientDao;
	
	@Override
	public HibernateDao<BisOutStock, String> getEntityDao() {
		return outStockDao;
	}
	/**
	 * 按条件查询出库联系单信息
	 * @param outNum 出库联系单id
	 * @param stockIn 存货方客户id
	 * @param receiverId 收货方客户id
	 * @return
	 */
	public  List<Map<String,Object>>  findList(String outNum,String stockIn,String receiverId){
		return outStockDao.findList( outNum, stockIn, receiverId);
	}
	/**
	 * 
	 * @author pyl
	 * @Description: 获得出库联系单号
	 * @date 2016年3月5日 下午12:08:24
	 * @return
	 * @throws
	 */
	public String getOutLinkIdToString() {
		User user = UserUtil.getCurrentUser();
		String userCode = user.getUserCode();
		//判断用户码是否为空
		if (StringUtils.isNull(user.getUserCode())) {
			userCode = "YZH";
		}else{//判断用户码 的长度
			if (userCode.length() > 3) {
				userCode = userCode.substring(0, 3);
			}else if(userCode.length() < 3){
				userCode = StringUtils.lpadStringLeft(3, userCode);
			}
		}
		String outLinkId = "D" + userCode + StringUtils.timeToString();
		return outLinkId;
	}
	/**
	 * 出库报告书
	 * @param itemNum 提单号
	 * @param cunNum 厢号
	 * @param stockIn 客户id
	 * @param linkId 联系单号
	 * @param strTime 入库时间开始
	 * @param endTime 入库时间结束
	 * @return
	 */
	public List<Map<String,Object>> findRepot(Integer ntype,String billNum,String itemNum,String cunNum,String stockIn,String linkId,String strTime,String endTime,String isBonded){
		List<Map<String,Object>> getlist=null;
		if(ntype!=null){
			if(1==ntype){
				getlist=outStockDao.findRepotPT(billNum,itemNum, cunNum, stockIn, linkId, strTime, endTime,isBonded);
			}else if(2==ntype){
				getlist=outStockDao.findRepotJP(billNum,itemNum, cunNum, stockIn, linkId, strTime, endTime,isBonded);
			}else{
				getlist=outStockDao.findRepotOTE(billNum,itemNum, cunNum, stockIn, linkId, strTime, endTime,isBonded);
			}
		}
		return getlist;
	}
	/**
	 * 出库报告书--OTE客户，根据联系单号导出PDF（出库日期为计划出库日期 ）gzq 20160623 添加
	 * @author gzq
	 * @param linkId 联系单号
	 * @return
	 */
	public List<Map<String,Object>> findRepotOTEByOutlinkid(String linkId){
		return outStockDao.findRepotOTEByOutlinkid(linkId);
	}
	

	public List<BisOutStock> findonly(Map<String, Object> params) {
		return outStockDao.findBy(params);
	}
	
	/*****
	 * 出库联系单审核后，填写了买方承担和仓储费开始日期的单子生成卖货费用
	 * @param outStock
	 */
	public void createSellFee(BisOutStock outStock) {
		List<BisOutStockInfo> infoList = outStockInfoDao.findBy("outLinkId", outStock.getOutLinkId());
		BaseClientInfo clientInfoR = clientDao.find(Integer.parseInt(outStock.getReceiverId())); 
		BaseClientInfo clientInfoS = clientDao.find(Integer.parseInt(outStock.getSettleOrgId())); 
		for(BisOutStockInfo info:infoList){

			BaseSkuBaseInfo skuObj=skuInfoDao.find(info.getSkuId());
			//查询  asnAction区间表数据
			List<PropertyFilter> actionPfs = new ArrayList<PropertyFilter>();
			actionPfs.add(new PropertyFilter("EQS_status", "1"));//状态，正常
			if(null==info.getAsn()||"".equals(info.getAsn())){
				actionPfs.add(new PropertyFilter("NULLS_asn", "")); //ASN
			}else{
				actionPfs.add(new PropertyFilter("EQS_asn", info.getAsn()));//ASN
			}
			actionPfs.add(new PropertyFilter("EQS_sku", info.getSkuId()));//sku
			actionPfs.add(new PropertyFilter("EQS_clientId", outStock.getStockInId()));//客户
			actionPfs.add(new PropertyFilter("NULLS_chargeEndDate", ""));//时间  为 null
			actionPfs.add(new PropertyFilter("NULLS_outId", ""));
			List<AsnAction> asnActions = asnActionDao.find(actionPfs);

			if(null != asnActions && asnActions.size() > 0){
					int numT=info.getOutNum();//出库数量
					int numA=0;//此ASNACTION要减少的数量
					int sign=0;//循环结束标志
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
									if(info.getAsn()==null||"".equals(info.getAsn())){
										pf.add(new PropertyFilter("NULLS_asn", "")); //ASN
									}else{
										pf.add(new PropertyFilter("EQS_asn", info.getAsn()));
									}
									pf.add(new PropertyFilter("EQS_sku", info.getSkuId()));//sku
									pf.add(new PropertyFilter("EQS_linkTransferId", asnAction.getLinkTransferId()));
									pf.add(new PropertyFilter("NULLS_outId", ""));
									pf.add(new PropertyFilter("NULLS_outLinkId", ""));
									pf.add(new PropertyFilter("EQS_status", "1"));
									pf.add(new PropertyFilter("NULLS_scrapCode", ""));
									List<AsnAction> actions = asnActionDao.find(pf);
									
									if(null != actions && actions.size() > 0){
										int i=0;
										for(AsnAction action : actions){
											//修改 主 AsnAction的数据
											action.setNum(action.getNum()-numA);
											action.setNetWeight(BigDecimalUtil.mul(skuObj.getNetSingle(), action.getNum()));
											action.setGrossWeight(BigDecimalUtil.mul(skuObj.getGrossSingle(), action.getNum()));
											asnActionDao.save(action);

											//复制 生成  子  AsnAction的数据,此条为原存货方到仓储费承担日期为止的
											AsnAction action2  = new AsnAction();
											BeanUtils.copyProperties(action, action2);//复制
											
											action2.setId(null);//主键
											action2.setNum(numA);
											action2.setNetWeight(BigDecimalUtil.mul(skuObj.getNetSingle(), action2.getNum()));
											action2.setGrossWeight(BigDecimalUtil.mul(skuObj.getGrossSingle(), action2.getNum()));
											//action2.setChargeEndDate(DateUtils.addDay(outStock.getStartStoreTiem(), -1));	
											if(action2.getChargeEndDate() == null || DateUtils.addDay(outStock.getStartStoreTiem(), -1).getTime() < action2.getChargeEndDate().getTime()){
												action2.setChargeEndDate(DateUtils.addDay(outStock.getStartStoreTiem(), -1));//出库时间
											}
//											action2.setTransferId(null);//货转单
//											action2.setLinkTransferId(null);
											
											//只有存货方客户的区间记录才更新出库单号、出库订单、结算客户，其他客户的货转记录保持单号是原来的状态，即原来的数据记录在入库联系单上，不记录在出库联系单上，出库联系单只记录当前收货方的存储费
											//if(action2.getClientId().equals(outStock.getStockInId())){
											//	action2.setOutId(info.getOutLinkId());//出库联系单
											//}
											
											
											
											action2.setOutLinkId(null);
											action2.setOutId(info.getOutLinkId());//出库联系单
											action2.setClientDay(clientInfoS.getCheckDay());//结算日
											asnActionDao.save(action2);
											//收货方的记录只生成一次
											if(i==0){
												//复制 生成  子  AsnAction的数据,此条为收货方结算从仓储费承担日期开始的
												AsnAction action3  = new AsnAction();
												BeanUtils.copyProperties(action, action3);//复制
												action3.setId(null);//主键
												action3.setChargeStaDate(outStock.getStartStoreTiem());
												action3.setNum(numA);
												action3.setNetWeight(BigDecimalUtil.mul(skuObj.getNetSingle(), action2.getNum()));
												action3.setGrossWeight(BigDecimalUtil.mul(skuObj.getGrossSingle(), action2.getNum()));
	//											action3.setTransferId(null);//货转单
	//											action3.setLinkTransferId(null);
												action3.setOutLinkId(null);
												action3.setOutId(info.getOutLinkId());//出库联系单
												action3.setJfClientId(outStock.getSettleOrgId()); //结算单位ID
												action3.setFeePlanId(outStock.getFeeScheme());//收货方费用方案
												action3.setClientDay(clientInfoR.getCheckDay());//结算日
												asnActionDao.save(action3);
											}
											i++;
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
										//判断 计费结束日期
										asnAction2.setChargeEndDate((DateUtils.addDay(outStock.getStartStoreTiem(), -1)));//出库时间
										asnAction2.setNum(numA);
										asnAction2.setNetWeight(BigDecimalUtil.mul(skuObj.getNetSingle(), asnAction2.getNum()));
										asnAction2.setGrossWeight(BigDecimalUtil.mul(skuObj.getGrossSingle(), asnAction2.getNum()));
										asnAction2.setOutId(outStock.getOutLinkId());
										
										asnActionDao.save(asnAction2);
										
										//复制生成  子  AsnAction的数据,收货方承担的部分
										AsnAction asnAction3  = new AsnAction();
										BeanUtils.copyProperties(asnAction, asnAction3);//复制
										
										asnAction3.setId(null);//主键
										//判断 计费结束日期
										asnAction3.setChargeStaDate(outStock.getStartStoreTiem());
										asnAction3.setNum(numA);
										asnAction3.setNetWeight(BigDecimalUtil.mul(skuObj.getNetSingle(), asnAction2.getNum()));
										asnAction3.setGrossWeight(BigDecimalUtil.mul(skuObj.getGrossSingle(), asnAction2.getNum()));
										asnAction3.setOutId(outStock.getOutLinkId());//出库联系单
										asnAction3.setJfClientId(outStock.getSettleOrgId()); //结算单位ID=页面结算单位
										asnAction3.setFeePlanId(outStock.getFeeScheme());//收货方费用方案
										asnAction3.setClientDay(clientInfoS.getCheckDay());//结算日=页面结算单位的结算日
										
										asnActionDao.save(asnAction3);
								}
							}else{//end if sign
								break;
							}
						}//end for asnAction
			}
		
		}
		
	}

	/**
	 * @param itemNum  提单号
	 * @param ctnNum  箱号
	 * @param realClientName  客户名称
	 * @throws Exception
	 * @throws
	 * @Description: 出库报告书  接口信息（海路通系统）
	 */
	public List<Map<String,Object>> outStockReportInfo(String itemNum, String ctnNum, String realClientName){
		List<Map<String,Object>> getList=outStockDao.outStockReportInfo(itemNum, ctnNum, realClientName);

		return getList;
	}



}
