package com.haiersoft.ccli.wms.web.tallying;

import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.PropertiesUtil;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.remoting.hand.in.service.WarehousingWebService;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.EnterTallyInfoToExcel;
import com.haiersoft.ccli.wms.entity.PreEntryInvtQuery.BisPreEntryInvtQuery;
import com.haiersoft.ccli.wms.entity.apiEntity.InvtHeadTypeVo;
import com.haiersoft.ccli.wms.service.tallying.TallyingManageService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 *理货入库
 */
@Controller
@RequestMapping(value = "wms/tallying")
public class TallyingManageController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(TallyingManageController.class);

	private static List<EnterTallyInfoToExcel> LIST = new ArrayList<>();
	private static int TOTAL = 0;

	@Autowired
	private WarehousingWebService warehousingWebService;

	//理货入库
	@RequestMapping(value = "tallying", method = RequestMethod.GET)
	public String tallying() {
		return "wms/tallying/tallying";
	}

	@RequestMapping(value = "json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		//返回数据
		Page<EnterTallyInfoToExcel> page = getPage(request);
		page.setResult(LIST);
		page.setTotalCount(TOTAL);
		return getEasyUIData(page);
	}

	/**
	 * java实现文件下载功能代码
	 */
	@RequestMapping(value = "download", method = RequestMethod.POST)
	@ResponseBody
	public void fileDownLoad(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		String fileName = "入库理货托盘明细导入模板.xls";
		String filePath = PropertiesUtil.getPropertiesByName("downloadexcel", "application");
		String realpath = filePath + "WEB-INF\\classes\\importExcel\\入库理货托盘明细导入模板.xls";
//		String realpath = "E:\\projects\\CCLI\\src\\main\\resources\\importExcel\\入库理货托盘明细导入模板.xls";
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
	 * 入库联系单明细excel跳转
	 */
	@RequestMapping(value = "into", method = RequestMethod.GET)
	public String intoExcel(Model model) {
		model.addAttribute("action", "intot");
		return "wms/tallying/tallyingInto";
	}

	/**
	 * excel导入
	 */
	@RequestMapping(value = "intot", method = RequestMethod.POST)
	@ResponseBody
	public String intoExcel(@RequestParam(value = "file", required = false) MultipartFile file,
							 HttpServletRequest request,
							 Model model) {
		Map<String,Object> result = new HashMap<>();
		String error = "";
		List<EnterTallyInfoToExcel> enterTallyInfoToExcelList = new ArrayList<>();
		try {
			ImportParams params = new ImportParams();
			params.setTitleRows(1);
			List<EnterTallyInfoToExcel> list = ExcelImportUtil.importExcel(file.getInputStream(), EnterTallyInfoToExcel.class, params);
			if (list != null && list.size() > 0) {
				int i = 2;
				for (EnterTallyInfoToExcel getObj : list) {
					Boolean e = false;
					if (getObj.getTallyNo() == null || getObj.getTallyNo().trim().length() == 0) {
						e = true;
					}
					if (getObj.getAsn() == null || getObj.getAsn().trim().length() == 0) {
						e = true;
					}
					if (getObj.getSku() == null || getObj.getSku().trim().length() == 0) {
						e = true;
					}
					String r = warehousingWebService.getTallyWarehousing(getObj.getAsn(),getObj.getSku());
					if (!r.contains("操作成功")){
						e = true;
					}
					if (e){
						error += String.valueOf(i) + ",";
						i++;
					}else{
						enterTallyInfoToExcelList.add(getObj);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (error == "") {
			LIST = enterTallyInfoToExcelList;
			TOTAL = enterTallyInfoToExcelList.size();
			return "success";
		} else {
			error = error.substring(0, error.length() - 1);
			LIST = new ArrayList<>();
			TOTAL = 0;
			return error;
		}
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	@ResponseBody
	public String save(@RequestBody List<EnterTallyInfoToExcel> enterTallyInfoToExcels) {
		User user = UserUtil.getCurrentUser();
		System.out.println("enterTallyInfoToExcels"+enterTallyInfoToExcels.toString());
		if (enterTallyInfoToExcels!=null && enterTallyInfoToExcels.size() > 0){
			for (EnterTallyInfoToExcel forEnterTallyInfoToExcel:enterTallyInfoToExcels) {
				String result = warehousingWebService.inStorageTally(forEnterTallyInfoToExcel.getAsn(),forEnterTallyInfoToExcel.getSku(),forEnterTallyInfoToExcel.getTallyNo(),
						forEnterTallyInfoToExcel.getState(),forEnterTallyInfoToExcel.getNum(),user.getName());
				if (!result.contains("操作成功")){
					return result;
				}
			}
		}
		LIST = new ArrayList<>();
		TOTAL = 0;
		return "success";
	}
}
