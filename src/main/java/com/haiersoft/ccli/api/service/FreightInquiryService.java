package com.haiersoft.ccli.api.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.haiersoft.ccli.api.dao.FreightInquiryDao;
import com.haiersoft.ccli.api.entity.ApiPledge;
import com.haiersoft.ccli.api.entity.ExchangeInfo;
import com.haiersoft.ccli.base.entity.BaseSkuBaseInfo;
import com.haiersoft.ccli.base.service.SkuInfoService;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.ApprovalBulkCommDao;
import com.haiersoft.ccli.wms.entity.BaseClientPledge;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import com.haiersoft.ccli.wms.service.ClientPledgeService;
import com.haiersoft.ccli.wms.service.PledgeComfirmService;
import com.haiersoft.ccli.wms.service.TrayInfoService;
import net.sf.json.util.JSONUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FreightInquiryService extends BaseService<TrayInfo, String> {
    @Autowired
    private FreightInquiryDao freightInquiryDao;
    @Autowired
    ApiPledgeService apiPledgeService;
    @Autowired
    private TrayInfoService trayInfoService;
    @Autowired
    private SkuInfoService skuInfoService;
    @Autowired
    ClientPledgeService clientPledgeService;
    @Autowired
    PledgeComfirmService pledgeComfirmService;
    @Autowired
    private ApprovalBulkCommDao approvalBulkCommDao;

    private static final Logger log = LoggerFactory.getLogger(FreightInquiryService.class);

    @Override
    public HibernateDao<TrayInfo, String> getEntityDao() {
        return freightInquiryDao;
    }

    public List<Map<String, Object>> findTrayInfoByStock(Map<String, String> params) {
        //根据客户ID名称查询库存
        if (!StringUtils.isNotEmpty(params.get("CustCode"))) {
            return null;
        }
        if (!StringUtils.isNotEmpty(params.get("CustName"))) {
            return null;
        }

        List<Map<String, Object>> result = freightInquiryDao.findTrayInfoByStock(params);
        return result;
    }

    public Map<String, String> superviseInspect(Map<String, Object> params) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date reqDd = new Date();
        //检验货物是否可以质押，不做实际操作
        Map<String, String> resMes = new HashMap<>();
        if (!StringUtils.isNotEmpty(params.get("CustCode") == null ? "" : params.get("CustCode").toString())) {
            resMes.put("message", "客户编码不正确");
            resMes.put("success", "false");
            resMes.put("operaCode", params.get("operaCode").toString());

            //成功和失败都保存在日志表
            Map<String, Object> logParams = new HashMap<>();
            String uuid = UUID.randomUUID().toString();
            logParams.put("ID", uuid.replace("-", ""));
            logParams.put("operaCode", params.get("operaCode").toString());
            logParams.put("CustCode", params.get("CustCode").toString());
            logParams.put("CustName", params.get("CustName").toString());
            logParams.put("operaType", "ZYCheck");
            logParams.put("params", params.toString());
            logParams.put("reponse", resMes.toString());
            logParams.put("reqDd", sdf.format(reqDd));
            int resLogBulk = freightInquiryDao.addLogBulk(logParams);
            if (resLogBulk > 0) {
            } else {
                log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
            }
            return resMes;
        }
        if (!StringUtils.isNotEmpty(params.get("CustName") == null ? "" : params.get("CustName").toString())) {
            resMes.put("message", "客户名称不正确");
            resMes.put("success", "false");
            resMes.put("operaCode", params.get("operaCode").toString());

            //成功和失败都保存在日志表
            Map<String, Object> logParams = new HashMap<>();
            String uuid = UUID.randomUUID().toString();
            logParams.put("ID", uuid.replace("-", ""));
            logParams.put("operaCode", params.get("operaCode").toString());
            logParams.put("CustCode", params.get("CustCode").toString());
            logParams.put("CustName", params.get("CustName").toString());
            logParams.put("operaType", "ZYCheck");
            logParams.put("params", params.toString());
            logParams.put("reponse", resMes.toString());
            logParams.put("reqDd", sdf.format(reqDd));
            int resLogBulk = freightInquiryDao.addLogBulk(logParams);
            if (resLogBulk > 0) {
            } else {
                log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
            }
            return resMes;
        }
        if (!StringUtils.isNotEmpty(params.get("cargoInfo") == null ? "" : params.get("cargoInfo").toString())) {
            resMes.put("message", "货物明细不能为空");
            resMes.put("success", "false");
            resMes.put("operaCode", params.get("operaCode").toString());

            //成功和失败都保存在日志表
            Map<String, Object> logParams = new HashMap<>();
            String uuid = UUID.randomUUID().toString();
            logParams.put("ID", uuid.replace("-", ""));
            logParams.put("operaCode", params.get("operaCode").toString());
            logParams.put("CustCode", params.get("CustCode").toString());
            logParams.put("CustName", params.get("CustName").toString());
            logParams.put("operaType", "ZYCheck");
            logParams.put("params", params.toString());
            logParams.put("reponse", resMes.toString());
            logParams.put("reqDd", sdf.format(reqDd));
            int resLogBulk = freightInquiryDao.addLogBulk(logParams);
            if (resLogBulk > 0) {
            } else {
                log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
            }
            return resMes;
        }
        //查询当前库存
        Map<String, String> findMap = new HashMap<>();
        String custName = params.get("CustName").toString();
        String custCode = params.get("CustCode").toString();
        Gson gson = new Gson();
        JSONObject jo = JSONObject.parseObject(gson.toJson(params));
        List<JSONObject> goodsList = JSONArray.parseArray(jo.getString("cargoInfo"),JSONObject.class);
        for (int i = 0; i < goodsList.size(); i++) {
            Object obj = JSONObject.toJSON(goodsList.get(i));
            JSONObject goodsInfo = JSONObject.parseObject(obj.toString());
            //货物主键
            if (!StringUtils.isNotEmpty(goodsInfo.get("id") == null ? "" : goodsInfo.get("id").toString())) {
                resMes.put("message", "货物ID不能为空，请检查");
                resMes.put("success", "false");
                resMes.put("operaCode", params.get("operaCode").toString());

                //成功和失败都保存在日志表
                Map<String, Object> logParams = new HashMap<>();
                String uuid = UUID.randomUUID().toString();
                logParams.put("ID", uuid.replace("-", ""));
                logParams.put("operaCode", params.get("operaCode").toString());
                logParams.put("CustCode", params.get("CustCode").toString());
                logParams.put("CustName", params.get("CustName").toString());
                logParams.put("operaType", "ZYCheck");
                logParams.put("params", params.toString());
                logParams.put("reponse", resMes.toString());
                logParams.put("reqDd", sdf.format(reqDd));
                int resLogBulk = freightInquiryDao.addLogBulk(logParams);
                if (resLogBulk > 0) {
                } else {
                    log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
                }
                return resMes;
            }
            if (!StringUtils.isNotEmpty(goodsInfo.get("pledgePiece") == null ? "" : goodsInfo.get("pledgePiece").toString())) {
                resMes.put("message", "质押件数不能为空，请检查");
                resMes.put("success", "false");
                resMes.put("operaCode", params.get("operaCode").toString());

                //成功和失败都保存在日志表
                Map<String, Object> logParams = new HashMap<>();
                String uuid = UUID.randomUUID().toString();
                logParams.put("ID", uuid.replace("-", ""));
                logParams.put("operaCode", params.get("operaCode").toString());
                logParams.put("CustCode", params.get("CustCode").toString());
                logParams.put("CustName", params.get("CustName").toString());
                logParams.put("operaType", "ZYCheck");
                logParams.put("params", params.toString());
                logParams.put("reponse", resMes.toString());
                logParams.put("reqDd", sdf.format(reqDd));
                int resLogBulk = freightInquiryDao.addLogBulk(logParams);
                if (resLogBulk > 0) {
                } else {
                    log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
                }
                return resMes;
            }

            if (!StringUtils.isNotEmpty(goodsInfo.get("pledgeGrossWeight") == null ? "" : goodsInfo.get("pledgeGrossWeight").toString())) {
                resMes.put("message", "质押重量不能为空，请检查");
                resMes.put("success", "false");
                resMes.put("operaCode", params.get("operaCode").toString());

                //成功和失败都保存在日志表
                Map<String, Object> logParams = new HashMap<>();
                String uuid = UUID.randomUUID().toString();
                logParams.put("ID", uuid.replace("-", ""));
                logParams.put("operaCode", params.get("operaCode").toString());
                logParams.put("CustCode", params.get("CustCode").toString());
                logParams.put("CustName", params.get("CustName").toString());
                logParams.put("operaType", "ZYCheck");
                logParams.put("params", params.toString());
                logParams.put("reponse", resMes.toString());
                logParams.put("reqDd", sdf.format(reqDd));
                int resLogBulk = freightInquiryDao.addLogBulk(logParams);
                if (resLogBulk > 0) {
                } else {
                    log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
                }
                return resMes;
            }

            //传入的质押件数和重量
            int pledgePiece = 0;
            double pledgeGrossWeight = 0.0;
            try {
                pledgePiece = Integer.parseInt(goodsInfo.get("pledgePiece").toString());
                pledgeGrossWeight = Double.parseDouble(goodsInfo.get("pledgeGrossWeight").toString());
            } catch (Exception e) {
                resMes.put("message", "质押件数/重量参数有错误");
                resMes.put("success", "false");
                resMes.put("operaCode", params.get("operaCode").toString());

                //成功和失败都保存在日志表
                Map<String, Object> logParams = new HashMap<>();
                String uuid = UUID.randomUUID().toString();
                logParams.put("ID", uuid.replace("-", ""));
                logParams.put("operaCode", params.get("operaCode").toString());
                logParams.put("CustCode", params.get("CustCode").toString());
                logParams.put("CustName", params.get("CustName").toString());
                logParams.put("operaType", "ZYCheck");
                logParams.put("params", params.toString());
                logParams.put("reponse", resMes.toString());
                logParams.put("reqDd", sdf.format(reqDd));
                int resLogBulk = freightInquiryDao.addLogBulk(logParams);
                if (resLogBulk > 0) {
                } else {
                    log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
                }
                return resMes;
            }

            String goodsID = goodsInfo.get("id").toString();
            findMap.put("CustName", custName);
            findMap.put("CustCode", custCode);
            findMap.put("goodsID", goodsID);
            TrayInfo info = trayInfoService.get(Integer.valueOf(goodsID.trim()));
//            TrayInfo info = freightInquiryDao.findTrayInfoByStock(findMap);
            // 判断当前处于什么状态 如果不是0正常 3静态质押的不可以质押
            if (!("0".equals(info.getIfTransfer())) && !("3".equals(info.getIfTransfer()))) {
                resMes.put("message", "该货物状态无法质押，请确认！");
                resMes.put("success", "false");
                resMes.put("operaCode", params.get("operaCode").toString());

                //成功和失败都保存在日志表
                Map<String, Object> logParams = new HashMap<>();
                String uuid = UUID.randomUUID().toString();
                logParams.put("ID", uuid.replace("-", ""));
                logParams.put("operaCode", params.get("operaCode").toString());
                logParams.put("CustCode", params.get("CustCode").toString());
                logParams.put("CustName", params.get("CustName").toString());
                logParams.put("operaType", "ZYCheck");
                logParams.put("params", params.toString());
                logParams.put("reponse", resMes.toString());
                logParams.put("reqDd", sdf.format(reqDd));
                int resLogBulk = freightInquiryDao.addLogBulk(logParams);
                if (resLogBulk > 0) {
                } else {
                    log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
                }
                return resMes;
            }

            // 检查质押数量或重量是否超出库存数量（单条检查）
            if (pledgePiece > info.getNowPiece()) {
                resMes.put("message", "质押数量超过本条库存的数量！");
                resMes.put("success", "false");
                resMes.put("operaCode", params.get("operaCode").toString());

                //成功和失败都保存在日志表
                Map<String, Object> logParams = new HashMap<>();
                String uuid = UUID.randomUUID().toString();
                logParams.put("ID", uuid.replace("-", ""));
                logParams.put("operaCode", params.get("operaCode").toString());
                logParams.put("CustCode", params.get("CustCode").toString());
                logParams.put("CustName", params.get("CustName").toString());
                logParams.put("operaType", "ZYCheck");
                logParams.put("params", params.toString());
                logParams.put("reponse", resMes.toString());
                logParams.put("reqDd", sdf.format(reqDd));
                int resLogBulk = freightInquiryDao.addLogBulk(logParams);
                if (resLogBulk > 0) {
                } else {
                    log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
                }
                return resMes;
            }

            // 检查质押数量或重量是否超出已静态质押数量（多条检查）
            //HashMap<String, Object> pledgedMap = apiPledgeService.countTotalPledgedByTrayInfoId(Integer.valueOf(requestVo.getId()));
            HashMap<String, Object> pledgedMap = apiPledgeService.sumStaticPledgedNum(Integer.valueOf(goodsID));

            Integer pledgedNum = Integer.valueOf(pledgedMap.get("sumNum").toString());
            if (pledgePiece > (info.getNowPiece() - pledgedNum)) {
                resMes.put("message", "质押数量超过本条库存可质押的数量！");
                resMes.put("success", "false");
                resMes.put("operaCode", params.get("operaCode").toString());

                //成功和失败都保存在日志表
                Map<String, Object> logParams = new HashMap<>();
                String uuid = UUID.randomUUID().toString();
                logParams.put("ID", uuid.replace("-", ""));
                logParams.put("operaCode", params.get("operaCode").toString());
                logParams.put("CustCode", params.get("CustCode").toString());
                logParams.put("CustName", params.get("CustName").toString());
                logParams.put("operaType", "ZYCheck");
                logParams.put("params", params.toString());
                logParams.put("reponse", resMes.toString());
                logParams.put("reqDd", sdf.format(reqDd));
                int resLogBulk = freightInquiryDao.addLogBulk(logParams);
                if (resLogBulk > 0) {
                } else {
                    log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
                }
                return resMes;
            }
            BaseSkuBaseInfo bs = skuInfoService.get(info.getSkuId());

            ApiPledge api = new ApiPledge();
//            api.setAccountId(requestVo.getAccountId());
//            api.setTrendId(requestVo.getTrendId());
//            api.setRelatedTrendId(requestVo.getTrendId());
            api.setTrayInfoId(Integer.valueOf(goodsID));
            api.setPledgeNumber(pledgePiece);
            api.setPledgeWeight(pledgeGrossWeight);
            api.setState(1);
            api.setCreateDate(new Date());
            api.setBillNum(info.getBillNum());
            api.setCtnNum(info.getCtnNum());
            api.setCustomerName(info.getStockName());
            api.setCustomerId(Integer.valueOf(info.getStockIn()));
            api.setSku(info.getSkuId());
            api.setItemClass(Integer.valueOf(bs.getClassType()));
            api.setpName(info.getCargoName());
            api.setEnterState(info.getEnterState());
            api.setWareHouse(info.getWarehouse());
            api.setWareHouseId(info.getWarehouseId());

            // 检查质押数量或重量是否超出可质押数量（总量）
            if (!apiPledgeService.pledgeCountCheck(api)) {
                resMes.put("message", "质押数量或质押重量超出可质押数量！");
                resMes.put("success", "false");
                resMes.put("operaCode", params.get("operaCode").toString());

                //成功和失败都保存在日志表
                Map<String, Object> logParams = new HashMap<>();
                String uuid = UUID.randomUUID().toString();
                logParams.put("ID", uuid.replace("-", ""));
                logParams.put("operaCode", params.get("operaCode").toString());
                logParams.put("CustCode", params.get("CustCode").toString());
                logParams.put("CustName", params.get("CustName").toString());
                logParams.put("operaType", "ZYCheck");
                logParams.put("params", params.toString());
                logParams.put("reponse", resMes.toString());
                logParams.put("reqDd", sdf.format(reqDd));
                int resLogBulk = freightInquiryDao.addLogBulk(logParams);
                if (resLogBulk > 0) {
                } else {
                    log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
                }
                return resMes;
            }
        }

        resMes.put("message", "OK");
        resMes.put("success", "success");
        resMes.put("operaCode", params.get("operaCode").toString());
        //写入操作记录表，默认未操作成功  在上方设置货物明细信息拼接
        Map<String, Object> logParams = new HashMap<>();
        String uuid = UUID.randomUUID().toString();

        logParams.put("ID", uuid.replace("-", ""));
        logParams.put("operaCode", params.get("operaCode").toString());
        logParams.put("CustCode", params.get("CustCode").toString());
        logParams.put("CustName", params.get("CustName").toString());
        //操作类型
        logParams.put("operaType", "ZYCheck");
        logParams.put("params", params.toString());
        logParams.put("reponse", resMes.toString());
        logParams.put("reqDd", sdf.format(reqDd));
        //成功和失败都保存在日志表，成功写入操作记录表   日志表：ID(UUID)  操作编码  客户编码  客户名称  操作类型  请求参数  请求时间  请求返回值
        int resLogBulk = freightInquiryDao.addLogBulk(logParams);
        if (resLogBulk > 0) {
        } else {
            log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
        }

        //执行时间、操作状态
        logParams.put("execDd", "");
        logParams.put("operStatue", "2");
        List<Map<String, Object>> auditData = approvalBulkCommDao.findAudit(logParams.get("operaCode").toString());
        if(!(auditData==null || auditData.size() == 0)){
            resMes.put("message", "校验通过，请求唯一编码出现重复！");
            resMes.put("success", "false");
            resMes.put("operaCode", params.get("operaCode").toString());
            return resMes;
        }
        int resAudit = freightInquiryDao.addAudit(logParams);
        if (resAudit > 0) {
        } else {
            resMes.put("message", "校验通过，但添加审核记录时出错！");
            resMes.put("success", "false");
            resMes.put("operaCode", params.get("operaCode").toString());
            return resMes;
        }

        //成功后插入从表数据
        for (int i = 0; i < goodsList.size(); i++) {
            String uuids = UUID.randomUUID().toString();
            Object obj = JSONObject.toJSON(goodsList.get(i));
            JSONObject goodsInfo = JSONObject.parseObject(obj.toString());
            Map<String, Object> param = new HashMap<>();
            param.put("ID", uuids.replace("-", ""));
            param.put("ZID", logParams.get("operaCode"));
            param.put("TRAY_ID", goodsInfo.get("id"));
            param.put("AID", goodsInfo.get("AID"));
            param.put("pledgePiece", goodsInfo.get("pledgePiece"));
            param.put("pledgeGrossWeight", goodsInfo.get("pledgeGrossWeight"));
            param.put("billNum", goodsInfo.get("billNum"));
            param.put("ctnNum", goodsInfo.get("ctnNum"));
            //从表： 货物ID   质押表ID     操作数量 操作重量  提单号 箱号
            int resAuditDetail = freightInquiryDao.addAuditDetail(param);
            if (resAuditDetail > 0) {
            } else {
                resMes.put("message", "校验通过，但添加审核记录明细时出错！");
                resMes.put("success", "false");
                resMes.put("operaCode", params.get("operaCode").toString());
                return resMes;
            }
        }
        return resMes;
    }

    //改为从审核同意方法执行
    public Map<String, String> effectivenessPledge(Map<String, Object> params) {
        //质押生效方法  结果返回给港云仓
        Map<String, String> resMes = new HashMap<>();
        if (!StringUtils.isNotEmpty(params.get("CustCode") == null ? "" : params.get("CustCode").toString())) {
            resMes.put("message", "客户编码不正确");
            resMes.put("success", "false");
            resMes.put("operaCode", params.get("operaCode").toString());
            return resMes;
        }
        if (!StringUtils.isNotEmpty(params.get("CustName") == null ? "" : params.get("CustName").toString())) {
            resMes.put("message", "客户名称不正确");
            resMes.put("success", "false");
            resMes.put("operaCode", params.get("operaCode").toString());
            return resMes;
        }
        if (!StringUtils.isNotEmpty(params.get("cargoInfo") == null ? "" : params.get("cargoInfo").toString())) {
            resMes.put("message", "货物明细不能为空");
            resMes.put("operaCode", params.get("operaCode").toString());
            resMes.put("success", "false");
            return resMes;
        }
        Gson gson = new Gson();
        JSONObject jo = JSONObject.parseObject(gson.toJson(params));
        List<JSONObject> goodsList = JSONArray.parseArray(jo.getString("cargoInfo"),JSONObject.class);
        for (int i = 0; i < goodsList.size(); i++) {
            Object obj = JSONObject.toJSON(goodsList.get(i));
            JSONObject goodsInfo = JSONObject.parseObject(obj.toString());
            //货物主键
            if (!StringUtils.isNotEmpty(goodsInfo.get("id") == null ? "" : goodsInfo.get("id").toString())) {
                resMes.put("message", "货物ID不能为空，请检查");
                resMes.put("success", "false");
                resMes.put("operaCode", params.get("operaCode").toString());
                return resMes;
            }
            if (!StringUtils.isNotEmpty(goodsInfo.get("pledgePiece") == null ? "" : goodsInfo.get("pledgePiece").toString())) {
                resMes.put("message", "质押件数不能为空，请检查");
                resMes.put("success", "false");
                resMes.put("operaCode", params.get("operaCode").toString());
                return resMes;
            }
            if (!StringUtils.isNotEmpty(goodsInfo.get("pledgeGrossWeight") == null ? "" : goodsInfo.get("pledgeGrossWeight").toString())) {
                resMes.put("message", "质押重量不能为空，请检查");
                resMes.put("success", "false");
                resMes.put("operaCode", params.get("operaCode").toString());
                return resMes;
            }

            //传入的质押件数和重量
            int pledgePiece = 0;
            double pledgeGrossWeight = 0.0;
            try {
                pledgePiece = Integer.parseInt(goodsInfo.get("pledgePiece").toString());
                pledgeGrossWeight = Double.parseDouble(goodsInfo.get("pledgeGrossWeight").toString());
            } catch (Exception e) {
                resMes.put("message", "质押件数/重量参数有错误");
                resMes.put("success", "false");
                resMes.put("operaCode", params.get("operaCode").toString());
                return resMes;
            }

            String goodsID = goodsInfo.get("id").toString();
            TrayInfo trayInfo = trayInfoService.get(Integer.valueOf(goodsID.trim()));

            // 修改库存表的质押件数/重量字段
            // 如果当前库存表中的可质押件数/重量为空时，即之前没有做过质押，则可质押件数/重量为本次质押的件数/重量
            if (null == trayInfo.getPledgePiece() || null == trayInfo.getPledgeGrossWeight()) {
                //修改库存表的  质押件数、质押重量
                trayInfo.setPledgePiece(pledgePiece);
                trayInfo.setPledgeGrossWeight(Double.parseDouble(goodsInfo.get("pledgeGrossWeight").toString()));
            }
            //如果不为空时,质押数量等于之前的质押数量加本次质押数量
            else {
                trayInfo.setPledgePiece(trayInfo.getPledgePiece() + pledgePiece);
                trayInfo.setPledgeGrossWeight(trayInfo.getPledgeGrossWeight() + pledgeGrossWeight);
            }

            trayInfo.setIfTransfer("3");
            trayInfoService.update(trayInfo);

            if (goodsInfo.get("AID") != null && !"".equals(goodsInfo.get("AID").toString())) {
                ApiPledge info = pledgeComfirmService.get(goodsInfo.get("AID").toString());
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
        //反写操作记录表，表示操作成功

        resMes.put("message", "OK");
        resMes.put("success", "success");
        resMes.put("operaCode", params.get("operaCode").toString());
        return resMes;
    }

    public Map<String, String> releaseCustody(Map<String, Object> params) {
        //解押校验方法
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date reqDd = new Date();

        Map<String, String> resMes = new HashMap<>();
        if (!StringUtils.isNotEmpty(params.get("CustCode") == null ? "" : params.get("CustCode").toString())) {
            resMes.put("message", "客户编码不正确");
            resMes.put("success", "false");
            resMes.put("operaCode", params.get("operaCode").toString());

            //成功和失败都保存在日志表
            Map<String, Object> logParams = new HashMap<>();
            String uuid = UUID.randomUUID().toString();
            logParams.put("ID", uuid.replace("-", ""));
            logParams.put("operaCode", params.get("operaCode").toString());
            logParams.put("CustCode", params.get("CustCode").toString());
            logParams.put("CustName", params.get("CustName").toString());
            logParams.put("operaType", "JYCheck");
            logParams.put("params", params.toString());
            logParams.put("reponse", resMes.toString());
            logParams.put("reqDd", sdf.format(reqDd));
            int resLogBulk = freightInquiryDao.addLogBulk(logParams);
            if (resLogBulk > 0) {
            } else {
                log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
            }
            return resMes;
        }
        if (!StringUtils.isNotEmpty(params.get("CustName") == null ? "" : params.get("CustName").toString())) {
            resMes.put("message", "客户名称不正确");
            resMes.put("success", "false");
            resMes.put("operaCode", params.get("operaCode").toString());

            //成功和失败都保存在日志表
            Map<String, Object> logParams = new HashMap<>();
            String uuid = UUID.randomUUID().toString();
            logParams.put("ID", uuid.replace("-", ""));
            logParams.put("operaCode", params.get("operaCode").toString());
            logParams.put("CustCode", params.get("CustCode").toString());
            logParams.put("CustName", params.get("CustName").toString());
            logParams.put("operaType", "JYCheck");
            logParams.put("params", params.toString());
            logParams.put("reponse", resMes.toString());
            logParams.put("reqDd", sdf.format(reqDd));
            int resLogBulk = freightInquiryDao.addLogBulk(logParams);
            if (resLogBulk > 0) {
            } else {
                log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
            }
            return resMes;
        }
        if (!StringUtils.isNotEmpty(params.get("cargoInfo") == null ? "" : params.get("cargoInfo").toString())) {
            resMes.put("message", "货物明细不能为空");
            resMes.put("success", "false");
            resMes.put("operaCode", params.get("operaCode").toString());

            //成功和失败都保存在日志表
            Map<String, Object> logParams = new HashMap<>();
            String uuid = UUID.randomUUID().toString();
            logParams.put("ID", uuid.replace("-", ""));
            logParams.put("operaCode", params.get("operaCode").toString());
            logParams.put("CustCode", params.get("CustCode").toString());
            logParams.put("CustName", params.get("CustName").toString());
            logParams.put("operaType", "JYCheck");
            logParams.put("params", params.toString());
            logParams.put("reponse", resMes.toString());
            logParams.put("reqDd", sdf.format(reqDd));
            int resLogBulk = freightInquiryDao.addLogBulk(logParams);
            if (resLogBulk > 0) {
            } else {
                log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
            }
            return resMes;
        }

        Gson gson = new Gson();
        JSONObject jo = JSONObject.parseObject(gson.toJson(params));
        List<JSONObject> goodsList = JSONArray.parseArray(jo.getString("cargoInfo"),JSONObject.class);
        for (int i = 0; i < goodsList.size(); i++) {
            Object obj = JSONObject.toJSON(goodsList.get(i));
            JSONObject goodsInfo = JSONObject.parseObject(obj.toString());
            if (!StringUtils.isNotEmpty(goodsInfo.get("id") == null ? "" : goodsInfo.get("id").toString())) {
                resMes.put("message", "货物ID不能为空，请检查");
                resMes.put("success", "false");
                resMes.put("operaCode", params.get("operaCode").toString());

                //成功和失败都保存在日志表
                Map<String, Object> logParams = new HashMap<>();
                String uuid = UUID.randomUUID().toString();
                logParams.put("ID", uuid.replace("-", ""));
                logParams.put("operaCode", params.get("operaCode").toString());
                logParams.put("CustCode", params.get("CustCode").toString());
                logParams.put("CustName", params.get("CustName").toString());
                logParams.put("operaType", "JYCheck");
                logParams.put("params", params.toString());
                logParams.put("reponse", resMes.toString());
                logParams.put("reqDd", sdf.format(reqDd));
                int resLogBulk = freightInquiryDao.addLogBulk(logParams);
                if (resLogBulk > 0) {
                } else {
                    log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
                }
                return resMes;
            }
            if (!StringUtils.isNotEmpty(goodsInfo.get("releasePiece") == null ? "" : goodsInfo.get("releasePiece").toString())) {
                resMes.put("message", "解押件数不能为空，请检查");
                resMes.put("success", "false");
                resMes.put("operaCode", params.get("operaCode").toString());

                //成功和失败都保存在日志表
                Map<String, Object> logParams = new HashMap<>();
                String uuid = UUID.randomUUID().toString();
                logParams.put("ID", uuid.replace("-", ""));
                logParams.put("operaCode", params.get("operaCode").toString());
                logParams.put("CustCode", params.get("CustCode").toString());
                logParams.put("CustName", params.get("CustName").toString());
                logParams.put("operaType", "JYCheck");
                logParams.put("params", params.toString());
                logParams.put("reponse", resMes.toString());
                logParams.put("reqDd", sdf.format(reqDd));
                int resLogBulk = freightInquiryDao.addLogBulk(logParams);
                if (resLogBulk > 0) {
                } else {
                    log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
                }
                return resMes;
            }

            if (!StringUtils.isNotEmpty(goodsInfo.get("releaseWeight") == null ? "" : goodsInfo.get("releaseWeight").toString())) {
                resMes.put("message", "解押重量不能为空，请检查");
                resMes.put("success", "false");
                resMes.put("operaCode", params.get("operaCode").toString());

                //成功和失败都保存在日志表
                Map<String, Object> logParams = new HashMap<>();
                String uuid = UUID.randomUUID().toString();
                logParams.put("ID", uuid.replace("-", ""));
                logParams.put("operaCode", params.get("operaCode").toString());
                logParams.put("CustCode", params.get("CustCode").toString());
                logParams.put("CustName", params.get("CustName").toString());
                logParams.put("operaType", "JYCheck");
                logParams.put("params", params.toString());
                logParams.put("reponse", resMes.toString());
                logParams.put("reqDd", sdf.format(reqDd));
                int resLogBulk = freightInquiryDao.addLogBulk(logParams);
                if (resLogBulk > 0) {
                } else {
                    log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
                }
                return resMes;
            }

            if (goodsInfo.get("AID") != null && !"".equals(goodsInfo.get("AID").toString())) {
                ApiPledge info = pledgeComfirmService.get(goodsInfo.get("AID").toString());
                if (info != null) {
                    ApiPledge pledged = apiPledgeService.findUniqueByTrendId(info.getRelatedTrendId());

                    // 如果解押数量大于原质押数量
                    if (info.getPledgeNumber() > pledged.getPledgeNumber()) {
                        resMes.put("message", "解押数量或重量超出当前质押数量或重量");
                        resMes.put("success", "false");
                        resMes.put("operaCode", params.get("operaCode").toString());
                        //成功和失败都保存在日志表
                        Map<String, Object> logParams = new HashMap<>();
                        String uuid = UUID.randomUUID().toString();
                        logParams.put("ID", uuid.replace("-", ""));
                        logParams.put("operaCode", params.get("operaCode").toString());
                        logParams.put("CustCode", params.get("CustCode").toString());
                        logParams.put("CustName", params.get("CustName").toString());
                        logParams.put("operaType", "JYCheck");
                        logParams.put("params", params.toString());
                        logParams.put("reponse", resMes.toString());
                        logParams.put("reqDd", sdf.format(reqDd));
                        int resLogBulk = freightInquiryDao.addLogBulk(logParams);
                        if (resLogBulk > 0) {
                        } else {
                            log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
                        }
                        return resMes;
                    }

                    //解押数量不能超出trend_id的数量
                    HashMap<String, Object> pledgedRelatedMap = apiPledgeService.countRelatedTrendIdByRelatedTrendId(info.getRelatedTrendId());
                    Integer pledgedRelatedNum = Integer.valueOf(pledgedRelatedMap.get("sumNum").toString());
                    Integer pledgedRelatedWeight = Integer.valueOf(pledgedRelatedMap.get("sumWeight").toString());
                    if (info.getPledgeNumber() > pledgedRelatedNum) {
                        resMes.put("message", "本次解押数量超出限制！");
                        resMes.put("success", "false");
                        resMes.put("operaCode", params.get("operaCode").toString());

                        //成功和失败都保存在日志表
                        Map<String, Object> logParams = new HashMap<>();
                        String uuid = UUID.randomUUID().toString();
                        logParams.put("ID", uuid.replace("-", ""));
                        logParams.put("operaCode", params.get("operaCode").toString());
                        logParams.put("CustCode", params.get("CustCode").toString());
                        logParams.put("CustName", params.get("CustName").toString());
                        logParams.put("operaType", "JYCheck");
                        logParams.put("params", params.toString());
                        logParams.put("reponse", resMes.toString());
                        logParams.put("reqDd", sdf.format(reqDd));
                        int resLogBulk = freightInquiryDao.addLogBulk(logParams);
                        if (resLogBulk > 0) {
                        } else {
                            log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
                        }
                        return resMes;
                    }
                }
            }

            TrayInfo trayInfo = trayInfoService.get(Integer.parseInt(goodsInfo.get("id").toString()));

            Integer trayInfoPledgePiece = 0;
            if (null == trayInfo.getPledgePiece()) {
                trayInfoPledgePiece = trayInfo.getNowPiece();
            } else {
                trayInfoPledgePiece = trayInfo.getPledgePiece();
            }
            // 库存表可解押数量计算
            // 如果解押数量大于库存表中的 质押数量
            if (Integer.parseInt(goodsInfo.get("releasePiece").toString()) > trayInfoPledgePiece) {
                resMes.put("message", "解押数量或重量超出当前质押数量或重量");
                resMes.put("success", "false");
                resMes.put("operaCode", params.get("operaCode").toString());

                //成功和失败都保存在日志表
                Map<String, Object> logParams = new HashMap<>();
                String uuid = UUID.randomUUID().toString();
                logParams.put("ID", uuid.replace("-", ""));
                logParams.put("operaCode", params.get("operaCode").toString());
                logParams.put("CustCode", params.get("CustCode").toString());
                logParams.put("CustName", params.get("CustName").toString());
                logParams.put("operaType", "JYCheck");
                logParams.put("params", params.toString());
                logParams.put("reponse", resMes.toString());
                logParams.put("reqDd", sdf.format(reqDd));
                int resLogBulk = freightInquiryDao.addLogBulk(logParams);
                if (resLogBulk > 0) {
                } else {
                    log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
                }
                return resMes;
            }
        }
        resMes.put("message", "OK");
        resMes.put("success", "success");
        resMes.put("operaCode", params.get("operaCode").toString());

        //成功和失败都保存在日志表
        Map<String, Object> logParams = new HashMap<>();
        String uuid = UUID.randomUUID().toString();
        logParams.put("ID", uuid.replace("-", ""));
        logParams.put("operaCode", params.get("operaCode").toString());
        logParams.put("CustCode", params.get("CustCode").toString());
        logParams.put("CustName", params.get("CustName").toString());
        logParams.put("operaType", "JYCheck");
        logParams.put("params", params.toString());
        logParams.put("reponse", resMes.toString());
        logParams.put("reqDd", sdf.format(reqDd));
        int resLogBulk = freightInquiryDao.addLogBulk(logParams);
        if (resLogBulk > 0) {
        } else {
            log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
        }

        //执行时间、操作状态
        logParams.put("execDd", "");
        logParams.put("operStatue", "2");
        int resAudit = freightInquiryDao.addAudit(logParams);
        if (resAudit > 0) {
        } else {
            resMes.put("message", "校验通过，但添加审核记录时出错！");
            resMes.put("success", "false");
            resMes.put("operaCode", params.get("operaCode").toString());
            return resMes;
        }

        //成功后插入从表数据
        for (int i = 0; i < goodsList.size(); i++) {
            String uuids = UUID.randomUUID().toString();
            Object obj = JSONObject.toJSON(goodsList.get(i));
            JSONObject goodsInfo = JSONObject.parseObject(obj.toString());
            Map<String, Object> param = new HashMap<>();
            param.put("ID", uuids.replace("-", ""));
            param.put("ZID", logParams.get("operaCode"));
            param.put("TRAY_ID", goodsInfo.get("id"));
            param.put("AID", goodsInfo.get("AID"));
            param.put("pledgePiece", goodsInfo.get("releasePiece"));
            param.put("pledgeGrossWeight", goodsInfo.get("releaseWeight"));
            param.put("billNum", goodsInfo.get("billNum"));
            param.put("ctnNum", goodsInfo.get("ctnNum"));
            //从表： 货物ID   质押表ID     操作数量 操作重量  提单号 箱号
            int resAuditDetail = freightInquiryDao.addAuditDetail(param);
            if (resAuditDetail > 0) {
            } else {
                resMes.put("message", "校验通过，但添加审核记录明细时出错！");
                resMes.put("success", "false");
                resMes.put("operaCode", params.get("operaCode").toString());
                return resMes;
            }
        }
        return resMes;
    }

    //改为从审核同意方法执行
    public Map<String, String> effectivenessRelease(Map<String, Object> params) {
        //解押生效方法
        Map<String, String> resMes = new HashMap<>();
        if (!StringUtils.isNotEmpty(params.get("CustCode") == null ? "" : params.get("CustCode").toString())) {
            resMes.put("message", "客户编码不正确");
            resMes.put("success", "false");
            resMes.put("operaCode", params.get("operaCode").toString());
            return resMes;
        }
        if (!StringUtils.isNotEmpty(params.get("CustName") == null ? "" : params.get("CustName").toString())) {
            resMes.put("message", "客户名称不正确");
            resMes.put("success", "false");
            resMes.put("operaCode", params.get("operaCode").toString());
            return resMes;
        }
        if (!StringUtils.isNotEmpty(params.get("cargoInfo") == null ? "" : params.get("cargoInfo").toString())) {
            resMes.put("message", "货物明细不能为空");
            resMes.put("success", "false");
            resMes.put("operaCode", params.get("operaCode").toString());
            return resMes;
        }

        Gson gson = new Gson();
        JSONObject jo = JSONObject.parseObject(gson.toJson(params));
        List<JSONObject> goodsList = JSONArray.parseArray(jo.getString("cargoInfo"),JSONObject.class);
        for (int i = 0; i < goodsList.size(); i++) {
            Object obj = JSONObject.toJSON(goodsList.get(i));
            JSONObject goodsInfo = JSONObject.parseObject(obj.toString());
            if (!StringUtils.isNotEmpty(goodsInfo.get("id") == null ? "" : goodsInfo.get("id").toString())) {
                resMes.put("message", "货物ID不能为空，请检查");
                resMes.put("success", "false");
                resMes.put("operaCode", params.get("operaCode").toString());
                return resMes;
            }
            if (!StringUtils.isNotEmpty(goodsInfo.get("releasePiece") == null ? "" : goodsInfo.get("releasePiece").toString())) {
                resMes.put("message", "解押件数不能为空，请检查");
                resMes.put("success", "false");
                resMes.put("operaCode", params.get("operaCode").toString());
                return resMes;
            }

            if (!StringUtils.isNotEmpty(goodsInfo.get("releaseWeight") == null ? "" : goodsInfo.get("releaseWeight").toString())) {
                resMes.put("message", "解押重量不能为空，请检查");
                resMes.put("success", "false");
                resMes.put("operaCode", params.get("operaCode").toString());
                return resMes;
            }

            Integer pledgedRelatedNum = 0;
            Integer pledgedRelatedWeight = 0;
            if (goodsInfo.get("AID") != null && !"".equals(goodsInfo.get("AID").toString())) {
                ApiPledge info = pledgeComfirmService.get(goodsInfo.get("AID").toString());
                if (info != null) {
                    ApiPledge pledged = apiPledgeService.findUniqueByTrendId(info.getRelatedTrendId());

                    // 如果解押数量大于原质押数量
                    if (info.getPledgeNumber() > pledged.getPledgeNumber()) {
                        resMes.put("message", "解押数量或重量超出当前质押数量或重量");
                        resMes.put("success", "false");
                        resMes.put("operaCode", params.get("operaCode").toString());
                        return resMes;
                    }

                    //解押数量不能超出trend_id的数量
                    HashMap<String, Object> pledgedRelatedMap = apiPledgeService.countRelatedTrendIdByRelatedTrendId(info.getRelatedTrendId());
                    pledgedRelatedNum = Integer.valueOf(pledgedRelatedMap.get("sumNum").toString());
                    pledgedRelatedWeight = Integer.valueOf(pledgedRelatedMap.get("sumWeight").toString());
                    if (info.getPledgeNumber() > pledgedRelatedNum) {
                        resMes.put("message", "本次解押数量超出限制！");
                        resMes.put("success", "false");
                        resMes.put("operaCode", params.get("operaCode").toString());
                        return resMes;
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

            TrayInfo trayInfo = trayInfoService.get(Integer.parseInt(goodsInfo.get("id").toString()));

            Integer trayInfoPledgePiece = 0;
            if (null == trayInfo.getPledgePiece()) {
                trayInfoPledgePiece = trayInfo.getNowPiece();
            } else {
                trayInfoPledgePiece = trayInfo.getPledgePiece();
            }
            // 库存表可解押数量计算
            // 如果解押数量大于库存表中的 质押数量
            if (Integer.parseInt(goodsInfo.get("releasePiece").toString()) > trayInfoPledgePiece) {
                resMes.put("message", "解押数量或重量超出当前质押数量或重量");
                resMes.put("success", "false");
                resMes.put("operaCode", params.get("operaCode").toString());
                return resMes;
            }


            trayInfo.setPledgePiece(trayInfo.getPledgePiece() - (pledgedRelatedNum - Integer.parseInt(goodsInfo.get("releasePiece").toString())));
            trayInfo.setPledgeGrossWeight(trayInfo.getPledgeGrossWeight() - (pledgedRelatedWeight.doubleValue() - Integer.parseInt(goodsInfo.get("releaseWeight").toString())));

            // 如果库存表记录中的质押件数为0时，设置IfTransfer标志为0正常
            if (0 == trayInfo.getPledgePiece()) {
                trayInfo.setIfTransfer("0");
            }
            trayInfoService.update(trayInfo);

            //操作记录解押完成

        }
        resMes.put("message", "OK");
        resMes.put("success", "success");
        resMes.put("operaCode", params.get("operaCode").toString());
        return resMes;
    }

    public String operaRequest(Map<String, Object> params) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date reqDd = new Date();
        if (!StringUtils.isNotEmpty(params.get("firstOperaCode") == null ? "" : params.get("firstOperaCode").toString())) {
            //成功和失败都保存在日志表
            Map<String, Object> logParams = new HashMap<>();
            String uuid = UUID.randomUUID().toString();
            logParams.put("ID", uuid.replace("-", ""));
            logParams.put("operaCode", params.get("operaCode"));
            logParams.put("CustCode", params.get("CustCode"));
            logParams.put("CustName", params.get("CustName"));
            logParams.put("operaType", params.get("operType"));
            logParams.put("params", params.toString());
            logParams.put("reponse", "第一次请求唯一编码参数为空");
            logParams.put("reqDd", sdf.format(reqDd));
            int resLogBulk = freightInquiryDao.addLogBulk(logParams);
            if (resLogBulk > 0) {
            } else {
                log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
            }
            return "第一次请求唯一编码参数为空";
        }
        //主表数据
        List<Map<String, Object>> auditData = approvalBulkCommDao.findAudit(params.get("firstOperaCode").toString());
        //从表数据
        List<Map<String, Object>> detailInfo = approvalBulkCommDao.findInfoById(params.get("firstOperaCode").toString());

        if (auditData.size() > 0 && auditData.get(0) != null) {
            if (auditData.get(0).get("OPER_STATE") != null && "3".equals(auditData.get(0).get("OPER_STATE"))) {
                //校验主表是否一致  (客户名称 客户编码)
                String custCode = params.get("CustCode") == null ? "" : params.get("CustCode").toString();
                String custName = params.get("CustName") == null ? "" : params.get("CustName").toString();
                String operType = params.get("operType") == null ? "" : params.get("operType").toString();
                if (!StringUtils.isNotEmpty(custCode) || !custCode.equals(auditData.get(0).get("CUSTOMER_CODE"))) {
                    //成功和失败都保存在日志表
                    Map<String, Object> logParams = new HashMap<>();
                    String uuid = UUID.randomUUID().toString();
                    logParams.put("ID", uuid.replace("-", ""));
                    logParams.put("operaCode", params.get("operaCode").toString());
                    logParams.put("CustCode", params.get("CustCode").toString());
                    logParams.put("CustName", params.get("CustName").toString());
                    logParams.put("operaType", params.get("operType"));
                    logParams.put("params", params.toString());
                    logParams.put("reponse", "客户编码不正确");
                    logParams.put("reqDd", sdf.format(reqDd));
                    int resLogBulk = freightInquiryDao.addLogBulk(logParams);
                    if (resLogBulk > 0) {
                    } else {
                        log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
                    }
                    return "客户编码不正确";
                }
                if (!StringUtils.isNotEmpty(custName) || !custName.equals(auditData.get(0).get("CUSTOMER_NAME"))) {
                    //成功和失败都保存在日志表
                    Map<String, Object> logParams = new HashMap<>();
                    String uuid = UUID.randomUUID().toString();
                    logParams.put("ID", uuid.replace("-", ""));
                    logParams.put("operaCode", params.get("operaCode"));
                    logParams.put("CustCode", params.get("CustCode"));
                    logParams.put("CustName", params.get("CustName"));
                    logParams.put("operaType", params.get("operType"));
                    logParams.put("params", params.toString());
                    logParams.put("reponse", "客户名称不正确");
                    logParams.put("reqDd", sdf.format(reqDd));
                    int resLogBulk = freightInquiryDao.addLogBulk(logParams);
                    if (resLogBulk > 0) {
                    } else {
                        log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
                    }
                    return "客户名称不正确";
                }
                if (!StringUtils.isNotEmpty(operType)) {
                    //成功和失败都保存在日志表
                    Map<String, Object> logParams = new HashMap<>();
                    String uuid = UUID.randomUUID().toString();
                    logParams.put("ID", uuid.replace("-", ""));
                    logParams.put("operaCode", params.get("operaCode"));
                    logParams.put("CustCode", params.get("CustCode"));
                    logParams.put("CustName", params.get("CustName"));
                    logParams.put("operaType", params.get("operType"));
                    logParams.put("params", params.toString());
                    logParams.put("reponse", "操作类型不能为空");
                    logParams.put("reqDd", sdf.format(reqDd));
                    int resLogBulk = freightInquiryDao.addLogBulk(logParams);
                    if (resLogBulk > 0) {
                    } else {
                        log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
                    }
                    return "操作类型不能为空";
                }
                if (!StringUtils.isNotEmpty(params.get("cargoInfo") == null ? "" : params.get("cargoInfo").toString())) {
                    //成功和失败都保存在日志表
                    Map<String, Object> logParams = new HashMap<>();
                    String uuid = UUID.randomUUID().toString();
                    logParams.put("ID", uuid.replace("-", ""));
                    logParams.put("operaCode", params.get("operaCode"));
                    logParams.put("CustCode", params.get("CustCode"));
                    logParams.put("CustName", params.get("CustName"));
                    logParams.put("operaType", params.get("operType"));
                    logParams.put("params", params.toString());
                    logParams.put("reponse", "货物明细不能为空");
                    logParams.put("reqDd", sdf.format(reqDd));
                    int resLogBulk = freightInquiryDao.addLogBulk(logParams);
                    if (resLogBulk > 0) {
                    } else {
                        log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
                    }
                    return "货物明细不能为空";
                }
                if (!(detailInfo != null && detailInfo.size() > 0)) {
                    //成功和失败都保存在日志表
                    Map<String, Object> logParams = new HashMap<>();
                    String uuid = UUID.randomUUID().toString();
                    logParams.put("ID", uuid.replace("-", ""));
                    logParams.put("operaCode", params.get("operaCode"));
                    logParams.put("CustCode", params.get("CustCode"));
                    logParams.put("CustName", params.get("CustName"));
                    logParams.put("operaType", params.get("operType"));
                    logParams.put("params", params.toString());
                    logParams.put("reponse", "货物明细不存在");
                    logParams.put("reqDd", sdf.format(reqDd));
                    int resLogBulk = freightInquiryDao.addLogBulk(logParams);
                    if (resLogBulk > 0) {
                    } else {
                        log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
                    }
                    return "货物明细不存在";
                }
                //校验从表是否一致  (提单号 箱号 件数 重量)
                Gson gson = new Gson();
                JSONObject jo = JSONObject.parseObject(gson.toJson(params));
                List<JSONObject> goodsList = JSONArray.parseArray(jo.getString("cargoInfo"),JSONObject.class);
                for (int i = 0; i < goodsList.size(); i++) {
                    Object obj = JSONObject.toJSON(goodsList.get(i));
                    JSONObject goodsInfo = JSONObject.parseObject(obj.toString());
                    for (Map<String, Object> detail : detailInfo) {
                        if(detail.get("TRAY_ID")!=null && goodsInfo.get("id")!=null && detail.get("TRAY_ID").toString().equals(goodsInfo.get("id").toString())){
                            //比较 提单号 箱号 件数 重量 是否一致
                            if(!(detail.get("OPER_QTY").toString().equals(goodsInfo.get("pledgePiece").toString()) && detail.get("OPER_WEIGHT").toString().equals(goodsInfo.get("pledgeGrossWeight").toString()) && detail.get("BILL_NUM").toString().equals(goodsInfo.get("billNum").toString()) && detail.get("CTN_NUM").toString().equals(goodsInfo.get("ctnNum").toString()))){
                                //成功和失败都保存在日志表
                                Map<String, Object> logParams = new HashMap<>();
                                String uuid = UUID.randomUUID().toString();
                                logParams.put("ID", uuid.replace("-", ""));
                                logParams.put("operaCode", params.get("operaCode"));
                                logParams.put("CustCode", params.get("CustCode"));
                                logParams.put("CustName", params.get("CustName"));
                                logParams.put("operaType", params.get("operType"));
                                logParams.put("params", params.toString());
                                logParams.put("reponse", "货物明细和校验时不一致");
                                logParams.put("reqDd", sdf.format(reqDd));
                                int resLogBulk = freightInquiryDao.addLogBulk(logParams);
                                if (resLogBulk > 0) {
                                } else {
                                    log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
                                }
                                return "货物明细和校验时不一致";
                            }
                        }
                    }
                }

                //判断完成后
                Map<String,Object> operaParams = new HashMap<>();
                operaParams.put("execDd", "");
                operaParams.put("operStatue", "2");
                operaParams.put("CustCode", params.get("CustCode").toString());
                operaParams.put("CustName", params.get("CustName").toString());
                operaParams.put("operaCode", params.get("operaCode").toString());
                operaParams.put("operaType", params.get("operType"));
                operaParams.put("reqDd", sdf.format(reqDd));
                int resAudit = freightInquiryDao.addAudit(operaParams);
                if (resAudit > 0) {} else {
                    return "校验通过，但添加审核记录时出错！";
                }

                //成功后插入从表数据
                for (int i = 0; i < goodsList.size(); i++) {
                    String uuids = UUID.randomUUID().toString();
                    Object obj = JSONObject.toJSON(goodsList.get(i));
                    JSONObject goodsInfo = JSONObject.parseObject(obj.toString());
                    Map<String, Object> param = new HashMap<>();
                    param.put("ID", uuids.replace("-", ""));
                    param.put("ZID", operaParams.get("operaCode"));
                    param.put("TRAY_ID", goodsInfo.get("id"));
                    param.put("AID", goodsInfo.get("AID"));
                    param.put("pledgePiece", goodsInfo.get("pledgePiece"));
                    param.put("pledgeGrossWeight", goodsInfo.get("pledgeGrossWeight"));
                    param.put("billNum", goodsInfo.get("billNum"));
                    param.put("ctnNum", goodsInfo.get("ctnNum"));
                    //从表： 货物ID   质押表ID     操作数量 操作重量  提单号 箱号
                    int resAuditDetail = freightInquiryDao.addAuditDetail(param);
                    if (resAuditDetail > 0) {
                    } else {
                        return "校验通过，但添加审核记录明细时出错！";
                    }
                }
                return "";
            }
            else {
                //成功和失败都保存在日志表
                Map<String, Object> logParams = new HashMap<>();
                String uuid = UUID.randomUUID().toString();
                logParams.put("ID", uuid.replace("-", ""));
                logParams.put("operaCode", params.get("operaCode"));
                logParams.put("CustCode", params.get("CustCode"));
                logParams.put("CustName", params.get("CustName"));
                logParams.put("operaType", params.get("operType"));
                logParams.put("params", params.toString());
                logParams.put("reponse", "操作申请不通过，请确认");
                logParams.put("reqDd", sdf.format(reqDd));
                int resLogBulk = freightInquiryDao.addLogBulk(logParams);
                if (resLogBulk > 0) {
                } else {
                    log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
                }
                return "操作申请不通过，请确认";
            }
        } else {
            //成功和失败都保存在日志表
            Map<String, Object> logParams = new HashMap<>();
            String uuid = UUID.randomUUID().toString();
            logParams.put("ID", uuid.replace("-", ""));
            logParams.put("operaCode", params.get("operaCode"));
            logParams.put("CustCode", params.get("CustCode"));
            logParams.put("CustName", params.get("CustName"));
            logParams.put("operaType", params.get("operType"));
            logParams.put("params", params.toString());
            logParams.put("reponse", "还未进行操作申请校验");
            logParams.put("reqDd", sdf.format(reqDd));
            int resLogBulk = freightInquiryDao.addLogBulk(logParams);
            if (resLogBulk > 0) {
            } else {
                log.error(logParams + "---BULKCOMM_LOG 日志保存错误---");
            }
            return "还未进行操作申请校验";
        }
    }
}
