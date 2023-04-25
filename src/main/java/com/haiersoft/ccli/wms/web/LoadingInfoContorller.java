package com.haiersoft.ccli.wms.web;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.wms.entity.BisLoadingInfo;
import com.haiersoft.ccli.wms.entity.BisLoadingOrder;
import com.haiersoft.ccli.wms.entity.SplitStockModel;
import com.haiersoft.ccli.wms.service.LoadingInfoService;
import com.haiersoft.ccli.wms.service.LoadingOrderService;
import com.haiersoft.ccli.wms.service.TransferService;
/**
 * 装车单管理
 *
 * @author LZG
 */
@Controller
@RequestMapping("bis/truck")
public class LoadingInfoContorller extends BaseController {
    @Autowired
    private LoadingInfoService loadingInfoService;
    @Autowired
    private LoadingOrderService loadingOrderService;//订单
    @Autowired
    private ClientService clientService;//客户
    @Autowired
    private TransferService transferService;//货转
  
    
    /*跳转装车单列表页面*/
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String List() {
        return "wms/truckloading/loadingList";
    }

    /*
     * 列表页面table获取json
     * */
    @RequiresPermissions("bis:truck:view")
    @RequestMapping(value = "listjson", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request) {
        Page<BisLoadingInfo> page = getPage(request);
        page.orderBy("id").order(Page.DESC);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        page = loadingInfoService.search(page, filters);
        return getEasyUIData(page);
    }

    /***
     * 根据订单号，拣货策略生成装车单
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> createTruck(HttpServletRequest request) {
        Map<String, Object> getList = null;
        if (request.getParameter("ordid") != null && request.getParameter("clcode") != null) {
            String ordCode = request.getParameter("ordid").toString();//订单编码
            String clCode = request.getParameter("clcode").toString();//策略编码
            getList = loadingInfoService.createTruck(ordCode, clCode, "");
        }
        return getList;
    }

    /***
     * 根据订单号判断是否可以生成装车单
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "iscreate", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> isCreateTruck(HttpServletRequest request) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        if (request.getParameter("ordid") != null) {
            String ordCode = request.getParameter("ordid").toString();//订单编码
            retMap = loadingInfoService.isCreateTruck(ordCode);
        } else {
            retMap.put("endStr", "error");
        }
        return retMap;
    }

    /***
     * 根据订单号，拣货策略生成装车单
     *
     * @return
     */
    @RequestMapping(value = "createct", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> createCTTruck(@Valid SplitStockModel spStock, Model model) {
        Map<String, Object> getList = null;
        if (spStock.getOrdId() != null && !"".equals(spStock.getOrdId())) {
            //根据订单类型调整到不同的处理
            //货转单//出库订单
            if ("T".equals(spStock.getOrdId().substring(0, 1))) {
                getList = transferService.createCTTransferTrayInfo(spStock);
            } else {
                getList = loadingInfoService.createCTLoadingInfo(spStock);
            }
        }
        return getList;
    }

    /**
     * 打印装车单
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "print/{trucknum}", method = RequestMethod.GET)
    public String print(@PathVariable("trucknum") String truckNum, Model model) {
        if (truckNum != null && !"".equals(truckNum)) {
            List<BisLoadingInfo> getObjList = loadingInfoService.getLoadingByNum(truckNum);
            Integer sumpiece=0;
            Double sumnet=0.0000;
            Double sumgross=0.0000;
            if (getObjList != null && getObjList.size() > 0) {
	            for (BisLoadingInfo bisLoadingInfo : getObjList) {
	            	sumpiece += bisLoadingInfo.getPiece();
	            	sumnet += bisLoadingInfo.getNetWeight();
	            	sumgross+=bisLoadingInfo.getGrossWeight();
				}
	            //按区位号、箱号、SKU分组统计数量
	            getObjList=this.groupBy(getObjList);
	            String sumNet = new DecimalFormat("##.####").format(sumnet);
	            String sumGross = new DecimalFormat("##.####").format(sumgross);
                BisLoadingInfo getObj = getObjList.get(0);
                model.addAttribute("tnum", getObj.getLoadingTruckNum());
                BisLoadingOrder ordObj = loadingOrderService.get(getObj.getLoadingPlanNum());
                if (ordObj != null) {
                    BaseClientInfo client = clientService.get(Integer.valueOf(ordObj.getReceiverId()));
                    if (client != null) {
                        model.addAttribute("user", client.getClientName());
                        model.addAttribute("adress", client.getAddress());
                        model.addAttribute("isok", 1);
                        model.addAttribute("sumpiece", sumpiece);
                        model.addAttribute("sumnet", sumNet);
                        model.addAttribute("sumgross", sumGross);
                    }
                } else {
                    model.addAttribute("isok", 0);
                    model.addAttribute("user", "出库订单未查询到");
                    model.addAttribute("adress", "出库订单未查询到");
                    model.addAttribute("sumpiece", sumpiece);
                    model.addAttribute("sumnet", sumNet);
                    model.addAttribute("sumgross", sumGross);
                }
                model.addAttribute("infolist", getObjList);
            }
        }
        return "wms/truckloading/print";
    }

    //按区位号、箱号、SKU分组统计数量
    private List<BisLoadingInfo> groupBy(List<BisLoadingInfo> getObjList) {
	    int size=getObjList.size();
	    List<BisLoadingInfo> newObjList=new ArrayList<BisLoadingInfo>();
	    //int c=-1;
	    int piece=0;
	    BisLoadingInfo newInfo = new BisLoadingInfo();
    	for(int i=0;i<size;i++){
	    	if(i==0){
	    		newObjList.add(getObjList.get(0));
	    		piece = getObjList.get(0).getPiece();
	    	}else{
	    		if(!getObjList.get(i).getAreaNum().equals(getObjList.get(i-1).getAreaNum()) || !getObjList.get(i).getCtnNum().equals(getObjList.get(i-1).getCtnNum()) || !getObjList.get(i).getSkuId().equals(getObjList.get(i-1).getSkuId())){
	    			newInfo.setAreaNum("小计");
	    			newInfo.setPiece(piece);
	    			newObjList.add(newInfo);
	    			newObjList.add(getObjList.get(i));
	    			piece = getObjList.get(0).getPiece();
	    		}else{
	    			newObjList.add(getObjList.get(i));
		    		piece += getObjList.get(i).getPiece();
	    		}
	    	}
	    	if(i==size-1){
	    		newInfo.setAreaNum("小计");
    			newInfo.setPiece(piece);
    			newObjList.add(newInfo);
	    	}
	    }
    	return newObjList;
	}

	/**
     * 打印装车清单
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "printinfo/{trucknum}", method = RequestMethod.GET)
    public String printInfo(@PathVariable("trucknum") String truckNum, Model model) {
        if (truckNum != null && !"".equals(truckNum)) {
            List<BisLoadingInfo> theList = loadingInfoService.getQingDan(truckNum);
            List<BisLoadingInfo> getObjList = loadingInfoService.getLoadingByNum(truckNum);
            if (getObjList != null && getObjList.size() > 0) {
                BisLoadingInfo getObj = getObjList.get(0);
                model.addAttribute("tnum", getObj.getLoadingTruckNum());
                BisLoadingOrder ordObj = loadingOrderService.get(getObj.getLoadingPlanNum());
                if (ordObj != null) {
                    BaseClientInfo client = clientService.get(Integer.valueOf(ordObj.getReceiverId()));
                    if (client != null) {
                        model.addAttribute("user", client.getClientName());
                        model.addAttribute("adress", client.getAddress());
                        model.addAttribute("car", getObj.getCarNo());
                        model.addAttribute("now", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
                        model.addAttribute("isok", 1);
                    }
                } else {
                    model.addAttribute("isok", 0);
                    model.addAttribute("user", "出库订单未查询到");
                    model.addAttribute("adress", "出库订单未查询到");
                }
                model.addAttribute("infolist", theList);
            }
        }
        return "wms/truckloading/printInfo";
    }

    /*
     * 判断出库订单是否已制作装车单
     * */
    @RequestMapping(value = "ifmakeloading/{orderId}", method = RequestMethod.GET)
    @ResponseBody
    public String ifmakeloading(@PathVariable("orderId") String orderId) {
        List<BisLoadingInfo> infoList = loadingInfoService.getLoadingByOrder(orderId);
        if (!infoList.isEmpty()) {
            return "success";
        } else {
            return "false";
        }
    }

    /***
     * 判断当前装车单是否可以删除
     *
     * @param truckNum 装车单号
     * @param model
     * @return
     */
    @RequestMapping(value = "isdel/{trucknum}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> isDel(@PathVariable("trucknum") String truckNum, Model model) {
        Map<String, Object> getMap = new HashMap<String, Object>();
        getMap.put("endstr", "error");
        if (truckNum != null && !"".equals(truckNum)) {
            List<BisLoadingInfo> listObj = loadingInfoService.getLoadingByNum(truckNum);
            String outLinkCode = null;//出庫联系单号
            if (listObj != null && listObj.size() > 0) {
                //判断装车单状态
                for (BisLoadingInfo obj : listObj) {
                    if (outLinkCode == null) {
                        outLinkCode = obj.getOutLinkId();
                    }
                    if (!"0".equals(obj.getLoadingState())) {
                        getMap.put("endstr", "truckerror");
                        return getMap;
                    }
                }
                //判断出庫联系单是否生成了最后一车
                if (outLinkCode != null) {
                    List<Map<String, Object>> getList = loadingInfoService.isLastCar(outLinkCode);
                    if (getList != null && getList.size() > 0) {
                        getMap.put("endstr", "outerror");
                        return getMap;
                    }
                }
                getMap.put("endstr", "success");
            }
        }
        return getMap;
    }

    /***
     * 装车单删除
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "zxdel/{ordnum}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> zxDel(@PathVariable("ordnum") String ordnum, Model model) {
        Map<String, Object> getMap = new HashMap<String, Object>();
        getMap.put("endstr", "error");
        if (ordnum != null && !"".equals(ordnum)) {
            boolean isClear = loadingInfoService.deleteClearTruck(ordnum);
            if (isClear == true) {
                BisLoadingOrder ordObj = loadingOrderService.get(ordnum);
                ordObj.setOrderState("1");
                loadingOrderService.update(ordObj);
                getMap.put("endstr", "success");
            }
        }
        return getMap;
    }

    /***
     * 根据查询条件求总计
     *
     * @return
     */
    @RequestMapping(value = "findall", method = RequestMethod.POST)
    @ResponseBody
    public List<String> zxDel(HttpServletRequest request, String loadingPlanNum, String loadingTruckNum, String outLinkId, String billNum, String carNo, String operator, String stockId) {
        List<Map<String, Object>> objList = loadingInfoService.findallnum(loadingPlanNum, loadingTruckNum, outLinkId, billNum, carNo, operator, stockId);
        List<String> obj = new ArrayList<String>();
        if (!objList.isEmpty()) {
            Integer a = objList.get(0).get("PIECE") != null ? ((BigDecimal) objList.get(0).get("PIECE")).intValue() : 0;
            obj.add(a.toString());

            DecimalFormat df = new DecimalFormat("###############0.00 ");//   16位整数位，两小数位
            Double b = objList.get(0).get("NET_WEIGHT") != null ? ((BigDecimal) objList.get(0).get("NET_WEIGHT")).doubleValue() : 0.00;
            obj.add(df.format(b));
            Double c = objList.get(0).get("GROSS_WEIGHT") != null ? ((BigDecimal) objList.get(0).get("GROSS_WEIGHT")).doubleValue() : 0.00;
            obj.add(df.format(c));
        } else {
            obj.add("0");
            obj.add("0.00");
            obj.add("0.00");
        }
        return obj;
    }

    /***
     * 装车单备注
     *
     * @param ordId 订单号
     * @param tNum  装车单号
     * @param model
     * @return
     */
    @RequestMapping(value = "remark/{ordId}/{tNum}", method = RequestMethod.POST)
    @ResponseBody
    public String remark(@PathVariable("ordId") String ordId, @PathVariable("tNum") String tNum) {
        loadingInfoService.insertRemark(ordId, tNum);
        return "success";
    }

    // \/\/\/\/\/\/\/\/\/\/\/\/\/\/ after 2016.8.12 'ddd' create \/\/\/\/\/\/\/\/\/\/\/\/\/\/

    /**
     * 通过出库订单号统计库件数合计
     *
     * @param orderNum 出库订单号
     * @return
     */
    @RequestMapping(value = "getOrderSumNum/{orderNum}", method = RequestMethod.GET)
    @ResponseBody
    public String getOrderSumNum(@PathVariable("orderNum") String orderNum) {

        int sum = 0;

        List<Map<String, Object>> list = loadingOrderService.getOrderSumNum(orderNum);

        for (Map<String, Object> map : list) {
            String number = map.get("PIECE").toString();
            sum += Integer.valueOf(number);
        }

        return String.valueOf(sum);
    }

    /**
     * 通过装车单号查询托盘信息
     *
     * @param truckNum 装车号
     * @return
     */
    @RequestMapping(value = "endOrderFind/{truckNum}", method = RequestMethod.GET)
    @ResponseBody
    public String endOrderFind(@PathVariable("truckNum") String truckNum) {

        List<Map<String, String>> list = loadingInfoService.getLoadingOrderInfo(truckNum);

        int sum = list.size();
        int okNum = 0;      //已装车托盘计数
        int backNum = 0;    //已回库托盘计数

        for (Map<String, String> map : list) {

            String state = map.get("LOADING_STATE");

            if ("1".equals(state)) return "1";      // 装车单有托盘已拣货,不能完结
            else if ("5".equals(state)) return "5"; // 装车单有托盘已回库理货,不能完结

            else if ("2".equals(state)) okNum++;    // 已装车托盘计数
            else if ("3".equals(state)) sum--;      // 将装车单中已置换托盘不计数
            else if ("6".equals(state)) backNum++;  // 已回库托盘计数

        }

        if (okNum == sum) return "0";
        if (backNum == sum) return "6";

        StringBuffer sb = new StringBuffer();

        sb.append("装车号(").append(truckNum).append(")共有").append(sum).append("个托盘。");

        sb.append("其中").append(okNum).append("个托盘已装车,");

        if (backNum > 0) sb.append(backNum + "个托盘已回库,");

        sb.append("剩余").append((sum - okNum - backNum)).append("个托盘,");
        sb.append("确定对其进行完结?");

        return sb.toString();
    }

    /**
     * 通过装车号将分配状态的托盘重置回上架状态
     *
     * @param truckNum 装车号
     * @return
     */
    @RequestMapping(value = "endOrder/{truckNum}/{outLinkId}/{loadingPlanNum}", method = RequestMethod.POST)
    @ResponseBody
    public String endOrder(@PathVariable("truckNum") String truckNum,
                           @PathVariable("outLinkId") String outLinkId,
                           @PathVariable("loadingPlanNum") String loadingPlanNum) {
        //处理ASN区间表
        this.updateAsnAction(truckNum, outLinkId, loadingPlanNum);
        List<Map<String, String>> list = loadingInfoService.getLoadingOrderInfo(truckNum);

        for (Map<String, String> map : list) {

            String state = map.get("LOADING_STATE");
            String trayId = map.get("TRAY_ID");

            if ("0".equals(state)) {
                loadingInfoService.updateLoadingOrderAndTrayInfo(trayId);
            }

        }

        return "ok";
    }

    private void updateAsnAction(String truckNum, String outLinkId, String loadingPlanNum) {
        loadingInfoService.updateAsnAction(truckNum, outLinkId, loadingPlanNum);
    }


}
