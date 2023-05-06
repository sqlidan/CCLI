package com.haiersoft.ccli.wms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.BaseClientRank;
import com.haiersoft.ccli.base.entity.BaseLoadingSQL;
import com.haiersoft.ccli.base.entity.BisExpenseSchemeScale;
import com.haiersoft.ccli.base.service.ClientRankService;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.base.service.ExpenseSchemeScaleService;
import com.haiersoft.ccli.base.service.LoadingSQLService;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.dao.TransferDao;
import com.haiersoft.ccli.wms.entity.BisDismantleTray;
import com.haiersoft.ccli.wms.entity.BisLoadingOrder;
import com.haiersoft.ccli.wms.entity.BisTransferStock;
import com.haiersoft.ccli.wms.entity.BisTransferStockInfo;
import com.haiersoft.ccli.wms.entity.BisTransferStockTrayInfo;
import com.haiersoft.ccli.wms.entity.SplitStockModel;
import com.haiersoft.ccli.wms.entity.TrayInfo;

/**
 * @ClassName: TransferService
 * @Description: 货转Service
 * @date 2016年3月4日 下午4:29:46
 */
@Service
public class TransferService extends BaseService<BisTransferStock, String> {

	@Autowired
	private TransferDao transferDao;
	@Autowired
	private TransferInfoService transferInfoService;//货转明细
	@Autowired
	private ExpenseSchemeScaleService expenseSchemeScaleService;//费用分摊比例
	@Autowired
	private LoadingSQLService loadingSQLService;//策略sql
	@Autowired
	private TrayInfoService trayInfoService;//库存明细
	@Autowired
	private TransferTrayInfoService transferTrayInfoService;//货转托盘明细
	@Autowired
	private DismantleTrayService dismantleTrayService;//拆托记录
	@Autowired
	private ClientRankService clientRankService;
	@Autowired
	private ClientService clientService;
	
	@Override
    public HibernateDao<BisTransferStock, String> getEntityDao() {
	    return transferDao;
    }
	/**
	 * 生成货转单号：T（货转标志）+操作人员代码(三位，自动补齐)+YYMMDDHHMMS
	 * @return
	 */
	public String  getTransferId() {
		StringBuffer retASN=new StringBuffer("T");
		User user = UserUtil.getCurrentUser();
		if(user!=null && user.getUserCode()!=null && !"".equals(user.getUserCode())){
			retASN.append(user.getUserCode());
		}else{
			retASN.append("YZH");
		}
		retASN.append(DateUtils.getDateStr(new Date(),"YYMMddHHmmss"));
		return retASN.toString();
	} 
	/**
	 * 保存货转单信息
	 * @param transfer
	 * @return
	 */
	public Map<String,Object> saveTransfer(BisTransferStock transfer){
		Map<String,Object> retMap=new HashMap<String,Object>();
		retMap.put("endStr", "ERROR");
		if(transfer!=null && transfer.getTransferId()!=null && !"".equals(transfer.getTransferId())){
			BisTransferStock getObj=this.get(transfer.getTransferId());
			if(getObj!=null && getObj.getTransferId()!=null){
				// update
				retMap=this.updateTransfer(transfer);
				return retMap;
			}
			User user = UserUtil.getCurrentUser();
			String userName="";
			Date nowDate=new Date();
			if(user!=null&& !"".equals(user)){
				userName=user.getName();	
			}
			transfer.setOperateTime(nowDate);
			transfer.setOperator(userName);
			transfer.setAuditingState("0");
			if(null==transfer.getIsBuyFee()){
				transfer.setIsBuyFee("0");
			}
			this.save(transfer);
			
			/************************费用分摊*********************************************/
			/*sellFee,buyFee 数据格式为：费目代码,是否按比例,费目代码,是否按比例,费目代码,是否按比例 */
			//存货方承担
			String sellFee=transfer.getSellFee()!=null?transfer.getSellFee():"";
			String sellFeeName=transfer.getSellFeeName()!=null?transfer.getSellFeeName():"";
			if(!"".equals(sellFee)){
				String []sellFeeList=sellFee.split(",");
				String []sellFeeNameList=sellFeeName.split(",");
				if(sellFeeList!=null && sellFeeList.length>0 && sellFeeNameList!=null && sellFeeNameList.length>0){
					saveExpenseSchemeScale(transfer.getTransferId(),transfer.getStockInId(),sellFeeList,sellFeeNameList,1);
				}
			}
			//收货方承担
			String buyFee=transfer.getBuyFee()!=null?transfer.getBuyFee():"";
			String buyFeeName=transfer.getBuyFeeName()!=null?transfer.getBuyFeeName():"";
			if(!"".equals(buyFee)){
				String []buyFeeList=buyFee.split(",");
				String []buyFeeNameList=buyFeeName.split(",");
				if(buyFeeList!=null && buyFeeList.length>0 && buyFeeNameList!=null && buyFeeNameList.length>0){
					saveExpenseSchemeScale(transfer.getTransferId(),transfer.getReceiver(),buyFeeList,buyFeeNameList,0);
				}
			}
			
			/************************明细信息*********************************************/
			String[] skuList=transfer.getSkuList();//sku集合
			String[] rkList=transfer.getRkList();//sku集合
			String[] billList=transfer.getBillList();//提单
			String[] ctnList=transfer.getCtnList();//厢号
			String[] foodList=transfer.getFoodList();//品名
			String[] intoList=transfer.getIntoList();//入库类型
			Double[] peacList=transfer.getPeacList();//出库件数
			Double[] netList=transfer.getNetList();//单净重
			Double[] grossList=transfer.getGrossList();//单毛重
			Double weight=0d;
			if(skuList!=null && skuList.length>0){
				BisTransferStockInfo infoObj=null;
				for(int i=0;i<skuList.length;i++){
					infoObj=new BisTransferStockInfo();
					if(peacList[i]!=null && grossList[i]!=null && netList[i]!=null ){
						infoObj.setTransferId(transfer.getTransferId());
						infoObj.setBillNum(billList[i]);
						infoObj.setCtnNum(ctnList[i]);
						infoObj.setSku(skuList[i]);
						if(rkList.length>0){
							infoObj.setRkNum(rkList[i]);
						}else{
							infoObj.setRkNum("");
						}
						infoObj.setCargoName(foodList[i]);
						infoObj.setPiece(peacList[i]);
						infoObj.setGrossWeight(Double.valueOf(peacList[i])*Double.valueOf(grossList[i]));
						infoObj.setNetWeight(Double.valueOf(peacList[i])*Double.valueOf(netList[i]));
						infoObj.setEnterState(intoList[i]);
						infoObj.setUntis("1");
						infoObj.setCrUser(userName);
						infoObj.setCrTime(nowDate);
						weight += infoObj.getNetWeight();
						transferInfoService.save(infoObj);
					}
				}
			}
			BaseClientInfo clientObj = clientService.get(Integer.valueOf(transfer.getStockInId()));
			if(this.checkRank(weight, clientObj)){
				retMap.put("endStr", "OVER");
			}else{
				retMap.put("endStr", "SUCCESS");
			}
		}
		return retMap;
	}
	/**
	 * 保存货转费用分摊数据
	 * @param transferId 货转联系单号
	 * @param clientId 客户id
	 * @param feeList 数据格式为：费目代码,是否按比例,费目代码,是否按比例,费目代码,是否按比例 
	 * @param feeNameList 
	 * @param type 0：买方，1卖方
	 */
	public void saveExpenseSchemeScale(String transferId,String clientId, String []feeList,String []feeNameList,int type){
		BisExpenseSchemeScale expenseSchemeScale=null;
		if(feeList!=null && feeList.length>0 && feeNameList!=null && feeNameList.length>0){
			for(int i=0;i<feeList.length;i=i+2){
				expenseSchemeScale=new BisExpenseSchemeScale();
				expenseSchemeScale.setLinkId(transferId);
				expenseSchemeScale.setCustomsId(clientId);
				expenseSchemeScale.setBosSign(type+"");//0：买方，1卖方
				expenseSchemeScale.setFeeCode(feeList[i]);
				expenseSchemeScale.setIfRatio(feeList[i+1]);
				expenseSchemeScale.setSchemeName(feeNameList[i/2]);
				expenseSchemeScale.setEntrySign("3");//标记货转
				expenseSchemeScaleService.save(expenseSchemeScale);
			}
		}
	}
	/**
	 * 保存货转单信息
	 * @param transfer
	 * @return
	 */
	public Map<String,Object> updateTransfer(BisTransferStock transfer){
		HashMap<String,Object> retMap=new HashMap<String,Object>();
		retMap.put("endStr", "ERROR");
		if(transfer!=null && transfer.getTransferId()!=null && !"".equals(transfer.getTransferId())){
			Date nowDate=new Date();
			transfer.setUpdateTime(nowDate);
			if(null==transfer.getIsBuyFee()){
				transfer.setIsBuyFee("0");
			}
			this.update(transfer);
			User user = UserUtil.getCurrentUser();
			String userName="";
			if(user!=null){
				userName=user.getName();	
			}
			/************************费用分摊*********************************************/
			/*sellFee,buyFee 数据格式为：费目代码,是否按比例,费目代码,是否按比例,费目代码,是否按比例 */
			//情空之前数据
			expenseSchemeScaleService.delByLinkId(transfer.getTransferId());
			//存货方承担
			String sellFee=transfer.getSellFee()!=null?transfer.getSellFee():"";
			String sellFeeName=transfer.getSellFeeName()!=null?transfer.getSellFeeName():"";
			if(!"".equals(sellFee)){
				String []sellFeeList=sellFee.split(",");
				String []sellFeeNameList=sellFeeName.split(",");
				if(sellFeeList!=null && sellFeeList.length>0 && sellFeeNameList!=null && sellFeeNameList.length>0){
					saveExpenseSchemeScale(transfer.getTransferId(),transfer.getStockInId(),sellFeeList,sellFeeNameList,1);
				}
			}
			//收货方承担
			String buyFee=transfer.getBuyFee()!=null?transfer.getBuyFee():"";
			String buyFeeName=transfer.getBuyFeeName()!=null?transfer.getBuyFeeName():"";
			if(!"".equals(buyFee)){
				String []buyFeeList=buyFee.split(",");
				String []buyFeeNameList=buyFeeName.split(",");
				if(buyFeeList!=null && buyFeeList.length>0 && buyFeeNameList!=null && buyFeeNameList.length>0){
					saveExpenseSchemeScale(transfer.getTransferId(),transfer.getReceiver(),buyFeeList,buyFeeNameList,0);
				}
			}
			/************************订单明细信息*********************************************/
			String[] skuList=transfer.getSkuList();//sku集合
			String[] rkList=transfer.getRkList();//sku集合
			String[] billList=transfer.getBillList();//提单
			String[] ctnList=transfer.getCtnList();//厢号
			String[] foodList=transfer.getFoodList();//品名
			String[] intoList=transfer.getIntoList();//入库类型
			Double[] peacList=transfer.getPeacList();//出库件数
			Double[] netList=transfer.getNetList();//单净重
			Double[] grossList=transfer.getGrossList();//单毛重
			String [] delList=transfer.getDelList();//明细删除列
			Double weight=0d;
			if(delList!=null && delList.length>0){
				//删除明细列
				String[] labList=null;
				for(String lab:delList){
					labList=lab.split("\\$\\$");
					if(4==labList.length){
						//执行明细删除
						transferInfoService.delTransferStockInfo(transfer.getTransferId(), labList[1], labList[2], labList[3], labList[0]); 
					}
				}
			}
			if(skuList!=null && skuList.length>0){
				BisTransferStockInfo infoObj=null;
				for(int i=0;i<skuList.length;i++){
					//获取出库订单明细，如果能拿取到说明该明细没有改动，如果该明细获取不到则标示为新添加明细
					infoObj=transferInfoService.findTransferInfoObj(transfer.getTransferId(), billList[i], ctnList[i], skuList[i], intoList[i]);
					if(infoObj==null){
						infoObj=new BisTransferStockInfo();
						if(peacList[i]!=null && grossList[i]!=null && netList[i]!=null ){
							infoObj.setTransferId(transfer.getTransferId());
							infoObj.setBillNum(billList[i]);
							infoObj.setCtnNum(ctnList[i]);
							infoObj.setSku(skuList[i]);
							if(rkList.length>0){
								infoObj.setRkNum(rkList[i]);
							}else{
								infoObj.setRkNum("");
							}
							infoObj.setCargoName(foodList[i]);
							infoObj.setPiece(peacList[i]);
							infoObj.setGrossWeight(Double.valueOf(peacList[i])*Double.valueOf(grossList[i]));
							infoObj.setNetWeight(Double.valueOf(peacList[i])*Double.valueOf(netList[i]));
							infoObj.setEnterState(intoList[i]);
							infoObj.setUntis("1");
							infoObj.setCrUser(userName);
							infoObj.setCrTime(nowDate);
							weight += infoObj.getNetWeight();
							transferInfoService.save(infoObj);
						}
					}
				}
			}
			BaseClientInfo clientObj = clientService.get(Integer.valueOf(transfer.getStockInId()));
			if(this.checkRank(weight, clientObj)){
				retMap.put("endStr", "OVER");
			}else{
				retMap.put("endStr", "SUCCESS");
			}
		}
		return retMap;
	}
	
	/**
	 * 根据货转单号，出库策略生成货转托盘信息
	 * @param ordCode //订单编号
	 * @param clCode //策略编号
	 */
	@Transactional(readOnly = false)
	public Map<String,Object> createTruck(String transferId,String clCode){
		Map<String,Object> retMap=new HashMap<String,Object>();
		retMap.put("endStr", "error");
		if(transferId!=null && !"".equals(transferId) && clCode!=null && !"".equals(clCode)){
				//获取货转明细
				List<Map<String,Object>> getOrdList=transferInfoService.findInfoList(transferId);
				//获取货转对象
				BisTransferStock getObj=this.get(transferId);
				if(getOrdList!=null && getOrdList.size()>0 && getObj!=null){
					String stockId=getObj.getStockInId();//存货方id
					String sqlIf="";
					String sqlOrd="FLOOR_NUM,ROOM_NUM,AREA_NUM,NOW_PIECE asc";
					//根据策略id获取策略sql
					if(Integer.valueOf(clCode)>0){
						BaseLoadingSQL getSQL=loadingSQLService.get(Integer.valueOf(clCode));
						sqlIf=getSQL.getIfStr();
						sqlOrd=getSQL.getOrdStr();
					} 
					StringBuffer trayIds=new StringBuffer();
					ArrayList<HashMap<String,String>> splitTray=new ArrayList<HashMap<String,String>>();//记录拆托
					for(Map<String,Object> getMap:getOrdList){
						StringBuffer ids=new StringBuffer();
						String billNum=getMap.get("BILL_NUM").toString();
						String ctnNum=getMap.get("CTN_NUM").toString();
						String asn="";
						String skuNum=getMap.get("SKU_ID").toString();
						String enterState=getMap.get("ENTER_STATE").toString();
						Integer allNum=Integer.valueOf(getMap.get("PIECE").toString());
						//按策略进行拣货
						List<Map<String, Object>> getTrayList = trayInfoService.findTrayList(getObj.getWarehouseId(), stockId, billNum, ctnNum, asn, skuNum, enterState, sqlIf, sqlOrd, allNum, "", "");
						if(getTrayList!=null && getTrayList.size()>0 && allNum>0){
							for(int i=0;i<getTrayList.size();i++){
								Map<String,Object> trayInfo=getTrayList.get(i);
								ids.append(trayInfo.get("ID")).append(",");
								allNum=allNum-Integer.valueOf(trayInfo.get("NOW_PIECE").toString());
								if(0>allNum){
									//标示拆托结束,添加需要拆托的数据
									HashMap<String,String> splitMap=new HashMap<String,String>();
									splitMap.put("id",trayInfo.get("ID").toString());
									splitMap.put("splitnum",String.valueOf(Integer.valueOf(trayInfo.get("NOW_PIECE").toString())+allNum));
									splitMap.put("tcode", trayInfo.get("TRAY_ID").toString());
									splitTray.add(splitMap);
									break;
								}else{
									trayIds.append(trayInfo.get("ID")).append(",");
								}
								if(0==allNum){//正好捡完则跳出
									break;
								}
							}
						}
						//策略拣货后与出库数量不足时进行默认拣货
						if(allNum>0 && !"".equals(sqlIf)){
							 sqlOrd="FLOOR_NUM,ROOM_NUM,AREA_NUM,NOW_PIECE asc";
							 getTrayList = trayInfoService.findNoIfTrayList(getObj.getWarehouseId(),stockId, billNum, ctnNum, skuNum, enterState, sqlOrd,ids.toString());
							 for(int i=0;i<getTrayList.size();i++){
									Map<String,Object> trayInfo=getTrayList.get(i);
									allNum=allNum-Integer.valueOf(trayInfo.get("NOW_PIECE").toString());
									if(0>allNum){
										//标示拆托结束,添加需要拆托的数据
										HashMap<String,String> splitMap=new HashMap<String,String>();
										splitMap.put("id",trayInfo.get("ID").toString());
										splitMap.put("splitnum",String.valueOf(Integer.valueOf(trayInfo.get("NOW_PIECE").toString())+allNum));
										splitMap.put("tcode", trayInfo.get("TRAY_ID").toString());
										splitTray.add(splitMap);
										break;
									}else{
										trayIds.append(trayInfo.get("ID")).append(",");
									}
									if(0==allNum){//正好捡完则跳出
										break;
									}
								}
						}else{
							//拣货策略完成拣货跳出当前循环
							continue;
						}
						//拣货后数量大于0，则库存不足
						if(allNum>0){
							retMap.put("billNum", billNum);
							retMap.put("ctnNum", ctnNum);
							retMap.put("skuNum", skuNum);
							retMap.put("state", enterState);
							return retMap;
						}
				}//end for
				if(splitTray.size()==0){
					//执行保存操作
					Map<String,Object> saveMap=transferTrayInfoService.saveTransferStockTrayInfo(trayIds.toString(),transferId);
					if("success".equals(saveMap.get("endStr"))){
						retMap.put("endStr", "success");
						retMap.put("tNum", saveMap.get("tNum"));
					}else{
						retMap.put("endStr", "erroe");
					}
				}else{
					retMap.put("endStr", "success");
					retMap.put("trayIds", trayIds);
					retMap.put("splitTray", splitTray);
				}
				return retMap;
			}else{
				//订单对象不存在
			}
		}
		return retMap;
	}
	
	/**
	 * 先进行库存明细拆托操作，再执行货转明细生成
	 * @param model
	 */
	@Transactional(readOnly = false)
	public Map<String,Object> createCTTransferTrayInfo(SplitStockModel model){
		Map<String,Object> retMap=new HashMap<String,Object>();
		retMap.put("endStr", "erroe");
		if(model.getOrdId()!=null && !"".equals(model.getOrdId())){
			User user = UserUtil.getCurrentUser();
			Date nowDate=new Date();
			//获取订单对象
			BisTransferStock transferObj=this.get(model.getOrdId());
			if(transferObj!=null && !"".equals(transferObj.getTransferId())){
				Integer[] tId=model.gettId();//代拆分库存明细id
				Integer[] spNum=model.getSpNum();//拆分数量 ==要出库数量
				String[]  newTCode=model.getNewTCode();//新托盘号
				StringBuffer getNowTids=new StringBuffer();//生成新托库存主键id集合
				if(tId!=null && tId.length>0){
					TrayInfo trayObj=null;
					TrayInfo newObj=null;
					BisDismantleTray dismantleTray=null;//拆托记录
					
					for(int i=0;i< tId.length;i++){
						//根据id获取库存明细对象，进行拆托处理
						trayObj=trayInfoService.get(tId[i]);
						if(trayObj!=null && trayObj.getId()>0 && spNum!=null && i<spNum.length){
							newObj=new TrayInfo();
							BeanUtils.copyProperties(trayObj, newObj);//复制
							//生成新托出库数量
							newObj.setId(null);
							newObj.setOriginalPiece(spNum[i]);
							newObj.setNowPiece(spNum[i]);
							newObj.setTrayId(newTCode[i]);
							newObj.setNetWeight(spNum[i]*trayObj.getNetSingle());
							newObj.setGrossWeight(spNum[i]*trayObj.getGrossSingle());
							newObj.setIfTransfer("1");//货转
							newObj.setIsTruck("0"); 
							newObj.setContactType("3");
							newObj.setContactNum(model.getOrdId());
							newObj.setBeforeStockIn(trayObj.getStockIn());
							newObj.setBeforeStockName(trayObj.getStockName());
							newObj.setStockIn(transferObj.getReceiver());
							newObj.setStockName(transferObj.getReceiverName());
							newObj.setRemovePiece(0);
							trayInfoService.save(newObj);
							newObj=trayInfoService.find("trayId", newTCode[i]);
							if(newObj!=null && newObj.getId()!=null){
								getNowTids.append(newObj.getId()).append(",");
							}
							
							//保留托盘 原货主
							trayObj.setNowPiece(trayObj.getNowPiece()-spNum[i]);
							trayObj.setNetWeight(trayObj.getNetWeight()-spNum[i]*trayObj.getNetSingle());
							trayObj.setGrossWeight(trayObj.getGrossWeight()-spNum[i]*trayObj.getGrossSingle());
							trayObj.setRemovePiece((trayObj.getRemovePiece()!=null?trayObj.getRemovePiece():0)+spNum[i]);
							trayInfoService.update(trayObj);
							//保存拆托记录
							dismantleTray = new BisDismantleTray();
							dismantleTray.setOldTrayCode(trayObj.getTrayId());
							dismantleTray.setNewTrayCode(newTCode[i]);
							dismantleTray.setNum(spNum[i]);
							dismantleTray.setDismantleType("3");
							if(user!=null){
								dismantleTray.setDismantleUser(user.getName());
								dismantleTray.setDismantleTime(nowDate);
							}
							dismantleTrayService.save(dismantleTray);
						}
					}
				}//end if
				//执行保存操作
				Map<String,Object> saveMap=transferTrayInfoService.saveTransferStockTrayInfo(model.getIds()+getNowTids.toString(),model.getOrdId());
				if("success".equals(saveMap.get("endStr"))){
					retMap.put("endStr", "success");
					retMap.put("tNum", saveMap.get("tNum"));
				}else{
					retMap.put("endStr", "erroe");
				}
			}
		}
		return retMap;
	}
	/**
	 * 根据货转单号删除货转明细
	 * @param transferId
	 */
	public String deleteTransfer(String transferId){
		String endStr="error";
		transferInfoService.deleteTransferStockInfo(transferId);
		this.delete(transferId);
		endStr="success";
		return endStr;
	}
	
	public Map<String,Object> checkUserNum(BisLoadingOrder order){
		 Map<String,Object> postMap=new HashMap<String,Object>();
		 postMap.put("retStr","success");
		 String[] skuList=order.getSkuList();//sku集合
		 StringBuffer skusb=new StringBuffer();
		 String[] billList=order.getBillList();//提单
		 StringBuffer billsb=new StringBuffer();
		 String[] ctnList=order.getCtnList();//厢号
		 StringBuffer ctnsb=new StringBuffer();
		 String[] intoList=order.getIntoList();//入库类型
		 StringBuffer intosb=new StringBuffer();
		 Integer[] peacList=order.getPeacList();//出库件数
		 if(skuList!=null && skuList.length>0 && billList!=null && ctnList!=null && intoList!=null){
			 for(int i=0;i<skuList.length;i++){
				 skusb.append("'").append(skuList[i]).append("',");
				 billsb.append("'").append(billList[i]).append("',");
				 ctnsb.append("'").append(ctnList[i]).append("',");
				 intosb.append("'").append(intoList[i]).append("',");
			 }
			 List<Map<String,Object>> getList=transferDao.getUserStockNum(order.getStockIn(), order.getWarehouseId(), skusb.substring(0,skusb.length()-1),  billsb.substring(0,billsb.length()-1),  ctnsb.substring(0,ctnsb.length()-1),  intosb.substring(0,intosb.length()-1));
			 if(getList!=null && getList.size()>0 && peacList!=null && peacList.length>0){
				 int myNum=0;
				 boolean isHave=false;
				 for(int i=0;i<peacList.length;i++){
					  isHave=false;
					  for(Map<String,Object> getMap:getList){
						  if(skuList[i].equals(getMap.get("SKU_ID").toString()) && billList[i].equals(getMap.get("BILL_NUM").toString()) && ctnList[i].equals(getMap.get("CTN_NUM").toString()) && intoList[i].equals(getMap.get("ENTER_STATE").toString()) ){
							  isHave=true;
							  //如果出库件数大于等于库存数量
							  if(peacList[i] >= Integer.valueOf(getMap.get("PIECE").toString())){
								  myNum++;
								  break;
							  }
						  }
					  }
					  //货转明细没有匹配到对应库存明细
					  if(false==isHave){
						  myNum++;
					  }
				 }
				 //记数等于要交易的明细数量。则说明为货转的最后批次货
				 if(myNum==skuList.length){
					 postMap.put("retStr","error");
				 }
				 
			 }
		 }
		return postMap;
		
	}
	/**
	 * 取消货转明细 
	 * @param transferId
	 * @return
	 */
	public Map<String, Object> deltruck(String transferId) {
		Map<String,Object> retMap=new HashMap<String,Object>();
		retMap.put("endstr", "error");
		if(transferId!=null && !"".equals(transferId)){
			BisTransferStock getObj=this.get(transferId);
			//查看要取消货转的货物是否被拆托过
			List<Map<String,Object>>  disList=transferDao.getDisObj(transferId);
			if(!disList.isEmpty()){
				retMap.put("endstr", "dis");
				return retMap;
			}
			List<BisTransferStockTrayInfo>  getList=transferTrayInfoService.getTransferStockTrayInfoList(transferId);
			if(getList!=null && getList.size()>0){
				TrayInfo trayInfo=null;
				for(BisTransferStockTrayInfo objinfo:getList){
					trayInfo=trayInfoService.get(objinfo.getTrayInfoId());
					if(trayInfo!=null && trayInfo.getTrayId()!=null){
						trayInfo.setStockIn(getObj.getStockInId());
						trayInfo.setStockName(getObj.getStockIn());
						trayInfoService.save(trayInfo);
						transferTrayInfoService.delete(objinfo);
					}
					retMap.put("endstr", "success");
				}
			}
		}
		return retMap;
	}
	
	/**
	 * 货转出库报告书
	 * @param itemNum 提单号
	 * @param cunNum 厢号
	 * @param stockIn 客户id
	 * @param linkId 联系单号
	 * @return
	 */
	public List<Map<String,Object>> findRepot(Integer ntype,String ifBonded,String transferNum){
		List<Map<String,Object>> getlist=null;
		if(ntype!=null){
			if(1==ntype){
				getlist=transferDao.findRepotPT(ifBonded,transferNum);
			}else if(2==ntype){
				getlist=transferDao.findRepotJP(ifBonded,transferNum);
			}else{
				getlist=transferDao.findRepotOTE(ifBonded,transferNum);
			}
		}
		return getlist;
	}
	
	
	/**
     * 判断出库的明细是否超了警戒线 
     * @param weight 明细总量
     * @param clientObj 客户
     */
    public Boolean checkRank(Double weight,BaseClientInfo clientObj){
    	 if(null==clientObj.getCustomerLevel()){
    		 return false;
    	 }else{
    		 BaseClientRank rank=clientRankService.get(clientObj.getCustomerLevel());
    		 Double safeWeight=rank.getMinNum();
    		 List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
    		 filters.add(new PropertyFilter("EQS_stockIn", clientObj.getIds().toString()));
    		 filters.add(new PropertyFilter("EQS_cargoState", "01"));
    		 List<TrayInfo> infos = trayInfoService.findByF(filters);
    		 Double nowPiece = 0d;
    		 if(!infos.isEmpty()){
	    		 for(TrayInfo info:infos){
	    			 nowPiece += info.getNetWeight();
	    		 }
    		 }
    		 if( nowPiece-weight>safeWeight){
    			 return false;
    		 }else{
    			 return true;
    		 }
    	 }
    }
}
