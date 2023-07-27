package com.haiersoft.ccli.cost.web;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.cost.dao.PlatformWorkTicketDao;
import com.haiersoft.ccli.cost.entity.PlatformWorkTicket;
import com.haiersoft.ccli.cost.entity.PlatformWorkTicketExcel;
import com.haiersoft.ccli.cost.service.PlatformWorkTicketService;
import com.haiersoft.ccli.system.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.SQLQuery;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;


@Controller
@RequestMapping("cost/platformWorkTicket")
public class PlatformWorkTicketController extends BaseController {

	@Autowired
	private PlatformWorkTicketService service;

	//工票
	@RequestMapping(value="list",method = RequestMethod.GET)
	public String list(HttpServletRequest request) {
		return "cost/platformWorkTicket/platformWorkTicketList";
	}

	@RequestMapping(value = "page")
	@ResponseBody
	public Map<String, Object> page(HttpServletRequest request) {

		Page<PlatformWorkTicket> pageData = getPage(request);
		Map<String, Object> map = PropertyFilter.buildFromHttpRequestMap(request);
		pageData = service.workTicketPage(pageData,map);
		return getEasyUIData(pageData);

	}


	//修改跳转
	@RequestMapping(value="update/{id}", method = RequestMethod.GET)
	public String updateContractForm(Model model, @PathVariable("id") String id) {
		PlatformWorkTicket platformUser = service.get(id);
		model.addAttribute("platformWorkTicket", platformUser);
		model.addAttribute("action", "update");
		return "cost/platformWorkTicket/platformWorkTicketInfo";
	}

	/**
	 * 修改保存报关单
	 * @throws ParseException
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public String update(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		/*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String operateTime = request.getParameter("operateTime");
		Date date = simpleDateFormat.parse(operateTime);*/
		PlatformWorkTicket platformWorkTicket = new PlatformWorkTicket();
		//转换对应实体类参数
		parameterReflect.reflectParameter(platformWorkTicket, request);

	/*	String id=request.getParameter("id");
		String tallyId=request.getParameter("tallyId");
		String forkliftSceneId=request.getParameter("forkliftSceneId");
		String forkliftUpId=request.getParameter("forkliftUpId");
		String clientId=request.getParameter("clientId");
		String stevedoreId=request.getParameter("stevedoreId");
		String stevedoreName=request.getParameter("stevedoreName");
		String numPlus=request.getParameter("numPlus");

		PlatformWorkTicket platformWorkTicketExist=service.get(id);

		platformWorkTicketExist.setTallyId(tallyId);
		platformWorkTicketExist.setForkliftSceneId(forkliftSceneId);
		platformWorkTicketExist.setForkliftUpId(forkliftUpId);

		platformWorkTicketExist.setClientId(clientId);
		platformWorkTicketExist.setStevedoreId(stevedoreId);
		platformWorkTicketExist.setStevedoreName(stevedoreName);
		platformWorkTicketExist.setNumPlus(numPlus);
		platformWorkTicketExist.setUpdatedTime(new Date());
			service.update(platformWorkTicketExist);
		*/



		//BeanUtils.copyProperties(platformWorkTicket,platformWorkTicketExist);//复制对象属性
		platformWorkTicket.setUpdatedTime(new Date());

		service.update(platformWorkTicket);


		//service.updateWorkTicket(platformWorkTicketExist);
		return "success";
	}

	/**
	 * @throws UnsupportedEncodingException
	 * @Description: 获得理货(无视大小写)
	 */
	@RequestMapping(value = "findTally", method = RequestMethod.GET)
	@ResponseBody
	public List<User> findTally(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		List<User> clientInfos = new ArrayList<User>();
		String param = request.getParameter("q");// 搜索值
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);

		if (param != null && !"".equals(param)) {

			param= URLDecoder.decode(param,"UTF-8");

		}
				List<Map<String, Object>> listC = service.findTally(param);
				int size = listC.size();
				int aa = 0;
				String bb = "";
				User info = null;
				for (int i = 0; i < size; i++) {
					info = new User();
					aa = ((BigDecimal) listC.get(i).get("ID")).intValue();
					info.setId(aa);
					bb = (String) listC.get(i).get("NAME");
					info.setName(bb);
					clientInfos.add(info);
				}


		return clientInfos;
	}
	/**
	 * @throws UnsupportedEncodingException
	 * @Description: 获得叉车司机(无视大小写)
	 */
	@RequestMapping(value = "findForklift", method = RequestMethod.GET)
	@ResponseBody
	public List<User> findForklift(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		List<User> clientInfos = new ArrayList<User>();
		String param = request.getParameter("q");// 搜索值
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);

		if (param != null && !"".equals(param)) {

			param= URLDecoder.decode(param,"UTF-8");

		}
		List<Map<String, Object>> listC = service.findForklift(param);
		int size = listC.size();
		int aa = 0;
		String bb = "";
		User info = null;
		for (int i = 0; i < size; i++) {
			info = new User();
			aa = ((BigDecimal) listC.get(i).get("ID")).intValue();
			info.setId(aa);
			bb = (String) listC.get(i).get("NAME");
			info.setName(bb);
			clientInfos.add(info);
		}


		return clientInfos;
	}
	/**
	 * @throws UnsupportedEncodingException
	 * @Description: 获得装卸队
	 */
	@RequestMapping(value = "findZxd", method = RequestMethod.GET)
	@ResponseBody
	public List<User> findZxd(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		List<User> clientInfos = new ArrayList<User>();
		String param = request.getParameter("q");// 搜索值
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);

		if (param != null && !"".equals(param)) {

			param= URLDecoder.decode(param,"UTF-8");

		}
		List<Map<String, Object>> listC = service.findZxd(param);
		int size = listC.size();
		int aa = 0;
		String bb = "";
		User info = null;
		for (int i = 0; i < size; i++) {
			info = new User();
			aa = ((BigDecimal) listC.get(i).get("ID")).intValue();
			info.setId(aa);
			bb = (String) listC.get(i).get("NAME");
			info.setName(bb);
			clientInfos.add(info);
		}


		return clientInfos;
	}

	/**
	 * @throws UnsupportedEncodingException
	 * @Description: 获得装卸工
	 */
	@RequestMapping(value = "findZxg/{zxg}", method = RequestMethod.GET)
	@ResponseBody
	public List<User> findZxg(HttpServletRequest request, @PathVariable("zxg") String zxg) throws UnsupportedEncodingException {
		List<User> clientInfos = new ArrayList<User>();

		List<Map<String, Object>> listC = service.findZxg(zxg);
		int size = listC.size();
		int aa = 0;
		String bb = "";
		User info = null;
		for (int i = 0; i < size; i++) {
			info = new User();
			aa = ((BigDecimal) listC.get(i).get("ID")).intValue();
			info.setId(aa);
			bb = (String) listC.get(i).get("NAME");
			info.setName(bb);
			clientInfos.add(info);
		}


		return clientInfos;
	}
	@RequestMapping(value = "exportExcel")
	@ResponseBody
	public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);

		//Page<PlatformWorkTicket> pageData = getPage(request);
		//String orderBy=" ORDER BY t1.CREATED_TIME DESC " ;
		//Page<PlatformWorkTicket> clientList = service.page(pageData,filters,PlatformWorkTicketDao.QueryType.PLATFORM_WORK_TICKET,orderBy);

	// clientList.getResult();
		Page<PlatformWorkTicket> pageData = getPage(request);
		Map<String, Object> map = PropertyFilter.buildFromHttpRequestMap(request);
		List<Map<String,String>> resultMap = service.workTicketOfExport(map);



		List<PlatformWorkTicket> result=JSON.parseArray(JSON.toJSON(resultMap).toString(), PlatformWorkTicket.class);

	//	List<PlatformWorkTicket> result = service.getWorkTicket(pageData,filters,PlatformWorkTicketDao.QueryType.PLATFORM_WORK_TICKET,orderBy);
		List<PlatformWorkTicketExcel> resultExcelList = new ArrayList<>();
		for( PlatformWorkTicket ticket :result){

			//错误数据会导致 空指针 程序报错  这边先做=1 处理
			if(StringUtils.isEmpty(ticket.getNumPlus())){
				ticket.setNumPlus("1");
			}
			BigDecimal numPlus = new BigDecimal(ticket.getNumPlus());

			PlatformWorkTicketExcel lh=new PlatformWorkTicketExcel();
			BeanUtils.copyProperties(ticket,lh);
			if(lh.getInOutBoundFlag().equals("1")){

				lh.setWeight(ticket.getInWeight());
				lh.setProductName(ticket.getInProductName());

			}else {
				lh.setWeight(ticket.getOutWeight());
				lh.setProductName(ticket.getOutProductName());

			}

			BigDecimal lhO = new BigDecimal("0.6");
			BigDecimal lhA = new BigDecimal("0.3");
			BigDecimal lhWeight = BigDecimal.valueOf(lh.getWeight()).multiply(numPlus);
			lh.setOperationWeight(lhO.multiply(lhWeight).doubleValue());
			lh.setActualWeightx(lhA.multiply(lhWeight).doubleValue());
			lh.setPost("理货");
			lh.setName(ticket.getTallyName());


			PlatformWorkTicketExcel forkliftScene=new PlatformWorkTicketExcel();
			BeanUtils.copyProperties(ticket,forkliftScene);
			if(forkliftScene.getInOutBoundFlag().equals("1")){

				forkliftScene.setWeight(ticket.getInWeight());
				forkliftScene.setProductName(ticket.getInProductName());

			}else {
				forkliftScene.setWeight(ticket.getOutWeight());
				forkliftScene.setProductName(ticket.getOutProductName());


			}

			BigDecimal forkliftSceneO = new BigDecimal("0.6");
			BigDecimal forkliftSceneA = new BigDecimal("0.3");
			BigDecimal forkliftWeight = BigDecimal.valueOf(forkliftScene.getWeight()).multiply(numPlus);
			forkliftScene.setOperationWeight(forkliftSceneO.multiply(forkliftWeight).doubleValue());
			forkliftScene.setActualWeightx(forkliftSceneA.multiply(forkliftWeight).doubleValue());

			forkliftScene.setPost("现场叉车");
			forkliftScene.setName(ticket.getForkliftSceneName());


			PlatformWorkTicketExcel forkliftUp=new PlatformWorkTicketExcel();
			BeanUtils.copyProperties(ticket,forkliftUp);
			if(forkliftUp.getInOutBoundFlag().equals("1")){

				forkliftUp.setWeight(ticket.getInWeight());
				forkliftUp.setProductName(ticket.getInProductName());


			}else {
				forkliftUp.setWeight(ticket.getOutWeight());
				forkliftUp.setProductName(ticket.getOutProductName());


			}
			BigDecimal forkliftUpO = new BigDecimal("0.8");
			BigDecimal forkliftUpA = new BigDecimal("0.4");
			BigDecimal forkliftUpWeight = BigDecimal.valueOf(forkliftUp.getWeight()).multiply(numPlus);


			forkliftUp.setOperationWeight(forkliftUpO.multiply(forkliftUpWeight).doubleValue());
			forkliftUp.setActualWeightx(forkliftUpA.multiply(forkliftUpWeight).doubleValue());
			forkliftUp.setPost("楼上叉车");
			forkliftUp.setName(ticket.getForkliftUpName());


			resultExcelList.add(lh);
			resultExcelList.add(forkliftScene);
			resultExcelList.add(forkliftUp);


		}


		ExportParams params = new ExportParams("操作部计件作业人员工票", "月台靠口工票记录Sheet", ExcelType.XSSF);

		Workbook workbook = ExcelExportUtil.exportExcel(params, PlatformWorkTicketExcel.class, resultExcelList);

		String formatFileNameP = "月台靠口工票记录" + ".xlsx";
		String formatFileName = new String(formatFileNameP.getBytes("GB2312"), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");// 设定输出文件头
		response.setContentType("application/msexcel");// 定义输出类型
		OutputStream os = response.getOutputStream();
		workbook.write(os); // 写入文件
		os.close(); // 关闭流
	}

	String bigDecimalToString(BigDecimal num){
		DecimalFormat df =new DecimalFormat("#.00");
		String result =df.format(num);
		return result;
	}


	@RequestMapping(value = "enterWeight")
	@ResponseBody
	public Map<String, Object> enterWeight(HttpServletRequest request) {
		Page<PlatformWorkTicket> pageData = new Page<PlatformWorkTicket>();
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		PlatformWorkTicketDao.QueryType type=PlatformWorkTicketDao.QueryType.NONE;
		String orderBy="";
		BigDecimal enterWeight=service.getEnterWeight(pageData,filters,PlatformWorkTicketDao.QueryType.ENTER_STEVEDORING_NO_FEE_PAID,orderBy);
		//NET
		Map<String, Object> resultMap=new HashMap<String, Object>();
		resultMap.put("enterWeight",bigDecimalToString(enterWeight));
		return resultMap;
	}

	@RequestMapping(value = "outWeight")
	@ResponseBody
	public Map<String, Object> outWeight(HttpServletRequest request) {
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		Page<PlatformWorkTicket> pageData=new Page<PlatformWorkTicket>();
		String orderBy="";
		BigDecimal outWeight=service.getOutWeight(pageData,filters,PlatformWorkTicketDao.QueryType.OUT_STEVEDORING_NO_FEE_PAID,orderBy);
		//NET_WEIGHT
		Map<String, Object> resultMap=new HashMap<String, Object>();
		resultMap.put("outWeight",bigDecimalToString(outWeight));
		return resultMap;
	}


	@RequestMapping(value = "enterAndOutWeight")
	@ResponseBody
	public Map<String, Object> enterAndOutWeight(HttpServletRequest request) {
		Page<PlatformWorkTicket> pageData = new Page<PlatformWorkTicket>();
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		PlatformWorkTicketDao.QueryType type=PlatformWorkTicketDao.QueryType.NONE;
		String orderBy="";
		BigDecimal enterWeight=service.getEnterWeight(pageData,filters,PlatformWorkTicketDao.QueryType.PLATFORM_WORK_TICKET,orderBy);
		BigDecimal outWeight=service.getOutWeight(pageData,filters,PlatformWorkTicketDao.QueryType.PLATFORM_WORK_TICKET,orderBy);

		Map<String, Object> resultMap=new HashMap<String, Object>();
		resultMap.put("enterWeight",bigDecimalToString(enterWeight));
		resultMap.put("outWeight",bigDecimalToString(outWeight));
		return resultMap;
	}






}
