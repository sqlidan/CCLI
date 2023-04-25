package com.haiersoft.ccli.remoting.hand.platform.service;

import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.remoting.platform.base.JsonBean;
import com.haiersoft.ccli.remoting.platform.dao.StockWebDAO;
import com.haiersoft.ccli.report.entity.Stock;
import com.haiersoft.ccli.report.service.InStockReportService;
import com.haiersoft.ccli.wms.entity.BisPlanTransport;
import com.haiersoft.ccli.wms.service.PlanTransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.WebParam;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BusinessPlatformWebService implements IBusinessPlatform {

    @Autowired
    private StockWebDAO stockWebDAO;

    @Autowired
    private InStockReportService inStockReportService;

    @Autowired
    private PlanTransportService planTransportService;

    /**
     * 查询用户是否有查看库存的权限
     *
     * @param typeId     返回数据格式
     * @param clientName 客户名称
     * @return 0.有权限 1.无权限
     */
    @Override
    public String hasAuth(@WebParam(name = "typeId") String typeId,
                          @WebParam(name = "clientName") String clientName) {

        String auth = stockWebDAO.hasAuthWithStock(clientName);

        JsonBean<String> result = new JsonBean<>(typeId);
        result.setResult(auth);

        return result.toJson();
    }

    /**
     * @param typeId       返回数据格式
     * @param reportType   报表类型 1.普通客户 2.日本客户 3.OTE --必填
     * @param billCode     提单号
     * @param clientId     货主id(存货方id)                   --必填
     * @param cargoType    产品分类(产品小类)
     * @param startTime    入库日期起
     * @param endTime      入库日期止
     * @param locationType 1.有库位 2.无库位                  --必填
     */
    @Override
    //TODO:需要规格、生产日期、项目号、船名批号
    public String findStockList(@WebParam(name = "typeId") String typeId,
                                @WebParam(name = "reportType") String reportType,
                                @WebParam(name = "billCode") String billCode,
                                @WebParam(name = "clientId") String clientId,
                                @WebParam(name = "cargoType") String cargoType,
                                @WebParam(name = "mr") String mr,
                                @WebParam(name = "startTime") String startTime,
                                @WebParam(name = "endTime") String endTime,
                                @WebParam(name = "locationType") String locationType) {

        if (reportType == null || reportType.length() == 0) {
            JsonBean<String> result = new JsonBean<>("string");
            result.setCode(JsonBean.ERROR);
            result.setResult("报表类型不能为空");
            return result.toJson();
        }

        if (clientId == null || clientId.length() == 0) {
            JsonBean<String> result = new JsonBean<>("string");
            result.setCode(JsonBean.ERROR);
            result.setResult("货主名不能为空");
            return result.toJson();
        }

        if (locationType == null || locationType.length() == 0) {
            JsonBean<String> result = new JsonBean<>("string");
            result.setCode(JsonBean.ERROR);
            result.setResult("库位不能为空");
            return result.toJson();
        }

        Stock stock = new Stock();
        stock.setReportType(reportType);
        stock.setBillCode(billCode);
        stock.setClientId(clientId);
        stock.setCargoType(cargoType);
        stock.setStrartTime(startTime);
        stock.setEndTime(endTime);
        stock.setLocationType(locationType);
        stock.setCtnNum(mr);

        List<Map<String, Object>> listMap = null;

        if ("1".equals(stock.getReportType())) {
            listMap = inStockReportService.searchInStockReport(stock);
        } else if ("2".equals(stock.getReportType())) {
            listMap = inStockReportService.searchInStockReportRB(stock);
        } else if ("3".equals(stock.getReportType())) {
            listMap = inStockReportService.searchInStockReportOTE(stock);
        }

        JsonBean<List<Map<String, Object>>> result = new JsonBean<>(typeId);
        result.setResult(listMap);

        return result.toJson();
    }

    /**
     * 添加一条预约计划，计划状态设置为未审核
     *
     * @param typeId     返回数据格式
     * @param planCode   预约单号
     * @param planTime   预约时间 格式:0800
     * @param planDate   预约日期 格式:yyyy-MM-dd
     * @param client     客户
     * @param clientName 客户名
     * @param billNum    提单号
     * @param ctnNum     箱号
     * @param sku        SKU
     * @param loadNum    提货量
     * @param quantity   件数
     * @param carCode    车牌号
     * @param idCode     身份证号
     * @param cargoName  产品名
     */
    @Override
    public String addPlan(@WebParam(name = "typeId") String typeId,
                          @WebParam(name = "planCode") String planCode,
                          @WebParam(name = "planTime") String planTime,
                          @WebParam(name = "planDate") String planDate,
                          @WebParam(name = "client") String client,
                          @WebParam(name = "clientName") String clientName,
                          @WebParam(name = "billNum") String billNum,
                          @WebParam(name = "ctnNum") String ctnNum,
                          @WebParam(name = "sku") String sku,
                          @WebParam(name = "loadNum") String loadNum,
                          @WebParam(name = "quantity") String quantity,
                          @WebParam(name = "carCode") String carCode,
                          @WebParam(name = "idCode") String idCode,
                          @WebParam(name = "cargoName") String cargoName) {

        Date now = new Date();

        Date date = DateUtils.toDate(planDate, "yyyy-MM-dd");

        BisPlanTransport entity = new BisPlanTransport();
        entity.setPlanCode(planCode);
        entity.setPlanTime(planTime);
        entity.setPlanDate(date);
        entity.setClient(client);
        entity.setClientName(clientName);
        entity.setBillNum(billNum);
        entity.setCtnNum(ctnNum);
        entity.setSkuId(sku);
        entity.setLoadNum(loadNum);
        entity.setQuantity(quantity);
        entity.setCarCode(carCode);
        entity.setIdCode(idCode);
        entity.setCargoName(cargoName);

        entity.setCreateDate(now);
        entity.setCheckState("0");

        planTransportService.merge(entity);

        JsonBean<String> result = new JsonBean<>(typeId);
        result.setResult("success");

        return result.toJson();
    }
    /**
             库存查询对预约平台
    * @param billCode     提单号
    * @param clientId     货主id(存货方id) ※必填
    * @param ctnNum     箱号
   * @return {"code":"0","result":"json数据","type":"list"}
     */
    @Override
    //TODO:需要规格、生产日期、项目号、船名批号
    public String findInventoryList( @WebParam(name = "billCode") String billCode,
                                @WebParam(name = "clientId") String clientId,
                                @WebParam(name = "ctnNum") String ctnNum) {
/*    	如果是 青岛新协航国际物流有限公司(XXH)  它对应可查看库存的客户信息如下：
    	青岛中和永投资控股有限公司  3710
                    青岛中和永投资控股有限公司（保税区库）3112
    	青岛中凯峰经贸有限公司(ZKF)  659
    	青岛中凯峰经贸有限公司(保税区库) 3709
    	齐鲁交通青岛保税港区发展有限公司3240
    	青岛新协航国际物流有限公司(XXH) 657
    	青岛新协航国际物流有限公司（保税区库）3712 3732 */
 
    	if("xxh".equals(clientId)){
    		clientId="(tray.STOCK_IN='3710' or tray.STOCK_IN='3112' or tray.STOCK_IN='659'"
    				+ "or tray.STOCK_IN='3709'or tray.STOCK_IN='3240'or tray.STOCK_IN='657'"
    				+ "or tray.STOCK_IN='3712'or tray.STOCK_IN='3732')";
    	}   	   	
    	Stock stock = new Stock();

    	stock.setBillCode(billCode);
    	stock.setCtnNum(ctnNum);
        List<Map<String, Object>> listMap = null;
         
        listMap = inStockReportService.findInventoryList(stock,clientId);
       
        JsonBean<List<Map<String, Object>>> result = new JsonBean<>();
        result.setResult(listMap);

        return result.toJson();
    }
}
