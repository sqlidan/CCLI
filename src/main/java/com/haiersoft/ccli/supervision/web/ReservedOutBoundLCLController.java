package com.haiersoft.ccli.supervision.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.haiersoft.ccli.base.service.HscodeService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.platform.entity.PlatformReservationOutbound;
import com.haiersoft.ccli.platform.service.ReservationOutboundService;
import com.haiersoft.ccli.supervision.entity.ApprHead;
import com.haiersoft.ccli.supervision.entity.ApprInfo;
import com.haiersoft.ccli.supervision.entity.ManiHead;
import com.haiersoft.ccli.supervision.entity.ManiInfo;
import com.haiersoft.ccli.supervision.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
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
				handler.startElement("", "", "XML_TYPE", attr);
				String XMLTYPE = "W3C";
				handler.characters(XMLTYPE.toCharArray(), 0, XMLTYPE.length());
				handler.endElement("", "", "XML_TYPE");
				handler.characters(eight.toCharArray(), 0, eight.length());

			/*	// 经营单位代码
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
				// 仓库名称
				attr.clear();
				handler.startElement("", "", "HouseName", attr);
				String HouseNameOne = "怡之航";
				handler.characters(HouseNameOne.toCharArray(), 0, HouseNameOne.length());
				handler.endElement("", "", "HouseName");
				handler.characters(eight.toCharArray(), 0, eight.length());*/







				attr.clear();
				handler.startElement("", "", "StockInfo", attr);
				handler.characters(ten.toCharArray(), 0, ten.length());
				   //经营单位代码
					attr.clear();
					handler.startElement("", "", "TradeCode", attr);
				    String TradeCode = "3702631016";
					handler.characters(TradeCode.toCharArray(), 0, TradeCode.length());
					handler.endElement("", "", "TradeCode");
					handler.characters(eight.toCharArray(), 0, eight.length());
					//报送日期
					attr.clear();
					handler.startElement("", "", "ApplyDate", attr);
					handler.characters(formatbd.format(time).toCharArray(), 0, formatbd.format(time).length());
					handler.endElement("", "", "ApplyDate");
					handler.characters(eight.toCharArray(), 0, eight.length());
					//仓库物料号
					attr.clear();
					handler.startElement("", "", "WmsMtsNo", attr);
					String wmsmtsNO = "";
					handler.characters(wmsmtsNO.toCharArray(), 0, wmsmtsNO.length());
					handler.endElement("", "", "WmsMtsNo");
					handler.characters(ten.toCharArray(), 0, ten.length());
					// 经营单位名称
					attr.clear();
					handler.startElement("", "", "TradeName", attr);
					handler.characters(FILEGERATER.toCharArray(), 0, FILEGERATER.length());
					handler.endElement("", "", "TradeName");
					handler.characters(eight.toCharArray(), 0, eight.length());
					// 账册号
					attr.clear();
					handler.startElement("", "", "EmsNo", attr);
					String EmsNo = "";
					handler.characters(EmsNo.toCharArray(), 0, EmsNo.length());
					handler.endElement("", "", "EmsNo");
					handler.characters(ten.toCharArray(), 0, ten.length());
					// 账册序号
					attr.clear();
					handler.startElement("", "", "EmsSeqNo", attr);
					String EmsSeqNo = "";
					handler.characters(EmsSeqNo.toCharArray(), 0, EmsSeqNo.length());
					handler.endElement("", "", "EmsSeqNo");
					handler.characters(ten.toCharArray(), 0, ten.length());


					// 商品料号
					attr.clear();
					handler.startElement("", "", "GoodsMtsNo", attr);
					// handler.characters("".toCharArray(), 0, "".length());
					String GoodsMtsNo = "";
					handler.characters(GoodsMtsNo.toCharArray(), 0, GoodsMtsNo.length());
					handler.endElement("", "", "GoodsMtsNo");
					handler.characters(ten.toCharArray(), 0, ten.length());
					// 商品编码
					attr.clear();
					handler.startElement("", "", "CodeTs", attr);
					// handler.characters("".toCharArray(), 0, "".length());
					String CodeTs = "";
					handler.characters(CodeTs.toCharArray(), 0, CodeTs.length());
					handler.endElement("", "", "CodeTs");
					handler.characters(ten.toCharArray(), 0, ten.length());
					// 商品名称
					attr.clear();
					handler.startElement("", "", "GoodsName", attr);
					// handler.characters("".toCharArray(), 0, "".length());
					String GoodsName = outbound.getProductName() == null ? "" : outbound.getProductName();
					handler.characters(GoodsName.toCharArray(), 0, GoodsName.length());
					handler.endElement("", "", "GoodsName");
					handler.characters(ten.toCharArray(), 0, ten.length());
					// 商品规格型号
					attr.clear();
					handler.startElement("", "", "GoodsModelDesc", attr);
					String GoodsModelDesc = "";
					handler.characters(GoodsModelDesc.toCharArray(), 0, GoodsModelDesc.length());
					handler.endElement("", "", "GoodsModelDesc");
					handler.characters(ten.toCharArray(), 0, ten.length());
					// 申报计量单位
					attr.clear();
					handler.startElement("", "", "WmsDclUnit", attr);
					String DclUnit = "kg";
					handler.characters(DclUnit.toCharArray(), 0, DclUnit.length());
					handler.endElement("", "", "WmsDclUnit");
					handler.characters(ten.toCharArray(), 0, ten.length());
					// 申报数量
					attr.clear();
					handler.startElement("", "", "WmsDclQty", attr);
					// handler.characters("".toCharArray(), 0, "".length());
					String DclQty = outbound.getNum() == null ? "" : outbound.getNum();
					handler.characters(DclQty.toCharArray(), 0, DclQty.length());
					handler.endElement("", "", "WmsDclQty");
					handler.characters(ten.toCharArray(), 0, ten.length());
					// 法定单位
					attr.clear();
					handler.startElement("", "", "WmsLawUnit", attr);
					String WmsDclUnit =  "035";
					handler.characters(WmsDclUnit.toCharArray(), 0, WmsDclUnit.length());
					handler.endElement("", "", "WmsLawUnit");
					handler.characters(ten.toCharArray(), 0, ten.length());
					// 法定数量
					attr.clear();
					handler.startElement("", "", "WmsLawQty", attr);
					String WmsDclQty = "";
					handler.characters(WmsDclQty.toCharArray(), 0, WmsDclQty.length());
					handler.endElement("", "", "WmsLawQty");
					handler.characters(ten.toCharArray(), 0, ten.length());
					// 库区号
					attr.clear();
					handler.startElement("", "", "PlaceIds", attr);
					//handler.characters("".toCharArray(), 0, "".length());
					String PlaceIds = outbound.getRoomNum() == null ? "" : outbound.getRoomNum();
					handler.characters(PlaceIds.toCharArray(), 0, PlaceIds.length());
					handler.endElement("", "", "PlaceIds");
					handler.characters(ten.toCharArray(), 0, ten.length());
					// 库位号
					attr.clear();
					handler.startElement("", "", "LocationIds", attr);
					//handler.characters("".toCharArray(), 0, "".length());
					String LocationIds = "";
					handler.characters(LocationIds.toCharArray(), 0, LocationIds.length());
					handler.endElement("", "", "LocationIds");
					handler.characters(ten.toCharArray(), 0, ten.length());
					// 出入库状态 0 预出入库 1在库
					attr.clear();
					handler.startElement("", "", "StockStatus", attr);
					handler.characters("1".toCharArray(), 0, "1".length());
					handler.endElement("", "", "StockStatus");
					handler.characters(ten.toCharArray(), 0, ten.length());

					handler.endElement("", "", "StockInfo");
					handler.characters(eight.toCharArray(), 0, eight.length());
				// 关闭document
				handler.endDocument();
				///文件上传FTP
				System.out.println("生成.FLJGRX成功");
				String ftpHost = "10.135.252.42";
				String ftpUserName = "yzh";
				String ftpPassword = "yzh";
				fileName = fileName + ".FLJGRX";
				int ftpPort = 21;
				String ftpPath = "Send/";
				FileInputStream in = new FileInputStream(f);
				//uploadFile(ftpHost,ftpUserName, ftpUserName, ftpPort, ftpPath, fileName, in);
				f.delete();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("生成.FLJGRX失败");
			}
		}
		return "success";
	}

}
