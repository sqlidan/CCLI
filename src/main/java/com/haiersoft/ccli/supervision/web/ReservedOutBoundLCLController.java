package com.haiersoft.ccli.supervision.web;

import com.haiersoft.ccli.base.service.HscodeService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.platform.entity.PlatformReservationOutbound;
import com.haiersoft.ccli.platform.service.ReservationOutboundService;
import com.haiersoft.ccli.supervision.service.*;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.helpers.AttributesImpl;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.xml.rpc.ServiceException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 分类监管 申请单controller
 * 
 * @author
 *
 */

@Controller
@RequestMapping("supervision/Lcl")
public class ReservedOutBoundLCLController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(ReservedOutBoundLCLController.class);

	@Autowired
	private ReservationOutboundService reservationOutboundService;

	@Autowired
	private ApprInfoService apprInfoService;

	@Autowired
	HscodeService hscodeService;
	@Autowired
	GetKeyService getKeyService;
	@Autowired
	FljgWsClient fljgWsClient;

	/**
	 * 默认页面
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "fljg/reservedOutBound";
	}

	@RequestMapping(value = "/selectList",method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> outboundList(HttpServletRequest request){
		Page<PlatformReservationOutbound> page=getPage(request);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		boolean appointDateHave = false;
		for (PropertyFilter propertyFilter : filters) {

			if (propertyFilter.getPropertyName().equals("appointDate")) {
				appointDateHave = true;
				break;
			}
		}
		//筛选条件没有日期  默认为今天
		if (!appointDateHave) {
			Calendar start = Calendar.getInstance();
			//结束时间
			Calendar end = Calendar.getInstance();
			start.add(Calendar.DATE, 0);
			// 时
			start.set(Calendar.HOUR_OF_DAY, 0);
			// 分
			start.set(Calendar.MINUTE, 0);
			// 秒
			start.set(Calendar.SECOND, 0);

			start.set(Calendar.MILLISECOND, 0);


			end.add(Calendar.DATE, 1);
			// 时
			end.set(Calendar.HOUR_OF_DAY, 0);
			// 分
			end.set(Calendar.MINUTE, 0);
			// 秒
			end.set(Calendar.SECOND, 0);
			end.set(Calendar.MILLISECOND, 0);

			filters.add(new PropertyFilter("GED_appointDate", start.getTime()));
			filters.add(new PropertyFilter("LTD_appointDate", end.getTime()));
		}

		//page.orderBy("appointDate").order("desc");
		// page.orderBy("status,appointDate").order("asc,desc");
		//page.orderBy("status,appointDate,queuingTime").order("asc,desc,asc").setOrderNulls("false,false,true");
		page.orderBy("appointDate,queuingTime").order("desc,asc").setOrderNulls("false,true");
		page= reservationOutboundService.search(page,filters);
		return getEasyUIData(page);
	}



	// 核放申报
	@Transactional
	@RequestMapping(value = "apply/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String applyData(HttpServletRequest request, @PathVariable("id") String id) throws IOException, ServiceException {

		//根据主键id查询预约出库记录
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_id", id));
		List<PlatformReservationOutbound> outboundList = reservationOutboundService.search(filters);

		String jsonString = "";

		if (!outboundList.isEmpty()) {
			//拼装Json
			PlatformReservationOutbound outbound = outboundList.get(0);
			// 1、创建一个SAXTransformerFactory类的对象
			SAXTransformerFactory tff = (SAXTransformerFactory) SAXTransformerFactory.newInstance();

			try {
				// 2、通过SAXTransformerFactory创建一个TransformerHandler的对象
				TransformerHandler handler = tff.newTransformerHandler();
				// 3、通过handler创建一个Transformer对象
				Transformer tr = handler.getTransformer();
				// 4、通过Transformer对象对生成的xml文件进行设置
				// 设置编码方式
				tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				// 设置是否换行
				tr.setOutputProperty(OutputKeys.INDENT, "yes");
				// 1. 读取系统时间
				Calendar calendar = Calendar.getInstance();
				Date time = calendar.getTime();
				// 2. 格式化系统时间
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
				SimpleDateFormat formatdate = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat formatbd = new SimpleDateFormat("yyyyMMdd");
				String fileName = "LL" + format.format(time); // 获取系统当前时间并将其转换为string类型,fileName即文件名

				File file = new File("D:/supervision");
				if (!file.isDirectory()) {// 路径文件夹是否存在，不存在则创建
					file.mkdirs();
				}
				// 5、创建一个Result对象
						File f = new File("D:/supervision/" + fileName + ".FLJGRX");
				// 判断文件是否存在
				if (!f.exists()) {
					f.createNewFile();
				}
				Result result = new StreamResult(new FileOutputStream(f));
				// 组建数据
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append("");

				// 6、使RESULT与handler关联
				handler.setResult(result);
				// 打开document
				handler.startDocument();
				AttributesImpl attr = new AttributesImpl();
				attr.clear();
				// 空格
				String four = "\n    ";
				String eight = "\n        ";
				String nine = "\n          ";
				String ten = "\n            ";

				// 组建参数
				handler.startElement("", "  ", "XMLObject", attr);
				handler.characters(eight.toCharArray(), 0, eight.length());
				// 报文头基本信息 应用代码
				attr.clear();
				handler.startElement("", "", "APP_CODE", attr);
				String APPCODE = "SAS";
				handler.characters(APPCODE.toCharArray(), 0, APPCODE.length());
				handler.endElement("", "", "APP_CODE");
				handler.characters(eight.toCharArray(), 0, eight.length());

				// 环节号
				attr.clear();
				handler.startElement("", "", "APP_STEP_ID", attr);
				String APPSTEPID = "STOCK_APPLY";
				handler.characters(APPSTEPID.toCharArray(), 0, APPSTEPID.length());
				handler.endElement("", "", "APP_STEP_ID");
				handler.characters(eight.toCharArray(), 0, eight.length());

				// 序列化类名
				attr.clear();
				handler.startElement("", "", "CLASS_NAME", attr);
				String CLASSNAME = "XMLOject";
				handler.characters(CLASSNAME.toCharArray(), 0, CLASSNAME.length());
				handler.endElement("", "", "CLASS_NAME");
				handler.characters(eight.toCharArray(), 0, eight.length());

				// 版本号
				attr.clear();
				handler.startElement("", "", "CLASS_VER", attr);
				String CLASSVER = "1.0";
				handler.characters(CLASSVER.toCharArray(), 0, CLASSVER.length());
				handler.endElement("", "", "CLASS_VER");
				handler.characters(eight.toCharArray(), 0, eight.length());

				// 生成日期
				attr.clear();
				handler.startElement("", "", "FILE_DATE_TIME", attr);
				handler.characters(formatdate.format(time).toCharArray(), 0, formatdate.format(time).length());
				handler.endElement("", "", "FILE_DATE_TIME");
				handler.characters(eight.toCharArray(), 0, eight.length());

				// 生成人
				attr.clear();
				handler.startElement("", "", "FILE_GERATER", attr);
				String FILEGERATER = "青岛港怡之航冷链物流有限公司";
				handler.characters(FILEGERATER.toCharArray(), 0, FILEGERATER.length());
				handler.endElement("", "", "FILE_GERATER");
				handler.characters(eight.toCharArray(), 0, eight.length());

				// 顺序
				attr.clear();
				handler.startElement("", "", "FILE_ORDER", attr);
				String FILEORDER = "1";
				handler.characters(FILEORDER.toCharArray(), 0, FILEORDER.length());
				handler.endElement("", "", "FILE_ORDER");
				handler.characters(eight.toCharArray(), 0, eight.length());

				// 文件名
				attr.clear();
				handler.startElement("", "", "FILE_ORIGINAL_NAME", attr);
				String FILENAME = fileName + ".FLJGRX";
				handler.characters(FILENAME.toCharArray(), 0, FILENAME.length());
				handler.endElement("", "", "FILE_ORIGINAL_NAME");
				handler.characters(eight.toCharArray(), 0, eight.length());

				// 大小
				attr.clear();
				handler.startElement("", "", "FILE_SIZE", attr);
				String FILESIZE = "0";
				handler.characters(FILESIZE.toCharArray(), 0, FILESIZE.length());
				handler.endElement("", "", "FILE_SIZE");
				handler.characters(eight.toCharArray(), 0, eight.length());

				attr.clear();
				handler.startElement("", "", "SDEPORT_DATA", attr);
				handler.characters(eight.toCharArray(), 0, eight.length());
					attr.clear();
					handler.startElement("", "", "NonbondedPassportMessage", attr);
					handler.characters(eight.toCharArray(), 0, eight.length());
						attr.clear();
						handler.startElement("", "", "NonbondedPassport", attr);
						handler.characters(eight.toCharArray(), 0, eight.length());
					//SeqNo
					attr.clear();
					handler.startElement("", "", "SeqNo", attr);
					String SeqNo ="";
					handler.characters(SeqNo.toCharArray(), 0, SeqNo.length());
					handler.endElement("", "", "SeqNo");
					handler.characters(eight.toCharArray(), 0, eight.length());
                    //CopSeqNo
					attr.clear();
					handler.startElement("", "", "CopSeqNo", attr);
					String CopSeqNo =outbound.getId();
					handler.characters(CopSeqNo.toCharArray(), 0, CopSeqNo.length());
					handler.endElement("", "", "CopSeqNo");
					handler.characters(eight.toCharArray(), 0, eight.length());
					//VehicleNo
					attr.clear();
					handler.startElement("", "", "VehicleNo", attr);
					String VehicleNo =outbound.getCarNumber();
					handler.characters(VehicleNo.toCharArray(), 0, VehicleNo.length());
					handler.endElement("", "", "VehicleNo");
					handler.characters(eight.toCharArray(), 0, eight.length());
				   //经营单位代码
					attr.clear();
					handler.startElement("", "", "TradeCode", attr);
				    String TradeCode = "3702631016";
					handler.characters(TradeCode.toCharArray(), 0, TradeCode.length());
					handler.endElement("", "", "TradeCode");
					handler.characters(eight.toCharArray(), 0, eight.length());

					// 经营单位名称
					attr.clear();
					handler.startElement("", "", "TradeName", attr);
					handler.characters(FILEGERATER.toCharArray(), 0, FILEGERATER.length());
					handler.endElement("", "", "TradeName");
					handler.characters(eight.toCharArray(), 0, eight.length());
					// 企业号
					attr.clear();
					handler.startElement("", "", "CustomsCode", attr);
					String EmsNo = "4230";
					handler.characters(EmsNo.toCharArray(), 0, EmsNo.length());
					handler.endElement("", "", "CustomsCode");
					handler.characters(ten.toCharArray(), 0, ten.length());
					// 出入状态
					attr.clear();
					handler.startElement("", "", "IEFlag", attr);
					String ieflag = "I";
					handler.characters(ieflag.toCharArray(), 0, ieflag.length());
					handler.endElement("", "", "IEFlag");
					handler.characters(ten.toCharArray(), 0, ten.length());

					// 备注
					attr.clear();
					handler.startElement("", "", "Rmk", attr);
					String rmk = "1";
					handler.characters(rmk.toCharArray(), 0, ieflag.length());
					handler.endElement("", "", "Rmk");
					handler.characters(ten.toCharArray(), 0, ten.length());




					handler.endElement("", "", "NonbondedPassport");
					handler.characters(eight.toCharArray(), 0, eight.length());
				handler.endElement("", "", "NonbondedPassportMessage");
				handler.characters(eight.toCharArray(), 0, eight.length());
				handler.endElement("", "", "SDEPORT_DATA");
				handler.characters(eight.toCharArray(), 0, eight.length());

				attr.clear();
				handler.startElement("", "", "XML_TYPE", attr);
				String XMLTYPE = "W3C";
				handler.characters(XMLTYPE.toCharArray(), 0, XMLTYPE.length());
				handler.endElement("", "", "XML_TYPE");
				handler.characters(eight.toCharArray(), 0, eight.length());

			    handler.endElement("", "  ", "XMLObject");
				// 关闭document
				handler.endDocument();
				//将生成的xml文件反写
				reservationOutboundService.updateFiledNameById(fileName,id);
				System.out.println("生成.FLJGRX成功");
				String ftpHost = "10.135.200.5";
				String ftpUserName = "adminftp";
				String ftpPassword = "adminftp!@#2023";
				fileName = fileName + ".FLJGRX";
				int ftpPort = 21;
				String ftpPath = "Send/";
				FileInputStream in = new FileInputStream(f);
				///文件上传FTP  //怡之航 将预约出库 报文申报上传到韩总电脑Send目录下
				uploadFile(ftpHost,ftpUserName, ftpPassword, ftpPort, ftpPath, fileName, in);
				f.delete();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("生成.FLJGRX失败");
			}
		}
		return "success";
	}


	/**
	 * Description:             向FTP服务器上传文件
	 * @param ftpHost              FTP服务器hostname
	 * @param ftpUserName          FTP登录账号
	 * @param ftpPassword          FTP登录密码
	 * @param ftpPort          FTP服务器基础目录
	 * @param ftpPath          FTP服务器文件存放路径。例如分日期存放：/2015/01/01。文件的路径为basePath+filePath
	 * @param filename          上传到FTP服务器上的文件名
	 * @param input             输入流
	 * @return                  成功返回true，否则返回false
	 */
	public static boolean uploadFile(String ftpHost, String ftpUserName, String ftpPassword, int ftpPort, String ftpPath, String filename, InputStream input) {
		boolean result = false;
		FTPClient ftpClient = new FTPClient();
		try {
			int reply;
			ftpClient = getFTPClient(ftpHost, ftpUserName, ftpPassword, ftpPort);
			ftpClient.changeWorkingDirectory(ftpPath);

			reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				return result;
			}

			filename = new String(filename.getBytes("GBK"), FTP.DEFAULT_CONTROL_ENCODING);//编码文件名，支持中文文件名
			//上传文件
			if (!ftpClient.storeFile(filename, input)) {

				return result;

			}
			System.out.println("上传成功");
			input.close();
			ftpClient.logout();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return result;
	}


	/**
	 * 获取FTPClient对象
	 * @param ftpHost       FTP主机服务器
	 * @param ftpUserName   FTP 登录密码
	 * @param ftpPassword   FTP登录用户名
	 * @param ftpPort       FTP端口 默认为21
	 * @return
	 */
	public static FTPClient getFTPClient(String ftpHost, String ftpUserName, String ftpPassword, int ftpPort) {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient = new FTPClient();
			ftpClient.connect(ftpHost, ftpPort);              // 连接FTP服务器
			ftpClient.login(ftpUserName, ftpPassword);// 登陆FTP服务器
			ftpClient.setControlEncoding("UTF-8"); // 中文支持
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				System.out.println("未连接到FTP，用户名或密码错误。");
				//logger.info();
				ftpClient.disconnect();
			} else {
				System.out.println("FTP连接成功。");
				// logger.info("");
			}
		} catch (SocketException e) {
			e.printStackTrace();
			System.out.println("FTP的IP地址可能错误，请正确配置。");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("FTP的端口错误,请正确配置。");
		}
		return ftpClient;
	}

}
