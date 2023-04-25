package com.haiersoft.ccli.wms.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.api.entity.ApiPledge;
import com.haiersoft.ccli.api.service.ApiPledgeService;
import com.haiersoft.ccli.api.service.PledgeDynamicService;
import com.haiersoft.ccli.api.service.PledgeStaticService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.wms.entity.BaseClientPledge;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import com.haiersoft.ccli.wms.service.ClientPledgeService;
import com.haiersoft.ccli.wms.service.PledgeComfirmService;
import com.haiersoft.ccli.wms.service.TrayInfoService;
@Controller
@RequestMapping("pledge/comfirm")
public class PledgeComfirmController extends BaseController{
    @Autowired
    ApiPledgeService apiPledgeService;

    @Autowired
    TrayInfoService trayInfoService;

    @Autowired
    PledgeComfirmService pledgeComfirmService;

    @Autowired
    PledgeStaticService pledgeStaticService;

    @Autowired
    PledgeDynamicService pledgeDynamicService;

    /*跳转静态列表页面*/
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String menuList() {
        return "wms/pledge/pledgeComfirm";
    }

    /*跳转动态列表页面*/
    @RequestMapping(value = "activeList", method = RequestMethod.GET)
    public String activeList() {
        return "wms/pledge/pledgeActiveComfirm";
    }

    /**
     * @return
     * @throws
     * @author wangxiang
     * @Description: 静态质押监管查询
     * @date 2020年9月14日 下午5:00:10
     */
    @RequestMapping(value = "json", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request) {
        Page<ApiPledge> page = getPage(request);
        Map<String, Object> map = PropertyFilter.buildFromHttpRequestMap(request);
        page = pledgeComfirmService.seachStaticApiPledgeSql(page, map);
        return getEasyUIData(page);
    }

    /**
     * @return
     * @throws
     * @author wangxiang
     * @Description: 静态质押监管查询
     * @date 2020年9月14日 下午5:00:10
     */
    @RequestMapping(value = "json2", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData2(HttpServletRequest request) {
        Page<ApiPledge> page = getPage(request);
        Map<String, Object> map = PropertyFilter.buildFromHttpRequestMap(request);
        page = pledgeComfirmService.seachActiveApiPledgeSql(page, map);
        return getEasyUIData(page);
    }

    @Autowired
    ClientPledgeService clientPledgeService;

    //质押生效
    @RequestMapping(value = "save/{id}")
    @ResponseBody
    @Transactional
    public String comfirm(@PathVariable("id") String id) {
        String result = "";

        ApiPledge info = pledgeComfirmService.get(id);
		// 静态质押
        if (info.getState() == 1)
        {

            TrayInfo trayInfo = trayInfoService.get(info.getTrayInfoId());
            // 静态质押，先查询当前库存

            // 如果当前库存(可质押数量/件数)小于本次质押数量/件数
            if (info.getPledgeNumber() > trayInfo.getNowPiece()) {
                return "质押数量超出限制！";
            }

            // 检查质押数量或重量是否超出已静态质押数量（检查存在多条质押/解押记录时）
            HashMap<String, Object> pledgedMap = apiPledgeService.countTotalPledgedByTrayInfoId(info.getTrayInfoId());
            Integer pledgedNum = Integer.valueOf(pledgedMap.get("sumNum").toString());
            if (info.getPledgeNumber() > (trayInfo.getNowPiece() - pledgedNum)) {
                return "质押数量超过本条库存可质押的数量！";
            }

            //质押件数总量判断
            // 检查质押数量或重量是否超出可质押数量（总量）
            if (!apiPledgeService.pledgeCountCheck(info)) {
                return "质押数量或质押重量超出可质押数量！";
            }


            // 修改库存表的质押件数/重量字段
            // 如果当前库存表中的可质押件数/重量为空时，即之前没有做过供应链质押，则可质押件数/重量为本次质押的件数/重量
            if (null == trayInfo.getPledgePiece() || null == trayInfo.getPledgeGrossWeight()) {
                trayInfo.setPledgePiece(info.getPledgeNumber());
                trayInfo.setPledgeGrossWeight(info.getPledgeWeight());
            }
            //如果不为空时,质押数量等于之前的质押数量加本次质押数量
            else {
                trayInfo.setPledgePiece(trayInfo.getPledgePiece() + info.getPledgeNumber());
                trayInfo.setPledgeGrossWeight(trayInfo.getPledgeGrossWeight() + info.getPledgeWeight());
            }

            trayInfo.setIfTransfer("3");
            trayInfoService.update(trayInfo);

            info.setConfirmStatus(1);
            info.setComfirmDate(new Date());
            // 修改质押表
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

            //同步静态质押监管（解除）指令结果
            pledgeStaticService.getApiPledge(id);

            return "success";

        }
		// 静态解押
        else if (info.getState() == 0)
        {
            ApiPledge pledged = apiPledgeService.findUniqueByTrendId(info.getRelatedTrendId());

            // 如果解押数量大于原质押数量
            if (info.getPledgeNumber() > pledged.getPledgeNumber()) {
                return "解押数量或重量超出当前质押数量或重量";
            }
            TrayInfo trayInfo = trayInfoService.get(info.getTrayInfoId());

            Integer trayInfoPledgePiece = 0;
            if (null == trayInfo.getPledgePiece()) {
                trayInfoPledgePiece = trayInfo.getNowPiece();
            } else {
                trayInfoPledgePiece = trayInfo.getPledgePiece();
            }
            // 库存表可解押数量计算
            // 如果解押数量大于库存表中的 质押数量
            if (info.getPledgeNumber() > trayInfoPledgePiece) {
                return "解押数量或重量超出当前质押数量或重量";
            }

//			HashMap<String, Object> pledgedMap= apiPledgeService.countTotalPledgedByTrayInfoId(Integer.valueOf(trayInfo.getId()));
//			Integer pledgedNum = Integer.valueOf(pledgedMap.get("sumNum").toString());
//
//			if (info.getPledgeNumber() > pledgedNum ) {
//				return "本次解押数量大于该库存总质押数量！";
//			} 

            //解押数量不能超出trend_id的数量
            HashMap<String, Object> pledgedRelatedMap = apiPledgeService.countRelatedTrendIdByRelatedTrendId(info.getRelatedTrendId());
            Integer pledgedRelatedNum = Integer.valueOf(pledgedRelatedMap.get("sumNum").toString());
            Integer pledgedRelatedWeight = Integer.valueOf(pledgedRelatedMap.get("sumWeight").toString());
            if (info.getPledgeNumber() > pledgedRelatedNum) {
                return "本次解押数量超出限制！";
            }

            info.setConfirmStatus(1);
            info.setComfirmDate(new Date());
            apiPledgeService.update(info);

            //设置库存表中的质押数量为本次解押的数量
            //trayInfo.setPledgePiece(trayInfo.getPledgePiece() - info.getPledgeNumber());
            //trayInfo.setPledgeGrossWeight(trayInfo.getPledgeGrossWeight() - info.getPledgeWeight());
            //查询该条库存所有质押中数量总和
//			HashMap<String, Object> sumStaticPledged = apiPledgeService.sumStaticPledgedNum(trayInfo.getId());
//			Integer sumStaticPledgedNum = Integer.valueOf(sumStaticPledged.get("sumNum").toString());
//			Integer sumStaticPledgedWeight = Integer.valueOf(sumStaticPledged.get("sumWeight").toString());


            trayInfo.setPledgePiece(trayInfo.getPledgePiece() - (pledgedRelatedNum - info.getPledgeNumber()));
            trayInfo.setPledgeGrossWeight(trayInfo.getPledgeGrossWeight() - (pledgedRelatedWeight.doubleValue() - info.getPledgeWeight()));

            // 如果库存表记录中的质押件数为0时，设置IfTransfer标志为0正常
            if (0 == trayInfo.getPledgePiece()) {
                trayInfo.setIfTransfer("0");
            }
            trayInfoService.update(trayInfo);

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

            //同步静态质押监管（解除）指令结果
            pledgeStaticService.getApiPledge(id);
            return "success";
        }

        if (info.getState() == 2) // 动态质押
        {
            if (!apiPledgeService.pledgeCountCheck(info)) // 动态质押确认时检查库存数量是否可以满足质押
            {
                return "最低质押数量或最低质押重量超出可质押数量！";

            }
//			BaseClientPledge clientPledge = new BaseClientPledge();
//			clientPledge.setBillNum(info.getBillNum());
//			clientPledge.setClientName(info.getCustomerName());
//			clientPledge.setCtnNum(info.getCtnNum());
//			clientPledge.setCuser(info.getAccountId());
//			clientPledge.setSkuId(info.getSku());
//			clientPledge.setNum(info.getPledgeNumber().doubleValue());
//			clientPledge.setCodeNum(info.getTrendId());
//			clientPledge.setClient(info.getCustomerId().toString());
//			clientPledge.setPname(info.getpName());
//			clientPledge.setCtime(new Date());
//			clientPledge.setPtype(2);
//			clientPledge.setPclass(2);
//			clientPledge.setNetWeight(info.getPledgeWeight());
//			clientPledge.setWarehouse(info.getWareHouse());
//			clientPledge.setWarehouseId(info.getWareHouseId());
//			clientPledge.setEnterState(info.getEnterState());
//			clientPledge.setTrack("1");
//			clientPledgeService.save(clientPledge);

        }
        if (info.getState() == 3) // 动态解押
        {
            List<ApiPledge> record = apiPledgeService.findOrderedListByRelatedTrendId(info.getRelatedTrendId());

            if (info.getPledgeNumber() > record.get(0).getPledgeNumber()) {
                return "解除质押的重量或数量超出当前质押重量或数量！";
            }

//			BaseClientPledge clientPledge = new BaseClientPledge();
//			clientPledge.setBillNum(info.getBillNum());
//			clientPledge.setClientName(info.getCustomerName());
//			clientPledge.setCtnNum(info.getCtnNum());
//			clientPledge.setCuser(info.getAccountId());
//			clientPledge.setSkuId(info.getSku());
//			clientPledge.setNum(0 - info.getPledgeNumber().doubleValue());
//			clientPledge.setCodeNum(info.getTrendId());
//			clientPledge.setClient(info.getCustomerId().toString());
//			clientPledge.setPname(info.getpName());
//			clientPledge.setCtime(new Date());
//			clientPledge.setPtype(2);
//			clientPledge.setPclass(2);
//			clientPledge.setNetWeight(0 - info.getPledgeWeight());
//			clientPledge.setWarehouse(info.getWareHouse());
//			clientPledge.setWarehouseId(info.getWareHouseId());
//			clientPledge.setEnterState(info.getEnterState());
//			clientPledge.setTrack("1");
//			clientPledgeService.save(clientPledge);
        }

        info.setConfirmStatus(1);
        info.setComfirmDate(new Date());
        // 修改质押表
        pledgeComfirmService.update(info);
        //动态 同步确认状态
        pledgeDynamicService.getApiPledge(info.getId());
        return "操作成功";
    }

    @RequestMapping(value = "refuse/{id}")
    @ResponseBody
    public String refuse(@PathVariable("id") String id) {
        String result = "";
        ApiPledge info = pledgeComfirmService.get(id);
        info.setConfirmStatus(2);
        info.setComfirmDate(new Date());
        //修改质押表
        pledgeComfirmService.update(info);
        if (info.getState() == 0 || info.getState() == 1) {
            //静态 同步驳回状态
            pledgeStaticService.getApiPledgeFail(info.getId());
        } else {
            //动态 同步驳回状态
            pledgeDynamicService.getApiPledgeFail(info.getId());
        }

        result = "success";

        return result;
    }

}
