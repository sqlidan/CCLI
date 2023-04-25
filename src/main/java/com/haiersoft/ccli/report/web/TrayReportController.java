package com.haiersoft.ccli.report.web;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest; 
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller; 
import org.springframework.web.bind.annotation.PathVariable; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.report.service.TrayReportService; 

/**
 * 
 * @author slh
 * @ClassName: TrayController
 * @Description: 库位情况
 * @date 2016年3月9日 下午2:35:31
 */
@Controller
@RequestMapping("report/Tray")
public class TrayReportController extends BaseController {

	@Autowired
	private TrayReportService trayReportService;

	/**
	 * 
	 * @author slh
	 * @Description: 库位情况页面
	 * @date 2016-6-23 10:08
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list() {
		return "report/TrayReportList";
	}
	/**
	 * 
	 * @author slh
	 * @Description: 库存货物量统计
	 * @date 2016-6-23 10:08
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "StoreSaveAccount", method = RequestMethod.GET)
	public String StoreSaveAccount() {
		return "report/TrayStroeInfoReport";
	}
	/**
	 * 
	 * @author slh
	 * @Description: 获取库存数据 根据楼号 
	 * @date 2016-6-23 10:08
	 * @return
	 * @throws
	 */
	@RequestMapping(value="GetFramInfoByBuiNum/{budnum}",method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String,Object>> GetFramInfoByBuiNum(HttpServletRequest request,@PathVariable("budnum") String budnum){ 
			List<Map<String,Object>> trayFramInfo = trayReportService.FindTrayInfoByBuiNum(budnum);
			return trayFramInfo;
		}
	/**
	 * 
	 * @author slh
	 * @Description: 获取库存数据 根据楼号+仓库id
	 * @date 2016-6-23 10:08
	 * @return
	 * @throws
	 */
	@RequestMapping(value="GetFramInfo",method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String,Object>> GetFramInfo(HttpServletRequest request, HttpServletResponse response) { 
        String budnum = request.getParameter("budnum");// 楼号
        String houseid = request.getParameter("houseid");// 仓库id
		List<Map<String,Object>> trayFramInfo = trayReportService.FindTrayInfo(budnum,houseid);
		return trayFramInfo;
	}
	
	/** 
	 * @author slh
	 * @Description: 获取库位框架
	 * @date 2016-6-23 10:08
	 * @return
	 * @throws
	 */
	@RequestMapping(value="GetFramByBuildingNum/{budnum}",method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String,Object>> GetFramByBuildingNum(HttpServletRequest request,@PathVariable("budnum") String budnum){ 
			List<Map<String,Object>> trayFram = trayReportService.FindTrayFByBuildingNum(budnum);
			return trayFram;
		}
	
	/** 
	 * @author slh
	 * @Description: 获取库位框架 根据楼号+仓库号
	 * @date 2016-6-23 10:08
	 * @return
	 * @throws
	 */
	@RequestMapping(value="GetFram",method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String,Object>> GetFram(HttpServletRequest request, HttpServletResponse response) { 
          String budnum = request.getParameter("budnum");// 楼号
          String houseid = request.getParameter("houseid");// 仓库id
          List<Map<String,Object>> trayFram = trayReportService.FindTrayF(budnum,houseid);
       return trayFram;
    }
	
	/** 
	 * @author slh
	 * @Description: 仓库货物存储统计 框架
	 * @date 2016-6-23 10:08
	 * @return
	 * @throws
	 */
	@RequestMapping(value="GetStroeSaveF",method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String,Object>> GetStroeSaveF(HttpServletRequest request, HttpServletResponse response) { 
          String budnum = request.getParameter("budnum");// 楼号
          String houseid = request.getParameter("houseid");// 仓库id
          List<Map<String,Object>> trayFram = trayReportService.FindBTrayStoreF(budnum,houseid);
          return trayFram;
    }
	/** 
	 * @author slh
	 * @Description: 仓库货物存储统计 货物数据统计
	 * @date 2016-6-23 10:08
	 * @return
	 * @throws
	 */
	@RequestMapping(value="GetTraySaveInfo",method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String,Object>> GetStroeStoreInfo(HttpServletRequest request, HttpServletResponse response) { 
          String budnum = request.getParameter("budnum");// 楼号
          String houseid = request.getParameter("houseid");// 仓库id
          List<Map<String,Object>> trayFram = trayReportService.FindTrayStoreInfo(budnum,houseid);
       return trayFram;
    }
	
	/** 
	 * @author slh
	 * @Description: 各类产品 货物数据统计
	 * @date 2016-6-23 10:08
	 * @return
	 * @throws
	 */
	@RequestMapping(value="GetTrayProductSum",method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String,Object>> GetTrayProductSum(HttpServletRequest request, HttpServletResponse response) { 
          String budnum = request.getParameter("budnum");// 楼号
          String houseid = request.getParameter("houseid");// 仓库id
          List<Map<String,Object>> trayFram = trayReportService.FindTrayProductSum(budnum,houseid);
       return trayFram;
    }
}
