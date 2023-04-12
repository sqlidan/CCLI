package com.haiersoft.ccli.common.utils;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringEscapeUtils;
import com.haiersoft.ccli.system.utils.UserUtil;
/**
 * 字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 *
 * @author ThinkGem
 * @version 2013-05-22
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

	public static String lowerFirst(String str) {
		if (StringUtils.isBlank(str)) {
			return "";
		} else {
			return str.substring(0, 1).toLowerCase() + str.substring(1);
		}
	}

	/**
	 * 财务接口对接数据整合
	 * 
	 * @param srcExchangeRate
	 * @return
	 */
	public static Map<String, Object> getMap(String codeNum, String customID, String custom, String label, String rate,
			String feeCode, String feeName, String rmb, String operator, String jsfs) {
		Map<String, Object> uploadMap = new HashMap<String, Object>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		uploadMap.put("srcSystem", "YW-711");// 来源系统
		uploadMap.put("srcFeeListId", codeNum);
		uploadMap.put("srcBizDocId", codeNum);
		uploadMap.put("srcBizDocNo", codeNum);
		uploadMap.put("srcCustID", customID);
		uploadMap.put("srcCustName", custom);
		uploadMap.put("srcBizType", "YW-711");
		uploadMap.put("srcBizTypeName", "青岛港怡之航冷链物流有限公司");
		uploadMap.put("srcDeptCode", "QDG065001");// 源业务部门编码
		uploadMap.put("srcDeptName", "财务部（怡之航）");// 源业务部门名称
		uploadMap.put("srcCurrency", label);// 源币种
		// uploadMap.put("srcSettlType",jsfs);//结算方式
		uploadMap.put("srcExchangeRate", Double.valueOf(rate));// 源汇率
		uploadMap.put("srcFeeItemCode", feeCode);// 源费目代码
		uploadMap.put("srcFeeItemName", feeName);// 源费目名称
		uploadMap.put("srcQty", Integer.valueOf(1));// 源数量
		uploadMap.put("srcUnit", "票");// 源开票单位
		uploadMap.put("srcPrice", Double.valueOf(rmb));// 单价1票时候就是总金额数量*单价
		uploadMap.put("srcArAmt", Double.valueOf(rmb));// 源应收金额
		// uploadMap.put("srcBizAmt",BigDecimalUtil.getAmt(rmb));//源交易金额
		uploadMap.put("srcSettlAmt", Double.valueOf(rmb));// 源结算金额
		uploadMap.put("srcFeeCollID", UserUtil.getCurrentUser() != null ? UserUtil.getCurrentUser().getName() : "");// 计费人
		uploadMap.put("srcCompanyCode", "QDG065");// 公司编码
		uploadMap.put("srcCompanyName", "青岛港怡之航冷链物流有限公司");// 公司名
		uploadMap.put("srcTaxRate", rate);// 源税率
		uploadMap.put("srcTNIAmt", BigDecimalUtil.getAmt(rmb));// 源不含税金额
		uploadMap.put("srcTax", BigDecimalUtil.getRate(BigDecimalUtil.getAmt(rmb)));// 源税额
		uploadMap.put("srcVatAmt", rmb);// 源含税金额
		uploadMap.put("srcFeeDate", format.format(new Date()));// 计费时间
		return uploadMap;
	}
	
	/**
	 * 明细整合
	 * 
	 * @param srcExchangeRate
	 * @return
	 */
	public static Map<String, Object> getEntryMap(String codeNum, String customID, String custom, String label, String rate,
			String feeCode, String feeName, String rmb, String operator, String jsfs) {
		Map<String, Object> uploadMap = new HashMap<String, Object>();
		uploadMap.put("produceNumber", feeCode);// 源费目代码
		uploadMap.put("produceName", feeName);// 源费目名称
		uploadMap.put("specs","");
		uploadMap.put("noTaxPrice",BigDecimalUtil.getAmt(rmb));
		uploadMap.put("measureUnit","票");
		uploadMap.put("price", Double.valueOf(rmb));//单价1票时候就是总金额数量*单价
		uploadMap.put("quantity", Integer.valueOf(1));//源数量
		uploadMap.put("noTaxAmt",BigDecimalUtil.getAmt(rmb));//不含税金额
		uploadMap.put("taxRate",0.06d);
		uploadMap.put("taxAmt",BigDecimalUtil.getRate(BigDecimalUtil.getAmt(rmb)));//税额
		uploadMap.put("priceTax",Double.valueOf(rmb));//价税合计
		uploadMap.put("discountRate",0d);
		uploadMap.put("discountAmt",0d);
		uploadMap.put("taxClassificationCode","");
		uploadMap.put("billRowType","0");
		uploadMap.put("taxClassName","");
		uploadMap.put("uuid",codeNum);
		return uploadMap;
	}
	/**
	 * 财务接口对接开票数据整合
	 * 
	 * @param srcExchangeRate
	 * @return
	 */
	public static Map<String, Object> getOpenMap(String codeNum,String drawer,String payer,String checker,String invoiceType, String currentAccountType, String custom, String currencynum,
			Double amtToble,String remark,String taxType) {
		Map<String, Object> uploadMap = new HashMap<String, Object>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		uploadMap.put("applicateDate",format.format(new Date()));//系统日期
		uploadMap.put("applicantNo",drawer);//申请人
		uploadMap.put("invoiceType",invoiceType);//发票类型
		uploadMap.put("currentAccountType",currentAccountType);//外来客户要求
		uploadMap.put("receiveCompanyName", custom);//收票公司名
		uploadMap.put("isContainTax","1");//单价是否含税
		uploadMap.put("drawerNo",drawer);//开票人
		uploadMap.put("payeeNo",payer);//收款人
		uploadMap.put("checkerNo",checker);//复核人
		uploadMap.put("currencynum",currencynum);//币种
		uploadMap.put("isList","0");//清单
		uploadMap.put("noTaxAmtTotal",null);//不含税金额总额
		uploadMap.put("taxAmtTotal",null);//税额总额
		uploadMap.put("priceTaxTotal",amtToble);//价税合计总额
		uploadMap.put("currAcctCompanynum","");
		uploadMap.put("currAcctSuppliernum","");
		uploadMap.put("currAcctCustomernum","");
		uploadMap.put("isPrint","0");//是否打印
		uploadMap.put("makeInvoiceOrgnum","QDG065001");//财务编码
		uploadMap.put("companyNum","QDG065");//公司编码
		uploadMap.put("goldTaxNum",taxType);//税盘号
		uploadMap.put("bizDate",format.format(new Date()));//税盘号
		uploadMap.put("uniqueId",codeNum);//源单号
		uploadMap.put("remark",remark);//备注
		return uploadMap;
	}

	public static String upperFirst(String str) {
		if (StringUtils.isBlank(str)) {
			return "";
		} else {
			return str.substring(0, 1).toUpperCase() + str.substring(1);
		}
	}

	/**
	 * 替换掉HTML标签方法
	 */
	public static String replaceHtml(String html) {
		if (isBlank(html)) {
			return "";
		}
		String regEx = "<.+?>";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		String s = m.replaceAll("");
		return s;
	}

	/**
	 * 缩略字符串（不区分中英文字符）
	 *
	 * @param str
	 *            目标字符串
	 * @param length
	 *            截取长度
	 * @return
	 */
	public static String abbr(String str, int length) {
		if (str == null) {
			return "";
		}
		try {
			StringBuilder sb = new StringBuilder();
			int currentLength = 0;
			for (char c : replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray()) {
				currentLength += String.valueOf(c).getBytes("GBK").length;
				if (currentLength <= length - 3) {
					sb.append(c);
				} else {
					sb.append("...");
					break;
				}
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 缩略字符串（替换html）
	 *
	 * @param str
	 *            目标字符串
	 * @param length
	 *            截取长度
	 * @return
	 */
	public static String rabbr(String str, int length) {
		return abbr(replaceHtml(str), length);
	}

	/**
	 * 转换为Double类型
	 */
	public static Double toDouble(Object val) {
		if (val == null) {
			return 0D;
		}
		try {
			return Double.valueOf(trim(val.toString()));
		} catch (Exception e) {
			return 0D;
		}
	}

	/**
	 * 转换为Float类型
	 */
	public static Float toFloat(Object val) {
		return toDouble(val).floatValue();
	}

	/**
	 * 转换为Long类型
	 */
	public static Long toLong(Object val) {
		return toDouble(val).longValue();
	}

	/**
	 * 转换为Integer类型
	 */
	public static Integer toInteger(Object val) {
		return toLong(val).intValue();
	}

	/**
	 * @param strIn
	 * @return
	 * @throws @author
	 *             Connor.M
	 * @Description: 判断是否空 true为空，false不空
	 * @date 2016年2月25日 上午11:38:58
	 */
	public static boolean isNull(String strIn) {
		return null == strIn || ("").equals(strIn.trim()) || ("NULL").equals(strIn.toUpperCase());
	}

	public static boolean nonNull(String strIn) {
		return !isNull(strIn);
	}

	/**
	 * 补齐不足长度
	 *
	 * @param length
	 *            长度
	 * @param number
	 *            数字
	 * @return
	 */
	public static String lpadInt(int length, int number) {
		String f = "%0" + length + "d";
		f = String.format(f, number);
		if (length < f.length()) {
			f = f.substring(f.length() - length);
		}
		return f;
	}

	/**
	 * @param length
	 * @param str
	 * @return
	 * @throws @author
	 *             Connor.M
	 * @Description: 字符转向左补零
	 * @date 2016年2月25日 下午12:18:33
	 */
	public static String lpadStringLeft(int length, String str) {
		int strLen = str.length();
		if (strLen < length) {
			while (strLen < length) {
				StringBuffer sb = new StringBuffer();
				sb.append("0").append(str);// 左补0
				str = sb.toString();
				strLen = str.length();
			}
		}
		return str;
	}

	/**
	 * @param length
	 * @param str
	 * @return
	 * @throws @author
	 *             Connor.M
	 * @Description: 字符转向右补零
	 * @date 2016年2月25日 下午12:18:33
	 */
	public static String lpadStringRight(int length, String str) {
		int strLen = str.length();
		if (strLen < length) {
			while (strLen < length) {
				StringBuffer sb = new StringBuffer();
				sb.append(str).append("0");// 右补0
				str = sb.toString();
				strLen = str.length();
			}
		}
		return str;
	}

	/**
	 * @param ascii
	 * @return
	 * @throws @author
	 *             Connor.M
	 * @Description: unicode转换为中文
	 * @date 2016年2月27日 上午11:05:48
	 */
	public static String ascii2native(String ascii) {
		int n = ascii.length() / 6;
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0, j = 2; i < n; i++, j += 6) {
			String code = ascii.substring(j, j + 4);
			char ch = (char) Integer.parseInt(code, 16);
			sb.append(ch);
		}
		return sb.toString();
	}

	/**
	 * @return
	 * @throws @author
	 *             pyl
	 * @Description: 获取当前时间的string类型:yymmddhhmmss
	 * @date 2016年2月27日 上午11:05:48 yy
	 */
	public static String timeToString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");// 可以方便地修改日期格式
		return dateFormat.format(new Date());
	}

	/**
	 * 获取编号的处理
	 * 
	 * @return
	 */
	public static String noToString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");// 可以方便地修改日期格式
		return dateFormat.format(new Date());
	}

	public static String dateToString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");// 可以方便地修改日期格式
		return dateFormat.format(new Date());
	}

	public static String mouthToString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");// 可以方便地修改日期格式
		return dateFormat.format(new Date());
	}

	/**
	 * @return
	 * @throws @author
	 *             pyl
	 * @Description: 费用编号格式：一共16位；“LK”+8位年月日（YYYYMMDD）+后6位台账Id（不足6位前补0）
	 * @date 2016年5月13日
	 */
	public static String numToCode(String num, Date date) {
		String code = "";
		if (num.length() < 6) {
			code = "LK" + DateUtils.formatDate(date, "yyyyMMdd") + lpadStringLeft(6, num);
		} else {
			code = num.substring(num.length() - 6, num.length());
			code = "LK" + DateUtils.formatDate(date, "yyyyMMdd") + code;
		}
		return code;
	}

	public static String strToUTF8(String s) {
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String combineToPath(String... s) {
		StringBuffer path = new StringBuffer();
		for (String tmp : s) {
			path.append(File.separator).append(tmp);
		}
		return path.toString();
	}
}
