package com.haiersoft.ccli.wms.web;

import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.common.utils.AESUtil2;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.HttpGo;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.wms.entity.BaseGoCardBulk;
import com.haiersoft.ccli.wms.entity.BaseGoCardContainer;
import com.haiersoft.ccli.wms.service.GoCardBulkService;
import com.haiersoft.ccli.wms.service.GoCardContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * GoCardBulkContorller
 *
 * @author PYL
 * @date 2016年6月28日
 */
@Controller
public class GoCardApplyContorller extends BaseController {

    @Autowired
    private GoCardBulkService goCardBulkService;


    @Autowired
    private GoCardContainerService goCardContainerService;

    @Autowired
    HttpGo httpGo;

    /**
     * @author
     * @Description: 散货发送出门证
     * @date 2022年5月
     */
    @RequestMapping(value = "gocard/bulk/apply/{gocard}")
    @ResponseBody
    public String apply(@PathVariable("gocard") String gocard) {
        System.out.println("abdec fgh:::: " + gocard);
        BaseGoCardBulk goCardBulk = goCardBulkService.get(gocard);
        String requestStr = this.requestBuilder(goCardBulk);
        String requestStrWithEncrypt = AESUtil2.encrypt(requestStr);
        String result = httpGo.sendRequest(URL, requestStrWithEncrypt);
        String resultWithDecrypt = AESUtil2.decrypt(result);
        this.dealWithResult(gocard,resultWithDecrypt);
        System.out.println("resultWithDecrypt:::" + resultWithDecrypt);
        return "success";
    }


    /**
     * @author
     * @Description: 集装箱发送出门证
     * @date 2022年5月
     */
    @RequestMapping(value = "gocard/container/apply/{gocard}")
    @ResponseBody
    public String containerApply(@PathVariable("gocard") String gocard) {
        System.out.println("abdec fgh:::: " + gocard);
        BaseGoCardContainer goCardContainer = goCardContainerService.get(gocard);
        String requestStr = this.containerRequestBuilder(goCardContainer);
        String requestStrWithEncrypt = AESUtil2.encrypt(requestStr);
        String result = httpGo.sendRequest(URL, requestStrWithEncrypt);
        String resultWithDecrypt = AESUtil2.decrypt(result);
        this.containerDealWithResult(gocard,resultWithDecrypt);
        System.out.println("resultWithDecrypt:::" + resultWithDecrypt);
        return "success";
    }

    /**
     * 散货处理返回值
     * @param gocard
     * @param resultWithDecrypt
     */
    private void dealWithResult(String gocard,String resultWithDecrypt) {
    	Map<String,Object> map = JSON.parseObject(resultWithDecrypt,HashMap.class);
    	if(!map.get("code").equals("200")){
    		return;
		}
    	//code 为 200 时 正常处理
		Map<String,String> returndata = (Map<String,String>)map.get("returndata");
		BaseGoCardBulk goCardBulk = goCardBulkService.get(gocard);
		goCardBulk.setGoCardStatus("1");
		goCardBulk.setOnlyMark(returndata.get("onlyMark"));
		goCardBulkService.update(goCardBulk);

	}

    /**
     * 集装箱处理返回值
     * @param gocard
     * @param resultWithDecrypt
     */
    private void containerDealWithResult(String gocard, String resultWithDecrypt) {
        Map<String,Object> map = JSON.parseObject(resultWithDecrypt,HashMap.class);
        if(!map.get("code").equals("200")){
            return;
        }
        //code 为 200 时 正常处理
        Map<String,String> returndata = (Map<String,String>)map.get("returndata");
        BaseGoCardContainer goCardContainer = goCardContainerService.get(gocard);
        goCardContainer.setGoCardStatus("1");
        goCardContainer.setOnlyMark(returndata.get("onlyMark"));
        goCardContainerService.update(goCardContainer);
    }

	private String URL = "http://lh-electroncard.sdlandsea.net/receptionData/addElectronicCard";
    private static String USERID = "YZH";
    private static String PSWD = "bx7bz2dBpK5wUy85xTuaNA==";
    private static String DIRECTION_TEST = "exitCard";
    private static String DIRECTION_PROD = "exitCardProd";

    private String requestBuilder(BaseGoCardBulk goCardBulk) {

        String date = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> map = new HashMap<>();
        map.put("userid", USERID);
        map.put("pswd", PSWD);
        map.put("newtime", date);
        map.put("direction", DIRECTION_PROD);

        Map<String, String> data = new HashMap<>();
        data.put("licensePlateNumber", goCardBulk.getCarNum());
        data.put("cargoCode", "02");
        data.put("cargoName", "散杂货");
        data.put("companyCode", USERID);
        data.put("companyName", "怡之航冷链中心");
        data.put("workCompleteTime", date);
        map.put("data", data);

        return JSON.toJSONString(map);
    }

    private String containerRequestBuilder(BaseGoCardContainer goCardContainer) {
        String date = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> map = new HashMap<>();
        map.put("userid", USERID);
        map.put("pswd", PSWD);
        map.put("newtime", date);
        map.put("direction", DIRECTION_PROD);

        Map<String, String> data = new HashMap<>();
        data.put("licensePlateNumber", goCardContainer.getCarNum());
        data.put("cargoCode", "01");
        data.put("cargoName", "集装箱");
        data.put("companyCode", USERID);
        data.put("companyName", "怡之航冷链中心");
        data.put("workCompleteTime", date);
        map.put("data", data);

        return JSON.toJSONString(map);

    }


}
