package com.haiersoft.ccli.wms.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;


import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.wms.entity.BisAsn;
import com.haiersoft.ccli.wms.entity.BisAsnInfo;
import com.haiersoft.ccli.wms.entity.BisGateCar;
import com.haiersoft.ccli.wms.entity.BisGateCarParams;
import com.haiersoft.ccli.wms.entity.BisGateCarWrongInfo;
import com.haiersoft.ccli.wms.entity.BisInOut;
import com.haiersoft.ccli.wms.entity.BisLoadingInfo;
import com.haiersoft.ccli.wms.entity.BisLoadingOrder;
import com.haiersoft.ccli.wms.entity.BisLoadingOrderInfo;
import com.haiersoft.ccli.wms.entity.BisPresenceBox;
import com.haiersoft.ccli.wms.service.ASNInfoService;
import com.haiersoft.ccli.wms.service.ASNService;
import com.haiersoft.ccli.wms.service.GateCarService;
import com.haiersoft.ccli.wms.service.GateCarWrongInfoService;
import com.haiersoft.ccli.wms.service.InOutService;
import com.haiersoft.ccli.wms.service.LoadingInfoService;
import com.haiersoft.ccli.wms.service.LoadingOrderInfoService;
import com.haiersoft.ccli.wms.service.LoadingOrderService;
import com.haiersoft.ccli.wms.service.PresenceBoxService;

/**
 * 闸口管理
 */
@Controller
@RequestMapping("wsgate/wms/gate")
public class WsGate2Controller extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(WsGate2Controller.class);
    @Autowired
    private InOutService inOutService;

    @Autowired
    private PresenceBoxService presenceBoxService;

    @Autowired
    private GateCarService gateCarService;
    @Autowired
    private GateCarWrongInfoService gateCarWrongInfoService;

    @Autowired
    private ASNService asnService;
    @Autowired
    private ASNInfoService asnInfoService;
    @Autowired
    private LoadingInfoService loadingInfoService;
    @Autowired
    private LoadingOrderService loadingOrderService;
    @Autowired
    private LoadingOrderInfoService loadingOrderInfoService;


    /**
     * 对外接口：直接调用并将数据存入数据库
     * 接收参数为：
     * carNum 车号
     * ctnNum1 箱号1
     * ctnNum2 箱号2
     * snFlag 南北闸标志 S:南闸,N:北闸
     * dataSource 数据来源:3:查验,2:冷链
     */
    @RequestMapping(value = "submit2", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public Map<String, Object> submit2(HttpServletRequest request) {

        /*
         * String carNum= null; String ctnNum1= null; String ctnNum2= null; String
         * snFlag= null; String dataSource= null; try { BufferedReader streamReader =
         * new BufferedReader( new InputStreamReader(request.getInputStream(),
         * "UTF-8")); StringBuilder responseStrBuilder = new StringBuilder(); String
         * inputStr; while ((inputStr = streamReader.readLine()) != null)
         * responseStrBuilder.append(inputStr);
         *
         * JSONObject jsonObject =
         * JSONObject.parseObject(responseStrBuilder.toString());
         *
         * logger.error( jsonObject.toJSONString()); carNum =
         * jsonObject.getString("carNum"); ctnNum1 = jsonObject.getString("ctnNum1");
         * ctnNum2 = jsonObject.getString("ctnNum2"); snFlag =
         * jsonObject.getString("snFlag"); dataSource =
         * jsonObject.getString("dataSource");
         *
         * } catch (Exception e) { e.printStackTrace(); }
         */

        String carNum = request.getParameter("carNum");
        Map<String, Object> resultMap = new HashMap();
        // 车牌号为空，直接return null
        if (StringUtils.isEmpty(carNum)) {
            resultMap.put("code", 1);
            resultMap.put("success", false);
            resultMap.put("msg", "车牌号不允许为空！");
            return resultMap;
        }
        String ctnNum1 = request.getParameter("ctnNum1");
        String ctnNum2 = request.getParameter("ctnNum2");
        String snFlag = request.getParameter("snFlag");
        if (StringUtils.isEmpty(snFlag)) {
            resultMap.put("code", 1);
            resultMap.put("success", false);
            resultMap.put("msg", "南北闸标志不能为空");
            return resultMap;
        }
        String dataSource = request.getParameter("dataSource");


        //数据处理
        if(snFlag.equals("N")) {
            return dealWithNorth(carNum,ctnNum1,ctnNum2);
        }
        //南北闸标志为S 南闸且数据来源类型为3 查验时
        if(!StringUtils.isEmpty(dataSource)) {
            if(snFlag.equals("S") && dataSource.equals("3")){
                return dealWithSouth(carNum,ctnNum1,ctnNum2);
            }
        }


        return resultMap;

    }
    private Map<String, Object> dealWithSouth(String carNo,String ctnNum1,String ctnNum2){
        Map<String, Object> resultMap = new HashMap();
        BisGateCar bgs = new BisGateCar();
        bgs.setCarNum(carNo);
        if (StringUtils.isNotEmpty(ctnNum1)) {
            bgs.setCtnNum(ctnNum1);
        }
        bgs.setBisType("5");
        bgs.setCreateUser("接口调用");
        bgs.setJobType("1");
        Date date = new Date();
        bgs.setCreateDate(date);
        gateCarService.save(bgs);
        if (StringUtils.isNotEmpty(ctnNum2)) {
            BisGateCar bgs2 = new BisGateCar();
            bgs2.setCarNum(carNo);
            bgs2.setCtnNum(ctnNum2);
            bgs2.setBisType("5");
            bgs2.setCreateUser("接口调用");
            bgs2.setJobType("1");
            bgs2.setCreateDate(date);
            gateCarService.save(bgs2);
        }
        return resultMap;
    }
    private Map<String, Object> dealWithNorth(String carNo,String ctnNum1,String ctnNum2){
        Map<String, Object> resultMap = new HashMap();
        BisGateCarParams bisGateCarParams = new BisGateCarParams();
        bisGateCarParams.setCarNum(carNo);
        List<String> list = new ArrayList();
        if (StringUtils.isNotEmpty(ctnNum1)) {
            list.add(ctnNum1);
        }
        if (StringUtils.isNotEmpty(ctnNum2)) {
            list.add(ctnNum2);
        }
        bisGateCarParams.setCtnNum(list);

        // 判断进出场记录表中是否有重复车牌号
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        PropertyFilter filter = new PropertyFilter("EQS_carNum", bisGateCarParams.getCarNum());
        filters.add(filter);
        List<BisInOut> inOutList = inOutService.search(filters);

        for (String ctnNum : bisGateCarParams.getCtnNum()) {
            if (StringUtils.isNotEmpty(ctnNum)) {
                BisGateCar bisGateCar = new BisGateCar();
                Map<String, Object> searchBises1 = searchBis1(bisGateCarParams.getCarNum(), ctnNum);
                // 判断在场箱表中是否有重复箱号
                // String ctnNo=gateEntity.getCtnNum();
                List<PropertyFilter> filters2 = new ArrayList<PropertyFilter>();
                PropertyFilter filter2 = new PropertyFilter("EQS_ctnNum", ctnNum);
                filters2.add(filter2);
                List<BisPresenceBox> boxList = presenceBoxService.search(filters2);

                bisGateCar = getBisGateCar(searchBises1, bisGateCar);
                setProperties(bisGateCarParams.getCarNum(), ctnNum, inOutList, boxList, resultMap, bisGateCar);
            }
        }
        //参数只有车牌号
        if(bisGateCarParams.getCtnNum() == null || bisGateCarParams.getCtnNum().size() <= 0) {
            BisGateCar bisGateCar = new BisGateCar();
            Map<String, Object> searchBises1 = searchBis1(bisGateCarParams.getCarNum(), null);

            // 判断在场箱表中是否有重复箱号
            // String ctnNo=gateEntity.getCtnNum();
            List<PropertyFilter> filters2 = new ArrayList<PropertyFilter>();
            PropertyFilter filter2 = new PropertyFilter("EQS_ctnNum", null);
            filters2.add(filter2);
            List<BisPresenceBox> boxList = presenceBoxService.search(filters2);

            bisGateCar = getBisGateCar(searchBises1, bisGateCar);
            setProperties(bisGateCarParams.getCarNum(), null, inOutList, boxList, resultMap, bisGateCar);

        }

        return resultMap;
    }

    private void setProperties(String carNo, String ctnNo, List<BisInOut> inOutList, List<BisPresenceBox> boxList,
                               Map<String, Object> resultMap, BisGateCar bisGateCar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BisGateCarWrongInfo wrongInfo = new BisGateCarWrongInfo();
        Date now = DateUtils.getNow();
        String date = sdf.format(now);
        wrongInfo.setCreateDate(date);
        wrongInfo.setCarNum(carNo);
        if(ctnNo != null) {
            wrongInfo.setCtnNum(ctnNo);
        }

        if (!inOutList.isEmpty()) {
            resultMap.put("code", 0);
            resultMap.put("success", true);
            resultMap.put("msg", "success");

            wrongInfo.setReason("overCar");
            gateCarWrongInfoService.save(wrongInfo);
            return;
        }

        if (!boxList.isEmpty()) {
            resultMap.put("code", 0);
            resultMap.put("success", true);
            resultMap.put("msg", "success");

            wrongInfo.setReason("overBox");
            gateCarWrongInfoService.save(wrongInfo);
            return;
        }

        // 车辆信息
        bisGateCar.setCreateDate(now);
        bisGateCar.setCreateUser("接口调用");
        bisGateCar.setJobType("1");
        if(ctnNo != null) {
            bisGateCar.setCtnNum(ctnNo);
        }
        bisGateCar.setCarNum(carNo);
        bisGateCar.setCtnSize("40");
        bisGateCar.setCtnType("RF");

        // 进出场信息
        BisInOut inOut = new BisInOut();
        BeanUtils.copyProperties(bisGateCar, inOut);// 复制
        // 在场箱信息
        BisPresenceBox presenceBox = new BisPresenceBox();
        BeanUtils.copyProperties(bisGateCar, presenceBox);// 复制
        presenceBox.setState("1");

        gateCarService.save(bisGateCar);
        inOutService.save(inOut);
        presenceBoxService.save(presenceBox);

        resultMap.put("code", 0);
        resultMap.put("success", true);
        resultMap.put("msg", "success");
    }

    private BisGateCar getBisGateCar(Map<String, Object> searchBises, BisGateCar bisGateCar) {

        // 取出查询的值
        for (String key : searchBises.keySet()) {
            Object value = searchBises.get(key);
            if ("empty".equals(searchBises.get("str"))) {
                bisGateCar.setBisType("3");
            }
            if ("ent".equals(searchBises.get("type"))) {
                bisGateCar.setBisType("1"); // 1:入库
            } else if ("out".equals(searchBises.get("type"))) {
                bisGateCar.setBisType("2"); // 出库
            } else {
                bisGateCar.setBisType("4");
            }
            if ("billNum".equals(key)) {
                String billNum = (String) searchBises.get("billNum");
                bisGateCar.setBillNum(billNum);
            }
            if ("asn".equals(key)) {
                String asn = (String) searchBises.get("asn");
                bisGateCar.setAsn(asn);
            }
            if ("clientId".equals(key)) {
                String clientId = (String) searchBises.get("clientId");
                bisGateCar.setStockId(clientId);
            }
            if ("clientName".equals(key)) {
                String clientName = (String) searchBises.get("clientName");
                bisGateCar.setStockName(clientName);
            }
            if ("entNum".equals(key)) {
                Double entNum = (Double) searchBises.get("entNum");
                bisGateCar.setEnterNum(entNum);
            }
            if ("outNum".equals(key)) {
                Double outNum = (Double) searchBises.get("outNum");
                bisGateCar.setOutNum(outNum);
            }
            if ("loadingNum".equals(key)) {
                String loadingNum = (String) searchBises.get("loadingNum");
                bisGateCar.setLoadingNum(loadingNum);
            }
            // resultMap.put(key, value);
        }
        return bisGateCar;
    }

    /**
     * 寻找业务类型
     *
     * @return
     */
    private Map<String, Object> searchBis1(String carNo, String ctnNum) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("str", "empty");
        List<PropertyFilter> asnF = new ArrayList<PropertyFilter>();
        asnF.add(new PropertyFilter("EQS_ctnNum", ctnNum));
        asnF.add(new PropertyFilter("INAS_asnState", new String[] { "1", "2" }));
        List<BisAsn> asnList = asnService.findByF(asnF);
        if (!asnList.isEmpty()) {
            BisAsn asn = asnList.get(0);
            List<BisAsnInfo> infoList = asnInfoService.getASNInfoList(asn.getAsn());
            Double entNum = 0d;
            for (BisAsnInfo info : infoList) {
                entNum += info.getPiece();
            }
            resultMap.put("str", "success");
            resultMap.put("type", "ent");
            resultMap.put("billNum", asn.getBillNum());
            resultMap.put("asn", asn.getAsn());
            resultMap.put("clientId", asn.getStockIn());
            resultMap.put("clientName", asn.getStockName());
            resultMap.put("entNum", entNum);
            return resultMap;
        } else {
            List<PropertyFilter> orderF = new ArrayList<PropertyFilter>();
            orderF.add(new PropertyFilter("EQS_carNum", carNo));
            orderF.add(new PropertyFilter("INAS_orderState", new String[] { "1", "2", "3" }));
            List<BisLoadingOrder> orderList = loadingOrderService.search(orderF);
            if (!orderList.isEmpty()) {
                BisLoadingOrder order = orderList.get(0);
                List<BisLoadingOrderInfo> infoList = loadingOrderInfoService.getInfoList(order.getOrderNum());
                Double outNum = 0d;
                for (BisLoadingOrderInfo info : infoList) {
                    outNum += info.getPiece();
                }
                resultMap.put("str", "success");
                resultMap.put("type", "out");
                resultMap.put("outNum", outNum);
                resultMap.put("clientId", order.getStockIn());
                resultMap.put("clientName", order.getStockName());
                List<BisLoadingInfo> loadingList = loadingInfoService.getLoadingByOrder(order.getOrderNum());
                if (loadingList.isEmpty()) {
                    resultMap.put("loadingNum", "");
                } else {
                    resultMap.put("loadingNum", loadingList.get(0).getLoadingTruckNum());
                }
                return resultMap;
            } else {
                return resultMap;
            }
        }
    }

}