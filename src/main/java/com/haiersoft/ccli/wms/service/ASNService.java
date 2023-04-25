package com.haiersoft.ccli.wms.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.haiersoft.ccli.common.persistence.Page;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.BaseSkuBaseInfo;
import com.haiersoft.ccli.base.entity.BaseTaxRate;
import com.haiersoft.ccli.base.entity.ExpenseSchemeInfo;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.base.service.ExpenseSchemeInfoService;
import com.haiersoft.ccli.base.service.SkuInfoService;
import com.haiersoft.ccli.base.service.TaxRateService;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.cost.entity.BisStandingBook;
import com.haiersoft.ccli.cost.service.AsnActionLogService;
import com.haiersoft.ccli.cost.service.StandingBookService;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.dao.ASNDao;
import com.haiersoft.ccli.wms.dao.AsnActionDao;
import com.haiersoft.ccli.base.dao.ClientDao;
import com.haiersoft.ccli.wms.entity.AsnAction;
import com.haiersoft.ccli.wms.entity.BisAsn;
import com.haiersoft.ccli.wms.entity.BisAsnInfo;
import com.haiersoft.ccli.wms.entity.BisEnterStock;
import com.haiersoft.ccli.wms.entity.BisTransferStock;
import com.haiersoft.ccli.wms.entity.TrayInfo;

@Service
public class ASNService extends BaseService<BisAsn, String> {

    @Autowired
    private ASNDao asnDao;
    @Autowired
    private AsnActionLogService asnActionLogService;
    @Autowired
    private ClientDao clientDao;
    @Autowired
    private AsnActionDao asnActionDao;
    @Autowired
    private ASNInfoService asnInfoService;//ans明细
    @Autowired
    private SkuInfoService skuInfoService;//sku
    @Autowired
    private AsnActionService asnActionService;//ASN计费区间
    @Autowired
    private EnterStockService enterStockService;//入库联系单
    @Autowired
    private ClientService clientService;//客户
    @Autowired
    private TransferService transferService;//货转
    @Autowired
    private StandingBookService standingBookService;//对账单
    @Autowired
    private TaxRateService taxRateService;//税率
    @Autowired
    private ExpenseSchemeInfoService expenseSchemeInfoService;//税率
    
    @Override
    public HibernateDao<BisAsn, String> getEntityDao() {
        return asnDao;
    }

    
    public List<Map<String, Object>> getAanT(String asnId,String state){
    	return asnDao.getAanT(asnId, state);
    }
    /**
     * 生成ASN号：YYMMDD+SEQ(3位循环)
     *
     * @return
     */
    public String getASNId() {
        StringBuffer retASN = new StringBuffer("");
        retASN.append(DateUtils.getDateStr(new Date(), "YYMMdd"));
        int getNum = asnDao.getSequenceId("SEQ_ASN");
        retASN.append(StringUtils.lpadInt(3, getNum));
        return retASN.toString();
    }

    /**
     * 保存ASN基础信息和Info
     *
     * @param obj
     * @return
     */
    @Transactional(readOnly = false)
    public String saveASN(BisAsn obj) {
        String retStr = "error";
        BisAsn getObj = this.get(obj.getAsn());
        //防页面多次点击保存页面
        if (getObj != null) {
            retStr = this.updateASN(obj);
            return retStr;
        }
        /************************ASN基本信息*********************************************/
        BisEnterStock getBisEnterStock = null;
        //判断是货转还是入库 通过联系单标示入库联系单 前缀E 货转联系单 前缀T
        if (obj.getLinkId().indexOf("E") == 0) {
            //根据联系单id获取联系单对象
            getBisEnterStock = enterStockService.get(obj.getLinkId());
            if (getBisEnterStock != null && getBisEnterStock.getLinkId() != null) {
                obj.setBillNum(getBisEnterStock.getItemNum());
                obj.setStockIn(getBisEnterStock.getStockId());
                obj.setStockName(getBisEnterStock.getStockIn());
                obj.setWarehouseId(getBisEnterStock.getWarehouseId());
                obj.setWarehouse(getBisEnterStock.getWarehouse());
            }
            obj.setMark("0");
        } else if (obj.getLinkId().indexOf("T") == 0) {
            //根据货转单id获取货转单对象
            BisTransferStock transferObj = transferService.get(obj.getLinkId());
            if (transferObj != null && transferObj.getTransferId() != null) {
                obj.setStockName(transferObj.getStockIn());
                //根据提单号获取入库联系单号，添加仓库信息
//				getBisEnterStock=null;//TODO 
//				obj.setWarehouseId(getBisEnterStock.getWarehouseId());
//				obj.setWarehouse(getBisEnterStock.getWarehouse());
            }
            obj.setMark("1");
        }
        this.save(obj);
        /****************************ASN明细信息************************************************/
        //保存明细
        String[] sukList = obj.getAddid();
        if (sukList != null && sukList.length > 0) {
            BisAsnInfo asnInfoObj = null;
            BaseSkuBaseInfo skuObj = null;
            Double[] piece = obj.getPiece();//件数集合
            Double[] netWeight = obj.getNetWeight();//总净重集合
            Double[] grossWeight = obj.getGrossWeight();//总毛重集合
            String[] salesNum = obj.getSalesNum();//SALES REF NO集合
            String[] rkNum = obj.getRkNum();//入库号集合
            String[] proTime = obj.getProTime();//入库号集合
            String[] remark1 = obj.getRemark1();//备注集合
            User user = UserUtil.getCurrentUser();
            AsnAction asnActionObj = new AsnAction();
            /****************************ASN计费************************************************/
            //添加计费
            asnActionObj.setStatus("1");//状态（1正常，0取消）
            asnActionObj.setCleanSign("0");//清库状态
            asnActionObj.setAsn(obj.getAsn());
            asnActionObj.setClientId(obj.getStockIn());//客户id
            asnActionObj.setJfClientId(getBisEnterStock.getStockOrgId());//结算单位id
            BaseClientInfo getClient = clientService.get(Integer.valueOf(getBisEnterStock.getStockOrgId()));
            if (getClient != null) {
                asnActionObj.setClientDay(getClient.getCheckDay());//客户计费日期
            }
            //if(正常 || 重收){计费开始=入库日期}else{计费开始=入库日期+1} 入库类型
            if (null != obj.getInboundTime()) {
                if ("1".equals(obj.getIfSecondEnter().trim()) || "2".equals(obj.getIfSecondEnter().trim())) {
                    asnActionObj.setChargeStaDate(obj.getInboundTime());
                } else {
                    asnActionObj.setChargeStaDate(DateUtils.addDay(obj.getInboundTime(), 1));
                }
            }
            //入库联系单
            if ("0".equals(obj.getMark())) {
                asnActionObj.setEnterId(obj.getLinkId());
            } else {//货转联系单
                asnActionObj.setTransferId(obj.getLinkId());
            }
            //填入费用方案
            if (getBisEnterStock != null) {
                asnActionObj.setFeePlanId(getBisEnterStock.getFeeId());
            }
            asnActionObj.setBillCode(getBisEnterStock.getItemNum());//提单号
            AsnAction newAsnAction = null;
            for (int i = 0; i < sukList.length; i++) {
                skuObj = skuInfoService.get(sukList[i]);
                if (skuObj != null && !"".equals(skuObj.getSkuId())) {
                    if (i < piece.length && !"".equals(piece[i])) {
                        asnInfoObj = new BisAsnInfo();
                        asnInfoObj.setAsnId(obj.getAsn());
                        asnInfoObj.setSkuId(sukList[i]);
                        asnInfoObj.setSkuDescribe(skuObj.getProducingArea());
                        asnInfoObj.setCargoState(skuObj.getCargoState());
                        asnInfoObj.setValidityTime(skuObj.getValidityTime());
                        asnInfoObj.setAttribute1(skuObj.getAttribute1());
                        asnInfoObj.setAttribute2(skuObj.getAttribute2());
                        asnInfoObj.setAttribute3(skuObj.getAttribute3());
                        asnInfoObj.setCargoName(skuObj.getCargoName());
                        asnInfoObj.setCargoType(skuObj.getCargoType());
                        asnInfoObj.setTypeSize(skuObj.getTypeSize());
                        asnInfoObj.setPiece(piece[i]);
                        asnInfoObj.setTransferPiece(0.0);
                        asnInfoObj.setIfSecondEnter(Integer.valueOf(obj.getIfSecondEnter()));//冗余入库类型
                        asnInfoObj.setNetWeight(netWeight[i]);
                        asnInfoObj.setGrossWeight(grossWeight[i]);
                        asnInfoObj.setNetSingle(skuObj.getNetSingle());
                        asnInfoObj.setGrossSingle(skuObj.getGrossSingle());
                        asnInfoObj.setUnits(skuObj.getUnits());
                        asnInfoObj.setMscNum(skuObj.getMscNum());
                        asnInfoObj.setLotNum(skuObj.getLotNum());
                        asnInfoObj.setProNum(skuObj.getProNum());
                        asnInfoObj.setSalesNum(salesNum.length >= i + 1 ? salesNum[i] : "");
                        asnInfoObj.setRkNum(rkNum.length >= i + 1 ? rkNum[i] : "");
                        asnInfoObj.setProTime(proTime.length >= i + 1 ? proTime[i] : "");
                        asnInfoObj.setRemark1(remark1.length >= i + 1 ? remark1[i] : "");
                        asnInfoObj.setOperator(user.getName());
                        asnInfoObj.setOperateTime(new Date());
                        asnInfoService.save(asnInfoObj);
                        //添加计费区间
                        newAsnAction = new AsnAction();
                        BeanUtils.copyProperties(asnActionObj, newAsnAction);//复制
                        newAsnAction.setCargoName(skuObj.getCargoName());
                        newAsnAction.setSku(skuObj.getSkuId());
                        asnActionService.save(newAsnAction);
                        asnActionLogService.saveLog(newAsnAction, "1", 0, 0, "ASN保存时产生的ASN区间表记录(入库前)");
                    }

                }
            }
        }
        retStr = "success";
        return retStr;
    }

    @SuppressWarnings("null")
	@Transactional(readOnly = false)
    public String updateASN(BisAsn obj) {
        String retStr = "error";
        if (obj != null && !"".equals(obj.getAsn())) {
            BisAsn getObj = this.get(obj.getAsn());
            /************************ASN基本信息更新*********************************************/
            BisEnterStock getBisEnterStock = null;
            //只有在途状态下才会修改
            if ("1".equals(getObj.getAsnState())) {
                //判断是货转还是入库 通过联系单标示入库联系单 前缀E 货转联系单 前缀T
                if (obj.getLinkId().indexOf("E") == 0) {
                    //根据联系单id获取联系单对象
                    getBisEnterStock = enterStockService.get(obj.getLinkId());
                    if (getBisEnterStock != null && getBisEnterStock.getLinkId() != null) {
                        obj.setBillNum(getBisEnterStock.getItemNum());
                        obj.setStockIn(getBisEnterStock.getStockId());
                        obj.setStockName(getBisEnterStock.getStockIn());
                        obj.setWarehouseId(getBisEnterStock.getWarehouseId());
                        obj.setWarehouse(getBisEnterStock.getWarehouse());
                    }
                    obj.setMark("0");
                } else if (obj.getLinkId().indexOf("T") == 0) {
                    //根据货转单id获取货转单对象
                    BisTransferStock transferObj = transferService.get(obj.getLinkId());
                    if (transferObj != null && transferObj.getTransferId() != null) {
                        obj.setStockName(transferObj.getStockIn());
                        //根据提单号获取入库联系单号，添加仓库信息
                        getBisEnterStock = null;//TODO
                        obj.setWarehouseId(getBisEnterStock.getWarehouseId());
                        obj.setWarehouse(getBisEnterStock.getWarehouse());
                    }
                    obj.setMark("1");
                }
                getObj.setLinkId(obj.getLinkId());
                getObj.setBillNum(obj.getBillNum());
                getObj.setIfSecondEnter(obj.getIfSecondEnter());
                getObj.setCtnNum(obj.getCtnNum());
                getObj.setOrderNum(obj.getOrderNum());
                getObj.setInboundTime(obj.getInboundTime());
                getObj.setStockIn(obj.getStockIn());
                getObj.setStockName(obj.getStockName());
                getObj.setRemark(obj.getRemark());
                getObj.setCreateUser(obj.getCreateUser()!=null?obj.getCreateUser():"");
                getObj.setCreateTime(obj.getCreateTime()!=null?obj.getCreateTime():null);
                this.update(getObj);
            }
            /****************************ASN明细信息更新************************************************/
            //更新明细
            String[] sukList = obj.getAddid();
            BisAsnInfo asnInfoObj = null;
            //删除sku明细信息
            String[] delSku = obj.getDelSkus();
            if (delSku != null && delSku.length > 0) {
                for (int i = 0; i < delSku.length; i++) {
                    asnInfoObj = asnInfoService.findBisAsnInfo(obj.getAsn(), delSku[i]);
                    if (asnInfoObj != null) {
                        asnInfoService.delete(asnInfoObj);
                        //删除ASNACTION表
                        List<PropertyFilter> delAsnAction = new ArrayList<PropertyFilter>();
                        delAsnAction.add(new PropertyFilter("EQS_asn", obj.getAsn()));
                        delAsnAction.add(new PropertyFilter("EQS_sku", delSku[i]));
                        delAsnAction.add(new PropertyFilter("NULLI_num", ""));
                        List<AsnAction> delObjList = asnActionService.findhz(delAsnAction);
                        if (!delObjList.isEmpty()) {
                            for (AsnAction delObj : delObjList) {
                                asnActionService.delete(delObj);
                            }
                        }
                    }
                }
            }
            if (sukList != null && sukList.length > 0) {
                BaseSkuBaseInfo skuObj = null;
                Double[] piece = obj.getPiece();//件数集合
                Double[] netWeight = obj.getNetWeight();//总净重集合
                Double[] grossWeight = obj.getGrossWeight();//总毛重集合
                String[] salesNum = obj.getSalesNum();//SALES REF NO集合
                String[] rkNum = obj.getRkNum();//入库号集合
                String[] proTime = obj.getProTime();//生产日期集合
                String[] remark1 = obj.getRemark1();//备注集合
                String[] hsCode = obj.getHsCode();//hs编码
                String[] hsItemname = obj.getHsItemname();//海关品名
                String[] accountBook = obj.getAccountBook();// 商品账册序号

                
                User user = UserUtil.getCurrentUser();
                for (int i = 0; i < sukList.length; i++) {
                    //获取到明细进行更新
                    asnInfoObj = asnInfoService.findBisAsnInfo(obj.getAsn(), sukList[i]);
                    if (asnInfoObj != null) {
                        if (hsCode != null && i < hsCode.length && !"".equals(hsCode[i])) {
                        	asnInfoObj.setHsCode(hsCode[i]);
        				}else{
        					asnInfoObj.setHsCode(null);
        				}
                        if (hsItemname != null && i < hsItemname.length && !"".equals(hsItemname[i])) {
                        	asnInfoObj.setHsItemname(hsItemname[i]);
        				}else {
        					asnInfoObj.setHsItemname(null);
						}

                        if (accountBook != null && i < accountBook.length && !"".equals(accountBook[i])) {
                        	asnInfoObj.setAccountBook(accountBook[i]);
       				    }else {
        					asnInfoObj.setAccountBook(null);
						}
                        if (piece != null && i < piece.length && !"".equals(piece[i])) {
                            asnInfoObj.setPiece(piece[i]);
                            asnInfoObj.setNetWeight(netWeight[i]);
                            asnInfoObj.setGrossWeight(grossWeight[i]);
                          //  asnInfoObj.setHsCode(hsCode[i]);
                          //  asnInfoObj.setHsItemname(hsItemname[i]);
                           // asnInfoObj.setAccountBook(accountBook[i]);
                        }
                        asnInfoObj.setSalesNum(salesNum != null && salesNum.length >= i + 1 ? salesNum[i] : "");
                        asnInfoObj.setRkNum(rkNum != null && rkNum.length >= i + 1 ? rkNum[i] : "");
                        asnInfoObj.setProTime(proTime != null && proTime.length >= i + 1 ? proTime[i] : "");
                        asnInfoObj.setRemark1(remark1 != null && remark1.length >= i + 1 ? remark1[i] : "");
                        asnInfoService.update(asnInfoObj);
                    } else {
                        //获取不到明细信息进行添加操作
                        skuObj = skuInfoService.get(sukList[i]);
                        if (skuObj != null && !"".equals(skuObj.getSkuId())) {
                            asnInfoObj = new BisAsnInfo();
                            asnInfoObj.setAsnId(obj.getAsn());
                            asnInfoObj.setSkuId(sukList[i]);
                            asnInfoObj.setSkuDescribe(skuObj.getProducingArea());
                            asnInfoObj.setCargoState(skuObj.getCargoState());
                            asnInfoObj.setValidityTime(skuObj.getValidityTime());
                            asnInfoObj.setAttribute1(skuObj.getAttribute1());
                            asnInfoObj.setAttribute2(skuObj.getAttribute2());
                            asnInfoObj.setAttribute3(skuObj.getAttribute3());
                            asnInfoObj.setCargoName(skuObj.getCargoName());
                            asnInfoObj.setCargoType(skuObj.getCargoType());
                            asnInfoObj.setTypeSize(skuObj.getTypeSize());
                            asnInfoObj.setPiece(piece[i]);
                            asnInfoObj.setTransferPiece(0.0);
                            asnInfoObj.setIfSecondEnter(Integer.valueOf(obj.getIfSecondEnter()));//冗余入库类型
                            asnInfoObj.setNetWeight(netWeight[i]);
                            asnInfoObj.setGrossWeight(grossWeight[i]);
                            asnInfoObj.setNetSingle(skuObj.getNetSingle());
                            asnInfoObj.setGrossSingle(skuObj.getGrossSingle());
                            asnInfoObj.setUnits(skuObj.getUnits());
                            asnInfoObj.setMscNum(skuObj.getMscNum());
                            asnInfoObj.setLotNum(skuObj.getLotNum());
                            asnInfoObj.setProNum(skuObj.getProNum());
                            asnInfoObj.setSalesNum(salesNum.length >= i + 1 ? salesNum[i] : "");
                            asnInfoObj.setRkNum(rkNum.length >= i + 1 ? rkNum[i] : "");
                            asnInfoObj.setProTime(proTime.length >= i + 1 ? proTime[i] : "");
                            asnInfoObj.setRemark1(remark1.length >= i + 1 ? remark1[i] : "");
                            asnInfoObj.setOperator(user.getName());
                            asnInfoObj.setOperateTime(new Date());
                            
                            if (hsCode != null && i < hsCode.length && !"".equals(hsCode[i])) {
                            	asnInfoObj.setHsCode(hsCode[i]);
            				}else{
            					asnInfoObj.setHsCode(null);
            				}
                            if (hsItemname != null && i < hsItemname.length && !"".equals(hsItemname[i])) {
                            	asnInfoObj.setHsItemname(hsItemname[i]);
            				}else {
            					asnInfoObj.setHsItemname(null);
    						}

                            if (accountBook != null && i < accountBook.length && !"".equals(accountBook[i])) {
                            	asnInfoObj.setAccountBook(accountBook[i]);
           				    }else {
            					asnInfoObj.setAccountBook(null);
    						}
                            asnInfoService.save(asnInfoObj);
                        }
                    }//end if
                }//end for
                /****************************ASN计费************************************************/
                //更新计费
                //只有在途状态下才会修改
                if ("1".equals(getObj.getAsnState())) {
                    AsnAction asnActionObj = new AsnAction();
                    asnActionObj.setStatus("1");
                    asnActionObj.setAsn(obj.getAsn());
                    asnActionObj.setClientId(obj.getStockIn());//客户id
                    asnActionObj.setJfClientId(getBisEnterStock.getStockOrgId());//结算单位id
                    BaseClientInfo getClient = clientService.get(Integer.valueOf(getBisEnterStock.getStockOrgId()));
                    if (getClient != null) {
                        asnActionObj.setClientDay(getClient.getCheckDay());//客户计费日期
                    }
                    //if(正常){计费开始=入库日期}else{计费开始=入库日期+1} 入库类型
                    if (null != obj.getInboundTime()) {
                        if ("1".equals(obj.getIfSecondEnter().trim())) {
                            asnActionObj.setChargeStaDate(obj.getInboundTime());
                        } else {
                            asnActionObj.setChargeStaDate(DateUtils.addDay(obj.getInboundTime(), 1));
                        }
                    } else {
                        asnActionObj.setChargeStaDate(null);
                        asnActionObj.setChargeStaDate(null);
                    }
                    //入库联系单
                    if ("0".equals(obj.getMark())) {
                        asnActionObj.setEnterId(obj.getLinkId());
                    } else {//货转联系单
                        asnActionObj.setTransferId(obj.getLinkId());
                    }
                    //填入费用方案
                    if (getBisEnterStock != null) {
                        asnActionObj.setFeePlanId(getBisEnterStock.getFeeId());
                        asnActionObj.setBillCode(getBisEnterStock.getItemNum());
                    }
                    AsnAction newAsnAction = null;
                    for (int i = 0; i < sukList.length; i++) {
                        skuObj = skuInfoService.get(sukList[i]);
                        newAsnAction = asnActionService.getAsnActionObj(obj.getAsn(), sukList[i]);
                        if (newAsnAction != null) {
                            newAsnAction.setStatus("1");
                            newAsnAction.setClientId(obj.getStockIn());
                            newAsnAction.setJfClientId(getBisEnterStock.getStockOrgId());
                            newAsnAction.setClientDay(asnActionObj.getClientDay());
                            newAsnAction.setChargeStaDate(asnActionObj.getChargeStaDate());
                            newAsnAction.setEnterId(asnActionObj.getEnterId());
                            newAsnAction.setFeePlanId(getBisEnterStock.getFeeId());
                            newAsnAction.setBillCode(getBisEnterStock.getItemNum());
                            newAsnAction.setSku(skuObj.getSkuId());
                            newAsnAction.setCargoName(skuObj.getCargoName());
                            asnActionService.update(newAsnAction);
                            asnActionLogService.saveLog(newAsnAction, "1", 0, 0, "ASN修改保存时发生改变的ASN区间表记录(入库前)");
                        } else {
                            newAsnAction = new AsnAction();
                            BeanUtils.copyProperties(asnActionObj, newAsnAction);//复制
                            newAsnAction.setSku(skuObj.getSkuId());
                            newAsnAction.setCargoName(skuObj.getCargoName());
                            asnActionService.save(newAsnAction);
                            asnActionLogService.saveLog(newAsnAction, "1", 0, 0, "ASN修改时产生的新的ASN区间表记录(入库前)");
                        }
                    }

                }//end 更新计费
            }//end
            //end 更新明细结束

            retStr = "success";
        }
        return retStr;
    }


    /**
     * 根据asnId删除ASN和明细信息
     *
     * @param asnId
     * @return
     */
    public String deleteASN(String asnId) {
        String retStr = "error";
        if (asnId != null && !"".equals(asnId)) {
            asnInfoService.deleteASNInfos(asnId);
            this.delete(asnId);
            retStr = "success";
        }
        return retStr;
    }


    //入库联系单修改后，修改相对应的ASN中的信息
    public void changeAsn(BisEnterStock enterStock) {
        String linkId = enterStock.getLinkId();
        List<BisAsn> bisAsnList = asnDao.findBy("linkId", linkId);
        List<AsnAction> asnActionList = null;
        BisAsn bisAsn = new BisAsn();
        AsnAction asnAction = new AsnAction();
        if (!bisAsnList.isEmpty()) {
            for (int i = 0; i < bisAsnList.size(); i++) {
                bisAsn = bisAsnList.get(i);
                bisAsn.setOrderNum(enterStock.getOrderNum());
                bisAsn.setStockIn(enterStock.getStockId());
                bisAsn.setStockName(enterStock.getStockIn());
                bisAsn.setBillNum(enterStock.getItemNum());
                bisAsn.setIsBonded(enterStock.getIfBonded());
                bisAsn.setWarehouseId(enterStock.getWarehouseId());
                bisAsn.setWarehouse(enterStock.getWarehouse());
                update(bisAsn);
                asnActionList = asnActionDao.findBy("asn", bisAsn.getAsn());
                if (asnActionList.size() != 0) {
                    for (int j = 0; j < asnActionList.size(); j++) {
                        asnAction = asnActionList.get(i);
                        asnAction.setFeePlanId(enterStock.getFeeId());
                        asnAction.setClientId(enterStock.getStockId());
                        BaseClientInfo clientInfo = clientDao.findBy("ids", Integer.valueOf(enterStock.getStockId())).get(0);
                        asnAction.setClientDay(clientInfo.getCheckDay());
                    }
                }//end if(asnActionList.size() != 0)
            }
        }//end if(bisAsnList.size()!=0)
    }

    //判断入库联系单是否已制作了ASN
    public List<BisAsn> ifasn(String linkId) {
        return asnDao.findBy("linkId", linkId);
    }

    //ASN点击完结时计算出入库费用
    public List<Map<String, Object>> crFee(Map<String, Object> params) {
        return asnDao.crFee(params);
    }

    //ASN点击完结时计算分拣费用
    public List<Map<String, Object>> fjFee(Map<String, Object> params) {
        return asnDao.fjFee(params);
    }

    /**
     * 根据提单号获取asn里订单号
     *
     * @param billNum
     * @return
     */
    public String getOrderNo(String billNum) {
        String orderNo = null;
        if (billNum != null) {
            List<Map<String, Object>> getList = asnDao.getOrderNo(billNum);
            if (getList != null && getList.size() > 0) {
                Map<String, Object> getMap = getList.get(0);
                if (getMap != null) {
                    orderNo = getMap.get("ORDER_NUM").toString();
                }
            }
        }
        String regEx = "[`~!@#$%^&*=|':;'/?~！@#￥%……&*‘”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        if (null != orderNo) {
            Matcher m = p.matcher(orderNo);
            return m.replaceAll("").trim();
        } else {
            return orderNo;
        }
    }

    /**
     * 获取ASN实际入库数量
     *
     * @param asnIds
     * @return
     */
    public List<Map<String, Object>> getASNPNum(String asnIds) {
        if (asnIds != null && !"".equals(asnIds)) {
            StringBuffer ids = new StringBuffer();
            String[] getList = asnIds.split(",");
            for (int i = 0; i < getList.length; i++) {
                if (!"".equals(getList[i])) {
                    ids.append("'").append(getList[i]).append("'").append(",");
                }
            }
            return asnDao.getASNPNum(ids.substring(0, ids.length() - 1));
        }
        return null;
    }

    public List<BisAsn> getList(Map<String, Object> params) {
        return asnDao.findBy(params);
    }

    public List<Map<String, Object>> getRkAsnByTime(String[] aa, String[] bb) throws ParseException {
        return asnDao.getRkAsnByTime(aa, bb);
    }

    public List<Map<String, Object>> getinfonum(String asn) {
        return asnDao.getinfonum(asn);
    }

    public Page<BisAsn> getAllASN(Page<BisAsn> page, BisAsn bisAsn) {
        return asnDao.getAllASN(page, bisAsn);
    }
    
    /*
     * 入库作业列表展示ASN
     */
    public Page<BisAsn> getAsnToWork(Page<BisAsn> page, BisAsn bisAsn) {
        return asnDao.getAsnToWork(page, bisAsn);
    }

    public Map<String, String> getPiece1Gross1NetCount(BisAsn bisAsn) {
        return asnDao.getPiece1Gross1NetCount(bisAsn);
    }

	public List<BisAsn> findByF(List<PropertyFilter> asnF) {
		return asnDao.find(asnF);
	}

	
	
	/**
     * @param getObj
     * @param crExpense
     * @return
     * @Description 回库操作时，重新生成一遍出入库费
     * @author PYL
	 * @throws ParseException 
     */
    @ResponseBody
    public String crFeeForBack(BisAsn getObj,TrayInfo tray,String userName) throws ParseException {
    	Date now = new Date();
        String asn = getObj.getAsn();
        //生成费用明细
        BisEnterStock enterStock = enterStockService.get(getObj.getLinkId());
        Map<String, Object> cr = new HashMap<String, Object>();
        String crFeeId = enterStock.getFeeId();
        cr.put("schemeNum", crFeeId);
        cr.put("feeType", "3");
        List<ExpenseSchemeInfo> crList = expenseSchemeInfoService.getFeeByName(cr);
        if (!crList.isEmpty()) {
        	ExpenseSchemeInfo exInfo = crList.get(0);
	        String linkId = getObj.getLinkId();
	        Map<String, Object> params = new HashMap<String, Object>();
	        params.put("asn", asn);
	        Double unit = exInfo.getUnit();
	        Double price = 0d;
	        Double jsNum = 0.00;
	        switch (exInfo.getBilling()) {
	            case "1":
	                price = unit * tray.getNowPiece();
	                jsNum = tray.getNowPiece().doubleValue();
	                break;
	            case "2":
	                price = unit * tray.getGrossWeight();
	                jsNum = tray.getGrossWeight() / 1000;
	                break;
	            case "3":
	                price = unit * tray.getNetWeight();
	                jsNum = tray.getNetWeight() / 1000;
	                break;
	        }
	        // 将费用加入台账表
	        BisStandingBook standingBook = new BisStandingBook();
	        standingBook.setStandingNum(standingBookService.getSequenceId());
	        standingBook.setCustomsNum(tray.getStockIn());
	        standingBook.setCustomsName(tray.getStockName());
	        standingBook.setBillNum(getObj.getBillNum());
	        standingBook.setAsn(asn);
	        standingBook.setLinkId(linkId);
	        standingBook.setFeePlan(exInfo.getSchemeNum());
	        standingBook.setFeeCode(exInfo.getFeeCode());
	        standingBook.setFeeName(exInfo.getFeeName());
	        standingBook.setCrkSign(1);
	        standingBook.setStorageDtae(DateUtils.dateToDate(now));
	        BaseClientInfo getClient = clientService.get(Integer.valueOf(tray.getStockIn()));
	        if (null != getClient) {
	            if (null != getClient.getCheckDay()) {
	                standingBook.setBillDate(DateUtils.ifBillDay(now, getClient.getCheckDay()));
	            }
	        }
	        standingBook.setIfReceive(1);
	        standingBook.setNum(jsNum);
	        standingBook.setPrice(unit);
	        standingBook.setReceiveAmount(price / 1000);
	        standingBook.setChargeDate(now);
	        standingBook.setCostDate(now);
	        standingBook.setInputPerson(userName);
	        standingBook.setInputDate(now);
	        standingBook.setFillSign(0);
	        standingBook.setCurrency(exInfo.getCurrency());
	        BaseTaxRate taxRate = taxRateService.getTaxByC(exInfo.getCurrency());
	        standingBook.setExchangeRate(taxRate.getExchangeRate());
	        standingBook.setExamineSign(0);
	        standingBook.setBisType("3");
	        standingBook.setShouldRmb(taxRate.getExchangeRate() * price / 1000);
	        standingBook.setReconcileSign(0);
	        standingBook.setSettleSign(0);
	        standingBook.setSplitSign(0);
	        standingBook.setRemark("因回库生成asn的出入库费，装车单号：");
	        standingBook.setContactType(1);
	        standingBook.setBoxSign(0);
	        standingBook.setShareSign(0);
	        standingBook.setPaySign("0");
	        standingBook.setChargeSign("0");
	        standingBook.setRealAmount(0.00);
	        standingBook.setRealRmb(0.00);
	        standingBook.setStandingCode(StringUtils.numToCode(String.valueOf(standingBook.getStandingNum()), now));
	        standingBookService.save(standingBook);
	        return SUCCESS;
        }else{
        	return "false";
        }
    }
    /**
     * 修改箱号
     * @param ctnNumNew
     * @param ctnNumOld
     * @param linkId
     * @return
     */
	public String updateASNforNewCtnNum(String ctnNumNew,String ctnNumOld,String linkId){
		return asnDao.updateASNforNewCtnNum(ctnNumNew,ctnNumOld,linkId); 
	}
    
    
 
}
