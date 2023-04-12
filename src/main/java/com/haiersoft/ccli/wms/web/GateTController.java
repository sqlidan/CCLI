package com.haiersoft.ccli.wms.web;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.PropertiesUtil;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.*;
import com.haiersoft.ccli.wms.service.*;

//import org.hibernate.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 闸口管理
 */
@Controller
@RequestMapping("wsgate/gate")
public class GateTController extends BaseController {

    @Autowired
    private GateCarService gateCarService;

    @RequestMapping(value = "fullScreen", method = RequestMethod.GET)
    public String fullScreen() {
        return "wms/gate/gateNewShow";
    }
     
    @RequestMapping(value = "fullJson")
    @ResponseBody
    public Map<String, Object> fullJson(HttpServletRequest request) {
    	List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        Page<BisGateCar> page = getPage(request);
        page = gateCarService.search(page, filters);
        return getEasyUIData(page);
    }
}
