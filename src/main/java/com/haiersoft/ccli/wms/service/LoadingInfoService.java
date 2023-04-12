package com.haiersoft.ccli.wms.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haiersoft.ccli.base.dao.ClientDao;
import com.haiersoft.ccli.base.dao.SkuInfoDao;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.BaseLoadingSQL;
import com.haiersoft.ccli.base.entity.BaseSkuBaseInfo;
import com.haiersoft.ccli.base.service.LoadingSQLService;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.cost.service.AsnActionLogService;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.dao.AsnActionDao;
import com.haiersoft.ccli.wms.dao.InspectStockDAO;
import com.haiersoft.ccli.wms.dao.LoadingInfoDao;
import com.haiersoft.ccli.wms.dao.LoadingOrderDao;
import com.haiersoft.ccli.wms.dao.OutStockDao;
import com.haiersoft.ccli.wms.dao.TrayInfoDao;
import com.haiersoft.ccli.wms.entity.AsnAction;
import com.haiersoft.ccli.wms.entity.BisDismantleTray;
import com.haiersoft.ccli.wms.entity.BisLoadingInfo;
import com.haiersoft.ccli.wms.entity.BisLoadingOrder;
import com.haiersoft.ccli.wms.entity.BisLoadingOrderInfo;
import com.haiersoft.ccli.wms.entity.BisOutStock;
import com.haiersoft.ccli.wms.entity.SplitStockModel;
import com.haiersoft.ccli.wms.entity.TrayInfo;

/**
 * @author Connor.M
 * @ClassName: LoadingInfoService
 * @Description: 装车单  service
 * @date 2016年3月11日 上午10:40:58
 */
@Service
public class LoadingInfoService extends BaseService<BisLoadingInfo, Integer> {

    @Autowired
    private LoadingInfoDao loadingInfofDao;
    @Autowired
	private AsnActionLogService asnActionLogService;
    @Autowired
    private LoadingOrderService loadingOrderService;//订单

    @Autowired
    private LoadingOrderInfoService loadingOrderInfoService;//订单明细

    @Autowired
    private LoadingSQLService loadingSQLService;//策略sql
    @Autowired
    private InspectStockDAO inspectStockDao;//库存明细
    @Autowired
    private TrayInfoService trayInfoService;//库存明细
    @Autowired
    private TrayInfoDao trayInfoDao;//库存明细
    @Autowired
    private DismantleTrayService dismantleTrayService;//拆托记录
    @Autowired
	private LoadingOrderDao loadingOrderDao;
    @Autowired
	private OutStockDao outStockDao;
    @Autowired
	private ClientDao clientDao;
    @Autowired
	private SkuInfoDao skuDao;
    @Autowired
	private AsnActionDao asnActionDao;
    @Autowired
	private AsnActionService asnActionService;
    @Override
    public HibernateDao<BisLoadingInfo, Integer> getEntityDao() {
        return loadingInfofDao;
    }

    /**
     * 生成装车单号：SEQ(8位)
     *
     * @return
     */
    public String getTraId() {
        int getNum = loadingInfofDao.getSequenceId("SEQ_LOADING_TRUCK_NUM");
        return StringUtils.lpadInt(8, getNum);
    }

    /**
     * 根据订单编号获取装车单对象集合
     *
     * @param ordCode 订单编号
     * @return
     */
    public List<BisLoadingInfo> getLoadingInfoIfOrd(String ordCode) {
        return loadingInfofDao.getLoadingInfoIfOrd(ordCode);
    }

    /**
     * 根据出库订单编号，出库策略生成装车单
     *
     * @param ordCode //订单编号
     * @param clCode  //策略编号
     */
    @Transactional(readOnly = false)
    public synchronized Map<String, Object> createTruck(String ordCode, String clCode) {//TODO BUG
        Map<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("endStr", "error");
        List<BisLoadingInfo> checkList = loadingInfofDao.findBy("loadingPlanNum", ordCode);
        if(checkList.isEmpty()){
	        if (ordCode != null && !"".equals(ordCode) && clCode != null && !"".equals(clCode)) {
	            boolean isClear = deleteClearTruck(ordCode);
	            if (false == isClear) {
	                return retMap;
	            }
	            //获取出库联系单对象
	            BisLoadingOrder loadingOrder = loadingOrderService.get(ordCode);
	            //获取订单明细
	            List<Map<String, Object>> getOrdList = loadingOrderInfoService.findInfoList(ordCode);
	            if (loadingOrder != null && getOrdList != null && getOrdList.size() > 0) {
	                String ordId = null;//订单号
	                String outId = null;//出库联系单号
	                String sqlIf = "";
	                String sqlOrd = "FLOOR_NUM,ROOM_NUM,substr(AREA_NUM,1,2),substr(AREA_NUM,3,2) desc,substr(AREA_NUM,5,2) desc,NOW_PIECE asc";
	                //根据策略id获取策略sql
	                if (Integer.valueOf(clCode) > 0) {
	                    BaseLoadingSQL getSQL = loadingSQLService.get(Integer.valueOf(clCode));
	                    sqlIf = getSQL.getIfStr();
	                    sqlOrd = getSQL.getOrdStr();
	                }
	                String stockId = null;//存货方id
	                String billNum = null;//提单号
	                String ctnNum = null;//厢号
	                String asn = null;//asn
	                String skuNum = null;//sku
	                String enterState = null;//入库状态 0（成品） ；1（货损）
	                Integer allNum = 0;
	                List<Map<String, Object>> getTrayList = null;
	                StringBuffer trayIds = new StringBuffer();
	                ArrayList<HashMap<String, String>> splitTray = new ArrayList<HashMap<String, String>>();//记录拆托
	                HashMap<String, String> splitMap = null;
	                StringBuffer ids = new StringBuffer();//用于记录id集合
	                for (Map<String, Object> getMap : getOrdList) {
	                    ids = new StringBuffer();
	                    if (ordId == null) {
	                        ordId = getMap.get("LOADING_PLAN_NUM").toString();
	                        outId = getMap.get("OUT_LINK_ID").toString();
	                    }
	                    billNum = getMap.get("BILL_NUM").toString();
	                    ctnNum = getMap.get("CTN_NUM").toString();
	                    asn=getMap.get("ASN")!=null?getMap.get("ASN").toString():"";
	                    skuNum = getMap.get("SKU_ID").toString();
	                    enterState = getMap.get("ENTER_STATE").toString();
	                    if (null == stockId && !"".equals(stockId)) {
	                        stockId = getMap.get("STOCK_ID").toString();
	                    }
	                    allNum = Integer.valueOf(getMap.get("PIECE").toString());
	                    //按策略进行拣货
	                    getTrayList = trayInfoService.findTrayList(loadingOrder.getWarehouseId(), stockId, billNum, ctnNum, asn,skuNum, enterState, sqlIf, sqlOrd);
	                    if (getTrayList != null && getTrayList.size() > 0 && allNum > 0) {
	                        Map<String, Object> trayInfo = null;
	                        for (int i = 0; i < getTrayList.size(); i++) {
	                            trayInfo = getTrayList.get(i);
	                            ids.append(trayInfo.get("ID")).append(",");
	                            allNum = allNum - Integer.valueOf(trayInfo.get("NOW_PIECE").toString());
	                            if (0 > allNum) {
	                                //标示拆托结束,添加需要拆托的数据
	                                //trayIds.append(trayInfo.get("ID")).append(",");
	                                splitMap = new HashMap<String, String>();
	                                splitMap.put("id", trayInfo.get("ID").toString());
	                                splitMap.put("splitnum", String.valueOf(Integer.valueOf(trayInfo.get("NOW_PIECE").toString()) + allNum));
	                                splitMap.put("tcode", trayInfo.get("TRAY_ID").toString());
	                                splitTray.add(splitMap);
	                                break;
	                            } else {
	                                trayIds.append(trayInfo.get("ID")).append(",");
	                            }
	                            if (0 == allNum) {//正好捡完则跳出
	                                break;
	                            }
	                        }
	                    }
	                    //策略拣货后与出库数量不足时进行默认拣货
	                    if (allNum > 0 && !"".equals(sqlIf)) {
	                        sqlOrd = "FLOOR_NUM,ROOM_NUM,substr(AREA_NUM,1,2),substr(AREA_NUM,3,2) desc,substr(AREA_NUM,5,2) desc,NOW_PIECE asc";
	                        //substr(ROOM_NUM,0,2),substr(t.cargo_location,3,2) desc,substr(t.cargo_location,6,2) desc
	                        getTrayList = trayInfoService.findNoIfTrayList(loadingOrder.getWarehouseId(), stockId, billNum, ctnNum, skuNum, enterState, sqlOrd, ids.toString());
	                        Map<String, Object> trayInfo = null;
	                        for (int i = 0; i < getTrayList.size(); i++) {
	                            trayInfo = getTrayList.get(i);
	                            allNum = allNum - Integer.valueOf(trayInfo.get("NOW_PIECE").toString());
	                            if (0 > allNum) {
	                                //标示拆托结束,添加需要拆托的数据
	                                //trayIds.append(trayInfo.get("ID")).append(",");
	                                splitMap = new HashMap<String, String>();
	                                splitMap.put("id", trayInfo.get("ID").toString());
	                                splitMap.put("splitnum", String.valueOf(Integer.valueOf(trayInfo.get("NOW_PIECE").toString()) + allNum));
	                                splitMap.put("tcode", trayInfo.get("TRAY_ID").toString());
	                                splitTray.add(splitMap);
	                                break;
	                            } else {
	                                trayIds.append(trayInfo.get("ID")).append(",");
	                            }
	                            if (0 == allNum) {//正好捡完则跳出
	                                break;
	                            }
	                        }
	                    } else {
	                        //拣货策略完成拣货跳出当前循环
	                        continue;
	                    }
	                    //拣货后数量大于0，则库存不足
	                    if (allNum > 0) {
	                        retMap.put("billNum", billNum);
	                        retMap.put("ctnNum", ctnNum);
	                        retMap.put("skuNum", skuNum);
	                        retMap.put("state", enterState);
	                        return retMap;
	                    }
	
	                }//end for
	
	                if (splitTray.size() == 0) {
	                    //执行保存操作
	                    Map<String, Object> saveMap = this.saveLoadingInfo(trayIds.toString(), ordId, outId);
	                    if ("success".equals(saveMap.get("endStr"))) {
	                        retMap.put("endStr", "success");
	                        retMap.put("tNum", saveMap.get("tNum"));
	//						loadingInfofDao.insertRemark(ordId,saveMap.get("tNum").toString());
	                    } else {
	                        retMap.put("endStr", "erroe");
	                    }
	                } else {
	                    retMap.put("endStr", "success");
	                    retMap.put("trayIds", trayIds);
	                    retMap.put("splitTray", splitTray);
	                }
	                return retMap;
	            } else {
	                //订单对象不存在
	            }
	        }
        }
        return retMap;
    }

    /**
     * 保存装车单信息
     *
     * @param trayIds 库存明细id
     * @param ordId   订单编号
     * @param outId   出车联系单编号
     * @return
     */
    @Transactional(readOnly = false)
    public Map<String, Object> saveLoadingInfo(String trayIds, String ordId, String outId) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("endStr", "error");
        if (trayIds != null && !"".equals(trayIds)) {
            String ids = trayIds.substring(0, trayIds.length() - 1);
            String[] idList = ids.split(",");
            BisLoadingOrder getLoadingOrder = loadingOrderService.get(ordId);
            if (idList != null && idList.length > 0) {
                TrayInfo trayInfoObj = null;
                BisLoadingInfo saveObj = null;
                String sCode = this.getTraId();//装车单id
                User user = UserUtil.getCurrentUser();
                Date nowDate = new Date();
                for (int i = 0; i < idList.length; i++) {
                    //根据id获取库存明细
                    trayInfoObj = trayInfoService.get(Integer.valueOf(idList[i]));
                    if (trayInfoObj != null && trayInfoObj.getId() > 0) {
                        //托盘上货品数量要大于0
                        if (trayInfoObj.getNowPiece() > 0) {
                            saveObj = new BisLoadingInfo();
                            saveObj.setLoadingTruckNum(sCode);
                            saveObj.setTrayId(trayInfoObj.getTrayId());
                            saveObj.setLoadingPlanNum(ordId);
                            saveObj.setOutLinkId(outId);
                            saveObj.setBillNum(trayInfoObj.getBillNum());
                            saveObj.setCtnNum(trayInfoObj.getCtnNum());
                            saveObj.setSkuId(trayInfoObj.getSkuId());
                            BaseSkuBaseInfo skuObj=skuDao.find(trayInfoObj.getSkuId());
                            saveObj.setMscNum(skuObj.getMscNum()!=null?skuObj.getMscNum():"");
                            saveObj.setLotNum(skuObj.getLotNum()!=null?skuObj.getLotNum():"");
                            saveObj.setTypeSize(skuObj.getTypeSize()!=null?skuObj.getTypeSize():"");
                            saveObj.setLoadingState("0");
                            saveObj.setPiece(trayInfoObj.getNowPiece());
                            saveObj.setCargoName(trayInfoObj.getCargoName());
                            saveObj.setCargoType(trayInfoObj.getCargoType());
                            saveObj.setAsnId(trayInfoObj.getAsn());
                            if (trayInfoObj.getNetSingle() != null) {
                                saveObj.setNetWeight(trayInfoObj.getNowPiece() * trayInfoObj.getNetSingle());//总净重
                            } else {
                                saveObj.setNetWeight(trayInfoObj.getNetWeight());//总净重
                            }
                            if (trayInfoObj.getGrossSingle() != null) {
                                saveObj.setGrossWeight(trayInfoObj.getNowPiece() * trayInfoObj.getGrossSingle());///总毛重
                            } else {
                                saveObj.setGrossWeight(trayInfoObj.getGrossWeight());///总毛重
                            }
                            saveObj.setStockId(trayInfoObj.getStockIn());
                            saveObj.setCargoLocation(trayInfoObj.getCargoLocation());
                            saveObj.setFloorNum(trayInfoObj.getFloorNum());
                            saveObj.setRoomNum(trayInfoObj.getRoomNum());
                            saveObj.setAreaNum(trayInfoObj.getAreaNum());
                            saveObj.setStoreroomNum(trayInfoObj.getStoreroomNum());
                            saveObj.setEnterState(trayInfoObj.getEnterState());
                            if (getLoadingOrder != null) {
                                //装车单添加车牌号
                                saveObj.setCarNo(getLoadingOrder.getCarNum());
                            }
                            if (user != null) {
                                saveObj.setLibraryManager(trayInfoObj.getEnterOperson());//gzq 20160627 修改，装车单库管人员应为入库操作人员（对应手持上架的人）
                                saveObj.setOperator(user.getName());
                            }
                            saveObj.setLibraryOpeTime(nowDate);
                            this.save(saveObj);
                        }
                    }
                }//end for
                getLoadingOrder.setOrderState("2");
                loadingOrderService.update(getLoadingOrder);
                // 执行库存明细数据锁定操作
                trayInfoService.lockTrayList(trayIds);
                retMap.put("endStr", "success");
                retMap.put("tNum", sCode);
            }
        }
        
        return retMap;
    }

    /**
     * 先进行库存明细拆托操作，再执行装车单生成
     *
     * @param model
     */
    @Transactional(readOnly = false)
    public Map<String, Object> createCTLoadingInfo(SplitStockModel model) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("endStr", "erroe");
        if (model.getOrdId() != null && !"".equals(model.getOrdId())) {
            User user = UserUtil.getCurrentUser();
            Date nowDate = new Date();
            //获取订单对象
            BisLoadingOrder ordObj = loadingOrderService.get(model.getOrdId());
            if (ordObj != null && !"".equals(ordObj.getOrderNum())) {
                Integer[] tId = model.gettId();//代拆分库存明细id
                Integer[] spNum = model.getSpNum();//拆分数量 ==要出库数量
                String[] newTCode = model.getNewTCode();//新托盘号
                StringBuffer getNowTids = new StringBuffer();//生成新托库存主键id集合
                if (tId != null && tId.length > 0) {
                    TrayInfo trayObj = null;
                    TrayInfo newObj = null;
                    BisDismantleTray dismantleTray = null;//拆托记录
                    for (int i = 0; i < tId.length; i++) {
                        //根据id获取库存明细对象，进行拆托处理
                        trayObj = trayInfoService.get(tId[i]);
                        if (trayObj != null && trayObj.getId() > 0 && spNum != null && i < spNum.length) {
                            newObj = new TrayInfo();
                            BeanUtils.copyProperties(trayObj, newObj);//复制
                            //生成新托出库数量
                            newObj.setId(null);
                            newObj.setOriginalPiece(spNum[i]);
                            newObj.setNowPiece(spNum[i]);
                            newObj.setTrayId(newTCode[i]);
                            newObj.setNetWeight(spNum[i] * trayObj.getNetSingle());
                            newObj.setGrossWeight(spNum[i] * trayObj.getGrossSingle());
                            newObj.setCargoState("10");//出库中
                            newObj.setIsTruck("1");
                            newObj.setRemovePiece(0);
                            trayInfoService.save(newObj);
                            if (newObj != null && newObj.getId() != null) {
                                getNowTids.append(newObj.getId()).append(",");
                            }
                            //保留托盘 原货主
                            trayObj.setNowPiece(trayObj.getNowPiece() - spNum[i]);
                            trayObj.setNetWeight(trayObj.getNetWeight() - spNum[i] * trayObj.getNetSingle());
                            trayObj.setGrossWeight(trayObj.getGrossWeight() - spNum[i] * trayObj.getGrossSingle());
                            trayObj.setRemovePiece((trayObj.getRemovePiece() != null ? trayObj.getRemovePiece() : 0) + spNum[i]);
                            trayInfoService.update(trayObj);
//                           trayObj = trayInfoService.find("trayId", newTCode[i]);

                            //保存拆托记录
                            dismantleTray = new BisDismantleTray();
                            dismantleTray.setOldTrayCode(trayObj.getTrayId());
                            dismantleTray.setNewTrayCode(newTCode[i]);
                            dismantleTray.setNum(spNum[i]);
                            dismantleTray.setDismantleType("2");
                            if (user != null) {
                                dismantleTray.setDismantleUser(user.getName());
                                dismantleTray.setDismantleTime(nowDate);
                            }
                            dismantleTrayService.save(dismantleTray);
                        }
                    }
                }//end if
                //执行保存操作
                Map<String, Object> saveMap = new HashMap<String, Object>();
                //if(getNowTids.length()>0){
                saveMap = this.saveLoadingInfo(model.getIds() + getNowTids.toString(), ordObj.getOrderNum(), ordObj.getOutLinkId());
                //}
                if ("success".equals(saveMap.get("endStr"))) {
                    retMap.put("endStr", "success");
                    retMap.put("tNum", saveMap.get("tNum"));
//					loadingInfofDao.insertRemark(ordObj.getOrderNum(),saveMap.get("tNum").toString());
                } else {
                    retMap.put("endStr", "erroe");
                }
            }
        }
        return retMap;
    }

    /**
     * 根据出库订单编号判断是否可以生成装车单
     *
     * @param ordCode
     */
    public boolean deleteClearTruck(String ordCode) {
        boolean retB = false;
        if (ordCode != null && !"".equals(ordCode)) {
            //根据订单id解锁锁定库存明细
            trayInfoService.unlockTrayList(ordCode);
            // 根据订单编号删除装车单对象集合
            loadingInfofDao.deleteLoadingInfo(ordCode);
            retB = true;
        }
        return retB;
    }

    /**
     * 根据出库订单编号判断是否可以生成装车单
     *
     * @param ordCode
     */
    public Map<String, Object> isCreateTruck(String ordCode) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("endStr", "error");
        if (ordCode != null && !"".equals(ordCode)) {
            //判断该订单是否已经存在装车单
            List<BisLoadingInfo> getList = loadingInfofDao.getLoadingInfoIfOrd(ordCode);
            if (getList != null && getList.size() > 0) {
                for (BisLoadingInfo obj : getList) {
                    //如果已存在装车单状态有非已分配状态，则不能进行再次生成操作
                    if (!"0".equals(obj.getLoadingState())) {
                        retMap.put("endStr", "herror");
                        return retMap;
                    }
                }
                retMap.put("endStr", "hsuccess");
            } else {
            	List<BisLoadingOrderInfo> infoList = loadingOrderInfoService.getInfoList(ordCode);
            	if(!infoList.isEmpty()){
            		Map<String,String> cacheMap = new HashMap<String,String>();
            		for(BisLoadingOrderInfo infoObj:infoList){
            			Map<String,Object> params = new HashMap<String,Object>();
            			params.put("skuId", infoObj.getSkuId());
            			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
            			list = inspectStockDao.findHebingBy(params);
            			
            			
            			if(!list.isEmpty() && list.get(0).get("AA")!=null){
            				
            				cacheMap.put("skuId", infoObj.getSkuId());
            				cacheMap.put("AA", list.get(0).get("AA").toString());
            			}
            		} //end for
            		if(cacheMap.isEmpty()){
            			retMap.put("endStr", "success");
            		}else{
            			StringBuffer sb=new StringBuffer();
            			sb.append("sku为"+cacheMap.get("skuId")+"的货物取样了"+cacheMap.get("AA"));
            			sb.append("是否继续生成装车单？");
            			retMap.put("endStr", "quyang");
            			retMap.put("message",sb.toString());
            		}
            	}else{
            		retMap.put("endStr", "success");
            	}
            }
        }
        return retMap;
    }


    /*
     * @author  PYL
     *  根据装车单号获取已装车的出库装车单信息
     */
    public List<BisLoadingInfo> getLoadingByNum(Map<String, Object> params) {
        return loadingInfofDao.findBy(params);
    }

    /*
     * @author  PYL
     *  根据装车单号获取所有状态的出库装车单信息
     */
    public List<BisLoadingInfo> getLoadingByNum(String loadingTruckNum) {
        //return loadingInfofDao.findBy("loadingTruckNum",loadingTruckNum);
        //追加库位升序排序
        return loadingInfofDao.getLoadingInfoIfTruckNum(loadingTruckNum);
    }

    /*
     * @author  PYL
     *  根据出库订单号获取所有状态的出库装车单信息
     */
    public List<BisLoadingInfo> getLoadingByOrder(String orderId) {
        return loadingInfofDao.findBy("loadingPlanNum", orderId);
    }

    //装车单打印清单
    public List<BisLoadingInfo> getQingDan(String truckNum) {
        return loadingInfofDao.getQingDan(truckNum);
    }

    /**
     * 根据出库联系单号获取订单标记为最后一车
     *
     * @param outLinkCode
     * @return
     */
    public List<Map<String, Object>> isLastCar(String outLinkCode) {
        return loadingInfofDao.isLastCar(outLinkCode);
    }

    public java.util.List<Map<String, Object>> findallnum(String loadingPlanNum,
                                                          String loadingTruckNum, String outLinkId, String billNum,
                                                          String carNo, String operator, String stockId) {
        return loadingInfofDao.findallnum(loadingPlanNum, loadingTruckNum, outLinkId, billNum, carNo, operator, stockId);
    }

    public List<Map<String, Object>> getLoadingTime(String[] aaa, String[] bbb) throws ParseException {
        return loadingInfofDao.getLoadingTime(aaa, bbb);
    }

    public void insertRemark(String ordId, String tNum) {
        loadingInfofDao.insertRemark(ordId, tNum);
    }

    /**
     * 根据装车单号查询托盘号
     *
     * @param truckNum 装车单号
     * @return
     */
    public List<Map<String, String>> getLoadingOrderInfo(String truckNum) {
        return loadingInfofDao.getOrderInfo(truckNum);
    }

    /**
     * 根据装车单号修改托盘状态
     *
     * @param trayId 托盘号
     */
    public void updateLoadingOrderAndTrayInfo(String trayId) {
        loadingInfofDao.updateLoadingInfoAndTrayInfo(trayId);
    }

	public List<BisLoadingInfo> findao(List<PropertyFilter> loadPFs){
		return loadingInfofDao.find(loadPFs);
	}

	public void updateAsnAction(String loadingCode,String outLinkId,String loadingPlanNum) {
				Date nowDate =new Date();
//				List<PropertyFilter> loadPFs = new ArrayList<PropertyFilter>();
//				loadPFs.add(new PropertyFilter("EQS_loadingTruckNum", loadingCode));
//				List<BisLoadingInfo> infos = loadingInfofDao.find(loadPFs);//获得装车单的信息
					//获得装车单对象
 //					BisLoadingInfo loadingInfo = infos.get(0);
					//统计 装车单数据 数量，净重，毛重
					List<BisLoadingInfo> loadingInfos = loadingInfofDao.getSumLoadingCode(loadingCode);
					
					//修改出库订单
					loadingOrderDao.updateState(loadingPlanNum);
					
					//获得出库联系单数据
					BisOutStock outStock = outStockDao.find(outLinkId);
					
					BaseClientInfo clientInfo = clientDao.find(Integer.parseInt(outStock.getSettleOrgId()));
					
					if(null != loadingInfos && loadingInfos.size() > 0){
						for(BisLoadingInfo info : loadingInfos){
							
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
							actionQK.add(new PropertyFilter("EQS_outLinkId", loadingPlanNum));//出库订单ID
							List<AsnAction> asnActionsQK = asnActionDao.find(actionQK);
							if(null != asnActions && asnActions.size() > 0){
								if(asnActionsQK.isEmpty()){
									//获得 asnAction 区间表 对象
									AsnAction asnAction = asnActions.get(0);
									
									//查看 货转单 是否存在
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
												//int num = action.getNum() - info.getPiece();
//												action.setNum(num);
//												action.setNetWeight(BigDecimalUtil.sub(action.getNetWeight(), info.getNetWeight()));
//												action.setGrossWeight(BigDecimalUtil.sub(action.getGrossWeight(), info.getGrossWeight()));
//												asnActionDao.save(action);
												List<Map<String,Object>> trayInfoList=trayInfoDao.updateAsnAction(info);
												if(!trayInfoList.isEmpty()){
													Integer pieceN = ((BigDecimal)trayInfoList.get(0).get("PIECE")).intValue();
													action.setNum(pieceN);
													Double netN = ((BigDecimal)trayInfoList.get(0).get("NET")).doubleValue();
													action.setNetWeight(netN);
													Double grossN = ((BigDecimal)trayInfoList.get(0).get("GROSS")).doubleValue();
													action.setGrossWeight(grossN);
													asnActionService.update(action);
												}else{
													System.out.print("ASN区间表未找到库存！！"+action.getId().toString());
													action.setNum(0);
													action.setNetWeight(0D);
													action.setGrossWeight(0D);
													asnActionService.update(action);
												}
												asnActionLogService.saveLog(action, "4",  action.getNum()+info.getPiece(), 0-info.getPiece(), "点击完结时，原ASN区间表记录");
												//复制 生成  子  AsnAction的数据
												AsnAction action2  = new AsnAction();
												BeanUtils.copyProperties(action, action2);//复制
												
												action2.setId(null);//主键
												//判断 计费结束日期
												if(action2.getChargeEndDate() == null || nowDate.getTime() < action2.getChargeEndDate().getTime()){
													action2.setChargeEndDate(nowDate);//出库时间
												}
												action2.setNum(info.getPiece());
												action2.setNetWeight(info.getNetWeight());
												action2.setGrossWeight(info.getGrossWeight());
												action2.setTransferId(null);//货转单
												action2.setLinkTransferId(null);
												action2.setOutLinkId(info.getLoadingPlanNum());//出库订单
			
												//只有存货方客户的区间记录才更新出库单号、出库订单、结算客户，其他客户的货转记录保持单号是原来的状态，即原来的数据记录在入库联系单上，不记录在出库联系单上，出库联系单只记录当前收货方的存储费
												if(action2.getClientId().equals(outStock.getStockInId())){
													action2.setOutId(info.getOutLinkId());//出库联系单
													action2.setJfClientId(outStock.getSettleOrgId()); //结算单位ID
													action2.setClientDay(clientInfo.getCheckDay());//结算日
												}
												asnActionService.update(action2);
												asnActionLogService.saveLog(action2, "4", 0, info.getPiece(), "点击完结时，新生成的ASN区间表记录");
											}
										}
									}else{
											//修改 主 AsnAction的数据
											//int num = asnAction.getNum() - info.getPiece();
//											asnAction.setNum(num);
//											asnAction.setNetWeight(BigDecimalUtil.sub(asnAction.getNetWeight(), info.getNetWeight()));
//											asnAction.setGrossWeight(BigDecimalUtil.sub(asnAction.getGrossWeight(), info.getGrossWeight()));
//											asnActionDao.save(asnAction);
											List<Map<String,Object>> trayInfoList=trayInfoDao.updateAsnAction(info);
											if(!trayInfoList.isEmpty()){
												Integer pieceN = ((BigDecimal)trayInfoList.get(0).get("PIECE")).intValue();
												asnAction.setNum(pieceN);
												Double netN = ((BigDecimal)trayInfoList.get(0).get("NET")).doubleValue();
												asnAction.setNetWeight(netN);
												Double grossN = ((BigDecimal)trayInfoList.get(0).get("GROSS")).doubleValue();
												asnAction.setGrossWeight(grossN);
												asnActionService.update(asnAction);
											}else{
												System.out.print("ASN区间表未找到库存！！"+asnAction.getId().toString());
												asnAction.setNum(0);
												asnAction.setNetWeight(0D);
												asnAction.setGrossWeight(0D);
												asnActionService.update(asnAction);
											}
											asnActionLogService.saveLog(asnAction, "4",  asnAction.getNum()+info.getPiece(), 0-info.getPiece(), "点击完结时，原ASN区间表记录");
											//复制生成  子  AsnAction的数据
											AsnAction asnAction2  = new AsnAction();
											BeanUtils.copyProperties(asnAction, asnAction2);//复制
											
											asnAction2.setId(null);//主键
											//判断 计费结束日期
											if(asnAction2.getChargeEndDate() == null || nowDate.getTime() < asnAction2.getChargeEndDate().getTime()){
												asnAction2.setChargeEndDate(nowDate);//出库时间
											}
											asnAction2.setNum(info.getPiece());
											asnAction2.setNetWeight(info.getNetWeight());
											asnAction2.setGrossWeight(info.getGrossWeight());
											asnAction2.setTransferId(null);//货转单
											asnAction2.setLinkTransferId(null);
											asnAction2.setOutLinkId(info.getLoadingPlanNum());//出库订单
											
											//只有存货方客户的区间记录才更新出库单号、出库订单、结算客户，其他客户的货转记录保持单号是原来的状态，即原来的数据记录在入库联系单上，不记录在出库联系单上，出库联系单只记录当前收货方的存储费
											if(asnAction2.getClientId().equals(outStock.getStockInId())){
												asnAction2.setOutId(info.getOutLinkId());//出库联系单
												asnAction2.setJfClientId(outStock.getSettleOrgId()); //结算单位ID
												asnAction2.setClientDay(clientInfo.getCheckDay());//结算日
											}
											asnActionService.save(asnAction2);
											asnActionLogService.saveLog(asnAction2, "4", 0, info.getPiece(), "点击完结时，新生成的ASN区间表记录");
									}
								}
							}
						}
					}
	}

	/*
     * 出库作业列表展示装车单
     */
    public Page<BisLoadingInfo> getLoadingToWork(Page<BisLoadingInfo> page, BisLoadingInfo bisLoadingInfo) {
        return loadingInfofDao.getLoadingToWork(page, bisLoadingInfo);
    }

    /*
     * 重收ASN关联装车单选择页面
     */
    public Page<BisLoadingInfo> getLoadingToAsn(Page<BisLoadingInfo> page, BisLoadingInfo bisLoadingInfo) {
        return loadingInfofDao.getLoadingToAsn(page, bisLoadingInfo);
    }
}
