package com.haiersoft.ccli.wms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.haiersoft.ccli.wms.dao.LoadingInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haiersoft.ccli.api.service.ApiPledgeService;
import com.haiersoft.ccli.base.entity.BaseProductClass;
import com.haiersoft.ccli.base.entity.BaseSkuBaseInfo;
import com.haiersoft.ccli.base.service.ProductClassService;
import com.haiersoft.ccli.base.service.SkuInfoService;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.dao.LoadingOrderDao;
import com.haiersoft.ccli.wms.entity.BisLoadingInfo;
import com.haiersoft.ccli.wms.entity.BisLoadingOrder;
import com.haiersoft.ccli.wms.entity.BisLoadingOrderInfo;
import com.haiersoft.ccli.wms.entity.BisOutStock;

@Service
public class LoadingOrderService extends BaseService<BisLoadingOrder, String> {

    @Autowired
    private LoadingOrderDao loadingOrderDao;

    @Autowired
    private LoadingOrderInfoService loadingOrderInfoService;

    @Autowired
    private OutStockService outStockService;//出库联系单

    @Autowired
    private TrayInfoService trayInfoService;//库存明细

    @Autowired
    private LoadingInfoDao loadingInfoDao;
    
    @Autowired
    private SkuInfoService skuInfoService;
    
    @Autowired
    private ApiPledgeService apiPledgeService;
    
    @Autowired
    private ProductClassService productClassService;
    
    @Override
    public HibernateDao<BisLoadingOrder, String> getEntityDao() {
        return loadingOrderDao;
    }

    /**
     * 生成出库订单号：O+YYMMDD+SEQ(4位循环)
     *
     * @return
     */
    public String getOrderId() {
        StringBuffer retASN = new StringBuffer("O");
        retASN.append(DateUtils.getDateStr(new Date(), "YYMMdd"));
        int getNum = loadingOrderDao.getSequenceId("SEQ_LOADING_ORDER");
        retASN.append(StringUtils.lpadInt(4, getNum));
        return retASN.toString();
    }

    /**
     * 根据订单id删除订单对象信息
     *
     * @param orderId
     * @return
     */
    public String deleteOder(String orderId) {
        String retStr = "error";
        if (orderId != null && !"".equals(orderId)) {
            loadingOrderInfoService.deleteOrderInfos(orderId);
            this.delete(orderId);
            retStr = "success";
        }
        return retStr;
    }

    /**
     * 保存出库订单基础信息和Info
     *
     * @param obj
     * @return
     */
    @Transactional(readOnly = false)
    public String saveOrder(BisLoadingOrder order) {
        String retStr = "error";
        order.setIslock((null!=order.getIslock()&&!"".equals(order.getIslock()))?order.getIslock():"0");
        if (order.getOrderNum() != null) {
            BisLoadingOrder getObj = this.get(order.getOrderNum());
            if (getObj != null && !"".equals(getObj.getOrderNum())) {
                return updateOrder(order);
            }
        }
        if (order.getOutLinkId() != null && !"".equals(order.getOutLinkId())) {
            User user = UserUtil.getCurrentUser();
            Date nowDate = new Date();
            //获取出库联系单对象
            BisOutStock outStock = outStockService.get(order.getOutLinkId());
            if (outStock != null && !"".equals(outStock.getOutLinkId())) {
                /************************订单基本信息*********************************************/
            	//如果出库联系单中标注为清库，则订单更新为最后一次
            	//标示清库
                if (outStock.getIfClearStore() != null && "1".equals(outStock.getIfClearStore())) {
                	order.setIsClear(1);
                	order.setLastCar(1);
                }
                order.setOrderState("1");
                order.setStockIn(outStock.getStockInId());//存货方id
                order.setStockName(outStock.getStockIn());//存货方名称
                order.setWarehouse(outStock.getWarehouse());//仓库
                order.setWarehouseId(outStock.getWarehouseId());//仓库id
                order.setCreateTime(nowDate);
                if (user != null) {
                    order.setCreatePerson(user.getName());
                }
                this.save(order);
                /************************订单明细信息*********************************************/
                String[] skuList = order.getSkuList();//sku集合
                String[] billList = order.getBillList();//提单
                String[] ctnList = order.getCtnList();//厢号
                String[] asnList = order.getAsnList();//厢号
                String[] foodList = order.getFoodList();//品名
                String[] intoList = order.getIntoList();//入库类型
                String[] rkList = order.getRkList();//入库类型
                String[] remarkList = order.getRemarkList();//入库类型
                Integer[] peacList = order.getPeacList();//出库件数
                Double[] netList = order.getNetList();//单净重
                Double[] grossList = order.getGrossList();//单毛重
                if (skuList != null && skuList.length > 0) {
                    BisLoadingOrderInfo orderInfo = null;
                    List<BisLoadingOrderInfo> chekList = new ArrayList<BisLoadingOrderInfo>();
                    for (int i = 0; i < skuList.length; i++) {
                        orderInfo = new BisLoadingOrderInfo();
                        orderInfo.setLoadingPlanNum(order.getOrderNum());
                        orderInfo.setOutLinkId(outStock.getOutLinkId());
                        orderInfo.setBillNum(billList[i]);
                        orderInfo.setCtnNum(ctnList[i]);
                        orderInfo.setAsn(asnList[i]);
                        orderInfo.setSkuId(skuList[i]);
                        if (rkList.length > 0) {
                            orderInfo.setRkNum(rkList[i]);
                        } else {
                            orderInfo.setRkNum("");
                        }
                        if (remarkList.length > 0) {
                            orderInfo.setRemark1(remarkList[i]);
                        } else {
                            orderInfo.setRemark1("");
                        }
                        orderInfo.setPiece(peacList[i]);
                        orderInfo.setCatgoName(foodList[i]);
                        orderInfo.setEnterState(intoList[i]);
                        if (i <= grossList.length && grossList[i] != null) {
                            orderInfo.setGrossSingle(grossList[i]);
                            orderInfo.setGrossWeight(grossList[i] * peacList[i]);
                            orderInfo.setNetSingle(netList[i]);
                            orderInfo.setNetWeight(netList[i] * peacList[i]);
                        }
                        orderInfo.setStockIn(outStock.getStockInId());
                        orderInfo.setStockName(outStock.getStockIn());
                        orderInfo.setUnits("1");//默认千克
                        if (user != null) {
                            orderInfo.setOperator(user.getName());
                        }
                        orderInfo.setOperateTime(nowDate);
                        loadingOrderInfoService.save(orderInfo);
                        chekList.add(orderInfo);
                    }
                    //如果出库订单勾选了最后一车或者是控车的则标记为不放行
                    if (1 == order.getLastCar()||"1".equals(order.getIslock())) {
                        outStock.setIfRelease("0");
                        outStockService.save(outStock);
                    }
                }
                retStr = "success";
            }//end if

        }
        return retStr;
    }


    /**
     * 判断出库联系单明细等于出库订单明细数量
     *
     * @param outCode
     * @return
     */
    public boolean checkOutstockNumEQInfoNum(String outCode) {
        boolean isreturn = true;
        List<Map<String, Object>> getList = loadingOrderDao.getOutCodeAndLoadingCodeNum(outCode);
        if (getList != null && getList.size() > 0) {
            int myNum = 0;
            for (Map<String, Object> getMap : getList) {
                if (getMap.get("APIECE") != null && getMap.get("BPIECE") != null) {
                    //如果联系单数量小于等于订单数量
                    if (Double.valueOf(getMap.get("APIECE").toString()) <= Double.valueOf(getMap.get("BPIECE").toString())) {
                        myNum++;
                    }
                }
            }
            //相当则说明当前出库订单为出库联系单的最后一车货
            if (myNum == getList.size()) {
                isreturn = false;
            }
        }
        return isreturn;

    }

//	/**
//	 * 判断出库明细数是否等于客户库存明细数（库存数-质押数）
//	 * @param userId 客户id
//	 * @param ckId 仓库id
//	 * @param infoList 出库明细对象列表
//	 * @return
//	 */
//	public boolean checkUStockNumEQInfoNum(String userId,String ckId,List<BisLoadingOrderInfo> infoList){
//		boolean isreturn=true;
//		//根据客户id获取客户库存数量
//		List<Map<String,Object>> getList=trayInfoService.findClientTrayList(userId,ckId);
//		if(getList!=null && infoList!=null && getList.size()==infoList.size()){
//			int nEQI=0;
//			for(Map<String,Object> getMap:getList){
//				for(BisLoadingOrderInfo obj:infoList){
//					//判断明细 和客户库存 数量减去质押数量是否一致
//					if(obj.getBillNum().equals(getMap.get("BILL_NUM").toString()) && obj.getSkuId().equals(getMap.get("SKU_ID").toString()) && obj.getStockIn().equals(getMap.get("STOCK_IN").toString()) && obj.getCtnNum().equals(getMap.get("CTN_NUM").toString()) && obj.getEnterState().equals(getMap.get("ENTER_STATE").toString())){
//						if(obj.getPiece()>=(Integer.valueOf(getMap.get("NOW_PIECE").toString())-Integer.valueOf(getMap.get("TNUM").toString()))){
//							nEQI++;
//						} 
//						break;
//					}
//				}//end for
//			}//end for
//			if(nEQI==getList.size()){
//				return false;
//			}else{
//				//动态质押
//				 getList=trayInfoService.findClientSKUTrayList(userId,ckId);
//				 if(getList!=null  && getList.size()>0){
//					 int getNum=0;
//					 int num=0;
//					 for(Map<String,Object> getMap:getList){
//						 //获取库存量
//						 getNum=Integer.valueOf(getMap.get("NOW_PIECE").toString())-Integer.valueOf(getMap.get("TNUM").toString());
//						 if(getNum>0){
//							 for(BisLoadingOrderInfo obj:infoList){
//								 if(obj.getSkuId().equals(getMap.get("SKU_ID").toString())){
//									 getNum=getNum-obj.getPiece();
//								 }
//							 }
//							 if(getNum<=0){
//								 num++;
//							 }
//						 }else{
//							 num++;
//						 }
//					 }//end for
//					 if(num==getList.size()){
//						 isreturn=false;
//					 }
//				 }else{
//					 isreturn=false;
//				 }
//			}
//		} 
//		return isreturn;
//	}

    /**
     * 更新出库订单基础信息和Info
     *
     * @param obj
     * @return
     */
    @Transactional(readOnly = false)
    public String updateOrder(BisLoadingOrder order) {
        String retStr = "error";
        if (order.getOrderNum() != null) {
            /** 获取数据库中,"订单号" 对应的 "出库订单"的数据*/
            BisLoadingOrder getObj = this.get(order.getOrderNum());
            if (getObj != null && order.getOutLinkId() != null && !"".equals(order.getOutLinkId())) {
                Date nowDate = new Date();
                User user = UserUtil.getCurrentUser();
                String yStockIn = getObj.getStockIn();//获取原数据存货方id
                //获取出库联系单对象
                BisOutStock outStock = outStockService.get(order.getOutLinkId());
                if (outStock != null && !"".equals(outStock.getOutLinkId())) {
                    /*************更新装车单表信息BIS_LOADING_INFO************/
                    loadingInfoDao.updateLoadingInfoCarNumWithLoadingOrderChanged(getObj.getOrderNum(),order.getCarNum());
                    /************************订单基本信息*********************************************/
                    getObj.setOutLinkId(order.getOutLinkId());
                    getObj.setReceiverId(order.getReceiverId());
                    getObj.setReceiverName(order.getReceiverName());
                    getObj.setCarNum(order.getCarNum()); // 车牌号
                    getObj.setIslock((null!=order.getIslock()&&!"".equals(order.getIslock()))?order.getIslock():"0");
                    getObj.setOrderState(getObj.getOrderState()); //修改出库订单时,订单状态不变,2016-8-22 ddd edit.
                    getObj.setStockIn(outStock.getStockInId());//存货方id
                    getObj.setStockName(outStock.getStockIn());//存货方名称
                    getObj.setWarehouse(outStock.getWarehouse());//仓库
                    getObj.setWarehouseId(outStock.getWarehouseId());//仓库id
                    getObj.setUpdateTime(nowDate);
                    getObj.setLastCar(order.getLastCar());
                    getObj.setUpdatePerson(user != null?user.getName():null);
                    getObj.setRemark(order.getRemark());
                    getObj.setPlanTime(order.getPlanTime());
                    getObj.setPlanDateNoPay(order.getPlanDateNoPay());
                    this.update(getObj);
                    /************************订单明细信息*********************************************/
                    String[] skuList = order.getSkuList();//sku集合
                    String[] mscList = order.getMscList();//MSC号集合
                    String[] lotList = order.getLotList();//LOT号集合
                    String[] typeSizeList = order.getTypeSizeList();//LOT号集合
                    String[] rkList = order.getRkList();//入库号集合
                    String[] billList = order.getBillList();//提单
                    String[] ctnList = order.getCtnList();//厢号
                    String[] foodList = order.getFoodList();//品名
                    String[] intoList = order.getIntoList();//入库类型
                    Integer[] peacList = order.getPeacList();//出库件数
                    Double[] netList = order.getNetList();//单净重
                    Double[] grossList = order.getGrossList();//单毛重
                    String[] delList = order.getDelList();//明细删除列
                    String[] remarkList = order.getRemarkList();//明细删除列
                    String[] asnList = order.getAsnList();//ASN
                    //如果原客户id和联系单内客户id不一致需要删除之前客户所有明细信息
                    if (!yStockIn.equals(outStock.getStockInId())) {
                        loadingOrderInfoService.deleteOrderInfos(getObj.getOrderNum());
                    } else {
                        if (delList != null && delList.length > 0) {
                            //删除明细列
                            String[] labList = null;
                            for (String lab : delList) {
                                labList = lab.split("\\$\\$");
                                if (5 == labList.length) {
                                    //执行明细删除
                                    loadingOrderInfoService.delOrderInfoObj(getObj.getOrderNum(), labList[1], labList[2], labList[3], labList[0]);
                                }
                            }
                        }
                    }//删除明细 end
                    if (skuList != null && skuList.length > 0) {
                        BisLoadingOrderInfo orderInfo = null;
                        List<BisLoadingOrderInfo> chekList = new ArrayList<BisLoadingOrderInfo>();
                        for (int i = 0; i < skuList.length; i++) {
                            //获取出库订单明细，如果能拿取到说明该明细没有改动，如果该明细获取不到则标示为新添加明细
                            orderInfo = loadingOrderInfoService.findOrderInfoObj(getObj.getOrderNum(), billList[i], ctnList[i], skuList[i], intoList[i],asnList[i]);
                            if (orderInfo == null) {
                                orderInfo = new BisLoadingOrderInfo();
                                orderInfo.setLoadingPlanNum(order.getOrderNum());
                                orderInfo.setOutLinkId(outStock.getOutLinkId());
                                orderInfo.setBillNum(billList[i]);
                                orderInfo.setCtnNum(ctnList[i]);
                                orderInfo.setSkuId(skuList[i]);
                                orderInfo.setAsn(asnList[i]);
                                if (rkList.length > 0) {
                                    orderInfo.setRkNum(rkList[i]);
                                } else {
                                    orderInfo.setRkNum("");
                                }
                                if (mscList.length > 0) {
                                    orderInfo.setMscNum(mscList[i]);
                                } else {
                                    orderInfo.setMscNum("");
                                }
                                if (lotList.length > 0) {
                                    orderInfo.setLotNum(lotList[i]);
                                } else {
                                    orderInfo.setLotNum("");
                                }
                                if (typeSizeList.length > 0) {
                                    orderInfo.setTypeSize(typeSizeList[i]);
                                } else {
                                    orderInfo.setLotNum("");
                                }
                                if (remarkList.length > 0) {
                                    orderInfo.setRemark1(remarkList[i]);
                                } else {
                                    orderInfo.setRemark1("");
                                }
                                orderInfo.setPiece(peacList[i]);
                                orderInfo.setCatgoName(foodList[i]);
                                orderInfo.setEnterState(intoList[i]);
                                if (i <= grossList.length && grossList[i] != null) {
                                    orderInfo.setGrossSingle(grossList[i]);
                                    orderInfo.setGrossWeight(grossList[i] * peacList[i]);
                                    orderInfo.setNetSingle(netList[i]);
                                    orderInfo.setNetWeight(netList[i] * peacList[i]);
                                }
                                orderInfo.setStockIn(outStock.getStockInId());
                                orderInfo.setStockName(outStock.getStockIn());
                                orderInfo.setUnits("1");//默认千克
                                orderInfo.setOperator(user != null?user.getName():null);
                                orderInfo.setOperateTime(nowDate);
                                //loadingOrderInfoService.getEntityDao().getSession().clear();
                                loadingOrderInfoService.getEntityDao().save(orderInfo);
                                chekList.add(orderInfo);
                            }
                        }//end for
                        //如果出库订单勾选了最后一车则标记为不放行
                        if (1 == order.getLastCar()||"1".equals(order.getIslock())) {
                            outStock.setIfRelease("0");
                            outStockService.save(outStock);
                        }
                        //如果取消控车的对应的更改放行标志
                        if("0".equals(order.getIslock())){
                        	outStock.setIfRelease("1");
                            outStockService.save(outStock);
                        }
                    }
                    retStr = "success";
                }//end 联系单
            }
        }
        return retStr;
    }

    /**
     * 校验库存明细SKU出库数量校验，出库数量>=库存量-质押量
     *
     * @param order
     */
    public Map<String, Object> checkSKUNum(BisLoadingOrder order) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("retStr", "success");
        if (order.getOutLinkId() != null && !"".equals(order.getOutLinkId())) {
            //获取出库联系单对象
            BisOutStock outStock = outStockService.get(order.getOutLinkId());
            if (outStock != null) {
                ///List<Map<String,Object>> getList=trayInfoService.findClientSKUTrayList(outStock.getStockInId());
                retMap = checkSKUNum(order, outStock.getWarehouseId(), outStock.getStockInId());
            }
        }
        return retMap;
    }

    /**
     * 校验库存明细SKU出库数量校验，出库数量>=库存量-质押量
     *
     * @param order
     * @param ckId     仓库id
     * @param clientId 库存货主
     */
    public Map<String, Object> checkSKUNum(BisLoadingOrder order, String ckId, String clientId) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("retStr", "success");
        if (order != null && order.getSkuList() != null && clientId != null && !"".equals(clientId) && ckId != null && !"".equals(ckId)) {

            List<Map<String, Object>> getList = trayInfoService.findClientSKUTrayList(clientId, ckId);
            if (getList != null && getList.size() > 0) {
                String[] skuList = order.getSkuList();//sku集合
                Integer[] peacList = order.getPeacList();//出库件数
                if (skuList != null && skuList.length > 0) {
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < skuList.length; i++) {
                        // 校验库存量是否小于输入的出库件数+动态质押量
                        int getNum = check(getList, skuList[i], peacList[i]);
                        if (0 < getNum) {
                            Map<String, Object> map = getList.get(getNum);
                            sb.append(skuList[i]).append("质押量为" + map.get("TNUM").toString()).append(",");
                        }
                    }//end fo
                    if (sb.length() > 0) {
                        retMap.put("retStr", "error");
                        retMap.put("sku", sb.substring(0, sb.length() - 1));
                    }
                }
            }
        }
        //按小类检查质押数量是否可用
        HashMap<String,Integer> classMap = new HashMap();
        String[] skuList = order.getSkuList();//sku集合
        Integer[] peacList = order.getPeacList();//出库件数
        //classMap中的值为小类和此小类对应的出库件数
        for (int i = 0; i < skuList.length; i++) {
        	BaseSkuBaseInfo sku = skuInfoService.getSKUInfo(skuList[i]);
        	if(null !=classMap.get(sku.getClassType())){
        		Integer sum = classMap.get(sku.getClassType());
        		classMap.put(sku.getClassType(), sum+peacList[i]);
        	}else {
        		classMap.put(sku.getClassType(), peacList[i]);
        	}
        }
        //遍历classMap比对每个货种小类的出库件数是否可用
        for(Map.Entry<String, Integer> entry : classMap.entrySet()){
        	//货种小类
            String mapKey = entry.getKey();
            //货种小类的累加出库件数
            Integer mapValue = entry.getValue();
            //此货种小类可用数量
            Integer availableNum = apiPledgeService.pledgeNumCount(Integer.valueOf(clientId), Integer.valueOf(mapKey));
            if(mapValue > availableNum) {
            	
            	BaseProductClass cla=productClassService.get(Integer.valueOf(mapKey));
            	retMap.put("retStr", "pledged");
            	retMap.put("classType", cla.getpName());
            	retMap.put("countNum", mapValue);
            	retMap.put("availableNum", availableNum);
            }
        }
//        if(true) {
//        	retMap.put("retStr", "pledged");
//        	retMap.put("classType", "货种小类1");
//        }
        return retMap;
    }

    /**
     * 校验库存量是否小于输入的出库件数+动态质押量
     *
     * @param getList
     * @param sku
     * @param valNum
     * @return
     */
    private int check(List<Map<String, Object>> getList, String sku, Integer valNum) {
        if (getList != null && getList.size() > 0 && sku != null && valNum != null) {
            Map<String, Object> map = null;
            for (int i = 0; i < getList.size(); i++) {
                map = getList.get(i);
                if (sku.equals(map.get("SKU_ID").toString())) {
                    Double sum = Double.valueOf(map.get("NOW_PIECE").toString());//库存量
                    Double tunm = Double.valueOf(map.get("TNUM").toString());//动态质押量
                    Double punm = Double.valueOf(valNum);//出库量
                    if (sum < tunm + punm) {
                        return i;
                    }
                }
            }
        }
        return 0;
    }

    public List<BisLoadingOrder> findList(Map<String, Object> params) {
        return loadingOrderDao.findBy(params);
    }

    //查找选择了【是最后一车】的出库订单，按照出库订单的明细，按照group by asn、sku
    public List<BisLoadingInfo> getObjList(String outLinkId) {
        return loadingOrderDao.getObjList(outLinkId);
    }

    //

    /**
     * 通过出库订单号获取装载信息
     *
     * @param orderId
     * @return
     */
    public List<Map<String, Object>> getOrderSumNum(String orderId) {
        return loadingOrderDao.getOrderSumNum(orderId);
    }


}
