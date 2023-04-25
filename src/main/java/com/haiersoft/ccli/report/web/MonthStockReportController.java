package com.haiersoft.ccli.report.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.CreatPDFUtils;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.ExcelUtil;
import com.haiersoft.ccli.common.utils.MyFileUtils;
import com.haiersoft.ccli.common.utils.MyPDFUtils;
import com.haiersoft.ccli.common.utils.PropertiesUtil;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.report.entity.Stock;
import com.haiersoft.ccli.report.service.InStockReportService;
import com.haiersoft.ccli.report.service.MonthStockReportService;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.BisEnterStockInfo;
import com.itextpdf.text.PageSize;

/**
 * @author 
 * @ClassName: 
 * @Description: 
 * @date
 */
@Controller
@RequestMapping("report/monthStock")
public class MonthStockReportController extends BaseController {

    @Autowired
    private MonthStockReportService monthStockReportService;
    @Autowired
    private ClientService clientService;

    /**
     * 默认页面
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list() {
        return "report/monthStockReportList";
    }

    @RequestMapping(value = "json", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request) {
    	Page<Object> page = getPage(request);
    	 String names = null;
    	 BaseClientInfo getObj = null;
    	String month = request.getParameter("months").toString();
    	String ids = request.getParameter("customer").toString();
    	if(ids != null && !"".equals(ids)) {
    		  getObj = clientService.get(Integer.valueOf(ids));
    	}
    	 if(getObj!=null) {
    		  names=getObj.getClientName();
    	 }
    	 page = monthStockReportService.getPage(page, month,names);
        return getEasyUIData(page);
    }
    
    @RequestMapping(value = "getAllMonth", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>>  getAllMonth(HttpServletRequest request, HttpServletResponse response){
    	 return monthStockReportService.getAllMonth();
    }

}
