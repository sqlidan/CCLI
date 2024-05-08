package com.haiersoft.ccli.api.service;

import com.haiersoft.ccli.api.dao.FreightInquiryDao;
import com.haiersoft.ccli.api.entity.ApiPledge;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.ApprovalBulkCommDao;
import com.haiersoft.ccli.wms.entity.AuditRecord;
import com.haiersoft.ccli.wms.entity.BaseClientPledge;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import com.haiersoft.ccli.wms.service.ClientPledgeService;
import com.haiersoft.ccli.wms.service.PledgeComfirmService;
import com.haiersoft.ccli.wms.service.TrayInfoService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ApprovalBulkCommService extends BaseService<AuditRecord, Integer> {

    @Autowired
    private ApprovalBulkCommDao approvalBulkCommDao;
    @Autowired
    private FreightInquiryDao freightInquiryDao;
    @Autowired
    private TrayInfoService trayInfoService;
    @Autowired
    PledgeComfirmService pledgeComfirmService;
    @Autowired
    ApiPledgeService apiPledgeService;
    @Autowired
    ClientPledgeService clientPledgeService;

    private static final Logger log = LoggerFactory.getLogger(ApprovalBulkCommService.class);

    @Override
    public HibernateDao<AuditRecord, Integer> getEntityDao() {
        return approvalBulkCommDao;
    }

    public Page<AuditRecord> pageList(Page<AuditRecord> page, AuditRecord entity) {
        return approvalBulkCommDao.pageList(page, entity);
    }

    public List<Map<String, Object>> findInfoById(String id) {
        return approvalBulkCommDao.findInfoById(id);
    }

    public String approvedPass(String operId) {
        //主表数据
        List<Map<String, Object>> auditData = approvalBulkCommDao.findAudit(operId);
        String operType = auditData.get(0).get("OPER_TYPE") == null ? "" : auditData.get(0).get("OPER_TYPE").toString();
        if ("ZYOper".equals(operType)) {
            //实际操作 质押
            //从表数据
            List<Map<String, Object>> detailInfo = approvalBulkCommDao.findInfoById(operId);
            for (Map<String, Object> detail : detailInfo) {
                //传入的质押件数和重量
                int pledgePiece = 0;
                double pledgeGrossWeight = 0.0;
                try {
                    pledgePiece = Integer.parseInt(detail.get("OPER_QTY").toString());
                    pledgeGrossWeight = Double.parseDouble(detail.get("OPER_WEIGHT").toString());
                } catch (Exception e) {
                    return "";
                }
                String goodsID = detail.get("TRAY_ID").toString();
                TrayInfo trayInfo = trayInfoService.get(Integer.valueOf(goodsID.trim()));

                // 修改库存表的质押件数/重量字段
                // 如果当前库存表中的可质押件数/重量为空时，即之前没有做过质押，则可质押件数/重量为本次质押的件数/重量
                if (null == trayInfo.getPledgePiece() || null == trayInfo.getPledgeGrossWeight()) {
                    //修改库存表的  质押件数、质押重量
                    trayInfo.setPledgePiece(pledgePiece);
                    trayInfo.setPledgeGrossWeight(Double.parseDouble(detail.get("OPER_WEIGHT").toString()));
                }
                //如果不为空时,质押数量等于之前的质押数量加本次质押数量
                else {
                    trayInfo.setPledgePiece(trayInfo.getPledgePiece() + pledgePiece);
                    trayInfo.setPledgeGrossWeight(trayInfo.getPledgeGrossWeight() + pledgeGrossWeight);
                }
                trayInfo.setIfTransfer("3");
                trayInfoService.update(trayInfo);
                if (detail.get("API_PLEDGE_ID") != null && !"".equals(detail.get("API_PLEDGE_ID").toString())) {
                    ApiPledge info = pledgeComfirmService.get(detail.get("API_PLEDGE_ID").toString());
                    if (info != null && info.getId() != null) {
                        info.setConfirmStatus(1);
                        info.setComfirmDate(new Date());
                        // 修改质押表 API_PLEDGE
                        pledgeComfirmService.update(info);

                        BaseClientPledge clientPledge = new BaseClientPledge();
                        clientPledge.setBillNum(info.getBillNum());
                        clientPledge.setClientName(info.getCustomerName());
                        clientPledge.setCtnNum(info.getCtnNum());
                        clientPledge.setCuser(info.getAccountId());
                        clientPledge.setSkuId(info.getSku());
                        clientPledge.setNum(info.getPledgeNumber().doubleValue());
                        clientPledge.setCodeNum(info.getTrendId());
                        clientPledge.setClient(info.getCustomerId().toString());
                        clientPledge.setPname(info.getpName());
                        clientPledge.setCtime(new Date());
                        clientPledge.setPtype(1);
                        clientPledge.setPclass(2);
                        clientPledge.setNetWeight(info.getPledgeWeight());
                        clientPledge.setWarehouse(info.getWareHouse());
                        clientPledge.setWarehouseId(info.getWareHouseId());
                        clientPledge.setEnterState(info.getEnterState());
                        clientPledge.setTrack("1");
                        clientPledgeService.save(clientPledge);
                    }
                }
            }
        } else if ("JYOper".equals(operType)) {
            //实际操作 解押
            //从表数据
            List<Map<String, Object>> detailInfo = approvalBulkCommDao.findInfoById(operId);
            for (Map<String, Object> detail : detailInfo) {
                Integer pledgedRelatedNum = 0;
                Integer pledgedRelatedWeight = 0;
                if (detail.get("API_PLEDGE_ID") != null && !"".equals(detail.get("API_PLEDGE_ID").toString())) {
                    ApiPledge info = pledgeComfirmService.get(detail.get("API_PLEDGE_ID").toString());
                    if (info != null) {
                        ApiPledge pledged = apiPledgeService.findUniqueByTrendId(info.getRelatedTrendId());

                        // 如果解押数量大于原质押数量
                        if (info.getPledgeNumber() > pledged.getPledgeNumber()) {
                            return "解押数量或重量超出当前质押数量或重量";
                        }

                        //解押数量不能超出trend_id的数量
                        HashMap<String, Object> pledgedRelatedMap = apiPledgeService.countRelatedTrendIdByRelatedTrendId(info.getRelatedTrendId());
                        pledgedRelatedNum = Integer.valueOf(pledgedRelatedMap.get("sumNum").toString());
                        pledgedRelatedWeight = Integer.valueOf(pledgedRelatedMap.get("sumWeight").toString());
                        if (info.getPledgeNumber() > pledgedRelatedNum) {
                            return "本次解押数量超出限制";
                        }
                        info.setConfirmStatus(1);
                        info.setComfirmDate(new Date());
                        apiPledgeService.update(info);

                        BaseClientPledge clientPledge = new BaseClientPledge();
                        clientPledge.setBillNum(info.getBillNum());
                        clientPledge.setClientName(info.getCustomerName());
                        clientPledge.setCtnNum(info.getCtnNum());
                        clientPledge.setCuser(info.getAccountId());
                        clientPledge.setSkuId(info.getSku());
                        clientPledge.setNum(0 - (pledgedRelatedNum.doubleValue() - info.getPledgeNumber().doubleValue()));
                        clientPledge.setCodeNum(info.getTrendId());
                        clientPledge.setClient(info.getCustomerId().toString());
                        clientPledge.setPname(info.getpName());
                        clientPledge.setCtime(new Date());
                        clientPledge.setPtype(1);
                        clientPledge.setPclass(2);
                        clientPledge.setNetWeight(0 - (pledgedRelatedWeight.doubleValue() - info.getPledgeWeight()));
                        clientPledge.setWarehouse(info.getWareHouse());
                        clientPledge.setWarehouseId(info.getWareHouseId());
                        clientPledge.setEnterState(info.getEnterState());
                        clientPledge.setTrack("1");
                        clientPledgeService.save(clientPledge);
                    }
                }

                TrayInfo trayInfo = trayInfoService.get(Integer.parseInt(detail.get("TRAY_ID").toString()));
                Integer trayInfoPledgePiece = 0;
                if (null == trayInfo.getPledgePiece()) {
                    trayInfoPledgePiece = trayInfo.getNowPiece();
                } else {
                    trayInfoPledgePiece = trayInfo.getPledgePiece();
                }
                // 库存表可解押数量计算
                // 如果解押数量大于库存表中的 质押数量
                if (Integer.parseInt(detail.get("OPER_QTY").toString()) > trayInfoPledgePiece) {
                    return "解押数量或重量超出当前质押数量或重量";
                }


                trayInfo.setPledgePiece(trayInfo.getPledgePiece() - (pledgedRelatedNum - Integer.parseInt(detail.get("OPER_QTY").toString())));
                trayInfo.setPledgeGrossWeight(trayInfo.getPledgeGrossWeight() - (pledgedRelatedWeight.doubleValue() - Integer.parseInt(detail.get("OPER_WEIGHT").toString())));

                // 如果库存表记录中的质押件数为0时，设置IfTransfer标志为0正常
                if (0 == trayInfo.getPledgePiece()) {
                    trayInfo.setIfTransfer("0");
                }
                trayInfoService.update(trayInfo);
            }
        }
        //向港云仓推送审核同意


        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date mkDd = new Date();
        //插入日志表数据，表示通过
        Map<String, Object> logParams = new HashMap<>();
        String uuid = UUID.randomUUID().toString();
        logParams.put("ID", uuid.replace("-", ""));
        logParams.put("operaCode", operId);
        logParams.put("CustCode", auditData.get(0) == null ? "" : auditData.get(0).get("CUSTOMER_CODE").toString());
        logParams.put("CustName", auditData.get(0) == null ? "" : auditData.get(0).get("CUSTOMER_NAME").toString());
        logParams.put("operaType", "PASS");
        logParams.put("params", "");
        logParams.put("reponse", "");
        logParams.put("reqDd", sdf2.format(mkDd));
        int resLogBulk = freightInquiryDao.addLogBulk(logParams);
        if (resLogBulk > 0) {
        } else {
            log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
        }

        logParams.put("state", "3");
        logParams.put("reason", "");
        //修改操作记录主表
        int resAudit = approvalBulkCommDao.updateAudit(logParams);
        if (resAudit > 0) {
        } else {
            log.error(logParams + "---AUDIT_RECORD 保存错误---");
        }
        return "success";
    }

    public String auditReject(String operIds) {
        String operId = operIds.split(":")[0];
        String reason = operIds.split(":")[1];
        List<Map<String, Object>> auditData = approvalBulkCommDao.findAudit(operId);
        //向港云仓推送审核拒绝


        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date mkDd = new Date();
        //插入日志表数据，表示通过
        Map<String, Object> logParams = new HashMap<>();
        String uuid = UUID.randomUUID().toString();
        logParams.put("ID", uuid.replace("-", ""));
        logParams.put("operaCode", operId);
        logParams.put("CustCode", auditData.get(0) == null ? "" : auditData.get(0).get("CUSTOMER_CODE").toString());
        logParams.put("CustName", auditData.get(0) == null ? "" : auditData.get(0).get("CUSTOMER_NAME").toString());
        logParams.put("operaType", "REFUSE");
        logParams.put("params", "");
        logParams.put("reponse", "");
        logParams.put("reqDd", sdf2.format(mkDd));
        int resLogBulk = freightInquiryDao.addLogBulk(logParams);
        if (resLogBulk > 0) {
        } else {
            log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
        }

        logParams.put("state", "4");
        logParams.put("reason", reason);
        //修改操作记录主表
        int resAudit = approvalBulkCommDao.updateAudit(logParams);
        if (resAudit > 0) {
        } else {
            log.error(logParams + "---AUDIT_RECORD 保存错误---");
        }
        return "success";
    }

    public List<Map<String, Object>> customerQuery(Map<String, String> params) {
        //客户查询
        List<Map<String, Object>> customerList = approvalBulkCommDao.customerQuery(params);
        return customerList;
    }

    public List<Map<String, Object>> typeQuery(Map<String, String> params) {
        //货种查询
        if (!StringUtils.isNotEmpty(params.get("typeName") == null ? "" : params.get("typeName"))) {
            return null;
        }
        if (!StringUtils.isNotEmpty(params.get("className") == null ? "" : params.get("className"))) {
            return null;
        }
        if (!StringUtils.isNotEmpty(params.get("cargoState") == null ? "" : params.get("cargoState"))) {
            return null;
        }

        List<Map<String, Object>> typeList = approvalBulkCommDao.typeQuery(params);
        return typeList;
    }

}
