package com.haiersoft.ccli.wms.web;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.wms.entity.PreEntryInvtQuery.BisPreEntryInvtQuery;
import com.haiersoft.ccli.wms.entity.apiEntity.InvtListType;
import com.haiersoft.ccli.wms.service.PreEntryInvtQuery.PreEntryInvtQueryService;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.BaseClientRank;
import com.haiersoft.ccli.base.entity.BaseSkuBaseInfo;
import com.haiersoft.ccli.base.service.ClientRankService;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.base.service.SkuInfoService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.PropertiesUtil;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.BaseClientPledge;
import com.haiersoft.ccli.wms.entity.BisEnterStockInfo;
import com.haiersoft.ccli.wms.entity.BisOutStock;
import com.haiersoft.ccli.wms.entity.BisOutStockInfo;
import com.haiersoft.ccli.wms.entity.OutStockInfoToExcel;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import com.haiersoft.ccli.wms.service.ClientPledgeService;
import com.haiersoft.ccli.wms.service.EnterStockInfoService;
import com.haiersoft.ccli.wms.service.OutStockInfoService;
import com.haiersoft.ccli.wms.service.OutStockService;
import com.haiersoft.ccli.wms.service.TrayInfoService;

/**
 * @author pyl
 * @ClassName: OutStockInfoController
 * @Description: 出库联系单明细Controller
 * @date 2016年3月12日 下午4:12:22
 */
@Controller
@RequestMapping("wms/outstockinfo")
public class OutStockInfoController extends BaseController {

    @Autowired
    private OutStockService outStockService;
    @Autowired
    private OutStockInfoService outStockInfoService;
    @Autowired
    private SkuInfoService skuInfoService;
    @Autowired
    private TrayInfoService trayInfoService;
    @Autowired
    private ClientPledgeService clientPledgeService;
    @Autowired
    private EnterStockInfoService enterStockInfoService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientRankService clientRankService;
    @Autowired
    private PreEntryInvtQueryService preEntryInvtQueryService;

    /**
     * @return
     * @throws
     * @author pyl
     * @Description: 出库联系单明细查询
     * @date 2016年3月3日 下午2:06:10
     */
    @RequestMapping(value = "json/{outLinkId}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request, @PathVariable("outLinkId") String outLinkId) {
        Page<BisOutStockInfo> page = getPage(request);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        PropertyFilter filter = new PropertyFilter("EQS_outLinkId", outLinkId);
        filters.add(filter);
        page = outStockInfoService.search(page, filters);
        return getEasyUIData(page);
    }

    /**
     * @param outLinkId
     * @return
     * @throws
     * @author Connor.M
     * @Description: 根据联系单  获得  提单数据
     * @date 2016年4月15日 上午10:28:03
     */
    @RequestMapping(value = "getOutStockInfoByLinkId", method = RequestMethod.GET)
    @ResponseBody
    public List<BisOutStockInfo> getOutStockInfoByLinkId(@RequestParam String linkId) {
        List<BisOutStockInfo> outStockInfos = new ArrayList<BisOutStockInfo>();
//		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
//		filters.add(new PropertyFilter("EQS_outLinkId", linkId));
        BisOutStockInfo outStockInfo = new BisOutStockInfo();
        outStockInfo.setOutLinkId(linkId);
        outStockInfos = outStockInfoService.searchBillCodeByLinkId(outStockInfo);
        return outStockInfos;
    }

    /**
     * @param linkId 出库联系单id
     * @Description: 添加出库联系单明细跳转
     */
    @RequestMapping(value = "addinfo/{outLinkId}", method = RequestMethod.GET)
    public String addinfo(@PathVariable("outLinkId") String linkId, Model model) {
        TrayInfo newObj = new TrayInfo();
        if (linkId != null && !"000000".equals(linkId)) {
            // 根据出库联系单id获取出库联系单对象
            BisOutStock outStock = outStockService.get(linkId);
            if (outStock != null) {
                newObj.setStockIn(outStock.getStockInId());//存货方id
                newObj.setStockName(outStock.getStockIn());//存货方名称
                newObj.setWarehouseId(outStock.getWarehouseId());
                newObj.setContactNum(linkId);
            }
        }
        model.addAttribute("trayInfo", newObj);
        return "wms/outstock/outStockInfoForm";
    }


    /**
     * 添加出库联系单明细
     *
     * @param user
     * @param model
     */
    @RequestMapping(value = "addoutinfo", method = RequestMethod.GET)
    @ResponseBody
    public String create(Integer outNum, String billNum, String ctnNum, String outLinkId,String asn, String sku, String saleNum, String enterState, String rkNum,Integer codeNum,HttpServletRequest request) {
        //2024-12-09 徐峥，获取核注清单中的提单号
        String billNmS = "";//提单号
        BisOutStock bisOutStock = outStockService.find("outLinkId",outLinkId);
        if (bisOutStock!=null && bisOutStock.getCheckListNo()!=null && bisOutStock.getCheckListNo().trim().length() > 0){
            //核注清单号可能会有多个，用英文分号隔开
            String[] string = null;
            if (bisOutStock.getCheckListNo().trim().contains(";")){
                string = bisOutStock.getCheckListNo().trim().split(";");
            }else{
                string = new String[1];
                string[0] = bisOutStock.getCheckListNo().trim();
            }
            for (int i = 0; i < string.length; i++) {
                List<BisPreEntryInvtQuery> bisPreEntryInvtQueryList = new ArrayList<>();
                List<PropertyFilter> filters = new ArrayList<>();
                filters.add(new PropertyFilter("EQS_bondInvtNo", string[i]));
                filters.add(new PropertyFilter("EQS_synchronization", "1"));
                bisPreEntryInvtQueryList = preEntryInvtQueryService.search(filters);
                if (bisPreEntryInvtQueryList != null && bisPreEntryInvtQueryList.size() == 1) {
                    BisPreEntryInvtQuery forBisPreEntryInvtQuery = bisPreEntryInvtQueryList.get(0);
                    //2024-12-09 徐峥，校验导入出库明细的提单号要和核注清单中的提单号一致
                    if (forBisPreEntryInvtQuery.getTdNo() != null && forBisPreEntryInvtQuery.getTdNo().trim().length() > 0) {
                        billNmS = billNmS + forBisPreEntryInvtQuery.getTdNo()+";";
                    }
                }
            }
            if (!billNmS.contains(billNum)) {
                return "新增提单需与核注清单中的提单保持一致";
            }
        }

        BisOutStockInfo outStockInfo = new BisOutStockInfo();
        //获得对应的入库联系单明细
        Map<String, Object> params3 = new HashMap<String, Object>();
        params3.put("itemNum", billNum);
        params3.put("sku", sku);
        List<BisEnterStockInfo> enterObjList = enterStockInfoService.getListByMap(params3);
        if (!enterObjList.isEmpty()) {
            BisEnterStockInfo enterObj = enterObjList.get(0);
            outStockInfo.setOrderNum(!StringUtils.isNull(enterObj.getOrderNum())?enterObj.getOrderNum():null);
            outStockInfo.setShipNum(!StringUtils.isNull(enterObj.getShipNum())?enterObj.getShipNum():null);
            outStockInfo.setProjectNum(!StringUtils.isNull(enterObj.getProjectNum())?enterObj.getProjectNum():null);
            outStockInfo.setMakeTime(null != enterObj.getMakeTime()?enterObj.getMakeTime():null);
        }
        BaseSkuBaseInfo skuObj=skuInfoService.find("skuId", sku);
        if(null!=skuObj){
        	outStockInfo.setMscNum(skuObj.getMscNum()!=null?skuObj.getMscNum():"");
        	outStockInfo.setLotNum(skuObj.getLotNum()!=null?skuObj.getLotNum():"");
        }
        outStockInfo.setBillNum(billNum);
        outStockInfo.setOutLinkId(outLinkId);
        outStockInfo.setOutNum(outNum);
        outStockInfo.setCodeNum(codeNum);
        outStockInfo.setCtnNum(ctnNum);
        outStockInfo.setAsn(asn);
        outStockInfo.setSalesNum(!StringUtils.isNull(saleNum)?saleNum:"");
        outStockInfo.setEnterState(enterState);
        BaseSkuBaseInfo skuInfo = skuInfoService.get(sku);
        outStockInfo.setSkuId(sku);
        outStockInfo.setRkNum(!StringUtils.isNull(skuInfo.getRkdh())?skuInfo.getRkdh():null);
        outStockInfo.setCargoName(skuInfo.getCargoName());
        outStockInfo.setGrossSingle(skuInfo.getGrossSingle() != null?skuInfo.getGrossSingle():0.0);
        outStockInfo.setGrossWeight((skuInfo.getGrossSingle() != null&&outNum!=null)?outNum * skuInfo.getGrossSingle():0.0);
        outStockInfo.setNetSingle(skuInfo.getNetSingle() != null?skuInfo.getNetSingle():0.00);
        outStockInfo.setNetWeight((skuInfo.getNetSingle() != null&&outNum!=null)?outNum * skuInfo.getNetSingle():0.00);
        outStockInfo.setPiece(skuInfo.getPiece() != null?skuInfo.getPiece():0);
        outStockInfo.setTypeSize(skuInfo.getTypeSize() != null?skuInfo.getTypeSize():"");
        outStockInfo.setUnits(skuInfo.getUnits());
        User user = UserUtil.getCurrentUser();
        outStockInfo.setOperator(user.getName());
        outStockInfo.setOperateTime(new Date());
        outStockInfoService.save(outStockInfo);
        return "success";
    }

    /**
     * 修改出库联系单明细跳转
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "update/{id}/{outLinkId}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Integer id,@PathVariable("outLinkId") String linkId, Model model) {
    	model.addAttribute("outStockInfo",outStockInfoService.getByLinkId(id, linkId));
        model.addAttribute("action", "update");
        return "wms/outstock/outStockInfoUpdateForm";
    }
    /**
     * 新增验证库存数量足不足的问题
     * @param id
     * @param linkId
     * @param model
     * @return
     */
    @RequestMapping(value = "checknum", method = RequestMethod.GET)
    @ResponseBody
    public String checknum(String id,String linkId,String outNum) {
    	BisOutStock outStock=outStockService.find("outLinkId", linkId);
    	BisOutStockInfo outStockInfo=outStockInfoService.getByLinkId(Integer.parseInt(id), linkId);
    	List<Map<String,Object>> getList= trayInfoService.findClientTrayList(linkId,outStock.getStockInId(),outStock.getWarehouseId(),outStockInfo.getRkNum(),outStockInfo.getBillNum(),outStockInfo.getSkuId(),outStockInfo.getCtnNum());
    	if(getList!=null&&getList.size()>0){
        	Map<String,Object> map=getList.get(0);
        	int num=Integer.parseInt(map.get("NOW_PIECE").toString());
        	int outnum=Integer.parseInt(outNum);
        	int tsum=Integer.parseInt(map.get("TNUM").toString());
        	int ttsum=Integer.parseInt(map.get("TTNUM").toString());
        	//int has=Integer.parseInt(map.get("HAS").toString());
        	if(num<outnum+tsum+ttsum){
        		return "出库件数应小于库存件数减去质押件数！";
        	}
        }
        return "success";
    }
    
    

	/**
	 * 修改出库联系单明细
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public String update(@Valid @ModelAttribute @RequestBody BisOutStockInfo outStockInfo,Model model) {
		outStockInfoService.updateByLinkId(outStockInfo);
		return "success";
	}

    /**
     * 复制出库联系单跳转
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "copy/{id}", method = RequestMethod.GET)
    public String copyForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("outStockInfo", outStockInfoService.get(id));
        model.addAttribute("action", "copy");
        return "wms/outstock/outStockInfoForm";
    }

//	/**
//	 * 复制后添加出库联系单
//	 * 
//	 * @param user
//	 * @param model
//	 */
//	@RequestMapping(value = "copy", method = RequestMethod.POST)
//	@ResponseBody
//	public String createCopy(@Valid BisEnterStockInfo bisEnterStockInfo,Model model) {
//		enterStockInfoService.save(bisEnterStockInfo);
//		return "success";
//	}

    /**
     * @param id
     * @return
     * @throws
     * @author pyl
     * @Description: 删除
     * @date 2016年3月3日 下午2:37:12
     */
    @RequestMapping(value = "deleteoutstockinfo/{ids}")
    @ResponseBody
    public String deleteContractInfo(@PathVariable("ids") List<Integer> ids) {
        for (int i = 0; i < ids.size(); i++) {
            outStockInfoService.delete(ids.get(i));
        }
        return "success";
    }

    /**
     * 出库联系单明细excel导入跳转
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "into/{outLinkId}", method = RequestMethod.GET)
    public String intoExcel(Model model, @PathVariable("outLinkId") String outLinkId) {
        model.addAttribute("outLinkId", outLinkId);
        model.addAttribute("action", "intot");
        return "wms/outstock/outStockInfoInto";
    }

    /**
     * 出库联系单明细excel导入
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "intot/{outLinkId}", method = RequestMethod.POST)
    @ResponseBody
    public String intoExcel2(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, Model model, @PathVariable("outLinkId") String outLinkId) {
        String ex = "";
        String more = "";
        String tray = "";
        String rk = "";
        Double weight=0d;  //明细总量

        //2024-12-09 徐峥，获取核注清单中的提单号
        String billNmS = "";//提单号
        BisOutStock bisOutStock = outStockService.find("outLinkId",outLinkId);
        if (bisOutStock!=null && bisOutStock.getCheckListNo()!=null && bisOutStock.getCheckListNo().trim().length() > 0){
            //核注清单号可能会有多个，用英文分号隔开
            String[] string = null;
            if (bisOutStock.getCheckListNo().trim().contains(";")){
                string = bisOutStock.getCheckListNo().trim().split(";");
            }else{
                string = new String[1];
                string[0] = bisOutStock.getCheckListNo().trim();
            }
            for (int i = 0; i < string.length; i++) {
                List<BisPreEntryInvtQuery> bisPreEntryInvtQueryList = new ArrayList<>();
                List<PropertyFilter> filters = new ArrayList<>();
                filters.add(new PropertyFilter("EQS_bondInvtNo", string[i]));
                filters.add(new PropertyFilter("EQS_synchronization", "1"));
                bisPreEntryInvtQueryList = preEntryInvtQueryService.search(filters);
                if (bisPreEntryInvtQueryList != null && bisPreEntryInvtQueryList.size() == 1) {
                    BisPreEntryInvtQuery forBisPreEntryInvtQuery = bisPreEntryInvtQueryList.get(0);
                    if (forBisPreEntryInvtQuery.getTdNo() != null && forBisPreEntryInvtQuery.getTdNo().trim().length() > 0) {
                        billNmS = billNmS + forBisPreEntryInvtQuery.getTdNo()+";";
                    }
                }
            }
        }

        List<BisOutStockInfo> infos=outStockInfoService.getList(outLinkId);
        if(!infos.isEmpty()){
	        for(BisOutStockInfo info:infos){
	        	weight += info.getNetWeight();
	        }
        }
        try {
            ImportParams params = new ImportParams();
            params.setTitleRows(1);
            List<OutStockInfoToExcel> list = ExcelImportUtil.importExcel(file.getInputStream(), OutStockInfoToExcel.class, params);

            //2024-12-09 徐峥，校验导入出库明细的提单号要和核注清单中的提单号一致
            if (billNmS != null && billNmS.trim().length() > 0){
                Boolean con = false;
                for (OutStockInfoToExcel getObj : list) {
                    if (!billNmS.contains(getObj.getBillNum())){
                        con = true;
                        break;
                    }
                }
                if (con){
                    return "over";
                }
            }

            BisOutStockInfo newObj = null;
            BaseSkuBaseInfo skuObj = null;
            User user = UserUtil.getCurrentUser();
            Map<String, Object> params2 = new HashMap<String, Object>();
            List<TrayInfo> trayInfo = null;
            String state = "";
            String key="";
            Map<String,Object> keyMap=new HashMap<String,Object>();
            int i = 2;
            if (list != null && list.size() > 0) {
                for (OutStockInfoToExcel getObj : list) {
                    i++;
                    params2.put("billNum", getObj.getBillNum());
                    params2.put("ctnNum", getObj.getCtnNum());
                    params2.put("skuId", getObj.getSku());
                    switch (getObj.getEnterState()) {
                        case "成品":
                            state = "0";
                            break;
                        case "货物破损":
                            state = "1";
                            break;
                        case "包装破损":
                            state = "2";
                            break;
                    }
                    params2.put("enterState", state);
                    trayInfo = new ArrayList<TrayInfo>();
                    trayInfo = findBySku(params2, "01", outLinkId);
                    key=getObj.getBillNum()+","+getObj.getSku();
                    if(null!=keyMap.get(key)){
                    	keyMap.put(key, Integer.valueOf(keyMap.get(key).toString())+getObj.getOutNum());
                    }else{
                    	keyMap.put(key, getObj.getOutNum());
                    }
                    if (!trayInfo.isEmpty()) {
                        //判断明细中是否已存在此条数据
 //                       if (!exist(params2, outLinkId)) {
                            //判断出库件数是否不大于库存总件数
                            if (judgePiece(trayInfo, getObj.getOutNum(), getObj, outLinkId,Integer.valueOf(keyMap.get(key).toString()))) {
                                skuObj = new BaseSkuBaseInfo();
                                skuObj = skuInfoService.get(getObj.getSku());
//									if(!StringUtils.isNull(getObj.getRkNum()) && skuObj.getRkdh().equals(getObj.getRkNum())){
                                Map<String, Object> paramsnew = new HashMap<String, Object>();
                                paramsnew.put("itemNum", getObj.getBillNum());
                                paramsnew.put("sku", getObj.getSku());
                                List<BisEnterStockInfo> enterObjList = enterStockInfoService.getListByMap(paramsnew);
                                newObj = new BisOutStockInfo();
                                if (!enterObjList.isEmpty()) {
                                    BisEnterStockInfo enterObj = enterObjList.get(0);
                                    newObj.setOrderNum(enterObj.getOrderNum());
                                    newObj.setProjectNum(enterObj.getProjectNum());
                                    newObj.setShipNum(enterObj.getShipNum());
                                    newObj.setMakeTime(enterObj.getMakeTime());
                                }
                                newObj.setOutLinkId(outLinkId);
                                newObj.setBillNum(getObj.getBillNum());
                                newObj.setCtnNum(getObj.getCtnNum());
                                newObj.setSkuId(getObj.getSku());
                                newObj.setMscNum(skuObj.getMscNum()!=null?skuObj.getMscNum():"");
                                newObj.setLotNum(skuObj.getLotNum()!=null?skuObj.getLotNum():"");
                                if (!"".equals(getObj.getSalesNum())) {
                                    newObj.setSalesNum(getObj.getSalesNum());
                                }
                                newObj.setEnterState(state);
                                newObj.setCodeNum(getObj.getCodeNum());
                                newObj.setOutNum(getObj.getOutNum());
                                newObj.setCargoName(skuObj.getCargoName());
                                if (skuObj.getTypeSize() != null) {
                                    newObj.setTypeSize(skuObj.getTypeSize());
                                } else {
                                    newObj.setTypeSize(" ");
                                }
                                newObj.setNetSingle(skuObj.getNetSingle());
                                newObj.setGrossSingle(skuObj.getGrossSingle());
                                newObj.setNetWeight(getObj.getOutNum() * skuObj.getNetSingle());
                                newObj.setGrossWeight(getObj.getOutNum() * skuObj.getGrossSingle());
                                newObj.setUnits("1");
                                newObj.setOperator(user.getName());
                                newObj.setOperateTime(new Date());
                                newObj.setPiece(0);
                                newObj.setRkNum(skuObj.getRkdh());
                                newObj.setAsn(getObj.getAsn());//20170919增加 yhn
                                weight += newObj.getNetWeight();
                                outStockInfoService.save(newObj);
//									}else{
//										rk+= String.valueOf(i)+",";
//									}
                                //end if 库存数量
                            } else {
                                more += String.valueOf(i) + ",";
                            }
                            //end if 明细存在
//                        } else {
//                            ex += String.valueOf(i) + ",";
//                        }
                        //end if
                    } else {
                        tray += String.valueOf(i) + ",";
                    }
                }//end for
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ex.equals("") && more.equals("") && tray.equals("") && rk.equals("")) {
        	BisOutStock outObj=outStockService.get(outLinkId);
        	BaseClientInfo clientObj=clientService.get(Integer.valueOf(outObj.getStockInId()));
        	if(this.checkRank(weight, clientObj)){
        		return "over";
        	}else{
        		return "success";
        	}
        } else {
            if (!ex.equals("")) {
                ex = ex.substring(0, ex.length() - 1);
            }
            if (!more.equals("")) {
                more = more.substring(0, more.length() - 1);
            }
            if (!tray.equals("")) {
                tray = tray.substring(0, tray.length() - 1);
            }
            if (!rk.equals("")) {
                rk = rk.substring(0, rk.length() - 1);
            }
            return ex + ":" + more + ":" + tray + ":" + rk;
        }
    }

    //判断此条库存中是否存在
    private List<TrayInfo> findBySku(Map<String, Object> params2, String cargoState, String outLinkId) {
        Map<String, Object> params4 = new HashMap<String, Object>();
        params4.putAll(params2);
        params4.put("cargoState", cargoState);
        BisOutStock outStock = outStockService.get(outLinkId);
        params4.put("stockIn", outStock.getStockInId());
        return trayInfoService.findBySku(params4);
    }


    //判断此条是否已存在于明细中
    @SuppressWarnings("unused")
	private boolean exist(Map<String, Object> params2, String outLinkId) {
        Map<String, Object> params3 = new HashMap<String, Object>();
        params3.putAll(params2);
        params3.put("outLinkId", outLinkId);
        List<BisOutStockInfo> infoList = outStockInfoService.findonly(params3);
        return !infoList.isEmpty();

    }


    //判断出库件数是否不多于库存件数
    private boolean judgePiece(List<TrayInfo> trayInfo, Integer outNum, OutStockInfoToExcel getObj, String outLinkId, Integer keyNum) {
        BisOutStock outStock = outStockService.get(outLinkId);
        Map<String, Object> pd = new HashMap<String, Object>();//动态质押参数params
        pd.put("skuId", getObj.getSku());
        pd.put("client", outStock.getStockInId());
        pd.put("warehouseId", outStock.getWarehouseId());
        pd.put("ptype", 2);
        //动态质押件数
        List<BaseClientPledge> pledgeListD = new ArrayList<BaseClientPledge>();
        pledgeListD = clientPledgeService.findBySku(pd);
        int pledgeD = 0;
        if (!pledgeListD.isEmpty()) {
            int sizeD = pledgeListD.size();
            for (int i = 0; i < sizeD; i++) {
                pledgeD += pledgeListD.get(i).getNum();
            }
        }
        Map<String, Object> pj = new HashMap<String, Object>();//静态质押参数params
        pj.put("skuId", getObj.getSku());
        pj.put("client", outStock.getStockInId());
        pj.put("warehouseId", outStock.getWarehouseId());
        pj.put("billNum", getObj.getBillNum());
        pj.put("ctnNum", getObj.getCtnNum());
        pj.put("ptype", 1);
        //静态质押件数
        List<BaseClientPledge> pledgeListJ = new ArrayList<BaseClientPledge>();
        pledgeListJ = clientPledgeService.findBySku(pj);
        int pledgeJ = 0;
        if (!pledgeListJ.isEmpty()) {
            int sizeJ = pledgeListJ.size();
            for (int j = 0; j < sizeJ; j++) {
                pledgeJ += pledgeListJ.get(j).getNum();
            }
        }
        int size = trayInfo.size();
        int nowPiece = 0;
        for (int i = 0; i < size; i++) {
            nowPiece += trayInfo.get(i).getNowPiece();
        }
        return keyNum + pledgeD + pledgeJ <= nowPiece;
    }

    /**
     * java实现文件下载功能代码
     * 创建时间：2014年12月23日
     *
     * @version
     */
    @RequestMapping(value = "download", method = RequestMethod.POST)
    @ResponseBody
    public void fileDownLoad(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String fileName = "出库联系单明细导入模板.xls";
        String filePath = PropertiesUtil.getPropertiesByName("downloadexcel", "application");
        String realpath = filePath + "WEB-INF\\classes\\importExcel\\出库联系单明细导入模板.xls";
//			  String  realpath = filePath + "classes\\importExcel\\出库联系单明细导入模板.xls";
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        OutputStream fos = null;
        InputStream fis = null;
        try {
            response.setContentType("application/vnd.ms-excel");
            String formatFileName = new String(fileName.getBytes("GB2312"), "ISO-8859-1");
//			   response.setHeader("Content-disposition", "attachment;filename=\"" + URLEncoder.encode(fileName, "UTF-8") +"\"");
            response.setHeader("Content-disposition", "attachment;filename=\"" + formatFileName + "\"");
            fis = new FileInputStream(realpath);
            bis = new BufferedInputStream(fis);
            fos = response.getOutputStream();
            bos = new BufferedOutputStream(fos);
            int bytesRead = 0;
            byte[] buffer = new byte[5 * 1024];
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);// 将文件发送到客户端
            }
            bos.close();
            bis.close();
            fos.close();
            fis.close();
        } catch (IOException e) {
            response.reset();
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (bos != null) {
                    bos.close();
                }
                if (fis != null) {
                    fis.close();
                }
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                System.err.print(e);
            }
        }
    }


    /**
     * 出库联系单明细导出报表
     */
    @RequestMapping(value = "exportinfo/{outLinkId}/{type}", method = RequestMethod.POST)
    @ResponseBody
    public void exportInfo(HttpServletRequest request,
                           HttpServletResponse response,
                           Model model,
                           @PathVariable("outLinkId") String outLinkId,
                           @PathVariable("type") String type) throws IOException {

        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();

        TemplateExportParams params = new TemplateExportParams("exceltemplate/outStockInfo.xls");

        String excelName = "出库联系单明细.xls";
        BisOutStock outStock = outStockService.get(outLinkId);
        map.put("receiver", outStock.getReceiver());
        map.put("stockIn", outStock.getStockIn());
        map.put("outLinkId", outLinkId);
        map.put("OutCustomsCount", outStock.getOutCustomsCount()!=null?outStock.getOutCustomsCount():0);
        map.put("OutCiqCount", outStock.getOutCiqCount()!=null?outStock.getOutCiqCount():0);//
        if (!StringUtils.isNull(outStock.getSellFee())) {
            map.put("sellFee", outStock.getSellFee());
        } else {
            map.put("sellFee", "");
        }
        if (!StringUtils.isNull(outStock.getBuyFee())) {
            map.put("buyFee", outStock.getBuyFee());
        } else {
            map.put("buyFee", "");
        }
        if (!StringUtils.isNull(outStock.getReceiverLinker())) {
            map.put("receiverLinker", outStock.getReceiverLinker());
        } else {
            map.put("receiverLinker", "");
        }
        if ("0".equals(outStock.getIfCustomsClearance())) {
            map.put("clearance", "否");
        } else {
            map.put("clearance", "是");
        }
        if ("0".equals(outStock.getIfBonded())) {
            map.put("bonded", "否");
        } else {
            map.put("bonded", "是");
        }
        if ("0".equals(outStock.getIfClearStore())) {
            map.put("clear", "否");
        } else {
            map.put("clear", "是");
        }
        User user = UserUtil.getCurrentUser();
        map.put("user", user.getName());
        map.put("now", new Date());
        if ("1".equals(type)) {//不带船名项目号
            excelName = "出库联系单明细.xls";
            params = new TemplateExportParams("exceltemplate/outStockInfo.xls");
        } else if ("2".equals(type)) {//带船名项目号
            excelName = "出库联系单明细（包含船名项目号）.xls";
            params = new TemplateExportParams("exceltemplate/outStockInfoWith.xls");
        }

        listMap = outStockInfoService.searchInfo(outLinkId);
        if (null != listMap && listMap.size() > 0) {
            map.put("maplist", listMap);
        }
        if (!StringUtils.isNull(outStock.getCarNum())) {
            map.put("carNum", outStock.getCarNum());
        } else {
            map.put("carNum", "");
        }

        if (!StringUtils.isNull(outStock.getCdNum())) {
            map.put("cdNum", outStock.getCdNum());
        } else {
            map.put("cdNum", "");
        }
        if (!StringUtils.isNull(outStock.getCiqNum())) {
            map.put("ciqNum", outStock.getCiqNum());
        } else {
            map.put("ciqNum", "");
        }
        if (!StringUtils.isNull(outStock.getSepcialAsk())) {
            map.put("teshu", outStock.getSepcialAsk());
        } else {
            map.put("teshu", "");
        }
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        String formatFileName = new String(excelName.getBytes("GB2312"), "ISO-8859-1");
        response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
        OutputStream os = response.getOutputStream();
        workbook.write(os); // 写入文件
        os.close(); // 关闭流
    }

    /**
     * 明细校验库存警戒线
     *
     * @param user
     * @param model
     */
    @RequestMapping(value = "checkRank", method = RequestMethod.GET)
    @ResponseBody
    public String check(String outLinkId, HttpServletRequest request) {
    	BisOutStock outObj = outStockService.get(outLinkId);
    	BaseClientInfo client= clientService.get(Integer.valueOf(outObj.getStockInId()));
    	Map<String,Object> param = new HashMap<String,Object>();
    	param.put("outLinkId", outLinkId);
    	List<BisOutStockInfo> infos = outStockInfoService.findonly(param);
    	Double weight=0d;
    	if(!infos.isEmpty()){
	    	for(BisOutStockInfo info:infos){
	    		weight += info.getNetWeight();
	    	}
    	}
    	if(this.checkRank(weight, client)){
    		return "false";
    	}else{
    		return "success";
    	}
    }
    
    
    /**
     * 判断出库的明细是否超了警戒线 
     * @param weight 明细总量
     * @param clientObj 客户
     */
    public Boolean checkRank(Double weight,BaseClientInfo clientObj){
    	 if(null==clientObj.getCustomerLevel()){
    		 return false;
    	 }else{
    		 BaseClientRank rank=clientRankService.get(clientObj.getCustomerLevel());
    		 Double safeWeight=rank.getMinNum();
    		 List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
    		 filters.add(new PropertyFilter("EQS_stockIn", clientObj.getIds().toString()));
    		 filters.add(new PropertyFilter("EQS_cargoState", "01"));
    		 List<TrayInfo> infos = trayInfoService.findByF(filters);
    		 Double nowPiece = 0d;
    		 if(!infos.isEmpty()){
	    		 for(TrayInfo info:infos){
	    			 nowPiece += info.getNetWeight();
	    		 }
    		 }
    		 if( nowPiece-weight>safeWeight){
    			 return false; //未超出警戒线
    		 }else{
    			 return true; //超出警戒线
    		 }
    	 }
    }

    @RequestMapping(value = "ifAllHasAsn/{outLinkId}", method = RequestMethod.GET)
    @ResponseBody
    public String ifAllHasAsn(@PathVariable("outLinkId") String linkId,HttpServletRequest request,HttpServletResponse response) {
    	List<BisOutStockInfo> infos=outStockInfoService.getList(linkId);
    	if(infos.size()>0){
    		for(BisOutStockInfo bosi : infos){
    			if(bosi.getAsn() == null || "".equals(bosi.getAsn())){
    				return "false";
    			}
    		}
    		return "success";
    	}else{
    		return "success";
    	}
    }

    public static Object ByteAryToObject(byte[] bytes) throws IOException, ClassNotFoundException {
        if(bytes == null){
            return null;
        }
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInputStream sIn = null;
        Object obj = null;
        sIn = new ObjectInputStream(in);
        obj = sIn.readObject();
        return obj;
    }
    
}
