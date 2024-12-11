package com.haiersoft.ccli.report.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.haiersoft.ccli.bounded.entity.BaseBounded;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.*;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.report.entity.AAccountBook;
import com.haiersoft.ccli.report.entity.ATray;
import com.haiersoft.ccli.report.entity.Stock;
import com.haiersoft.ccli.report.service.AAccountBookService;
import com.haiersoft.ccli.report.service.AtrayService;
import com.haiersoft.ccli.report.service.StockReportService;
import com.haiersoft.ccli.supervision.entity.*;
import com.haiersoft.ccli.supervision.service.*;
import com.haiersoft.ccli.supervision.web.OpApprController;
import com.haiersoft.ccli.system.utils.ExcelUtils;
import com.haiersoft.ccli.wms.entity.BisAsn;
import com.haiersoft.ccli.wms.entity.BisAsnInfo;
import com.haiersoft.ccli.wms.entity.BisEnterStockInfo;
import com.haiersoft.ccli.wms.entity.BisOutStock;
import com.haiersoft.ccli.wms.service.EnterStockInfoService;
import com.itextpdf.text.PageSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.xml.rpc.ServiceException;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

@Controller
@RequestMapping("report/ATray")
public class ATrayController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(ATrayController.class);

    @Autowired
    private AtrayService atrayService;
    @Autowired
    private AAccountBookService aAccountBookService;
    @Autowired
    private OpApprHeadService opApprHeadService;
    @Autowired
    private OpApprInfoService opApprInfoService;
    @Autowired
    private EnterStockInfoService enterStockInfoService;
    @Autowired
    private OpManiHeadService opManiHeadService;
    @Autowired
    private OpManiInfoService opManiInfoService;


    @Autowired
    private ApprHeadService apprHeadService;
    @Autowired
    private ApprInfoService apprInfoService;
    @Autowired
    private ManiHeadService maniHeadService;
    @Autowired
    private ManiInfoService maniInfoService;

    @Autowired
    GetKeyService getKeyService;
    @Autowired
    FljgWsClient fljgWsClient;

    @RequestMapping(value = "excelNewTemp", method = RequestMethod.GET)
    public String toexcelNewTemp(Model model) {
        Date now = new Date();
        model.addAttribute("strTime", DateUtils.getDateStart(now));
        model.addAttribute("endTime", DateUtils.getDateEnd(now));
        return "report/stockInfoReportNewTemp";
    }
    /**
     * 查询
     */
    @RequestMapping(value = "jsonReportNewTemp", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> jsonReportNewTemp(HttpServletRequest request) {
        Page<ATray> page = getPage(request);
        page.setOrder("asc");
        page.setOrderBy("id");
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        page = atrayService.search(page, filters);
        return getEasyUIData(page);
    }

    /**
     * 数据生成
     */
    @RequestMapping(value = "createDataNewTemp", method = RequestMethod.GET)
    @ResponseBody
    public String createDataNewTemp() {
        try {
            return atrayService.createDataNewTemp();
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 批量生成申请单
     */
    @RequestMapping(value = "createSQD", method = RequestMethod.GET)
    @ResponseBody
    public String createSQD() {
        //查询所有未生成申请单的数据
        List<ATray> aTrayList = new ArrayList<>();
        aTrayList = atrayService.querySQDData();
        //整理内通道的申请单数据
        for (ATray forATray:aTrayList) {
            //生成申请单表头
            OpApprHead opApprHead = new OpApprHead();
            opApprHead.setLinkId(forATray.getLinkId());//业务单号
            opApprHead.setApprType("3");//申请类型
            opApprHead.setIoType("1");//出入区类型
            opApprHead.setBondInvtNo("");//关联核注单编号
            opApprHead.setCustomsCode("4258");//主管海关
//            opApprHead.setEmsNo("NH4230210001");//账册编号
            opApprHead.setEmsNo("NH4230240017");//新账册编号
            opApprHead.setItemNum(forATray.getBillCode());//提单号
            opApprHead.setgNo(Integer.parseInt(forATray.getXh().toString()));//底账商品项号
            opApprHead.setOwnerCode("3702631016");//货主单位代码
            opApprHead.setOwnerName("青岛港怡之航冷链物流有限公司");//货主单位名称
            opApprHead.setGrossWt(forATray.getAllgross().toString());//重量
            opApprHead.setdNote("");//备注
            opApprHead.setLocalStatus("1");//本地状态为1，保存
            opApprHead.setCreateTime(new Date());
            opApprHead.setApprAddType("2");
            opApprHeadService.save(opApprHead);

            //依据联系单号获取入库联系单明细
            List<BisEnterStockInfo> bisEnterStockInfoList = new ArrayList<>();
            List<PropertyFilter> filters =new ArrayList<PropertyFilter>();
            PropertyFilter filter = new PropertyFilter("EQS_linkId", forATray.getLinkId());
            filters.add(filter);
            bisEnterStockInfoList = enterStockInfoService.search(filters);
            if (bisEnterStockInfoList!=null && bisEnterStockInfoList.size() > 0){
                //生成申请单表体
                OpApprInfo opApprInfo = new OpApprInfo();
                opApprInfo.setHeadId(opApprHead.getId());
                opApprInfo.setCodeTs(bisEnterStockInfoList.get(0).getHsCode());//商品编码
                opApprInfo.setgName(bisEnterStockInfoList.get(0).getHsItemname());//商品名称
                opApprInfo.setgModel(bisEnterStockInfoList.get(0).getTypeSize());//规格
                opApprInfo.setgNo(opApprHead.getgNo());//底账商品项号
                opApprInfo.setgQty(forATray.getNowNum().toString());//数量
                opApprInfo.setGrossWt(forATray.getAllgross().toString());//重量
                opApprInfo.setgUnit("035");//单位
                opApprInfo.setApprGNo(1);
                opApprInfo.setQty1(opApprInfo.getgQty());
                opApprInfo.setUnit1(opApprInfo.getgUnit());
                opApprInfo.setCreateTime(new Date());
                opApprInfoService.save(opApprInfo);
            }
            forATray.setCreatesqd(1);
            atrayService.merge(forATray);
        }
        return "success";
    }

    /**
     * 申请单批量申报
     */
    @RequestMapping(value = "createSB", method = RequestMethod.GET)
    @ResponseBody
    public String createSB() throws RemoteException, ServiceException {
        //查询所有未生成申请单的数据
        List<OpApprHead> opApprHeadList = new ArrayList<>();
        List<PropertyFilter> filters =new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_apprAddType", "2"));
        filters.add(new PropertyFilter("NULLS_apprId", ""));
        opApprHeadList = opApprHeadService.search(filters);
        //整理内通道的申请单数据
        for (OpApprHead forOpApprHead:opApprHeadList) {
            String id = forOpApprHead.getId();
            OpApprHead opApprHead = opApprHeadService.find("id", id);
            Map<String, Object> apprHeadMap = this.buildApprHead(opApprHead);
            List<OpApprInfo> infolist = opApprInfoService.getListByHeadId(id);

            List<Map<String, Object>> ApprLists = this.buildApprLists(opApprHead,infolist);
            //拼装Json
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("SaveType", "1");//这里0为暂存，咱们不存在暂存申请，直接发送1
            map.put("DeclType", "1");
            map.put("ApprLists", ApprLists);
            map.put("ApprHead", apprHeadMap);

            String jsonString = JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);
            System.out.println(jsonString);
            logger.error(jsonString);
            // 1 获得key
            String tickId = getKeyService.builder();
            System.out.println(tickId);

            // 2 调用接口
            //设置接口名
            String serviceName = "ApprSave";
            // 调用 申请单保存
            String result = fljgWsClient.getResult(jsonString, tickId, serviceName);
            System.out.println("ApprSave result: " + result);
            logger.error(">>>>>>>>>>>>>>>>>调用申请单申报result： "+result);

            JSONObject jsonObject = JSON.parseObject(result);
            String state = jsonObject.getString("state");
            if(state.equals("1")) {
                String data = jsonObject.getString("data");
                opApprHeadService.updateArrpIdById(data,"1" ,id);

                List<ATray> aTrayList = new ArrayList<>();
                List<PropertyFilter> filtersATray =new ArrayList<PropertyFilter>();
                filtersATray.add(new PropertyFilter("EQS_billCode", forOpApprHead.getItemNum()));
                aTrayList = atrayService.search(filtersATray);
                for (ATray forATray:aTrayList) {
                    forATray.setCreatesb(1);
                    atrayService.merge(forATray);
                }
            }else {
                String CheckInfos = jsonObject.getString("CheckInfos");
                logger.error("CheckInfos"+CheckInfos);
            }
        }
        return "success";
    }

    /**wms/passPort
     * 批量生成虚拟核放单
     */
    @RequestMapping(value = "createXNHFD", method = RequestMethod.GET)
    @ResponseBody
    public String createXNHFD() {
        //查询所有未生成申请单的数据
        List<OpApprHead> opApprHeadList = new ArrayList<>();
        List<PropertyFilter> filters =new ArrayList<PropertyFilter>();
        PropertyFilter filter = new PropertyFilter("EQS_apprAddType", "2");
        filters.add(filter);
        opApprHeadList = opApprHeadService.search(filters);
        if(opApprHeadList!=null && opApprHeadList.size() > 0){
            for (OpApprHead forOpApprHead:opApprHeadList) {
                if (forOpApprHead.getApprId()==null || forOpApprHead.getApprId().trim().length() == 0){
                    throw new RuntimeException("申请单未获得申请单号，无法生成虚拟核放单");
                }
                //先查询申请单号是否已存在对应的核放单
                List<PropertyFilter> filtersOpMini = new ArrayList<PropertyFilter>();
                filtersOpMini.add(new PropertyFilter("EQS_ApprId", forOpApprHead.getApprId()));
                List<OpManiHead> opManiHeadList = opManiHeadService.search(filtersOpMini);
                if(opManiHeadList.size() !=0) {
                    logger.info("已存在对应的核放单，不能生成虚拟核放单");
                    continue;
                }
                OpManiHead opManiHead = new OpManiHead();
                opManiHead.setApprId(forOpApprHead.getApprId());//申请单号
                opManiHead.setContaId(null);//箱号
                opManiHead.setIeFlag(getIeFlag(forOpApprHead.getIoType()));//进出标志
                opManiHead.setVehicleId("V"+(int)((Math.random()*9+1)*100000));//生成虚拟车号
                opManiHead.setVehicleWeight("20");//车辆自重保存必填
                opManiHead.setTradeCode("3702631016");//经营单位编码
                opManiHead.setTradeName("青岛港怡之航冷链物流有限公司");//经营单位名称

                opManiHead.setEmptyFlag("N");//空车标志
                opManiHead.setGrossWt(forOpApprHead.getGrossWt());//货物毛重保存必填
                opManiHead.setIeFlagNote("虚拟核放单");//进出标志备注
                opManiHead.setLocalStatus("1");//本地状态为1，保存
                opManiHead.setCreateTime(new Date());
                opManiHead.setApprAddType("2");
                opManiHeadService.save(opManiHead);

                List<PropertyFilter> filtersOpApprInfo = new ArrayList<PropertyFilter>();
                filtersOpApprInfo.add(new PropertyFilter("EQS_headId", forOpApprHead.getId()));
                List<OpApprInfo> opApprInfoList = opApprInfoService.search(filtersOpApprInfo);
                for(OpApprInfo forOpApprInfo:opApprInfoList) {
                    OpManiInfo opManiInfo= new OpManiInfo();
                    opManiInfo.setHeadId(opManiHead.getId());//主表id
                    opManiInfo.setgNo(forOpApprInfo.getgNo());//底账商品项号
                    opManiInfo.setApprId(forOpApprHead.getApprId());//申请单号
                    opManiInfo.setApprGNo(forOpApprInfo.getApprGNo());//申请单商品序号
                    opManiInfo.setCodeTs(forOpApprInfo.getCodeTs());//HS编码
                    opManiInfo.setGName(forOpApprInfo.getgName());//商品名称
                    opManiInfo.setGModel(forOpApprInfo.getgModel());//规格型号
                    opManiInfo.setGUnit(forOpApprInfo.getgUnit());//申报计量单位
                    opManiInfo.setGUnit1(forOpApprInfo.getgUnit());//申报计量单位
                    opManiInfo.setGQty(forOpApprInfo.getgQty());//申报数量
                    opManiInfo.setGQty1(forOpApprInfo.getgQty());//申报数量
                    opManiInfo.setGrossWt(forOpApprInfo.getGrossWt());//毛重
                    opManiInfo.setConfirmQty(forOpApprInfo.getgQty());//到货确认数量

                    opManiInfoService.save(opManiInfo);
                }

                List<ATray> aTrayList = new ArrayList<>();
                List<PropertyFilter> filtersATray =new ArrayList<PropertyFilter>();
                filtersATray.add(new PropertyFilter("EQS_billCode", forOpApprHead.getItemNum()));
                aTrayList = atrayService.search(filtersATray);
                for (ATray forATray:aTrayList) {
                    forATray.setCreatexnhfd(1);
                    atrayService.merge(forATray);
                }
            }
        }else{
            return "error";
        }
        return "success";
    }

    // 核放单申报
    @RequestMapping(value = "createSB2", method = RequestMethod.GET)
    @ResponseBody
    public String createSB2() throws IOException, ServiceException {
        //查询所有未生成申请单的数据
        List<OpManiHead> opManiHeadList = new ArrayList<>();
        List<PropertyFilter> filters =new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_apprAddType", "2"));
        filters.add(new PropertyFilter("NULLS_ManifestId", ""));
        opManiHeadList = opManiHeadService.search(filters);
        //整理内通道的申请单数据
        for (OpManiHead forOpManiHead:opManiHeadList) {
            String id = forOpManiHead.getId();

            JSONObject jsonObject = new JSONObject();
            //构建接口json.表头
            JSONObject manibase = new JSONObject();
            manibase.put("ManifestId", forOpManiHead.getManifestId()==null?null:forOpManiHead.getManifestId());
            manibase.put("TradeCode",  forOpManiHead.getTradeCode()==null?null:forOpManiHead.getTradeCode());
            manibase.put("TradeName",  forOpManiHead.getTradeName()==null?null:forOpManiHead.getTradeName());
            manibase.put("CustomsCode",forOpManiHead.getCustomsCode()==null?null:forOpManiHead.getCustomsCode());
            manibase.put("IeFlag",forOpManiHead.getIeFlag()==null?null:forOpManiHead.getIeFlag());
            manibase.put("GoodsValue", forOpManiHead.getGoodsValue()==null?null:forOpManiHead.getGoodsValue());

            manibase.put("GrossWt", opManiInfoService.sumGrossWtByHeadId(id));

            manibase.put("VehicleId", forOpManiHead.getVehicleId()==null?null:forOpManiHead.getVehicleId());
            manibase.put("VehicleWeight", forOpManiHead.getVehicleWeight()==null?null:forOpManiHead.getVehicleWeight());
            manibase.put("Status", forOpManiHead.getStatus()==null?null:forOpManiHead.getStatus());
            manibase.put("PassStatus", forOpManiHead.getPassStatus()==null?null:forOpManiHead.getPassStatus());
            manibase.put("InputEr", forOpManiHead.getInputEr()==null?null:forOpManiHead.getInputEr());
            manibase.put("InputDate", forOpManiHead.getInputDate()==null?null:forOpManiHead.getInputDate());
            manibase.put("DDate", forOpManiHead.getDDate()==null?null:forOpManiHead.getDDate());
            manibase.put("DNote", forOpManiHead.getDNote()==null?null:forOpManiHead.getDNote());
            manibase.put("ApprDate", forOpManiHead.getApprDate()==null?null:forOpManiHead.getApprDate());
            manibase.put("ApprNote", forOpManiHead.getApprNote()==null?null:forOpManiHead.getApprNote());
            manibase.put("OrgCode", forOpManiHead.getOrgCode()==null?null:forOpManiHead.getOrgCode());
            manibase.put("ContaId", forOpManiHead.getContaId()==null?null:forOpManiHead.getContaId());
            manibase.put("PassportDate", forOpManiHead.getPassportDate()==null?null:forOpManiHead.getPassportDate());
            manibase.put("DeclType", forOpManiHead.getDeclType()==null?null:forOpManiHead.getDeclType());
            manibase.put("SeqNo", forOpManiHead.getSeqNo()==null?null:forOpManiHead.getSeqNo());
            manibase.put("CusStatus", forOpManiHead.getCusStatus()==null?null:forOpManiHead.getCusStatus());
            manibase.put("CusRmk", forOpManiHead.getCusRmk()==null?null:forOpManiHead.getCusRmk());

            //表头加入json中
            jsonObject.put("Manibase", manibase);

            //构建货物信息
            JSONArray jsonArray = new JSONArray();
            //根据ManiHead的ID查询ManiInfo
            List<PropertyFilter> infofilters = new ArrayList<PropertyFilter>();
            infofilters.add(new PropertyFilter("EQS_headId",id));
            List<OpManiInfo> maniInfoList = opManiInfoService.search(infofilters);
            for (OpManiInfo maniInfo :maniInfoList) {
                JSONObject maniGoods = new JSONObject();
                maniGoods.put("ManifestId", maniInfo.getManifestId()==null?null:maniInfo.getManifestId());
                maniGoods.put("GNo",maniInfo.getgNo()==null?null:maniInfo.getgNo());
                maniGoods.put("ApprId", maniInfo.getApprId()==null?null:maniInfo.getApprId());
                maniGoods.put("ApprGNo", maniInfo.getApprGNo()==null?null:maniInfo.getApprGNo());
                maniGoods.put("CodeTs",maniInfo.getCodeTs()==null?null:maniInfo.getCodeTs());
                maniGoods.put("GName", maniInfo.getGName()==null?null:maniInfo.getGName());
                maniGoods.put("GModel",maniInfo.getGModel()==null?null:maniInfo.getGModel());
                maniGoods.put("GUnit",maniInfo.getGUnit()==null?null:maniInfo.getGUnit());
                maniGoods.put("GQty", maniInfo.getGQty()==null?null:maniInfo.getGQty());
                maniGoods.put("Unit1", maniInfo.getGUnit1()==null?null:maniInfo.getGUnit1());
                maniGoods.put("Qty1", maniInfo.getGQty1()==null?null:maniInfo.getGQty1());
                maniGoods.put("GrossWt",maniInfo.getGrossWt()==null?null:maniInfo.getGrossWt());
                maniGoods.put("ConfirmQty",maniInfo.getConfirmQty()==null?null:maniInfo.getConfirmQty());
                maniGoods.put("EmsGNo",maniInfo.getEmsGNo()==null?null:maniInfo.getEmsGNo());
                maniGoods.put("GdsMtno",maniInfo.getGdsMtno()==null?null:maniInfo.getGdsMtno());
                jsonArray.add(maniGoods);
            }

            //货物信息加入
            jsonObject.put("ManiGoods", jsonArray);
            jsonObject.put("DeclType","1");//1首次申报2变更3作废
            jsonObject.put("SaveType","1");// 0-暂存，1-申报
            System.out.println(">>>>>>>>>>>>>>>>>调用核放单申报json： "+jsonObject.toJSONString());
            logger.error(">>>>>>>>>>>>>>>>>调用核放单申报json： "+jsonObject.toJSONString());
            //调用接口
            // 1 获得key
            String tickId = getKeyService.builder();
            System.out.println(tickId);
            // 2 调用接口
            //设置接口名
            String serviceName = "ManiSave";

            // 调用 申请单保存
            String result = fljgWsClient.getResult(jsonObject.toJSONString(), tickId, serviceName);
            System.out.println("result: " + result);
            logger.error(">>>>>>>>>>>>>>>>>调用核放单申报result： "+result);
            //收到返回值后处理，保存核放单号至MainHead 和 MainInfo
            JSONObject jsonResult = JSON.parseObject(result);
            String state = jsonResult.getString("state");
            if(state.equals("1")) {
                String data = jsonResult.getString("data");
                //根据id设置记录中的Apprid，同时更新stauts状态
                opManiHeadService.updateManiFestIdById(data,"1" ,id);
                opManiInfoService.updateManiFestIdbyHeadId(data, id);

            }else {
                String message = jsonResult.getString("message");
                String CheckInfos = jsonResult.getString("CheckInfos");
                logger.error(message+CheckInfos);
            }
        }
        return "success";
    }

    /**
     * 批量到货确认
     */
    @RequestMapping(value = "createDHQR", method = RequestMethod.GET)
    @ResponseBody
    public String createDHQR() throws RemoteException, ServiceException {
        List<OpManiHead> opManiHeadList = new ArrayList<>();
        List<PropertyFilter> filters =new ArrayList<PropertyFilter>();
        PropertyFilter filter = new PropertyFilter("EQS_apprAddType", "2");
        filters.add(filter);
        opManiHeadList = opManiHeadService.search(filters);
        if(opManiHeadList!=null && opManiHeadList.size() > 0) {
            for (OpManiHead forOpManiHead : opManiHeadList) {
                if(forOpManiHead.getPassStatus() !=null && forOpManiHead.getPassStatus().equals("2")) {
                    continue;
                }

		        //获得key
                String  tickId = getKeyService.builder();
                System.out.println(tickId);
		        //设置接口名
                String serviceName = "ManiConfirm";
		        //拼接json
                String json = buildJson(forOpManiHead);
                logger.error(">>>>>>>>>>>>>>>>>调用到货确认json： "+json);
                String result = fljgWsClient.getResult(json, tickId, serviceName);
                System.out.println("result: " + result);
                logger.error(">>>>>>>>>>>>>>>>>调用到货确认result： "+result);
                JSONObject jsonObject = JSON.parseObject(result);
                String state = jsonObject.getString("state");

                //如果接口state返回1为提交成功
                if(state.equals("1")) {
                    forOpManiHead.setPassStatus("2");
                    forOpManiHead.setManiConfirmStatus("Y");
                    opManiHeadService.save(forOpManiHead);

                    List<OpApprHead> opApprHeadList = new ArrayList<>();
                    List<PropertyFilter> filtersATray1 =new ArrayList<PropertyFilter>();
                    filtersATray1.add(new PropertyFilter("EQS_apprId", forOpManiHead.getApprId()));
                    opApprHeadList = opApprHeadService.search(filtersATray1);
                    if (opApprHeadList!=null && opApprHeadList.size() > 0){
                        List<ATray> aTrayList = new ArrayList<>();
                        List<PropertyFilter> filtersATray2 =new ArrayList<PropertyFilter>();
                        filtersATray2.add(new PropertyFilter("EQS_billCode", opApprHeadList.get(0).getItemNum()));
                        aTrayList = atrayService.search(filtersATray2);
                        for (ATray forATray:aTrayList) {
                            forATray.setCreatedhqr(1);
                            atrayService.merge(forATray);
                        }
                    }
                }
            }
        }else{
            return "没有需要提交到货确认的核放单";
        }
        return "success";
    }



    /**
     * 旧账册数据清零申请单
     */
    @RequestMapping(value = "clearInventoryS", method = RequestMethod.GET)
    @ResponseBody
    public String clearInventoryS() throws RemoteException, ServiceException {
        //查询剩余数量大于0的底账信息
        List<AAccountBook> aAccountBookList = new ArrayList<>();
        aAccountBookList = aAccountBookService.queryClearInventorySData();
        System.out.println("aAccountBookList"+JSON.toJSONString(aAccountBookList));

//        //申请单
//        List<Map<String, Object>> ApprLists = this.buildApprLists(opApprHead,infolist);
//        //拼装Json
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("SaveType", "1");//这里0为暂存，咱们不存在暂存申请，直接发送1
//        map.put("DeclType", "1");
//        map.put("ApprLists", ApprLists);
//        map.put("ApprHead", apprHeadMap);
//
//        String jsonString = JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);
//        System.out.println(jsonString);
//        logger.error(jsonString);
//        // 1 获得key
//        String tickId = getKeyService.builder();
//        System.out.println(tickId);
//
//        // 2 调用接口
//        //设置接口名
//        String serviceName = "ApprSave";
//        // 调用 申请单保存
//        String result = fljgWsClient.getResult(jsonString, tickId, serviceName);
//        System.out.println("ApprSave result: " + result);
//        logger.error(">>>>>>>>>>>>>>>>>调用申请单申报result： "+result);
//
//        JSONObject jsonObject = JSON.parseObject(result);
//        String state = jsonObject.getString("state");
//        if(state.equals("1")) {
//
//        }else {
//            String CheckInfos = jsonObject.getString("CheckInfos");
//            logger.error("CheckInfos"+CheckInfos);
//        }
//

        return "success";
    }

    /**
     * 旧账册数据清零核放单
     */
    @RequestMapping(value = "clearInventoryH", method = RequestMethod.GET)
    @ResponseBody
    public String clearInventoryH() throws RemoteException, ServiceException {
        //查询剩余数量大于0的底账信息
        List<AAccountBook> aAccountBookList = new ArrayList<>();
        aAccountBookList = aAccountBookService.queryClearInventoryHData();

//        //申请单
//        List<Map<String, Object>> ApprLists = this.buildApprLists(opApprHead,infolist);
//        //拼装Json
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("SaveType", "1");//这里0为暂存，咱们不存在暂存申请，直接发送1
//        map.put("DeclType", "1");
//        map.put("ApprLists", ApprLists);
//        map.put("ApprHead", apprHeadMap);
//
//        String jsonString = JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);
//        System.out.println(jsonString);
//        logger.error(jsonString);
//        // 1 获得key
//        String tickId = getKeyService.builder();
//        System.out.println(tickId);
//
//        // 2 调用接口
//        //设置接口名
//        String serviceName = "ApprSave";
//        // 调用 申请单保存
//        String result = fljgWsClient.getResult(jsonString, tickId, serviceName);
//        System.out.println("ApprSave result: " + result);
//        logger.error(">>>>>>>>>>>>>>>>>调用申请单申报result： "+result);
//
//        JSONObject jsonObject = JSON.parseObject(result);
//        String state = jsonObject.getString("state");
//        if(state.equals("1")) {
//
//        }else {
//            String CheckInfos = jsonObject.getString("CheckInfos");
//            logger.error("CheckInfos"+CheckInfos);
//        }
//

        return "success";
    }
    
//==================================================================================================================
    private Map<String, Object> buildApprHead(OpApprHead opApprHead) {
        Map<String, Object> apprHeadMap = new HashMap<>();
        apprHeadMap.put("ApprId", opApprHead.getApprId() != null ? opApprHead.getApprId() : null);
        apprHeadMap.put("LinkId", opApprHead.getLinkId()!= null ? opApprHead.getLinkId() : null);
        apprHeadMap.put("OrderNum", opApprHead.getOrderNum()!= null ? opApprHead.getOrderNum() : null);
        apprHeadMap.put("ApprType", opApprHead.getApprType()!= null ? opApprHead.getApprType() : null);
        apprHeadMap.put("IoType", opApprHead.getIoType()!= null ? opApprHead.getIoType() : null);
        apprHeadMap.put("BondInvtNo", opApprHead.getBondInvtNo()!= null ? opApprHead.getBondInvtNo() : null);
        //件数
        apprHeadMap.put("PackNo", opApprInfoService.sumQtyByHeadId(opApprHead.getId()));
        //重量
        apprHeadMap.put("GrossWt", opApprInfoService.sumWeightByHeadId(opApprHead.getId()));

        apprHeadMap.put("CustomsCode", opApprHead.getCustomsCode()!= null ? opApprHead.getCustomsCode() : null);
        apprHeadMap.put("EmsNo", opApprHead.getEmsNo()!= null ? opApprHead.getEmsNo() : null);
        apprHeadMap.put("GNo", opApprHead.getgNo()!= null ? opApprHead.getgNo() : null);
        apprHeadMap.put("DNote", opApprHead.getdNote()!= null ? opApprHead.getdNote() : null);

        apprHeadMap.put("ItemNum", opApprHead.getItemNum()!= null ? opApprHead.getItemNum() : null);
        apprHeadMap.put("InputEr", opApprHead.getInputEr()!= null ? opApprHead.getInputEr() : null);
        apprHeadMap.put("InputDate", opApprHead.getInputDate()!= null ? opApprHead.getInputDate() : null);

        apprHeadMap.put("DDate", opApprHead.getdDate()!= null ? opApprHead.getdDate() : null);
        apprHeadMap.put("ApproveDate", opApprHead.getApproveDate()!= null ? opApprHead.getApproveDate() : null);
        apprHeadMap.put("ApproveNote", opApprHead.getApproveNote()!= null ? opApprHead.getApproveNote() : null);

        apprHeadMap.put("TradeCode", opApprHead.getTradeCode()!= null ? opApprHead.getTradeCode() : null);
        apprHeadMap.put("TradeName", opApprHead.getTradeName()!= null ? opApprHead.getTradeName() : null);

        apprHeadMap.put("OwnerCode", opApprHead.getOwnerCode()!= null ? opApprHead.getOwnerCode() : null);
        apprHeadMap.put("OwnerName", opApprHead.getOwnerName()!= null ? opApprHead.getOwnerName() : null);

        apprHeadMap.put("AgentCode", opApprHead.getAgentCode()!= null ? opApprHead.getAgentCode() : null);
        apprHeadMap.put("AgentName", opApprHead.getAgentName()!= null ? opApprHead.getAgentName() : null);
        apprHeadMap.put("LoadingFlag", opApprHead.getLoadingFlag()!= null ? opApprHead.getLoadingFlag() : null);

        apprHeadMap.put("DeclType", opApprHead.getDeclType()!= null ? opApprHead.getDeclType() : null);
        apprHeadMap.put("Status", opApprHead.getStatus()!= null ? opApprHead.getStatus() : null);
        apprHeadMap.put("PassStatus", opApprHead.getPassStatus()!= null ? opApprHead.getPassStatus() : null);
        apprHeadMap.put("CREATETIME", opApprHead.getCreateTime()!= null ? opApprHead.getCreateTime() : null);
        return apprHeadMap;
    }
    private List<Map<String, Object>> buildApprLists(OpApprHead opApprHead, List<OpApprInfo> infolist) {

        List<Map<String, Object>> list= new ArrayList<>();
        for(OpApprInfo info : infolist){
            Map<String, Object> map = new HashMap<>(16);
            map.put("ApprGNo",info.getApprGNo());
            map.put("ApprId",opApprHead.getApprId());
            map.put("BisInfoId",null);
            map.put("CodeTs",info.getCodeTs());
            map.put("ctnNum",info.getCtnNum());
            map.put("Curr",null);
            map.put("GModel",null);
            map.put("GName",info.getgName());
            map.put("GNo",info.getgNo());
            map.put("Gqty",info.getgQty());
            map.put("GrossWt",info.getGrossWt());
            map.put("GUnit",info.getgUnit());

            map.put("ioType",opApprHead.getIoType());
            map.put("LinkId",opApprHead.getLinkId());
            map.put("Qty1",info.getgQty());
            map.put("TotalSum",null);
            map.put("Unit1",info.getgUnit());
            list.add(map);
        }

        return list;
    }

    private String getIeFlag(String IoType) {
        if(IoType.equals("1"))
            return "I";
        else if(IoType.equals("2"))
            return "E";
        else
            return "0";

    }

    public String buildJson(OpManiHead opManiHead) {

        List<PropertyFilter> infofilters = new ArrayList<PropertyFilter>();
        infofilters.add(new PropertyFilter("EQS_headId", opManiHead.getId()));
        List<OpManiInfo> maniInfoList = opManiInfoService.search(infofilters);
        //拼装Json
        JSONObject jsonObject = new JSONObject();
        //构建接口json Manibase 表头
        JSONObject manibase = new JSONObject();

        manibase.put("ManifestId", opManiHead.getManifestId()==null?null:opManiHead.getManifestId());
        manibase.put("TradeCode",  opManiHead.getTradeCode()==null?null:opManiHead.getTradeCode());
        manibase.put("TradeName",  opManiHead.getTradeName()==null?null:opManiHead.getTradeName());
        manibase.put("CustomsCode",opManiHead.getCustomsCode()==null?null:opManiHead.getCustomsCode());
        manibase.put("IeFlag",opManiHead.getIeFlag()==null?null:opManiHead.getIeFlag());
        manibase.put("GoodsValue", opManiHead.getGoodsValue()==null?null:opManiHead.getGoodsValue());
        manibase.put("GrossWt", opManiHead.getGrossWt()==null?null:opManiHead.getGrossWt());
        manibase.put("VehicleId", opManiHead.getVehicleId()==null?null:opManiHead.getVehicleId());
        manibase.put("VehicleWeight", opManiHead.getVehicleWeight()==null?null:opManiHead.getVehicleWeight());
        manibase.put("Status", opManiHead.getStatus()==null?null:opManiHead.getStatus());
        manibase.put("PassStatus", opManiHead.getPassStatus()==null?null:opManiHead.getPassStatus());
        manibase.put("InputEr", opManiHead.getInputEr()==null?null:opManiHead.getInputEr());
        manibase.put("InputDate", opManiHead.getInputDate()==null?null:opManiHead.getInputDate());
        manibase.put("DDate", opManiHead.getDDate()==null?null:opManiHead.getDDate());
        manibase.put("DNote", opManiHead.getDNote()==null?null:opManiHead.getDNote());
        manibase.put("ApprDate", opManiHead.getApprDate()==null?null:opManiHead.getApprDate());
        manibase.put("ApprNote", opManiHead.getApprNote()==null?null:opManiHead.getApprNote());
        manibase.put("OrgCode", opManiHead.getOrgCode()==null?null:opManiHead.getOrgCode());
        manibase.put("ContaId", opManiHead.getContaId()==null?null:opManiHead.getContaId());
        manibase.put("PassportDate", opManiHead.getPassportDate()==null?null:opManiHead.getPassportDate());
        manibase.put("DeclType", opManiHead.getDeclType()==null?null:opManiHead.getDeclType());
        manibase.put("SeqNo", opManiHead.getSeqNo()==null?null:opManiHead.getSeqNo());
        manibase.put("CusStatus", opManiHead.getCusStatus()==null?null:opManiHead.getCusStatus());
        manibase.put("CusRmk", opManiHead.getCusRmk()==null?null:opManiHead.getCusRmk());

        //表头加入json Manibase中
        jsonObject.put("Manibase", manibase);

        //构建货物信息
        JSONArray jsonArray = new JSONArray();


        for (OpManiInfo opManiInfo :maniInfoList) {
            JSONObject maniGoods = new JSONObject();
            maniGoods.put("ManifestId", opManiInfo.getManifestId()==null?null:opManiInfo.getManifestId());
            maniGoods.put("GNo",opManiInfo.getgNo()==null?null:opManiInfo.getgNo());
            maniGoods.put("ApprId", opManiInfo.getApprId()==null?null:opManiInfo.getApprId());
            maniGoods.put("ApprGNo", opManiInfo.getApprGNo()==null?null:opManiInfo.getApprGNo());
            maniGoods.put("CodeTs",opManiInfo.getCodeTs()==null?null:opManiInfo.getCodeTs());
            maniGoods.put("GName", opManiInfo.getGName()==null?null:opManiInfo.getGName());
            maniGoods.put("GModel",opManiInfo.getGModel()==null?null:opManiInfo.getGModel());
            maniGoods.put("GUnit",opManiInfo.getGUnit()==null?null:opManiInfo.getGUnit());
            maniGoods.put("GQty", opManiInfo.getGQty()==null?null:opManiInfo.getGQty());
            maniGoods.put("Unit1", opManiInfo.getGUnit1()==null?null:opManiInfo.getGUnit1());
            maniGoods.put("Qty1", opManiInfo.getGQty1()==null?null:opManiInfo.getGQty1());
            maniGoods.put("GrossWt",opManiInfo.getGrossWt()==null?null:opManiInfo.getGrossWt());
            maniGoods.put("ConfirmQty",opManiInfo.getConfirmQty()==null?null:opManiInfo.getConfirmQty());
            maniGoods.put("EmsGNo",opManiInfo.getEmsGNo()==null?null:opManiInfo.getEmsGNo());
            maniGoods.put("GdsMtno",opManiInfo.getGdsMtno()==null?null:opManiInfo.getGdsMtno());
            jsonArray.add(maniGoods);
        }
        //货物信息加入
        jsonObject.put("ManiGoods", jsonArray);
        jsonObject.put("DeclType","1");//1首次申报2变更3作废
        jsonObject.put("SaveType","1");// 0-暂存，1-申报
        return jsonObject.toJSONString();

    }

}
