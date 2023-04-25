package com.haiersoft.ccli.junit;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * 电商平台接口
 */
@WebService(targetNamespace = "http://penghaisoft.com/")
public interface IBusinessPlatform {

    /**
     * 查询用户是否有查看库存的权限
     *
     * @param typeId     返回数据格式
     * @param clientName 客户名称 ※必填
     *
     * @return {"code":"0","result":"0","type":"string"} 0.有权限 1.无权限
     */
    String hasAuth(@WebParam(name = "typeId") String typeId,
                   @WebParam(name = "clientName") String clientName);

    /**
     * @param typeId       返回数据格式
     * @param reportType   报表类型 1.普通客户 2.日本客户 3.OTE ※必填
     * @param billCode     提单号
     * @param clientId     货主id(存货方id) ※必填
     * @param cargoType    产品分类(产品小类)
     * @param startTime    入库日期起
     * @param endTime      入库日期止
     * @param locationType 1.有库位 2.无库位 ※必填
     *
     * @return {"code":"0","result":"json数据","type":"list"}
     */
    String findStockList(@WebParam(name = "typeId") String typeId,
                         @WebParam(name = "reportType") String reportType,
                         @WebParam(name = "billCode") String billCode,
                         @WebParam(name = "clientId") String clientId,
                         @WebParam(name = "cargoType") String cargoType,
                         @WebParam(name = "startTime") String startTime,
                         @WebParam(name = "endTime") String endTime,
                         @WebParam(name = "locationType") String locationType);

    /**
     * 添加一条预约计划，计划状态设置为未审核
     *
     * @param typeId     返回数据格式
     * @param planCode   预约单号
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
     *
     * @return {"code":"0","result":"success","type":"string"} success.成功
     */
    String addPlan(@WebParam(name = "typeId") String typeId,
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
                   @WebParam(name = "cargoName") String cargoName);

}
