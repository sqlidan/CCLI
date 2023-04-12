package com.haiersoft.ccli.platform.web;

import com.haiersoft.ccli.common.utils.*;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.report.entity.Stock;
import com.haiersoft.ccli.report.service.InStockReportService;
import com.haiersoft.ccli.report.service.StockReportService;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.BisEnterStock;
import com.haiersoft.ccli.wms.entity.BisOutStock;
import com.haiersoft.ccli.wms.service.ASNService;
import com.haiersoft.ccli.wms.service.EnterStockService;
import com.haiersoft.ccli.wms.service.OutStockService;
import com.itextpdf.text.PageSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预约Controller
 *
 * @author
 * @date 2020年9月9日
 */
@Controller
@RequestMapping("platform/api/reservation")
public class ReservationController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(ReservationController.class);

	@Autowired
	private EnterStockService enterStockService;
	@Autowired
	private ASNService asnService;

	@Autowired
	private InStockReportService inStockReportService;

	@Autowired
	private StockReportService stockReportService;

	@Autowired
	private OutStockService outStockService;

	@RequestMapping(value = "reportpdf")
	@ResponseBody
	public void exportpdf(@Valid @ModelAttribute @RequestBody BisEnterStock obj, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (obj.getNtype() != null && obj.getNtype() > 0) {
			List<Map<String, Object>> getlist = enterStockService.findRepot(obj.getNtype(), obj.getSearchItemNum(), obj.getSearchCunNum(), obj.getSearchStockIn(), obj.getSearchLinkId(), obj.getSearchSKUNum(), obj.getSearchStrTime(), obj.getSearchEndTime(),obj.getIfBonded());
			String[] headCN = {"提单号", "MR/集装箱号", "SKU号", "货物描述", "入库日期", "货物状态", "件数", "总净重(KGS)", "总毛重(KGS)"};//,"存货方"
			String[] headEN = {"B/L NO.", "MR/CTN NO.", "SKU", "Description of cargo", "Inbound Date", "State of cargo", "Qty", "Net Weight", "Gross Weight"};//,"The customer"
			String pdfTitle = "入库报告书";
			StringBuffer pdfCN = new StringBuffer();
			if (1 == obj.getNtype()) {
				pdfCN.append(""
						+ "提单号"
						+ "                                               "
						+ "MR/集装箱号" + "                                     "
						+ "SKU" + "                                                    "
						+ "货物描述" + "                                                              "
						+ "入库日期" + "                    "
						+ "货物状态" + "                     "
						+ "件数" + "                            "
						+ "总净重(KGS)" + "                "
						+ "总毛重(KGS)" + "               "
				);
			}
			if (2 == obj.getNtype()) {
				pdfCN.append(""
						+ "提单号"
						+ "                                         "
						+ "MR/集装箱号" + "                               "
						+ "SKU" + "                                                  "
						+ "货物描述" + "                                                       "
						+ "入库号" + "                    "
						+ "入库日期" + "                    "
						+ "货物状态" + "                 "
						+ "件数" + "                         "
						+ "总净重(KGS)" + "           "
						+ "总毛重(KGS)" + "               "
				);
			}
			if (3 == obj.getNtype()) {
				pdfCN.append(""
						+ "提单号"
						+ "                          "
						+ "MR/集装箱号" + "                  "
						+ "SKU" + "                                       "
						+ "货物描述" + "                                  "
						+ "规格" + "                 "
						+ "项目号" + "            "
						+ "船名批名" + "          "
						+ "MSC" + "                   "
						+ "生产日期" + "          "
						+ "入库日期" + "          "
						+ "货物状态" + "         "
						+ "件数" + "         "
						+ "总净重(KGS)" + "    "
						+ "总毛重(KGS)" + "    "
						+ "订单号" + "        "
				);
			}
			String path = request.getSession().getServletContext().getRealPath("/");//获取web项目的路径"d://exceltemplate//syshtm.html";
			String pathHtml = path + "//isyshtm.html";
			String pathPdf = path + "//isyspdf.pdf";
			StringBuffer sbHtml = new StringBuffer();

			sbHtml.append("<div  style=\"height:5px;\"></div>");
			sbHtml.append("<table id=\"ctable\" style=\"border-spacing:0px;text-align:left; border-collapse:collapse;font-family:宋体;font-size:17px;width:100%\">");
			//填充标题头
			sbHtml.append("<tr style=\"height:30px; \">");
			for (String lab : headCN) {
				if ("货物描述".equals(lab)) {
					sbHtml.append("<td  width=\"300px\">").append(lab).append("</td>");
				} else if ("SKU号".equals(lab) || "MR/集装箱号".equals(lab) || "提单号".equals(lab)) {
					sbHtml.append("<td  width=\"250px\">").append(lab).append("</td>");
				} else {
					sbHtml.append("<td class=\"htd\">").append(lab).append("</td>");
				}
				if (2 == obj.getNtype() && "货物描述".equals(lab)) {
					sbHtml.append("<td class=\"htd\">").append("入库号").append("</td>");
				}
				if (3 == obj.getNtype() && "货物描述".equals(lab)) {
					sbHtml.append("<td class=\"htd\">").append("规格").append("</td>");
					sbHtml.append("<td class=\"htd\">").append("项目号").append("</td>");
					sbHtml.append("<td class=\"htd\">").append("船名批号").append("</td>");
					sbHtml.append("<td class=\"htd\">").append("MSC").append("</td>");
					sbHtml.append("<td class=\"htd\">").append("生产日期").append("</td>");
				}
			}
			if (3 == obj.getNtype()) {
				sbHtml.append("<td class=\"htd\">").append("订单号").append("</td>");
			}
			sbHtml.append("</tr>");
			sbHtml.append("<tr style=\"height:30px; \">");
			for (String lab : headEN) {
				sbHtml.append("<td class=\"htd\">").append(lab).append("</td>");
				if (2 == obj.getNtype() && "Description of cargo".equals(lab)) {
					sbHtml.append("<td class=\"htd\">").append("Inbound NO.").append("</td>");
				}
				if (3 == obj.getNtype() && "Description of cargo".equals(lab)) {
					sbHtml.append("<td class=\"htd\">").append("Standard").append("</td>");
					sbHtml.append("<td class=\"htd\">").append("Pro No.").append("</td>");
					sbHtml.append("<td class=\"htd\">").append("Lot No.").append("</td>");
					sbHtml.append("<td class=\"htd\">").append("MSC").append("</td>");
					sbHtml.append("<td class=\"htd\">").append("Pro Date").append("</td>");
				}
			}
			if (3 == obj.getNtype()) {
				sbHtml.append("<td class=\"htd\">").append("Order No").append("</td>");
			}
			sbHtml.append("</tr>");
			int nTJ = 5;
			String custNmae = null;
			//填充内容
			if (getlist != null && getlist.size() > 0) {
				String getOrderNum = null;//订单号
				Double sumPice = 0d;
				Double sumNet = 0d;
				Double sumGross = 0d;
				Map<String, Object> getMap = null;
				for (int i = 0; i < getlist.size(); i++) {
					getMap = getlist.get(i);
					if (getMap != null && getMap.size() > 0) {
						sbHtml.append("<tr>");
						sbHtml.append("<td>").append(getMap.get("BILL_NUM") != null ? getMap.get("BILL_NUM").toString() : "").append("</td>");
						sbHtml.append("<td>").append(getMap.get("CTN_NUM") != null ? getMap.get("CTN_NUM").toString() : "").append("</td>");
						sbHtml.append("<td>").append(getMap.get("SKU_ID") != null ? getMap.get("SKU_ID").toString() : "").append("</td>");
						sbHtml.append("<td>").append(getMap.get("CARGO_NAME") != null ? getMap.get("CARGO_NAME").toString().replaceAll("&", "&amp;") : "").append("</td>");
						if (2 == obj.getNtype()) {
							sbHtml.append("<td>").append(getMap.get("RK_NUM") != null ? getMap.get("RK_NUM").toString() : "").append("</td>");
							nTJ = 6;
						}
						if (3 == obj.getNtype()) {
							sbHtml.append("<td>").append(getMap.get("TYPE_SIZE") != null ? getMap.get("TYPE_SIZE").toString() : "").append("</td>");
							sbHtml.append("<td>").append(getMap.get("PRO_NUM") != null ? getMap.get("PRO_NUM").toString() : "").append("</td>");
							sbHtml.append("<td>").append(getMap.get("LOT_NUM") != null ? getMap.get("LOT_NUM").toString() : "").append("</td>");
							sbHtml.append("<td>").append(getMap.get("MSC_NUM") != null ? getMap.get("MSC_NUM").toString() : "").append("</td>");
							sbHtml.append("<td>").append(getMap.get("PRO_TIME") != null ? getMap.get("PRO_TIME").toString() : "").append("</td>");
							nTJ = 10;
						}
						sbHtml.append("<td>").append(getMap.get("INBOUND_DATE") != null ? getMap.get("INBOUND_DATE").toString() : "").append("</td>");
						sbHtml.append("<td>").append(getMap.get("ENTER_STATE") != null ? getMap.get("ENTER_STATE").toString() : "").append("</td>");
						sbHtml.append("<td>").append(new DecimalFormat("####").format(Double.parseDouble(getMap.get("RUKU_PIECE_SUM") != null ? getMap.get("RUKU_PIECE_SUM").toString() : "0"))).append("</td>");
						sbHtml.append("<td>").append(new DecimalFormat("####.00").format(Double.parseDouble(getMap.get("NET_WEIGHT_SUM") != null ? getMap.get("NET_WEIGHT_SUM").toString() : "0"))).append("</td>");
						sbHtml.append("<td>").append(new DecimalFormat("####.00").format(Double.parseDouble(getMap.get("GROSS_WEIGHT_SUM") != null ? getMap.get("GROSS_WEIGHT_SUM").toString() : "0"))).append("</td>");
						//sbHtml.append("<td>").append(getMap.get("CLIENT_NAME")!=null?getMap.get("CLIENT_NAME").toString():"").append("</td>");
						if (3 == obj.getNtype()) {
							getOrderNum = asnService.getOrderNo(getMap.get("BILL_NUM") != null ? getMap.get("BILL_NUM").toString() : "");
							sbHtml.append("<td>").append(getOrderNum != null ? getOrderNum : "").append("</td>");
						}
						sbHtml.append("</tr>");
						sumPice += Double.valueOf(getMap.get("RUKU_PIECE_SUM") != null ? getMap.get("RUKU_PIECE_SUM").toString() : "0");
						sumNet += Double.valueOf(getMap.get("NET_WEIGHT_SUM") != null ? getMap.get("NET_WEIGHT_SUM").toString() : "0");
						sumGross += Double.valueOf(getMap.get("GROSS_WEIGHT_SUM") != null ? getMap.get("GROSS_WEIGHT_SUM").toString() : "0");
						if (custNmae == null) {
							custNmae = getMap.get("CLIENT_NAME") != null ? getMap.get("CLIENT_NAME").toString() : "";
						}
					}
				}//end for

				//添加合计
				sbHtml.append("<tr><td class=\"ftd\" colspan=\"" + nTJ + "\" style=\"border:0px;height:30px; \"></td>");
				sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">合计：</td>");
				sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####").format(sumPice)).append("</td>");
				sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####.00").format(sumNet)).append("</td>");
				sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####.00").format(sumGross)).append("</td>");
				sbHtml.append("</tr>");
			}
			sbHtml.append("</table>");
			sbHtml.append("<table style=\"border-spacing:0px;text-align:center; border-collapse:collapse;font-family:宋体;font-size:12px;width:100%\"><tr>");
			//User user = UserUtil.getCurrentUser();
			sbHtml.append("<td style=\"text-align:right;margin-top:10px;\">").append("操作人员：").append(" PrintTime : &nbsp;").append(DateUtils.getDateTime()).append("</td>");
			sbHtml.append("</tr></table>");
			MyFileUtils html = new MyFileUtils();
			html.setFilePath(pathHtml);
			html.saveStrToFile(CreatPDFUtils.createPdfHtmlEnterStock("入库报告书", "Inbound Report（IN）", "The Customer：", custNmae, sbHtml.toString()));
			MyPDFUtils.setsDEST(pathPdf);
			MyPDFUtils.setsHTML(pathHtml);
			MyPDFUtils.createPdf(PageSize.A3, pdfCN.toString(), pdfTitle);


			//下载操作
			FileInputStream in = new FileInputStream(new File(pathPdf));
			int len = 0;
			byte[] buffer = new byte[1024];
			String formatFileName = URLEncoder.encode("入库报告单" + ".pdf", "UTF-8");
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
	}






	/**
	 * @param stock
	 * @param request
	 * @param response
	 * @throws Exception
	 * @throws
	 * @Description: 导出 在库报告书 PDF
	 */
	@RequestMapping("exportInStockPDF")
	@ResponseBody
	public void exportInStockPDF(@Valid @ModelAttribute @RequestBody Stock stock, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (stock.getReportType() != null && !"".equals(stock.getReportType())) {
			List<Map<String, Object>> getlist = null;
			String[] headCN = {"提单号", "MR/集装箱号", "SKU", "货物描述", "小类", "货物状态", "入库日期", "件数", "单净重(KGS)", "单毛重(KGS)", "总净重(KGS)", "总毛重(KGS)"};
			String[] headEN = {"B/L NO.", "MR/CTN NO.", "SKU", "Description of cargo", "Sub Class", "State of cargo", "Inbound Date", "Qty", "Net Single", "Gross Single", "Net Weight", "Gross Weight"};
			String pdfTitle = "在库报告书";
			StringBuffer pdfCN = new StringBuffer();
			if ("1".equals(stock.getReportType())) {
				pdfCN.append("提单号"
						+ "                 "
						+ "MR/集装箱号" + "                             "
						+ "SKU" + "                                    "
						+ "货物描述" + "                                    "
						+ "小类" + "                    "
						+ "货物状态" + "                 "
						+ "区位" + "               "
						+ "入库日期" + "           "
						+ "件数" + "              "
						+ "单净重(KGS)" + "      "
						+ "单毛重(KGS)" + "        "
						+ "总净重(KGS)" + "      "
						+ "总毛重(KGS)" + "           "
				);
			}
			if ("2".equals(stock.getReportType())) {
				pdfCN.append("提单号"
						+ "            "
						+ "MR/集装箱号" + "                         "
						+ "SKU" + "                                     "
						+ "入库号" + "          "
						+ "货物描述" + "                                  "
						+ "小类" + "                   "
						+ "货物状态" + "                "
						+ "区位" + "                "
						+ "入库日期" + "          "
						+ "件数" + "           "
						+ "单净重(KGS)" + "    "
						+ "单毛重(KGS)" + "        "
						+ "总净重(KGS)" + "    "
						+ "总毛重(KGS)" + "        "
				);
			}
			if ("3".equals(stock.getReportType())) {
				pdfCN.append("提单号"
						+ "       "
						+ "MR/集装箱号" + "            "
						+ "SKU" + "                             "
						+ "货物描述" + "                      "
						+ "小类" + "           "
						+ "货物状态" + "          "
						+ "区位" + "        "
						+ "入库日期" + "     "
						+ "件数" + "     "
						+ "单净重(KGS)" + " "
						+ "单毛重(KGS)" + " "
						+ "总净重(KGS)" + " "
						+ "总毛重(KGS)" + " "
						+ "生产日期" + "          "
						+ "规格" + "             "
						+ "项目号" + "          "
						+ "船名批名" + "        "
						+ "MSC" + "        "
						+ "ORDER号" + "        "
				);
			}
			// String pdfCN = "提单号"+"                 "+ "MR/集装箱号"+"                       "+"SKU"+"                                           "+"货物描述"+"                                                            "+"小类"+"                          "+"货物状态"+"                    "+"入库日期"+"                          "+"件数"+"                                  "+"总净重(KGS)"+"                         "+"总毛重(KGS)"+"               ";
			//获取web项目的路径"d://exceltemplate//syshtm.html";
			String path = request.getSession().getServletContext().getRealPath("/");
			//  String path="D:/apache-tomcat-8.5.53-windows-x64/apache-tomcat-8.5.53/webapps/ccli";
			String pathHtml = path + "insyshtm.html";
			String pathPdf = path + "insyspdf.pdf";
			StringBuffer sbHtml = new StringBuffer();

			sbHtml.append("<div  style=\"height:5px;\"></div>");
			sbHtml.append("<table id=\"ctable\" style=\"border-spacing:0px;margin:none; text-align:left; border-collapse:collapse;font-family:宋体;font-size:17px;width:100%\">");
			//if ("1".equals(stock.getReportType())) {
			getlist = inStockReportService.searchInStockReport(stock);
			//}
            /*if ("2".equals(stock.getReportType())) {
                getlist = inStockReportService.searchInStockReportRB(stock);
            }
            if ("3".equals(stock.getReportType())) {
                getlist = inStockReportService.searchInStockReportOTE(stock);
            }*/
			String customer = null;
			//填充标题头
			sbHtml.append("<tr style=\"height:30px; \">");
			//  String    lab= URLDecoder.decode(headCN,"UTF-8");
			for ( String lab : headCN) {
				System.out.println("lab"+lab);
				if ("货物描述".equals(lab)) {
					sbHtml.append("<td  width=\"300px\">").append(lab).append("</td>");
				} else if ("MR/集装箱号".equals(lab) || "SKU".equals(lab)) {
					sbHtml.append("<td  width=\"250px\">").append(lab).append("</td>");
				} else {
					sbHtml.append("<td class=\"htd\">").append(lab).append("</td>");
				}
				if ("2".equals(stock.getReportType()) && "SKU".equals(lab)) {
					sbHtml.append("<td class=\"htd\">").append("入库号").append("</td>");
				}
				if ("1".equals(stock.getLocationType()) && "货物状态".equals(lab)) {//有库位
					sbHtml.append("<td class=\"htd\">").append("区位").append("</td>");
				}
			}
			if ("3".equals(stock.getReportType())) {
				sbHtml.append("<td class=\"htd\">").append("生产日期").append("</td>");
				sbHtml.append("<td class=\"htd\">").append("规格").append("</td>");
				sbHtml.append("<td class=\"htd\">").append("项目号").append("</td>");
				sbHtml.append("<td class=\"htd\">").append("船名批号").append("</td>");
				sbHtml.append("<td class=\"htd\">").append("MSC").append("</td>");
				sbHtml.append("<td class=\"htd\">").append("ORDER号").append("</td>");
			}
			sbHtml.append("</tr>");
			sbHtml.append("<tr style=\"height:30px; \">");
			for (String lab : headEN) {
				sbHtml.append("<td class=\"htd\">").append(lab).append("</td>");
				if ("2".equals(stock.getReportType()) && "SKU".equals(lab)) {
					sbHtml.append("<td class=\"htd\">").append("Inbound NO.").append("</td>");
				}
				if ("1".equals(stock.getLocationType()) && "State of cargo".equals(lab)) {//有库位
					sbHtml.append("<td class=\"htd\">").append("Cargo Location").append("</td>");
				}
			}
			if ("3".equals(stock.getReportType())) {
				sbHtml.append("<td class=\"htd\">").append("Produce Date").append("</td>");
				sbHtml.append("<td class=\"htd\">").append("Standard").append("</td>");
				sbHtml.append("<td class=\"htd\">").append("Pro No.").append("</td>");
				sbHtml.append("<td class=\"htd\">").append("Lot No.").append("</td>");
				sbHtml.append("<td class=\"htd\">").append("MSC").append("</td>");
				sbHtml.append("<td class=\"htd\">").append("ORDERNUM").append("</td>");
			}
			sbHtml.append("</tr>");
			int nTJ = 7;
			//填充内容
			if (getlist != null && getlist.size() > 0) {
				Double sumPice = 0d;
				Double sumNet = 0d;
				Double sumGross = 0d;
				Double xjPice = 0d;
				Double xjNet = 0d;
				Double xjGross = 0d;
				String getBillCode = null;
				Map<String, Object> getMap = null;
				for (int i = 0; i < getlist.size(); i++) {
					getMap = getlist.get(i);
					if (null == customer) {
						customer = getMap.get("CLIENT_NAME").toString();
					}
					//添加小计
					if (getBillCode != null && !getBillCode.equals(getMap.get("BILL_NUM") != null ? getMap.get("BILL_NUM").toString() : "")) {
						sbHtml.append("<tr><td class=\"ftd\"  style=\"border:0px;height:30px; \">合计/Total：</td>");
						sbHtml.append("<td class=\"ftd\" colspan=\"" + ("1".equals(stock.getReportType()) ? ("1".equals(stock.getLocationType()) ? 7 : 6) : nTJ) + "\" style=\"border:0px; \"></td>");
						sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####").format(xjPice)).append("</td>");
						sbHtml.append("<td class=\"ftd\" style=\"border:0px; \"></td> ");
						sbHtml.append("<td class=\"ftd\" style=\"border:0px; \"></td> ");
						sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####.00").format(xjNet)).append("</td>");
						sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####.00").format(xjGross)).append("</td>");
						sbHtml.append("</tr>");
						xjPice = 0d;
						xjNet = 0d;
						xjGross = 0d;
					}
					getBillCode = getMap.get("BILL_NUM") != null ? getMap.get("BILL_NUM").toString() : "";
					if (getMap != null && getMap.size() > 0) {
						sbHtml.append("<tr>");

						sbHtml.append("<td>").append(getMap.get("BILL_NUM") != null ? getMap.get("BILL_NUM").toString() : "").append("</td>");
						sbHtml.append("<td>").append(getMap.get("CTN_NUM") != null ? getMap.get("CTN_NUM").toString() : "").append("</td>");
						sbHtml.append("<td>").append(getMap.get("SKU_ID") != null ? getMap.get("SKU_ID").toString() : "").append("</td>");
						if ("2".equals(stock.getReportType())) {
							sbHtml.append("<td>").append(getMap.get("RK_NUM") != null ? getMap.get("RK_NUM").toString() : "").append("</td>");
							nTJ = 8;
						}
						sbHtml.append("<td >").append(getMap.get("CARGO_NAME") != null ? getMap.get("CARGO_NAME").toString().replaceAll("<", "《").replaceAll(">", "》").replaceAll("&", "&amp;") : "").append("</td>");
						sbHtml.append("<td>").append(getMap.get("CLASS_NAME") != null ? getMap.get("CLASS_NAME").toString() : "").append("</td>");

						sbHtml.append("<td>").append(getMap.get("ENTER_STATE") != null ? getMap.get("ENTER_STATE").toString() : "").append("</td>");
						if ("1".equals(stock.getLocationType())) {//有库位
							sbHtml.append("<td>").append(getMap.get("CARGO_LOCATION") != null ? getMap.get("CARGO_LOCATION").toString() : "").append("</td>");
						}
						sbHtml.append("<td>").append(getMap.get("INBOUND_DATE") != null ? getMap.get("INBOUND_DATE") : "").append("</td>");
						sbHtml.append("<td>").append(new DecimalFormat("####").format(Double.valueOf(getMap.get("NOW_PIECE_SUM") != null ? getMap.get("NOW_PIECE_SUM").toString() : "0"))).append("</td>");
						sbHtml.append("<td>").append(new DecimalFormat("####.00").format(Double.valueOf(getMap.get("NET_SINGLE") != null ? getMap.get("NET_SINGLE").toString() : "0"))).append("</td>");
						sbHtml.append("<td>").append(new DecimalFormat("####.00").format(Double.valueOf(getMap.get("GROSS_SINGLE") != null ? getMap.get("GROSS_SINGLE").toString() : "0"))).append("</td>");
						sbHtml.append("<td>").append(new DecimalFormat("####.00").format(Double.valueOf(getMap.get("NET_WEIGHT_SUM") != null ? getMap.get("NET_WEIGHT_SUM").toString() : "0"))).append("</td>");
						sbHtml.append("<td>").append(new DecimalFormat("####.00").format(Double.valueOf(getMap.get("GROSS_WEIGHT_SUM") != null ? getMap.get("GROSS_WEIGHT_SUM").toString() : "0"))).append("</td>");
						if ("3".equals(stock.getReportType())) {
							sbHtml.append("<td>").append(getMap.get("PRO_TIME") != null ? getMap.get("PRO_TIME").toString() : "").append("</td>");
							sbHtml.append("<td>").append(getMap.get("TYPE_SIZE") != null ? getMap.get("TYPE_SIZE").toString() : "").append("</td>");
							sbHtml.append("<td>").append(getMap.get("PRO_NUM") != null ? getMap.get("PRO_NUM").toString() : "").append("</td>");
							sbHtml.append("<td>").append(getMap.get("LOT_NUM") != null ? getMap.get("LOT_NUM").toString() : "").append("</td>");
							sbHtml.append("<td>").append(getMap.get("MSC_NUM") != null ? getMap.get("MSC_NUM").toString() : "").append("</td>");
							sbHtml.append("<td>").append(getMap.get("ORDER_NUM") != null ? getMap.get("ORDER_NUM").toString() : "").append("</td>");
							nTJ = 8;//nTJ=7->8
						}
						if (!"1".equals(stock.getLocationType())) {//no库位
							nTJ = nTJ - 1;
						}
						sbHtml.append("</tr>");
						sumPice += Double.valueOf(getMap.get("NOW_PIECE_SUM") != null ? getMap.get("NOW_PIECE_SUM").toString() : "0");
						sumNet += Double.valueOf(getMap.get("NET_WEIGHT_SUM") != null ? getMap.get("NET_WEIGHT_SUM").toString() : "0");
						sumGross += Double.valueOf(getMap.get("GROSS_WEIGHT_SUM") != null ? getMap.get("GROSS_WEIGHT_SUM").toString() : "0");
						xjPice += Double.valueOf(getMap.get("NOW_PIECE_SUM") != null ? getMap.get("NOW_PIECE_SUM").toString() : "0");
						xjNet += Double.valueOf(getMap.get("NET_WEIGHT_SUM") != null ? getMap.get("NET_WEIGHT_SUM").toString() : "0");
						xjGross += Double.valueOf(getMap.get("GROSS_WEIGHT_SUM") != null ? getMap.get("GROSS_WEIGHT_SUM").toString() : "0");
					}
					//最后一条添加合计
					if (i == getlist.size() - 1) {
						sbHtml.append("<tr><td class=\"ftd\"  style=\"border:0px;height:30px; \">合计/Total：</td>");
						sbHtml.append("<td class=\"ftd\" colspan=\"" + ("1".equals(stock.getReportType()) ? ("1".equals(stock.getLocationType()) ? 7 : 6) : nTJ) + "\" style=\"border:0px; \"></td>");
						sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####").format(xjPice)).append("</td>");
						sbHtml.append("<td class=\"ftd\" style=\"border:0px; \"></td> ");
						sbHtml.append("<td class=\"ftd\" style=\"border:0px; \"></td> ");
						sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####.00").format(xjNet)).append("</td>");
						sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####.00").format(xjGross)).append("</td>");
						sbHtml.append("</tr>");
					}
				}//end for

				//添加合计
				sbHtml.append("<tr><td class=\"ftd\" style=\"border:0px; \">合计/Total：</td>");
				sbHtml.append("<td class=\"ftd\" colspan=\"" + ("1".equals(stock.getReportType()) ? ("1".equals(stock.getLocationType()) ? 7 : 6) : nTJ) + "\" style=\"border:0px;height:30px; \"></td>");
				sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####").format(sumPice)).append("</td>");
				sbHtml.append("<td class=\"ftd\" style=\"border:0px; \"></td> ");
				sbHtml.append("<td class=\"ftd\" style=\"border:0px; \"></td> ");
				sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####.00").format(sumNet)).append("</td>");
				sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####.00").format(sumGross)).append("</td>");
				sbHtml.append("</tr>");
			}
			sbHtml.append("</table>");

			sbHtml.append("<table style=\"border-spacing:0px;text-align:center; border-collapse:collapse;font-family:宋体;font-size:12px;width:100%\"><tr>");
			@SuppressWarnings("unused")
			User user = UserUtil.getCurrentUser();
			sbHtml.append("<td style=\"text-align:right;margin-top:5px;\">").append(" PrintTime : &nbsp;").append(DateUtils.getDateTime()).append("</td>");
			sbHtml.append("</tr></table>");

			MyFileUtils html = new MyFileUtils();
			System.out.println("pathHtml"+pathHtml);
			html.setFilePath(pathHtml);

			html.saveStrToFile(CreatPDFUtils.createPdfHtmlInStock("在库报告书", "Inventory Report", "The Customer:", customer, sbHtml.toString()));
			MyPDFUtils.setsDEST(pathPdf);
			MyPDFUtils.setsHTML(pathHtml);
			MyPDFUtils.createPdf(PageSize.A3, pdfCN.toString(), pdfTitle);


			//下载操作
			FileInputStream in = new FileInputStream(new File(pathPdf));
			int len = 0;
			byte[] buffer = new byte[1024];
			String formatFileName = URLEncoder.encode("在库报告书" + ".pdf", "UTF-8");
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


	}



	/**
	 * 入库报告书 接口信息（海路通系统）
	 * @param itemNum  提单号
	 * @param ctnNum  箱号
	 * @param realClientName  客户名称
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "enterReportInfo")
	@ResponseBody
	public Map<String, Object> enterReportInformation(String itemNum, String ctnNum, String realClientName) throws Exception {
		Map infoMap = new HashMap();
		if (StringUtils.isEmpty(itemNum) && StringUtils.isEmpty(ctnNum) && StringUtils.isEmpty(realClientName)) {
			infoMap.put("message","提单号，箱号和客户名称不能都为空");
			return infoMap;
		} else {
			List<Map<String, Object>> getList  = enterStockService.enterReportInformation( itemNum, ctnNum, realClientName);
			infoMap.put("data",getList);
			return infoMap;
		}
	}

	/**
	 * @param itemNum  提单号
	 * @param ctnNum  箱号
	 * @param realClientName  客户名称
	 * @throws Exception
	 * @throws
	 * @Description: 在库报告书 接口信息（海路通系统）
	 */
	@RequestMapping("inStockReportInfo")
	@ResponseBody
	public Map<String, Object> inStockReportInfo(String itemNum, String ctnNum, String realClientName) throws Exception {
		Map infoMap = new HashMap();
		if (StringUtils.isEmpty(itemNum) && StringUtils.isEmpty(ctnNum) && StringUtils.isEmpty(realClientName)) {
			infoMap.put("message","提单号，箱号和客户名称不能都为空");
			return infoMap;
		} else {
			List<Map<String, Object>> getList  = inStockReportService.inStockReportInfo( itemNum, ctnNum, realClientName);
			infoMap.put("data",getList);
			return infoMap;
		}
	}



	/**
	 * @param itemNum  提单号
	 * @param ctnNum  箱号
	 * @param realClientName  客户名称
	 * @throws Exception
	 * @throws
	 * @Description: 在库明细  接口信息（海路通系统）
	 */
	@RequestMapping(value = "inStockDetailReportInfo")
	@ResponseBody
	public Map<String, Object> inStockDetailReportInfo(String itemNum, String ctnNum, String realClientName) throws Exception {
		Map infoMap = new HashMap();
		if (StringUtils.isEmpty(itemNum) && StringUtils.isEmpty(ctnNum) && StringUtils.isEmpty(realClientName)) {
			infoMap.put("message","提单号，箱号和客户名称不能都为空");
			return infoMap;
		} else {
			List<Map<String, Object>> getList  = stockReportService.inStockDetailReportInfo( itemNum, ctnNum, realClientName);
			infoMap.put("data",getList);
			return infoMap;
		}
	}


	/**
	 * @param itemNum  提单号
	 * @param ctnNum  箱号
	 * @param realClientName  客户名称
	 * @throws Exception
	 * @throws
	 * @Description: 出库报告书  接口信息（海路通系统）
	 */
	@RequestMapping(value = "outStockReportInfo")
	@ResponseBody
	public Map<String, Object> outStockReportInfo(String itemNum, String ctnNum, String realClientName) throws Exception {

		Map infoMap = new HashMap();
		if (StringUtils.isEmpty(itemNum) && StringUtils.isEmpty(ctnNum) && StringUtils.isEmpty(realClientName)) {
			infoMap.put("message","提单号，箱号和客户名称不能都为空");
			return infoMap;
		} else {
			List<Map<String, Object>> getList  = outStockService.outStockReportInfo( itemNum, ctnNum, realClientName);
			infoMap.put("data",getList);
			return infoMap;
		}

	}
}
