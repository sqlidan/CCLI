package com.haiersoft.ccli.cost.web;

import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.cost.service.BaseExpenseCategoryDetailService;
import com.haiersoft.ccli.cost.service.BisPayMidGroupServeice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("cost/baseExpense")
public class BaseExpenseCategoryDetailContorller extends BaseController {

    @Autowired
    private BaseExpenseCategoryDetailService baseExpenseCategoryDetailService;
    @Autowired
    private BisPayMidGroupServeice bisPayMidGroupServeice;
    @RequestMapping("testDemo")
    public  String  testDemo(){
        String detailCode = "";
        String detailcodename = "";
        Map<String, Object> mapBase = baseExpenseCategoryDetailService.getCodeByFeeCode("hgyh");
        Object detail = mapBase.get("DETAIL_CODE");
        Object detailname = mapBase.get("DETAIL_CODE_NAME");
        if(StringUtils.isEmpty(detail)){
            System.out.println("111");
        }
        detailCode = detail.toString();
        detailcodename = detailname.toString();
        String costClassifyCode = bisPayMidGroupServeice.sendParamReturncostClassifyCode(detailcodename);
        if(StringUtils.isEmpty(costClassifyCode)){
            System.out.println("2222");
        }
        return costClassifyCode;
    }

}
