package com.haiersoft.ccli.wms.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.BeanUtils;
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
import com.haiersoft.ccli.base.entity.BaseSkuBaseInfo;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.base.service.ProductClassService;
import com.haiersoft.ccli.base.service.SkuInfoService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.CreatPDFUtils;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.ExcelUtil;
import com.haiersoft.ccli.common.utils.MyFileUtils;
import com.haiersoft.ccli.common.utils.MyPDFUtils;
import com.haiersoft.ccli.common.utils.PropertiesUtil;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.cost.service.AsnActionLogService;
import com.haiersoft.ccli.report.entity.Stock;
import com.haiersoft.ccli.report.service.InStockReportService;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.AsnAction;
import com.haiersoft.ccli.wms.entity.AsnInfoToExcel;
import com.haiersoft.ccli.wms.entity.BisAsn;
import com.haiersoft.ccli.wms.entity.BisAsnInfo;
import com.haiersoft.ccli.wms.entity.BisEnterStock;
import com.haiersoft.ccli.wms.service.ASNInfoService;
import com.haiersoft.ccli.wms.service.ASNService;
import com.haiersoft.ccli.wms.service.AsnActionService;
import com.haiersoft.ccli.wms.service.EnterStockService;
import com.itextpdf.text.PageSize;

/**
 * ASNInfocontroller
 *
 * @author lzg
 * @date 2016年3月1日
 */
@Controller
@RequestMapping("bis/asninfo")
public class ASNInfoContorller extends BaseController {
	@Autowired
	private ASNInfoService asnInfoService;
	@Autowired
	private AsnActionLogService asnActionLogService;
	@Autowired
	private ASNService asnService;
	@Autowired
	private SkuInfoService skuInfoService;
	@Autowired
	private ProductClassService productClassService;// 货品类型
	@Autowired
	private EnterStockService enterStockService;// 入库联系单
	@Autowired
	private ClientService clientService;// 客户
	@Autowired
	private AsnActionService asnActionService;
	@Autowired
	private InStockReportService inStockReportService;
	
	/*
	 * 列表页面table获取json
	 */
	@RequestMapping(value = "listjson", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<BisAsnInfo> page = getPage(request);
		page.setPageSize(99999);
		page.orderBy("operateTime").order(Page.DESC);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = asnInfoService.search(page, filters);
		return getEasyUIData(page);
	}

	/**
	 * java实现文件下载功能代码 创建时间：2014年12月23日
	 *
	 * @version
	 */
	@RequestMapping(value = "download", method = RequestMethod.POST)
	@ResponseBody
	public void fileDownLoad(HttpServletRequest request,HttpServletResponse response) throws ServletException {
		String fileName = "ASN明细导入模板.xls";
		String filePath = PropertiesUtil.getPropertiesByName("downloadexcel",
				"application");
		String realpath = filePath
				+ "WEB-INF\\classes\\importExcel\\ASN明细导入模板.xls";
		System.out.println(realpath);
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		OutputStream fos = null;
		InputStream fis = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			String formatFileName = new String(fileName.getBytes("GB2312"),
					"ISO-8859-1");
			// response.setHeader("Content-disposition",
			// "attachment;filename=\"" + URLEncoder.encode(fileName, "UTF-8")
			// +"\"");
			response.setHeader("Content-disposition", "attachment;filename=\""
					+ formatFileName + "\"");
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
	 * ASN明细excel跳转
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "into/{asn}", method = RequestMethod.GET)
	public String intoExcel(Model model, @PathVariable("asn") String asn) {
		model.addAttribute("asn", asn);
		model.addAttribute("action", "intot");
		return "wms/asn/asnInfoInto";
	}

	/**
	 * 入库联系单明细excel导入
	 *
	 * @param asn
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "intot/{asn}", method = RequestMethod.POST)
	@ResponseBody
	public String intoExcel2(
			@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request, Model model,
			@PathVariable("asn") String asn) {

		String error = "";

		try {

			ImportParams params = new ImportParams();

			params.setTitleRows(1);

			List<AsnInfoToExcel> list = ExcelImportUtil.importExcel(file.getInputStream(), AsnInfoToExcel.class, params);

			BisAsnInfo newObj = null;
			BaseSkuBaseInfo skuObj = null;

			User user = UserUtil.getCurrentUser();

			BisAsn bisAsn = asnService.get(asn);

			if (list != null && list.size() > 0) {

				// asn区间表共通存储
				AsnAction asnActionObj = new AsnAction();
				asnActionObj.setAsn(asn);
				asnActionObj.setEnterId(bisAsn.getLinkId());
				asnActionObj.setStatus("1");
				asnActionObj.setClientId(bisAsn.getStockIn());// 客户id

				BisEnterStock enterStock = enterStockService.get(bisAsn.getLinkId());

				asnActionObj.setJfClientId(enterStock.getStockOrgId());// 结算单位id

				BaseClientInfo getClient = clientService.get(Integer.valueOf(enterStock.getStockOrgId()));

				if (getClient != null) {
					asnActionObj.setClientDay(getClient.getCheckDay());// 客户计费日期
				}

				// if(正常||重收){计费开始=入库日期}else{计费开始=入库日期+1} 入库类型
                if (bisAsn.getInboundTime() != null) {

					if ("1".equals(bisAsn.getIfSecondEnter().trim()) || "2".equals(bisAsn.getIfSecondEnter().trim())) {
						asnActionObj.setChargeStaDate(bisAsn.getInboundTime());
					} else {
						asnActionObj.setChargeStaDate(DateUtils.addDay(bisAsn.getInboundTime(), 1));
					}

				} else {
					asnActionObj.setChargeStaDate(null);
					asnActionObj.setChargeStaDate(null);
				}

				// 填入费用方案
				if (enterStock != null) {
					asnActionObj.setFeePlanId(enterStock.getFeeId());
					asnActionObj.setBillCode(enterStock.getItemNum());
				}

				// 遍历明细
				AsnAction newAsnAction = null;

				int i = 2;

				for (AsnInfoToExcel getObj : list) {

					// 创建一个新的SKU
					i++;

					skuObj = new BaseSkuBaseInfo();
					skuObj = createNewSku(getObj);

					if (skuObj == null) {
						error += String.valueOf(i) + ",";
					} else {

						newObj = new BisAsnInfo();
						newObj.setAsnId(asn);
						newObj.setCargoName(getObj.getCargoName());
						newObj.setSkuId(skuObj.getSkuId());
						newObj.setTypeSize(skuObj.getTypeSize());
						newObj.setPiece(getObj.getPiece().doubleValue());
						newObj.setGrossWeight(getObj.getGrossWeight());
						newObj.setNetWeight(getObj.getNetWeight());
						newObj.setGrossSingle(skuObj.getGrossSingle());
						newObj.setNetSingle(skuObj.getNetSingle());
						newObj.setUnits("1");
						newObj.setOperator(user.getName());
						newObj.setOperateTime(new Date());
						newObj.setCargoType(skuObj.getCargoType());
						newObj.setRkNum(getObj.getRkNum());
						newObj.setMscNum(getObj.getMscNum());
						newObj.setLotNum(getObj.getLotNum());
						newObj.setCargoState("1");
						
						newObj.setHsCode(getObj.getHscode());
						newObj.setAccountBook(getObj.getAccountBook());
						newObj.setHsItemname(getObj.getHsItemname());
						

						asnInfoService.save(newObj);

						newAsnAction = new AsnAction();
						BeanUtils.copyProperties(asnActionObj, newAsnAction);// 复制
						newAsnAction.setSku(skuObj.getSkuId());
						newAsnAction.setCargoName(skuObj.getCargoName());

						asnActionService.save(newAsnAction);
						asnActionLogService.saveLog(newAsnAction, "1", 0, 0, "ASN明细EXCEL导入时增加ASN区间表记录(入库前)");

					}// end if sku

				}// end for

			}// end if

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
	 * @author PYL 根据导入模板的数据创建一条新的SKU
	 */
	private BaseSkuBaseInfo createNewSku(AsnInfoToExcel getObj) {

		BaseSkuBaseInfo sku = new BaseSkuBaseInfo();

		List<String> typeId = getTypeId(getObj.getBigTypeName(), getObj.getLittleTypeName());// 获得大类小类的ID

		if (!typeId.get(0).equals("0") && !typeId.get(1).equals("0")) {

			String skuId = skuInfoService.getSKUId(Integer.valueOf(typeId.get(0)));
			sku.setSkuId(skuId);// id
			sku.setCargoState("1");// 库存类型
			sku.setCargoName(getObj.getCargoName()); // 品名
			sku.setCargoType(typeId.get(0));
			sku.setClassType(typeId.get(1));
			sku.setTypeName(getObj.getBigTypeName()); // 大类名称
			sku.setClassName(getObj.getLittleTypeName()); // 小类名称

			if (getObj.getTypeSize() != null) {
				sku.setTypeSize(getObj.getTypeSize()); // 规格
				sku.setProducingArea(getObj.getCargoName() + " " + getObj.getTypeSize());// 属性
			} else {
				sku.setTypeSize(""); // 规格
				sku.setProducingArea(getObj.getCargoName() + "");// 属性
			}

			sku.setPiece(getObj.getPiece());
			sku.setNetWeight(getObj.getNetWeight());
			sku.setGrossWeight(getObj.getGrossWeight());
			sku.setNetSingle(getObj.getNetWeight() / getObj.getPiece());
			sku.setGrossSingle(getObj.getGrossWeight() / getObj.getPiece());

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

	// 获取大类小类的ID
	private List<String> getTypeId(String bigTypeName, String littleTypeName) {
		List<String> typeId = new ArrayList<String>();
		int bigId = productClassService.getProductClassId(bigTypeName, true);
		int littleId = productClassService.getProductClassId2(littleTypeName,
				bigId);
		/*
		 * if(littleId!=0 && bigId!=0){ if(
		 * productClassService.get(littleId).getPrintId() != bigId){ bigId = 0;
		 * littleId = 0; } }
		 */
		typeId.add(String.valueOf(bigId));
		typeId.add(String.valueOf(littleId));
		return typeId;
	}

	// 判断件数是否为整数
	public static boolean isNumP(String str) {
		Pattern pattern = Pattern.compile("[0-9]+.?[0-9]+");
		Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

	// 判断重量是否为数字
	public static boolean isNumW(String str) {
		Pattern pattern = Pattern.compile("[0-9]+.?[0-9]+");
		Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }
	
	/* 跳转ASN报表页面 */
	@RequestMapping(value = "infoReport", method = RequestMethod.GET)
	public String menuList() {
		return "wms/asn/asnInfoReport";
	}

	
	@RequestMapping("exportExcel")
    @ResponseBody
    public void exportInStockExcelB(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String asn=request.getParameter("asnId");
        String formatFileName = URLEncoder.encode("ASN明细报告书" + ".xls", "UTF-8");
        ExcelUtil excelUtil = new ExcelUtil();
        String filePath = PropertiesUtil.getPropertiesByName("filepath", "application");
        String srcPath = null;
        String desPath = null;
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        if (filePath == null || "".equals(filePath)) {
            filePath = request.getSession().getServletContext().getRealPath("/");
                listMap = asnInfoService.getAsnReportByAsn(asn);
                    srcPath = filePath + "WEB-INF\\classes\\exceltemplate\\asnInfoReport.xls";
//        			srcPath = filePath+"123\\inStockReportY.xls";
            desPath = filePath + "WEB-INF\\classes\\excelpost\\asnInfoReport.xls";
        } else {
                listMap = asnInfoService.getAsnReportByAsn(asn);
                    srcPath = filePath + "exceltemplate/asnInfoReport.xls";
            desPath = filePath + "excelpost/asnInfoReport.xls";
        }

        excelUtil.setSrcPath(srcPath);
        excelUtil.setDesPath(desPath);
        excelUtil.setSheetName("Sheet1");
        excelUtil.getSheet();

        int myNum = 10;

        if (listMap != null && listMap.size() > 0) {

            Map<String, Object> map = null;
            Double sumPice = 0d;
            excelUtil.setCellStrValue(4, 8, asn);
            for (int i = 0; i < listMap.size(); i++) {
                map = listMap.get(i);
                excelUtil.setCellStrValue(myNum + i , 0, map.get("TRAY_ID") != null ? map.get("TRAY_ID").toString() : "");
                excelUtil.setCellStrValue(myNum + i , 1, map.get("BILL_NUM") != null ? map.get("BILL_NUM").toString() : "");
                excelUtil.setCellStrValue(myNum + i , 2, map.get("CTN_NUM") != null ? map.get("CTN_NUM").toString() : "");
                excelUtil.setCellStrValue(myNum + i , 3, map.get("SKU_ID") != null ? map.get("SKU_ID").toString() : "");
                excelUtil.setCellStrValue(myNum + i , 4, map.get("CARGO_NAME") != null ? map.get("CARGO_NAME").toString() : "");
                excelUtil.setCellStrValue(myNum + i , 5, map.get("CARGO_LOCATION") != null ? map.get("CARGO_LOCATION").toString() : "");
                excelUtil.setCellDoubleValue(myNum + i , 6, Double.valueOf(map.get("PIECE") != null ? map.get("PIECE").toString() : "0d"));
                sumPice += Double.valueOf(map.get("PIECE") != null ? map.get("PIECE").toString() : "0d");
            }
            excelUtil.setCellStrValue(myNum + listMap.size()+1 , 0, "合计/Total");
            excelUtil.setCellDoubleValue(myNum + listMap.size()+1 , 6, sumPice);
        }

        excelUtil.exportToNewFile();
        FileInputStream in = new FileInputStream(new File(desPath));
        int len = 0;
        byte[] buffer = new byte[1024];
        response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
        OutputStream out = response.getOutputStream();
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
        if (null != in) {
            in.close();
        }
        if (null != out) {
            out.close();
        }

    }

    /**
     * @param request
     * @param response
     * @throws Exception
     * @throws
     * @Description: 导出 ASN明细报告书 PDF
     */
    @RequestMapping("exportPDF")
    @ResponseBody
    public void exportInStockPDF( HttpServletRequest request, HttpServletResponse response) throws Exception {
    		String asn=request.getParameter("asnId");
    		List<Map<String, Object>> getlist = null;
            String[] headCN = {"托盘号", "提单号", "MR（集装箱号）", "SKU号", "货物描述", "区位", "件数"};
            String[] headEN = {"LPN No.", "Inbound B/L No.", "MR/CTN No.", "SKU", "Description of cargo", "Lane", "Qty."};
            String pdfTitle = "在库报告书";
            StringBuffer pdfCN=new StringBuffer();
            	pdfCN.append("托盘号"
            				+" 	                                               "
            				+ "提单号"+"                                                     "
            				+"MR（集装箱号）"+"                             "
            				+"SKU号"+"                                                    "
            				+"货物描述"+"                                                 "
            				+"区位"+"                                                           "
            				+"件数"+"                      "
            				);
            String path = request.getSession().getServletContext().getRealPath("/");
            String pathHtml = path + "//insyshtm.html";
            String pathPdf = path + "//insyspdf.pdf";
            StringBuffer sbHtml = new StringBuffer();

            sbHtml.append("<div  style=\"height:5px;\"></div>");
            sbHtml.append("<table id=\"ctable\" style=\"border-spacing:0px;margin:none; text-align:left; border-collapse:collapse;font-family:宋体;font-size:17px;width:100%\">");
            getlist = asnInfoService.getAsnReportByAsn(asn);
            String customer = null;
            //填充标题头
            sbHtml.append("<tr style=\"height:30px; \">");
            for (String lab : headCN) {
//            	if("货物描述".equals(lab)){
//            		sbHtml.append("<td  width=\"300px\">").append(lab).append("</td>");
//            	}else if("MR/集装箱号".equals(lab)||"SKU".equals(lab)){
//            		sbHtml.append("<td  width=\"250px\">").append(lab).append("</td>");
//            	}else{
            		sbHtml.append("<td class=\"htd\">").append(lab).append("</td>");
//            	}
            }
            sbHtml.append("</tr>");
            sbHtml.append("<tr style=\"height:30px; \">");
            for (String lab : headEN) {
            	sbHtml.append("<td class=\"htd\">").append(lab).append("</td>");
            }
            sbHtml.append("</tr>");
            //int nTJ = 7;
            //填充内容
            if (getlist != null && getlist.size() > 0) {
                Double sumPice = 0d;
                Map<String, Object> getMap = null;
                customer = asn.toString();
                for (int i = 0; i < getlist.size(); i++) {
                    getMap = getlist.get(i);
                    if (getMap != null && getMap.size() > 0) {
                        sbHtml.append("<tr>");
                        sbHtml.append("<td>").append(getMap.get("TRAY_ID") != null ? getMap.get("TRAY_ID").toString() : "").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("BILL_NUM") != null ? getMap.get("BILL_NUM").toString() : "").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("CTN_NUM") != null ? getMap.get("CTN_NUM").toString() : "").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("SKU_ID") != null ? getMap.get("SKU_ID").toString() : "").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("CARGO_NAME") != null ? getMap.get("CARGO_NAME").toString().replaceAll("<", "《").replaceAll(">", "》") : "").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("CARGO_LOCATION") != null ? getMap.get("CARGO_LOCATION").toString() : "").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("PIECE") != null ? getMap.get("PIECE").toString() : "").append("</td>");
                        sbHtml.append("</tr>");
                       sumPice += Double.valueOf(getMap.get("PIECE") != null ? getMap.get("PIECE").toString() : "0");
                    }
                }//end for

                //添加合计
                sbHtml.append("<tr><td class=\"ftd\" style=\"border:0px; \">合计/Total：</td>");
                 sbHtml.append("<td class=\"ftd\" colspan=\"" + 5 + "\" style=\"border:0px;height:30px; \"></td>");
                 sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("#").format(sumPice)).append("</td>");
                sbHtml.append("</tr>");
            }
            sbHtml.append("</table>");

            sbHtml.append("<table style=\"border-spacing:0px;text-align:center; border-collapse:collapse;font-family:宋体;font-size:12px;width:100%\"><tr>");
            @SuppressWarnings("unused")
            User user = UserUtil.getCurrentUser();
            sbHtml.append("<td style=\"text-align:right;margin-top:5px;\">").append(" PrintTime : &nbsp;").append(DateUtils.getDateTime()).append("</td>");
            sbHtml.append("</tr></table>");

            System.out.println(sbHtml.toString());

            MyFileUtils html = new MyFileUtils();
            html.setFilePath(pathHtml);
            html.saveStrToFile(CreatPDFUtils.createPdfHtmlAddRight("ASN明细报告书", "ASN Inventory Report", "ASN号:", customer, sbHtml.toString()));
            MyPDFUtils.setsDEST(pathPdf);
            MyPDFUtils.setsHTML(pathHtml);
            MyPDFUtils.createPdf(PageSize.A3,pdfCN.toString(),pdfTitle);


            //下载操作
            FileInputStream in = new FileInputStream(new File(pathPdf));
            int len = 0;
            byte[] buffer = new byte[1024];
            String formatFileName = URLEncoder.encode("ASN明细报告书" + ".pdf", "UTF-8");
            response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");// 设定输出文件头
            response.setContentType("application/msexcel");// 定义输出类型
            OutputStream out = response.getOutputStream();
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            if (null != in) {
                in.close();
            }
            if (null != out) {
                out.close();
            }


    }
    
    /**
     * 报表查询 asn明细表 请求处理
     * @param request
     * BisEnterSteveDoring 借用属性传递asnId
     * @return
     */
    @RequestMapping(value = "reportJson", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getASNReportData(@Valid @ModelAttribute @RequestBody BisAsnInfo obj,HttpServletRequest request) {

    	Page<Stock> page = getPage(request);
    	
    	Stock stock = new Stock();
    	parameterReflect.reflectParameter(stock, request);
    	page = inStockReportService.getASNReportStocks(page, obj.getAsnId());
    	 
    	return getEasyUIData(page);
    }
}
