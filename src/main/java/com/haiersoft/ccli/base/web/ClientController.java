package com.haiersoft.ccli.base.web;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

//import com.haiersoft.ccli.wms.service.BusinessService;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.BaseClientRelation;
import com.haiersoft.ccli.base.service.ClientRelationService;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.HttpGo;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.web.BaseController;

/**
 * 用户controller
 *
 * @author lzg
 * @date 2016年2月24日
 */
@Controller
@RequestMapping("base/client")
public class ClientController extends BaseController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRelationService clientRelationService;


    /**
     * 跳转列表页面
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String menuList() {
        return "base/clientList";
    }

    /**
     * 列表页面table获取json
     */
    @RequiresPermissions("base:client:view")
    @RequestMapping(value = "listjson", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request) {
//    	String syncClientName = request.getParameter("syncClientName");
        Page<BaseClientInfo> page = getPage(request);
        //当jsp页面传来的syncClientName为空时正常查询,返回查询结果.
//    	if(StringUtils.isEmpty(syncClientName)) {

        //PropertyFilter里有查询参数
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        PropertyFilter filter = new PropertyFilter("EQI_delFlag", 0);
        filters.add(filter);
        page = clientService.search(page, filters);

//    	}
        //syncClientName不为空时进行客户同步,并返回查询结果
//    	else {
//    		List<PropertyFilter> filters = new ArrayList();
//            PropertyFilter f1 = new PropertyFilter("EQI_delFlag", 0);
//            PropertyFilter f2 = new PropertyFilter("EQS_syncClientName", syncClientName);
//            filters.add(f1);
//            filters.add(f2);
//            page = clientService.search(page, filters);
//            BaseClientInfo baseClientInfo = null;
//            if(page.getResult().size() !=0) {
//            	baseClientInfo = page.getResult().get(0);
//            }
//            else {
//    			baseClientInfo=new BaseClientInfo();
//    		}
//    		String getJson = getJsonData_api(syncClientName);
//    		if(null !=getJson) {
//    			JSONObject obj = JSONObject.parseObject(getJson);
//    			//BaseClientInfo baseClientInfo = new BaseClientInfo();
//    			baseClientInfo.setClientSort("0");
//    			baseClientInfo.setSyncClientName(obj.getString("customerName"));
//    			baseClientInfo.setSyncComcode(obj.getString("comCode"));
//    			baseClientInfo.setTaxAccount(obj.getString("dutyNumber"));
//    			baseClientInfo.setAddress(obj.getString("address"));
//    			baseClientInfo.setTelNum(obj.getString("phone"));
//    			baseClientInfo.setDelFlag(0);
//    			clientService.save(baseClientInfo);
//
////                List<PropertyFilter> filters2 = new ArrayList<PropertyFilter>();
// //               PropertyFilter filter = new PropertyFilter("EQS_syncClientName",syncClientName );
// //               filters2.add(filter);
//                page = clientService.search(page, filters);
//    		}

//    	}

        return getEasyUIData(page);

    }


    @RequestMapping(value = "getClientList", method = RequestMethod.GET)
    @ResponseBody
    public List<BaseClientInfo> getClientList(HttpServletRequest request) {
        Page<BaseClientInfo> page = getPage(request);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        PropertyFilter filter = new PropertyFilter("EQI_delFlag", 0);
        filters.add(filter);
        page = clientService.search(page, filters);

        return page.getResult();
    }

    /**
     * 列表页面table同步客户信息
     */
    @RequiresPermissions("base:client:view")
    @RequestMapping(value = "listTb", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> listTb(HttpServletRequest request, Model model) {
        String syncClientName = request.getParameter("syncClientName"); // 陆海通平台客户名称
        String syncTaxAccount = request.getParameter("syncTaxAccount"); // 税号
        String urlParams = (syncClientName ==null ?"" :syncClientName )+ "&code=" + (syncTaxAccount ==null ?"":syncTaxAccount);
        String getJson = getJsonData_api(urlParams);

        BaseClientInfo baseClientInfo = new BaseClientInfo();
        if(null !=getJson) {
            JSONObject obj = JSONObject.parseObject(getJson);
            baseClientInfo.setClientSort("0");
            baseClientInfo.setSyncClientName(obj.getString("customerName"));
            baseClientInfo.setSyncComcode(obj.getString("comCode"));
            baseClientInfo.setTaxAccount(obj.getString("dutyNumber"));
            baseClientInfo.setAddress(obj.getString("address"));
            baseClientInfo.setTelNum(obj.getString("phone"));
            baseClientInfo.setDelFlag(0);
            // 将陆海通平台客户名称  -》 客户名称
            baseClientInfo.setClientName(obj.getString("customerName"));
            // 查询客户名称是这个 陆海通平台客户名称
            if(StringUtils.isNotBlank(baseClientInfo.getSyncClientName())) {
                Page<BaseClientInfo> page = getPage(request);
                List<PropertyFilter> filters = new ArrayList();
                PropertyFilter f1 = new PropertyFilter("EQI_delFlag", 0);
                PropertyFilter f2 = new PropertyFilter("EQS_syncClientName", baseClientInfo.getSyncClientName());
                filters.add(f1);
                filters.add(f2);
                page = clientService.search(page, filters);
                if(page.getResult() == null || page.getResult().size() ==0){ // 没查到数据 直接保存
                    clientService.save(baseClientInfo);
                }else{
                    for(BaseClientInfo biEntity: page.getResult()){
                        biEntity.setClientSort("0");
                        biEntity.setSyncClientName(baseClientInfo.getSyncClientName());
                        biEntity.setSyncComcode(baseClientInfo.getSyncComcode());
                        biEntity.setTaxAccount(baseClientInfo.getTaxAccount());
                        biEntity.setAddress(baseClientInfo.getAddress());
                        biEntity.setTelNum(baseClientInfo.getTelNum());
                        biEntity.setDelFlag(0);
                        // 将陆海通平台客户名称  -》 客户名称
                        //biEntity.setClientName(baseClientInfo.getClientName());
                        clientService.update(biEntity);
                    }
                }
            }

        }
        String syn = baseClientInfo.getClientName();
        syn = syn == null ?"":syn;
        Map<String, Object> map = new HashMap();
        map.put("syn", syn);
        return map;

    }
    /**
     * 添加客户跳转
     */
    @RequiresPermissions("base:client:add")
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createForm(Model model) {
        model.addAttribute("client", new BaseClientInfo());
        model.addAttribute("fclient", new BaseClientRelation());
        model.addAttribute("action", "create");
        return "base/clientForm";
    }

    /**
     * 添加客户
     */
    @RequiresPermissions("base:client:add")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public String create(@Valid BaseClientInfo client, Model model) {

        clientService.save(client);

        // TODO
        // 同步到电商平台
        //businessService.clientAdd(client);

        BaseClientRelation fcliet = new BaseClientRelation();
        fcliet.setClientId(String.valueOf(client.getIds()));
        fcliet.setClientName(client.getClientName());
        fcliet.setParentId(client.getfClientId());
        fcliet.setParentName(client.getfClientName());
        clientRelationService.save(fcliet);

        return "success";
    }

    /**
     * 修改客户跳转
     */
    @RequiresPermissions("base:client:update")
    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Integer id, Model model) {

        BaseClientRelation getObj = clientRelationService.getObjIfClientId(String.valueOf(id));

        model.addAttribute("client", clientService.get(id));
        model.addAttribute("fclient", getObj);
        model.addAttribute("action", "update");

        return "base/clientForm";
    }

    /**
     * 修改客户
     */
    @RequiresPermissions("base:client:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public String update(@Valid @ModelAttribute @RequestBody BaseClientInfo client, Model model) {

        clientService.update(client);

        // TODO
        /*try{
        	// 同步到电商平台
        	businessService.clientUpdate(client);
        }catch(Exception e){
        	e.printStackTrace();
        }
*/
        BaseClientRelation getObj = clientRelationService.getObjIfClientId(String.valueOf(client.getIds()));

        if ((client.getfClientId() == null || "".equals(client.getfClientId())) && getObj != null && getObj.getParentId() != null && getObj.getParentId().length() > 0) {
            clientRelationService.delete(getObj);
        } else if (getObj != null && !client.getfClientId().equals(getObj.getParentId())) {
            getObj.setParentId(client.getfClientId());
            getObj.setParentName(client.getfClientName());
            clientRelationService.save(getObj);
        } else if (getObj == null && (client.getfClientId() != null && !"".equals(client.getfClientId()))) {
            BaseClientRelation fcliet = new BaseClientRelation();
            fcliet.setClientId(String.valueOf(client.getIds()));
            fcliet.setClientName(client.getClientName());
            fcliet.setParentId(client.getfClientId());
            fcliet.setParentName(client.getfClientName());
            clientRelationService.save(fcliet);
        }

        return "success";
    }

    /**
     * 删除客户
     */
    @RequiresPermissions("base:client:delete")
    @RequestMapping(value = "delete/{id}")
    @ResponseBody
    public String delete(@PathVariable("id") Integer id) {

        if (id > 0) {
            BaseClientInfo getObj = clientService.get(id);
            getObj.setDelFlag(1);
            clientService.update(getObj);

            // TODO
            // 同步到电商平台
            //businessService.clientDelete(id);
        }

        return "success";
    }

    /**
     * 根据客户id获取客户
     */
    @RequestMapping(value = "get/{uid}")
    @ResponseBody
    public BaseClientInfo getObj(@PathVariable("uid") Integer uid, Model model) {
        BaseClientInfo getObj = new BaseClientInfo();
        if (uid != null && uid > 0) {
            getObj = clientService.get(uid);
        }
        return getObj;
    }

    /**
     * 根据客户id获取客户名称
     */
    @RequestMapping(value = "getname/{uid}")
    @ResponseBody
    public String getname(@PathVariable("uid") Integer uid, Model model) {
        BaseClientInfo getObj = new BaseClientInfo();
        if (uid != null && uid > 0) {
            getObj = clientService.get(uid);
        }
        return getObj.getClientName();
    }

    /**
     * Ajax请求校验ClientName是否唯一。
     */
    @RequestMapping(value = "checkClientName")
    @ResponseBody
    public String checkClientName(String clientName) {
        List<Object> getList = clientService.getClient("clientName", clientName);
        if (getList != null && getList.size() > 0) {
            return "false";
        } else {
            return "true";
        }
    }

    /**
     * Ajax请求校验clientCode是否唯一。
     */
    @RequestMapping(value = "checkClientCode")
    @ResponseBody
    public String checkClientCode(String clientCode) {
        List<Object> getList = clientService.getClient("clientCode", clientCode);
        if (getList != null && getList.size() > 0) {
            return "false";
        } else {
            return "true";
        }
    }

    /**
     * Ajax请求校验税号是否唯一。
     */
    @RequestMapping(value = "checkClientTax")
    @ResponseBody
    public String checkClientTax(String tax) {
        List<Object> getList = clientService.getClient("taxAccount", tax);
        if (getList != null && getList.size() > 0) {
            return "false";
        } else {
            return "true";
        }
    }

    /**
     * @throws UnsupportedEncodingException
     * @Description: 获得所有客户(无视大小写)
     */
    @RequestMapping(value = "getClientAll", method = RequestMethod.GET)
    @ResponseBody
    public List<BaseClientInfo> getClientAll(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        List<BaseClientInfo> clientInfos = new ArrayList<BaseClientInfo>();
        String param = request.getParameter("q");// 搜索值

        String clientSort = request.getParameter("filter_EQS_clientSort");// 客户类型
        String setid = request.getParameter("setid");// 原数据填充值
        if ((param != null && !"".equals(param))
                || (setid != null && !"".equals(setid))) {
            if (param != null && !"".equals(param)) {
/*                try {
        			param=new String(param.getBytes("ISO8859-1"), "UTF-8");
        		} catch (UnsupportedEncodingException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}*/
                param= URLDecoder.decode(param,"UTF-8");
                List<Map<String, Object>> listC = clientService.findClient(param, clientSort);
                int size = listC.size();
                int aa = 0;
                String bb = "";
                BaseClientInfo info = null;
                for (int i = 0; i < size; i++) {
                    info = new BaseClientInfo();
                    aa = ((BigDecimal) listC.get(i).get("IDS")).intValue();
                    info.setIds(aa);
                    bb = (String) listC.get(i).get("CLIENT_NAME");
                    info.setClientName(bb);
                    clientInfos.add(info);
                }
            } else {
                // 根据原值id获取对象
                BaseClientInfo getObj = clientService.get(Integer.valueOf(setid));
                if (getObj != null) {
                    clientInfos.add(getObj);
                }

            }
        }
        return clientInfos;
    }

    /**
     * 获取联系人
     *
     * @param clientId
     * @param model
     * @return
     */
    @RequestMapping(value = "getcontract/{clientId}", method = RequestMethod.GET)
    @ResponseBody
    public String getcontract(@PathVariable("clientId") Integer clientId, Model model) {
        BaseClientInfo client = clientService.get(clientId);
        String contract = client.getContactMan();
        return contract;
    }


    /**
     * @param request
     * @param response
     * @throws Exception
     * @throws
     * @Description: 导出excel
     * @date 2016年7月11日 下午2:22:55
     */
    @RequestMapping(value = "export")
    @ResponseBody
    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        PropertyFilter filter = new PropertyFilter("EQI_delFlag", 0);
        filters.add(filter);

        List<BaseClientInfo> clientList = clientService.search(filters);

        ExportParams params = new ExportParams("客户", "客户Sheet", ExcelType.XSSF);

        Workbook workbook = ExcelExportUtil.exportExcel(params, BaseClientInfo.class, clientList);

        String formatFileNameP = "客户" + ".xlsx";
        String formatFileName = new String(formatFileNameP.getBytes("GB2312"), "ISO-8859-1");
        response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
        OutputStream os = response.getOutputStream();
        workbook.write(os); // 写入文件
        os.close(); // 关闭流
    }

    // /**
    // *
    // * @author Connor.M
    // * @Description: 获得所有客户
    // * @date 2016年2月25日 下午6:57:14
    // * @param code
    // * @return
    // * @throws
    // */
    // @RequestMapping(value = "getClientAll", method = RequestMethod.GET)
    // @ResponseBody
    // public List<BaseClientInfo> getClientAll(HttpServletRequest request,
    // HttpServletResponse response) {
    // List<BaseClientInfo> clientInfos =new ArrayList<BaseClientInfo>();
    // String param=request.getParameter("q");//搜索值
    // String setid=request.getParameter("setid");//原数据填充值
    // if( (param!=null && !"".equals(param)) ||( setid!=null &&
    // !"".equals(setid))){
    // List<PropertyFilter> filters
    // =PropertyFilter.buildFromHttpRequest(request);
    // PropertyFilter filter = new PropertyFilter("EQI_delFlag",
    // String.valueOf(0));
    // filters.add(filter);
    // if(param!=null && !"".equals(param)){
    // PropertyFilter filterparam = new PropertyFilter("LIKES_clientName",
    // String.valueOf(param));
    // filters.add(filterparam);
    // clientInfos = clientService.search(filters);
    // }else{
    // //根据原值id获取对象
    // BaseClientInfo getObj= clientService.get(Integer.valueOf(setid));
    // if(getObj!=null){
    // clientInfos.add(getObj);
    // }
    //
    // }
    // }
    //
    // // String toParm = JSON.toJSONString(clientInfos);
    // // try {
    // // ResponseJsonWriter.write(toParm, response);
    // // } catch (Exception e) {
    // // e.printStackTrace();
    // // }
    // return clientInfos;
    // }


    /**
     * 客商平台客户同步信息获取接口数据
     */
    @Autowired
    private HttpGo httpGo;

    public String getJsonData_api(String syncClientName) {
        String requestUrl = "http://lht.sdland-sea.com/api/lh-xxwh/service/customers/getlist?key=";
        //获取前端参数
        //String parameter = request.getParameter("filter_LIKES_clientName");
        //String api_result = httpRequest(requestUrl,parameter);
        String api_result = httpGo.getRequest(requestUrl,syncClientName);
        JSONObject obj = JSONObject.parseObject(api_result);
        JSONArray jsonArray = null;
        if(200==obj.getInteger("code")) {
            jsonArray=obj.getJSONArray("data");

        }
        if(null !=jsonArray) {
            return jsonArray.getString(0);
        }
//    	getDataFromJson(api_result);
        return null;
    }

}
