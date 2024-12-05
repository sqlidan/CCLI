package com.haiersoft.ccli.wms.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.haiersoft.ccli.cost.entity.BisStandingBook;
import com.haiersoft.ccli.cost.service.StandingBookService;
import com.haiersoft.ccli.wms.entity.BisInOut;
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

import com.haiersoft.ccli.base.entity.BaseItemname;
import com.haiersoft.ccli.base.entity.BaseSkuBaseInfo;
import com.haiersoft.ccli.base.service.ItemnameService;
import com.haiersoft.ccli.base.service.ProductClassService;
import com.haiersoft.ccli.base.service.SkuInfoService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.PropertiesUtil;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.BisEnterStock;
import com.haiersoft.ccli.wms.entity.BisEnterStockInfo;
import com.haiersoft.ccli.wms.entity.EnterStockInfoToExcel;
import com.haiersoft.ccli.wms.service.ASNService;
import com.haiersoft.ccli.wms.service.EnterStockInfoService;
import com.haiersoft.ccli.wms.service.EnterStockService;
import com.haiersoft.ccli.wms.service.TrayInfoService;

/**
 * @author pyl
 * @ClassName: EnterStockInfoController
 * @Description: 入库联系单明细Controller
 * @date 2016年3月3日 下午2:02:22
 */

@Controller
@RequestMapping("wms/enterStockInfo")
public class EnterStockInfoController extends BaseController {

    @Autowired
    private EnterStockService enterStockService;
    @Autowired
    private EnterStockInfoService enterStockInfoService;
    @Autowired
    private SkuInfoService skuInfoService;
    @Autowired
    private ProductClassService productClassService;//货品类型
    @Autowired
    private ASNService asnService;
    @Autowired
    private TrayInfoService trayInfoService;
    @Autowired
    private StandingBookService standingBookService;
    
    @Autowired
    private ItemnameService ItemnameService;
    
    /**
     * @return
     * @throws
     * @author pyl
     * @Description: 入库联系单明细查询
     * @date 2016年3月3日 下午2:06:10
     */
    @RequestMapping(value = "json/{linkId}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request, @PathVariable("linkId") String linkId) {
        Page<BisEnterStockInfo> page = getPage(request);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        PropertyFilter filter = new PropertyFilter("EQS_linkId", linkId);
        filters.add(filter);
        page = enterStockInfoService.search(page, filters);
        return getEasyUIData(page);
    }

    /**
     * 添加入库联系单明细跳转
     *
     * @param model
     */
    @RequestMapping(value = "create/{linkId}", method = RequestMethod.GET)
    public String createForm(@PathVariable("linkId") String linkId, Model model) {
        model.addAttribute("linkId", linkId);
        BisEnterStock bisEnterStock=enterStockService.get(linkId);
        model.addAttribute("bisEnterStock",bisEnterStock!=null?bisEnterStock:new BisEnterStock());
        BisEnterStockInfo bisEnterStockInfo=new BisEnterStockInfo();
        bisEnterStockInfo.setBgdh(bisEnterStock!=null?bisEnterStock.getBgdh():null);
        bisEnterStockInfo.setYcg(bisEnterStock!=null?bisEnterStock.getYcg():null);
        bisEnterStockInfo.setBgdhDate(bisEnterStock!=null?bisEnterStock.getBgdhDate():null);
        model.addAttribute("enterStockInfo",bisEnterStockInfo);
        model.addAttribute("action", "create");
        return "wms/enterStock/enterStockInfoForm";
    }

    /**
     * 添加入库联系单明细
     *
     * @param user
     * @param model
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public String create(@Valid BisEnterStockInfo bisEnterStockInfo, Model model, HttpServletRequest request) {
        User user = UserUtil.getCurrentUser();
		Date date=new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");  
        if (null == bisEnterStockInfo.getSku() || "".equals(bisEnterStockInfo.getSku())) {
            String skuId = this.makeNewSku(bisEnterStockInfo);
            bisEnterStockInfo.setSku(skuId);
        }

        //2024-11-19 徐峥
        //添加的入库联系单明细时，校验HS编码是否不一样
        if (bisEnterStockInfo.getHsCode()!=null && bisEnterStockInfo.getHsCode().trim().length() > 0){
            List<PropertyFilter> filters =new ArrayList<PropertyFilter>();
            PropertyFilter filter = new PropertyFilter("EQS_linkId", bisEnterStockInfo.getLinkId());
            filters.add(filter);
            List<BisEnterStockInfo> bisEnterStockInfoList = enterStockInfoService.search(filters);
            if (bisEnterStockInfoList!=null && bisEnterStockInfoList.size() > 0){
                for (BisEnterStockInfo forBisEnterStockInfo:bisEnterStockInfoList) {
                    if (!bisEnterStockInfo.getHsCode().equals(forBisEnterStockInfo.getHsCode())){
                        return "入库联系单"+bisEnterStockInfo.getLinkId()+" 明细中的HS编码不一致";
                    }
                }
            }
        }

/*    	if(null != bisEnterStockInfo.getHsItemname() || !"".equals(bisEnterStockInfo.getHsItemname())){
        	//BaseItemname baseItemname=ItemnameService.find("cargoName", bisEnterStockInfo.getHsItemname());
        	if (ItemnameService.find("cargoName", bisEnterStockInfo.getHsItemname())==null) {
        		BaseItemname baseItemname=new BaseItemname();
        		baseItemname.setCode(formatter.format(date));
    			baseItemname.setCargoName(bisEnterStockInfo.getHsItemname());
    			ItemnameService.save(baseItemname);
			}
        }*/
        bisEnterStockInfo.setOperator(user.getName());
        bisEnterStockInfo.setOperateTime(new Date());
        enterStockInfoService.save(bisEnterStockInfo);
        return "success";
    }

    /**
     * 修改入库联系单明细跳转
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Integer id, Model model) {
    	BisEnterStockInfo enterStockInfo=enterStockInfoService.get(id);
        model.addAttribute("enterStockInfo",enterStockInfo);
        BisEnterStock bisEnterStock=enterStockService.get(enterStockInfo.getLinkId());
        model.addAttribute("bisEnterStock",bisEnterStock!=null?bisEnterStock:new BisEnterStock());
        model.addAttribute("action", "update");
        return "wms/enterStock/enterStockInfoForm";
    }
    
    /**
     * 修改入库联系单明细跳转(后期调整)
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "updateUpdate/{id}", method = RequestMethod.GET)
    public String updateUpdateForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("enterStockInfo", enterStockInfoService.get(id));
        model.addAttribute("action", "updateForCtnNumCascade");
        return "wms/enterStock/enterStockInfoFormUpdate";
    }
    
    /**
     * 修改入库联系单明细
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public String update(@Valid @ModelAttribute @RequestBody BisEnterStockInfo bisEnterStockInfo, Model model) {
		Date date=new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS"); 
/*    	if(null != bisEnterStockInfo.getHsItemname() || !"".equals(bisEnterStockInfo.getHsItemname())){
        	//BaseItemname baseItemname=ItemnameService.find("cargoName", bisEnterStockInfo.getHsItemname());
        	if (ItemnameService.find("cargoName", bisEnterStockInfo.getHsItemname())==null) {
        		BaseItemname baseItemname=new BaseItemname();
        		baseItemname.setCode(formatter.format(date));
    			baseItemname.setCargoName(bisEnterStockInfo.getHsItemname());
    			ItemnameService.save(baseItemname);
			}
        }*/
        bisEnterStockInfo.setUpdateTime(new Date());
        enterStockInfoService.update(bisEnterStockInfo);
        return skuInfoService.updateSKUInfo(bisEnterStockInfo);
    }

    /**
     * 修改入库联系单明细
     */
    @RequestMapping(value = "updateForCtnNumCascade", method = RequestMethod.POST)
    @ResponseBody
    public String updateForCtnNumCascade(HttpServletRequest request,@Valid @ModelAttribute @RequestBody BisEnterStockInfo bisEnterStockInfo, Model model) {
    	//todo
    	String ctnNumOld = request.getParameter("ctnNumOld");
    	String linkId = request.getParameter("linkId");
    	String ctnNumNew = request.getParameter("ctnNum");
        bisEnterStockInfo.setUpdateTime(new Date());
        enterStockInfoService.update(bisEnterStockInfo);
        String result = skuInfoService.updateSKUInfo(bisEnterStockInfo);
        //更新asn的箱号
        String result1 = asnService.updateASNforNewCtnNum(ctnNumNew,ctnNumOld,linkId);
        //更新库存表的箱号
        String result2 = trayInfoService.updateTrayInfoforNewCtnNum(ctnNumNew,ctnNumOld,linkId);
        if("".equals(result)){
        	return "false1";
        }else if("".equals(result1)){
        	return "false2";
        }else if("".equals(result2)){
        	return "false3";
        }else{
        	return "success";
        }
    }
    
    /**
     * 复制入库联系单跳转
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "copy/{id}", method = RequestMethod.GET)
    public String copyForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("enterStockInfo", enterStockInfoService.get(id));
        model.addAttribute("action", "copy");
        return "wms/enterStock/enterStockInfoForm";
    }

    /**
     * 复制后添加入库联系单
     *
     * @param user
     * @param model
     */
    @RequestMapping(value = "copy", method = RequestMethod.POST)
    @ResponseBody
    public String createCopy(@Valid BisEnterStockInfo bisEnterStockInfo, Model model) {
        User user = UserUtil.getCurrentUser();
        bisEnterStockInfo.setOperator(user.getName());
        bisEnterStockInfo.setOperateTime(new Date());
        enterStockInfoService.save(bisEnterStockInfo);
        return "success";
    }

    /**
     * 获取SKu字典
     */
    @RequestMapping(value = "selSku", method = RequestMethod.GET)
    @ResponseBody
    public List<BaseSkuBaseInfo> getSku(HttpServletRequest request) {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        List<BaseSkuBaseInfo> skuBaseInfo = skuInfoService.search(filters);
        return skuBaseInfo;
    }

    /**
     * 获取linkId字典
     */
    @RequestMapping(value = "selLinkId", method = RequestMethod.GET)
    @ResponseBody
    public List<BisEnterStock> getLinkId(HttpServletRequest request) {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        List<BisEnterStock> bisEnterStock = enterStockService.search(filters);
        return bisEnterStock;
    }

    /**
     * 根据入库联系单ID自动获取相对应的提单号
     */
    @RequestMapping(value = "changeItemNum", method = RequestMethod.GET)
    @ResponseBody
    public String changeItemNum(String linkId, HttpServletRequest request) {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        PropertyFilter filter = new PropertyFilter("EQS_linkId", linkId);
        filters.add(filter);
        List<BisEnterStock> enterStock = enterStockService.search(filters);
        BisEnterStock tt = null;
        if (!enterStock.isEmpty()) {
            tt = enterStock.get(0);
            return tt.getItemNum();
        } else {
            return "";
        }
    }

    /**
     * 根据skuId获得SKU的基本信息
     */
    @RequestMapping(value = "cSku", method = RequestMethod.GET)
    @ResponseBody
    public List<Object> cSku(String sku, HttpServletRequest request) {
//		BaseSkuBaseInfo skuBaseInfo = skuInfoService.get(skuId);
        PropertyFilter filter = new PropertyFilter("EQS_skuId", sku);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        filters.add(filter);
        List<BaseSkuBaseInfo> skuBaseInfo = skuInfoService.search(filters);
        BaseSkuBaseInfo skutt = skuBaseInfo.get(0);
        List<Object> skuBase = new ArrayList<Object>();
        skuBase.add(skutt.getCargoName());
        skuBase.add(skutt.getTypeSize());
        skuBase.add(skutt.getNetSingle());
        skuBase.add(skutt.getGrossSingle());
        skuBase.add("千克");
        skuBase.add(skutt.getCargoType());
        skuBase.add(skutt.getTypeName());
        skuBase.add(skutt.getClassType());
        skuBase.add(skutt.getClassName());
        skuBase.add(skutt.getMscNum());
        skuBase.add(skutt.getLotNum());
        skuBase.add(skutt.getProNum());
        skuBase.add(skutt.getRkdh());
        skuBase.add(skutt.getShipNum());
        return skuBase;
    }

    /**
     * @param id
     * @return
     * @throws
     * @author pyl
     * @Description: 删除
     * @date 2016年3月3日 下午2:37:12
     */
    @RequestMapping(value = "deleteEnterStockInfo/{ids}")
    @ResponseBody
    public String deleteContractInfo(@PathVariable("ids") List<Integer> ids) {
//		enterStockInfoService.deleteInfo(id);
        for (int i = 0; i < ids.size(); i++) {
            enterStockInfoService.delete((ids.get(i)));
        }
        return "success";
    }

    /**
     * 添加SKUList跳转
     */
    @RequestMapping(value = "skulist", method = RequestMethod.GET)
    public String seletSKUList(Model model) {
        return "wms/enterStock/skuListByEnter";
    }
    /**
     * 添加hsList跳转
     */
    @RequestMapping(value = "hslist", method = RequestMethod.GET)
    public String seletHSList(Model model) {
        return "wms/enterStock/hsListByEnter";
    }

    
    
    
    
    /**
     * 入库联系单明细excel跳转
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "into/{linkId}", method = RequestMethod.GET)
    public String intoExcel(Model model, @PathVariable("linkId") String linkId) {
        model.addAttribute("linkId", linkId);
        model.addAttribute("action", "intot");
        return "wms/enterStock/enterStockInfoInto";
    }

    /**
     * 入库联系单明细excel导入
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "intot/{linkId}", method = RequestMethod.POST)
    @ResponseBody
    public String intoExcel2(@RequestParam(value = "file", required = false) MultipartFile file,
                             HttpServletRequest request,
                             Model model,
                             @PathVariable("linkId") String linkId) {
        String error = "";
        try {
            ImportParams params = new ImportParams();
            params.setTitleRows(1);
            List<EnterStockInfoToExcel> list = ExcelImportUtil.importExcel(file.getInputStream(), EnterStockInfoToExcel.class, params);
            BisEnterStockInfo newObj = null;
            BaseSkuBaseInfo skuObj = null;
            User user = UserUtil.getCurrentUser();
            BisEnterStock enter = enterStockService.get(linkId);
            if (list != null && list.size() > 0) {
                int i = 2;
                for (EnterStockInfoToExcel getObj : list) {
                    //创建一个新的SKU
                    i++;
                    skuObj = new BaseSkuBaseInfo();
                    skuObj = createNewSku(getObj);
                    if (skuObj == null) {
                        error += String.valueOf(i) + ",";
                    } else {
                        newObj = new BisEnterStockInfo();
                        newObj.setLinkId(linkId);
                        newObj.setItemNum(enter.getItemNum());
                        newObj.setCargoName(getObj.getCargoName());
                        newObj.setSku(skuObj.getSkuId());
                        newObj.setTypeSize(skuObj.getTypeSize());
                        newObj.setCarNum(getObj.getCarNum());
                        newObj.setCtnNum(getObj.getCtnNum());
                        newObj.setBgdh(getObj.getBgdh());
                        newObj.setYcg(getObj.getYcg());
                        newObj.setFeeCode(getObj.getFeeCode());
                       //hs zengjia 
                        newObj.setHsCode(getObj.getHscode());
                        newObj.setHsItemname(getObj.getHsItemname());//海关品名
                        newObj.setAccountBook(getObj.getAccountBook());//账册商品序号
                        if(null!=getObj.getFeeCode()){
                            newObj.setIfUseTruck("1");
                        }
                        newObj.setDcarNum(getObj.getDcarNum());
                        newObj.setDctnNum(getObj.getDctnNum());
                        newObj.setIfBack(getObj.getIfBack());
                        newObj.setIfBagging(getObj.getIfBagging());
                        newObj.setIfWrap(getObj.getIfWrap());
                        newObj.setIfCheck(getObj.getIfCheck());
                        newObj.setOrderNum(getObj.getOrderNum());
                        newObj.setPiece(getObj.getPiece());
                        newObj.setGrossWeight(getObj.getGrossWeight());
                        newObj.setNetWeight(getObj.getNetWeight());
                        newObj.setGrossSingle(skuObj.getGrossSingle());
                        newObj.setNetSingle(skuObj.getNetSingle());
                        newObj.setUnits("1");
                        newObj.setProjectNum(getObj.getProjectNum());
                        newObj.setOperator(user.getName());
                        newObj.setOperateTime(new Date());
                        
                        if (null != getObj.getMakeTime()) {
                            newObj.setMakeTime(getObj.getMakeTime());
                        }
                        newObj.setBigType(skuObj.getCargoType());
                        newObj.setBigTypeName(getObj.getBigTypeName());
                        newObj.setLittleType(skuObj.getClassType());
                        newObj.setLittleTypeName(getObj.getLittleTypeName());
                        newObj.setPrice(getObj.getPrice());
                        if (!StringUtils.isNull(getObj.getRkNum())) {
                            newObj.setRkNum(getObj.getRkNum());
                        }
                        newObj.setMscNum(getObj.getMscNum());
                        newObj.setLotNum(getObj.getLotNum());
                        newObj.setShipNum(getObj.getShipNum());
                        enterStockInfoService.save(newObj);
                    }//end if sku
                }//end for
            }//end if
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (error == "") {
            return "success";
        } else {
            error = error.substring(0, error.length() - 1);
            return error;
        }
    }
    /*
     * @author PYL
     * 根据导入模板的数据创建一条新的SKU
     * update slh 根据单净、毛，计算总净、毛，要求“总”保留小数后8位
     */
    private BaseSkuBaseInfo createNewSku(EnterStockInfoToExcel getObj) {
        BaseSkuBaseInfo sku = new BaseSkuBaseInfo();
        List<String> typeId = getTypeId(getObj.getBigTypeName(), getObj.getLittleTypeName());//获得大类小类的ID
        if (!typeId.get(0).equals("0") && !typeId.get(1).equals("0")) {
            String skuId = skuInfoService.getSKUId(Integer.valueOf(typeId.get(0)));
            sku.setSkuId(skuId);//id
            sku.setCargoState("1");//库存类型
            sku.setCargoName(getObj.getCargoName()); //品名
            sku.setCargoType(typeId.get(0));
            sku.setClassType(typeId.get(1));
            sku.setTypeName(getObj.getBigTypeName()); //大类名称
            sku.setClassName(getObj.getLittleTypeName()); //小类名称
            if (null != getObj.getTypeSize()) {
                sku.setTypeSize(getObj.getTypeSize()); //规格
                sku.setProducingArea(getObj.getCargoName() + " " + getObj.getTypeSize());//属性
            } else {
                sku.setTypeSize(""); //规格
                sku.setProducingArea(getObj.getCargoName() + "");//属性
            }
            sku.setPiece(getObj.getPiece());
            sku.setNetWeight(getObj.getNetWeight());
            sku.setGrossWeight(getObj.getGrossWeight());

            //单净、单毛
            //sku.setNetSingle(getObj.getNetWeight() / getObj.getPiece());
            //sku.setGrossSingle(getObj.getGrossWeight() / getObj.getPiece());
            double pnetsingle = getObj.getNetWeight() / getObj.getPiece();
            double pgrosssingle = getObj.getGrossWeight() / getObj.getPiece();

            double pns = (new BigDecimal(pnetsingle)).setScale(8, BigDecimal.ROUND_DOWN).doubleValue();
            double pgs = (new BigDecimal(pgrosssingle)).setScale(8, BigDecimal.ROUND_DOWN).doubleValue();

            sku.setNetSingle(pns);
            sku.setGrossSingle(pgs);


            if (getObj.getLotNum() != "") {
                sku.setLotNum(getObj.getLotNum());
            }
            if (getObj.getMscNum() != "") {
                sku.setMscNum(getObj.getMscNum());
            }
            if (getObj.getProjectNum() != "") {
                sku.setProNum(getObj.getProjectNum());
            }
            if (getObj.getRkNum() != "") {
                sku.setRkdh(getObj.getRkNum());
            }
            if (getObj.getShipNum() != "") {
                sku.setShipNum(getObj.getShipNum());
            }
            User user = UserUtil.getCurrentUser();
            if (user != null) {
                sku.setOperator(user.getName());
            }
            sku.setOperateTime(new Date());
            skuInfoService.save(sku);
        } else {
            sku = null;
        }
        return sku;
    }

    //获取大类小类的ID
    private List<String> getTypeId(String bigTypeName, String littleTypeName) {
        List<String> typeId = new ArrayList<String>();
        int bigId = productClassService.getProductClassId(bigTypeName, true);
        int littleId = productClassService.getProductClassId2(littleTypeName, bigId);
        if (littleId != 0 && bigId != 0) {
            if (productClassService.get(littleId).getPrintId() != bigId) {
                bigId = 0;
                littleId = 0;
            }
        }
        typeId.add(String.valueOf(bigId));
        typeId.add(String.valueOf(littleId));
        return typeId;
    }

    //判断件数是否为整数
    public static boolean isNumP(String str) {
        Pattern pattern = Pattern.compile("[0-9]+.?[0-9]+");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    //判断重量是否为数字
    public static boolean isNumW(String str) {
        Pattern pattern = Pattern.compile("[0-9]+.?[0-9]+");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
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
        String fileName = "入库联系单明细导入模板.xls";
        String filePath = PropertiesUtil.getPropertiesByName("downloadexcel", "application");
        String realpath = filePath + "WEB-INF\\classes\\importExcel\\入库联系单明细导入模板.xls";
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        OutputStream fos = null;
        InputStream fis = null;
        try {
            response.setContentType("application/vnd.ms-excel");
            String formatFileName = new String(fileName.getBytes("GB2312"), "ISO-8859-1");
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
            	e.printStackTrace();
            }
        }
    }

    /**
     * @return
     * @throws
     * @author pyl
     * @Description: 新建SKU
     * @date 2016年6月14日
     */
    public String makeNewSku(BisEnterStockInfo info) {
        User user = UserUtil.getCurrentUser();
        BaseSkuBaseInfo skuObj = new BaseSkuBaseInfo();
        String linkId=info.getLinkId();
        BisEnterStock enterObj=enterStockService.get(linkId);
        String bondSign="";
        if(enterObj.getIfBonded().equals("0")){
        	bondSign="FBS";
        }else{
        	bondSign="BS";
        }
        skuObj.setSkuId(bondSign+skuInfoService.getSKUId(Integer.valueOf(info.getBigType())));
        skuObj.setCargoState("1");
        skuObj.setProducingArea(info.getCargoName() + info.getTypeSize());
        skuObj.setCargoName(info.getCargoName());
        skuObj.setCargoType(info.getBigType());
        skuObj.setTypeName(info.getBigTypeName());
        skuObj.setClassType(info.getLittleType());
        skuObj.setClassName(info.getLittleTypeName());
        skuObj.setTypeSize(info.getTypeSize());
        skuObj.setPiece(info.getPiece());
        skuObj.setNetWeight(info.getNetWeight());
        skuObj.setNetSingle(info.getNetSingle());
        skuObj.setGrossWeight(info.getGrossWeight());
        skuObj.setGrossSingle(info.getGrossSingle());
        skuObj.setMscNum(info.getMscNum());
        skuObj.setLotNum(info.getLotNum());
        skuObj.setProNum(info.getProjectNum());
        skuObj.setRkdh(info.getRkNum());
        skuObj.setShipNum(info.getShipNum());
        skuObj.setOperator(user.getName());
        skuObj.setOperateTime(new Date());
        skuInfoService.save(skuObj);
        return skuObj.getSkuId();
    }


    /**
     * 入库联系单明细导出报表
     *
     * @param id
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "exportinfo/{linkId}", method = RequestMethod.POST)
    @ResponseBody
    public void exportInfo(HttpServletRequest request, HttpServletResponse response, @PathVariable("linkId") String linkId) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        TemplateExportParams params = new TemplateExportParams("exceltemplate/enterStockInfo.xls");
        String excelName = "入库联系单明细.xls";
        BisEnterStock obj = enterStockService.get(linkId);
        map.put("stockIn", obj.getStockIn());
        if (null != obj.getEtaShip()) {
            map.put("etaShip", obj.getEtaShip());
        }

        String sortingAsk = "";
        if (!"".equals(obj.getSortingAsk1()) && null != obj.getSortingAsk1()) {
            sortingAsk += obj.getSortingAsk1() + ",";
        }
        if (!"".equals(obj.getSortingAsk2()) && null != obj.getSortingAsk2()) {
            sortingAsk += obj.getSortingAsk2() + ",";
        }
        if (!"".equals(obj.getSortingAsk3()) && null != obj.getSortingAsk3()) {
            sortingAsk += obj.getSortingAsk3() + ",";
        }
        if (!"".equals(obj.getSortingAsk4()) && null != obj.getSortingAsk4()) {
            sortingAsk += obj.getSortingAsk4() + ",";
        }
        if (!"".equals(obj.getSortingAsk5()) && null != obj.getSortingAsk5()) {
            sortingAsk += obj.getSortingAsk5() + ",";
        }
        if (!"".equals(obj.getSortingAsk6()) && null != obj.getSortingAsk6()) {
            sortingAsk += obj.getSortingAsk6() + ",";
        }
        if (!"".equals(sortingAsk)) {
            sortingAsk = sortingAsk.substring(0, sortingAsk.length() - 1);
        }
        map.put("sortingAsk", sortingAsk);

        map.put("billNum", obj.getItemNum());
        map.put("ctnAmount", obj.getCtnAmount());

        if (!StringUtils.isNull(obj.getRkNum())) {
            map.put("rkNum", obj.getRkNum());
        } else {
            map.put("rkNum", "");
        }

        if (null != obj.getEtdWarehouse()) {
            map.put("etdWarehouse", obj.getEtdWarehouse());
        }

        if ("0".equals(obj.getIfMacAdmit())) {
            map.put("ifMacAdmit", "否");
        } else {
            map.put("ifMacAdmit", "是");
        }

        if (!StringUtils.isNull(obj.getTemperature())) {
            map.put("temperature", obj.getTemperature());
        } else {
            map.put("temperature", " ");
        }

        User user = UserUtil.getCurrentUser();
        map.put("user", user.getName());
        map.put("now", new Date());

        if ("0".equals(obj.getIfWeigh())) {
            map.put("ifWeigh", "否");
        } else {
            map.put("ifWeigh", "是");
        }

        if ("0".equals(obj.getIfBagging())) {
            map.put("ifBagging", "否");
        } else {
            map.put("ifBagging", "是");
        }

        map.put("receptacle", obj.getReceptacle());

        if ("0".equals(obj.getIfWrap())) {
            map.put("ifWrap", "否");
        } else {
            map.put("ifWrap", "是");
        }

        if (!"".equals(obj.getSortingSpecial()) && null != obj.getSortingSpecial()) {
            map.put("special", obj.getSortingSpecial());
        } else {
            map.put("special", "");
        }

        listMap = enterStockInfoService.searchInfo(linkId);
        if (null != listMap && listMap.size() > 0) {
            map.put("maplist", listMap);
        }

        if (!StringUtils.isNull(obj.getRemark())) {
            map.put("remark", obj.getRemark());
        } else {
            map.put("remark", "");
        }

        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        workbook.getSheetAt(0).setForceFormulaRecalculation(true);//强制执行公式

        String formatFileName = new String(excelName.getBytes("GB2312"), "ISO-8859-1");
        response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
        OutputStream os = response.getOutputStream();
        workbook.write(os); // 写入文件
        os.close(); // 关闭流
    }

    /**
     * 查验作业计费单
     */
    @RequestMapping(value = "exportCheckFeeExcel/{linkIdStr}", method = RequestMethod.POST)
    @ResponseBody
    public void exportCheckFeeExcel(HttpServletRequest request, HttpServletResponse response, @PathVariable("linkIdStr") String linkIdStr) throws IOException {
        String linkId = linkIdStr.split("_")[0];
        String itemNum = linkIdStr.split("_")[1];
        Map<String, Object> map = new HashMap<String, Object>();
        TemplateExportParams params = new TemplateExportParams("exceltemplate/enterStockCheckFeeExcel.xls");
        String excelName = "查验作业计费单.xls";
        //数据查询
        BisEnterStock bisEnterStock = enterStockService.exportCheckFeeData(linkId);
        map.put("linkId", bisEnterStock.getLinkId()==null?"无":bisEnterStock.getLinkId());
        map.put("customerName", bisEnterStock.getRemark()==null?"无":bisEnterStock.getRemark());
        map.put("itemNum", bisEnterStock.getItemNum()==null?"无":bisEnterStock.getItemNum());
        map.put("vesselName", bisEnterStock.getVesselName()==null?"无":bisEnterStock.getVesselName());
        map.put("cargoName", bisEnterStock.getCargoName()==null?"无":bisEnterStock.getCargoName());
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("now",sdf.format(new Date()));
        List<PropertyFilter> filters = new ArrayList<>();
        PropertyFilter filter1 = new PropertyFilter("EQS_linkId", linkId);
        PropertyFilter filter2 = new PropertyFilter("EQI_ifReceive", "1");
        PropertyFilter filter3 = new PropertyFilter("EQS_billNum", itemNum);
        filters.add(filter1);
        filters.add(filter2);
        filters.add(filter3);
        List<BisStandingBook> maplist = standingBookService.search(filters);
        if (null != maplist && maplist.size() > 0) {
            List<Map<String, Object>> list = new ArrayList<>();
            Double allAmount = new Double(0.00);
            for (BisStandingBook forBisStandingBook:maplist) {
                Map<String, Object> mapTemp = new HashMap<String, Object>();
                mapTemp.put("feeName",forBisStandingBook.getFeeName()==null?" ":forBisStandingBook.getFeeName());
                mapTemp.put("num",forBisStandingBook.getNum()==null?" ":forBisStandingBook.getNum());
                mapTemp.put("price",forBisStandingBook.getPrice()==null?" ":forBisStandingBook.getPrice());
                mapTemp.put("receiveAmount",forBisStandingBook.getReceiveAmount()==null?" ":forBisStandingBook.getReceiveAmount());
                list.add(mapTemp);
                allAmount = allAmount + (forBisStandingBook.getReceiveAmount()==null?0.00:forBisStandingBook.getReceiveAmount());
            }
            map.put("maplist", list);
            map.put("allAmount", allAmount);
            String allAmountStr = convert(allAmount);
            map.put("allAmountStr", allAmountStr);
        }
        //汇总金额查询
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        workbook.getSheetAt(0).setForceFormulaRecalculation(true);//强制执行公式

        String formatFileName = new String(excelName.getBytes("GB2312"), "ISO-8859-1");
        response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
        OutputStream os = response.getOutputStream();
        workbook.write(os); // 写入文件
        os.close(); // 关闭流
    }

    private static final char[] CN_NUMBERS = {'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'};
    private static final char[] CN_UNITS = {'分', '角', '元', '拾', '佰', '仟', '万', '拾', '佰', '仟', '亿', '拾', '佰', '仟'};

    public static String convert(double money) {
        StringBuilder result = new StringBuilder();
        int yuan = (int) Math.floor(money);
        int jiao = (int) Math.floor(money * 10 % 10); // 角
        int fen = (int) Math.floor(money * 100 % 10); // 分

        if (yuan > 0) {
            String yuanStr = String.valueOf(yuan);
            for (int i = 0; i < yuanStr.length(); i++) {
                result.append(CN_NUMBERS[yuanStr.charAt(i) - '0']);
                result.append(CN_UNITS[yuanStr.length() - 1 - i + 2]);
            }
        } else {
            result.append(CN_NUMBERS[0]);
        }
//        result.append(CN_UNITS[2]); // 元

        if (jiao > 0) {
            result.append(CN_NUMBERS[jiao]);
            result.append(CN_UNITS[1]);
        } else if (jiao == 0 && result.charAt(result.length() - 1) == '元') {
            result.append(CN_NUMBERS[0]);
            result.append(CN_UNITS[1]);
        }

        if (fen > 0) {
            result.append(CN_NUMBERS[fen]);
            result.append(CN_UNITS[0]);
        }

        return result.toString();
    }
    
    /**
            * 分类监管核放单
     * 
     */
    @RequestMapping(value = "mani/{ids}")
    public String maniForm(HttpServletRequest request,Model model, @PathVariable("ids") List<Integer> ids) {

        List<BisEnterStockInfo> bisEnterStockInfos = new ArrayList<BisEnterStockInfo>();
        Double grossWeightTotal = 0.0; 
        int pieceTotal = 0;

        for(Integer id :ids) {
        	BisEnterStockInfo info = enterStockInfoService.get(id);
        	bisEnterStockInfos.add(info);
        	
        	BigDecimal bTotal = new BigDecimal(Double.toString(grossWeightTotal));
        	BigDecimal bThis = new BigDecimal(Double.toString(info.getGrossWeight()==null?0.00:info.getGrossWeight()));
        	grossWeightTotal = bTotal.add(bThis).doubleValue();

            pieceTotal += Integer.valueOf(info.getPiece()==null?0:info.getPiece());
        }
        
        BisEnterStock bes = enterStockService.get(bisEnterStockInfos.get(0).getLinkId());
                
        model.addAttribute("ctnTypeSize", bes.getCtnTypeSize());
        
        model.addAttribute("bisEnterStockInfos", bisEnterStockInfos);
        model.addAttribute("pieceTotal", pieceTotal);
        model.addAttribute("grossWeightTotal", grossWeightTotal);
        model.addAttribute("infoIds", ids);
        return "wms/enterStock/maniForm";
    }

}
