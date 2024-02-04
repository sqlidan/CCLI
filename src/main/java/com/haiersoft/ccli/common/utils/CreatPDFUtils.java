package com.haiersoft.ccli.common.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Entities;

public class CreatPDFUtils {
	static String filePath = PropertiesUtil.getPropertiesByName("projectpath", "application");
	/**
	 * 生成pdf html内容，
	 * @param cnTitle 中文标题
	 * @param enTitle 英文标题
	 * @param contHtml 内容块
	 * @return
	 */
	public static String createPdfHtml(String cnTitle,String enTitle,String contHtml){
		StringBuffer sbHtml=new StringBuffer();
		sbHtml.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
		sbHtml.append("<html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" /> </head><body  >");
		sbHtml.append("<style>.htd{text-align:left;font-weight:800;font-size:17px;width:150px; } .ftd{text-align:left;font-weight:800;font-size:17px;width:150px;height:20px; }#ctable td{border:0px solid #000;}</style>");
		sbHtml.append("<table style=\"width:100%;\">");
		sbHtml.append("<tr><td><img src=\""+filePath+"/static/images/tpdf1.jpg\" /><img src=\""+filePath+"/static/images/tpdf2.jpg\" /></td><td style=\"width:10px;\"></td><td style=\"width:250px;font-size:24px; font-family:宋体;text-align:center;font-weight:900;\" rowspan=\"2\">"+cnTitle+"<br/><br/>"+enTitle+"</td></tr>");
		sbHtml.append("<tr><td> QingDao Port Eimskip Coldchain Logistics Co.Ltd<br/> Weisan Road , Qianwan Port, Huangdao District<br/> Qingdao ,Shandong 266500,China<br/> Tel: +86 532 82987866   Fax: +86 532 82987995<br/></td><td></td></tr>");
		sbHtml.append("</table>");
		sbHtml.append(contHtml);
		sbHtml.append("</body></html>");
		return sbHtml.toString();
	}
	/**
	 * 生成pdf html内容
	 * @param cnTitle 中文标题
	 * @param enTitle 英文标题
	 * @param rName 右侧名称
	 * @param rCont 右侧内容
	 * @param contHtml 内容块		
	 * @return
	 */
	public static String createPdfHtmlAddRight(String cnTitle,String enTitle,String rName,String rCont,String contHtml){
		StringBuffer sbHtml=new StringBuffer();
		sbHtml.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
		sbHtml.append("<html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" /> </head><body  >");
		sbHtml.append("<style>.htd{text-align:left;font-weight:800;font-size:17px;width:150px; } .ftd{text-align:left;font-weight:800;font-size:17px;width:150px;height:20px; }#ctable td{border:0px solid #000;}</style>");
		sbHtml.append("<table style=\"width:100%;\">");
		sbHtml.append("<tr><td><img src=\""+filePath+"/static/images/tpdf1.jpg\" /><img src=\""+filePath+"/static/images/tpdf2.jpg\" /></td><td style=\"width:1px;\"></td><td style=\"width:200px;font-size:24px; font-family:宋体;text-align:center;font-weight:900;\" rowspan=\"2\">"+cnTitle+"<br/><br/>"+enTitle+"</td><td style=\"width:200px;\" colspan=\"4\"></td></tr>");
		sbHtml.append("<tr><td> QingDao Port Eimskip Coldchain Logistics Co.Ltd<br/> Weisan Road , Qianwan Port, Huangdao District<br/> Qingdao ,Shandong 266500,China<br/> Tel: +86 532 82987866   Fax: +86 532 82987995<br/></td><td></td><td style=\"font-size:20px; font-family:宋体;text-align:center;font-weight:900;\" colspan=\"2\">"+rName+"</td><td style=\"font-size:20px; font-family:宋体;text-align:center;font-weight:800;\" colspan=\"2\">"+rCont+"</td></tr>");
		sbHtml.append("</table>");
		sbHtml.append(contHtml);
		sbHtml.append("</body></html>");

		String formatHtml = formatHtml(sbHtml.toString());

		return formatHtml;
	}
	/*
	 * 后加 为解决缺少标签
	 * */
	private static String formatHtml(String html) {
		org.jsoup.nodes.Document doc = Jsoup.parse(html);
		// jsoup标准化标签，生成闭合标签
		doc.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);
		doc.outputSettings().escapeMode(Entities.EscapeMode.xhtml);
		return doc.html();
	}

	//在库报告书创建PDF
	public static String createPdfHtmlInStock(String cnTitle,String enTitle,String rName,String rCont,String contHtml){
		StringBuffer sbHtml=new StringBuffer();
		sbHtml.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
		sbHtml.append("<html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" /> </head><body  >");
		sbHtml.append("<style>.htd{text-align:left;font-weight:800;font-size:17px;width:150px; } .ftd{text-align:left;font-weight:800;font-size:17px;width:150px;height:20px; }#ctable td{border:0px solid #000;}</style>");
		sbHtml.append("<table style=\"width:100%;\">");
		sbHtml.append("<tr><td><img src=\""+filePath+"/static/images/tpdf1.jpg\" /><img src=\""+filePath+"/static/images/tpdf2.jpg\" /></td><td style=\"width:1px;\"></td><td style=\"width:170px;font-size:24px; font-family:宋体;text-align:center;font-weight:900;\" rowspan=\"2\">"+cnTitle+"<br/><br/>"+enTitle+"</td><td style=\"width:200px;\" colspan=\"7\"></td></tr>");
		sbHtml.append("<tr><td> QingDao Port Eimskip Coldchain Logistics Co.Ltd<br/> Weisan Road , Qianwan Port, Huangdao District<br/> Qingdao ,Shandong 266500,China<br/> Tel: +86 532 82987866   Fax: +86 532 82987995<br/></td><td></td><td style=\"font-size:20px; font-family:宋体;text-align:center;font-weight:900;\" colspan=\"7\">"+rName+rCont+"</td></tr>");
		sbHtml.append("</table>");
		sbHtml.append(contHtml);
		sbHtml.append("</body></html>");
		return sbHtml.toString();
	}

	//入库报告书创建PDF
	public static String createPdfHtmlEnterStock(String cnTitle,String enTitle,String rName,String rCont,String contHtml){
		StringBuffer sbHtml=new StringBuffer();
		sbHtml.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
		sbHtml.append("<html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" /> </head><body  >");
		sbHtml.append("<style>.htd{text-align:left;font-weight:800;font-size:17px;width:150px; } .ftd{text-align:left;font-weight:800;font-size:17px;width:150px;height:20px; }#ctable td{border:0px solid #000;}</style>");
		sbHtml.append("<table style=\"width:100%;\">");
		sbHtml.append("<tr><td style=\"width:50%;\" colspan=\"4\"><img src=\""+filePath+"/static/images/tpdf1.jpg\" /><img src=\""+filePath+"/static/images/tpdf2.jpg\" /></td><td style=\"width:50%;font-size:24px; font-family:宋体;text-align:center;font-weight:900;\" colspan=\"2\">"+cnTitle+"</td><td colspan=\"4\"></td></tr>");
		//TODO 怡之航黄岛冷库使用 2024-01-17 徐峥
		sbHtml.append("<tr><td style=\"width:50%;\" colspan=\"4\"> QingDao Port Eimskip Coldchain Logistics Co.Ltd<br/> Weisan Road , Qianwan Port, Huangdao District<br/> Qingdao ,Shandong 266500,China<br/> Tel: +86 532 82987866   Fax: +86 532 82987995<br/></td><td style=\"width:50%;font-size:24px; font-family:宋体;text-align:center;font-weight:900;\" colspan=\"2\">"+enTitle+"</td><td style=\"font-size:20px; font-family:宋体;text-align:center;font-weight:900;\" colspan=\"4\">"+rName+rCont+"</td></tr>");
		//TODO 即墨冷库使用 2024-01-17 徐峥
//		sbHtml.append("<tr><td style=\"width:50%;font-size:16px; font-family:宋体;\" colspan=\"4\"> 山港陆海智慧冷链物流(青岛)有限公司<br/> 山东省青岛市即墨区蓝村镇泉东村1号附4<br/> 中国山东青岛266500<br/> 电话：0532-89067231</td><td style=\"width:50%;font-size:24px; font-family:宋体;text-align:center;font-weight:900;\" colspan=\"2\">"+enTitle+"</td><td style=\"font-size:20px; font-family:宋体;text-align:center;font-weight:900;\" colspan=\"4\">"+rName+rCont+"</td></tr>");
		sbHtml.append("</table>");
		sbHtml.append(contHtml);
		sbHtml.append("</body></html>");
		return sbHtml.toString();
	}

	//出库报告书创建PDF
	public static String createPdfHtmlOutStock(String cnTitle,String enTitle,String rName,String contHtml){
		StringBuffer sbHtml=new StringBuffer();
		sbHtml.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
		sbHtml.append("<html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" /> </head><body  >");
		sbHtml.append("<style>.htd{text-align:left;font-weight:800;font-size:17px;width:150px; } .ftd{text-align:left;font-weight:800;font-size:17px;width:150px;height:20px; }#ctable td{border:0px solid #000;}</style>");
		sbHtml.append("<table style=\"width:100%;\">");
		sbHtml.append("<tr><td style=\"width:50%;\" colspan=\"4\"><img src=\""+filePath+"/static/images/tpdf1.jpg\" /><img src=\""+filePath+"/static/images/tpdf2.jpg\" /></td><td style=\"width:50%;font-size:24px; font-family:宋体;text-align:center;font-weight:900;\" colspan=\"2\">"+cnTitle+"</td><td colspan=\"4\"></td></tr>");
		//TODO 怡之航黄岛冷库使用 2024-02-04 徐峥
		sbHtml.append("<tr><td style=\"width:50%;\" colspan=\"4\"> QingDao Port Eimskip Coldchain Logistics Co.Ltd<br/> Weisan Road , Qianwan Port, Huangdao District<br/> Qingdao ,Shandong 266500,China<br/> Tel: +86 532 82987866   Fax: +86 532 82987995<br/></td><td style=\"width:50%;font-size:24px; font-family:宋体;text-align:center;font-weight:900;\" colspan=\"2\">"+enTitle+"<br/><br/> <br/></td><td style=\"font-size:20px; font-family:宋体;text-align:left;font-weight:900;\" colspan=\"4\">"+rName+"</td></tr>");
		//TODO 即墨冷库使用 2024-02-04 徐峥
//		sbHtml.append("<tr><td style=\"width:50%;font-size:16px; font-family:宋体;\" colspan=\"4\"> QingDao Port Eimskip Coldchain Logistics Co.Ltd<br/> 山东省青岛市即墨区蓝村镇泉东村1号附4<br/> Qingdao ,Shandong 266500,China<br/> Tel: +86 532 82987866   Fax: +86 532 82987995</td><td style=\"width:50%;font-size:24px; font-family:宋体;text-align:center;font-weight:900;\" colspan=\"2\">"+enTitle+"</td><td style=\"font-size:20px; font-family:宋体;text-align:center;font-weight:900;\" colspan=\"4\">"+rName+"</td></tr>");
		sbHtml.append("</table>");
		sbHtml.append(contHtml);
		sbHtml.append("</body></html>");
		return sbHtml.toString();
	}

	/**
	 * 获取项目部署路径
	 * @return
	 */
	@SuppressWarnings("unused")
	private  static String getUrlPath(){
		return filePath;
	}
}
