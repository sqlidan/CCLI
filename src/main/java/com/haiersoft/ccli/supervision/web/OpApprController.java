package com.haiersoft.ccli.supervision.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.haiersoft.ccli.base.service.HscodeService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.supervision.entity.ApprHead;
import com.haiersoft.ccli.supervision.entity.ApprInfo;
import com.haiersoft.ccli.supervision.entity.OpApprHead;
import com.haiersoft.ccli.supervision.entity.OpApprInfo;
import com.haiersoft.ccli.supervision.service.*;
import com.haiersoft.ccli.system.entity.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;

/**
 * 分类监管 手工申请单controller
 *
 * @author
 */

@Controller
@RequestMapping("supervision/opAppr")
public class OpApprController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(OpApprController.class);

    @Autowired
    private OpApprHeadService opApprHeadService;

    @Autowired
    private OpApprInfoService opApprInfoService;

    @Autowired
    HscodeService hscodeService;
    @Autowired
    GetKeyService getKeyService;
    @Autowired
    FljgWsClient fljgWsClient;

    /**
     * 默认页面
     */
    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "fljg/opApprList";
    }

    @RequestMapping(value = "json", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request) {
        Page<OpApprHead> page = getPage(request);
        page.orderBy("createTime").order("desc");
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        page = opApprHeadService.search(page, filters);
        return getEasyUIData(page);
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createForm(Model model) {
        return "fljg/opApprForm";
    }

    /**
     * 添加申请单头
     *
     * @param opApprHead
     * @param model
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public String create(@Valid OpApprHead opApprHead, Model model) {
        //本地状态为1，保存
        opApprHead.setLocalStatus("1");
        opApprHead.setCreateTime(new Date());
        opApprHeadService.save(opApprHead);
        return "success";
    }


    // 申请单申报
    @Transactional
    @RequestMapping(value = "apply/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String applyData(HttpServletRequest request, @PathVariable("id") String id) throws IOException, ServiceException {

        OpApprHead opApprHead = opApprHeadService.find("id", id);
        Map<String, Object> apprHeadMap = this.buildApprHead(opApprHead);
        Map<String, Object> apprListsMap = new HashMap<>();
        List<OpApprInfo> infolist = opApprInfoService.getListByHeadId(id);

		List<Map<String, Object>> ApprLists = this.buildApprLists(opApprHead,infolist);
        //拼装Json
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("SaveType", "1");//这里0为暂存，咱们不存在暂存申请，直接发送1
        map.put("DeclType", "1");
        map.put("ApprLists", ApprLists);
        map.put("ApprHead", apprHeadMap);


        String jsonString =JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);
        System.out.println(jsonString);
        logger.error(jsonString);
        // 1 获得key
        String tickId = getKeyService.builder();
        System.out.println(tickId);

        // 2 调用接口
        //设置接口名
        String serviceName = "ApprSave";
        // 调用 申请单保存
        String result = fljgWsClient.getResult(jsonString, tickId, serviceName);
        System.out.println("ApprSave result: " + result);
        logger.error(">>>>>>>>>>>>>>>>>调用申请单申报result： "+result);
        /*
         * //测试返回字符串 String tempRes = "{\r\n" + "    \"state\":1,\r\n" +
         * "    \"message\":\"\",\r\n" + "    \"data\":\"A42302000000000001\",\r\n" +
         * "    \"CheckInfos\":null\r\n" + "}";
         */

        JSONObject jsonObject = JSON.parseObject(result);
        String state = jsonObject.getString("state");

        if(state.equals("1")) {
            String data = jsonObject.getString("data");
            opApprHeadService.updateArrpIdById(data,"1" ,id);
            return "success";
        }else {
            String CheckInfos = jsonObject.getString("CheckInfos");
            return CheckInfos;

        }
    }

	private Map<String, Object> buildApprHead(OpApprHead opApprHead) {
        Map<String, Object> apprHeadMap = new HashMap<>();
        apprHeadMap.put("ApprId", opApprHead.getApprId() != null ? opApprHead.getApprId() : null);
        apprHeadMap.put("LinkId", opApprHead.getLinkId()!= null ? opApprHead.getLinkId() : null);
        apprHeadMap.put("OrderNum", opApprHead.getOrderNum()!= null ? opApprHead.getOrderNum() : null);
        apprHeadMap.put("ApprType", opApprHead.getApprType()!= null ? opApprHead.getApprType() : null);
        apprHeadMap.put("IoType", opApprHead.getIoType()!= null ? opApprHead.getIoType() : null);
        apprHeadMap.put("BondInvtNo", opApprHead.getBondInvtNo()!= null ? opApprHead.getBondInvtNo() : null);
        //件数
        apprHeadMap.put("PackNo", opApprInfoService.sumQtyByHeadId(opApprHead.getId()));
        //重量
        apprHeadMap.put("GrossWt", opApprInfoService.sumWeightByHeadId(opApprHead.getId()));

        apprHeadMap.put("CustomsCode", opApprHead.getCustomsCode()!= null ? opApprHead.getCustomsCode() : null);
        apprHeadMap.put("EmsNo", opApprHead.getEmsNo()!= null ? opApprHead.getEmsNo() : null);
        apprHeadMap.put("GNo", opApprHead.getgNo()!= null ? opApprHead.getgNo() : null);
        apprHeadMap.put("DNote", opApprHead.getdNote()!= null ? opApprHead.getdNote() : null);

        apprHeadMap.put("ItemNum", opApprHead.getItemNum()!= null ? opApprHead.getItemNum() : null);
        apprHeadMap.put("InputEr", opApprHead.getInputEr()!= null ? opApprHead.getInputEr() : null);
        apprHeadMap.put("InputDate", opApprHead.getInputDate()!= null ? opApprHead.getInputDate() : null);

        apprHeadMap.put("DDate", opApprHead.getdDate()!= null ? opApprHead.getdDate() : null);
        apprHeadMap.put("ApproveDate", opApprHead.getApproveDate()!= null ? opApprHead.getApproveDate() : null);
        apprHeadMap.put("ApproveNote", opApprHead.getApproveNote()!= null ? opApprHead.getApproveNote() : null);

        apprHeadMap.put("TradeCode", opApprHead.getTradeCode()!= null ? opApprHead.getTradeCode() : null);
        apprHeadMap.put("TradeName", opApprHead.getTradeName()!= null ? opApprHead.getTradeName() : null);

        apprHeadMap.put("OwnerCode", opApprHead.getOwnerCode()!= null ? opApprHead.getOwnerCode() : null);
        apprHeadMap.put("OwnerName", opApprHead.getOwnerName()!= null ? opApprHead.getOwnerName() : null);

        apprHeadMap.put("AgentCode", opApprHead.getAgentCode()!= null ? opApprHead.getAgentCode() : null);
        apprHeadMap.put("AgentName", opApprHead.getAgentName()!= null ? opApprHead.getAgentName() : null);
        apprHeadMap.put("LoadingFlag", opApprHead.getLoadingFlag()!= null ? opApprHead.getLoadingFlag() : null);

        apprHeadMap.put("DeclType", opApprHead.getDeclType()!= null ? opApprHead.getDeclType() : null);
        apprHeadMap.put("Status", opApprHead.getStatus()!= null ? opApprHead.getStatus() : null);
        apprHeadMap.put("PassStatus", opApprHead.getPassStatus()!= null ? opApprHead.getPassStatus() : null);
        apprHeadMap.put("CREATETIME", opApprHead.getCreateTime()!= null ? opApprHead.getCreateTime() : null);
        return apprHeadMap;
    }

	private List<Map<String, Object>> buildApprLists(OpApprHead opApprHead, List<OpApprInfo> infolist) {

		List<Map<String, Object>> list= new ArrayList<>();
		for(OpApprInfo info : infolist){
			Map<String, Object> map = new HashMap<>(16);
			map.put("ApprGNo",info.getApprGNo());
			map.put("ApprId",opApprHead.getApprId());
			map.put("BisInfoId",null);
			map.put("CodeTs",info.getCodeTs());
			map.put("ctnNum",info.getCtnNum());
			map.put("Curr",null);
			map.put("GModel",null);
			map.put("GName",info.getgName());
			map.put("GNo",info.getgNo());
			map.put("Gqty",info.getgQty());
			map.put("GrossWt",info.getGrossWt());
			map.put("GUnit",info.getgUnit());

			map.put("ioType",opApprHead.getIoType());
			map.put("LinkId",opApprHead.getLinkId());
			map.put("Qty1",info.getgQty());
			map.put("TotalSum",null);
			map.put("Unit1",info.getgUnit());
			list.add(map);
		}

		return list;
	}

    //删除申请
    @Transactional
    @RequestMapping(value = "/del/{ids}")
    @ResponseBody
    public String deleteAppr(@PathVariable("ids") List<String> ids) throws RemoteException, ServiceException {
        List<OpApprHead> toDelRemote = new ArrayList<OpApprHead>();
        List<OpApprHead> toDelLocal = new ArrayList<OpApprHead>();
        //检查本地状态,先整理出需要接口删除的申请单
        for(String id : ids) {
            OpApprHead opApprHead = opApprHeadService.find("id", id);
            if((null ==opApprHead.getApprId()) || ("".equals(opApprHead.getApprId()))) {
                toDelLocal.add(opApprHead);
            }else {
                toDelRemote.add(opApprHead);
            }

        }

        //需要调用接口删除的appr
        if(toDelRemote.size() != 0) {
            // 1 获得key
            String tickId = getKeyService.builder();
            System.out.println(tickId);
            if((null ==tickId) || ("".equals(tickId)))
            {
                return "没有获得tickId,请重新操作";
            }
            // 2 调用接口
            //设置接口名
            String serviceName = "ApprDelete";
            for(OpApprHead app:toDelRemote) {
                String apprdelete="{\"KeyModel\":{\"ApprId\":\""+app.getApprId()+"\"}}";
                logger.error(">>>>>>>>>>>>>>>>>调用申请单删除json： "+apprdelete);
                String result = fljgWsClient.getResult(apprdelete, tickId, serviceName);
                logger.error(">>>>>>>>>>>>>>>>>调用申请单申报result： "+result);
                System.out.println("result: " + result);
                JSONObject jsonObject = JSON.parseObject(result);
                String state = jsonObject.getString("state");
                //如果接口state返回1为删除成功
                if(state.equals("1")) {
                    //删除本地数据库中的记录
                    opApprHeadService.deleteById(app.getId());
                    opApprInfoService.deleteByHeadId(app.getId());
                    return "success";
                }else {

                    return "接口错误";
                }
            }

        }

        //不需要调用接口删除的 list
        for(OpApprHead app:toDelLocal) {
            opApprHeadService.deleteById(app.getId());
            opApprInfoService.deleteByHeadId(app.getId());
        }

        return "success";

    }

    //作废申请
    @Transactional
    @RequestMapping(value = "/cancel/{ids}")
    @ResponseBody
    public String cancelAppr(@PathVariable("ids") List<String> ids) throws RemoteException, ServiceException {


        return "success";
    }


}
