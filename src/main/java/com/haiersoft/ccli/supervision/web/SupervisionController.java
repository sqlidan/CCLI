package com.haiersoft.ccli.supervision.web;

import com.haiersoft.ccli.supervision.service.SupervisionService;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.helpers.AttributesImpl;

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
 * 海关监管文件
 * 
 * @author liuyuyu
 *
 */
@Component("supervision")
public class SupervisionController {

	 //public static SupervisionController supervisionController;

	//
//	private SupervisionService supervisionService;
	// 生成xml

	@ResponseBody
	public void createXml() {

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

			//
			attr.clear();
			handler.startElement("", "", "SDEPORT_DATA", attr);
			attr.clear();
			handler.characters(nine.toCharArray(), 0, nine.length());

			handler.startElement("", "", "StockRecordMessage", attr);
			handler.characters(nine.toCharArray(), 0, nine.length());

			/*
			 * CopBaseInfo copBaseInfo = supervisionService.getBaseinfo(id);
			 * 
			 * 
			 * List<CopBaseInfo> list=supervisionService.getBaseinfo();
			 */
			int aa = 01;
			List<HashMap> list = new ArrayList<>();
			ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
			SupervisionService supervisionService = (SupervisionService) applicationContext
				.getBean("supervisionService");
			list = supervisionService.searchStockReport(aa);
		//	System.out.println(list);
			for (HashMap hashMap : list) {
				// CopBaseInfo copBase = (CopBaseInfo) list.get(i);
				String ZKTRADE = hashMap.get("ZKTRADE") == null ? "" : hashMap.get("ZKTRADE").toString();//在库保税
                String ZKFOWARD= hashMap.get("ZKFORWARD") == null ? "" : hashMap.get("ZKFORWARD").toString();//在库一般贸易
                String GoodsType = hashMap.get("GOODSTYPE") == null ? "" : hashMap.get("GOODSTYPE").toString();
    //            if ((ZKTRADE==null&&ZKFOWARD==null)||(ZKTRADE.equals("")&&ZKFOWARD.equals(""))) {
					
				
				attr.clear();
				handler.startElement("", "", "StockInfo", attr);
				handler.characters(ten.toCharArray(), 0, ten.length());

				// 账册号
				attr.clear();
				handler.startElement("", "", "EmsNo", attr);
				String EmsNo="";
				if (GoodsType.equals("1")) {
					EmsNo="T4230W000031";//如果是保税货物，账册号固定
				}else{
					EmsNo="";
				}
			
				handler.characters(EmsNo.toCharArray(), 0, EmsNo.length());
				handler.endElement("", "", "EmsNo");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 账册序号
				attr.clear();
				handler.startElement("", "", "EmsSeqNo", attr);
				String EmsSeqNo ="";

				if (GoodsType.equals("1")) {
					EmsSeqNo= hashMap.get("EMSSEQNO") == null ? "" : hashMap.get("EMSSEQNO").toString();//保税货物有账册商品序号
				}
				handler.characters(EmsSeqNo.toCharArray(), 0, EmsSeqNo.length());
				handler.endElement("", "", "EmsSeqNo");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 仓库物料号
				attr.clear();
				handler.startElement("", "", "WmsMtsNo", attr);
				String wmsmtsNO = hashMap.get("WMSMTSNO") == null ? "" : hashMap.get("WMSMTSNO").toString();
					wmsmtsNO=bSubstring(wmsmtsNO,29);
				 handler.characters(wmsmtsNO.toCharArray(), 0, wmsmtsNO.length());
				handler.endElement("", "", "WmsMtsNo");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 商品料号
				attr.clear();
				handler.startElement("", "", "GoodsMtsNo", attr);
				// handler.characters("".toCharArray(), 0, "".length());
				String GoodsMtsNo = hashMap.get("GOODSMTSNO") == null ? "" : hashMap.get("GOODSMTSNO").toString();

				handler.characters(GoodsMtsNo.toCharArray(), 0, GoodsMtsNo.length());
				handler.endElement("", "", "GoodsMtsNo");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 商品编码
				attr.clear();
				handler.startElement("", "", "CodeTs", attr);
				// handler.characters("".toCharArray(), 0, "".length());
				String CodeTs = hashMap.get("hscode") == null ? "" : hashMap.get("hscode").toString();

				handler.characters(CodeTs.toCharArray(), 0, CodeTs.length());
				handler.endElement("", "", "CodeTs");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 商品名称
				attr.clear();
				handler.startElement("", "", "GoodsName", attr);
				// handler.characters("".toCharArray(), 0, "".length());
				String GoodsName = hashMap.get("GOODSNAME") == null ? "" : hashMap.get("GOODSNAME").toString();

				handler.characters(GoodsName.toCharArray(), 0, GoodsName.length());
				handler.endElement("", "", "GoodsName");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 商品规格型号
				attr.clear();
				handler.startElement("", "", "GoodsModelDesc", attr);
				String GoodsModelDesc=hashMap.get("TYPESIZE") == null ? "" : hashMap.get("TYPESIZE").toString();
				handler.characters(GoodsModelDesc.toCharArray(), 0, GoodsModelDesc.length());
				// handler.characters(copBase.getGoodsModelDesc().toCharArray(),
				// 0, copBase.getGoodsModelDesc().length());
				handler.endElement("", "", "GoodsModelDesc");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 申报计量单位
				attr.clear();
				handler.startElement("", "", "WmsDclUnit", attr);
				String DclUnit = hashMap.get("DCLUNIT") == null ? "" : hashMap.get("DCLUNIT").toString();

				handler.characters(DclUnit.toCharArray(), 0, DclUnit.length());
				// handler.characters(copBase.getWmsDclUnit().toCharArray(), 0,
				// copBase.getWmsDclUnit().length());
				handler.endElement("", "", "WmsDclUnit");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 申报数量
				attr.clear();
				handler.startElement("", "", "WmsDclQty", attr);
				// handler.characters("".toCharArray(), 0, "".length());
				String DclQty = hashMap.get("DCLQTY") == null ? "" : hashMap.get("DCLQTY").toString();

				handler.characters(DclQty.toCharArray(), 0, DclQty.length());
				handler.endElement("", "", "WmsDclQty");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 法定单位
				attr.clear();
				handler.startElement("", "", "WmsLawUnit", attr);
				String WmsDclUnit = hashMap.get("itemnum") == null ? "035" : hashMap.get("itemnum").toString();

				handler.characters(WmsDclUnit.toCharArray(), 0, WmsDclUnit.length());
				// handler.characters(copBase.getWmsLawUnit().toCharArray(), 0,
				// copBase.getWmsLawUnit().length());
				handler.endElement("", "", "WmsLawUnit");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 法定数量
				attr.clear();
				handler.startElement("", "", "WmsLawQty", attr);
				String WmsDclQty = hashMap.get("WMSDCLQTY") == null ? "" : hashMap.get("WMSDCLQTY").toString();

				handler.characters(WmsDclQty.toCharArray(), 0, WmsDclQty.length());
				// handler.characters(copBase.getWmsLawQty().toCharArray(), 0,
				// copBase.getWmsLawQty().length());
				handler.endElement("", "", "WmsLawQty");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 库区号
				attr.clear();
				handler.startElement("", "", "PlaceIds", attr);
				//handler.characters("".toCharArray(), 0, "".length());
				String PlaceIds = hashMap.get("PLACEIDS") == null ? null : hashMap.get("PLACEIDS").toString();
				if(null==PlaceIds||" ".equals(PlaceIds)){
					continue;
				}
				handler.characters(PlaceIds.toCharArray(), 0, PlaceIds.length());
				handler.endElement("", "", "PlaceIds");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 库位号
				attr.clear();
				handler.startElement("", "", "LocationIds", attr);
				//handler.characters("".toCharArray(), 0, "".length());
				String LocationIds = hashMap.get("LOCATIONIDS") == null ? null : hashMap.get("LOCATIONIDS").toString();
				if(null==LocationIds||" ".equals(LocationIds)){
					continue;
				}
				handler.characters(LocationIds.toCharArray(), 0, LocationIds.length());
				handler.endElement("", "", "LocationIds");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 出入库状态 0 预出入库 1在库
				attr.clear();
				handler.startElement("", "", "StockStatus", attr);
				handler.characters("1".toCharArray(), 0, "1".length());
				// handler.characters(copBase.getStockStatus().toCharArray(),
				// 0,copBase.getStockStatus().length());
				handler.endElement("", "", "StockStatus");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 货物类型 0 非保 1 保税
				attr.clear();
				handler.startElement("", "", "GoodsType", attr);
				// handler.characters("0".toCharArray(), 0, "0".length());
			//	String GoodsType = hashMap.get("GOODSTYPE") == null ? "" : hashMap.get("GOODSTYPE").toString();

				if (GoodsType != null && GoodsType.equals("1")) {
					handler.characters("1".toCharArray(), 0, "1".length());
				} else {
					handler.characters("0".toCharArray(), 0, "0".length());
				}
				// handler.characters(copBase.getGoodsType().toCharArray(), 0,
				// copBase.getGoodsType().length());
				handler.endElement("", "", "GoodsType");
				handler.characters(nine.toCharArray(), 0, nine.length());
				handler.endElement("", "", "StockInfo");
				handler.characters(eight.toCharArray(), 0, eight.length());
				
				
				
      //         }
//                else if (GoodsType.equals("1")){
//
//					//2.保税转一般贸易
//					// 1.保税转一般贸易后库中保税
//    				attr.clear();
//    				handler.startElement("", "", "StockInfo", attr);
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//
//    				// 账册号
//    				attr.clear();
//    				handler.startElement("", "", "EmsNo", attr);
//
//    				handler.characters("T4230W000031".toCharArray(), 0, "T4230W000031".length());
//    				handler.endElement("", "", "EmsNo");
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//    				// 账册序号
//    				attr.clear();
//    				handler.startElement("", "", "EmsSeqNo", attr);
//    				String EmsSeqNo ="";
//
//
//    				EmsSeqNo= hashMap.get("EMSSEQNO") == null ? "" : hashMap.get("EMSSEQNO").toString();//保税货物有账册商品序号
//
//    				handler.characters(EmsSeqNo.toCharArray(), 0, EmsSeqNo.length());
//    				handler.endElement("", "", "EmsSeqNo");
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//    				// 仓库物料号
//    				attr.clear();
//    				handler.startElement("", "", "WmsMtsNo", attr);
//    				String wmsmtsNO = hashMap.get("WMSMTSNO") == null ? "" : hashMap.get("WMSMTSNO").toString();
//					wmsmtsNO=bSubstring(wmsmtsNO,29);
//
//					handler.characters(wmsmtsNO.toCharArray(), 0, wmsmtsNO.length());
//    				handler.endElement("", "", "WmsMtsNo");
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//    				// 商品料号
//    				attr.clear();
//    				handler.startElement("", "", "GoodsMtsNo", attr);
//    				// handler.characters("".toCharArray(), 0, "".length());
//    				String GoodsMtsNo = hashMap.get("GOODSMTSNO") == null ? "" : hashMap.get("GOODSMTSNO").toString();
//
//    				handler.characters(GoodsMtsNo.toCharArray(), 0, GoodsMtsNo.length());
//    				handler.endElement("", "", "GoodsMtsNo");
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//    				// 商品编码
//    				attr.clear();
//    				handler.startElement("", "", "CodeTs", attr);
//    				// handler.characters("".toCharArray(), 0, "".length());
//    				String CodeTs = hashMap.get("hscode") == null ? "" : hashMap.get("hscode").toString();
//
//    				handler.characters(CodeTs.toCharArray(), 0, CodeTs.length());
//    				handler.endElement("", "", "CodeTs");
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//    				// 商品名称
//    				attr.clear();
//    				handler.startElement("", "", "GoodsName", attr);
//    				// handler.characters("".toCharArray(), 0, "".length());
//    				String GoodsName = hashMap.get("GOODSNAME") == null ? "" : hashMap.get("GOODSNAME").toString();
//
//    				handler.characters(GoodsName.toCharArray(), 0, GoodsName.length());
//    				handler.endElement("", "", "GoodsName");
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//    				// 商品规格型号
//    				attr.clear();
//    				handler.startElement("", "", "GoodsModelDesc", attr);
//    				String GoodsModelDesc=hashMap.get("TYPESIZE") == null ? "" : hashMap.get("TYPESIZE").toString();
//    				handler.characters(GoodsModelDesc.toCharArray(), 0, GoodsModelDesc.length());
//
//    				// handler.characters(copBase.getGoodsModelDesc().toCharArray(),
//    				// 0, copBase.getGoodsModelDesc().length());
//    				handler.endElement("", "", "GoodsModelDesc");
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//    				// 申报计量单位
//    				attr.clear();
//    				handler.startElement("", "", "WmsDclUnit", attr);
//    				String WmsDclUnit = hashMap.get("itemnum") == null ? "035" : hashMap.get("itemnum").toString();
//
//    				handler.characters(WmsDclUnit.toCharArray(), 0, WmsDclUnit.length());
//    				handler.endElement("", "", "WmsDclUnit");
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//    				// 申报数量
//    				attr.clear();
//    				handler.startElement("", "", "WmsDclQty", attr);
//    				handler.characters(ZKTRADE.toCharArray(), 0, ZKTRADE.length());
//    				handler.endElement("", "", "WmsDclQty");
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//    				// 法定单位
//    				attr.clear();
//    				handler.startElement("", "", "WmsLawUnit", attr);
//
//    				handler.characters(WmsDclUnit.toCharArray(), 0, WmsDclUnit.length());
//    				handler.endElement("", "", "WmsLawUnit");
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//    				// 法定数量
//    				attr.clear();
//    				handler.startElement("", "", "WmsLawQty", attr);
//    				handler.characters(ZKTRADE.toCharArray(), 0, ZKTRADE.length());
//    				handler.endElement("", "", "WmsLawQty");
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//    				// 库区号
//    				attr.clear();
//    				handler.startElement("", "", "PlaceIds", attr);
//    				String PlaceIds = hashMap.get("PLACEIDS") == null ? "" : hashMap.get("PLACEIDS").toString();
//					if("".equals(PlaceIds)){
//						continue;
//					}
//    				handler.characters(PlaceIds.toCharArray(), 0, PlaceIds.length());
//    				handler.endElement("", "", "PlaceIds");
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//    				// 库位号
//    				attr.clear();
//    				handler.startElement("", "", "LocationIds", attr);
//    				String LocationIds = hashMap.get("LOCATIONIDS") == null ? "" : hashMap.get("LOCATIONIDS").toString();
//    				if("".equals(LocationIds)){
//    					continue;
//					}
//
//    				handler.characters(LocationIds.toCharArray(), 0, LocationIds.length());
//    				handler.endElement("", "", "LocationIds");
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//    				// 出入库状态 0 预出入库 1在库
//    				attr.clear();
//    				handler.startElement("", "", "StockStatus", attr);
//    				handler.characters("1".toCharArray(), 0, "1".length());
//    				handler.endElement("", "", "StockStatus");
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//    				// 货物类型 0 非保 1 保税
//    				attr.clear();
//    				handler.startElement("", "", "GoodsType", attr);
//
//    				handler.characters("1".toCharArray(), 0, "1".length());
//    				handler.endElement("", "", "GoodsType");
//    				handler.characters(nine.toCharArray(), 0, nine.length());
//    				handler.endElement("", "", "StockInfo");
//    				handler.characters(eight.toCharArray(), 0, eight.length());
//
//
//
//					// 1.保税转一般贸易后库中一般贸易
//    				attr.clear();
//    				handler.startElement("", "", "StockInfo", attr);
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//
//    				// 账册号
//    				attr.clear();
//    				handler.startElement("", "", "EmsNo", attr);
//    				handler.characters("".toCharArray(), 0, "".length());
//    				handler.endElement("", "", "EmsNo");
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//    				// 账册序号
//    				attr.clear();
//    				handler.startElement("", "", "EmsSeqNo", attr);
//    				handler.characters("".toCharArray(), 0, "".length());
//    				handler.endElement("", "", "EmsSeqNo");
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//    				// 仓库物料号
//    				attr.clear();
//    				handler.startElement("", "", "WmsMtsNo", attr);
//    				wmsmtsNO=wmsmtsNO+"YB";
//					wmsmtsNO=bSubstring(wmsmtsNO,29);
//
//					handler.characters(wmsmtsNO.toCharArray(), 0, wmsmtsNO.length());
//    				handler.endElement("", "", "WmsMtsNo");
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//    				// 商品料号
//    				attr.clear();
//    				handler.startElement("", "", "GoodsMtsNo", attr);
//    				// handler.characters("".toCharArray(), 0, "".length());
//
//    				handler.characters(GoodsMtsNo.toCharArray(), 0, GoodsMtsNo.length());
//    				handler.endElement("", "", "GoodsMtsNo");
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//    				// 商品编码
//    				attr.clear();
//    				handler.startElement("", "", "CodeTs", attr);
//    				// handler.characters("".toCharArray(), 0, "".length());
//    				handler.characters(CodeTs.toCharArray(), 0, CodeTs.length());
//    				handler.endElement("", "", "CodeTs");
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//    				// 商品名称
//    				attr.clear();
//    				handler.startElement("", "", "GoodsName", attr);
//    				// handler.characters("".toCharArray(), 0, "".length());
//    				handler.characters(GoodsName.toCharArray(), 0, GoodsName.length());
//    				handler.endElement("", "", "GoodsName");
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//    				// 商品规格型号
//    				attr.clear();
//    				handler.startElement("", "", "GoodsModelDesc", attr);
//    				handler.characters(GoodsModelDesc.toCharArray(), 0, GoodsModelDesc.length());
//
//    				// handler.characters(copBase.getGoodsModelDesc().toCharArray(),
//    				// 0, copBase.getGoodsModelDesc().length());
//    				handler.endElement("", "", "GoodsModelDesc");
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//    				// 申报计量单位
//    				attr.clear();
//    				handler.startElement("", "", "WmsDclUnit", attr);
//    				handler.characters(WmsDclUnit.toCharArray(), 0, WmsDclUnit.length());
//    				handler.endElement("", "", "WmsDclUnit");
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//    				// 申报数量
//    				attr.clear();
//    				handler.startElement("", "", "WmsDclQty", attr);
//    				handler.characters(ZKFOWARD.toCharArray(), 0, ZKFOWARD.length());
//    				handler.endElement("", "", "WmsDclQty");
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//    				// 法定单位
//    				attr.clear();
//    				handler.startElement("", "", "WmsLawUnit", attr);
//
//    				handler.characters(WmsDclUnit.toCharArray(), 0, WmsDclUnit.length());
//    				handler.endElement("", "", "WmsLawUnit");
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//    				// 法定数量
//    				attr.clear();
//    				handler.startElement("", "", "WmsLawQty", attr);
//    				handler.characters(ZKFOWARD.toCharArray(), 0, ZKFOWARD.length());
//    				handler.endElement("", "", "WmsLawQty");
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//    				// 库区号
//    				attr.clear();
//    				handler.startElement("", "", "PlaceIds", attr);
//    				String ZKPLACEIDS= hashMap.get("ZKPLACEIDS") == null ? "" : hashMap.get("ZKPLACEIDS").toString();
//
//    				handler.characters(ZKPLACEIDS.toCharArray(), 0, ZKPLACEIDS.length());
//    				handler.endElement("", "", "PlaceIds");
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//    				// 库位号
//    				attr.clear();
//    				handler.startElement("", "", "LocationIds", attr);
//    				String ZKLOCATIONIDS = hashMap.get("ZKLOCATIONIDS") == null ? "" : hashMap.get("ZKLOCATIONIDS").toString();
//
//    				handler.characters(ZKLOCATIONIDS.toCharArray(), 0, ZKLOCATIONIDS.length());
//    				handler.endElement("", "", "LocationIds");
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//    				// 出入库状态 0 预出入库 1在库
//    				attr.clear();
//    				handler.startElement("", "", "StockStatus", attr);
//    				handler.characters("1".toCharArray(), 0, "1".length());
//    				handler.endElement("", "", "StockStatus");
//    				handler.characters(ten.toCharArray(), 0, ten.length());
//    				// 货物类型 0 非保 1 保税
//    				attr.clear();
//    				handler.startElement("", "", "GoodsType", attr);
//
//    				handler.characters("0".toCharArray(), 0, "0".length());
//    				handler.endElement("", "", "GoodsType");
//    				handler.characters(nine.toCharArray(), 0, nine.length());
//    				handler.endElement("", "", "StockInfo");
//    				handler.characters(eight.toCharArray(), 0, eight.length());
//
//
//
//                }

			}
			// 经营单位代码
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
			// 生成日期
			attr.clear();
			handler.startElement("", "", "ApplyDate", attr);
			handler.characters(formatbd.format(time).toCharArray(), 0, formatbd.format(time).length());
			handler.endElement("", "", "ApplyDate");
			handler.characters(eight.toCharArray(), 0, eight.length());
/*		 for(HashMap hashMap :list){ 

			attr.clear();
			handler.startElement("", "", "SasStockInfo", attr);
			handler.characters(ten.toCharArray(), 0, ten.length());

			// 账册号
			attr.clear();
			handler.startElement("", "", "EmsNo", attr);
			//handler.characters("".toCharArray(), 0, "".length());
			handler.characters("".toCharArray(), 0, "".length());
			handler.endElement("", "", "EmsNo");
			handler.characters(ten.toCharArray(), 0, ten.length());
			// 账册序号
			attr.clear();
			handler.startElement("", "", "EmsSeqNo", attr);
			handler.characters("".toCharArray(), 0, "".length());
		    
			handler.endElement("", "", "EmsSeqNo");
			handler.characters(ten.toCharArray(), 0, ten.length());
			// 商品编码
			attr.clear();
			handler.startElement("", "", "CodeTs", attr);
			handler.characters("".toCharArray(), 0, "".length());
		    
			handler.endElement("", "", "CodeTs");
			handler.characters(eight.toCharArray(), 0, eight.length());

			handler.endElement("", "", "SasStockInfo");
			handler.characters(eight.toCharArray(), 0, eight.length());
		    }*/
		 
			handler.endElement("", "  ", "StockRecordMessage");
			handler.characters(eight.toCharArray(), 0, eight.length());
			
			handler.endElement("", "  ", "SDEPORT_DATA");
			handler.characters(eight.toCharArray(), 0, eight.length());
			// Xml类型
			attr.clear();
			handler.startElement("", "", "XML_TYPE", attr);
			String XMLTYPE = "W3C";
			handler.characters(XMLTYPE.toCharArray(), 0, XMLTYPE.length());
			handler.endElement("", "", "XML_TYPE");
		//	handler.characters(eight.toCharArray(), 0, eight.length());

			handler.endElement("", "  ", "XMLObject");

			// 关闭document
			handler.endDocument();
			///文件上传FTP
			System.out.println("生成.FLJGRX成功");
			String ftpHost="10.135.252.42";
		    String ftpUserName = "yzh";
		    String ftpPassword = "Eimskip0804";
		    fileName=fileName+".FLJGRX";
		    int ftpPort = 21;
		    String ftpPath = "Send/";
	    FileInputStream in=new FileInputStream(f);
		//uploadFile(ftpHost,ftpUserName, ftpUserName, ftpPort, ftpPath, fileName, in);
		 f.delete();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("生成.FLJGRX失败");
		}

	}
	public static String bSubstring(String str, int subSLength)    {
		String  subStr ="";
		try {
			if (str == null) return "";
			else{
				int tempSubLength = subSLength;//截取字节数
				subStr = str.substring(0, str.length()<subSLength ? str.length() : subSLength);//截取的子串
				int subStrByetsL = subStr.getBytes("GBK").length;//截取子串的字节长度
				// 说明截取的字符串中包含有汉字
				while (subStrByetsL > tempSubLength){
					int subSLengthTemp = --subSLength;
					subStr = str.substring(0, subSLengthTemp>str.length() ? str.length() : subSLengthTemp);
					subStrByetsL = subStr.getBytes("GBK").length;
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return subStr;
	}
	/**8
	 * 每天变动库存
	 */
	@ResponseBody
	public void UpdateStoryXml() {

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

			//
			attr.clear();
			handler.startElement("", "", "SDEPORT_DATA", attr);
			attr.clear();
			handler.characters(nine.toCharArray(), 0, nine.length());

			handler.startElement("", "", "StockRecordMessage", attr);
			handler.characters(nine.toCharArray(), 0, nine.length());

			/*
			 * CopBaseInfo copBaseInfo = supervisionService.getBaseinfo(id);
			 * 
			 * 
			 * List<CopBaseInfo> list=supervisionService.getBaseinfo();
			 */
			int aa = 01;
			List<HashMap> list = new ArrayList<>();
			ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
			SupervisionService supervisionService = (SupervisionService) applicationContext
					.getBean("supervisionService");
			list = supervisionService.UpdateStockReport(aa);
		//	System.out.println(list);
			for (HashMap hashMap : list) {
				// CopBaseInfo copBase = (CopBaseInfo) list.get(i);
				String ZKTRADE = hashMap.get("ZKTRADE") == null ? "" : hashMap.get("ZKTRADE").toString();//在库保税
                String ZKFOWARD= hashMap.get("ZKFORWARD") == null ? "" : hashMap.get("ZKFORWARD").toString();//在库一般贸易
                String GoodsType = hashMap.get("GOODSTYPE") == null ? "" : hashMap.get("GOODSTYPE").toString();
                //2025-03-10 徐峥修改，取消保税T4230W000031账册报文的发送
				if (GoodsType.equals("1")) {
					continue;
				}
                if (ZKTRADE==null&&ZKFOWARD==null||ZKTRADE.equals("")&&ZKFOWARD.equals("")) {
					
				
				attr.clear();
				handler.startElement("", "", "StockInfo", attr);
				handler.characters(ten.toCharArray(), 0, ten.length());

				// 账册号
				attr.clear();
				handler.startElement("", "", "EmsNo", attr);
				String EmsNo="";
				if (GoodsType.equals("1")) {
					EmsNo="T4230W000031";//如果是保税货物，账册号固定
				}else{
					EmsNo="";
				}
			
				handler.characters(EmsNo.toCharArray(), 0, EmsNo.length());
				handler.endElement("", "", "EmsNo");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 账册序号
				attr.clear();
				handler.startElement("", "", "EmsSeqNo", attr);
				String EmsSeqNo ="";

				if (GoodsType.equals("1")) {
					EmsSeqNo= hashMap.get("EMSSEQNO") == null ? "" : hashMap.get("EMSSEQNO").toString();//保税货物有账册商品序号
				}
				handler.characters(EmsSeqNo.toCharArray(), 0, EmsSeqNo.length());
				handler.endElement("", "", "EmsSeqNo");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 仓库物料号
				attr.clear();
				handler.startElement("", "", "WmsMtsNo", attr);
				String wmsmtsNO = hashMap.get("WMSMTSNO") == null ? "" : hashMap.get("WMSMTSNO").toString();
				 handler.characters(wmsmtsNO.toCharArray(), 0, wmsmtsNO.length());
				handler.endElement("", "", "WmsMtsNo");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 商品料号
				attr.clear();
				handler.startElement("", "", "GoodsMtsNo", attr);
				// handler.characters("".toCharArray(), 0, "".length());
				String GoodsMtsNo = hashMap.get("GOODSMTSNO") == null ? "" : hashMap.get("GOODSMTSNO").toString();

				handler.characters(GoodsMtsNo.toCharArray(), 0, GoodsMtsNo.length());
				handler.endElement("", "", "GoodsMtsNo");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 商品编码
				attr.clear();
				handler.startElement("", "", "CodeTs", attr);
				// handler.characters("".toCharArray(), 0, "".length());
				String CodeTs = hashMap.get("hscode") == null ? "" : hashMap.get("hscode").toString();

				handler.characters(CodeTs.toCharArray(), 0, CodeTs.length());
				handler.endElement("", "", "CodeTs");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 商品名称
				attr.clear();
				handler.startElement("", "", "GoodsName", attr);
				// handler.characters("".toCharArray(), 0, "".length());
				String GoodsName = hashMap.get("GOODSNAME") == null ? "" : hashMap.get("GOODSNAME").toString();

				handler.characters(GoodsName.toCharArray(), 0, GoodsName.length());
				handler.endElement("", "", "GoodsName");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 商品规格型号
				attr.clear();
				handler.startElement("", "", "GoodsModelDesc", attr);
				String GoodsModelDesc=hashMap.get("TYPESIZE") == null ? "" : hashMap.get("TYPESIZE").toString();
				handler.characters(GoodsModelDesc.toCharArray(), 0, GoodsModelDesc.length());

				// handler.characters(copBase.getGoodsModelDesc().toCharArray(),
				// 0, copBase.getGoodsModelDesc().length());
				handler.endElement("", "", "GoodsModelDesc");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 申报计量单位
				attr.clear();
				handler.startElement("", "", "WmsDclUnit", attr);
				String WmsDclUnit = hashMap.get("itemnum") == null ? "035" : hashMap.get("itemnum").toString();

				handler.characters(WmsDclUnit.toCharArray(), 0, WmsDclUnit.length());
				// handler.characters(copBase.getWmsDclUnit().toCharArray(), 0,
				// copBase.getWmsDclUnit().length());
				handler.endElement("", "", "WmsDclUnit");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 申报数量
				attr.clear();
				handler.startElement("", "", "WmsDclQty", attr);
				// handler.characters("".toCharArray(), 0, "".length());
				String WmsDclQty = hashMap.get("WMSDCLQTY") == null ? "" : hashMap.get("WMSDCLQTY").toString();

				handler.characters(WmsDclQty.toCharArray(), 0, WmsDclQty.length());
				handler.endElement("", "", "WmsDclQty");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 法定单位
				attr.clear();
				handler.startElement("", "", "WmsLawUnit", attr);

				handler.characters(WmsDclUnit.toCharArray(), 0, WmsDclUnit.length());
				// handler.characters(copBase.getWmsLawUnit().toCharArray(), 0,
				// copBase.getWmsLawUnit().length());
				handler.endElement("", "", "WmsLawUnit");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 法定数量
				attr.clear();
				handler.startElement("", "", "WmsLawQty", attr);
				handler.characters(WmsDclQty.toCharArray(), 0, WmsDclQty.length());
				// handler.characters(copBase.getWmsLawQty().toCharArray(), 0,
				// copBase.getWmsLawQty().length());
				handler.endElement("", "", "WmsLawQty");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 库区号
				attr.clear();
				handler.startElement("", "", "PlaceIds", attr);
				//handler.characters("".toCharArray(), 0, "".length());
				String PlaceIds = hashMap.get("PLACEIDS") == null ? "" : hashMap.get("PLACEIDS").toString();

				handler.characters(PlaceIds.toCharArray(), 0, PlaceIds.length());
				handler.endElement("", "", "PlaceIds");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 库位号
				attr.clear();
				handler.startElement("", "", "LocationIds", attr);
				//handler.characters("".toCharArray(), 0, "".length());
				String LocationIds = hashMap.get("LOCATIONIDS") == null ? "" : hashMap.get("LOCATIONIDS").toString();

				handler.characters(LocationIds.toCharArray(), 0, LocationIds.length());
				handler.endElement("", "", "LocationIds");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 出入库状态 0 预出入库 1在库
				attr.clear();
				handler.startElement("", "", "StockStatus", attr);
				handler.characters("1".toCharArray(), 0, "1".length());
				// handler.characters(copBase.getStockStatus().toCharArray(),
				// 0,copBase.getStockStatus().length());
				handler.endElement("", "", "StockStatus");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 货物类型 0 非保 1 保税
				attr.clear();
				handler.startElement("", "", "GoodsType", attr);
				// handler.characters("0".toCharArray(), 0, "0".length());
			//	String GoodsType = hashMap.get("GOODSTYPE") == null ? "" : hashMap.get("GOODSTYPE").toString();

				if (GoodsType != null && GoodsType.equals("1")) {
					handler.characters("1".toCharArray(), 0, "1".length());
				} else {
					handler.characters("0".toCharArray(), 0, "0".length());
				}
				// handler.characters(copBase.getGoodsType().toCharArray(), 0,
				// copBase.getGoodsType().length());
				handler.endElement("", "", "GoodsType");
				handler.characters(nine.toCharArray(), 0, nine.length());
				handler.endElement("", "", "StockInfo");
				handler.characters(eight.toCharArray(), 0, eight.length());
				
				
				
                }else if (GoodsType.equals("1")){
                	
					//2.保税转一般贸易
					// 1.保税转一般贸易后库中保税
    				attr.clear();
    				handler.startElement("", "", "StockInfo", attr);
    				handler.characters(ten.toCharArray(), 0, ten.length());

    				// 账册号
    				attr.clear();
    				handler.startElement("", "", "EmsNo", attr);

    				handler.characters("T4230W000031".toCharArray(), 0, "T4230W000031".length());
    				handler.endElement("", "", "EmsNo");
    				handler.characters(ten.toCharArray(), 0, ten.length());
    				// 账册序号
    				attr.clear();
    				handler.startElement("", "", "EmsSeqNo", attr);
    				String EmsSeqNo ="";

    				
    				EmsSeqNo= hashMap.get("EMSSEQNO") == null ? "" : hashMap.get("EMSSEQNO").toString();//保税货物有账册商品序号
    			
    				handler.characters(EmsSeqNo.toCharArray(), 0, EmsSeqNo.length());
    				handler.endElement("", "", "EmsSeqNo");
    				handler.characters(ten.toCharArray(), 0, ten.length());
    				// 仓库物料号
    				attr.clear();
    				handler.startElement("", "", "WmsMtsNo", attr);
    				String wmsmtsNO = hashMap.get("WMSMTSNO") == null ? "" : hashMap.get("WMSMTSNO").toString();
    				 handler.characters(wmsmtsNO.toCharArray(), 0, wmsmtsNO.length());
    				handler.endElement("", "", "WmsMtsNo");
    				handler.characters(ten.toCharArray(), 0, ten.length());
    				// 商品料号
    				attr.clear();
    				handler.startElement("", "", "GoodsMtsNo", attr);
    				// handler.characters("".toCharArray(), 0, "".length());
    				String GoodsMtsNo = hashMap.get("GOODSMTSNO") == null ? "" : hashMap.get("GOODSMTSNO").toString();

    				handler.characters(GoodsMtsNo.toCharArray(), 0, GoodsMtsNo.length());
    				handler.endElement("", "", "GoodsMtsNo");
    				handler.characters(ten.toCharArray(), 0, ten.length());
    				// 商品编码
    				attr.clear();
    				handler.startElement("", "", "CodeTs", attr);
    				// handler.characters("".toCharArray(), 0, "".length());
    				String CodeTs = hashMap.get("hscode") == null ? "" : hashMap.get("hscode").toString();

    				handler.characters(CodeTs.toCharArray(), 0, CodeTs.length());
    				handler.endElement("", "", "CodeTs");
    				handler.characters(ten.toCharArray(), 0, ten.length());
    				// 商品名称
    				attr.clear();
    				handler.startElement("", "", "GoodsName", attr);
    				// handler.characters("".toCharArray(), 0, "".length());
    				String GoodsName = hashMap.get("GOODSNAME") == null ? "" : hashMap.get("GOODSNAME").toString();

    				handler.characters(GoodsName.toCharArray(), 0, GoodsName.length());
    				handler.endElement("", "", "GoodsName");
    				handler.characters(ten.toCharArray(), 0, ten.length());
    				// 商品规格型号
    				attr.clear();
    				handler.startElement("", "", "GoodsModelDesc", attr);
    				String GoodsModelDesc=hashMap.get("TYPESIZE") == null ? "" : hashMap.get("TYPESIZE").toString();
    				handler.characters(GoodsModelDesc.toCharArray(), 0, GoodsModelDesc.length());

    				// handler.characters(copBase.getGoodsModelDesc().toCharArray(),
    				// 0, copBase.getGoodsModelDesc().length());
    				handler.endElement("", "", "GoodsModelDesc");
    				handler.characters(ten.toCharArray(), 0, ten.length());
    				// 申报计量单位
    				attr.clear();
    				handler.startElement("", "", "WmsDclUnit", attr);
    				String WmsDclUnit = hashMap.get("itemnum") == null ? "035" : hashMap.get("itemnum").toString();

    				handler.characters(WmsDclUnit.toCharArray(), 0, WmsDclUnit.length());
    				handler.endElement("", "", "WmsDclUnit");
    				handler.characters(ten.toCharArray(), 0, ten.length());
    				// 申报数量
    				attr.clear();
    				handler.startElement("", "", "WmsDclQty", attr);
    				handler.characters(ZKTRADE.toCharArray(), 0, ZKTRADE.length());
    				handler.endElement("", "", "WmsDclQty");
    				handler.characters(ten.toCharArray(), 0, ten.length());
    				// 法定单位
    				attr.clear();
    				handler.startElement("", "", "WmsLawUnit", attr);

    				handler.characters(WmsDclUnit.toCharArray(), 0, WmsDclUnit.length());
    				handler.endElement("", "", "WmsLawUnit");
    				handler.characters(ten.toCharArray(), 0, ten.length());
    				// 法定数量
    				attr.clear();
    				handler.startElement("", "", "WmsLawQty", attr);
    				handler.characters(ZKTRADE.toCharArray(), 0, ZKTRADE.length());
    				handler.endElement("", "", "WmsLawQty");
    				handler.characters(ten.toCharArray(), 0, ten.length());
    				// 库区号
    				attr.clear();
    				handler.startElement("", "", "PlaceIds", attr);
    				String PlaceIds = hashMap.get("PLACEIDS") == null ? "" : hashMap.get("PLACEIDS").toString();

    				handler.characters(PlaceIds.toCharArray(), 0, PlaceIds.length());
    				handler.endElement("", "", "PlaceIds");
    				handler.characters(ten.toCharArray(), 0, ten.length());
    				// 库位号
    				attr.clear();
    				handler.startElement("", "", "LocationIds", attr);
    				String LocationIds = hashMap.get("LOCATIONIDS") == null ? "" : hashMap.get("LOCATIONIDS").toString();

    				handler.characters(LocationIds.toCharArray(), 0, LocationIds.length());
    				handler.endElement("", "", "LocationIds");
    				handler.characters(ten.toCharArray(), 0, ten.length());
    				// 出入库状态 0 预出入库 1在库
    				attr.clear();
    				handler.startElement("", "", "StockStatus", attr);
    				handler.characters("1".toCharArray(), 0, "1".length());
    				handler.endElement("", "", "StockStatus");
    				handler.characters(ten.toCharArray(), 0, ten.length());
    				// 货物类型 0 非保 1 保税
    				attr.clear();
    				handler.startElement("", "", "GoodsType", attr);

    				handler.characters("1".toCharArray(), 0, "1".length());
    				handler.endElement("", "", "GoodsType");
    				handler.characters(nine.toCharArray(), 0, nine.length());
    				handler.endElement("", "", "StockInfo");
    				handler.characters(eight.toCharArray(), 0, eight.length());
    				


					// 1.保税转一般贸易后库中一般贸易
    				attr.clear();
    				handler.startElement("", "", "StockInfo", attr);
    				handler.characters(ten.toCharArray(), 0, ten.length());

    				// 账册号
    				attr.clear();
    				handler.startElement("", "", "EmsNo", attr);
    				handler.characters("".toCharArray(), 0, "".length());
    				handler.endElement("", "", "EmsNo");
    				handler.characters(ten.toCharArray(), 0, ten.length());
    				// 账册序号
    				attr.clear();
    				handler.startElement("", "", "EmsSeqNo", attr);   				   			
    				handler.characters("".toCharArray(), 0, "".length());
    				handler.endElement("", "", "EmsSeqNo");
    				handler.characters(ten.toCharArray(), 0, ten.length());
    				// 仓库物料号
    				attr.clear();
    				handler.startElement("", "", "WmsMtsNo", attr);
    				wmsmtsNO=wmsmtsNO+"YB";
    				 handler.characters(wmsmtsNO.toCharArray(), 0, wmsmtsNO.length());
    				handler.endElement("", "", "WmsMtsNo");
    				handler.characters(ten.toCharArray(), 0, ten.length());
    				// 商品料号
    				attr.clear();
    				handler.startElement("", "", "GoodsMtsNo", attr);
    				// handler.characters("".toCharArray(), 0, "".length());

    				handler.characters(GoodsMtsNo.toCharArray(), 0, GoodsMtsNo.length());
    				handler.endElement("", "", "GoodsMtsNo");
    				handler.characters(ten.toCharArray(), 0, ten.length());
    				// 商品编码
    				attr.clear();
    				handler.startElement("", "", "CodeTs", attr);
    				// handler.characters("".toCharArray(), 0, "".length());
    				handler.characters(CodeTs.toCharArray(), 0, CodeTs.length());
    				handler.endElement("", "", "CodeTs");
    				handler.characters(ten.toCharArray(), 0, ten.length());
    				// 商品名称
    				attr.clear();
    				handler.startElement("", "", "GoodsName", attr);
    				// handler.characters("".toCharArray(), 0, "".length());
    				handler.characters(GoodsName.toCharArray(), 0, GoodsName.length());
    				handler.endElement("", "", "GoodsName");
    				handler.characters(ten.toCharArray(), 0, ten.length());
    				// 商品规格型号
    				attr.clear();
    				handler.startElement("", "", "GoodsModelDesc", attr);
    				 GoodsModelDesc=hashMap.get("TYPESIZE") == null ? "" : hashMap.get("TYPESIZE").toString();
    				handler.characters(GoodsModelDesc.toCharArray(), 0, GoodsModelDesc.length());

    				// handler.characters(copBase.getGoodsModelDesc().toCharArray(),
    				// 0, copBase.getGoodsModelDesc().length());
    				handler.endElement("", "", "GoodsModelDesc");
    				handler.characters(ten.toCharArray(), 0, ten.length());
    				// 申报计量单位
    				attr.clear();
    				handler.startElement("", "", "WmsDclUnit", attr);
    				handler.characters(WmsDclUnit.toCharArray(), 0, WmsDclUnit.length());
    				handler.endElement("", "", "WmsDclUnit");
    				handler.characters(ten.toCharArray(), 0, ten.length());
    				// 申报数量
    				attr.clear();
    				handler.startElement("", "", "WmsDclQty", attr);
    				handler.characters(ZKFOWARD.toCharArray(), 0, ZKFOWARD.length());
    				handler.endElement("", "", "WmsDclQty");
    				handler.characters(ten.toCharArray(), 0, ten.length());
    				// 法定单位
    				attr.clear();
    				handler.startElement("", "", "WmsLawUnit", attr);

    				handler.characters(WmsDclUnit.toCharArray(), 0, WmsDclUnit.length());
    				handler.endElement("", "", "WmsLawUnit");
    				handler.characters(ten.toCharArray(), 0, ten.length());
    				// 法定数量
    				attr.clear();
    				handler.startElement("", "", "WmsLawQty", attr);
    				handler.characters(ZKFOWARD.toCharArray(), 0, ZKFOWARD.length());
    				handler.endElement("", "", "WmsLawQty");
    				handler.characters(ten.toCharArray(), 0, ten.length());
    				// 库区号
    				attr.clear();
    				handler.startElement("", "", "PlaceIds", attr);
    				String ZKPLACEIDS= hashMap.get("ZKPLACEIDS") == null ? "" : hashMap.get("ZKPLACEIDS").toString();

    				handler.characters(ZKPLACEIDS.toCharArray(), 0, ZKPLACEIDS.length());
    				handler.endElement("", "", "PlaceIds");
    				handler.characters(ten.toCharArray(), 0, ten.length());
    				// 库位号
    				attr.clear();
    				handler.startElement("", "", "LocationIds", attr);
    				String ZKLOCATIONIDS = hashMap.get("ZKLOCATIONIDS") == null ? "" : hashMap.get("ZKLOCATIONIDS").toString();

    				handler.characters(ZKLOCATIONIDS.toCharArray(), 0, ZKLOCATIONIDS.length());
    				handler.endElement("", "", "LocationIds");
    				handler.characters(ten.toCharArray(), 0, ten.length());
    				// 出入库状态 0 预出入库 1在库
    				attr.clear();
    				handler.startElement("", "", "StockStatus", attr);
    				handler.characters("1".toCharArray(), 0, "1".length());
    				handler.endElement("", "", "StockStatus");
    				handler.characters(ten.toCharArray(), 0, ten.length());
    				// 货物类型 0 非保 1 保税
    				attr.clear();
    				handler.startElement("", "", "GoodsType", attr);

    				handler.characters("0".toCharArray(), 0, "0".length());
    				handler.endElement("", "", "GoodsType");
    				handler.characters(nine.toCharArray(), 0, nine.length());
    				handler.endElement("", "", "StockInfo");
    				handler.characters(eight.toCharArray(), 0, eight.length());
                 	
                	
                	
                }

			}
			// 经营单位代码
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
			// 生成日期
			attr.clear();
			handler.startElement("", "", "ApplyDate", attr);
			handler.characters(formatbd.format(time).toCharArray(), 0, formatbd.format(time).length());
			handler.endElement("", "", "ApplyDate");
			handler.characters(eight.toCharArray(), 0, eight.length());
/*		 for(HashMap hashMap :list){ 

			attr.clear();
			handler.startElement("", "", "SasStockInfo", attr);
			handler.characters(ten.toCharArray(), 0, ten.length());

			// 账册号
			attr.clear();
			handler.startElement("", "", "EmsNo", attr);
			//handler.characters("".toCharArray(), 0, "".length());
			handler.characters("".toCharArray(), 0, "".length());
			handler.endElement("", "", "EmsNo");
			handler.characters(ten.toCharArray(), 0, ten.length());
			// 账册序号
			attr.clear();
			handler.startElement("", "", "EmsSeqNo", attr);
			handler.characters("".toCharArray(), 0, "".length());
		    
			handler.endElement("", "", "EmsSeqNo");
			handler.characters(ten.toCharArray(), 0, ten.length());
			// 商品编码
			attr.clear();
			handler.startElement("", "", "CodeTs", attr);
			handler.characters("".toCharArray(), 0, "".length());
		    
			handler.endElement("", "", "CodeTs");
			handler.characters(eight.toCharArray(), 0, eight.length());

			handler.endElement("", "", "SasStockInfo");
			handler.characters(eight.toCharArray(), 0, eight.length());
		    }*/
		 
			handler.endElement("", "  ", "StockRecordMessage");
			handler.characters(eight.toCharArray(), 0, eight.length());
			
			handler.endElement("", "  ", "SDEPORT_DATA");
			handler.characters(eight.toCharArray(), 0, eight.length());
			// Xml类型
			attr.clear();
			handler.startElement("", "", "XML_TYPE", attr);
			String XMLTYPE = "W3C";
			handler.characters(XMLTYPE.toCharArray(), 0, XMLTYPE.length());
			handler.endElement("", "", "XML_TYPE");
		//	handler.characters(eight.toCharArray(), 0, eight.length());

			handler.endElement("", "  ", "XMLObject");

			// 关闭document
			handler.endDocument();
			///文件上传FTP
			System.out.println("生成.FLJGRX成功");
			String ftpHost="10.135.252.42";
		    String ftpUserName = "yzh";
		    String ftpPassword = "Eimskip0804";
		    fileName=fileName+".FLJGRX";
		    int ftpPort = 21;
		    String ftpPath = "Send/";
	    FileInputStream in=new FileInputStream(f);
	//	uploadFile(ftpHost,ftpUserName, ftpUserName, ftpPort, ftpPath, fileName, in);
		 f.delete();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("生成.FLJGRX失败");
		}

	}
	
	/**
	 * 监管作业场所高风险动植物及其产品数据申报报文
	 */
	@ResponseBody
	public void DeclarationMessage(){


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
			SimpleDateFormat formatdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat formatbd = new SimpleDateFormat("yyyyMMdd");
			String fileName = "01"+"CNQGD42SZ01"+format.format(time); // 获取系统当前时间并将其转换为string类型,fileName即文件名

			File file = new File("D:/supervision");
			if (!file.isDirectory()) {// 路径文件夹是否存在，不存在则创建
				file.mkdirs();
			}
			// 5、创建一个Result对象
			File f = new File("D:/supervision/" + fileName + ".xml");
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

			
			int aa = 01;
			List<HashMap> list = new ArrayList<>();
			ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
			SupervisionService supervisionService = (SupervisionService) applicationContext
					.getBean("supervisionService");
			list = supervisionService.declarationMessage(aa);

			List<List<HashMap>> listall=splitList(list, 10);
			
			
			// 组建参数
			handler.startElement("", "  ", "HriskCargoDeclareInfo", attr);
			handler.characters(eight.toCharArray(), 0, eight.length());
		
			handler.startElement("", "  ", "Head", attr);
			handler.characters(nine.toCharArray(), 0, nine.length());

			// 报文编码
			attr.clear();
			handler.startElement("", "", "MsgId", attr);
			String MsgId = "01"+"CNQGD42SZ01"+format.format(time);
			handler.characters(MsgId.toCharArray(), 0, MsgId.length());
			handler.endElement("", "", "MsgId");
			handler.characters(ten.toCharArray(), 0, ten.length());

			// 报文类型
			attr.clear();
			handler.startElement("", "", "MsgType", attr);
			String MsgType = "CSA03";
			handler.characters(MsgType.toCharArray(), 0, MsgType.length());
			handler.endElement("", "", "MsgType");
			handler.characters(ten.toCharArray(), 0, ten.length());

			// 关卡代码
			attr.clear();
			handler.startElement("", "", "CustomsCode", attr);
			String CustomsCode = "4230";
			handler.characters(CustomsCode.toCharArray(), 0, CustomsCode.length());
			handler.endElement("", "", "CustomsCode");
			handler.characters(ten.toCharArray(), 0, ten.length());

			// 作业场所编号
			attr.clear();
			handler.startElement("", "", "SupvLoctCode", attr);
			String SupvLoctCode = "CNQGD42SZ01";
			handler.characters(SupvLoctCode.toCharArray(), 0, SupvLoctCode.length());
			handler.endElement("", "", "SupvLoctCode");
			handler.characters(ten.toCharArray(), 0, ten.length());

			// 报文发送时间
			attr.clear();
			handler.startElement("", "", "DeclDate", attr);
			handler.characters(formatdate.format(time).toCharArray(), 0, formatdate.format(time).length());
			handler.endElement("", "", "DeclDate");
			handler.characters(ten.toCharArray(), 0, ten.length());

			// 申报数据类型
			attr.clear();
			handler.startElement("", "", "DeclareDataType", attr);
			String DeclareDataType = "0";
			handler.characters(DeclareDataType.toCharArray(), 0, DeclareDataType.length());
			handler.endElement("", "", "DeclareDataType");
			handler.characters(ten.toCharArray(), 0, ten.length());

			// 申报数据报文总数
			attr.clear();
			handler.startElement("", "", "TotalMsgNo", attr);
			String TotalMsgNo = "1";
			handler.characters(TotalMsgNo.toCharArray(), 0, TotalMsgNo.length());
			handler.endElement("", "", "TotalMsgNo");
			handler.characters(ten.toCharArray(), 0, ten.length());

			// 当前报文序号
			attr.clear();
			handler.startElement("", "", "CurMsgNo", attr);
			String CurMsgNo = "1";
			handler.characters(CurMsgNo.toCharArray(), 0, CurMsgNo.length());
			handler.endElement("", "", "CurMsgNo");
			handler.characters(ten.toCharArray(), 0, ten.length());
	
			handler.endElement("", "  ", "Head");
			handler.characters(nine.toCharArray(), 0, nine.length());

			
			//

			handler.startElement("", "", "Declaration", attr);
			handler.characters(nine.toCharArray(), 0, nine.length());


			for (HashMap hashMap : list) {
				
				attr.clear();
				handler.startElement("", "", "Data", attr);
				handler.characters(ten.toCharArray(), 0, ten.length());

				// 商品类别
				attr.clear();
				handler.startElement("", "", "GoodsClassify", attr);
				String GoodsClassify=hashMap.get("GoodsClassify").toString();
				if (GoodsClassify.equals("肉类")) {
					GoodsClassify="01";
				}else if (GoodsClassify.equals("水产类")){
					GoodsClassify="02";
				}else if (GoodsClassify.equals("果蔬类")){
					GoodsClassify="04";
				}			
				handler.characters(GoodsClassify.toCharArray(), 0, GoodsClassify.length());
				handler.endElement("", "", "GoodsClassify");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 商品子类
				attr.clear();
				handler.startElement("", "", "GoodsSubclass", attr);
				String GoodsSubclass =hashMap.get("GoodsSubclass").toString();
				if (GoodsClassify.equals("01")) {
					if (GoodsSubclass.endsWith("猪肉")) {
						GoodsSubclass="01001";
					}else if (GoodsSubclass.equals("羊肉")){
						GoodsSubclass="01003";
					}else if (GoodsSubclass.equals("牛肉")){
						GoodsSubclass="01004";
					}else if (GoodsSubclass.equals("鸡肉")){
						GoodsSubclass="01005";
					}else{
						GoodsSubclass="01006";
					}					
				}else{
					GoodsSubclass="";
				}
				handler.characters(GoodsSubclass.toCharArray(), 0, GoodsSubclass.length());
				handler.endElement("", "", "GoodsSubclass");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 商品编号
				attr.clear();
				handler.startElement("", "", "GoodsCode", attr);
				String GoodsCode = hashMap.get("GoodsCode") == null ? "" : hashMap.get("GoodsCode").toString();
				handler.characters(GoodsCode.toCharArray(), 0, GoodsCode.length());
				handler.endElement("", "", "GoodsCode");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 商品名称
				attr.clear();
				handler.startElement("", "", "GoodsName", attr);
				// handler.characters("".toCharArray(), 0, "".length());
				String GoodsName = hashMap.get("GoodsName") == null ? "" : hashMap.get("GoodsName").toString();

				handler.characters(GoodsName.toCharArray(), 0, GoodsName.length());
				handler.endElement("", "", "GoodsName");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 规格型号
				attr.clear();
				handler.startElement("", "", "GoodsModel", attr);
				// handler.characters("".toCharArray(), 0, "".length());
				handler.characters("".toCharArray(), 0, "".length());
				handler.endElement("", "", "GoodsModel");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 数量
				attr.clear();
				handler.startElement("", "", "Quantity", attr);
				// handler.characters("".toCharArray(), 0, "".length());
				String Quantity = hashMap.get("Quantity") == null ? "" : hashMap.get("Quantity").toString();

				handler.characters(Quantity.toCharArray(), 0, Quantity.length());
				handler.endElement("", "", "Quantity");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 单位
				attr.clear();
				handler.startElement("", "", "Unit", attr);
				handler.characters("Kg".toCharArray(), 0, "Kg".length());
				// handler.characters(copBase.getGoodsModelDesc().toCharArray(),
				// 0, copBase.getGoodsModelDesc().length());
				handler.endElement("", "", "Unit");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 集装箱号
				attr.clear();
				handler.startElement("", "", "ContaId", attr);
				handler.characters("".toCharArray(), 0, "".length());
				// handler.characters(copBase.getWmsDclUnit().toCharArray(), 0,
				// copBase.getWmsDclUnit().length());
				handler.endElement("", "", "ContaId");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 进场时间
				attr.clear();
				handler.startElement("", "", "EntranceDate", attr);
				// handler.characters("".toCharArray(), 0, "".length());
			//	String EntranceDate = hashMap.get("EntranceDate") == null ? " " : hashMap.get("EntranceDate").toString();
			//	EntranceDate=EntranceDate+" 00:00:00";
				handler.characters("".toCharArray(), 0, "".length());
				handler.endElement("", "", "EntranceDate");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 数据处理标识
				attr.clear();
				handler.startElement("", "", "DataDealFlag", attr);
                String DataDealFlag="A";
				handler.characters(DataDealFlag.toCharArray(), 0, DataDealFlag.length());
				// handler.characters(copBase.getWmsLawUnit().toCharArray(), 0,
				// copBase.getWmsLawUnit().length());
				handler.endElement("", "", "DataDealFlag");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 提单号
				attr.clear();
				handler.startElement("", "", "BillNo", attr);
				String BillNo= hashMap.get("BillNo") == null ? "" : hashMap.get("BillNo").toString();;
				handler.characters(BillNo.toCharArray(), 0, BillNo.length());
				// handler.characters(copBase.getWmsLawQty().toCharArray(), 0,
				// copBase.getWmsLawQty().length());
				handler.endElement("", "", "BillNo");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 报关单号
				attr.clear();
				handler.startElement("", "", "EntryId", attr);
				//handler.characters("".toCharArray(), 0, "".length());
				String EntryId = hashMap.get("EntryId") == null ? "" : hashMap.get("EntryId").toString();

				handler.characters(EntryId.toCharArray(), 0, EntryId.length());
				handler.endElement("", "", "EntryId");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 转关单号
				attr.clear();
				handler.startElement("", "", "PreNo", attr);
				//handler.characters("".toCharArray(), 0, "".length());

				handler.characters("".toCharArray(), 0, "".length());
				handler.endElement("", "", "PreNo");
				handler.characters(ten.toCharArray(), 0, ten.length());
				//多式联运单号
				attr.clear();
				handler.startElement("", "", "MtApplyBlNo", attr);
				handler.characters("".toCharArray(), 0, "".length());
				// handler.characters(copBase.getStockStatus().toCharArray(),
				// 0,copBase.getStockStatus().length());
				handler.endElement("", "", "MtApplyBlNo");
				handler.characters(ten.toCharArray(), 0, ten.length());
				// 备注
				attr.clear();
				handler.startElement("", "", "Remark", attr);
				 String Remark = "";
				handler.characters(Remark.toCharArray(), 0, Remark.length());			   
				handler.endElement("", "", "Remark");
				handler.characters(ten.toCharArray(), 0, ten.length());

				handler.endElement("", "", "Data");
				handler.characters(nine.toCharArray(), 0, nine.length());
				
				
				
                

			}
		 
			handler.endElement("", "  ", "Declaration");
			handler.characters(eight.toCharArray(), 0, eight.length());
					//	handler.characters(eight.toCharArray(), 0, eight.length());

			handler.endElement("", "  ", "HriskCargoDeclareInfo");

			// 关闭document
			handler.endDocument();
			///文件上传FTP
			System.out.println("生成.FLJGRX成功");
			String ftpHost="10.135.252.42";
		    String ftpUserName = "yzh";
		    String ftpPassword = "Eimskip0804";
		    fileName=fileName+".FLJGRX";
		    int ftpPort = 21;
		    String ftpPath = "Send/";
	    FileInputStream in=new FileInputStream(f);
		//uploadFile(ftpHost,ftpUserName, ftpUserName, ftpPort, ftpPath, fileName, in);
		 f.delete();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("生成.FLJGRX失败");
		}

	
	}
	
	
	

	
	
    /**  
     * Description:             向FTP服务器上传文件  
     * @param host              FTP服务器hostname  
     * @param port              FTP服务器端口  
     * @param username          FTP登录账号  
     * @param password          FTP登录密码  
     * @param basePath          FTP服务器基础目录 
     * @param filePath          FTP服务器文件存放路径。例如分日期存放：/2015/01/01。文件的路径为basePath+filePath 
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
     * @param ftpPassword   FTP 登录密码 
     * @param ftpUserName   FTP登录用户名 
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
    
    private List<List<HashMap>> splitList(List<HashMap> list , int groupSize){
        int length = list.size();
        // 计算可以分成多少组
        int num = ( length + groupSize - 1 )/groupSize ; // TODO 
        List<List<HashMap>> newList = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            // 开始位置
            int fromIndex = i * groupSize;
            // 结束位置
            int toIndex = (i+1) * groupSize < length ? ( i+1 ) * groupSize : length ;
            newList.add(list.subList(fromIndex,toIndex)) ;
        }
        return  newList ;
    }
    
    

	
}
