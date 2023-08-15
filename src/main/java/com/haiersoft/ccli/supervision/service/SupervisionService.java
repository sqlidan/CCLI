package com.haiersoft.ccli.supervision.service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.supervision.dao.SupervisionDao;
import com.haiersoft.ccli.supervision.entity.CopBaseInfo;
import com.haiersoft.ccli.supervision.web.FTPUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.helpers.AttributesImpl;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SupervisionService extends BaseService<CopBaseInfo, Integer> {
	@Autowired
	private SupervisionDao supervisionDao;

	@Override
	public HibernateDao<CopBaseInfo, Integer> getEntityDao() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<CopBaseInfo> getBaseinfo(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public void createXml() {
		List<HashMap> list = new ArrayList<>();
		int aa = 01;
		list = searchStockReport(aa);

		// 1、创建一个SAXTransformerFactory类的对象
		SAXTransformerFactory tff = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
		List<List<HashMap>> groupList = splitList(list, 900);
		int i = 1;
		for (List<HashMap> group : groupList) {
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
				String FILEORDER = String.valueOf(i);
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

				//	System.out.println(list);
				for (HashMap hashMap : group) {
					// CopBaseInfo copBase = (CopBaseInfo) list.get(i);
					String ZKTRADE = hashMap.get("ZKTRADE") == null ? "" : hashMap.get("ZKTRADE").toString();//在库保税
					String ZKFOWARD = hashMap.get("ZKFORWARD") == null ? "" : hashMap.get("ZKFORWARD").toString();//在库一般贸易
					String GoodsType = hashMap.get("GOODSTYPE") == null ? "" : hashMap.get("GOODSTYPE").toString();
					//            if ((ZKTRADE==null&&ZKFOWARD==null)||(ZKTRADE.equals("")&&ZKFOWARD.equals(""))) {


					attr.clear();
					handler.startElement("", "", "StockInfo", attr);
					handler.characters(ten.toCharArray(), 0, ten.length());

					// 账册号
					attr.clear();
					handler.startElement("", "", "EmsNo", attr);
					String EmsNo = "";
					if (GoodsType.equals("1")) {
						EmsNo = "T4230W000036";//如果是保税货物，账册号固定
					} else {
						EmsNo = "";
					}

					handler.characters(EmsNo.toCharArray(), 0, EmsNo.length());
					handler.endElement("", "", "EmsNo");
					handler.characters(ten.toCharArray(), 0, ten.length());
					// 账册序号
					attr.clear();
					handler.startElement("", "", "EmsSeqNo", attr);
					String EmsSeqNo = "";

					if (GoodsType.equals("1")) {
						EmsSeqNo = hashMap.get("EMSSEQNO") == null ? "" : hashMap.get("EMSSEQNO").toString();//保税货物有账册商品序号
					}
					handler.characters(EmsSeqNo.toCharArray(), 0, EmsSeqNo.length());
					handler.endElement("", "", "EmsSeqNo");
					handler.characters(ten.toCharArray(), 0, ten.length());
					// 仓库物料号
					attr.clear();
					handler.startElement("", "", "WmsMtsNo", attr);
					String wmsmtsNO = hashMap.get("WMSMTSNO") == null ? "" : hashMap.get("WMSMTSNO").toString();
					wmsmtsNO = bSubstring(wmsmtsNO, 29);
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
					String GoodsModelDesc = hashMap.get("TYPESIZE") == null ? "" : hashMap.get("TYPESIZE").toString();
					handler.characters(GoodsModelDesc.toCharArray(), 0, GoodsModelDesc.length());
					// handler.characters(copBase.getGoodsModelDesc().toCharArray(),
					// 0, copBase.getGoodsModelDesc().length());
					handler.endElement("", "", "GoodsModelDesc");
					handler.characters(ten.toCharArray(), 0, ten.length());
					// 申报计量单位
					attr.clear();
					handler.startElement("", "", "WmsDclUnit", attr);
					String DclUnit = hashMap.get("DCLUNIT") == null ? "035" : hashMap.get("DCLUNIT").toString();

					handler.characters(DclUnit.toCharArray(), 0, DclUnit.length());
					// handler.characters(copBase.getWmsDclUnit().toCharArray(), 0,
					// copBase.getWmsDclUnit().length());
					handler.endElement("", "", "WmsDclUnit");
					handler.characters(ten.toCharArray(), 0, ten.length());
					// 申报数量
					attr.clear();
					handler.startElement("", "", "WmsDclQty", attr);
					// handler.characters("".toCharArray(), 0, "".length());
					String DclQty = hashMap.get("DCLQTY") == null ? "035" : hashMap.get("DCLQTY").toString();

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
					if (null == PlaceIds || " ".equals(PlaceIds)) {
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
					if (null == LocationIds || " ".equals(LocationIds)) {
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
				String ftpHost = "10.135.252.42";
				String ftpUserName = "yzh";
				String ftpPassword = "Eimskip0804";
				fileName = fileName + ".FLJGRX";
				int ftpPort = 21;
				String ftpPath = "/Send/";
				FileInputStream in = new FileInputStream(f);
				//uploadFile(ftpHost,ftpUserName, ftpUserName, ftpPort, ftpPath, fileName, in);
				FTPUtils ftp = FTPUtils.getInstance();
				boolean flag = ftp.uploadFile(ftpPath, fileName, in);
				//   System.out.println("上传文件是否成功：" + flag);
				f.delete();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("生成.FLJGRX失败");
			}
			i++;
		}

	}

	public static String bSubstring(String str, int subSLength) {
		String subStr = "";
		try {
			if (str == null) return "";
			else {
				int tempSubLength = subSLength;//截取字节数
				subStr = str.substring(0, str.length() < subSLength ? str.length() : subSLength);//截取的子串
				int subStrByetsL = subStr.getBytes("GBK").length;//截取子串的字节长度
				// 说明截取的字符串中包含有汉字
				while (subStrByetsL > tempSubLength) {
					int subSLengthTemp = --subSLength;
					subStr = str.substring(0, subSLengthTemp > str.length() ? str.length() : subSLengthTemp);
					subStrByetsL = subStr.getBytes("GBK").length;
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return subStr;
	}

	/**
	 * Description:             向FTP服务器上传文件
	 *
	 * @param                  FTP服务器hostname
	 * @param                  FTP服务器端口
	 * @param          FTP登录账号
	 * @param                  FTP登录密码
	 * @param                  FTP服务器基础目录
	 * @param                  FTP服务器文件存放路径。例如分日期存放：/2015/01/01。文件的路径为basePath+filePath
	 * @param filename                  上传到FTP服务器上的文件名
	 * @param input                        输入流
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
	 *
	 * @param ftpHost     FTP主机服务器
	 * @param ftpPassword FTP 登录密码
	 * @param ftpUserName FTP登录用户名
	 * @param ftpPort     FTP端口 默认为21
	 * @return
	 */
	public static FTPClient getFTPClient(String ftpHost, String ftpUserName, String ftpPassword, int ftpPort) throws IOException {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient = new FTPClient();
			ftpClient.connect(ftpHost, ftpPort);              // 连接FTP服务器
			ftpClient.login(ftpUserName, ftpPassword);// 登陆FTP服务器
			ftpClient.setControlEncoding("UTF-8"); // 中文支持
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			//ftpClient.enterLocalPassiveMode();
			if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				System.out.println("未连接到FTP，用户名或密码错误。");
				//logger.info();
				ftpClient.disconnect();
			} else {
				System.out.println("FTP连接成功。");
				// logger.info("");
			}
		} catch (Exception e) {
			e.printStackTrace();

			System.out.println("FTP的IP地址可能错误，请正确配置。");
		}
		return ftpClient;
	}


	 private List<List<HashMap>> splitList(List<HashMap> list, int groupSize) {
		int length = list.size();
		// 计算可以分成多少组
		int num = (length + groupSize - 1) / groupSize; // TODO
		List<List<HashMap>> newList = new ArrayList<>(num);
		for (int i = 0; i < num; i++) {
			// 开始位置
			int fromIndex = i * groupSize;
			// 结束位置
			int toIndex = (i + 1) * groupSize < length ? (i + 1) * groupSize : length;
			newList.add(list.subList(fromIndex, toIndex));
		}
		return newList;
	}

	/*public static void main(String[] args) {
		List<HashMap> list = new ArrayList<>();
		SAXTransformerFactory tff = (SAXTransformerFactory) SAXTransformerFactory.newInstance();

		HashMap map;
		for (int i = 1; i < 100; i++) {
			map = new HashMap();
			map.put("index", i);
			list.add(map);
		}
		List<List<HashMap>> groupList = splitList(list, 200);
		int i = 1;
		for (List<HashMap> group : groupList) {


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
				String FILEORDER = String.valueOf(i);
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

				*//*
				 * CopBaseInfo copBaseInfo = supervisionService.getBaseinfo(id);
				 *
				 *
				 * List<CopBaseInfo> list=supervisionService.getBaseinfo();
				 *//*


				//	System.out.println(list);
				int c = 0;
				for (HashMap hashMap : group) {
					// CopBaseInfo copBase = (CopBaseInfo) list.get(i);


					handler.endElement("", "", "GoodsType");
					handler.characters((i + "_" + String.valueOf(c)).toCharArray(), 0, (i + "_" + String.valueOf(c)).length());
					handler.endElement("", "", "GoodsType");
					handler.characters(eight.toCharArray(), 0, eight.length());
//
//
//
//                }
					c++;

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
*//*		 for(HashMap hashMap :list){

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
		    }*//*

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

				//   System.out.println("上传文件是否成功：" + flag);
				//f.delete();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("生成.FLJGRX失败");
			}
			i++;
		}
		System.out.println(1111);
	}*/

	/**
	 * 当前在库库存
	 *
	 * @param aa
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<HashMap> searchStockReport(int aa) {
		// TODO Auto-generated method stub
		return supervisionDao.searchStockReport(aa);
	}

	@SuppressWarnings("rawtypes")
	public List<HashMap> UpdateStockReport(int aa) {
		// TODO Auto-generated method stub
		return supervisionDao.UpdateStockReport(aa);
	}

	public List<HashMap> declarationMessage(int aa) {
		// TODO Auto-generated method stub
		return supervisionDao.declarationMessage(aa);
	}


}
