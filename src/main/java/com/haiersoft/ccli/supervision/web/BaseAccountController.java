package com.haiersoft.ccli.supervision.web;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.platform.entity.PlatformReservationOutbound;
import com.haiersoft.ccli.supervision.entity.BaseAccount;
import com.haiersoft.ccli.supervision.service.BaseAccountService;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分类监管 底账明细controller
 * @author 
 *
 */

@Controller
@RequestMapping("supervision/baseAccountInfo")
public class BaseAccountController extends BaseController{
    
	@Autowired
	private BaseAccountService baseAccountService;

	/**
	 * 默认页面
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "fljg/accountList";
	}

	/**
	 * 修改底账明细跳转
	 *
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("account", baseAccountService.queryBaseAccountInfo(id));
		model.addAttribute("action", "update");
		return "fljg/accountForm";
	}

	/**
	 * 修改底账明细
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public String update(@Valid @ModelAttribute @RequestBody BaseAccount baseAccount, Model model) {
		baseAccountService.updateById(baseAccount);
		return "success";
	}

	@RequestMapping(value="json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<BaseAccount> page = getPage(request);
		Map<String, Object> map=PropertyFilter.buildFromHttpRequestMap(request);

		page = baseAccountService.seachSql(page, map);
		return getEasyUIData(page);
	}

	/**
	 * @param request
	 * @param response
	 * @throws Exception
	 * @throws
	 * @Description: 导出excel
	 * @date 2023年3月24日 下午2:22:55
	 */
	@RequestMapping(value = "export")
	@ResponseBody
	public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map=PropertyFilter.buildFromHttpRequestMap(request);


		List<BaseAccount> baseAccountList = baseAccountService.seachSql(map);

		ExportParams params = new ExportParams("底账明细", "底账明细Sheet", ExcelType.XSSF);

		Workbook workbook = ExcelExportUtil.exportExcel(params, BaseAccount.class, baseAccountList);

		String formatFileNameP = "底账明细" + ".xlsx";
		String formatFileName = new String(formatFileNameP.getBytes("GB2312"), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");// 设定输出文件头
		response.setContentType("application/msexcel");// 定义输出类型
		OutputStream os = response.getOutputStream();
		workbook.write(os); // 写入文件
		os.close(); // 关闭流
	}

	/**
	 * 拆分功能
	 * 将拆分的功能保存到底账中
	 * @param
	 */
	@RequestMapping(value = "saveInfo", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public String saveInfo(@RequestBody List<PlatformReservationOutbound> info) {

	    //查询抵账信息
		BaseAccount account = baseAccountService.selectByBillNoAndCno (info.get(0).getBillNo(), info.get(0).getContainerNo());

		//插入之前进行判断
		Boolean flag = baseAccountService.flagInsert(info);
		if (flag) {
			return "fail";
		}
		//插入拆分的数据
		for (int i = 0; i < info.size(); i++) {
			baseAccountService.getEntityAndInsert(info.get(i));
		}
		//删除
		if (account != null && account.getId() != null) {
			baseAccountService.delete(account.getId().toString());
		}
		//修改预约出库字段为已拆分
		baseAccountService.updateOutBoundIsSplit(info.get(0).getId());
		return "success";
	}


	/**
	 * @param request
	 * @throws Exception
	 * @throws
	 * @Description: 根据 箱号  提单号  获取抵账信息
	 * @date 2023年3月30日 下午5:22:55
	 */

	@RequestMapping(value="getAccountDataByNo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getAccountDataByNo(HttpServletRequest request) {
		Page<BaseAccount> page = getPage(request);
		Map<String, Object> map=PropertyFilter.buildFromHttpRequestMap(request);

		page = baseAccountService.seachData(page, map);
		return getEasyUIData(page);
	}

}
