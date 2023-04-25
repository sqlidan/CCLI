package com.haiersoft.ccli.wms.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.haiersoft.ccli.base.entity.BaseSkuBaseInfo;
import com.haiersoft.ccli.base.service.SkuInfoService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.PropertiesUtil;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.BaseClientPledge;
import com.haiersoft.ccli.wms.entity.BisTransferStock;
import com.haiersoft.ccli.wms.entity.BisTransferStockInfo;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import com.haiersoft.ccli.wms.service.ClientPledgeService;
import com.haiersoft.ccli.wms.service.TransferInfoService;
import com.haiersoft.ccli.wms.service.TransferService;
import com.haiersoft.ccli.wms.service.TrayInfoService;
/**
 * 货转单明细
 * @author LZG
 *
 */
@Controller
@RequestMapping("bis/transferinfo")
public class TransferInfoContorller extends BaseController {
	@Autowired
	private TransferService transferService;
	@Autowired
	private TransferInfoService transferInfoService;
	@Autowired
	private TrayInfoService trayInfoService;
	@Autowired
	private ClientPledgeService clientPledgeService;
	@Autowired
	private SkuInfoService skuInfoService;
	 
	/*
	 * 列表页面table获取json
	 * */
	@RequestMapping(value="listjson",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<BisTransferStockInfo> page = getPage(request);
		page.orderBy("crTime").order(Page.DESC);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = transferInfoService.search(page, filters);
		return getEasyUIData(page);
	}
	/*
	 * 列表页面table获取json
	 * */
	@RequestMapping(value="alljson",method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String,Object>> getJosnData(HttpServletRequest request) {
		String getOrderId=request.getParameter("transferId");
		List<Map<String,Object>> getList = transferInfoService.findInfoList(getOrderId);
		if(getList==null){
			getList=new ArrayList<Map<String,Object>>(); 
		}
		return getList;
	}
	
	/**
	 *  java实现文件下载功能代码
	 *  创建时间：2014年12月23日
	 * @version
	 */
	@RequestMapping(value = "download", method = RequestMethod.POST)
	@ResponseBody
	public void fileDownLoad(HttpServletRequest request, HttpServletResponse response) throws ServletException {
			  String fileName = "货转联系单明细导入模板.xls";
			  String  filePath=PropertiesUtil.getPropertiesByName("downloadexcel", "application");
 			  String  realpath = filePath + "WEB-INF\\classes\\importExcel\\货转联系单明细导入模板.xls";
//			  String  realpath = filePath + "classes\\importExcel\\货转联系单明细导入模板.xls";
			  BufferedInputStream bis = null;
			  BufferedOutputStream bos = null;
			  OutputStream fos = null;
			  InputStream fis = null;
			  try {
			   response.setContentType("application/vnd.ms-excel");
		       String formatFileName = new String(fileName.getBytes("GB2312"),"ISO-8859-1");
//			   response.setHeader("Content-disposition", "attachment;filename=\"" + URLEncoder.encode(fileName, "UTF-8") +"\"");
		       response.setHeader("Content-disposition", "attachment;filename=\"" + formatFileName +"\"");
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
	 * 货转联系单明细excel导入跳转
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "into/{transferId}", method = RequestMethod.GET)
	public String intoExcel( Model model,@PathVariable("transferId") String transferId) {
		model.addAttribute("transferId", transferId);
		model.addAttribute("action", "intot");
		return "wms/transfer/transferInfoInto";
	}
	
	/**
	 * 货转联系单明细excel导入 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "intot/{transferId}", method = RequestMethod.POST)
	@ResponseBody
	public String intoExcel2(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, Model model,@PathVariable("transferId") String transferId) {
		String ex = "";
		String more = "";
		String tray = "";
		try {
			ImportParams params = new ImportParams();  
			params.setTitleRows(1);
			List<BisTransferStockInfo> list = ExcelImportUtil.importExcel(file.getInputStream(), BisTransferStockInfo.class, params);
			System.out.println(list.size());
			BisTransferStockInfo newObj=null;
			BaseSkuBaseInfo skuObj = null;
			User user = UserUtil.getCurrentUser();
			Map<String,Object> params2 = new HashMap<String,Object>();
			List<TrayInfo> trayInfo = null;
			String state="";
			int i = 2;
			if(list!=null && list.size()>0){
				for(BisTransferStockInfo getObj:list){
					i++;
					params2.put("billNum", getObj.getBillNum());
					params2.put("ctnNum", getObj.getCtnNum());
					params2.put("skuId", getObj.getSku());
					switch(getObj.getEnterState()){
						case "成品":state="0";break;
						case "货物破损":state="1";break;
						case "包装破损":state="2";break;
					}
					params2.put("enterState", state);
					trayInfo = new ArrayList<TrayInfo>();
					trayInfo = findBySku(params2,"01",transferId);
					if(!trayInfo.isEmpty()){
						//判断明细中是否已存在此条数据
						if(!exist(params2,transferId)){
								//判断出库件数是否不大于库存总件数
								if(judgePiece(trayInfo,getObj.getPiece(),getObj,transferId)){
									skuObj = new BaseSkuBaseInfo();
									skuObj = skuInfoService.get(getObj.getSku());
									newObj = new BisTransferStockInfo();
									newObj.setTransferId(transferId);
									newObj.setBillNum(getObj.getBillNum());
									newObj.setCtnNum(getObj.getCtnNum());
									newObj.setSku(getObj.getSku());
									newObj.setEnterState(state);
									newObj.setPiece(getObj.getPiece());
									newObj.setCargoName(skuObj.getCargoName());
									if(skuObj.getTypeSize() != null){
										newObj.setTypeSize(skuObj.getTypeSize());
									}else{
										newObj.setTypeSize(" ");
									}
									newObj.setNetWeight(getObj.getPiece() * skuObj.getNetSingle());
									newObj.setGrossWeight(getObj.getPiece()*skuObj.getGrossSingle());
									newObj.setUntis("1");
									newObj.setCrUser(user.getName());
									newObj.setCrTime(new Date());
									transferInfoService.save(newObj);
								//end if 库存数量
								}else{
									more += String.valueOf(i)+",";
								}
						//end if 明细存在
						}else{
							ex += String.valueOf(i)+",";
						}
					//end if
					}else{
						tray += String.valueOf(i)+",";
					}
				}//end for
			}
		}   catch (Exception e) {
			e.printStackTrace();
		}
		if(ex.equals("") && more.equals("") && tray.equals("")){
			return "success";
		}else{
			if(!ex.equals("")){
			ex = ex.substring(0, ex.length()-1);
			}
			if(!more.equals("")){
			more = more.substring(0, more.length()-1);
			}
			if(!tray.equals("")){
				tray = tray.substring(0, tray.length()-1);
			}
			return ex + ":" +more+":"+tray;
		}
	}
	
	//判断此条库存中是否存在
	private List<TrayInfo> findBySku(Map<String, Object> params2, String cargoState, String transferId) {
		Map<String,Object> params4 = new HashMap<String,Object>();
		params4.putAll(params2);
		params4.put("cargoState", cargoState);
		BisTransferStock transfer= transferService.get(transferId);
		params4.put("stockIn", transfer.getStockInId()); 
		return trayInfoService.findBySku(params4);
	}


	//判断此条是否已存在于明细中
	private boolean exist(Map<String, Object> params2, String transferId) {
		Map<String,Object> params3 = new HashMap<String,Object>();
		params3.putAll(params2);
		params3.put("transferId", transferId);
		params3.put("sku", params3.get("skuId"));
		params3.remove("skuId");
		List<BisTransferStockInfo> infoList = transferInfoService.findonly(params3);
        return !infoList.isEmpty();
		
	}

	
	//判断件数是否不多于库存件数
	private boolean judgePiece(List<TrayInfo> trayInfo, Double piece, BisTransferStockInfo getObj, String transferId) {
		BisTransferStock transfer = transferService.get(transferId);
		Map<String,Object> pd = new HashMap<String,Object>();//动态质押参数params
		pd.put("skuId", getObj.getSku());
		pd.put("client", transfer.getStockInId());
		pd.put("warehouseId", transfer.getWarehouseId());
		pd.put("ptype", 2);
		//动态质押件数
		List<BaseClientPledge> pledgeListD = new ArrayList<BaseClientPledge>();
		pledgeListD = clientPledgeService.findBySku(pd);
		int pledgeD = 0;
		if(!pledgeListD.isEmpty()){
			int sizeD=pledgeListD.size();
			for(int i=0;i<sizeD;i++){
				pledgeD += pledgeListD.get(i).getNum();
			}
		}
		Map<String,Object> pj = new HashMap<String,Object>();//静态质押参数params
		pj.put("skuId", getObj.getSku());
		pj.put("client", transfer.getStockInId());
		pj.put("warehouseId", transfer.getWarehouseId());
		pj.put("billNum", getObj.getBillNum());
		pj.put("ctnNum", getObj.getCtnNum());
		pj.put("ptype", 1);
		//静态质押件数
		List<BaseClientPledge> pledgeListJ = new ArrayList<BaseClientPledge>();
		pledgeListJ = clientPledgeService.findBySku(pj);
		int pledgeJ = 0;
		if(!pledgeListJ.isEmpty()){
			int sizeJ=pledgeListJ.size();
			for(int j=0;j<sizeJ;j++){
				pledgeJ += pledgeListJ.get(j).getNum();
			}
		}
		int size = trayInfo.size();
		int nowPiece = 0;
		for(int i=0;i<size;i++){
			nowPiece += trayInfo.get(i).getNowPiece();
		}
        return !(piece + pledgeD + pledgeJ > nowPiece);
	}
}
