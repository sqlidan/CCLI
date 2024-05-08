package com.haiersoft.ccli.api.web;

import com.haiersoft.ccli.api.entity.ResponseVo;
import com.haiersoft.ccli.api.service.FreightInquiryService;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * 货物查询
 */
@Controller
public class FreightInquiryController {

    @Autowired
    private FreightInquiryService freightInquiryService;

    /**
     * 	货物查询
     *
     * @param  CustCode 客户代码
     * @param CustName 客户名称
     * @return
     */
    @RequestMapping("/staple/goodQuery")
    @ResponseBody
    public ResponseVo queryApply(@RequestBody Map<String, String> params) {
        List<Map<String,Object>> list = freightInquiryService.findTrayInfoByStock(params);
        if(list.size()==0){
            ResponseVo.errDz(null,"查询失败，请检查客户名称和客户号是否正确");
        }
        return ResponseVo.resDz(list,"操作成功");
    }

    /**
     * 	质押监管校验接口
     *
     * @param  CustCode 客户代码
     * @param CustName 客户名称
     * @param cargoInfo 货物信息(List)
     * @param operaCode 本次操作唯一ID
     * @return
     */
    @RequestMapping("/staple/superviseInspect")
    @ResponseBody
    @Transactional
    public ResponseVo superviseInspect(@RequestBody Map<String, Object> params) {
        Map<String,String> res = freightInquiryService.superviseInspect(params);
        if(!"success".equals(res.get("success"))){
            return ResponseVo.check(false,res.get("message"),res.get("operaCode"));
        }
        return ResponseVo.check(true,"校验通过，可操作冻结",res.get("operaCode"));
    }

    /**
     * 	解押校验接口
     *
     * @param  CustCode 客户代码
     * @param CustName 客户名称
     * @param cargoInfo 货物信息(List)
     * @param operaCode 本次操作唯一ID
     * @return
     */
    @RequestMapping("staple/releaseCustody")
    @ResponseBody
    @Transactional
    public ResponseVo releaseCustody(@RequestBody Map<String, Object> params) {
        Map<String,String> res = freightInquiryService.releaseCustody(params);
        if(!"true".equals(res.get("success"))){
            return ResponseVo.check(false,res.get("message"),res.get("operaCode"));
        }
        return ResponseVo.check(true,"校验通过，可操作解押",res.get("operaCode"));
    }

//    /**
//     * 	解押生效接口
//     *
//     * @param  CustCode 客户代码
//     * @param CustName 客户名称
//     * @param cargoInfo 货物信息(List)
//     * @param operaCode 本次操作唯一ID
//     * @param firstOperaCode 校验操作唯一ID
//     * @return
//     */
//    @RequestMapping("/staple/effectivenessRelease")
//    @ResponseBody
//    @Transactional
//    public ResponseVo effectivenessRelease(@RequestBody Map<String, Object> params) {
//        Map<String,String> res = freightInquiryService.effectivenessRelease(params);
//        if(!"true".equals(res.get("success"))){
//            return ResponseVo.check(false,res.get("message"));
//        }
//        return ResponseVo.check(true,"解押成功");
//    }

//    /**
//     * 	质押监管生效接口
//     *
//     * @param  CustCode 客户代码
//     * @param CustName 客户名称
//     * @param cargoInfo 货物信息(List)
//     * @param operaCode 本次操作唯一ID
//     * @param firstOperaCode 校验操作唯一ID
//     * @return
//     */
//    @RequestMapping("/staple/effectivenessPledge")
//    @ResponseBody
//    @Transactional
//    public ResponseVo effectivenessPledge(@RequestBody Map<String, Object> params) {
//        Map<String,String> res = freightInquiryService.effectivenessPledge(params);
//        if(!"true".equals(res.get("success"))){
//            return ResponseVo.check(false,res.get("message"));
//        }
//        return ResponseVo.check(true,"质押成功");
//    }

    /**
     * 	第二次请求校验接口
     * @param  operType 操作类型(解押/质押/货转)
     * @param  CustCode 客户代码
     * @param CustName 客户名称
     * @param cargoInfo 货物信息(List)
     * @param operaCode 本次操作唯一ID
     * @param firstOperaCode 校验操作唯一ID
     * @return
     */
    @RequestMapping("/staple/operaRequest")
    @ResponseBody
    public ResponseVo operaRequest(@RequestBody Map<String, Object> params) {
        String result = freightInquiryService.operaRequest(params);
        if(!"".equals(result)){
         return ResponseVo.error(result);
        }
        return ResponseVo.pledgeOk();
    }

    /**
     * 	测试接口连通
     *
     * @return
     */
    @RequestMapping("/staple/test")
    @ResponseBody
    public ResponseVo test(@RequestBody Map<String, String> params) {
        List<Map<String,Object>> list = new ArrayList<>();
        if(list.size()==0){
            ResponseVo.errDz(null,"查询失败");
        }
        return ResponseVo.resDz(list,"操作成功");
    }

}
